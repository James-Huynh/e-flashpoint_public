package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import actions.Action;
import actions.ActionList;
import chat.ChatMsgEntity;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.GameSaver;
import game.GameState;
import lobby.Lobby;
import managers.GameManager;
import tile.Tile;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;

public class ServerManager {
	
	private HashMap<String,String> accounts;
	private HashMap<Integer, Player> onlinePlayers;
	private ArrayList<Lobby> currentLobbies;
	private ArrayList<GameState> savedGames;
	private ArrayList<String> savedNameGames;
	private static String defaulGamesPath = "savedGames/";
	private static String currentPath = System.getProperty("user.dir");
	private boolean firstTurn; //should be in game state?
	
	private Lobby activeLobby;
	private GameState gameState;
	private GameManager gameManager;
	
	public ArrayList<Integer> randomBoards;
	
	private int placedFF = 0;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	public ServerManager() {
		onlinePlayers = new HashMap<Integer, Player>();
		accounts = new HashMap<String,String>();
		currentLobbies = new ArrayList<Lobby>();
		accounts.put("Zaid", "apple");
		accounts.put("me", "aa");
		savedGames = new ArrayList<GameState>();
		savedNameGames = new ArrayList<String>();
		setSavedGames();
		randomBoards = new ArrayList<Integer>();
		randomBoards.add(6);
	}
	
	public void createPlayer(String name, String password, Integer ID) {
		onlinePlayers.put(ID, new Player(name, password, ID));
		System.out.println(this.onlinePlayers.size());
	}
	
	public void createGame() {
//		if(gameState == null) {
			for(Player p: activeLobby.getPlayers()) {
				p.reset();
				
			}
			initializeGameManager();
			
			gameManager.setup();
			gameState = gameManager.getGameState();
			gameState.setActiveFireFighterIndex(-1);
			gameManager.setFirstAction(true);
			firstTurn = true;
			if (gameManager.getGameState().getRandomBoard() >= 0) {
				// gameManager.getGameState().setRandomGame(randomBoards.get(randomBoards.size()-1));
				this.randomBoards.add(gameManager.getGameState().getRandomBoard());
				this.gameManager.setRandomBoards(this.randomBoards);
			}
//		}
		
	}
	
	public void initializeGameManager() {
//		gameManager = new GameManager(gameState, activeLobby);
		gameManager = new GameManager(activeLobby);
		gameManager.setRandomBoards(this.randomBoards);
	}
	
	public void generateActions() {
		gameManager.setAllAvailableActions(gameManager.generateAllPossibleActions());
		gameState.updateActionList(gameManager.getAllAvailableActions());
	}

	
//	public void createLobby() {
//		testLobby = new Lobby();
//		testLobby.setName("FUNTIMES");
//		testLobby.addPlayer(onlinePlayers.get(Integer.valueOf(12345)));
//		currentLobbies.add(testLobby);
//	}
	
	public void placeFirefighter(int[] coords, Integer userId) {
		boolean canPlace = false;
		for(Firefighter f: onlinePlayers.get(userId).getFirefighters()) {
			if(f.getCurrentPosition() == null) {
				canPlace = true;
			}
		}
		if(canPlace) {
			gameState.placeFireFighter(onlinePlayers.get(userId).getFirefighter(), gameState.returnTile(coords[0],coords[1]));
			placedFF++;
		}
		if(placedFF == gameState.getFireFighterList().size()) {
			if(!gameState.isExperienced()) {
				gameState.setActiveFireFighterIndex(0);
				generateActions();
			} else {
				for(int i = 0; i < 4; i++) {
					if(gameState.getAmbulances()[i].getCar()) {
						for(int j = 0; j<4; j++) {
							if(gameState.getEngines()[j].getCar()) {
								gameState.setActiveFireFighterIndex(0);
								generateActions();
							}
						}
					}
				}
			}
		}
	}
	
	public void placeVehicle(int direction, Vehicle type) {
		boolean f1 = true;
		if(type.equals(Vehicle.Ambulance)) {
			for(int i = 0; i < 4; i++) {
				if(gameState.getAmbulances()[i].getCar()) {
					f1 = false;
				}
			}
			if(f1) {
				gameState.getAmbulances()[direction].setCar(true);
			}
		}
		else if(type.equals(Vehicle.Engine)) {
			for(int j = 0; j<4; j++) {
				if(gameState.getEngines()[j].getCar()) {
					f1 = false;
				}
			}
			if(f1) {
				gameState.getEngines()[direction].setCar(true);
			}
		}
		
		for(int i = 0; i < 4; i++) {
			if(gameState.getAmbulances()[i].getCar()) {
				for(int j = 0; j<4; j++) {
					if(gameState.getEngines()[j].getCar()) {
						if(placedFF == gameState.getFireFighterList().size()) {
							gameState.setActiveFireFighterIndex(0);
							generateActions();
						}
					}
				}
			}
		}
	}
	
	public void performAction(Action a) {
		a.perform(gameState);
		gameManager.setFirstAction(false);
		generateActions();
	}
	
	public HashMap<String, String> getAccounts(){
		return this.accounts;
	}
	
	public HashMap<Integer, Player> getPlayers(){
		return this.onlinePlayers;
	}
	
	
	public GameState getGameState() {
		return this.gameState;
	}
	public List<ChatMsgEntity> getChatArray(){
		return this.mDataArrays;
	}
	public void addChatArray(ChatMsgEntity chatMsgEntity) {
		mDataArrays.add(chatMsgEntity);
	}
	
	public void setLobby(Lobby newLobby) {
		this.activeLobby = newLobby;
		this.currentLobbies.add(this.activeLobby);
		this.randomBoards = newLobby.getRandomBoards();
	}
	
	public Player getPlayer(Integer inputInteger) {
		return onlinePlayers.get(inputInteger);
	}
	
	public void addPlayerToLobby(Player additionalPlayer) {
		if(this.activeLobby.getPlayers().size() < this.activeLobby.getCapacity()) {
			this.activeLobby.addPlayer(additionalPlayer);
		}
	}
	
	public Lobby getLobby() {
		return this.activeLobby;
	}

	public ArrayList<Lobby> getLobbyList() {
		return this.currentLobbies;
	}

	public void endTurn() {
		Firefighter currentOne = gameState.getPlayingFirefighter();
		//currentOne.endOfTurn(); // cannot be any longer here - bc of dodge
		advanceFireNew();
		System.out.println("endturn over");
		if(gameState.isGameTerminated() || gameState.isGameWon()) {
			//@matekrk! here should be pop-in window so something like gameState.setEndString(gameManager.getAdvEndMessage)
			setFFNextTurn();
			generateActions();
		} 
		else {
			setFFNextTurn();
			generateActions();
		}
		
		
	}
	
	public void setFFNextTurn() {
		gameManager.setFirstAction(true);
		int newIndex = (gameState.getActiveFireFighterIndex() + 1);
		if (gameManager.getGameState().isExperienced()) { //is it really working?
			if (gameManager.getGameState().getPlayingFirefighter().getSpeciality() == Speciality.VETERAN) {
				
			}
			else {
				if (!firstTurn && gameManager.getGameState().getPlayingFirefighter().getCanDodge() && !gameManager.getGameState().getPlayingFirefighter().getUsedAP()) {
					gameManager.getGameState().getPlayingFirefighter().setAP( Math.max(0, gameManager.getGameState().getPlayingFirefighter().getAP()-1) ) ;
				}
			}
		}
		
		if (newIndex >= gameState.getFireFighterList().size()) {
			firstTurn = false;
		}
		gameState.setActiveFireFighterIndex( newIndex%(gameState.getFireFighterList().size()) );
		if(!firstTurn) {
			gameState.getPlayingFirefighter().endOfTurn();
		}
		gameState.vicinity(gameState.getPlayingFirefighter());
	}
	
	public void advanceFire() {
		gameManager.advanceFire(false); //this boolean is saying that it is the initial advanced fire and not one caused by a hotspot flare up
		gameState.setAdvFireString(gameManager.getAdvFireMessage());
	}
	
	public void advanceFireNew() {
		System.out.println("starting advance fire new\n\n\n\n");
		//looper set to false in family, true if the initial tile has a hot spot
		boolean hasHotSpot = true;
		//used to know if the loop is on the first iteration or not
		boolean additionalHotSpot;
		int count = 0;
		gameManager.setAdvFire("");
    	
		//checking if a vet exists in the game
		boolean dodgeCheck = false;
		if(gameState.isExperienced()) {
			for(Firefighter f : gameState.getFireFighterList()) {
				if(f.getSpeciality() == Speciality.VETERAN) {
					dodgeCheck = true;
					System.out.println("deodgecheck" + dodgeCheck);
				}
			}
		}
		
    	//gs.endTurn();
    	while(hasHotSpot) {
    		additionalHotSpot = 0<count;
    		Tile targetTile = gameState.rollForTile();
        	if(gameState.isExperienced()) {
        		hasHotSpot = targetTile.containsHotSpot();
        	} else {
        		hasHotSpot = false;
        	}
        	
        	if(gameManager.advanceFireStart(targetTile, additionalHotSpot) && dodgeCheck) {
        		System.out.println("dodge triggered after start");
        	}
        	
        	if(gameManager.resolveFlashOver() && dodgeCheck) {
        		System.out.println("dodge triggered after flashover");
        	}
        	
        	if(gameState.isExperienced()) {
        		if(gameManager.resolveHazmatExplosions() && dodgeCheck) {
        			System.out.println("dodge triggered after hazmatExplosion");
        		}
        	}
        	
        	if(additionalHotSpot) {
        		if(gameState.getHotSpot()>0) {
    				targetTile.setHotSpot(1);
        			gameState.setHotSpot(gameState.getHotSpot() - 1);
        			gameManager.setAdvFire("hotSpot added to final tile at coords: " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1]  +"\n");
    			}
        	}
        	
        	count++;
        	if(hasHotSpot) {
        		gameManager.setAdvFire("hotSpot triggered another advanceFire \n");
        	}
    	}
    	
    	gameManager.checkKnockDowns();
    	gameManager.placePOI();
    	gameManager.clearExteriorFire();
    	
    	int wallCheck = gameState.getDamageCounter();//should this running the same time with the main process? @Eric
    	int victimCheck = gameState.getLostVictimsList().size();
    	int savedVictimCheck = gameState.getSavedVictimsList().size();
    	
    	
    	if(wallCheck >= 24 || victimCheck >= 4) {
    		gameState.terminateGame();
    	} else if(savedVictimCheck >= 7) {
    		gameState.winGame();
    	}
    	gameState.setAdvFireString(gameManager.getAdvFireMessage());
	}
	
	public boolean askRelevantFirefighters(Vehicle type) {
		return gameState.createFFToAsk(type);
	}
	
	public boolean hasEveryoneResponded() {
		return gameState.hasEveryoneResponded();
	}
	
	public void updateResponse(boolean b, int i) {
		gameState.setRideOption(b, i);
	}
	
	public boolean isEndTurnerInvolved(ArrayList<Firefighter> fList) {
		boolean flag = false;
		for(int i=0; i<fList.size();i++) {
			flag = gameState.getRideMapper().get(fList.get(i))[0];
			if(flag) {
				break;
			}
		}
		return flag;
	}
	
	
	public void saveGame() throws IOException {
		//@eric take the current game state and save it here
		//@junha, I move part of ur code into the Class GameSaver
		GameSaver g=new GameSaver();
		//g.start(gameState);
		g.saveObjectByObjectOutput(gameState);
//		try {
//			int savedGameNumber = new File("C:\\Users\\junha\\git\\f2018-group11\\FlashPoint\\src\\savedGame").listFiles().length; //check how many games are already saved
//			FileOutputStream f = new FileOutputStream(new File("savedGame" + (savedGameNumber++) + ".txt")); //save it as "savedGame#.txt"
//			
//			ObjectOutputStream o = new ObjectOutputStream(f);
//	
//			o.writeObject(gameState);
//			
//			o.close();
//			f.close();
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	//JUNHA : theoretically this should read our savedGame#.txt file and read our object 
	public void loadGame(int gameNumber) {
		//when loading a game, change the myowner and my firefighter also change the firefighter name;
		try {
			FileInputStream fi = new FileInputStream(new File(defaulGamesPath + gameNumber + ".txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			initializeGameManager();
			this.gameState = (GameState) oi.readObject();
			this.gameManager.getGameState().updateGameStateFromObject(gameState);
			this.gameState = gameManager.getGameState();
			this.gameState.setListOfPlayers(this.activeLobby.getPlayers());
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	//@matekrk
	public void saveGameMat(GameState gs, String name) {
    	try {
			FileOutputStream fo = new FileOutputStream(new File(defaulGamesPath + name + ".txt"));
			ObjectOutputStream oo = new ObjectOutputStream(fo);

			// Write object to file
			oo.writeObject(gs);

			oo.close();
			System.out.println(oo.toString());
			fo.close();


		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
    }
    public GameState loadGameMat(int index) {
    	//for (int ind=0; ind<this.savedGames.size(); ind++) {
    	
    	/* THAT WAS EARLIER
    	
    		GameState gs = savedGames.get(index);
    		initializeGameManager();
			this.gameManager.getGameState().updateGameStateFromObject(gs);			
			this.gameState = gameManager.getGameState();
			this.gameState.setListOfPlayers(this.activeLobby.getPlayers());
			generateActions();
			return gs; 
			
		*/
    	
    	String folderPath = defaulGamesPath;
    	File folder = new File(folderPath);
    	File[] listOfFiles = folder.listFiles();
    	GameState gs1 = GameState.getInstance();
    	
    	
    	
    		try {
        		FileInputStream fi = new FileInputStream(new File(defaulGamesPath + listOfFiles[index].getName()));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			// Read objects
    			GameState gs = (GameState) oi.readObject();
    			
    			gs1.updateGameStateFromObject(gs);
    			this.gameState = gs1;
    			System.out.println(gs1.getDamageCounter());
    			
    			initializeGameManager();
    	    	this.gameManager.getGameState().updateGameStateFromObject(gs1);

    			oi.close();
    			fi.close();
    			
    		} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				System.out.println("Error initializing stream");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
    	return gs1;
			
    	//}
    	/*
    	try {
    		FileInputStream fi = new FileInputStream(new File(defaulGamesPath + name + ".txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			// Read objects
			GameState gs = (GameState) oi.readObject();
			GameState gs1 = GameState.getInstance();
			gs1.updateGameStateFromObject(gs);
			System.out.println(gs1.getDamageCounter());

			oi.close();
			fi.close();
			
			//this.savedGames.add(gs1);
			
			initializeGameManager();
			this.gameManager.getGameState().updateGameStateFromObject(gs);
			this.gameState = gameManager.getGameState();
			this.gameState.setListOfPlayers(this.activeLobby.getPlayers());
				
			return gs1; //if not void
			
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				System.out.println("Error initializing stream");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
    	return null;
    	*/
    }
    
    public GameState loadGameMatOver(String name) {
    	try {
    		FileInputStream fi = new FileInputStream(new File(defaulGamesPath + name + ".txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			// Read objects
			GameState gs = (GameState) oi.readObject();
			GameState gs1 = GameState.getInstance();
			gs1.updateGameStateFromObject(gs);
			System.out.println(gs1.getDamageCounter());

			oi.close();
			fi.close();
			
			// this.savedGames.add(gs1);
			this.savedNameGames.add(name+".txt");
			
			return gs1;
			
    	} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	return null;
    }
    
    public void setSavedGames() {
    	this.savedGames.clear();
    	this.savedNameGames.clear();
    	String folderPath = defaulGamesPath;
    	File folder = new File(folderPath);
    	File[] listOfFiles = folder.listFiles();
    	if (listOfFiles.length == 0) {
    		return;
    	}
    	for (int len=0; len < listOfFiles.length; len++) {
    		//try {
    			File f = listOfFiles[len];
    			this.savedNameGames.add(f.getName());
    			
    			/*
        		FileInputStream fi = new FileInputStream(new File(defaulGamesPath + f.getName()));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			// Read objects
    			GameState gs = (GameState) oi.readObject();
    			GameState gs1 = GameState.getInstance();
    			gs1.updateGameStateFromObject(gs);
    			System.out.println(gs1.getDamageCounter());
    			System.out.println("im here:" + gs1.getSavedVictims());
    			oi.close();
    			fi.close();
    			
    			this.savedGames.add(gs1);
    			
    			
    		} catch (FileNotFoundException e) {
    			System.out.println("File not found");
    		} catch (IOException e) {
    			System.out.println("Error initializing stream");
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		}
    		*/
    		
    			
    		//GameState gs = GameState.getInstance();
    		//gs = loadGameMatOver(f.getName());
//    		System.out.println("im here:" + gs.getSavedVictims());
//    		System.out.println("im here:" + gs.getListOfPlayers().get(0).getUserName());
//    		this.savedGames.add(gs);
    	}
    }
    
    public ArrayList<GameState> getSavedGames(){
    	return this.savedGames;
    }
	
    public int getSavedGamesNumber() {
    	return this.savedGames.size();
    }
    
    public void removeSavedGame(int index) {
    	//GameState gs = GameState.getInstance();
    	this.savedGames.remove(index);
    }

    public ArrayList<String> getSavedNameGames(){
    	return savedNameGames;
    }
    
    public void setSavedNameGames(ArrayList<String> savedNameGames) {
    	this.savedNameGames = savedNameGames;
    }
    
    
	public void setSpeciality(User person, Speciality desiredSpeciality) {
		if(gameState.getFreeSpecialities().contains(desiredSpeciality)) {
			Player player = onlinePlayers.get(person.getId());
			if(gameState.getFireFighterList().get(person.getDesiredFirefighter()).getSpeciality()!=null) {
				gameState.addFreedSpeciality(gameState.getFireFighterList().get(person.getDesiredFirefighter()).getSpeciality());
				gameState.getFireFighterList().get(person.getDesiredFirefighter()).setSpeciality(desiredSpeciality);
				gameState.removeSelectedSpeciality(desiredSpeciality);
			}else {
				gameState.getFireFighterList().get(person.getDesiredFirefighter()).setSpeciality(desiredSpeciality);
				gameState.removeSelectedSpeciality(desiredSpeciality);
			}
			
//			for(Player p: gameState.getListOfPlayers()) {
//				if(p.getUserName().equals(person.getName())) {
//					if(gameState.getFireFighterList().get(count).getSpeciality()!=null) {
//						gameState.addFreedSpeciality(gameState.getFireFighterList().get(count).getSpeciality());
//						gameState.getFireFighterList().get(count).setSpeciality(desiredSpeciality);
//						gameState.removeSelectedSpeciality(desiredSpeciality);
//					}else {
//						gameState.getFireFighterList().get(count).setSpeciality(desiredSpeciality);
//						gameState.removeSelectedSpeciality(desiredSpeciality);
//					}
//					
//					count++;
//				}
//			}
		}
		
	}

	public void setSpecialitySelecting(Boolean b) {
		gameState.setSpecialitySelecting(b);
	}
	
	public void setDice() {
		gameState.setProposedDices();
	}
	
	public void setDice(int i) {
		if (i==0) {
			gameState.setProposedDicesKeepRed();
		}
		else {
			gameState.setProposedDicesKeepBlack();
		}
	}
	
	//for @matekrk
		public GameManager getGameManager() {
			return this.gameManager;
		}

		public void setFirefighter(User person) {
			token.Colour ff = person.getDesiredFirefighterColour();
			int desiredFF = 0;
			System.out.println("we are in hrerererer" + ff.toString());
			if(gameState.getFreeFirefighters().size() != 0) {
				Player player = onlinePlayers.get(person.getId());
				for(int i = 0; i<gameState.getFreeFirefighters().size(); i++) {
					if(gameState.getFreeFirefighters().get(i).getColour() == ff) {
						desiredFF = i;
					}
				}
				
				
				
				if(player.getFirefighters().size() != 0) {
//					if(gameState.getListOfPlayers().size() == gameState.getFireFighterList().size()) {
//						gameState.getFreeFirefighters().add(player.getFirefighter());
//						player.setFirefighter(gameState.getFreeFirefighters().get(desiredFF));
//						gameState.getFreeFirefighters().get(desiredFF).setPlayer(player);
//						gameState.getFreeFirefighters().remove(desiredFF);
//					}
					boolean selectAgain = true;
					for(Player p: gameState.getListOfPlayers()) {
						if(p.getFirefighters().size() == 0) {
							selectAgain = false; 
						}
					}
					boolean replace = false;
					if(gameState.getListOfPlayers().size() == gameState.getFireFighterList().size()) {
						replace = true;
					}
					
					if(selectAgain) {
						player.setFirefighter(gameState.getFreeFirefighters().get(desiredFF));
						gameState.getFreeFirefighters().get(desiredFF).setPlayer(player);
						gameState.getFreeFirefighters().remove(desiredFF);
					} else {
						if(replace) {
							gameState.getFreeFirefighters().add(player.getFirefighters().remove(0));
							player.setFirefighter(gameState.getFreeFirefighters().get(desiredFF));
							gameState.getFreeFirefighters().get(desiredFF).setPlayer(player);
							gameState.getFreeFirefighters().remove(desiredFF);
						}
					}
					
					
				} else {
					System.out.println("were here");
					player.setFirefighter(gameState.getFreeFirefighters().get(desiredFF));
					System.out.println(player.getFirefighter().getOwner().getUserName());
					gameState.getFreeFirefighters().get(desiredFF).setPlayer(player);
					System.out.println(gameState.getFreeFirefighters().get(desiredFF).getOwner().getUserName());
					gameState.getFreeFirefighters().remove(desiredFF);
					System.out.println(gameState.getFreeFirefighters().size());
					
					
				}
			} else System.out.println("we should not be here");
		}

		public void resetHashMap() {
			gameState.resetHashMap();
			
		}

		public boolean generateDodgeActions() {
			return gameManager.generateDodgeActions();
			
			
		}
		
		public void updateDodgeRespone(Action dodgeAction, int myFFIndex) {
			Firefighter inturn = gameState.getPlayingFirefighter();
			gameState.setPlayingFirefighter(gameState.getFireFighterList().get(myFFIndex));
			if(dodgeAction != null) {
				dodgeAction.perform(gameState);
			}
			gameState.setPlayingFirefighter(inturn);
			gameManager.setDodgeResponseChecker(myFFIndex);
			
		}

		public boolean hasEveryoneDodged() {
			boolean flag = gameManager.hasEveryoneDodged();
			if(flag) {
				gameState.setIsDodging(false);
			}
			return flag;
		}

		public void resetForNewGame() {
			this.placedFF = 0;
			currentLobbies.remove(activeLobby); //we can only have one active lobby in our game
			this.firstTurn = true;
			this.activeLobby = null;
			this.gameState = null;
			this.gameManager = null;
			
			
		}
		public void updateLobbyForLoadGame(int savedIndex) {
			
			this.gameState = loadGameMat(savedIndex);
			
			this.activeLobby.setCapacity( this.gameState.getFireFighterList().size() ); 
			
			if(this.gameState.getRandomBoard() == -1) {
				this.activeLobby.setBoard("Standard Board");
			}else {
				this.activeLobby.setBoard("Random Board");
			}
			
			this.activeLobby.setName(this.gameState.getSavedGameName());
			
//			this.activeLobby.setDifficulty(getSavedGames().get(savedIndex).getDifficulty());
			
			if(this.gameState.isExperienced()) {
				this.activeLobby.setMode("Experienced");
				this.activeLobby.setDifficulty(this.gameState.getExperiencedModeMode());
			}else {
				this.activeLobby.setMode("Family");
			}
		}

		public void setGameContinue() {
			gameState.continueGame();
			
		}
	
}

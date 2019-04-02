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
	private static String defaulGamesPath = "savedGames/";
	private static String currentPath = System.getProperty("user.dir");
	
	private Lobby activeLobby;
	private GameState gameState;
	private GameManager gameManager;
	
	
	private int placedFF = 0;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	public ServerManager() {
		onlinePlayers = new HashMap<Integer, Player>();
		accounts = new HashMap<String,String>();
		currentLobbies = new ArrayList<Lobby>();
		accounts.put("Zaid", "apple");
		accounts.put("me", "aa");
		savedGames = new ArrayList<GameState>();
		setSavedGames();
	}
	
	public void createPlayer(String name, String password, Integer ID) {
		onlinePlayers.put(ID, new Player(name, password, ID));
		System.out.println(this.onlinePlayers.size());
	}
	
	public void createGame() {
		if(gameState == null) {
			initializeGameManager();
			gameManager.setup();
			gameState = gameManager.getGameState();
			gameState.setActiveFireFighterIndex(-1);
			gameManager.setFirstAction(true);
		}
		
	}
	
	public void initializeGameManager() {
//		gameManager = new GameManager(gameState, activeLobby);
		gameManager = new GameManager(activeLobby);
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
		gameState.placeFireFighter(onlinePlayers.get(userId).getFirefighter(), gameState.returnTile(coords[0],coords[1]));
		placedFF++;
		if(placedFF == gameState.getFireFighterList().size()) {
			if(!gameState.isExperienced()) {
				gameState.setActiveFireFighterIndex(0);
				generateActions();
			}
		}
	}
	
	public void placeVehicle(int direction, Vehicle type) {
		if(type.equals(Vehicle.Ambulance)) {
			gameState.getAmbulances()[direction].setCar(true);
		}
		else if(type.equals(Vehicle.Engine)) {
			gameState.getEngines()[direction].setCar(true);
		}
		
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
		System.out.println(this.currentLobbies.size());
	}
	
	public Player getPlayer(Integer inputInteger) {
		return onlinePlayers.get(inputInteger);
	}
	
	public void addPlayerToLobby(Player additionalPlayer) {
		this.activeLobby.addPlayer(additionalPlayer);
	}
	
	public Lobby getLobby() {
		return this.activeLobby;
	}

	public ArrayList<Lobby> getLobbyList() {
		System.out.println(this.currentLobbies.size());
		return this.currentLobbies;
	}

	public void endTurn() {
		Firefighter currentOne = gameState.getPlayingFirefighter();
		currentOne.endOfTurn();
		advanceFire();
		if(gameState.isGameTerminated() || gameState.isGameWon()) {
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
		gameState.setActiveFireFighterIndex( (gameState.getActiveFireFighterIndex() + 1)%(gameState.getFireFighterList().size()) );
		
	}
	
	public void advanceFire() {
		gameManager.advanceFire(false); //this boolean is saying that it is the initial advanced fire and not one caused by a hotspot flare up
		gameState.setAdvFireString(gameManager.getAdvFireMessage());
	}
	
	public void askRelevantFirefighters(Vehicle type) {
		System.out.println("we are in the ask");
		gameState.createFFToAsk(type);
	}
	
	public boolean hasEveryoneResponded() {
		return gameState.hasEveryoneResponded();
	}
	
	public void updateResponse(boolean b, int i) {
		gameState.setRideOption(b, i);
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
    public GameState loadGameMat(String name) {
    	try {
    		FileInputStream fi = new FileInputStream(new File(defaulGamesPath + name + ".txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			GameState gs1 = (GameState) oi.readObject();
			GameState gs = GameState.getInstance();
			System.out.println(gs1.toString());

			oi.close();
			fi.close();
			
			gs.updateGameStateFromObject(gs1);
			this.savedGames.add(gs);
			
			return gs1; //if not void
			
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
    	String folderPath = defaulGamesPath;
    	File folder = new File(folderPath);
    	File[] listOfFiles = folder.listFiles();
    	for (File f : listOfFiles) {
    		GameState gs = GameState.getInstance();
    		gs = loadGameMat(f.getAbsolutePath());
    		this.savedGames.add(gs);
    	}
    }
    
    public ArrayList<GameState> getSavedGames(){
    	return this.savedGames;
    }
	
    public int getSavedGamesNumber() {
    	return this.savedGames.size();
    }
    
    public void removeSavedGame(String name) {
    	GameState gs = GameState.getInstance();
    	gs.updateGameStateFromObject(loadGameMat(name + getSavedGamesNumber() + ".txt"));
    	this.savedGames.remove(gs);
    }

	public void setSpeciality(User person, Speciality desiredSpeciality) {
		int count = 0;
		if(gameState.getFreeSpecialities().contains(desiredSpeciality)) {
			Player player = onlinePlayers.get(person.getId());
			if(player.getFirefighter().getSpeciality()!=null) {
				gameState.addFreedSpeciality(player.getFirefighter().getSpeciality());
				player.getFirefighter().setSpeciality(desiredSpeciality);
				gameState.removeSelectedSpeciality(desiredSpeciality);
			}else {
				player.getFirefighter().setSpeciality(desiredSpeciality);
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
			int ff = person.getDesiredFirefighter();
			System.out.println("we are in hrerererer" + ff);
			if(gameState.getFreeFirefighters().size() != 0) {
				Player player = onlinePlayers.get(person.getId());
				if(player.getFirefighter()!=null) {
					if(gameState.getListOfPlayers().size() == gameState.getFireFighterList().size()) {
						gameState.getFreeFirefighters().add(player.getFirefighter());
						player.setFirefighter(gameState.getFreeFirefighters().get(ff));
						gameState.getFreeFirefighters().get(ff).setPlayer(player);
						gameState.getFreeFirefighters().remove(ff);
					}
				} else {
					System.out.println("were here");
					player.setFirefighter(gameState.getFreeFirefighters().get(ff));
					System.out.println(player.getFirefighter().getOwner().getUserName());
					gameState.getFreeFirefighters().get(ff).setPlayer(player);
					System.out.println(gameState.getFreeFirefighters().get(ff).getOwner().getUserName());
					gameState.getFreeFirefighters().remove(ff);
					System.out.println(gameState.getFreeFirefighters().size());
					
					
				}
			} else System.out.println("we should not be here");
		}

		public void resetHashMap() {
			gameState.resetHashMap();
			
		}
	
}

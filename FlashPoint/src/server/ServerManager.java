package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import actions.Action;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import game.GameState;
import lobby.Lobby;
import managers.GameManager;
import tile.Tile;
import token.Firefighter;

public class ServerManager {
	
	private HashMap<String,String> accounts;
	private HashMap<Integer, Player> onlinePlayers;
	private ArrayList<Lobby> currentLobbies;
	
	private Lobby activeLobby;
	private GameState gameState;
	private GameManager gameManager;
	
	private int placedFF = 0;
	
	public ServerManager() {
		onlinePlayers = new HashMap<Integer, Player>();
		accounts = new HashMap<String,String>();
		currentLobbies = new ArrayList<Lobby>();
		accounts.put("Zaid", "apple");
	}
	
	public void createPlayer(String name, String password, Integer ID) {
		onlinePlayers.put(ID, new Player(name, password, ID));
		System.out.println(this.onlinePlayers.size());
	}
	
	public void createGame() {
	/**	if(gameState == null) {
			System.out.println("ServerManager :- GameState was null and is now set up");
			gameState = GameState.getInstance();
			gameState.updateGameStateFromLobby(activeLobby);
			initializeGameManager();
			gameState.setListOfPlayers(activeLobby.getPlayers());
			
			if(activeLobby.getBoard().equals("Board 1")) {
				if(activeLobby.getMode().equals("Family")) {
				gameState.initializeEdges(activeLobby.getTemplate().getEdgeLocations());
				// we are setting outer doors open!
				//if exterior door then open the door not implemented!!
				gameState.openExteriorDoors();
				gameState.initializePOI(activeLobby.getTemplate().getPOILocations());
				gameState.initializeFire(activeLobby.getTemplate().getFireLocations());
				}
				if(activeLobby.getMode().equals("Experienced")) {
					if(activeLobby.getDifficulty().equals("Recruit")) initExperiencedGame(3,3);
					if(activeLobby.getDifficulty().equals("Veteran")) initExperiencedGame(3,4);
					if(activeLobby.getDifficulty().equals("Heroic")) initExperiencedGame(4,5);
				}
				
				}
				gameState.setFirefighters();
			
			
			
			gameState.setActiveFireFighterIndex(-1);
			System.out.println("active FF index is (3) :- " + gameState.getActiveFireFighterIndex());
		}
		testGS.placeFireFighter(onlinePlayers.get(Integer.valueOf(12345)).getFirefighter(), testGS.returnTile(3,0));
		generateActions();**/
		
		/**New Initialization**/
		if(gameState == null) {
			initializeGameManager();
			gameManager.setup();
			gameState = gameManager.getGameState();
			gameState.setActiveFireFighterIndex(-1);
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
			//set the index to the initial player. This trigger the player to be able to view their actions.
			//testGS.setActiveFireFighterIndex(0);
			gameState.setActiveFireFighterIndex(0);
			generateActions();
		}
	}
	
	public void performAction(Action a) {
		a.perform(gameState);
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
//		this.activeLobby = new Lobby();
//		this.activeLobby.setName("FUNTIMES");
//		Player temp = new Player("hello", "goodbye");
//		addPlayerToLobby(temp);
//		this.currentLobbies.add(activeLobby);
		System.out.println(this.currentLobbies.size());
		return this.currentLobbies;
	}

	public void endTurn() {
		Firefighter temp = gameState.getPlayingFirefighter();
		int AP = temp.getAP();
		if(AP + 4 > 8) {
			temp.setAP(8);
		} else {
			temp.setAP(AP + 4);
		}
		advanceFire();
		if(gameState.isGameTerminated() || gameState.isGameWon()) {
			setFFNextTurn();
			generateActions();
		} else {
			setFFNextTurn();
			generateActions();
		}
		
		
		
	}
	
	public void setFFNextTurn() {
		gameState.setActiveFireFighterIndex( (gameState.getActiveFireFighterIndex() + 1)%(gameState.getFireFighterList().size()) );
		
	}
	
	public void advanceFire() {
		gameManager.advanceFire();
		gameState.setAdvFireString(gameManager.getAdvFireMessage());
	}
	public void initExperiencedGame(int initialExplosions, int hazmats) {
		//resolve initialExplosion amount of explosions
		for(int i=initialExplosions; i > 0; i--) {
		
			int blackDice = 0;
			
			if(i == initialExplosions) {
				int explosionAt = gameState.getRandomNumberInRange(1,8);
				blackDice = explosionAt;
				
				if (explosionAt == 1) {
					gameState.returnTile(3,3).setFire(2);
					gameState.returnTile(3,3).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(3,3));
				}
				else if (explosionAt == 2) {
					gameState.returnTile(3,4).setFire(2);
					gameState.returnTile(3,4).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(3,4));
				}
				else if (explosionAt == 3) {
					gameState.returnTile(3,5).setFire(2);
					gameState.returnTile(3,5).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(3,5));
				}
				else if (explosionAt == 4) {
					gameState.returnTile(3,6).setFire(2);
					gameState.returnTile(3,6).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(3,6));
				}
				else if (explosionAt == 5) {
					gameState.returnTile(4,6).setFire(2);
					gameState.returnTile(4,6).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(4,6));
				}
				else if (explosionAt == 6) {
					gameState.returnTile(4,5).setFire(2);
					gameState.returnTile(4,5).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(4,5));
				}
				else if (explosionAt == 7) {
					gameState.returnTile(4,4).setFire(2);
					gameState.returnTile(4,4).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(4,4));
				}
				else {
					gameState.returnTile(4,3).setFire(2);
					gameState.returnTile(4,3).setHotSpot(1);
					gameManager.explosion(gameState.returnTile(4,3));
				}
			}else if(i == initialExplosions - 1) {
				boolean exit = true;
				
				while(exit) {
					Tile explosionAt = gameState.rollForTile();
					if(explosionAt.getFire() != 2) {
						explosionAt.setFire(2);
						explosionAt.setHotSpot(1);
						gameManager.explosion(explosionAt);
						blackDice = explosionAt.getY(); //Confirm with Ben!
						exit = false;
					}
				}
			}else if(i == initialExplosions - 2) {
//				Tile explosionAt = testGS.returnTile((9 - blackDice),testGS.getRandomNumberInRange(1, 6));
//				if(explosionAt.getFire() != 2) {
//					explosionAt.setFire(2);
//					explosionAt.setHotSpot(1);
//					gameManager.explosion(explosionAt);
//					//blackDice = explosionAt.getX();
//				}
				
				boolean exit = true;

//				while(exit) {
//					Tile newExplosionAt = testGS.rollForTile();
//					
//					if(newExplosionAt.getFire() != 2) {
//						newExplosionAt.setFire(2);
//						newExplosionAt.setHotSpot(1);
//						gameManager.explosion(newExplosionAt);
//						//blackDice = newExplosionAt.getX();
//						exit = false;
//					}
//				}
				
				Tile newExplosionAt = gameState.returnTile(gameState.getRandomNumberInRange(1, 6), (9 - blackDice));
				while(exit) {
					if(newExplosionAt.getFire() != 2) {
						newExplosionAt.setFire(2);
						newExplosionAt.setHotSpot(1);
						gameManager.explosion(newExplosionAt);
						//blackDice = newExplosionAt.getX();
						exit = false;
					}
					else {
						newExplosionAt = gameState.rollForTile();
					}
				}
				
			}else {
				boolean exit = true;
				
				while(exit) {
					Tile newExplosionAt = gameState.rollForTile();
					
					if(newExplosionAt.getFire() != 2) {
						newExplosionAt.setFire(2);
						newExplosionAt.setHotSpot(1);
						gameManager.explosion(newExplosionAt);
						//blackDice = newExplosionAt.getX();
						exit = false;
					}
				}
			}
		}
		//roll to place hazmats
		
		for(int i = 0; i < hazmats; i++) {
		boolean exit = true;
		
		while(exit) {
			Tile hazmatAt = gameState.rollForTile();
			
			if(hazmatAt.getFire() != 2) {
				hazmatAt.setHazmat(1);
				exit = false;
			}
		}
		
		}
		//place additional 3 hotspots for veteran/heroic mode
		if((initialExplosions == 3 && hazmats == 4) || (initialExplosions == 4 && hazmats == 5)) {
			for(int i = 0; i < 3; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gameState.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
		//place additional hotspot depedning on player number
		if(gameState.getListOfPlayers().size() >= 4) {
			for(int i = 0; i < 3; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gameState.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
		//place additional hotspot if player = 3
		if(gameState.getListOfPlayers().size() == 3) {
			for(int i = 0; i < 2; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gameState.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
				
	}

}

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
	private GameState testGS;
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
		if(testGS == null) {
			System.out.println("ServerManager :- GameState was null and is now set up");
			testGS = GameState.getInstance();
			testGS.updateGameStateFromLobby(activeLobby);
			initializeGameManager();
			testGS.setListOfPlayers(activeLobby.getPlayers());
			
			if(activeLobby.getBoard().equals("Board 1")) {
				if(activeLobby.getMode().equals("Family")) {
				testGS.initializeEdges(activeLobby.getTemplate().getEdgeLocations());
				// we are setting outer doors open!
				//if exterior door then open the door not implemented!!
				testGS.openExteriorDoors();
				testGS.initializePOI(activeLobby.getTemplate().getPOILocations());
				testGS.initializeFire(activeLobby.getTemplate().getFireLocations());
				}
				if(activeLobby.getMode().equals("Experienced")) {
					if(activeLobby.getDifficulty().equals("Recruit")) initExperiencedGame(3,3);
					if(activeLobby.getDifficulty().equals("Veteran")) initExperiencedGame(3,4);
					if(activeLobby.getDifficulty().equals("Heroic")) initExperiencedGame(4,5);
				}
				
				}
				testGS.setFirefighters();
			
			
			
			testGS.setActiveFireFighterIndex(-1);
			System.out.println("active FF index is (3) :- " + testGS.getActiveFireFighterIndex());
		}
//		testGS.placeFireFighter(onlinePlayers.get(Integer.valueOf(12345)).getFirefighter(), testGS.returnTile(3,0));
//		generateActions();
	}
	
	public void initializeGameManager() {
		gameManager = new GameManager(testGS);
	}
	
	public void generateActions() {
		gameManager.setAllAvailableActions(gameManager.generateAllPossibleActions());
		testGS.updateActionList(gameManager.getAllAvailableActions());
	}

	
//	public void createLobby() {
//		testLobby = new Lobby();
//		testLobby.setName("FUNTIMES");
//		testLobby.addPlayer(onlinePlayers.get(Integer.valueOf(12345)));
//		currentLobbies.add(testLobby);
//	}
	
	public void placeFirefighter(int[] coords, Integer userId) {
		testGS.placeFireFighter(onlinePlayers.get(userId).getFirefighter(), testGS.returnTile(coords[0],coords[1]));
		placedFF++;
		if(placedFF == testGS.getFireFighterList().size()) {
			//set the index to the initial player. This trigger the player to be able to view their actions.
			//testGS.setActiveFireFighterIndex(0);
			testGS.setActiveFireFighterIndex(0);
			generateActions();
		}
	}
	
	public void performAction(Action a) {
		a.perform(testGS);
		generateActions();
	}
	
	public HashMap<String, String> getAccounts(){
		return this.accounts;
	}
	
	public HashMap<Integer, Player> getPlayers(){
		return this.onlinePlayers;
	}
	
	
	public GameState getGameState() {
		return this.testGS;
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
		Firefighter temp = testGS.getPlayingFirefighter();
		int AP = temp.getAP();
		if(AP + 4 > 8) {
			temp.setAP(8);
		} else {
			temp.setAP(AP + 4);
		}
		advanceFire();
		if(testGS.isGameTerminated() || testGS.isGameWon()) {
			setFFNextTurn();
			generateActions();
		} else {
			setFFNextTurn();
			generateActions();
		}
		
		
		
	}
	
	public void setFFNextTurn() {
		testGS.setActiveFireFighterIndex( (testGS.getActiveFireFighterIndex() + 1)%(testGS.getFireFighterList().size()) );
		
	}
	
	public void advanceFire() {
		gameManager.advanceFire();
		testGS.setAdvFireString(gameManager.getAdvFireMessage());
	}
	public void initExperiencedGame(int initialExplosions, int hazmats) {
		//resolve initialExplosion amount of explosions
		for(int i=initialExplosions; i > 0; i--) {
		
			int blackDice = 0;
			
			if(i == initialExplosions) {
				int explosionAt = testGS.getRandomNumberInRange(1,8);
				blackDice = explosionAt;
				
				if (explosionAt == 1) {
					testGS.returnTile(3,3).setFire(2);
					testGS.returnTile(3,3).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(3,3));
				}
				else if (explosionAt == 2) {
					testGS.returnTile(3,4).setFire(2);
					testGS.returnTile(3,4).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(3,4));
				}
				else if (explosionAt == 3) {
					testGS.returnTile(3,5).setFire(2);
					testGS.returnTile(3,5).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(3,5));
				}
				else if (explosionAt == 4) {
					testGS.returnTile(3,6).setFire(2);
					testGS.returnTile(3,6).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(3,6));
				}
				else if (explosionAt == 5) {
					testGS.returnTile(4,6).setFire(2);
					testGS.returnTile(4,6).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(4,6));
				}
				else if (explosionAt == 6) {
					testGS.returnTile(4,5).setFire(2);
					testGS.returnTile(4,5).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(4,5));
				}
				else if (explosionAt == 7) {
					testGS.returnTile(4,4).setFire(2);
					testGS.returnTile(4,4).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(4,4));
				}
				else {
					testGS.returnTile(4,3).setFire(2);
					testGS.returnTile(4,3).setHotSpot(1);
					gameManager.explosion(testGS.returnTile(4,3));
				}
			}else if(i == initialExplosions - 1) {
				boolean exit = true;
				
				while(exit) {
					Tile explosionAt = testGS.rollForTile();
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
				
				Tile newExplosionAt = testGS.returnTile(testGS.getRandomNumberInRange(1, 6), (9 - blackDice));
				while(exit) {
					if(newExplosionAt.getFire() != 2) {
						newExplosionAt.setFire(2);
						newExplosionAt.setHotSpot(1);
						gameManager.explosion(newExplosionAt);
						//blackDice = newExplosionAt.getX();
						exit = false;
					}
					else {
						newExplosionAt = testGS.rollForTile();
					}
				}
				
			}else {
				boolean exit = true;
				
				while(exit) {
					Tile newExplosionAt = testGS.rollForTile();
					
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
			Tile hazmatAt = testGS.rollForTile();
			
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
					Tile hotspotAt = testGS.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
		//place additional hotspot depedning on player number
		if(testGS.getListOfPlayers().size() >= 4) {
			for(int i = 0; i < 3; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = testGS.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
		//place additional hotspot if player = 3
		if(testGS.getListOfPlayers().size() == 3) {
			for(int i = 0; i < 2; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = testGS.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
				}
		}
				
	}

}

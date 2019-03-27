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
import game.GameState;
import lobby.Lobby;
import managers.GameManager;
import tile.Tile;
import token.Firefighter;
import token.Vehicle;

public class ServerManager {
	
	private HashMap<String,String> accounts;
	private HashMap<Integer, Player> onlinePlayers;
	private ArrayList<Lobby> currentLobbies;
	
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
		gameManager.advanceFire(false); //this boolean is saying that it is the initial advanced fire and not one caused by a hotspot flare up
		gameState.setAdvFireString(gameManager.getAdvFireMessage());
	}
	
	public void saveGame() {
		//@eric take the current game state and save it here
		
		try {
			int savedGameNumber = new File("C:\\Users\\junha\\git\\f2018-group11\\FlashPoint\\src\\savedGame").listFiles().length; //check how many games are already saved
			FileOutputStream f = new FileOutputStream(new File("savedGame" + (savedGameNumber++) + ".txt")); //save it as "savedGame#.txt"
			
			ObjectOutputStream o = new ObjectOutputStream(f);
	
			o.writeObject(gameState);
			
			o.close();
			f.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	//JUNHA : theoretically this should read our savedGame#.txt file and read our object 
	public void loadGame(int gameNumber) {
		
		try {
			FileInputStream fi = new FileInputStream(new File("savedGame" + gameNumber + ".txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			this.gameState = (GameState) oi.readObject();
					
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
	
}

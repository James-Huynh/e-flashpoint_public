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

public class ServerManager {
	
	private HashMap<String,String> accounts;
	private HashMap<Integer, Player> onlinePlayers;
	private ArrayList<Lobby> currentLobbies;
	
	private Lobby activeLobby;
	private GameState testGS;
	private GameManager gameManager;
	
	public ServerManager() {
		onlinePlayers = new HashMap<Integer, Player>();
		accounts = new HashMap<String,String>();
		currentLobbies = new ArrayList<Lobby>();
	}
	
	public void createPlayer(String name, String password, Integer ID) {
		onlinePlayers.put(ID, new Player(name, password));
		System.out.println(this.onlinePlayers.size());
	}
	
	public void createGame() {
		testGS = GameState.getInstance();
		testGS.updateGameStateFromLobby(activeLobby);
		initializeGameManager();
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
//		System.out.println(onlinePlayers.get(Integer.valueOf(12345)).getFirefighter().getAP());
		testGS.placeFireFighter(onlinePlayers.get(userId).getFirefighter(), testGS.returnTile(coords[0],coords[1]));
//		testGS.getFireFighterList().get(0).setAP(10);
//		System.out.println(testGS.returnTile(coords[0],coords[1]).getFirefighterList().get(0).getAP());
	}
	
	public void performAction(Action a) {
		a.perform(testGS);
	}
	
	public HashMap<String, String> getAccounts(){
		return this.accounts;
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
		this.activeLobby = new Lobby();
		this.activeLobby.setName("FUNTIMES");
		Player temp = new Player("hello", "goodbye");
		addPlayerToLobby(temp);
		this.currentLobbies.add(activeLobby);
		System.out.println(this.currentLobbies.size());
		return this.currentLobbies;
	}



}

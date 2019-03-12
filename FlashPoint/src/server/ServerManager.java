package server;

import java.io.IOException;
import java.io.ObjectInputStream;
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
	
	private Lobby testLobby;
	private GameState testGS;
	private GameManager gameManager;
	
	public ServerManager() {
		onlinePlayers = new HashMap<Integer, Player>();
		accounts = new HashMap<String,String>();
	}
	
	public void createPlayer(String name, String password, Integer ID) {
		onlinePlayers.put(ID, new Player(name, password));
	}
	
	public void createGame() {
		testGS = GameState.getInstance();
		testGS.updateGameStateFromLobby(testLobby);
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

	
	public void createLobby() {
		testLobby = new Lobby();
		testLobby.addPlayer(onlinePlayers.get(Integer.valueOf(12345)));
	}
	
	public void placeFirefighter(int[] coords) {
//		System.out.println(onlinePlayers.get(Integer.valueOf(12345)).getFirefighter().getAP());
		testGS.placeFireFighter(onlinePlayers.get(Integer.valueOf(12345)).getFirefighter(), testGS.returnTile(coords[0],coords[1]));
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
		this.testLobby = newLobby;
	}
	
	public Player getPlayer(Integer inputInteger) {
		return onlinePlayers.get(inputInteger);
	}
	
	public void addPlayerToLobby(Player additionalPlayer) {
		this.testLobby.addPlayer(additionalPlayer);
	}
	
	public Lobby getLobby() {
		return this.testLobby;
	}



}

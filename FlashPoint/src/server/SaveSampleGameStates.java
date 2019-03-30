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

public class SaveSampleGameStates {

	private static String defaulGamesPath = "savedGames/";
	
	public SaveSampleGameStates() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// CREATE AND SAVE COUPLE OF GAME STATES
		
		//general
		GameState gameState;
		ServerManager server = new ServerManager();
		server.createPlayer("Mat", "abc", 1);
		server.createPlayer("Ben", "lol", 2);
		server.createPlayer("Zaid", "hello", 3);
		server.createPlayer("Eric", "eric", 4);
		server.createPlayer("Junha", "starcraft", 5);
		Lobby lobby = new Lobby();
		lobby.setClickable(true);
		ArrayList<Player> al = new ArrayList<Player>(server.getPlayers().values());
		lobby.setPlayers(al);
		lobby.assignColours();
		lobby.setMode("Family");
		lobby.setName("myLobby1");
		lobby.setDifficulty("Heroic");
		lobby.setBoard("Board 1");
		lobby.createTemplate();
		server.setLobby(lobby);
		server.initializeGameManager();
		GameManager gm = server.getGameManager();
		//1. the very beginning of the game - family version.
		gm.setup();
		server.saveGameMat(gm.getGameState(), "family");
		
		//2. the very beginning of the game - advanced version.
		Lobby lobby2 = new Lobby();
		ArrayList<Player> all = new ArrayList<Player>(server.getPlayers().values());
		lobby2.setPlayers(all);
		lobby2.setClickable(true);
		lobby2.assignColours();
		lobby2.setMode("Experienced");
		lobby2.setName("myLobby2");
		lobby2.setDifficulty("Heroic");
		lobby2.setBoard("Board 2");
		lobby2.createTemplate();
		server.setLobby(lobby2);
		GameManager gm2 = server.getGameManager();
		gm2.setup();
		server.saveGameMat(gm2.getGameState(), "advancedHeroic");
	}

}

package gui;

import java.util.ArrayList;
import java.util.Set;

import actions.Action;
import game.FamilyGame;
import game.GameState;
import gui.Table;
import lobby.Lobby;
import managers.GameManager;
import server.Player;
import tile.Tile;
import token.Colour;
import token.Firefighter;
import token.Token;

public class gameTest {
	
	protected static LocalizedTable table;
	protected static GameState tester;
	protected static GameManager current;
	
public static void main(String[] args) {
		
		tester = GameState.getInstance();
		
		Lobby tempLobby = new Lobby();
		
		tester.updateGameStateFromLobby(tempLobby);
		
		current = new GameManager(tester);

		
		table = new LocalizedTable(tester);
		
	}
	
	public static void repainter() {

		current.setAllAvailableActions(current.generateAllPossibleActions());

		tester.updateActionList(current.getAllAvailableActions());
//		table = new LocalizedTable(tester);
		table.refresh(tester);
//		table.updateBoard(tester);
	}
	
	public static void nextTurn() {
		Firefighter temp = tester.getPlayingFirefighter();
		int AP = temp.getAP();
		if(AP + 4 > 8) {
			temp.setAP(8);
		}else {
			temp.setAP(AP + 4);
		}
		current.advanceFire();
		if(tester.isGameTerminated()) {
			System.out.println("Game Over");
		} else if(tester.isGameWon()) {
			System.out.println("Game Won");
		}else {
			tester.setActiveFireFighterIndex( (tester.getActiveFireFighterIndex() + 1)%(tester.getFireFighterList().size()) );
			current.setAllAvailableActions(current.generateAllPossibleActions());
			tester.updateActionList(current.getAllAvailableActions());
//			table = new LocalizedTable(tester);
			table.refresh(tester);
//			table.updateBoard(tester);
		}
	}
	
	public static void placeFF(Tile tile) {
		Firefighter temp = tester.getPlayingFirefighter();
		tester.placeFireFighter(temp, tile);
		if(tester.getActiveFireFighterIndex() + 1 == tester.getFireFighterList().size()) {
			LocalizedTable.setPlacing(false);
			tester.setActiveFireFighterIndex( (tester.getActiveFireFighterIndex() + 1)%(tester.getFireFighterList().size()));
			System.out.println(tester.getActiveFireFighterIndex());
			repainter();
		}else {
			tester.setActiveFireFighterIndex( (tester.getActiveFireFighterIndex() + 1));
//			table = new LocalizedTable(tester);
			table.refresh(tester);
//			table.updateBoard(tester);
		}
	}
	
}

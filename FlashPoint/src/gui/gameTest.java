package gui;

import game.GameState;
import lobby.Lobby;
import managers.GameManager;
import tile.Tile;
import token.Firefighter;

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
		table.hideAdvPanel();
		table.refresh(tester);
		
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
		table.showAdvanceFireString(current.getAdvFireMessage());
		if(tester.isGameTerminated()) {
			table.showGameTermination();
			System.out.println("Game Over");
		} else if(tester.isGameWon()) {
			table.showGameTermination();
			System.out.println("Game Won");
		}else {
			tester.setActiveFireFighterIndex( (tester.getActiveFireFighterIndex() + 1)%(tester.getFireFighterList().size()) );
			current.setAllAvailableActions(current.generateAllPossibleActions());
			tester.updateActionList(current.getAllAvailableActions());
			table.refresh(tester);
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
			table.refresh(tester);
		}
	}
	
}

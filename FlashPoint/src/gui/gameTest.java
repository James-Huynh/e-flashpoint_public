package gui;

import game.BoardOne;
import game.GameState;
import lobby.Lobby;
import managers.GameManager;
import server.Player;
import tile.Tile;
import token.Firefighter;
import token.POI;

public class gameTest {
	
	protected static LocalizedTable table;
	protected static GameState tester;
	protected static GameManager current;
	
public static void main(String[] args) {
		
		tester = GameState.getInstance();
		
		Lobby tempLobby = new Lobby();
		tempLobby.addPlayer(new Player("Logan", "ben", 12));
		tempLobby.addPlayer(new Player("Ben", "ben", 12));
//		tempLobby.addPlayer(new Player("Cao", "ben", 12));

		
		tempLobby.setMode("Family");
//		tempLobby.setTemplete(new FamilyGame());
		
		tempLobby.setBoard("Board1");
		tempLobby.createTemplate();
		
//		tester.updateGameStateFromLobby(tempLobby);
		
		current = new GameManager(tempLobby);
		current.setup();
		tester = current.getGameState();
		
//		//Player placement
//		tester.placeFireFighter(tester.getFireFighterList().get(0), tester.returnTile(5,0));
//		tester.placeFireFighter(tester.getFireFighterList().get(1), tester.returnTile(5,0));
//		
//		//Wall Damage tracker.
//		tester.returnTile(1,1).getEdge(0).chop();
//		tester.returnTile(1,1).getEdge(0).chop();
//		tester.returnTile(2,1).getEdge(0).chop();
//		tester.returnTile(2,1).getEdge(0).chop();
//		tester.returnTile(4,1).getEdge(0).chop();
//		tester.returnTile(4,1).getEdge(0).chop();
//		tester.returnTile(5,1).getEdge(0).chop();
//		tester.returnTile(5,1).getEdge(0).chop();
//		tester.returnTile(1,1).getEdge(1).chop();
//		tester.returnTile(1,1).getEdge(1).chop();
//		tester.returnTile(1,2).getEdge(1).chop();
//		tester.returnTile(1,2).getEdge(1).chop();
//		tester.returnTile(1,3).getEdge(1).chop();
//		tester.returnTile(1,3).getEdge(1).chop();
//		tester.returnTile(1,4).getEdge(1).chop();
//		tester.returnTile(1,4).getEdge(1).chop();
//		tester.returnTile(1,5).getEdge(1).chop();
//		tester.returnTile(1,5).getEdge(1).chop();
//		tester.returnTile(1,7).getEdge(1).chop();
//		tester.returnTile(1,7).getEdge(1).chop();
//		tester.returnTile(1,8).getEdge(1).chop();
//		tester.returnTile(1,8).getEdge(1).chop();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		tester.updateDamageCounter();
//		
//		
//		
//		//victims lost conditions
//		tester.getLostVictimsList().add(new POI(true));
//		tester.getLostVictimsList().add(new POI(true));
//		tester.getLostVictimsList().add(new POI(true));
//		
//		tester.returnTile(4, 5).addPoi(new POI(true));
//		tester.returnTile(6, 7).addPoi(new POI(true));
//		tester.returnTile(2,4).getPoiList().remove(0);
//		tester.returnTile(5,8).getPoiList().remove(0);
//		
//		//victims won conditions
//		tester.getSavedVictimsList().add(new POI(true));
//		tester.getSavedVictimsList().add(new POI(true));
//		tester.getSavedVictimsList().add(new POI(true));
//		tester.getSavedVictimsList().add(new POI(true));
//		tester.getSavedVictimsList().add(new POI(true));
//		tester.getSavedVictimsList().add(new POI(true));
		
		
//		current = new GameManager(tester);

		
		table = new LocalizedTable(tester);
//		current.setAllAvailableActions(current.generateAllPossibleActions());
//		tester.updateActionList(current.getAllAvailableActions());
		
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
		if(tester.isGameWon()) {
			table.showGameTermination();
			System.out.println("Game Won");
			table.refresh(tester);
			table.showGameTermination();
		} else if(tester.isGameTerminated()) {
			System.out.println("Game Over");
			table.refresh(tester);
			table.showGameTermination();
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

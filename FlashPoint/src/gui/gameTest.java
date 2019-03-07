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
		Tile[][] testerBoard = tester.getMatTiles();
		Lobby tempLobby = new Lobby();
		tester.updateGameStateFromLobby(tempLobby);
		Tile testTile = tester.returnTile(4, 5);
		Tile testTile2 = tester.returnTile(1, 4);
		Tile testTile3 = tester.returnTile(5, 6);
		current = new GameManager(tester);
		tester.placeFireFighter(tester.getFireFighterList().get(0), testTile);
		tester.placeFireFighter(tester.getFireFighterList().get(1), testTile3);
		tester.placeFireFighter(tester.getFireFighterList().get(2), testTile2);
//		testTile.getPoiList().get(0).reveal();
		
		current.generateAllPossibleActions();
		
		tester.updateActionList(current.getAllAvailableActions());

		
		table = new LocalizedTable(tester);
		
		System.out.println("testComplete");
	}
	
	public static void repainter() {
//
		current.generateAllPossibleActions();
//		current.getAllAvailableActions();
		tester.updateActionList(current.getAllAvailableActions());
		table = new LocalizedTable(tester);
	}
	
	//tester code
//	for(int i =0; i<8;i++) {
//		for(int j=0; j<10; j++) {
//			System.out.println(i + "|"+ j + "|" + test[i][j].checkInterior());
////			for(int e=0; e<4; e++) {
////				System.out.println(i + "|"+ j + "|" + test[i][j].getEdge(e).isBlank());
////			}
//		}
//	}
}

package gui;

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
	
public static void main(String[] args) {
		
		GameState tester = GameState.getInstance();
		Tile[][] testerBoard = tester.getMatTiles();
		Lobby tempLobby = new Lobby();
		tester.updateGameStateFromLobby(tempLobby);
		Tile testTile = tester.returnTile(3, 3);
		Tile testTile2 = tester.returnTile(2, 4);
		Tile testTile3 = tester.returnTile(5, 6);
//		Firefighter tempFF = new Firefighter(Colour.WHITE);
//		Firefighter tempFF2 = new Firefighter(Colour.BLUE);
//		tester.placeFireFighter(tempFF, testTile);
//		tester.placeFireFighter(tempFF2, testTile2);
		GameManager current = new GameManager(tester);
		tester.placeFireFighter(tester.getFireFighterList().get(0), testTile);
		tester.placeFireFighter(tester.getFireFighterList().get(1), testTile3);
		tester.placeFireFighter(tester.getFireFighterList().get(2), testTile2);
//		tester.returnTile(4, 5).setFire(1);
//		tester.returnTile(2, 4).setFire(2);
//		tester.returnTile(4, 6).setFire(1);
//		current.generateAllPossibleActions();
		current.advanceFire();
		current.advanceFire();
		current.advanceFire();
		current.advanceFire();
		current.advanceFire();
		current.advanceFire();
		
		Table table = new Table(tester);
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

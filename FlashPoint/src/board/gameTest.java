package board;

import board.Table;
import game.FamilyGame;
import game.GameState;
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
		Firefighter tempFF = new Firefighter(Colour.WHITE);
		Firefighter tempFF2 = new Firefighter(Colour.BLUE);
		tester.placeFireFighter(tempFF, testTile);
		tester.placeFireFighter(tempFF2, testTile2);
//		testTile2.getEdge(0).chop();
//		testTile2.getEdge(3).chop();
//		testTile2.getEdge(3).chop();
//		Tile testTile3 = tester.returnTile(2, 6);
//		Tile testTile4 = tester.returnTile(1, 6);
//		testTile3.getEdge(0).change();
//		testTile4.getEdge(1).destroyDoor();
		GameManager current = new GameManager(tester);
//		current.advanceFire();
		tester.returnTile(4, 5).setFire(1);
		tester.returnTile(2, 4).setFire(2);
		tester.returnTile(4, 6).setFire(1);
//		current.resolveFlashOver();
//		current.checkKnockDowns();
//		current.placePOI();
//		current.generateAllPossibleActions();
		current.advanceFire();
//		current.explosion(targetTile);
//		current.explosion(targetTile);
//		current.explosion(targetTile);
//		current.explosion(targetTile);
//		current.explosion(targetTile);
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

package board;

import board.Table;
import game.FamilyGame;
import game.GameState;
import lobby.Lobby;
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
		Tile testTile = tester.returnTile(0, 5);
		Tile testTile2 = tester.returnTile(2, 4);
		Firefighter tempFF = new Firefighter(Colour.WHITE);
		Firefighter tempFF2 = new Firefighter(Colour.BLUE);
		tester.placeFireFighter(tempFF, testTile);
		tester.placeFireFighter(tempFF2, testTile2);
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

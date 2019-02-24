package test;

import game.FamilyGame;
import game.GameState;

public class MainTest {

	public static void main(String[] args) {
		GameState gState = new GameState();
		FamilyGame template = new FamilyGame();
		
		gState.loadTemplate(template);

	}

}

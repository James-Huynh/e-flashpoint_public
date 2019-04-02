package lobby;

import java.io.Serializable;
// Start of user code for imports
import java.util.ArrayList;
import java.util.Random;

import game.BoardTwo;
import game.RandomBoardFive;
import game.RandomBoardFour;
import game.RandomBoardOne;
import game.RandomBoardThree;
import game.RandomBoardTwo;
import game.BoardOne;
import game.TemplateGame;
import server.Player;

public class Lobby implements Serializable  {

	private boolean isClickable;
	private boolean isLoadGame;
	private ArrayList<Player> players;
	private ArrayList<token.Colour> assignableColours;

	private String mode, name, difficulty, board;
	private int capacity;

	private TemplateGame template;
	private static final long serialVersionUID = 1L;


	public Lobby(){

		//dummy
		players = new ArrayList<Player>();

//		assignColours();

	}

	public void assignColours(){
		assignableColours = new ArrayList<token.Colour>();
		assignableColours.add(token.Colour.GREEN);
		assignableColours.add(token.Colour.BLACK);
		assignableColours.add(token.Colour.WHITE);
		assignableColours.add(token.Colour.RED);
		assignableColours.add(token.Colour.PURPLE);
		assignableColours.add(token.Colour.BLUE);
		for(int i = 0; i<players.size(); i++) {
			if(this.players.get(i) != null) {
				this.players.get(i).setColour(this.assignableColours.get(i));
			}
		}
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public String getType() {
		return mode;
	}

	public void setType(String type) {
		this.mode = type;
	}

	public TemplateGame getTemplate() {
		return template;
	}

	public void setTemplete(TemplateGame template) {
		this.template = template;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void addPlayer(Player newPlayer) {
		this.players.add(newPlayer);
		assignColours();
	}

	public void setDifficulty(String difficulty) {
		if(!difficulty.equals("notSelected"))
			this.difficulty = difficulty;
		
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setFamilyGame() {
		System.out.println("IN FAMILY GAME EVER GETTING CALLED?");
		template = new BoardOne("Family");
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}
	
	public void createTemplate() {
		System.out.println("this is in createTemplate");
		System.out.println(this.board);
		if(this.board.equals("Board 1")) {
			System.out.println(this.difficulty);
			if(this.mode.equals("Family")) {
				template = new BoardOne("Family");
				System.out.println("Do I come here?");
			}
			else if(this.mode.equals("Experienced")) {
				template = new BoardOne("Experienced");
			}
		}
		
		else if(this.board.equals("Board 2")) {
			if(this.mode.equals("Family")) {
				template = new BoardTwo("Family");
			}
			else if(this.mode.equals("Experienced")) {
				template = new BoardTwo("Experienced");
			}
		}
		
		else {
			System.out.println("creating random Board");
			Random rn = new Random();
			int boardNumber = rn.nextInt(5) + 1;
			if(this.mode.equals("Family")) {
				if(boardNumber == 1) template = new RandomBoardOne("Family");
				if(boardNumber == 2) template = new RandomBoardTwo("Family");
				if(boardNumber == 3) template = new RandomBoardThree("Family");
				if(boardNumber == 4) template = new RandomBoardFour("Family");
				if(boardNumber == 5) template = new RandomBoardFive("Family");
			}else if(this.mode.equals("Experienced")) {
				if(boardNumber == 1) template = new RandomBoardOne("Experienced");
				if(boardNumber == 2) template = new RandomBoardTwo("Experienced");
				if(boardNumber == 3) template = new RandomBoardThree("Experienced");
				if(boardNumber == 4) template = new RandomBoardFour("Experienced");
				if(boardNumber == 5) template = new RandomBoardFive("Experienced");
			}
		}
	}

	public boolean getIsLoadGame() {
		return isLoadGame;
	}

	public void setIsLoadGame(boolean isLoadGame) {
		this.isLoadGame = isLoadGame;
	}

}

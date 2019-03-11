package lobby;

import java.io.Serializable;
// Start of user code for imports
import java.util.ArrayList;

import game.FamilyGame;
import game.TemplateGame;
import server.Player;

public class Lobby implements Serializable  {

	private boolean isClickable;
	private ArrayList<Player> players;
	private ArrayList<token.Colour> assignableColours;

	private String mode, name;
	private int capacity;

	private TemplateGame template;
	private static final long serialVersionUID = 1L;


	public Lobby(){

		//dummy
		players = new ArrayList<Player>();
		//			ArrayList<Player> playingPlayers = new ArrayList<Player>(3);
		players.add(new Player("Mat", "Cuba123"));
		players.add(new Player("Zaid", "zeroOneTwoThree"));
		players.add(new Player("Junha", "myPassword"));
		players.add(new Player("Ben", "Cuba123"));
		players.add(new Player("Cao", "zeroOneTwoThree"));
		players.add(new Player("James", "myPassword"));
		mode = "Family";
		if(mode.equals("Family")) {
			template = new FamilyGame();
		}
		assignColours();

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

}

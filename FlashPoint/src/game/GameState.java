package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.Serializable;

import actions.Action;
import token.Vehicle;
import edge.Edge;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Vehicle;
import game.FamilyGame;
import lobby.Lobby;
import server.Player;
import edge.Door;
import edge.Wall;
import edge.Blank;

public class GameState implements Serializable {

	protected int remainingVictims; //start with 12  //10 for Family 
	protected int remainingFalseAlarms; //start with 6 //5 for Family
	protected int wallsDamaged; //start with 0 upto MAX_WALL_DMGD
	protected int lostVictims; //if 4 lost lose!
	protected int savedVictims; //if 7 rescued win!
	protected int currentPoi; //DO WE NEED THIS? =remainingVictims+remainingFalseAlarms
	protected boolean gameTerminated;
	protected boolean gameWon;
	protected int activeFireFighterIndex;
	protected boolean isActiveGame;
	protected Edge[][] matEdges;
	protected Tile[][] matTiles;
	protected ParkingSpot[] engines;
	protected ParkingSpot[] ambulances;

	protected int currentTile; // ??
	protected ArrayList<Action> availableActions;
	

	protected ArrayList<Firefighter> listOfFireFighter;
	public int MAX_WALL_DMGD = 24;
	protected POI[] poiList; //@matekrk: this is important (fixed array) it is on purpose. 
	//you can't remove poi so easily. you need to replace with new one!
	private static final long serialVersionUID = 1L; //serialization


	/**
	 * modified by @matekrk :) :)
	 */

	//good question - how to init the game. 
	/*
	 * @matekrk:
	 * I think template is good approach. Either XML or JSON as a file of template
	 * but also it would be convenient - specially for debugging to have method reading all this crap as parameters
	 * we can help with that shit
	 * Below there is my approach towards SINGLETON!
	 * 
	 * Question 2 from @matekrk - remember in the end of the day this info is from... LOBBY! 
	 * so maybe i can also have gamestate instance from lobby
	 * me: ¯\_(ツ)_/¯
	 */

	/*
	 * SINGLETON
	 */
	private GameState() {
		
	}

	//preventing from creating other instances
	private static final GameState instance = new GameState();

	//but everybody can access it
	public static GameState getInstance() {
		return instance;
	} 


	/*
	 * UPDATE/LOADING
	 */

	//copy constructor
	public void updateGameStateFromObject(GameState gs1) {
		remainingVictims = gs1.remainingVictims;
		remainingFalseAlarms = gs1.remainingFalseAlarms;
		wallsDamaged = gs1.wallsDamaged;
		lostVictims = gs1.wallsDamaged;
		savedVictims = gs1.savedVictims;
		currentPoi = gs1.currentPoi;
		gameTerminated = gs1.gameTerminated;
		gameWon = gs1.gameWon;
		activeFireFighterIndex = gs1.activeFireFighterIndex;
		isActiveGame = gs1.isActiveGame;
		matEdges = gs1.matEdges;
		matTiles = gs1.matTiles;
		currentTile = gs1.currentTile;
		availableActions = gs1.availableActions;
		listOfFireFighter = gs1.listOfFireFighter;
		MAX_WALL_DMGD = gs1.MAX_WALL_DMGD;
		poiList = gs1.poiList; 	
	}
	
	/*
    public void updateGameStateFromTemplate() {
    	this.isActiveGame = true;

    	initializeTiles();
    	initializeEdges();
    	initializeFires();
    }
	 */

	//ASSUME FOR DEMO THAT LOBBY GIVES ME ALWAYS FAMILY SETTINGS AND I HAVE LIST OF USERS
	//added in basic family setting inits - ben
	public void updateGameStateFromLobby(Lobby lobby) {
		remainingVictims = 10;
		remainingFalseAlarms = 5;
		wallsDamaged = 0;
		lostVictims = 0;
		savedVictims = 0;
		currentPoi = 0;
		gameTerminated = false;
		gameWon = false;
		activeFireFighterIndex = 0;
		isActiveGame = true;
		matEdges = new Edge[21][9];
		matTiles = new Tile[8][10];
		initializeTiles();
		createAmbulances();
		createEnginge();
//		setClosest();
		initializeEdges(lobby.getTemplate().getEdgeLocations());
		initializeBasicTokens(lobby.getTemplate().getTokenLocations());
		
	}

	/*
	 * GETTERS
	 */

	//ALWAYS 3!
	public POI[] retrievePOI() {
		return poiList;
	}

	public boolean isGameTerminated() {
		return gameTerminated;
	}

	public boolean isGameWon() {
		if (gameTerminated == true) {
			return gameWon;
		}
		else return false;
	}
	
	public ParkingSpot[] getAmbulances() {
		return ambulances;
	}
	
	public ParkingSpot[] getEngines() {
		return engines;
	}

	//QUESTION
	public Tile nextTile() {
		/* only advanced version right? */
		return null;
	}

	//QUESTION
	public int getCurrentPOI() {
		// what is that even?
		return 0;
	}
	
	public Tile[][] getMatTiles(){
		return matTiles;
	}

	public Tile returnTile(int x, int y) {
		return matTiles[x][y];
	}

	public int getSavedVictims() {
		return savedVictims;
	}

	public ArrayList<Firefighter> getFireFighterList() {
		return getFireFighterList();
	}

	public Firefighter getPlayingFirefighter() {
		/* TODO: No message view defined */
		return listOfFireFighter.get(activeFireFighterIndex);
	}

	public int getActiveFireFighterIndex() {
		return activeFireFighterIndex;
	}

	public int getDamageCounter() {
		return wallsDamaged;
	}

	public ArrayList<Action> getAvailableActions(){
		return availableActions;
	}

	/*
	 * SETTERS
	 */

	public void setActiveFireFighterIndex(int i) {
		activeFireFighterIndex = i;
	}


	public void updateDamageCounter() {
		this.wallsDamaged = this.wallsDamaged + 1;
	}

	public void updatePOI(POI toUpdate) {
		for (int ind = 0; ind<poiList.length; ind++) {
			if (poiList[ind].equals(null)) {
				poiList[ind] = toUpdate;
				break;
			}
		}
	}

	public void removePOI(POI toRemove) {
		for (int ind = 0; ind<poiList.length; ind++) {
			if (poiList[ind].equals(toRemove)) {
				poiList[ind] = null;
				break;
			}
		}
	}

	public void updateSavedCount() {
		savedVictims++;
	}

	public void updateLostCount() {
		lostVictims++;
	}

	public void setIsActiveGame(boolean isActiveGame) {
		this.isActiveGame = isActiveGame;
	}
	
	public void setEngines(ParkingSpot[] engines) {
		this.engines = engines;
	}

	public void setAmbulances(ParkingSpot[] ambulances) {
		this.ambulances = ambulances;
	}

	//@matekrk question about efficiency, do we need this?

	// @James
	/**
	 * This method is used only once at the start of the game for each player.
	 * @param f
	 * @param t
	 */
	public void placeFireFighter(Firefighter f, Tile t) {
		//listOfFireFighter.add(f);
		t.addToFirefighterList(f);
		f.setCurrentPosition(t);
	}
	
	// @matekrk + Zaid
	/**
	 * 
	 */
	public void createAmbulances() {
		this.ambulances = new ParkingSpot[4];
		for (ParkingSpot forAmbulance : ambulances) {
			forAmbulance = new ParkingSpot(Vehicle.Ambulance, false);
		}
	}
	
	public void createEnginge() {
		this.engines = new ParkingSpot[4];
		for (ParkingSpot forEngine : engines) {
			forEngine = new ParkingSpot(Vehicle.Engine, false);
		}
	}
	
	/**
	 * This method initializes 2D array of tiles and place ambulance/engine spot in the right place. 
	 * It should be more general! Based on some template!!
	 */
	
	//updated tiles, parking spots need a little bit of work - ben
	public void initializeTiles() {
		
		//creating 10 x 8 tiles

		for (int i=0; i<=7; i++) {
			for (int j=0; j<=9; j++) {
			
				///// AMBULANCES
				
				if (i==0 && (j==5 || j==6)) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
//					matTiles[i][j].setParkingSpot(ambulances[0]);
//					ambulances[0].setTile(matTiles[i][j]);
				}
				
				else if ((i==3 || i==4) && j==0) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
//					matTiles[i][j].setParkingSpot(ambulances[3]);
//					ambulances[0].setTile(matTiles[i][j]);
				}
				
				else if (i==8 && (j==3 || j==4)) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
//					matTiles[i][j].setParkingSpot(ambulances[1]);
//					ambulances[0].setTile(matTiles[i][j]);
				}
				
				else if ((i==3 || i==4) && j==9) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
//					matTiles[i][j].setParkingSpot(ambulances[2]);
//					ambulances[0].setTile(matTiles[i][j]);
				}
				
				///////// ENGINES
				
				else if (i==0 && (j==7 || j==8)) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Engine);
//					matTiles[i][j].setParkingSpot(engines[0]);
//					engines[0].setTile(matTiles[i][j]);
				}
				
				else if ((i==1 || i==2) && j==0) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Engine);
//					matTiles[i][j].setParkingSpot(engines[3]);
//					engines[0].setTile(matTiles[i][j]);
				}
				
				else if (i==8 && (j==5 || j==6)) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Engine);
//					matTiles[i][j].setParkingSpot(engines[1]);
//					engines[0].setTile(matTiles[i][j]);
				}
				
				else if ((i==1 || i==2) && j==9) {
					matTiles[i][j] = new Tile(false, new int[] {i,j});
					matTiles[i][j].setParkingType(Vehicle.Engine);
//					matTiles[i][j].setParkingSpot(engines[2]);
//					engines[0].setTile(matTiles[i][j]);
				}
				
				else if (i==0 || i==7 || j==0 || j==9) {
					matTiles[i][j] = new Tile(false, new int[] {i,j}); //create exterior tiles
				}
				else { 
					matTiles[i][j] = new Tile(true, new int[] {i,j}); //create interior tiles
				}
			}
		}

	}
	
	/*
	 * This method indicates the closest ambulances and set its value
	 * complexity big O (n^5)
	 */
	public void setClosest() {
		for (int i=0; i<=matEdges.length; i++) {
			for (int j=0; j<=matEdges[0].length; j++) {
				int minDistance = (int)Double.POSITIVE_INFINITY;
				for (ParkingSpot a : ambulances) {
					for (Tile t: a.getTiles()) {
						if (minDistance > Math.abs(t.getX()-i) + Math.abs(t.getY()-j)) {
							returnTile(i,j).setNearestAmbulance(a);
						}
					}
				}
			}
		}
	}

	/**
	 * This method initializes Edges and puts them in to adjacentEdge array of each Tile.
	 */
	//updated the math for edges, the arrays were flipped - ben
	public void initializeEdges(/*TemplateGame template*/ int[][] edgeStartingPos) {

//		int[][] edgePos = template.getEdgeLocations();
		int[][] edgePos = edgeStartingPos;

		for (int i=0; i<9; i++) {
			for (int j=0; j<21; j++) {
//				Tile tempTile;
				int edgeChecker = edgePos[i][j];

				if (edgeChecker == 2) {
					if ((j+1)%2 == 0) { // if left/right edge
						if(j == 1) {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Door());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][j/2].setEdge(3, new Door());
							}
							else {
								matTiles[i][j/2].setEdge(1, new Door());
								matTiles[i-1][j/2].setEdge(3, new Door());
							}
						} else {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Door());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][j/2-1].setEdge(3, new Door());
							}
							else {
								matTiles[i][j/2].setEdge(1, new Door());
								matTiles[i-1][j/2].setEdge(3, new Door());
							}
						}
					}
					else if ((j+1)%2 == 1) { //if top/down edge
						if(j==0) { //if the edge has no left tile
							matTiles[i][j/2].setEdge(0, new Door());
						}
						else if(j==20) { //if the edge has no right tile
							matTiles[i][(j/2)-1].setEdge(2, new Door());
						}
						else {
							matTiles[i][(j/2)-1].setEdge(2, new Door());
							matTiles[i][(j/2)].setEdge(0, new Door());
						}
					}
				}

				if (edgeChecker == 1) {
					if ((j+1)%2 == 0) { // if left/right edge
						if(j == 1) {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Wall());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][(j/2)].setEdge(3, new Wall());
							}
							else {
								matTiles[i][j/2].setEdge(1, new Wall());
								matTiles[i-1][j/2].setEdge(3, new Wall());
							}
						}else {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Wall());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][(j/2)-1].setEdge(3, new Wall());
							}
							else {
								matTiles[i][j/2].setEdge(1, new Wall());
								matTiles[i-1][j/2].setEdge(3, new Wall());
							}
						}
					}
					else if ((j+1)%2 == 1) { //if top/down edge
						if(j==0) { //if the edge has no left tile
							matTiles[i][j].setEdge(0, new Wall());
						}
						else if(j==20) { //if the edge has no right tile
							matTiles[i][(j/2)-1].setEdge(2, new Wall());
						}
						else {
							matTiles[i][(j/2)-1].setEdge(2, new Wall());
							matTiles[i][(j/2)].setEdge(0, new Wall());
						}
					}

				}
				if (edgeChecker == 0){
					if ((j+1)%2 == 0) { // if left/right edge
						if(j==1) {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Blank());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][(j/2)].setEdge(3, new Blank());
							}
							else {
								matTiles[i][(j/2)].setEdge(1, new Blank());
								matTiles[i-1][(j/2)].setEdge(3, new Blank());
							}
						}else {
							if (i == 0) { //if the edge has no upper tile
								matTiles[i][j/2].setEdge(1, new Blank());
							}
							else if(i == 8){ //if the edge has no lower tile
								matTiles[i-1][(j/2)-1].setEdge(3, new Blank());
								if(j==19) {
									matTiles[i-1][9].setEdge(3, new Blank());
								}
							}
							else {
								matTiles[i][(j/2)].setEdge(1, new Blank());
								matTiles[i-1][(j/2)].setEdge(3, new Blank());
							}
						}
					}
					else if ((j+1)%2 == 1) { //if top/down edge
						if(j==0) { //if the edge has no left tile
							matTiles[i][j].setEdge(0, new Blank());
						}
						else if(j==20) { //if the edge has no right tile
							matTiles[i][(j/2)-1].setEdge(2, new Blank());
						}
						else {
							matTiles[i][(j/2)-1].setEdge(2, new Blank());
							matTiles[i][(j/2)].setEdge(0, new Blank());
						}
					}
				}
				
				//no idea why this is happening but temp fix for the last edge...
				
				//didnt cover case edgeChecker == -1
			}
		}


	}



	/*
	 * RANDOM
	 */

	public Tile rollForTile() {
		//random:
		Random r = new Random();
		int i = r.nextInt(matTiles.length);
		int j = r.nextInt(matEdges[0].length);
		return returnTile(i,j);
	}

	/*
	 * POIs
	 */

	//based on current state - randomly new POI
	public POI generatePOI() {
		int x = remainingFalseAlarms + remainingVictims;
		Random r = new Random();
		int y = r.nextInt(x);
		POI newPOI;

		//shit: what if x==0? @matekrk

		if (y < remainingFalseAlarms) {
			newPOI = new POI(false);
			remainingFalseAlarms--;
		}
		else {
			newPOI = new POI(true);
			remainingVictims--;
		}
		return newPOI;
	}

	public void newPOI(POI toBeAdded) {
		for (POI po : poiList) {
			if (po == null) {
				po = toBeAdded;
			}
		}
	}

	/*
	 * Available ACTIONS
	 */

	boolean containsAvailableActions(GameState a) {
		return a.getAvailableActions().contains(a);
	}

	int sizeOfAvailableActions(GameState a) {
		/* TODO: No message view defined */
		return a.getAvailableActions().size();
	}

	void removeAvailableAction(Action a) {
		/* TODO: No message view defined */
		if (getAvailableActions().contains(a)) {
			getAvailableActions().remove(a);
		}
	}

	void addAvailableActions(Action a) {
		getAvailableActions().add(a);
	}

	/*
	 * NAVIGATION
	 */

	//left: 0, top: 1, right: 2, down: 3
	public Tile getNeighbour(Tile tile, int direction) {
		int[] coords = tile.getCoords();
		if (direction == 0) {
			return matTiles[coords[0]-1][coords[1]];
		}
		else if (direction == 1) {
			return matTiles[coords[0]][coords[1]-1];
		}
		else if (direction == 2) {
			return matTiles[coords[0]+1][coords[1]];
		}
		else if (direction == 3) {
			return matTiles[coords[0]][coords[1]+1];
		}
		else { //self, so direction==-1
			return tile;
		}
	}

	/*
	 * SAVING
	 */

	@Override
	public String toString() {
		return "GameState [remainingVictims=" + remainingVictims + ", remainingFalseAlarms=" + remainingFalseAlarms
				+ ", wallsDamaged=" + wallsDamaged + ", lostVictims=" + lostVictims + ", savedVictims=" + savedVictims
				+ ", remainingPoi=" + currentPoi + ", gameTerminated=" + gameTerminated + ", gameWon=" + gameWon
				+ ", activeFireFighterIndex=" + activeFireFighterIndex + ", isActiveGame=" + isActiveGame
				+ ", matEdges=" + Arrays.toString(matEdges) + ", matTiles=" + Arrays.toString(matTiles)
				+ ", currentTile=" + currentTile + ", availableActions=" + availableActions.stream().map(Object::toString).collect(Collectors.joining(", ")) + ", listOfFireFighter="
				+ listOfFireFighter.stream().map(Object::toString).collect(Collectors.joining(", ")) + ", MAX_WALL_DMGD=" + MAX_WALL_DMGD + ", poiList=" + Arrays.toString(poiList)
				+ "]";
	}

	// @James
	/**
	 * 
	 * @param template
	 */
	public void loadTemplate(TemplateGame template) {
		//this.matEdges = familyTemplate.getEdgeLocations();

		this.isActiveGame = true;

		initializeTiles();
//		initializeEdges(template);
//		initializeBasicTokens(template);


	}

	// @James
	/**
	 * Tokens such as fires and POI will be initialized unto the board. Modular design
	 * @param defaultGame
	 */
	public void initializeBasicTokens(/*TemplateGame template*/ int[][] tokenStarting) {
//		int[][] tokenLocations = template.getTokenLocations();
		int[][] tokenLocations = tokenStarting;
		
		// loops over the whole game board
		for (int i=0; i<8; i++) {
			for (int j=0; j<10; j++) {
				
				switch (tokenLocations[i][j]) {
				case 1: this.matTiles[i][j].setFire(2); 
					break;
				case 2: this.matTiles[i][j].addPoi(new POI(true));
					break;
				}
				
			}
		}
		
		
		
	}

}

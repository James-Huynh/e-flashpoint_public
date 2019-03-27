package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.Serializable;

import actions.Action;
import token.Vehicle;
import edge.Edge;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.Hazmat;
import token.Speciality;
import token.POI;
import token.Vehicle;
import game.BoardOne;
import lobby.Lobby;
import server.Player;
import edge.Door;
import edge.Wall;
import edge.Blank;

public class GameState implements Serializable {
	
	protected int remainingVictims; // start with 12 //10 for Family
	protected int remainingFalseAlarms; // start with 6 //5 for Family
	protected int wallsDamaged; // start with 0 upto MAX_WALL_DMGD
	protected int lostVictims; // if 4 lost lose!
	protected int savedVictims; // if 7 rescued win!
	// protected int currentPoi; //DO WE NEED THIS?
	// =remainingVictims+remainingFalseAlarms
	protected boolean gameTerminated;
	protected boolean gameWon;
	protected int activeFireFighterIndex;
	protected boolean isActiveGame;
	protected Edge[][] matEdges;
	protected Tile[][] matTiles;
	protected ParkingSpot[] engines;
	protected ParkingSpot[] ambulances;

	protected String advFireString;

	protected int currentTile; // ??
	protected Set<Action> availableActions;

	protected ArrayList<Player> listOfPlayers; // unsure if we need this as long as we have the firefighters, but the
												// player indexes will match the fireFighters for games where players do
												// not double up
	protected ArrayList<Firefighter> listOfFirefighters;
	public int MAX_WALL_DMGD = 24;
	protected ArrayList<POI> poiList; // no longer fixed, you can have less than 3 at any one time but no more than 3
										// will ever be generated and added to the board.
	protected ArrayList<POI> lostVictimsList;
	protected ArrayList<POI> savedVictimsList;
	protected ArrayList<POI> revealedFalseAlarmsList;
	protected ArrayList<Speciality> freeSpecialities;
	protected ArrayList<Hazmat> lostHazmat;
	protected ArrayList<Hazmat> disposedHazmat;

	protected int remainingHotSpots; //this includes all unplaced hotspots. gets initialized during game init. This does not include hotspots on the board.
	protected boolean experiencedMode;
	
	
	private static final long serialVersionUID = 1L; // serialization

	/**
	 * modified by @matekrk :) :)
	 */

	// good question - how to init the game.
	/*
	 * @matekrk: I think template is good approach. Either XML or JSON as a file of
	 * template but also it would be convenient - specially for debugging to have
	 * method reading all this crap as parameters we can help with that shit Below
	 * there is my approach towards SINGLETON!
	 * 
	 * Question 2 from @matekrk - remember in the end of the day this info is
	 * from... LOBBY! so maybe i can also have gamestate instance from lobby me:
	 * ¯\_(ツ)_/¯
	 */

	/*
	 * SINGLETON
	 */
	private GameState() {

	}

	// preventing from creating other instances
	private static final GameState instance = new GameState();

	// but everybody can access it
	public static GameState getInstance() {
		return instance;
	}

	/*
	 * UPDATE/LOADING
	 */

	// copy constructor
	public void updateGameStateFromObject(GameState gs1) {
		remainingVictims = gs1.remainingVictims;
		remainingFalseAlarms = gs1.remainingFalseAlarms;
		wallsDamaged = gs1.wallsDamaged;
		lostVictims = gs1.wallsDamaged;
		savedVictims = gs1.savedVictims;
		// currentPoi = gs1.currentPoi;
		gameTerminated = gs1.gameTerminated;
		gameWon = gs1.gameWon;
		activeFireFighterIndex = gs1.activeFireFighterIndex;
		isActiveGame = gs1.isActiveGame;
		matEdges = gs1.matEdges;
		matTiles = gs1.matTiles;
		currentTile = gs1.currentTile;
		availableActions = gs1.availableActions;
		listOfFirefighters = gs1.listOfFirefighters;
		MAX_WALL_DMGD = gs1.MAX_WALL_DMGD;
		poiList = gs1.poiList;
	}

	/*
	 * public void updateGameStateFromTemplate() { this.isActiveGame = true;
	 * 
	 * initializeTiles(); initializeEdges(); initializeFires(); }
	 */

	// ASSUME FOR DEMO THAT LOBBY GIVES ME ALWAYS FAMILY SETTINGS AND I HAVE LIST OF
	// USERS
	// added in basic family setting inits - ben
	public void updateGameStateFromLobby(Lobby lobby) {
		this.remainingVictims = 10;
		this.remainingFalseAlarms = 5;
		this.wallsDamaged = 0;
		this.lostVictims = 0;
		this.savedVictims = 0;
		this.gameTerminated = false;
		this.gameWon = false;
		this.activeFireFighterIndex = 0;
		this.isActiveGame = true;
		this.matEdges = new Edge[21][9];
		this.matTiles = new Tile[8][10];
		this.poiList = new ArrayList<POI>();
		this.revealedFalseAlarmsList = new ArrayList<POI>();
		this.lostVictimsList = new ArrayList<POI>();
		this.savedVictimsList = new ArrayList<POI>();
		this.freeSpecialities = new ArrayList<Speciality>();
		this.remainingHotSpots = 0;
		this.lostHazmat = new ArrayList<Hazmat>();
		this.disposedHazmat = new ArrayList<Hazmat>();
		if(lobby.getMode().equals("Experienced")) {
			this.experiencedMode = true;
		} else this.experiencedMode = false;
		createAmbulances();
		createEngine();
		initializeTiles();
		setClosest();
		
		

	}
	

	//this method checks if the door is an exterior door and opens it at game init.
	public void openExteriorDoors() {
		
		for (int j = 0; j < 10; j++) {
			if(matTiles[0][j].getEdge(3).isDoor()) matTiles[0][j].getEdge(3).change();
		}
		for (int j = 0; j < 10; j++) {
			if(matTiles[7][j].getEdge(1).isDoor()) matTiles[7][j].getEdge(1).change();
		}
		for (int i = 0; i < 8; i++) {
			if(matTiles[i][0].getEdge(2).isDoor()) matTiles[i][0].getEdge(2).change();
		}
		for (int i = 0; i < 8; i++) {
			if(matTiles[i][9].getEdge(0).isDoor()) matTiles[i][9].getEdge(0).change();
		}

		
	}

	public void initializeFire(int[][] fireLocations) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				if (fireLocations[i][j] == 2)
					this.matTiles[i][j].setFire(2);
				if(fireLocations[i][j] == 1)
					this.matTiles[i][j].setFire(1);
			}
		}

	}

	public void initializePOI(int[][] poiLocations) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				if (poiLocations[i][j] == 1) {
					POI addition = generatePOI();
					this.matTiles[i][j].addPoi(addition);
					updatePOI(addition);
				}
			}
		}

	}

	/*
	 * GETTERS
	 */

	// ALWAYS 3!
	public ArrayList<POI> retrievePOI() {
		return poiList;
	}

	public boolean isGameTerminated() {
		return gameTerminated;
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public ParkingSpot[] getAmbulances() {
		return ambulances;
	}

	public ParkingSpot[] getEngines() {
		return engines;
	}

	// QUESTION
	public Tile nextTile() {
		/* only advanced version right? */
		return null;
	}

	// QUESTION
	public int getCurrentPOI() {
		// what is that even?
		return poiList.size();
	}

	public Tile[][] getMatTiles() {
		return matTiles;
	}

	public Tile returnTile(int x, int y) {
		return matTiles[x][y];
	}

	public int getSavedVictims() {
		return savedVictims;
	}

	public ArrayList<Firefighter> getFireFighterList() {
		return this.listOfFirefighters;
	}

	public Firefighter getPlayingFirefighter() {
		/* TODO: No message view defined */
		return listOfFirefighters.get(activeFireFighterIndex);
	}

	public int getActiveFireFighterIndex() {
		return activeFireFighterIndex;
	}

	public ArrayList<POI> getLostVictimsList() {
		return this.lostVictimsList;
	}

	public ArrayList<POI> getSavedVictimsList() {
		return this.savedVictimsList;
	}

	public ArrayList<POI> getRevealedFalseAlarmsList() {
		return this.revealedFalseAlarmsList;
	}

	public int getDamageCounter() {
		return wallsDamaged;
	}

	public Set<Action> getAvailableActions() {
		return availableActions;
	}

	public ArrayList<Speciality> getFreeSpecialities(){
		return freeSpecialities;
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

//changed this class, we need to have an array list that is not of fixed size in order to make the advance fire work - ben
	public void updatePOI(POI toUpdate) {
		this.poiList.add(toUpdate);
//		for (int ind = 0; ind<poiList.length; ind++) {
//			if (poiList[ind].equals(null)) {
//				poiList[ind] = toUpdate;
//				break;
//			}
//		}
	}

	public void removePOI(POI toRemove) {
		this.poiList.remove(toRemove);
//		for (int ind = 0; ind<poiList.length; ind++) {
//			if (poiList[ind].equals(toRemove)) {
//				poiList[ind] = null;
//				break;
//			}
//		}
	}

	public void terminateGame() {
		this.gameTerminated = true;
	}

	public void updateActionList(Set<Action> newActionList) {
		this.availableActions = newActionList;
	}

	public void updateSavedCount(POI savedPoi) {
		savedVictims++;
		this.savedVictimsList.add(savedPoi);
	}

	public void updateLostCount(POI lostPoi) {
		lostVictims++;
		this.lostVictimsList.add(lostPoi);
	}

	public void updateRevealPOI(POI inputPOI) {
		// TODO Auto-generated method stub
		this.revealedFalseAlarmsList.add(inputPOI);
	}
	
	public void addDisposedHazmat(Hazmat h){
		this.disposedHazmat.add(h);
	}
	
	public void addLostHazmat(Hazmat h){
		this.lostHazmat.add(h);
	}

	public ArrayList<Hazmat> getDisposedHazmat(){
		return this.disposedHazmat;
	}
	
	public ArrayList<Hazmat> getLostHazmat(){
		return this.lostHazmat;
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

	public void setAllSpecialities() {
		for (Speciality s : token.Speciality.values()) {
			this.freeSpecialities.add(s);
		}
	}
	
	public void setFreeSpecialities(ArrayList<Speciality> sp) {
		this.freeSpecialities = sp;
	}
	
	//for command
	public void setPlayingFirefighter(Firefighter f) {
		int j = 0;
		for (int i=0; i < this.listOfFirefighters.size(); i++) {
			if (listOfFirefighters.get(i).equals(f)){
				j=i;
				break;
			}
		}
		this.activeFireFighterIndex = j;
	}

	// @James
	/**
	 * This method is used only once at the start of the game for each player.
	 * 
	 * @param f
	 * @param t
	 */
	public void placeFireFighter(Firefighter f, Tile t) {
		// listOfFireFighter.add(f);
		t.addToFirefighterList(f);
		f.setCurrentPosition(t);
	}
	
	public void setFirefighterSpeciality(Firefighter f, Speciality s) {
		freeSpecialities.add(f.getSpeciality());
		f.setSpeciality(s);
		freeSpecialities.remove(s);
	}

	// @matekrk + Zaid
	/**
	 * 
	 */
	public void createAmbulances() {
		this.ambulances = new ParkingSpot[4];
		this.ambulances[0] = new ParkingSpot(Vehicle.Ambulance, false);
		this.ambulances[1] = new ParkingSpot(Vehicle.Ambulance, false);
		this.ambulances[2] = new ParkingSpot(Vehicle.Ambulance, false);
		this.ambulances[3] = new ParkingSpot(Vehicle.Ambulance, false);
//		for (ParkingSpot forAmbulance : ambulances) {
//			forAmbulance = new ParkingSpot(Vehicle.Ambulance, false);
//		}
	}

	public void createEngine() {
		this.engines = new ParkingSpot[4];
		this.engines[0] = new ParkingSpot(Vehicle.Engine, false);
		this.engines[1] = new ParkingSpot(Vehicle.Engine, false);
		this.engines[2] = new ParkingSpot(Vehicle.Engine, false);
		this.engines[3] = new ParkingSpot(Vehicle.Engine, false);
//		for (ParkingSpot forEngine : engines) {
//			forEngine = new ParkingSpot(Vehicle.Engine, false);
//		}
	}
	
	public ArrayList<Player> getListOfPlayers(){
		return this.listOfPlayers;
	}
	
	public void setListOfPlayers(ArrayList<Player> playerList) {
		this.listOfPlayers = playerList;
	}
	
	public void setFirefighters() {
		this.listOfFirefighters = new ArrayList<Firefighter>();
		for (int i = 0; i < listOfPlayers.size(); i++) {
			if (this.listOfPlayers.get(i) != null) {
				Firefighter tempFirefighter = new Firefighter(this.listOfPlayers.get(i).getColour());
				tempFirefighter.setPlayer(this.listOfPlayers.get(i));
				this.listOfPlayers.get(i).setFirefighter(tempFirefighter);
				this.listOfFirefighters.add(tempFirefighter);
			}
		}
	}

	/**
	 * This method initializes 2D array of tiles and place ambulance/engine spot in
	 * the right place. It should be more general! Based on some template!!
	 */

	// updated tiles, parking spots need a little bit of work - ben
	public void initializeTiles() {

		// creating 10 x 8 tiles

		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 9; j++) {

				///// AMBULANCES -- 0 = left, 1 = up, 2 = right, 3 = bottom

				if (i == 0 && (j == 5 || j == 6)) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
					matTiles[i][j].setParkingSpot(ambulances[0]);
					ambulances[1].setTile(matTiles[i][j]);
				}

				else if ((i == 3 || i == 4) && j == 0) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
					matTiles[i][j].setParkingSpot(ambulances[3]);
					ambulances[0].setTile(matTiles[i][j]);
				}

				else if (i == 7 && (j == 3 || j == 4)) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
					matTiles[i][j].setParkingSpot(ambulances[1]);
					ambulances[3].setTile(matTiles[i][j]);
				}

				else if ((i == 3 || i == 4) && j == 9) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Ambulance);
					matTiles[i][j].setParkingSpot(ambulances[2]);
					ambulances[2].setTile(matTiles[i][j]);
				}

				///////// ENGINES

				else if (i == 0 && (j == 7 || j == 8)) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Engine);
					matTiles[i][j].setParkingSpot(engines[0]);
					engines[1].setTile(matTiles[i][j]);
				}

				else if ((i == 1 || i == 2) && j == 0) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Engine);
					matTiles[i][j].setParkingSpot(engines[3]);
					engines[0].setTile(matTiles[i][j]);
				}

				else if (i == 7 && (j == 1 || j == 2)) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Engine);
					matTiles[i][j].setParkingSpot(engines[1]);
					engines[3].setTile(matTiles[i][j]);
				}

				else if ((i == 6 || i == 5) && j == 9) {
					matTiles[i][j] = new Tile(false, new int[] { i, j });
					matTiles[i][j].setParkingType(Vehicle.Engine);
					matTiles[i][j].setParkingSpot(engines[2]);
					engines[2].setTile(matTiles[i][j]);
				}

				else if (i == 0 || i == 7 || j == 0 || j == 9) {
					matTiles[i][j] = new Tile(false, new int[] { i, j }); // create exterior tiles
				} else {
					matTiles[i][j] = new Tile(true, new int[] { i, j }); // create interior tiles
				}
			}
		}

	}

	/*
	 * This method indicates the closest ambulances and set its value complexity big
	 * O (n^5)
	 */
	public void setClosest() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 10; j++) {
				int minDistance = (int) Double.POSITIVE_INFINITY;
				for (ParkingSpot a : ambulances) {
					for (Tile t : a.getTiles()) {
						if (minDistance > Math.abs(t.getX() - i) + Math.abs(t.getY() - j)) {
							minDistance = (Math.abs(t.getX() - i) + Math.abs(t.getY() - j));
							returnTile(i, j).setNearestAmbulance(a);
						}
					}
				}
			}
		}
	}

	/**
	 * This method initializes Edges and puts them in to adjacentEdge array of each
	 * Tile.
	 */
	// updated the math for edges, the arrays were flipped - ben
	public void initializeEdges(/* TemplateGame template */ int[][] edgeStartingPos) {

//		int[][] edgePos = template.getEdgeLocations();
		int[][] edgePos = edgeStartingPos;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 21; j++) {
//				Tile tempTile;
				int edgeChecker = edgePos[i][j];

				if (edgeChecker == 2) {
					if ((j + 1) % 2 == 0) { // if left/right edge
						if (j == 1) {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Door());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][j / 2].setEdge(3, new Door());
							} else {
								Door temp = new Door();
								matTiles[i][j / 2].setEdge(1, temp);
								matTiles[i - 1][j / 2].setEdge(3, temp);
							}
						} else {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Door());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][j / 2 - 1].setEdge(3, new Door());
							} else {
								Door temp = new Door();
								matTiles[i][j / 2].setEdge(1, temp);
								matTiles[i - 1][j / 2].setEdge(3, temp);
							}
						}
					} else if ((j + 1) % 2 == 1) { // if top/down edge
						if (j == 0) { // if the edge has no left tile
							matTiles[i][j / 2].setEdge(0, new Door());
						} else if (j == 20) { // if the edge has no right tile
							matTiles[i][(j / 2) - 1].setEdge(2, new Door());
						} else {
							Door temp = new Door();
							matTiles[i][(j / 2) - 1].setEdge(2, temp);
							matTiles[i][(j / 2)].setEdge(0, temp);
						}
					}
				}

				if (edgeChecker == 1) {
					if ((j + 1) % 2 == 0) { // if left/right edge
						if (j == 1) {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Wall());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][(j / 2)].setEdge(3, new Wall());
							} else {
								Wall temp = new Wall();
								matTiles[i][j / 2].setEdge(1, temp);
								matTiles[i - 1][j / 2].setEdge(3, temp);
							}
						} else {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Wall());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][(j / 2) - 1].setEdge(3, new Wall());
							} else {
								Wall temp = new Wall();
								matTiles[i][j / 2].setEdge(1, temp);
								matTiles[i - 1][j / 2].setEdge(3, temp);
							}
						}
					} else if ((j + 1) % 2 == 1) { // if top/down edge
						if (j == 0) { // if the edge has no left tile
							matTiles[i][j].setEdge(0, new Wall());
						} else if (j == 20) { // if the edge has no right tile
							matTiles[i][(j / 2) - 1].setEdge(2, new Wall());
						} else {
							Wall temp = new Wall();
							matTiles[i][(j / 2) - 1].setEdge(2, temp);
							matTiles[i][(j / 2)].setEdge(0, temp);
						}
					}

				}
				if (edgeChecker == 0) {
					if ((j + 1) % 2 == 0) { // if left/right edge
						if (j == 1) {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Blank());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][(j / 2)].setEdge(3, new Blank());
							} else {
								Blank temp = new Blank();
								matTiles[i][(j / 2)].setEdge(1, temp);
								matTiles[i - 1][(j / 2)].setEdge(3, temp);
							}
						} else {
							if (i == 0) { // if the edge has no upper tile
								matTiles[i][j / 2].setEdge(1, new Blank());
							} else if (i == 8) { // if the edge has no lower tile
								matTiles[i - 1][(j / 2) - 1].setEdge(3, new Blank());
								if (j == 19) {
									matTiles[i - 1][9].setEdge(3, new Blank());
								}
							} else {
								Blank temp = new Blank();
								matTiles[i][(j / 2)].setEdge(1, temp);
								matTiles[i - 1][(j / 2)].setEdge(3, temp);
							}
						}
					} else if ((j + 1) % 2 == 1) { // if top/down edge
						if (j == 0) { // if the edge has no left tile
							matTiles[i][j].setEdge(0, new Blank());
						} else if (j == 20) { // if the edge has no right tile
							matTiles[i][(j / 2) - 1].setEdge(2, new Blank());
						} else {
							Blank temp = new Blank();
							matTiles[i][(j / 2) - 1].setEdge(2, temp);
							matTiles[i][(j / 2)].setEdge(0, temp);
						}
					}
				}

				// no idea why this is happening but temp fix for the last edge...

				// didnt cover case edgeChecker == -1
			}
		}

	}

	/*
	 * RANDOM
	 */

	public Tile rollForTile() {
		// random: should only ever be an interior tile
		Random r = new Random();
		// generates a row [1,6]
		int i = r.nextInt(6) + 1;
		// generates a column [1,8]
		int j = r.nextInt(8) + 1;

		return returnTile(i, j);
	}

	/*
	 * POIs
	 */

	// based on current state - randomly new POI
	public POI generatePOI() {
		int x = remainingFalseAlarms + remainingVictims;
		// if victims fall below 1
		if (x == 0) {
			return new POI(false);
		}
		Random r = new Random();
		int y = r.nextInt(x);
		POI newPOI;

		// shit: what if x==0? @matekrk

		if (y < remainingFalseAlarms) {
			newPOI = new POI(false);
			remainingFalseAlarms--;
		} else {
			newPOI = new POI(true);
			remainingVictims--;
		}
		return newPOI;
	}

	// unused method
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

	// left: 0, top: 1, right: 2, down: 3
	public Tile getNeighbour(Tile tile, int direction) {
		int[] coords = tile.getCoords();
		if (direction == 0) {
			if (coords[1] - 1 < 0) {
				return null;
			} else {
				return matTiles[coords[0]][coords[1] - 1];
			}
		} else if (direction == 1) {
			if (coords[0] - 1 < 0) {
				return null;
			} else {
				return matTiles[coords[0] - 1][coords[1]];
			}
		} else if (direction == 2) {
			if (coords[1] + 1 > 9) {
				return null;
			} else {
				return matTiles[coords[0]][coords[1] + 1];
			}
		} else if (direction == 3) {
			if (coords[0] + 1 > 7) {
				return null;
			} else {
				return matTiles[coords[0] + 1][coords[1]];
			}
		} else { // self, so direction==-1
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
				+ ", remainingPoi=" /* + currentPoi */ + ", gameTerminated=" + gameTerminated + ", gameWon=" + gameWon
				+ ", activeFireFighterIndex=" + activeFireFighterIndex + ", isActiveGame=" + isActiveGame
				+ ", matEdges=" + Arrays.toString(matEdges) + ", matTiles=" + Arrays.toString(matTiles)
				+ ", currentTile=" + currentTile + ", availableActions="
				+ availableActions.stream().map(Object::toString).collect(Collectors.joining(", "))
				+ ", listOfFireFighter="
				+ listOfFirefighters.stream().map(Object::toString).collect(Collectors.joining(", "))
				+ ", MAX_WALL_DMGD=" + MAX_WALL_DMGD + ", poiList="
				+ poiList.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
	}

	// @James
	/**
	 * 
	 * @param template
	 */
	public void loadTemplate(TemplateGame template) {
		// this.matEdges = familyTemplate.getEdgeLocations();

		this.isActiveGame = true;

		initializeTiles();
//		initializeEdges(template);
//		initializeBasicTokens(template);

	}

	/*
	 * // @James
	 *//**
		 * Tokens such as fires and POI will be initialized unto the board. Modular
		 * design
		 * 
		 * @param defaultGame
		 *//*
			 * public void initializeBasicTokens(TemplateGame template int[][]
			 * tokenStarting) { // int[][] tokenLocations = template.getTokenLocations();
			 * int[][] tokenLocations = tokenStarting;
			 * 
			 * // loops over the whole game board for (int i=0; i<8; i++) { for (int j=0;
			 * j<10; j++) {
			 * 
			 * switch (tokenLocations[i][j]) { case 1: this.matTiles[i][j].setFire(2);
			 * break; case 2: POI addition = generatePOI();
			 * this.matTiles[i][j].addPoi(addition); this.updatePOI(addition); break; }
			 * 
			 * } } }
			 */

	public void winGame() {
		// TODO Auto-generated method stub
		this.gameWon = true;
	}

	public void setTiles(Tile[][] matTiles2) {
		// TODO Auto-generated method stub
		this.matTiles = matTiles2;

	}

	public String getAdvFireString() {
		// TODO Auto-generated method stub
		return this.advFireString;
	}

	public void setAdvFireString(String newAdvFireString) {
		this.advFireString = newAdvFireString;
	}
	
	public int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public void setHotSpot(int updated) {
		this.remainingHotSpots = updated;
	}
	
	public int getHotSpot() {
		return this.remainingHotSpots;
	}
	
	public boolean isExperienced() {
		return this.experiencedMode;
	}
	
}

package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.Serializable;

import actions.Action;
import edge.Edge;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Vehicle;
import game.FamilyGame;
import edge.Door;
import edge.Wall;
import edge.Blank;

public class GameState implements Serializable {
    
    protected int remainingVictims; //start with 12  //10 for Family 
    protected int remainingFalseAlarms; //start with 6 //5 for Family
    protected int wallsDamaged; //start with 24
    protected int lostVictims; //if 4 lost lose!
    protected int savedVictims; //if 7 rescued win!
    protected int remainingPoi; //DO WE NEED THIS? =remainingVictims+remainingFalseAlarms
    protected boolean gameTerminated;
    protected boolean gameWon;
    protected int activeFireFighterIndex;
    protected boolean isActiveGame;
    protected Edge[][] matEdges;
    protected Tile[][] matTiles;
    
    protected int currentTile; // ??
    protected ArrayList<Action> availableActions;
    protected ArrayList<Firefighter> listOfFireFighter;
    public int MAX_WALL_DMGD;
    protected POI[] poiList; //@matekrk: this is important (fixed array) it is on purpose. 
    						 //you can't remove poi so easily. you need to replace with new one!
    private static final long serialVersionUID = 1L; //serialization
    
    
    /**
     * modified by @matekrk
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
    
    //@matekrk - private is key word here
    public GameState() {
    	/*
    	 * I wonder what arguments passed - question probably to init team
    	 */
    	
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
    	remainingPoi = gs1.remainingPoi;
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
    
    public void updateGameStateFromTemplate() {
    	this.isActiveGame = true;
    	
    	initializeEdges();
    	initializeFires();
    	initializeTiles();
    	
    }
    
    public void updateGameStateFromLobby() {
    	
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

    //@matekrk question about efficiency, do we need this?
    public void placeFireFighter(Firefighter f, Tile t) {
        f.setCurrentPosition(t);
    }
    
    public void initializeTiles() {
    	
    	//creating 10 x 8 tiles
    	
    	for (int i=0; i<=9; i++) {
    		for (int j=0; j<=7; j++) {
    			int[] position = {i,j};
    			if((i==0 && (j==5||j==6))||(j==0 && (i==3||i==5)) || (i==9 && (j==3||j==4)) || (j==7 && (i==3||i==4)) )
    				matTiles[i][j] = new ParkingSpot(true, position, Vehicle.Ambulance );
    			else if((i==0 && (j==7||j==8)) || (j==0 && (i==1||i==2)) || (i==9 && (i==5||i==6)) || (j==7 && (i==1||i==2)) ) 
    				matTiles[i][j] = new ParkingSpot(true, position, Vehicle.Engine );
    			else{ 
    				matTiles[i][j] = new Tile(false, position);
    			}
    		}
    	}
    	
    	//
    	
    	//creating parking spots 
    }

    public void initializeEdges() {
    	
    	FamilyGame template = new FamilyGame();
    	int[][] edgePos = template.getEdgeLocations();
    	
    	for (int i=0; i<=20; i++) {
    		for (int j=0; j<=8; j++) {
    			
    			int edgeChecker = edgePos[i][j];
    			
    			if (edgeChecker == 2) {
    				if (i%2 == 1) { // if left/right edge
    					if (j == 0) { //if the edge has no upper tile
    						matTiles[i/2][j-1].setEdge(3, new Door());
    					}
    					else if(j == 8){ //if the edge has no lower tile
    						matTiles[i/2][j].setEdge(1, new Door());
    					}
    					else {
    					matTiles[i/2][j].setEdge(1, new Door());
    					matTiles[i/2][j-1].setEdge(3, new Door());
    					}
    				}
    				else if (i%2 == 0) { //if top/down edge
    					if(i==0) { //if the edge has no left tile
    						matTiles[i/2][j].setEdge(2, new Door());
    					}
    					else if(i==20) { //if the edge has no right tile
    						matTiles[(i/2)-1][j].setEdge(0, new Door());
    					}
    					else {
    					matTiles[i/2][j].setEdge(2, new Door());
    					matTiles[(i/2)-1][j].setEdge(0, new Door());
    					}
    				}
    			}
    				
    			if (edgeChecker == 1) {
    				if (i%2 == 1) { // if left/right edge
    					if (j == 0) { //if the edge has no upper tile
    						matTiles[i/2][j-1].setEdge(3, new Wall());
    					}
    					else if(j == 8){ //if the edge has no lower tile
    						matTiles[i/2][j].setEdge(1, new Wall());
    					}
    					else {
    					matTiles[i/2][j].setEdge(1, new Wall());
    					matTiles[i/2][j-1].setEdge(3, new Wall());
    					}
    				}
    				else if (i%2 == 0) { //if top/down edge
    					if(i==0) { //if the edge has no left tile
    						matTiles[i/2][j].setEdge(2, new Wall());
    					}
    					else if(i==20) { //if the edge has no right tile
    						matTiles[(i/2)-1][j].setEdge(0, new Wall());
    					}
    					else {
    					matTiles[i/2][j].setEdge(2, new Wall());
    					matTiles[(i/2)-1][j].setEdge(0, new Wall());
    					}
    				}
    				
    			}
    			if (edgeChecker == 0){
    				if (i%2 == 1) { // if left/right edge
    					if (j == 0) { //if the edge has no upper tile
    						matTiles[i/2][j-1].setEdge(3, new Blank());
    					}
    					else if(j == 8){ //if the edge has no lower tile
    						matTiles[i/2][j].setEdge(1, new Blank());
    					}
    					else {
    					matTiles[i/2][j].setEdge(1, new Blank());
    					matTiles[i/2][j-1].setEdge(3, new Blank());
    					}
    				}
    				else if (i%2 == 0) { //if top/down edge
    					if(i==0) { //if the edge has no left tile
    						matTiles[i/2][j].setEdge(2, new Blank());
    					}
    					else if(i==20) { //if the edge has no right tile
    						matTiles[(i/2)-1][j].setEdge(0, new Blank());
    					}
    					else {
    					matTiles[i/2][j].setEdge(2, new Blank());
    					matTiles[(i/2)-1][j].setEdge(0, new Blank());
    					}
    				}
    			}
    			//didnt cover case edgeChecker == -1
    		}
    	}
    	
    	
    }

    public void initializeFires(TemplateGame defaultGame) {
        /* TODO: No message view defined */
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
				+ ", remainingPoi=" + remainingPoi + ", gameTerminated=" + gameTerminated + ", gameWon=" + gameWon
				+ ", activeFireFighterIndex=" + activeFireFighterIndex + ", isActiveGame=" + isActiveGame
				+ ", matEdges=" + Arrays.toString(matEdges) + ", matTiles=" + Arrays.toString(matTiles)
				+ ", currentTile=" + currentTile + ", availableActions=" + availableActions.stream().map(Object::toString).collect(Collectors.joining(", ")) + ", listOfFireFighter="
				+ listOfFireFighter.stream().map(Object::toString).collect(Collectors.joining(", ")) + ", MAX_WALL_DMGD=" + MAX_WALL_DMGD + ", poiList=" + Arrays.toString(poiList)
				+ "]";
	}
}

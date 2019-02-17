package game;

import java.util.ArrayList;
import java.util.Random;

import actions.Action;
import edge.Edge;
import tile.Tile;
import token.Firefighter;
import token.POI;

public class GameState {
    
    protected int remainingVictims; //start with 12
    protected int remainingFalseAlarms; //start with 6
    protected int wallsDamaged; //start with 26
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
    
    //@matekrk - private is key word here
    private GameState() {
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
    
    
    public void updateGameStateFromTemplate(TemplateGame template) {
    	this.isActiveGame = true;
    	
    	initializeEdges(template);
    	initializeFires(template);
    	initializeTiles(template);
    	
    }
    
    public void updateGameStateFromLobby() {
    	
    }

    public Tile rollForTile() {
        //random:
    	Random r = new Random();
    	int i = r.nextInt(matTiles.length);
    	int j = r.nextInt(matEdges[0].length);
    	return returnTile(i,j);
    }

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
    
    public void setActiveFireFighterIndex(int i) {
    	activeFireFighterIndex = i;
    }
    
    public int getDamageCounter() {
    	return wallsDamaged;
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

    public void initializeTiles(TemplateGame defaultGame) {
        /* TODO: No message view defined */
    }

    public void initializeEdges(TemplateGame defaultGame) {
        /* TODO: No message view defined */
    }

    public void initializeFires(TemplateGame defaultGame) {
        /* TODO: No message view defined */
    }
    
    public ArrayList<Action> getAvailableActions(){
    	return availableActions;
    }
  
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
}

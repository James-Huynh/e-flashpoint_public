package game;

import java.util.ArrayList;

import actions.Action;
import edge.Edge;
import tile.Tile;
import token.Firefighter;
import token.POI;

public class GameState {
    
    protected int remainingVictims;
    protected int remainingFalseAlarms;
    protected int wallsDamaged;
    protected int lostVictims;
    protected int savedVictims;
    protected int remainingPoi;
    protected int activeFireFighterIndex;
    protected boolean isActiveGame;
    protected Edge[][] matEdges;
    protected Tile[][] matTiles;
    protected int currTile;
    protected ArrayList<Action> availableActions;
    protected ArrayList<Firefighter> listOfFireFighter;
    public int MAX_WALL_DMGD = 30;
    
    
    // @James	
    /**
     * modified by @matekrk
     */
    
    public GameState() {
    	
    }
    
    public GameState(TemplateGame template) {
    	this.isActiveGame = true;
    	
    	initializeEdges(template);
    	initializeFires(template);
    	initializeTiles(template);
    	
    }

    public Tile rollForTile() {
        /* TODO: No message view defined */
        return null;
    }

    public POI retrievePOI() {
        return null;
    }

    public boolean isGameTerminated() {
        /* TODO: No message view defined */
        return false;
    }

    public Tile nextTile() {
        /* TODO: No message view defined */
        return null;
    }

    public int getCurrentPOI() {
        /* TODO: No message view defined */
        return 0;
    }

    public Tile returnTile(int x, int y) {
        /* TODO: No message view defined */
        return null;
    }

    public int getVictims() {
        /* TODO: No message view defined */
        return 0;
    }

    public Firefighter getFireFighterList() {
        /* TODO: No message view defined */
        return null;
    }

    public Firefighter getPlayingFirefighter() {
        /* TODO: No message view defined */
        return listOfFireFighter.get(activeFireFighterIndex);
    }

    public int getDamageCounter() {
        /* TODO: No message view defined */
        return 0;
    }

    public void updateDamageCounter() {
        /* TODO: No message view defined */
    }

    public void updatePOI() {
        /* TODO:fined */
    }

    public void setSavedCount(int saveCount) {
        /* TODO: No message view defined */
    }

    public void setIsActiveGame(boolean isActiveGame) {
     
    }

    public void placeFireFighter(Firefighter f, Tile t) {
        /* TODO: No message view defined */
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
  //WHY HERE
    boolean containsAvailableActions(GameState a) {
        /* TODO: No message view defined */
        return a.getAvailableActions().contains(a);
    }

    int sizeOfAvailableActions(GameState a) {
        /* TODO: No message view defined */
        return a.getAvailableActions().size();
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
    	else { //self
    		return tile;
    	}
    }
}

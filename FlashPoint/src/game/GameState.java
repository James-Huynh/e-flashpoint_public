package game;

import edge.Edge;
import tile.Tile;
import token.Firefighter;
import token.Poi;

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
    
    
    // @James	
    /**
     * 
     */
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

    public Poi retrievePOI() {
        /* TODO: No message view defined */
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

    public Firefighter playingFireFighter() {
        /* TODO: No message view defined */
        return null;
    }

    public int getDamageCounter() {
        /* TODO: No message view defined */
        return 0;
    }

    public void updateDamageCounter() {
        /* TODO: No message view defined */
    }

    public void updatePOI() {
        /* TODO: No message view defined */
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
}

package actions;

import edge.Door;
import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;


public class Handle extends Action{
	
	protected ActionList title = ActionList.Handle;

	protected int direction;
    
	public Handle(int direction) {
		this.APcost = 1;
		this.direction = direction;
	}
	
	
    public int getDirection() {
        return direction;
    }
    
    public ActionList getTitle() {
    	return this.title;
    }
	
    @Override
	public boolean validate(GameState gs) {
		boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Edge edge = currentPosition.getEdge(direction);
        
        if( edge.isDoor() ) {
        	if ( edge.isDestroyed() == false) { //Check if door is destroyed, currently not implemented
        		flag = true;
        	}
        }
        
		return flag;
	}
	
    @Override
	public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Edge door = currentPosition.getEdge(direction);
        door.change();
	}
    
    @Override
	public String toString() {
		return "Handle [direction=" + direction + ", APcost=" + APcost + "]";
	}
}
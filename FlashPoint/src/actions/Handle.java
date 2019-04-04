package actions;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Speciality;


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
        if (playingFirefighter.getSpeciality() == Speciality.DOG) {
			return false;
		}
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Edge edge = currentPosition.getEdge(direction);
        int cost = super.getCost();
        int AP = playingFirefighter.getAP();
        int fire = currentPosition.getFire();

        if(fire == 2 && AP ==1) {
        	return false;
        }
        
        if( edge.isDoor() ) {
        	if ( edge.isDestroyed() == false) { //Check if door is destroyed, currently not implemented
        		if(AP >= cost) {
        			flag = true;
        		}
        	}
        }
        
		return flag;
	}
	
    @Override
	public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Edge door = currentPosition.getEdge(direction);
        int cost = super.getCost();
        int AP = playingFirefighter.getAP();
        door.change();
        playingFirefighter.setAP(AP - cost);
        playingFirefighter.setUsedAP(true);
	}
    
    @Override
	public void adjustAction(GameState gs) {
		
	}
    
    @Override
	public String toString() {
		return "Handle [title=" + title + ", direction=" + direction + ", APcost=" + APcost + "]";
	}
}
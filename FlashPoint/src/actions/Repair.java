package actions;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Speciality;

public class Repair extends Action {

	 protected int direction;
	 protected ActionList title = ActionList.Chop;
	 private static final long serialVersionUID = 1L;
	
	public Repair(int direction, int cost) {
		this.APcost = cost;
    	this.direction = direction;
    	this.title = ActionList.Repair;
	}

	public int getDirection() {
        return direction;
    }
    
    public ActionList getTitle() {
    	return this.title;
    }
	
	@Override
	public void perform(GameState gs) {
		Firefighter current = gs.getPlayingFirefighter();
		Edge edge = current.getCurrentPosition().getEdge(this.direction);
		
		current.setAP(current.getAP() - APcost);
		gs.updateDamageCounterRepair();
		edge.build();
		current.setUsedAP(true);
		
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
		
		Firefighter current = gs.getPlayingFirefighter();
		Edge edge = current.getCurrentPosition().getEdge(this.direction);
		
    	if (current.getSpeciality() == Speciality.BOBTHEBUILDER) {
    		if (edge.isWall()) {
    			if (current.getAP() >= APcost) {
    				if (edge.getDamage() + APcost <= 2 && gs.getDamageCounter() >= 0 ) {
    					flag = true;
    				}
    			}
    		}
    	}
		
		return flag;
	}

	@Override
	public void adjustAction(GameState gs) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Repair [direction=" + direction + ", title=" + title + ", APcost=" + APcost + "]";
	}
	
	

}

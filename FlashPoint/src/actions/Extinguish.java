package actions;

// Start of user code for imports
// Trying to push
import java.util.*;
// End of user code

import game.GameState;
import token.Firefighter;
import tile.Tile;
import edge.Edge;

/**
 * Extinguish class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Extinguish extends Action {
    
    protected int direction;
    protected ActionList title = ActionList.Extinguish;
    
    public Extinguish() {
    	this.APcost = 1;
    }
    
    public Extinguish(int direction, int cost) {
    	this.direction = direction;
    	this.APcost = cost;
    }
    
    /*
     * GETTERS
     */
    
    public int getDirection() {
        return direction;
    }
    
    public ActionList getTitle() {
    	return this.title;
    }

    /*
     * @OVERRIDE
     */
    
    @Override
    public boolean validate(GameState gs) {
        boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
        int cost = super.getCost();
        
        //We cannot extinguish by 2 where have only smoke
        int fire = neighbour.getFire();

        if (fire >= 1 && fire >= cost) { 
            if (currentPosition.equals(neighbour)) { //in other words: direction -1
                if (aP >= cost) {
                    flag = true;
                }
            }
            else {
            	Edge edge = currentPosition.getEdge(this.direction);
            	if (edge.isBlank()) {
                    if (aP >= cost) {
                        flag = true;
                    }
            	}
            	else if (edge.isDoor()) {
                    boolean status = edge.getStatus();
                    if (status == true) {
                        if (aP >= cost) {
                            flag = true;
                        }
                    }
                } 
            	else if (edge.isWall()) { 
                    int damage = edge.getDamage();
                    if (damage == 0) {
                        if (aP >= cost) {
                            flag = true;
                        }
                    }
                }
            }
        } 
        return flag;
    }

    @Override
    public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        Tile neighbour = gs.getNeighbour(currentPosition, direction);
        int prevFire = neighbour.getFire();
        neighbour.setFire(prevFire - this.APcost);
    }

	@Override
	public String toString() {
		return "Extinguish [direction=" + direction + "] by AP = " + this.APcost + "."; 
		//Add "Fire was converted to smoke/..", another level of detail that might help in debugging and game terminal
	}
    
    
}

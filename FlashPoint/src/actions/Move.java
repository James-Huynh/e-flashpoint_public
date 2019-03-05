package actions;

import java.util.ArrayList;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;

/**
 * Move class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Move extends Action {

	protected int direction;
	protected ActionList title = ActionList.Move;
	
	public Move(int direction) {
		this.direction = direction;
		this.APcost = 1;
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
        Tile neighbour = gs.getNeighbour(currentPosition, direction);
        int fire = neighbour.getFire();
        int aP = playingFirefighter.getAP();
        
        if ( edge.isDoor() ) {
        	boolean status = edge.getStatus();
        	if(status == true) {
        		if (fire < 2) {
        			if( aP >= 1) {
        				flag = true;
        			}
        		}
        		
        	   else if (fire == 2 && aP >= 2) {
        		   flag = true;
        		   this.APcost = 2;
        	   }
        		
        	}
        }
        
        else if( edge.isBlank() ) {
        	if( fire < 2 && aP >= 1) {
        		flag = true;
        	}
        	
        	else if( fire == 2 && aP >= 2) {
        		flag = true;
        		this.APcost = 2;
        	}
        }
        
        else if( edge.isWall() ) {
        	int damage = edge.getDamage();
        	if(damage == 0) {
        		if( fire < 2 && aP >= 1) {
        			flag = true;
        		}
        		
        		else if( fire == 2 && aP >= 2) {
        				flag = true;
        				this.APcost = 2;
        			}
        		}
        }
        return flag;
    }

    @Override
    public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        
        Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
        playingFirefighter.setCurrentPosition(neighbour);
        currentPosition.removeFromFirefighterList(playingFirefighter);
        neighbour.addToFirefighterList(playingFirefighter);
        
        if(neighbour.containsPOI() == true) {
        	ArrayList<POI> Pois = neighbour.getPoiList();
        	for(POI poi:Pois) {
        		if(!poi.isVictim()) { //Why? POIs should get revealed if victim or not
        			poi.reveal();
        		}
        	}
        }
        
        //Do when move saves the victim
    }
    
    @Override
	public String toString() {
		return "Move [direction=" + direction + ", APcost=" + APcost + "]";
	}
}

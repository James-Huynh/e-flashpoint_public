package actions;

import java.util.ArrayList;

import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;

public class MoveWithVictim extends Move{
	
	protected ActionList title = ActionList.MoveWithVictim;
	
	public MoveWithVictim(int direction){
		super(direction);
		this.APcost = 2;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	@Override
	public boolean validate(GameState gs) {
		boolean normalMove = super.validate(gs);
		/*
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		int aP = playingFirefighter.getAP();
		if (normalMove && playingFirefighter.getCarrying() && aP >= 2 ) {
			return true;
		}
		else {
			return false;
		}*/
		
		if(normalMove) {
			Firefighter playingFirefighter = gs.getPlayingFirefighter();
			int aP = playingFirefighter.getAP();
			Tile currentPosition = playingFirefighter.getCurrentPosition();
			if(currentPosition.containsPOI()) {
				ArrayList<POI> pois = currentPosition.getPoiList();
				for(POI p: pois) {
					if(p.isVictim() && p.isRevealed() && aP >= 2) {
						return true;
					}
				}
			}
		}
		return false;

	}
	
	@Override
	public void perform(GameState gs) {
		super.perform(gs);
		
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
		//boolean carrying = playingFirefighter.getCarrying(); //Is this check necessary? The action is Move w Victim - Z
		Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
		
		ArrayList<POI> pois = currentPosition.getPoiList(); //This is under the assumption we can have only
		for(POI p: pois) { 									//one victim POI on a Tile at any time
			if(p.isVictim() && p.isRevealed()) {
				currentPosition.removeFromPoiList(p);
				neighbour.addPoi(p);
			}
		}
        /*if (carrying == true) {
        	currentPosition.removeFromPoiList( playingFirefighter.getVictim() );
        	neighbour.addPoi( playingFirefighter.getVictim() );
        }*/
        
	}

	@Override
	public String toString() {
		return "MoveWithVictim [direction=" + direction + ", APcost=" + APcost + "]";
	}
	
	
}

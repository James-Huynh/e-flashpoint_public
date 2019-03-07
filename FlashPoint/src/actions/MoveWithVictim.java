package actions;

import java.util.ArrayList;

import edge.Edge;
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
	        Edge edge = currentPosition.getEdge(direction);
	        Tile neighbour = gs.getNeighbour(currentPosition, direction);
	        int fire = neighbour.getFire();			
			if(currentPosition.containsPOI()) {
				ArrayList<POI> pois = currentPosition.getPoiList();
				if(fire < 2) {
					if(edge.isDoor()) {
						if(edge.getStatus() == true) {
							for(POI p: pois) {
								if(p.isVictim() && p.isRevealed() && aP >=2) {
									return true;
								}
							}
						}
					}
					else if(edge.isWall()) {
						if(edge.getDamage() == 0) {
							for(POI p: pois) {
								if(p.isVictim() && p.isRevealed() && aP >=2) {
									return true;
								}
							}
						}
					}
					else if(edge.isBlank()) {
						for(POI p:pois) {
							if(p.isVictim() && p.isRevealed() && aP >=2) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void perform(GameState gs) {

		
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
		//boolean carrying = playingFirefighter.getCarrying(); //Is this check necessary? The action is Move w Victim - Z
		Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
		
		ArrayList<POI> pois = currentPosition.getPoiList(); //This is under the assumption we can have only

		//switch to this from the loop, because you will never be in a space with an unrevealed poi and there fore they will always be a victim. 
		if(!neighbour.checkInterior()) {
			POI temp = pois.remove(0);
			gs.removePOI(temp);
			gs.updateSavedCount(temp);
		}else {
			neighbour.addPoi(pois.remove(0));
		}
		
		
//		for(POI p: pois) { 									//one victim POI on a Tile at any time
//			if(p.isVictim() && p.isRevealed()) {			//
//				currentPosition.removeFromPoiList(p);
//				neighbour.addPoi(p);
//			}
//		}
		super.perform(gs);
        /*if (carrying == true) {							//carrying property is Firefighter is of no use yet
        	currentPosition.removeFromPoiList( playingFirefighter.getVictim() ); //If want to model multiple victims on one Tile and want to access a particular one
        	neighbour.addPoi( playingFirefighter.getVictim() );				//Then it's useful - however would have to be careful to reset property when Firefighter opts to just 'Move'
        }*/
        
	}

	@Override
	public String toString() {
		return "MoveWithVictim [direction=" + direction + ", APcost=" + APcost + "]";
	}
	
	
}

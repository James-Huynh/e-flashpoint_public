package actions;

import java.util.ArrayList;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;
import token.Vehicle;

public class MoveWithVictim extends Move{
	
	private static final long serialVersionUID = 1L;
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

		if(normalMove) {
			Firefighter playingFirefighter = gs.getPlayingFirefighter();
			int aP = playingFirefighter.getAP();
			Tile currentPosition = playingFirefighter.getCurrentPosition();
	        Edge edge = currentPosition.getEdge(direction);
	        Tile neighbour = gs.getNeighbour(currentPosition, direction);
	        int fire = neighbour.getFire();		
	        
	        if (playingFirefighter.getSpeciality() == Speciality.DOG) {
				this.APcost = 4;
				if (edge.isWall() && edge.getDamage() == 1) { //dog cannot combine sqeeze and drag.
					return false;
				}
			}
	        
			if(currentPosition.containsPOI()) {
				ArrayList<POI> pois = currentPosition.getPoiList();
				if(fire < 2) {
					if(edge.isDoor()) {
						if(edge.getStatus() == true || edge.isDestroyed()) {
							for(POI p: pois) {
								if(p.isVictim() && p.isRevealed() && aP >= APcost) {
									return true;
								}
							}
						}
					}
					else if(edge.isWall()) {
						if(edge.getDamage() == 0) {
							for(POI p: pois) {
								if(p.isVictim() && p.isRevealed() && aP >= APcost) {
									return true;
								}
							}
						}
					}
					else if(edge.isBlank()) {
						for(POI p:pois) {
							if(p.isVictim() && p.isRevealed() && aP >= APcost) {
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
		Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
		
		ArrayList<POI> pois = currentPosition.getPoiList(); //This is under the assumption we can have only

		//switch to this from the loop, because you will never be in a space with an unrevealed poi and there fore they will always be a victim.
		if (!gs.isExperienced()) {
			if(!neighbour.checkInterior()) {
				POI temp = pois.remove(0);
				gs.removePOI(temp);
				gs.updateSavedCount(temp);
			}
			else {
				neighbour.addPoi(pois.remove(0)); //smart
			}
		}
		else {
			if (neighbour.getParkingSpot() != null) {
				if (neighbour.getParkingSpot().getParkingType() == Vehicle.Ambulance && neighbour.getParkingSpot().getCar()) {
					POI temp = pois.remove(0);
					gs.removePOI(temp);
					gs.updateSavedCount(temp);
				}
			}
			else {
				neighbour.addPoi(pois.remove(0)); //smart
			}
		}
		super.perform(gs);
	}

	@Override
	public void adjustAction(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		if (playingFirefighter.getSpeciality() == Speciality.DOG) {
			this.APcost = 4;
		}
	}
	
	@Override
	public String toString() {
		return "MoveWithVictim [title=" + title + ", direction=" + direction + ", APcost=" + APcost + "]";
	}
	
	
}

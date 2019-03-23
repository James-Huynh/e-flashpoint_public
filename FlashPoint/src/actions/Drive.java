package actions;

import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Vehicle;

public class Drive extends Action {
	
	protected int direction;
	protected boolean moveWith;
	protected Vehicle vehicle;
	protected ActionList title = ActionList.Drive;
	
	public Drive() {
		// TODO Auto-generated constructor stub
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	public Drive(int dir, boolean flag, Vehicle type) {
		direction = dir;
		moveWith = flag;
		vehicle = type;
		if(direction == 0) {
			APcost = 4;
		}
		else {
			APcost = 2;
		}
	}


	@Override
	public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        //Tile destination = Vehicle.getTile().getOtherParkingSpots()[direction];
        //vehicle.setTile(destination);
        
        /* Ride */
        //ParkingSpots[] others = Vehicle.getTile().getOtherParkingSpots;
        if(vehicle == Vehicle.Engine) {
        	//for(ParkingSpot in others){
       			//for(tile in ParkingSpot.getTiles()) {
        			//if(tile.containsFirefighter) {
        				//Prompt Firefighter if he wants to travel also
        			//}
        		//} 		
        }
        
        if(vehicle == Vehicle.Engine || moveWith == true) {
        	//playingFirefighter.setCurrPos(destination);
        }
        
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        
		if(vehicle == Vehicle.Ambulance) {
			if(aP >= APcost) {
				flag = true;
			}
		}
		else {
			Tile currentPosition = playingFirefighter.getCurrentPosition();
			if(aP >= APcost) {
				//If the vehicle is on the same location as FF. I think Engine/Ambulance should be an object
				//because then we can do E.ambulance.currentPos == currentPos
					flag = true;
			}
		}
		
		return flag;
	}

	@Override
	public void adjustAction(GameState gs) {
		
	}

	@Override
	public String toString() {
		return "Drive [direction=" + direction + ", moveWith=" + moveWith + ", vehicle=" + vehicle + ", title=" + title
				+ "]";
	}
	
	
}

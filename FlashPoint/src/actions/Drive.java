package actions;

import java.util.HashSet;
import java.util.Set;

import game.GameState;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.Vehicle;

public class Drive extends Action {
	
	private static final long serialVersionUID = 1L;
	protected int direction; // 1 clockwise -1 anti-clockwise
	protected boolean moveWith;
	protected Set<Firefighter> moveWithWho;
	protected ParkingSpot parking;
	protected ActionList title = ActionList.Drive;
	
	public Drive() {
		APcost = 2;
	}
	/** -- For constructors
	 * Similar to MoveWithVictim & RemoveHazmat, the Action factory does not have access to the ParkingSpot
	 * So they cannot be passed in the constructor. We have to 'get' them ourselves
	 * Same thing for HashSet of moving Firefighters
	 * 
	 * We do however need some kind of indicator in constructor to tell which Vehicle is to be Driven
	 * Either take in String/ActionList?
	 */
	public Drive(ParkingSpot parking, int direction) {
		APcost = 2;
		this.parking = parking;
		this.direction = direction;
	}	
	
	public Drive(ParkingSpot parking, int direction, boolean moveWith, HashSet<Firefighter> moveWithWho) {
		APcost = 2;
		this.parking = parking;
		this.direction = direction;
		this.moveWith = moveWith;
		this.moveWithWho = moveWithWho;
	}	
	
	
	public ActionList getTitle() {
    	return this.title;
    }


	@Override
	public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        
        //calling ambulance
        if (!currentPosition.getParkingSpot().equals(parking)) {
        	ParkingSpot nextAmbulance = null;
        	if (this.parking.getParkingType() == Vehicle.Ambulance) { //always (assure)
    	        for (int i=0; i<4; i++) {
    	        	if (gs.getAmbulances()[i].equals(parking)) {
    	        		nextAmbulance = gs.getAmbulances()[ (i+direction)%4 ];
    	        		nextAmbulance.setCar(true);
    	        		parking.setCar(false);
    	        	}
    	        }
            }
        	
        	/** --For below--
        	 * Input will have to be taken for each Firefighter that is on an Ambulance ParkingSpot
        	 * So it will be more like a loop over all Ambulance PS's -> make Ride request -> add them to HashSet
        	 * Add the end probably, we will loop over this HashSet and reset their positions
        	 */
        	if (moveWith) {  
        		for (Firefighter f : moveWithWho) {
        			int whichOne;
        			if (parking.getTiles()[0].equals(f.getCurrentPosition())){
        	        	whichOne = 0;
        	        }
        	        else {
        	        	whichOne = 1;
        	        }
        			f.getCurrentPosition().getFirefighterList().remove(f);
        			f.setCurrentLocation(nextAmbulance.getTiles()[whichOne]);
        		}
        	}
        }
       
        
        //normal driving
        int whichOne;
        if (parking.getTiles()[0].equals(currentPosition)){
        	whichOne = 0;
        }
        else {
        	whichOne = 1;
        }
        
        Tile target = null;
        if (this.parking.getParkingType() == Vehicle.Ambulance) { 
	        for (int i=0; i<4; i++) {
	        	if (gs.getAmbulances()[i].equals(parking)) {
	        		ParkingSpot nextAmbulance = gs.getAmbulances()[ (i+direction)%4 ];
	        		nextAmbulance.setCar(true);
	        		parking.setCar(false);
	        		target = nextAmbulance.getTiles()[whichOne];
	        	}
	        }
        }
        else {
        	for (int i=0; i<4; i++) {
	        	if (gs.getEngines()[i].equals(parking)) {
	        		ParkingSpot nextEngine = gs.getEngines()[ (i+direction)%4 ];
	        		target = nextEngine.getTiles()[whichOne];
	        		nextEngine.setCar(true);
	        		parking.setCar(false);
	        		target = nextEngine.getTiles()[whichOne];
	        	}
	        }
        }
        /** --For moveWith
         * moveWith was supposed to say if the playingFF wants to Move or not. So in Engine instance, it would be set to true and in Ambulance,
         * there'd be one instance of both? (if we don't want to get into input taking, we can validate both and display both)
         * So when resetting positions, we have to check moveWith since it won't happen always
         */
        currentPosition.getFirefighterList().remove(playingFirefighter);
        playingFirefighter.setCurrentLocation(target);
        
        //so far not ready for riding ambulance
        if (moveWith) {
        	for (Firefighter f : moveWithWho) {
        		if (f.getCurrentPosition().equals(currentPosition)){
        			f.getCurrentPosition().getFirefighterList().remove(f);
        			f.setCurrentPosition(target);
        		}
        		else {
        			f.getCurrentPosition().getFirefighterList().remove(f);
        			for (Tile tt : target.getParkingSpot().getTiles()) {
        				if (!tt.equals(target)) {
        					f.setCurrentLocation(tt);
        					break;
        				}
        			}
        		}
        	}
        }
        
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        
		if(parking.getParkingType() == Vehicle.Ambulance) {
			ParkingSpot nextAmbulance = null;
			for (int i=0; i<4; i++) {
	        	if (gs.getAmbulances()[i].equals(parking)) {
	        		nextAmbulance = gs.getAmbulances()[ (i+direction)%4 ];
	        	}
			}
			if(aP >= APcost && !nextAmbulance.getCar()) {
				flag = true;
			}
		}
		
		else {
			Tile currentPosition = playingFirefighter.getCurrentPosition();
			ParkingSpot nextEngine = null;
			for (int i=0; i<4; i++) {
	        	if (gs.getAmbulances()[i].equals(parking)) {
	        		nextEngine = gs.getEngines()[ (i+direction)%4 ];
	        	}
			}
			if(currentPosition.getParkingSpot().equals(parking) && aP >= APcost && !nextEngine.getCar()) { //One more check than above, why?
					flag = true;
			}
		}
		
		return flag;
		
		/**
		 * Logically, moving from PS to opposite PS will require (4AP), so two Drive Actions. 
		 * So far, we have incorporated all such things as one option (e.g. Chop by 4) so it would be nice to do this also
		 * If so, APcost will initialized accordingly. But the 'next' check in validate will require edit as you worry about 
		 * two after, not the next one. Same thing in perform
		 */
	}

	@Override
	public void adjustAction(GameState gs) {
		
	}

	@Override
	public String toString() {
		return "Drive [direction=" + direction + ", moveWith=" + moveWith + ", parking=" + parking + ", title=" + title
				+ ", APcost=" + APcost + "]";
	}
	
	
}

package actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import game.GameState;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;
import token.Vehicle;

/**
 * 
 * @author zaidyahya
 * For Mat - assume that obtainTravellers() will put only those Firefighters that have said YES to travelling in the 'travellers' attribute
 * So, just look at the code for moving these travellers and confirm if it works (I did not want to touch it since it is your logic)
 * For both cases; when in-turn player has called and when in-turn player is riding 
 */

public class Drive extends Action {
	
	private static final long serialVersionUID = 1L;
	protected int direction; // 1 clockwise -1 anti-clockwise
	protected boolean moveWith;
	protected Set<Firefighter> travellers;
	protected boolean isAmbulance;
	protected ActionList title = ActionList.Drive;
	protected int index;
	
	public Drive() {
		APcost = 2;
	}
	
	public Drive(int i, int direction, boolean moveWith, boolean typeFlag) {
		APcost = 2;
		this.index = i;
		this.direction = direction;
		this.moveWith = moveWith;
		this.isAmbulance = typeFlag;
		this.travellers = new HashSet<Firefighter>();
	}	
	
	public ActionList getTitle() {
    	return this.title;
    }


	@Override
	public void perform(GameState gs) {
        Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        playingFirefighter.setUsedAP(true);
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        
        ParkingSpot parking;
        if(isAmbulance) {
        	parking = gs.getAmbulances()[this.index];
        }
        else {
        	parking = gs.getEngines()[this.index];
        }
        
        //calling ambulance
        if (!moveWith) {
        	ParkingSpot nextAmbulance = gs.getAmbulances()[ (index+direction+4)%4 ];
    	    nextAmbulance.setCar(true);
    	    parking.setCar(false);
    	    for (int j=0; j<=1; j++) {
    			if (!nextAmbulance.getTiles()[j].getPoiList().isEmpty()) {
    				for (int ind=0; ind<nextAmbulance.getTiles()[j].getPoiList().size(); ind++) {
    					//assume victims!
    					POI temp = nextAmbulance.getTiles()[j].getPoiList().remove(ind);
    					gs.removePOI(temp);
    					gs.updateSavedCount(temp);
    				}
    			}
    		}
        	
        	obtainTravellers(gs);
        	
        	if (!travellers.isEmpty()) {  // with people
        		for (Firefighter f : travellers) {
        			int whichOne;
        			if (parking.getTiles()[0].equals(f.getCurrentPosition())){
        	        	whichOne = 0;
        	        }
        	        else {
        	        	whichOne = 1;
        	        }
        			f.getCurrentPosition().getFirefighterList().remove(f);
        			f.setCurrentLocation(nextAmbulance.getTiles()[whichOne]);
        			nextAmbulance.getTiles()[whichOne].addToFirefighterList(f);
        			if (!f.getCanDodge()) {
        	        	gs.vicinity(f);
        	        }
        		}
        	}
        }
        
      //normal driving
        else {
            int whichOne;
            if (parking.getTiles()[0].equals(currentPosition)){
            	whichOne = 0;
            }
            else {
            	whichOne = 1;
            }
            
            Tile target = null;
            if (parking.getParkingType() == Vehicle.Ambulance) { 
    	        for (int i=0; i<4; i++) {
    	        	if (gs.getAmbulances()[i].equals(parking)) {
    	        		ParkingSpot nextAmbulance = gs.getAmbulances()[ (i+direction+4)%4 ];
    	        		nextAmbulance.setCar(true);
    	        		parking.setCar(false);
    	        		target = nextAmbulance.getTiles()[whichOne];
    	        		for (int j=0; j<=1; j++) {
    	        			if (!nextAmbulance.getTiles()[j].getPoiList().isEmpty()) {
    	        				for (int ind=0; ind<nextAmbulance.getTiles()[j].getPoiList().size(); ind++) {
    	        					//assume victims!
    	        					POI temp = nextAmbulance.getTiles()[j].getPoiList().remove(ind);
    	        					gs.removePOI(temp);
    	        					gs.updateSavedCount(temp);
    	        				}
    	        			}
    	        		}
    	        	}
    	        }
            }
            else {
        		ParkingSpot nextEngine = gs.getEngines()[ (index+direction+4)%4 ];
        		target = nextEngine.getTiles()[whichOne];
        		nextEngine.setCar(true);
        		parking.setCar(false);
        		target = nextEngine.getTiles()[whichOne];
            }
            currentPosition.getFirefighterList().remove(playingFirefighter);
            playingFirefighter.setCurrentLocation(target);
            target.addToFirefighterList(playingFirefighter);
            if (!playingFirefighter.getCanDodge()) {
            	gs.vicinity(playingFirefighter);
            }
            
            obtainTravellers(gs);
            
            if (!travellers.isEmpty()) {
            	for (Firefighter f : travellers) {
            		if (f.getCurrentPosition().equals(currentPosition)){ //the same tile as FF who does the action
            			f.getCurrentPosition().getFirefighterList().remove(f);
            			f.setCurrentPosition(target);
            			target.addToFirefighterList(f);
            			if (!f.getCanDodge()) {
            	        	gs.vicinity(f);
            	        }
            		}
            		else {
            			f.getCurrentPosition().getFirefighterList().remove(f);
            			for (Tile tt : target.getParkingSpot().getTiles()) {
            				if (!tt.equals(target)) { //tt is second tile!
            					f.getCurrentPosition().getFirefighterList().remove(f);
            					f.setCurrentLocation(tt);
            					tt.addToFirefighterList(f);
            					if (!f.getCanDodge()) {
                    	        	gs.vicinity(f);
                    	        }
            					break;
            				}
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
        if (playingFirefighter.getSpeciality() == Speciality.DOG) {
			return false;
		}
        int aP = playingFirefighter.getAP();
        
        ParkingSpot parking;
        if(isAmbulance) {
        	parking = gs.getAmbulances()[this.index];
        }
        else {
        	parking = gs.getEngines()[this.index];
        }
        
		if(parking.getParkingType() == Vehicle.Ambulance) {
			ParkingSpot nextAmbulance = gs.getAmbulances()[(index+direction+4) % 4];
			
			if(moveWith) {
				Tile currentPosition = playingFirefighter.getCurrentPosition();
				if(currentPosition.getParkingSpot() != null) {
					if(currentPosition.getParkingSpot().equals(parking)) {
						if(aP >= APcost && !nextAmbulance.getCar() && parking.getCar()) {
							flag = true;
						}
					}
				}
			}
			else {
				if(aP >= APcost && !nextAmbulance.getCar()) {
					flag = true;
				}
			}
		}
		
		else {
			Tile currentPosition = playingFirefighter.getCurrentPosition();
			ParkingSpot nextEngine = null;
			for (int i=0; i<4; i++) {
	        	if (gs.getEngines()[i].equals(parking)) {
	        		nextEngine = gs.getEngines()[ (i+direction+4)%4 ];
	        	}
			}
			if(currentPosition.getParkingSpot() != null) {
				if(currentPosition.getParkingSpot().equals(parking)) {
					if(aP >= APcost && !nextEngine.getCar()) {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}
	
	//Mat: all good! but where/how do you set the value for gs.rideMap?
	public void obtainTravellers(GameState gs) {
//		Map<Firefighter, Boolean[]> rideMap = gs.getRideMapper();
	    Iterator<Entry<Firefighter, Boolean[]>> it = gs.getRideMapper().entrySet().iterator();
	    while (it.hasNext()) {
	    	System.out.println("Iterating over rideMapper");
	        Map.Entry<Firefighter, Boolean[]> pair = it.next();
	        System.out.println(pair.getValue()[0]);
	        System.out.println(pair.getValue()[1]);
	        if(pair.getValue()[1] == true) {
	        	travellers.add(pair.getKey());
	        }
//	        it.remove(); // avoids a ConcurrentModificationException, don't know if necessary? -Mat: garbage collector, don't worry
	    }
	    System.out.println(gs.getRideMapper().size());
	    System.out.println("this is the size of travelers" + travellers.size());
	}

	@Override
	public void adjustAction(GameState gs) {
		
	}
	
	@Override
	public boolean isAmbulance() {
		return this.isAmbulance;
	}
	
	@Override
	public boolean canMove() {
		return this.moveWith;
	}
	
	@Override 
	public int getDirection() {
		return this.direction;
	}

	@Override
	public String toString() {
		return "Drive [direction=" + direction + ", moveWith=" + moveWith + ", title=" + title
				+ ", APcost=" + APcost + "]";
	}
	
	
}
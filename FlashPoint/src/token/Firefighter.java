package token;

import java.io.Serializable;

import server.Player;
import tile.ParkingSpot;
import tile.Tile;

/**
 * 
 * @author James
 * modified by @matekrk
 */
//removed abstraction to allow a firefighter to be initialized elsewhere - ben
public class Firefighter extends Token implements Serializable {

	protected int AP;
	protected int savedAP;
	protected boolean carryingVictim;
	//protected POI victim;
	//protected Player owner;
	protected Colour myColour;
	protected Player myPlayer;
	public Speciality speciality;
	protected int SP;
	protected boolean canDodge; // be careful! dog canDodge can be true but in validation it is false!
	protected boolean usedAP; //Experience point
	private static final long serialVersionUID = 1L;
	protected POI carrying;
	protected boolean ifCommandCAPSthisTurn;
	
	
	//constr
	
	public Firefighter(/*Player newOwner*/Colour setColour) {
		//this.owner = newOwner;
		super();
		this.AP = 4;
		this.savedAP = 0;
		this.myColour = setColour;
		this.speciality = null;
		this.SP = 0;
		this.canDodge = false;
		this.usedAP = false;
		this.carrying = null;
		this.ifCommandCAPSthisTurn = false;
		//Colour = Player.getColour();
	}
	
	public Firefighter(Colour setColour, Speciality speciality) {
		super();
		this.savedAP = 0;
		this.myColour = setColour;
		this.speciality = speciality;
		if (speciality == Speciality.CAPTAIN) {
			this.AP = 4;
			this.SP = 2;
			
		}
		else if (speciality == Speciality.CAFS) {
			this.AP = 3;
			this.SP = 3;
		}
		else if (speciality == Speciality.GENERALIST) {
			this.AP = 5;
			this.SP = 0;
		}
		else if (speciality == Speciality.RESCUE_SPECIALIST) {
			this.AP = 4;
			this.SP = 3;
		}
		else if (speciality == Speciality.DOG) {
			this.AP = 12;
			this.SP = 0;
		}
		else if (speciality == Speciality.VETERAN) {
			this.AP = 4; 
			this.SP = 0; 
			this.canDodge = true;
		}
		else {
			this.AP = 4;
			this.SP = 0;
		}
		
		this.carrying = null;
		this.ifCommandCAPSthisTurn = false;
		
	}

	//------------------------ GETTERS -------------------------//
	public int getAP() {
		return this.AP;
	}

	public Colour getColour() {
		return this.myColour;
	}
	
	public int getSavedAP() {
		return this.savedAP;
	}
	
	public Player getOwner() {
		return this.myPlayer;
	}

	public boolean getCarrying() {
		return this.carryingVictim;
	}
	
	//public POI getVictim() {
	//	return victim;
	//}
	
	public Speciality getSpeciality() {
		return speciality;
	}
	
	public int getSP() {
		return this.SP;
	}
	
	public POI getCarriedPOI() {
		return this.carrying;
	}
	
	public boolean getIfCommandCAPSthisTurn() {
		return this.ifCommandCAPSthisTurn;
	}
	
	public boolean getCanDodge() {
		return canDodge;
	}
	
	public boolean getUsedAP() {
		return usedAP;
	}
	
	//------------------------ SETTTERS -------------------------//
	public void setAP(int AP) {
		this.AP = AP;
	}

	public void setSavedAP(int AP) {
		this.savedAP = AP;
	}
	
	//public void setVictim(POI victim) {
	//	this.victim = victim;
	//}
	
	public void setPlayer(Player player) {
		// TODO Auto-generated method stub
		this.myPlayer = player;
	}
	
	public void setColour(Colour c) {
		this.myColour = c;
	}

	/**
	 * updateLocation sets the coordinates of the firefighter to the tile
	 * @param respawnTile The tile that the firefighter is on
	 */
	public void updateLocation(ParkingSpot respawnTile) {
		this.x = respawnTile.getTiles()[0].getX(); // @matekrk - (imo) this algo should return the closest tile 
													// with non-null pointer to parkingSpot
		this.y = respawnTile.getTiles()[0].getY();
		tileOn = respawnTile.getTiles()[0];
	}

	public void setCurrentLocation(Tile target) {
		this.x = target.getX();
		this.y = target.getY();
		tileOn = target;
	}
	
	public void setSpeciality (Speciality speciality) {
		this.speciality = speciality;
		if (speciality == Speciality.CAPTAIN) {
			this.AP = 4;
			this.SP = 2;
			this.ifCommandCAPSthisTurn = false;
			
		}
		else if (speciality == Speciality.CAFS) {
			this.AP = 3;
			this.SP = 3;
		}
		else if (speciality == Speciality.GENERALIST) {
			this.AP = 5;
			this.SP = 0;
		}
		else if (speciality == Speciality.RESCUE_SPECIALIST) {
			this.AP = 4;
			this.SP = 3;
		}
		else if (speciality == Speciality.DOG) {
			this.AP = 12;
			this.SP = 0;
		}
		else if (speciality == Speciality.VETERAN) {
			this.AP = 4;
			this.SP = 0;
			this.canDodge = true;
		}
		else {
			this.AP = 4;
			this.SP = 0;
		}
	}
	
	public void setSP(int SP) {
		this.SP = SP;
	}
	
	public void setCanDodge(boolean canDodge) {
		this.canDodge = canDodge;
	}
	
	public void setUsedAP(boolean usedAP) {
		this.usedAP = usedAP;
	}
	
	public void setCarriedPOI(POI victim) {
		carrying = victim;
	}
	
	public void setIfCommandCAPSthisTurn(boolean value) {
		ifCommandCAPSthisTurn = value;
	}
	
	// end of turn ---- big update in terms of logic - actually it is beginning of turn!
	
	public void endOfTurn() {
		if (speciality == Speciality.CAPTAIN) {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(8, this.AP + 4);
			this.SP = 2;
			this.canDodge = false;
			this.ifCommandCAPSthisTurn = false;
		}
		else if (speciality == Speciality.CAFS) {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(8, this.AP + 3);
			this.SP = 3;
			this.canDodge = false;
		}
		else if (speciality == Speciality.GENERALIST) {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(8, this.AP + 5);
			this.SP = 0;
			this.canDodge = false;
		}
		else if (speciality == Speciality.RESCUE_SPECIALIST) {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(8, this.AP + 4);
			this.SP = 3;
			this.canDodge = false;
		}
		else if (speciality == Speciality.DOG) {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(18, this.AP + 12);
			this.SP = 0;
			this.canDodge = false;
		}
		else if (speciality == Speciality.VETERAN) {
			this.AP = Math.min(8, this.AP + 4);
			this.SP = 0;
		}
		else {
			if (canDodge && !usedAP) {
				this.AP = Math.max(0, AP-1);
			}
			this.AP = Math.min(8, this.AP + 4);
			this.SP = 0;
			this.canDodge = false;
		}
		this.setUsedAP(usedAP);
	}
	

	@Override
	public String toString() {
		return "Firefighter [AP=" + AP + ", savedAP=" + savedAP + ", carryingVictim=" + carryingVictim + ", victim=" +
				//victim.toString() + ", myColour=" + 
				myColour.toString() + ", speciality=" 
				+ speciality.toString() + ", SP="
				+ SP + ", canDodge=" + canDodge + ", usedAP=" + usedAP + ", carrying=" + carrying
				+ ", ifCommandCAPSthisTurn=" + ifCommandCAPSthisTurn + ", x=" + x + ", y=" + y
				+ "]";
	}

}

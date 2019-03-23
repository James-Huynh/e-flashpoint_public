package token;

import server.Player;
import tile.ParkingSpot;
import tile.Tile;

/**
 * 
 * @author James
 * modified by @matekrk
 */
//removed abstraction to allow a firefighter to be initialized elsewhere - ben
public class Firefighter extends Token {

	protected int AP;
	protected int savedAP;
	protected boolean carryingVictim;
	protected POI victim;
	//protected Player owner;
	protected Colour myColour;
	protected Player myPlayer;
	public Speciality speciality;
	protected int SP;
	private static final long serialVersionUID = 1L;
	protected POI follow;
	
	
	//constr
	
	public Firefighter(/*Player newOwner*/Colour setColour) {
		//this.owner = newOwner;
		super();
		this.AP = 4;
		this.savedAP = 0;
		this.myColour = setColour;
		this.speciality = null;
		this.SP = 0;
		//Colour = Player.getColour();
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
	
	public POI getVictim() {
		return victim;
	}
	
	public Speciality getSpeciality() {
		return speciality;
	}
	
	public int getSP() {
		return this.SP;
	}
	
	public POI getPOI() {
		return this.follow;
	}
	
	//------------------------ SETTTERS -------------------------//
	public void setAP(int AP) {
		this.AP = AP;
	}

	public void setSavedAP(int AP) {
		this.savedAP = AP;
	}
	
	public void setVictim(POI victim) {
		this.victim = victim;
	}
	
	public void setPlayer(Player player) {
		// TODO Auto-generated method stub
		this.myPlayer = player;
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
	}
	
	public void setSP(int SP) {
		this.SP = SP;
	}
	
	public void setFollow(POI victim) {
		follow = victim;
	}
	

	@Override
	public String toString() {
		return "Firefighter [AP=" + AP + ", savedAP=" + savedAP + ", carryingVictim=" + carryingVictim + ", victim="
				+ victim + ", myColour=" + myColour + ", myPlayer=" + myPlayer + ", speciality=" + speciality + ", SP="
				+ SP + ", x=" + x + ", y=" + y + ", tileOn=" + tileOn + "]";
	}

}

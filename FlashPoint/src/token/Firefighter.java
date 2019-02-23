package token;

import java.util.*;

import tile.ParkingSpot;
import tile.Tile;

/**
 * 
 * @author James
 * modified by @matekrk
 */
abstract public class Firefighter extends Token {

	protected int AP;
	protected int savedAP;
	protected boolean carryingVictim;
	protected POI victim;

	/**
	 * updateLocation sets the coordinates of the firefighter to the tile
	 * @param respawnTile The tile that the firefighter is on
	 */
	public void updateLocation(ParkingSpot respawnTile) {
		this.x = respawnTile.getTiles()[0].getX(); // @matekrk - (imo) this algo should return the closest tile 
													// with non-null pointer to parkingSpot
		this.y = respawnTile.getTiles()[0].getY();
	}


	//------------------------ GETTERS -------------------------//
	public int getAP() {
		return this.AP;
	}

	public int getSavedAP() {
		return this.savedAP;
	}
	
	

	public boolean getCarrying() {
		return this.carryingVictim;
	}
	
	public POI getVictim() {
		return victim;
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


	@Override
	public String toString() {
		return "Firefighter [AP=" + AP + ", savedAP=" + savedAP + ", carryingVictim=" + carryingVictim + ", victim="
				+ victim.toString() + "]";
	}

}

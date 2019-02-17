package token;

import java.util.*;

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
	 * @param target The tile that the firefighter is on
	 */
	public void updateLocation(Tile target) {
		this.x = target.getCoords()[0];
		this.y = target.getCoords()[1];
	}


	//------------------------ GETTERS -------------------------//
	public int getAP() {
		return this.AP;
	}

	public int getSavedAP() {
		return this.savedAP;
	}
	
	public Tile getCurrentPosition() {
		return this.tileOn;
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
	
	public void setCurrentPosition(Tile tile) {
		this.tileOn = tile;
	}
	
	public void setVictim(POI victim) {
		this.victim = victim;
	}

}

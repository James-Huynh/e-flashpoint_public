package token;

import java.util.*;

import tile.Tile;

/**
 * 
 * @author James
 *
 */
abstract public class Firefighter extends Token {

	protected int AP;
	protected int savedAP;
	protected boolean carryingVictim;

	/**
	 * 
	 * @param target
	 */
	public void updateLocation(Tile target) {
		this.x = target.getCoords()[0];
		this.y = target.getCoords()[1];
	}

	public int getAP() {
		return this.AP;
	}

	public int getSavedAP() {
		return this.savedAP;
	}

	public void setAP(int AP) {
		this.AP = AP;
	}

	public void setSavedAP(int AP) {
		this.savedAP = AP;
	}

	public Tile getCurrentPos() {
		/* TODO: No message view defined */
		return null;
	}

	public boolean getCarrying() {
		/* TODO: No message view defined */
		return false;
	}
}

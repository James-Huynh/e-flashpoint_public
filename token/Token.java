package token;

import tile.Tile;

abstract public class Token {
	int x;
	int y;

	Tile tileOn;
	
	/*
	 * GETTER
	 */
	public Tile getCurrentPosition() {
		return this.tileOn;
	}
	/*
	 * SETTER
	 */
	
	public void setCurrentPosition(Tile tile) {
		this.tileOn = tile;
	}
	
}

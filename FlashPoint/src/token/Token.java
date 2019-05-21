package token;

import java.io.Serializable;

import tile.Tile;


//removed the abstraction, I wasn't sure how to implement a working firefighter elsewhere with it in... will bring up to group. Ben
public class Token implements Serializable {
	private static final long serialVersionUID = 1L;
	int x;
	int y;

	Tile tileOn;
	
	public Token() {
		this.x=0;
		this.y=0;
		tileOn = null;
	}
	
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
		this.x = tile.getX();
		this.y = tile.getY();
		this.tileOn = tile;
	}
	
}

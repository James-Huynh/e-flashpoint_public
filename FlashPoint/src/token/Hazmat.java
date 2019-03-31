package token;

import tile.Tile;

public class Hazmat extends Token {

	private static final long serialVersionUID = 1L;
	private boolean disposed;
	
	public Hazmat() {
		disposed = false;
	}

	public void setCurrentLocation(Tile target) {
		this.x = target.getX();
		this.y = target.getY();
		tileOn = target;
		
		if(!target.checkInterior()) {
			disposed = true;
			//Needs to be destroyed
		}
	}
	
	public void setDisposed() {
		this.disposed = !disposed;
	}

	@Override
	public String toString() {
		return "Hazmat [disposed=" + disposed + ", x=" + x + ", y=" + y + ", tileOn=" + tileOn.toString() + "]";
	}
	
}

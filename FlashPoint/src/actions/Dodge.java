package actions;

import game.GameState;
import token.Speciality;

public class Dodge extends Move {
	
	private static final long serialVersionUID = 1L;

	public Dodge(int direction) {
		super(direction);
		this.APcost = 1;
	}

	@Override
	public void perform(GameState gs) {
		if (direction==0) {
			return;
		}
		else {
			super.perform(gs);
		}
		// FF should be able to click if they want to stay or move.
	}

	@Override
	public boolean validate(GameState gs) {
		boolean sup = super.validate(gs);
		
		return (gs.getPlayingFirefighter().getSpeciality() == Speciality.VETERAN && sup);
		
		//comment: so POI should die when fire appears - so no need to deal with pick/drop or move with victim/hazmat.
	}

	@Override
	public void adjustAction(GameState gs) {

	}

	@Override
	public String toString() {
		return "Dodge [direction=" + direction + ", title=" + title + ", APcost=" + APcost + "]";
	}
	
	

}

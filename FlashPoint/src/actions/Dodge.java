package actions;

import game.GameState;
import token.Speciality;

public class Dodge extends Move {
	
	private static final long serialVersionUID = 1L;

	public Dodge(int direction) {
		super(direction);
		this.APcost = 1;
		this.title = ActionList.Dodge;
	}
	

	@Override
	public void perform(GameState gs) {
		if (direction==0) {
			// no AP removal!
			return;
		}
		else {
			//if NOT RESCUE SPECIALIST
			super.perform(gs);
			//if rescue then take only from AP not from SP!! ambig.
		}
		// FF should be able to click if they want to stay or move.
		
		gs.getPlayingFirefighter().setCanDodge(false);
		gs.vicinity(gs.getPlayingFirefighter());
		gs.getPlayingFirefighter().setUsedAP(true);
		
		/*
		 * Comment
		 * if double advance fire and FF was knocked out twice
		 * he is eligible to dodge providing he is nearby (in neighbourhood to Veteran)
		 */
	}

	@Override
	public boolean validate(GameState gs) {
		boolean sup = super.validate(gs);
		
		if (gs.getNeighbour(gs.getPlayingFirefighter().getCurrentPosition(),direction).getFire() == 2) {
			return false;
		}
		
		if (gs.getPlayingFirefighter().getSpeciality() != Speciality.VETERAN) {
			this.APcost = 2;
		}
		
		if (gs.getPlayingFirefighter().getSpeciality() == Speciality.DOG) {
			return false;
		}
		
		return (gs.getPlayingFirefighter().getCanDodge() && sup);
		
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

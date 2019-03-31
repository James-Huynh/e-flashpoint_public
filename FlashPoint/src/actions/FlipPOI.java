package actions;

import java.util.ArrayList;

import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;

public class FlipPOI extends Action {

	private static final long serialVersionUID = 1L;
	protected ActionList title = ActionList.Flip;
	protected POI victim;
	protected int[] tileLocation;
	
	public FlipPOI(POI victim, int[] loc) {
		this.victim = victim;
		this.APcost = 1;
		this.tileLocation = loc;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	@Override
	public void perform(GameState gs) {
		gs.getPlayingFirefighter().setAP(gs.getPlayingFirefighter().getAP() - APcost);
		if (victim.isVictim()) {
			victim.reveal();
		}
		else {
			gs.returnTile(tileLocation[0], tileLocation[1]).removeFromPoiList(victim);;
			gs.getRevealedFalseAlarmsList().add(victim);
			gs.removePOI(victim);
		}
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		int aP = playingFirefighter.getAP();
		System.out.println(playingFirefighter.getSpeciality());
		if (playingFirefighter.getSpeciality() == (Speciality.IMAGING_TECHNICIAN)) {
			if (!victim.isRevealed()) {
				if (aP >= APcost) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void adjustAction(GameState gs) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int[] getTileLocation() {
		return this.tileLocation;
	}

}

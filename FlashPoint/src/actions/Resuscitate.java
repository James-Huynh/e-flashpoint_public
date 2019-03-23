package actions;

import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;

public class Resuscitate extends Action{

	private static final long serialVersionUID = 1L;
	protected POI victim;
	protected ActionList title = ActionList.Reusciate;
	
	public Resuscitate() {
		this.APcost = 1;
	}
	
	public Resuscitate(POI victim) {
		this.victim = victim;
	}
	
	@Override
	public void perform(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
		victim.setResuscitate(playingFirefighter);
		playingFirefighter.setAP(aP - APcost);
		playingFirefighter.setFollow(victim);
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        if (playingFirefighter.speciality.equals(Speciality.PARAMEDIC)) {
        	if (currentPosition.getPoiList().contains(victim)){
        		if (aP >= APcost) {
        			if (!victim.isResuscitated()) {
        				return true;
        			}
        		}
        	}
        }
		return false;
	}

	@Override
	public void adjustAction(GameState gs) {
		
	}

	@Override
	public String toString() {
		return "Resuscitate [victim=" + victim + ", title=" + title + ", APcost=" + APcost + ", direction=" + direction
				+ "]";
	}

	
}

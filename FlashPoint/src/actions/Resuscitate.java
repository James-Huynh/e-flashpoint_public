package actions;

import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.POI;
import token.Speciality;

public class Resuscitate extends Action{

	private static final long serialVersionUID = 1L;
	protected POI victim;
	protected ActionList title;
	
	public Resuscitate() {
		this.APcost = 1;
	}
	
	public Resuscitate(POI victim) {
		this.victim = victim;
		this.title = ActionList.Resuscitate;
		this.APcost = 1;
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
        if (playingFirefighter.speciality == (Speciality.PARAMEDIC)) {
        	if (currentPosition.getPoiList().contains(victim)){
        		System.out.println("Returning right POI");
        		if (aP >= APcost) {
        			if (!victim.isResuscitated()) {
        				System.out.println("Returning true");
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

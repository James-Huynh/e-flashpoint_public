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
	protected int[] tileLocation;
	protected int index;
	
	public Resuscitate() {
		this.APcost = 1;
	}
	
	public Resuscitate(POI victim, int[] loc) {
		this.victim = victim;
		this.title = ActionList.Resuscitate;
		this.APcost = 1;
		this.tileLocation = loc;
	}
	
	public Resuscitate(int i) {
		this.APcost = 1;
		this.index = i;
		this.title = ActionList.Resuscitate;
	}
	
	public ActionList getTitle() {
		return title;
	}
	
	@Override
	public void perform(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
//		victim.setResuscitate(playingFirefighter);
		playingFirefighter.setAP(aP - APcost);
//		playingFirefighter.setFollow(victim);
		playingFirefighter.getCurrentPosition().getPoiList().get(index).heal();
	}

	@Override
	public boolean validate(GameState gs) {
		//victim = gs.returnTile(tileLocation[0], tileLocation[1]).getPoiList().get(0);
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
//        if (playingFirefighter.speciality == (Speciality.PARAMEDIC)) {
//        	if (currentPosition.getPoiList().contains(victim)){
//        		if (aP >= APcost) {
//        			if (!victim.isResuscitated()) {
//        				System.out.println("Returning true in Resuscitate");
//        				return true;
//        			}
//        		}
//        	}
//        }
        
        //New one
        if (playingFirefighter.speciality == (Speciality.PARAMEDIC)) {
        		if (aP >= APcost) {
        			if (!currentPosition.getPoiList().get(index).isHealed()) {
        				System.out.println("Returning true in Resuscitate");
        				return true;
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

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
	protected Tile t;
	
	public FlipPOI(POI victim) {
		this.victim = victim;
		this.APcost = 1;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	@Override
	public void perform(GameState gs) {
		//cannot be in fire
		gs.getPlayingFirefighter().setAP(gs.getPlayingFirefighter().getAP() - APcost);
    	victim.reveal();
    	
    	/* THAT'S ALL?
    	Tile t = this.victim.getCurrentPosition();
    	ArrayList<POI> Pois = t.getPoiList();
    	POI toDiscover = Pois.get(Pois.size()-1);
    	if(!poi.isRevealed()){
  		  	if(poi.isVictim()){
    	ArrayList<POI> POIStoRemove = new ArrayList<POI>();
    	for(POI poi:Pois) {
    		if(!poi.isRevealed()){
    		  if(poi.isVictim()){
    		   	poi.reveal();
    		  } else {
    			POIStoRemove.add(poi);
    		  }
    		}
    	}
    	for(POI poi:POIStoRemove) {
    		gs.removePOI(poi);
    		Pois.remove(poi);
    		gs.getRevealedFalseAlarmsList().add(poi);
    	}
    	*/

	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		int aP = playingFirefighter.getAP();
		
		if (playingFirefighter.getSpeciality() == (Speciality.IMAGING_TECHNICIAN)) {
			if (victim.isVictim() && !victim.isRevealed()) {
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

}

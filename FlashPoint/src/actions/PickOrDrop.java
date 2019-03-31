package actions;

import game.GameState;
import token.Firefighter;
import token.POI;
import token.Speciality;

public class PickOrDrop extends Action {

	private static final long serialVersionUID = 1L;
	protected ActionList title;
	protected POI healedVictim;
	protected int index;

	public PickOrDrop(int i) {
		APcost = 0;
		this.index = i;
	}
	
	public PickOrDrop(POI healedVictim) {
		APcost = 0;
		this.healedVictim = healedVictim;
	}

	@Override
	public void perform(GameState gs) {
		Firefighter f = gs.getPlayingFirefighter();
		if (title == ActionList.Drop){
			f.setCarriedPOI(null);
			healedVictim.setLeader(null);
//			healedVictim.setResuscitate(null);
		}
		else {
			f.setCarriedPOI(healedVictim);
			healedVictim.setLeader(f);
//			healedVictim.setResuscitate(f);
		}
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter f = gs.getPlayingFirefighter();
		healedVictim = f.getCurrentPosition().getPoiList().get(index);
		
//		if (f.getSpeciality() == Speciality.PARAMEDIC) {
//			if (f.getFollow() != null) { //to drop
//				title = ActionList.Drop;
//				return true;
//			}
//			else if (f.getFollow() == null && healedVictim.isResuscitated()) { //to pick
//				title = ActionList.Pick;
//				return true;
//			}
//		}
		
		 

		if (f.getCarriedPOI() != null) { //to drop
			title = ActionList.Drop;
			healedVictim  = f.getCarriedPOI();
			return true;
		}
		else if (f.getCarriedPOI() == null && healedVictim.isHealed() && !healedVictim.hasLeader()) { //to pick
			title = ActionList.Pick;
			return true;
			}
		return false;
	}

	@Override
	public void adjustAction(GameState gs) {

	}
	
	public ActionList getTitle() {
		System.out.println(this.title);
    	return this.title;
    }

}

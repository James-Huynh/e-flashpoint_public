package actions;

import game.GameState;
import token.Firefighter;
import token.Speciality;

/*
 * @matekrk
 */

public class Change extends Action {

	protected Speciality toSpeciality;
	protected ActionList title = ActionList.Change;
	private static final long serialVersionUID = 1L;
	
	//constr
	public Change(Speciality toSpeciality) {
		this.APcost = 2;
		this.toSpeciality = toSpeciality;
	}
	
	@Override
	public void perform(GameState gs) {
		Firefighter current = gs.getFireFighterList().get(gs.getActiveFireFighterIndex());
		gs.setFirefighterSpeciality(current, toSpeciality);
		current.setAP(current.getAP()-this.APcost);
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter current = gs.getPlayingFirefighter();

		if (current.getAP() >= APcost) {
			if (gs.getFreeSpecialities().contains(toSpeciality) || toSpeciality.equals(null)) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public void adjustAction(GameState gs) {
		
	}
	
	@Override
	public String toString() {
		return "Change [toSpeciality=" + toSpeciality + ", title=" + title + ", APcost=" + APcost + ", direction="
				+ direction + "]";
	}
	
	
}

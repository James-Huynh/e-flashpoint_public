package actions;

import game.GameState;
import token.Firefighter;
import token.Hazmat;
import token.Speciality;

public class RemoveHazmat extends Action {

	private static final long serialVersionUID = 1L;
//	protected Hazmat hazmat;
	protected ActionList title = ActionList.RemoveHazmat;
	
	public RemoveHazmat() {
		this.APcost = 2;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	@Override
	public void perform(GameState gs) {
		gs.getPlayingFirefighter().setAP( gs.getPlayingFirefighter().getAP() - APcost);
		Hazmat hazmat = gs.getPlayingFirefighter().getCurrentPosition().popHazmat();
		hazmat.setDisposed();
		gs.addDisposedHazmat(hazmat);
		//place in rescued spot! ask Ben
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		if (playingFirefighter.getSpeciality() == (Speciality.HAZMAT_TECHNICIAN)) {
			if (playingFirefighter.getCurrentPosition().containsHazmat()) {
				if (playingFirefighter.getAP() >= APcost) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void adjustAction(GameState gs) {

	}

}

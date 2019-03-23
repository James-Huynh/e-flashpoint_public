package actions;

import game.GameState;
import token.Firefighter;
import token.Hazmat;
import token.Speciality;

public class RemoveHazmat extends Action {

	private static final long serialVersionUID = 1L;
	protected Hazmat hazmat;
	
	public RemoveHazmat() {
		this.APcost = 2;
	}
	
	@Override
	public void perform(GameState gs) {
		gs.getPlayingFirefighter().setAP( gs.getPlayingFirefighter().getAP() - APcost);
		hazmat.setDisposed();
		gs.getPlayingFirefighter().getCurrentPosition().popHazmat(hazmat);
		//place in rescued spot!

	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		if (playingFirefighter.getSpeciality().equals(Speciality.HAZMAT_TECHNICIAN)) {
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
		// TODO Auto-generated method stub

	}

}

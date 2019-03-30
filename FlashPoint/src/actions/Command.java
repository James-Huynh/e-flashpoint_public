package actions;

import game.GameState;
import token.Firefighter;
import token.Speciality;

public class Command extends Action {

	protected Firefighter toObey;
	protected Action action; //only: Move with victim, hazmat, normal move, move with threated victim, handle
	protected ActionList title = ActionList.Command;
	
	private static final long serialVersionUID = 1L;

	public Command(Firefighter toObey, Action action) {
		this.toObey = toObey;
		this.APcost = action.APcost;
		this.action = action;
	}
	
	@Override
	public void perform(GameState gs) {
		Firefighter me = gs.getPlayingFirefighter();
		gs.setPlayingFirefighter(toObey); // if it is fucked up then overloaded perform/validate with extra parameter
		action.perform(gs);
		gs.getPlayingFirefighter().setAP(gs.getPlayingFirefighter().getAP() + action.APcost);
		gs.setPlayingFirefighter(me);

		if (me.getSP() >= action.APcost) { //if i can do everything from SP
			me.setSP(me.getSP() - action.APcost);
		}
		else if(me.getSP() > 0) { //splitted
			int difference = action.APcost - me.getSP();
			me.setSP(0);
			me.setAP(me.getAP() - difference);
		}
		else { //everything from AP
			me.setAP(me.getAP() - action.APcost);
		}
		
		if (toObey.getSpeciality() == Speciality.CAFS) {
			me.setIfCommandCAPSthisTurn(true);
		}
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
		Firefighter captain = gs.getPlayingFirefighter();
		if (action.title == (ActionList.Handle) || action.title ==(ActionList.Move) || 
				action.title == (ActionList.MoveWithHazmat) || action.title == (ActionList.MoveWithVictim)) {
			if (captain.getSP() + captain.getAP() >= action.APcost) {
				gs.setPlayingFirefighter(toObey);
				if (action.validate(gs) && !captain.getIfCommandCAPSthisTurn() && !(toObey.getSpeciality()==Speciality.CAFS && action.APcost>1)) {
					//request and permission from toObey.
					flag = true;
				}
			}
		}
		
		return flag;
	}

	@Override
	public void adjustAction(GameState gs) {
		// TODO Auto-generated method stub

	}

}

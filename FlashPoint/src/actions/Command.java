package actions;

import game.GameState;
import token.Firefighter;

public class Command extends Action {

	protected Firefighter toObey;
	protected Action action; //only: Move with victim, hazmat, normal move, move with threated victim, handle
	
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
		me.setAP(me.getAP() - action.APcost);
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
		if (action.title.equals(ActionList.Handle) || action.title.equals(ActionList.Move) || 
				action.title.equals(ActionList.MoveWithHazmat) || action.title.equals(ActionList.MoveWithHazmat)) {
			flag = true; //finish it mat.
		}
		
		return flag;
	}

	@Override
	public void adjustAction(GameState gs) {
		// TODO Auto-generated method stub

	}

}

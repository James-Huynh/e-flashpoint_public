package actions;

import game.GameState;

public class Finish extends Action{
	
	public Finish() {
		this.APcost = 0;
	}

	@Override
	public String toString() {
		return "Finish [APcost=" + APcost + "]";
	}

	@Override
	public void perform(GameState gs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validate(GameState gs) {
		return true;  //pop-in window?
	}
	
}

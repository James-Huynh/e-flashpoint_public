package actions;

import game.GameState;

public class Finish extends Action{
	
	protected ActionList title = ActionList.Finish;
	
	public Finish() {
		this.APcost = 0;
	}
	
	public ActionList getTitle() {
    	return this.title;
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

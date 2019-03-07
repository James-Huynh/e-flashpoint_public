package actions;

import game.GameState;
import tile.Tile;
import token.Firefighter;

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
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int fire = currentPosition.getFire();
        if(fire == 2) {
        	return false;
        }
		return true;  //pop-in window?
	}
	
}

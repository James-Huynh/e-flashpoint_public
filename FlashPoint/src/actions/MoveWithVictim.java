package actions;

import game.GameState;
import tile.Tile;
import token.Firefighter;

public class MoveWithVictim extends Move{
	
	public MoveWithVictim(int direction){
		super(direction);
		this.APcost = 2;
	}
	
	@Override
	public boolean validate(GameState gs) {
		boolean normalMove = super.validate(gs);
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		int aP = playingFirefighter.getAP();
		if (normalMove && playingFirefighter.getCarrying() && aP >= 2 ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void perform(GameState gs) {
		super.perform(gs);
		
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
		boolean carrying = playingFirefighter.getCarrying();
		Tile neighbour = gs.getNeighbour(currentPosition, this.direction);
		
        if (carrying == true) {
        	currentPosition.removeFromPoiList( playingFirefighter.getVictim() );
        	neighbour.addPoi( playingFirefighter.getVictim() );
        }
	}

	@Override
	public String toString() {
		return "MoveWithVictim [direction=" + direction + ", APcost=" + APcost + "]";
	}
	
	
}

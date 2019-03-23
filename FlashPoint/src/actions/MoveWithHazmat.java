package actions;

import java.util.ArrayList;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Hazmat;

public class MoveWithHazmat extends Move {

	private static final long serialVersionUID = 1L;
	protected ActionList title = ActionList.MoveWithHazmat;
	protected Hazmat hazmat;
	
	public MoveWithHazmat(int direction, Hazmat hazmat){
		super(direction);
		this.APcost = 2;
		this.hazmat = hazmat;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	public Hazmat getHazmat() {
		return hazmat;
	}
	
	public void setHazmat(Hazmat hazmat) {
		this.hazmat = hazmat;
	}
	
	@Override
	public void perform(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		int aP = playingFirefighter.getAP();
		Tile currentPosition = playingFirefighter.getCurrentPosition();
		Tile neighbour = gs.getNeighbour(currentPosition, direction);
		
		super.perform(gs);
		currentPosition.popHazmat(hazmat);
		neighbour.setHazmat(hazmat); //includes setting position of hazmat
		if (!neighbour.checkInterior()) {
			hazmat.setDisposed();
			//place in rescued spot!
		}

	}

	@Override
	public boolean validate(GameState gs) {
		boolean normalMove = super.validate(gs);
		if (normalMove) {
			Firefighter playingFirefighter = gs.getPlayingFirefighter();
			int aP = playingFirefighter.getAP();
			Tile currentPosition = playingFirefighter.getCurrentPosition();
	        Edge edge = currentPosition.getEdge(direction);
	        Tile neighbour = gs.getNeighbour(currentPosition, direction);
	        int fire = neighbour.getFire();			
			if(currentPosition.containsHazmat(hazmat) && direction!=-1) {
				if(fire < 2) {
					if(edge.isDoor()) {
						if(edge.getStatus() == true || edge.isDestroyed()) {
							if (aP >= APcost) {
								return true;
							}
						}
					}
					else if(edge.isWall()) {
						if(edge.getDamage() == 0) {
							if (aP >= APcost) {
								return true;
							}
						}
					}
					else if(edge.isBlank()) {
						if (aP >= APcost) {
							return true;
						}
					}
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

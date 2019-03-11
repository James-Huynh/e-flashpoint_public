package actions;
import java.util.Random;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;

public class FireGun extends Action {
	
	protected ActionList title = ActionList.FireGun;

	protected int[][] quadrantIndices = new int[12][2]; //To hold the indices of the quadrant
	public FireGun() {									//FF location corresponds to
		APcost = 4;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }

	@Override
	public void perform(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        Tile currentPosition = playingFirefighter.getCurrentPosition();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        
        int[] coords = rollDice(currentPosition.getCoords());
        
        Tile target = gs.returnTile(coords[0], coords[1]);
        target.setFire(0);
        Edge adjacent;
        for(int i=0;i<4;i++) {
        	adjacent = target.getEdge(i);
        	if(adjacent.isBlank() || (adjacent.isDoor() && (adjacent.getStatus() || adjacent.isDestroyed())) || (adjacent.isWall() && adjacent.getDamage() == 0)){
        		Tile neighbour;
        		if(i == 0) {
        			neighbour = gs.returnTile(coords[0] -1, coords[1]); //Direction with indices w Ben
        			//If neighbour's coords are in quadrantIndices but 'splashover can extend beyond boundary'?
        					neighbour.setFire(0);
        		}
        		else if(i == 1) {
        			neighbour = gs.returnTile(coords[0] + 1, coords[1]);
        		}
        		else if(i == 2) {
        			neighbour = gs.returnTile(coords[0], coords[1] + 1);
        			
        		}
        		else {
        			neighbour = gs.returnTile(coords[0], coords[1] - 1);
        		}
        	}
        }
	}

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		Tile currentPosition = playingFirefighter.getCurrentPosition();
		
		//Here I can set up quadrantIndices -> setIndices(currentPosition.getCoords());
		//Then I can use this array below, in rollDice and in perform
		
		/*if(currentPosition.ParkingSpot == Vehicle.Engine) {
			Check associated quadrant based on FF location - if FF present, can't fire **Math required**
			int aP = playingFirefighter.getAP();
			if(aP >= 4) {
				flag = true;
			}
		}*/
		
		return flag;
	}
	
	public int[] rollDice(int[] location) {
		//Or I can call the regular roll dice method that will exist in GameState
		Random rand = new Random();
		int red = rand.nextInt(5);
		red += 1;
		int black = rand.nextInt(7);
		black += 1;
		
		while(red < location[0] && black < location[1]) { //Some Math here to get the indices of possible quadrant tiles
			red = rand.nextInt(5);						  //Based on FF location -- Ask Ben
			red += 1;
			black = rand.nextInt(7);
			black += 1;
		}
		int[] targetSpace = new int[2];
		targetSpace[0] = red;
		targetSpace[1] = black;
		return targetSpace;
	}

}

package actions;
import java.util.Arrays;
import java.util.Random;

import edge.Edge;
import game.GameState;
import tile.Tile;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;

public class FireGun extends Action {
	
	private static final long serialVersionUID = 1L;
	protected ActionList title = ActionList.FireGun;
	protected boolean driver;
	protected int[] result;

	protected int[][] quadrantIndices = new int[12][2]; //To hold the indices of the quadrant
	public FireGun() {									//FF location corresponds to
		APcost = 4;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	public int[] getResult() {
		return result;
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
		if (currentPosition.getParkingSpot() == null && currentPosition.getParkingSpot().getParkingType().equals(Vehicle.Engine) 
				&& currentPosition.getParkingSpot().getCar() == true) {
			if (playingFirefighter.getAP() >= APcost) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	@Override
	public void adjustAction(GameState gs) {
		if (gs.getPlayingFirefighter().getSpeciality().equals(Speciality.DRIVER)) {
			driver = true;
			APcost = 2;
		}
	}
	
	
	@Override
	public String toString() {
		return "FireGun [title=" + title + ", quadrantIndices=" + Arrays.toString(quadrantIndices) + ", APcost="
				+ APcost + ", direction=" + direction + "]";
	}

	public int[] rollDice(int[] location) {
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
	
	//both public important for Driver!
	public int[] rerollRedDice(int[] location, int[] result) {
		Random rand = new Random();
		int red = rand.nextInt(5);
		red += 1;
		while(red < location[0]) {
			red = rand.nextInt(5);
			red += 1;
		}
		int[] targetSpace = new int[2];
		targetSpace[0] = red;
		targetSpace[1] = result[1];
		return targetSpace;
	}
	
	public int[] rerollBlackDice(int[] location, int[] result) {
		Random rand = new Random();
		int black = rand.nextInt(7);
		black += 1;
		while(black < location[1]) {
			black = rand.nextInt(7);
			black += 1;
		}
		int[] targetSpace = new int[2];
		targetSpace[0] = result[0];
		targetSpace[1] = black;
		return targetSpace;
	}

}

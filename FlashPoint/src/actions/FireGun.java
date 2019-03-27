package actions;
import java.util.Arrays;
import java.util.Random;

import edge.Edge;
import game.GameState;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;

public class FireGun extends Action {
	
	private static final long serialVersionUID = 1L;
	protected ActionList title = ActionList.FireGun;
	protected boolean driver; //This works I think - we should overload constructors, one for everyone, other for Driver (his APcost is also different) and Action Factory responsible for intializing properly. 
	protected int[] result;
	protected ParkingSpot veh;
	protected int[][] quadrantIndices = new int[12][2]; //hardcoded for advanced
	
	public FireGun() {							
		APcost = 4;
	}
	
	public FireGun(ParkingSpot veh) {
		APcost = 4;
		this.veh = veh; //Same thing, won't be passed in
		
		if (veh.getTiles()[0].getCoords()[0] == 0) {
			int i=0;
			for (int k=1; k<=4; k++) {
				for (int j=4; j<=6; j++) {
					this.quadrantIndices[i][0] = k;
					this.quadrantIndices[i][1] = j;
					i++;
				}
			}
		}
		
		else if ((veh.getTiles()[0].getCoords()[0] == 9)) {
			int i=0;
			for (int k=5; k<=8; k++) {
				for (int j=1; j<=3; j++) {
					this.quadrantIndices[i][0] = k;
					this.quadrantIndices[i][1] = j;
					i++;
				}
			}
		}
		
		else if ((veh.getTiles()[0].getCoords()[1] == 0)) {
			int i=0;
			for (int k=1; k<=4; k++) {
				for (int j=1; j<=3; j++) {
					this.quadrantIndices[i][0] = k;
					this.quadrantIndices[i][1] = j;
					i++;
				}
			}
		}
		
		else if ((veh.getTiles()[0].getCoords()[1] == 7)) {
			int i=0;
			for (int k=5; k<=8; k++) {
				for (int j=4; j<=6; j++) {
					this.quadrantIndices[i][0] = k;
					this.quadrantIndices[i][1] = j;
					i++;
				}
			}
		}
		
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
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        
        int[] coords = rollDice();
        
        Tile target = gs.returnTile(coords[0], coords[1]);
        target.setFire(0);
        Edge adjacent;
        for(int i=0;i<4;i++) {
        	adjacent = target.getEdge(i);
        	if(adjacent.isBlank() || 
        			(adjacent.isDoor() && (adjacent.getStatus() || adjacent.isDestroyed())) || 
        			(adjacent.isWall() && adjacent.getDamage() == 0)){
        		Tile neighbour = gs.getNeighbour(target, i);
        		neighbour.setFire(0);
        	}
        }
	}
	
	//perform driver
	
	//?

	@Override
	public boolean validate(GameState gs) {
		boolean flag = false;
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
		if(playingFirefighter.getSpeciality() == Speciality.DRIVER ) {
			driver = true;
			APcost = 2;
		}
		Tile currentPosition = playingFirefighter.getCurrentPosition();
		if (currentPosition.getParkingSpot() == this.veh && currentPosition.getParkingSpot().getParkingType() == (Vehicle.Engine) 
				&& currentPosition.getParkingSpot().getCar() == true) {
			if (playingFirefighter.getAP() >= APcost && noOne(gs)) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	public boolean noOne(GameState gs) {
		for (int i=0; i<quadrantIndices.length; i++) {
			if(!gs.returnTile(quadrantIndices[i][0], quadrantIndices[i][1]).getFirefighterList().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void adjustAction(GameState gs) {
		if (gs.getPlayingFirefighter().getSpeciality() == (Speciality.DRIVER)) {
			driver = true;
			APcost = 2;
		}
	}
	
	
	@Override
	public String toString() {
		return "FireGun [title=" + title + ", quadrantIndices=" + Arrays.toString(quadrantIndices) + ", APcost="
				+ APcost + ", direction=" + direction + "]";
	}

	public int[] rollDice() {
	
		Random rand = new Random();
		int whichOne = rand.nextInt(12);
		int red = quadrantIndices[whichOne][0];
		int black = quadrantIndices[whichOne][1];
		
		int[] targetSpace = new int[2];
		targetSpace[0] = red;
		targetSpace[1] = black;
		return targetSpace;
	}
	
	//both public important for Driver!
	public int[] rerollRedDice(int[] result) {
		Random rand = new Random();
		/*
		int red = rand.nextInt(5);
		red += 1;
		while(red < location[0]) {
			red = rand.nextInt(5);
			red += 1;
		}
		*/
		int whichOne = rand.nextInt(12);
		int red = quadrantIndices[whichOne][0];
		
		int[] targetSpace = new int[2];
		targetSpace[0] = red;
		targetSpace[1] = result[1];
		return targetSpace;
	}
	
	public int[] rerollBlackDice(int[] result) {
		
		Random rand = new Random();
		/*
		int black = rand.nextInt(7);
		black += 1;
		while(black < location[1]) {
			black = rand.nextInt(7);
			black += 1;
		}
		*/
		
		int whichOne = rand.nextInt(12);
		int black = quadrantIndices[whichOne][1];
		
		int[] targetSpace = new int[2];
		targetSpace[0] = result[0];
		targetSpace[1] = black;
		return targetSpace;
	}

}

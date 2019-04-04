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
	protected int index;
	
	public FireGun() {							
		APcost = 4;
	}
	
	public FireGun(int index) {
		APcost = 4;
		this.index = index;
	}
	
	//overloaded for specialist Driver
	public FireGun(int index, int[] coords) {
		APcost = 2;
		this.result = coords;
	}
	
	public ActionList getTitle() {
    	return this.title;
    }
	
	public int[] getResult() {
		return result;
	}
	
	public void setResult(int[] newCords) {
		this.result[0] = newCords[0];
		this.result[1] = newCords[1];
	}

	@Override
	public void perform(GameState gs) {
		Firefighter playingFirefighter = gs.getPlayingFirefighter();
        int aP = playingFirefighter.getAP();
        playingFirefighter.setAP(aP - this.APcost);
        playingFirefighter.setUsedAP(true);
        
        Tile target;
        if (driver) {
           target = gs.returnTile(result[0], result[1]);

        }
        else {
	        int[] coords = rollDice(gs);
	        target = gs.returnTile(coords[0], coords[1]);
        }
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
        System.out.println("DUPA FIRE GUN" + target.getX() + target.getY());
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
			result = rollDice(gs);
		}
		Tile currentPosition = playingFirefighter.getCurrentPosition();
		ParkingSpot ps = gs.getEngines()[index];
		if (currentPosition.getParkingSpot() != null && currentPosition.getParkingSpot().equals(ps) && 
				currentPosition.getParkingSpot().getParkingType() == (Vehicle.Engine) && //unnecessary but assertion
				currentPosition.getParkingSpot().getCar() == true) {
			if (playingFirefighter.getAP() >= APcost && noOne(gs)) { // at least not now and not in that form
				flag = true;
			}
		}
		
		return flag;
	}
	
	public boolean noOne(GameState gs) {
		int[][] quadrantIndices = gs.getEngines()[index].getQuadrants();
		for (int i=0; i<quadrantIndices.length; i++) {
			if(!gs.returnTile(quadrantIndices[i][0], quadrantIndices[i][1]).getFirefighterList().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean firePresent(GameState gs) {
		boolean flag = false;
		int[][] quadrantIndices = gs.getEngines()[index].getQuadrants();
		for(int i=0;i<quadrantIndices.length;i++) {
			if(gs.returnTile(quadrantIndices[i][0], quadrantIndices[i][1]).getFire() == 2) {
				flag = true;
			}
		}
		return flag;
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
		return "FireGun [title=" + title + ", driver=" + driver + ", result=" + Arrays.toString(result) + ", index="
				+ index + ", APcost=" + APcost + ", direction=" + direction + "]";
	}

	public int[] rollDice(GameState gs) {
		int[][] quadrantIndices = gs.getEngines()[index].getQuadrants();
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
	public int[] rerollRedDice(GameState gs, int[] result) {
		int[][] quadrantIndices = gs.getEngines()[index].getQuadrants();
		
		Random rand = new Random();
		int whichOne = rand.nextInt(12);
		int red = quadrantIndices[whichOne][0];
		
		int[] targetSpace = new int[2];
		targetSpace[0] = red;
		targetSpace[1] = result[1];
		return targetSpace;
	}
	
	public int[] rerollBlackDice(GameState gs, int[] result) {
		
		int[][] quadrantIndices = gs.getEngines()[index].getQuadrants();
		
		Random rand = new Random();
		
		int whichOne = rand.nextInt(12);
		int black = quadrantIndices[whichOne][1];
		
		int[] targetSpace = new int[2];
		targetSpace[0] = result[0];
		targetSpace[1] = black;
		return targetSpace;
	}

}

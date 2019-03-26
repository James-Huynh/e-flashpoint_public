package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// Start of user code for imports
// End of user code
import java.util.HashSet;
import java.util.Set;

import actions.Action;
import actions.Chop;
import actions.Extinguish;
import actions.Finish;
import actions.Handle;
import actions.Move;
import actions.MoveWithVictim;
import edge.Edge;
import game.GameState;
import lobby.Lobby;
import tile.ParkingSpot;
import tile.Tile;
import token.Firefighter;
import token.Hazmat;
import token.POI;

/**
 * GameManager class definition.
 * Generated by the TouchCORE code generator.
 * modified by @matekrk @eric
 */
public class GameManager {
    
	//changes by ben
	//private final GameState gs = GameState.getInstance();
	private GameState gs;
	private Set<Action> possibleActions = generateAllPossibleActions();
	private String recentAdvFire;
	private Lobby representsLobby;
	
	// MAIN
    public void runFlashpoint() {
    	// list of Players from lobby - somehow getLobby from Server-Client connection
    	Lobby lobby = new Lobby(); //dumb lobby for now
    	gs.updateGameStateFromLobby(lobby);
    	setup();
    	doTurns();
    }
    
    public GameManager(Lobby lobby) {
    	gs = GameState.getInstance();
    	representsLobby = lobby;
    	
    }
    
    public GameManager(GameState input) {
    	gs = input;

    	
    }
    
    public void setup() {
    	gs.updateGameStateFromLobby(representsLobby);
    	gs.setListOfPlayers(representsLobby.getPlayers());
    	setupGameType();
    	gs.setFirefighters();
    }
	
    public void setupGameType() {
		if(true/*representsLobby.getBoard().equals("Board 1")*/) {
			/**Board structure edges structure would go here**/
			if(representsLobby.getMode().equals("Family")) {
				gs.initializeEdges(representsLobby.getTemplate().getEdgeLocations());
				gs.openExteriorDoors(); //open doors because doors are closed by default
				gs.initializePOI(representsLobby.getTemplate().getPOILocations());
				gs.initializeFire(representsLobby.getTemplate().getFireLocations());
			}
			
			else if(representsLobby.getMode().equals("Experienced")) {
				if(representsLobby.getDifficulty().equals("Recruit")) {
					gs.initializeEdges(representsLobby.getTemplate().getEdgeLocations());
					gs.openExteriorDoors();
					gs.setHotSpot(6);
					initializeExperiencedGame(3,3);
				}
				else if(representsLobby.getDifficulty().equals("Veteran")) {
					gs.initializeEdges(representsLobby.getTemplate().getEdgeLocations());
					gs.openExteriorDoors();
					gs.setHotSpot(6);
					initializeExperiencedGame(3,4);
				}
				else if(representsLobby.getDifficulty().equals("Heroic")) {
					gs.initializeEdges(representsLobby.getTemplate().getEdgeLocations());
					gs.openExteriorDoors();
					gs.setHotSpot(12);
					initializeExperiencedGame(4,5);
				}
				setupPOIS();
			}
		}
		
    }
    
	public void initializeExperiencedGame(int initialExplosions, int hazmats) {
		//resolve initialExplosion amount of explosions
		System.out.println("Called experienced game");
		int blackDice = 0;
		for(int i=initialExplosions; i > 0; i--) {
		
			
			
			if(i == initialExplosions) {
				int explosionAt = gs.getRandomNumberInRange(1,8);
				blackDice = explosionAt;
				
				if (explosionAt == 1) {
					gs.returnTile(3,3).setFire(2);
					gs.returnTile(3,3).setHotSpot(1);
					this.explosion(gs.returnTile(3,3));
					System.out.println("Explosion was at 3 3");
				}
				else if (explosionAt == 2) {
					gs.returnTile(3,4).setFire(2);
					gs.returnTile(3,4).setHotSpot(1);
					this.explosion(gs.returnTile(3,4));
					System.out.println("Explosion was at 3 5");
				}
				else if (explosionAt == 3) {
					gs.returnTile(3,5).setFire(2);
					gs.returnTile(3,5).setHotSpot(1);
					this.explosion(gs.returnTile(3,5));
					System.out.println("Explosion was at 3 5");
				}
				else if (explosionAt == 4) {
					gs.returnTile(3,6).setFire(2);
					gs.returnTile(3,6).setHotSpot(1);
					this.explosion(gs.returnTile(3,6));
					System.out.println("Explosion was at 3 6");
				}
				else if (explosionAt == 5) {
					gs.returnTile(4,6).setFire(2);
					gs.returnTile(4,6).setHotSpot(1);
					this.explosion(gs.returnTile(4,6));
					System.out.println("Explosion was at 4 6");
				}
				else if (explosionAt == 6) {
					gs.returnTile(4,5).setFire(2);
					gs.returnTile(4,5).setHotSpot(1);
					this.explosion(gs.returnTile(4,5));
					System.out.println("Explosion was at 4 5");
				}
				else if (explosionAt == 7) {
					gs.returnTile(4,4).setFire(2);
					gs.returnTile(4,4).setHotSpot(1);
					this.explosion(gs.returnTile(4,4));
					System.out.println("Explosion was at 4 4");
				}
				else {
					gs.returnTile(4,3).setFire(2);
					gs.returnTile(4,3).setHotSpot(1);
					this.explosion(gs.returnTile(4,3));
					System.out.println("Explosion was at 4 3");
				}
				
			}
			
			else if(i == initialExplosions - 1) {
				boolean exit = true;
				
				while(exit) {
					Tile explosionAt = gs.rollForTile();
					if(explosionAt.getFire() != 2) {
						explosionAt.setFire(2);
						explosionAt.setHotSpot(1);
						this.explosion(explosionAt);
						blackDice = explosionAt.getY();
						exit = false;
						System.out.println("Explosion was at " + explosionAt.getX() + explosionAt.getY());
					}
				}
			}
			
			else if(i == initialExplosions - 2) {
				boolean exit = true;
				
				Tile newExplosionAt = gs.returnTile(gs.getRandomNumberInRange(1, 6), (9 - blackDice));
				while(exit) {
					if(newExplosionAt.getFire() != 2) {
						newExplosionAt.setFire(2);
						newExplosionAt.setHotSpot(1);
						this.explosion(newExplosionAt);
						//blackDice = newExplosionAt.getX();
						exit = false;
						System.out.println("Explosion was at " + newExplosionAt.getX() + newExplosionAt.getY());
					}
					else {
						newExplosionAt = gs.returnTile(gs.getRandomNumberInRange(1, 6), (9 - blackDice));
					}
				}
				
			}
			
			else {
				boolean exit = true;
				
				while(exit) {
					Tile newExplosionAt = gs.rollForTile();
					
					if(newExplosionAt.getFire() != 2) {
						newExplosionAt.setFire(2);
						newExplosionAt.setHotSpot(1);
						this.explosion(newExplosionAt);
						//blackDice = newExplosionAt.getX();
						exit = false;
					}
				}
			}
		}
		
		clearExteriorFire();
		
		//roll to place hazmats
		for(int i = 0; i < hazmats; i++) {
			boolean exit = true;
		
			while(exit) {
				Tile hazmatAt = gs.rollForTile();
				
				if(hazmatAt.getFire() != 2) {
//					hazmatAt.setHazmat(1);
					hazmatAt.setHazmat(new Hazmat());
					exit = false;
				}
			}
		}
		
		//place additional 3 hotspots for veteran/heroic mode
		if((initialExplosions == 3 && hazmats == 4) || (initialExplosions == 4 && hazmats == 5)) {
			for(int i = 0; i < 3; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gs.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
			}
		}
		
		//place additional hotspot depedning on player number
		if(gs.getListOfPlayers().size() >= 4) {
			for(int i = 0; i < 3; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gs.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
			}
		}
		
		//place additional hotspot if player = 3
		if(gs.getListOfPlayers().size() == 3) {
			for(int i = 0; i < 2; i++) {
				boolean exit = true;
				
				while(exit) {
					Tile hotspotAt = gs.rollForTile();
					
					if(hotspotAt.getHotSpot() != 1) {
						hotspotAt.setHotSpot(1);
						exit = false;
					}
				}
				
			}
		}
				
	}
	
	public void setupPOIS() {
		int[][] locations = representsLobby.getTemplate().getPOILocations();
		for(int i=0; i<3; i++) {
			Tile targetTile = gs.rollForTile();
			while(targetTile.getFire() == 2) {
				targetTile = gs.rollForTile();
			}
			locations[targetTile.getX()][targetTile.getY()] = 1;
		}
		gs.initializePOI(locations);
	}
    
    public void doTurns() {
    	return;
    	/*
    	while(!gs.isGameTerminated()) {
    		gs.getPlayingFirefighter().setAP( Math.min(8, gs.getPlayingFirefighter().getAP() + 4) );
    		takeATurn();
    		advanceFire();
    		gs.setActiveFireFighterIndex( (gs.getActiveFireFighterIndex() + 1)%(gs.getFireFighterList().size()) );
    	}
    	System.out.println(gs.isGameWon());
    	*/
    }
    
    public void takeATurn() {
    	Set<Action> availableActions = getAllAvailableActions();
    	// pass to GUI
    	// GUI passes which action
    	// perform 
    	// if it was end of turn die, if not recursion (but GameState is different now!)	
    }
    
    public Set<Action> generateAllPossibleActions(){
    	
    	Set<Action> allPossibleActions = new HashSet<Action>(30);
    	
    	//move + chop + handle
    	for (int dir : new int[]{0,1,2,3} ) {
    		allPossibleActions.add(new Move(dir));
    		allPossibleActions.add(new MoveWithVictim(dir));
    		allPossibleActions.add(new Chop(dir, 2));
    		allPossibleActions.add(new Chop(dir, 4));
    		allPossibleActions.add(new Handle(dir));
    	}
    	
    	//extinguish
    	for (int dir : new int[]{-1,0,1,2,3,} ) {
    		allPossibleActions.add(new Extinguish(dir, 1)); 
    		allPossibleActions.add(new Extinguish(dir, 2));
    	}
 
//    	//handle
//    	allPossibleActions.add(new Handle());
    	
    	//finish
    	allPossibleActions.add(new Finish());
    	
    	//
    	return allPossibleActions;
    }
    
    //TODO: Zaid + Mat based on validations
    public Set<Action> getAllAvailableActions() {
    	Set<Action> allValidActions = new HashSet<Action>(30);
        for (Action a : possibleActions) {
        	if (a.validate(gs)) {
        		allValidActions.add(a);
        	}
        }
        return allValidActions;
    }
    
    public void setAllAvailableActions(Set<Action> newSet) {
    	this.possibleActions = newSet;
    }
    
    
	//Ben and eric, ready for testing
    public void explosion(Tile targetTile) {
        /*
         * Ben: Can u remember how we consider the tile check? From the first tile or the 0th Tile?
         * I can not remember here need some modification
         * By Eric
         * 
         * */
    	for(int direction = 0; direction < 4; direction++) {
    		
    		if(direction == 0) {
    			recentAdvFire += "The explosions continued left. \n";
    		} else if(direction == 1) {
    			recentAdvFire += "The explosions continued up. \n";
    		} else if(direction == 2){
    			recentAdvFire += "The explosions continued right. \n";
    		} else if(direction == 3) {
    			recentAdvFire += "The explosions continued down. \n";
    		}
    		boolean checkBarriers = targetTile.checkBarriers(direction);
    		
    		Edge targetEdge;
    		
    		//checks if the wall or door that is the next edge in this direction is closed or intact. Explosion halts here
    		if(checkBarriers == true) {
    			targetEdge = targetTile.getEdge(direction);
    			
    			if(targetEdge.isDoor()) {
    				
    				targetEdge.destroyDoor();
    				recentAdvFire += "The explosion destroyed a closed door and halted.\n";
    				//continue instead of break, to check the next direction but continue the explosion
    				continue;
    			} 
    			else if(targetEdge.isWall()) {
    				//wall is only damaged 1 for family game
    				targetEdge.chop();
    				gs.updateDamageCounter();
    				recentAdvFire += "The explosion damaged a wall.\n";
    				//continue instead of break, to check the next direction but continue the explosion
    				continue;
    			}
    		}
    			
			//destroys the open door but the explosion continues through the door.
			if(targetTile.getEdge(direction).isDoor()) {
				if(!targetTile.getEdge(direction).isDestroyed()) {
					targetTile.getEdge(direction).destroyDoor();
					recentAdvFire += "The explosion destroyed an open door but did not stop. \n";
				}
			}
			
			Tile tempTile = gs.getNeighbour(targetTile, direction);
			
			
			while(true) {
				//updates final tile of explosion
				if(tempTile.getFire()<2) {
					
					tempTile.setFire(2);
					recentAdvFire += "The explosion caused tile " + tempTile.getCoords()[0] + "," + tempTile.getCoords()[1] + " to catch fire.\n";
					
					break;
				} 
				
				//halts the explosion from leaving the board
				if(!tempTile.checkInterior()) {
					recentAdvFire += "The explosion has reached the end of the board.\n";
					break;
				}
				
				boolean barCheck = tempTile.checkBarriers(direction);
				//destroyed or damages walls in the way
				if(barCheck == true) {
					targetEdge = tempTile.getEdge(direction);
					
					if(targetEdge.isDoor()) {
						
						targetEdge.destroyDoor();
						recentAdvFire += "The explosion destroyed a closed door and halted.\n";
						break;
					} 
					else if(targetEdge.isWall()) {
						
						targetEdge.chop();
						
						gs.updateDamageCounter();
						recentAdvFire += "The explosion damaged a wall.\n";
						
						break;
					}
				}
				
				//destroys the open door if the explosion is passing through.
				if(tempTile.getEdge(direction).isDoor()) {
					if(!tempTile.getEdge(direction).isDestroyed()) {
						tempTile.getEdge(direction).destroyDoor();
						recentAdvFire += "The explosion destroyed an open door but did not stop.\n";
					}
				}
				
				//gets the next tile
				tempTile = gs.getNeighbour(tempTile, direction);
			}
    		
    	}
    }

  //Ben and eric, ready for testing
    public void resolveFlashOver() {
        /* TODO: No message view defined */
    	Tile targetTile = gs.returnTile(0,0);
    	int[] tempCoords = targetTile.getCoords();
    	while(true) {       // all tiles
    		int curFire = targetTile.getFire();
    		tempCoords = targetTile.getCoords();
    		if(curFire == 1) { //checking if the tile is smokey, if so it will check adj tiles to see if it should be set on fire
    			
    			for(int direction=0; direction<4;direction++) {
    				//checks for if the adj tiles are above/below the map
    				if(tempCoords[0] == 0) {
    					if(direction == 1) {
    						continue;
    					}
    				} else if(tempCoords[0] == 7) {
    					if(direction == 3) {
    						continue;
    					}
    				}
    				//checks for if the adj tiles are left or right of the map
    				if(tempCoords[1] == 0) {
    					if(direction == 0) {
    						continue;
    					}
    				} else if(tempCoords[1] == 9) {
    					if(direction == 2) {
    						continue;
    					}
    				}
    				
    				//checks if a barrier is in the way, if not it checks if the tile in said direction is on fire and flashes over is so
    				boolean checkBarriers = targetTile.checkBarriers(direction);
    				if(checkBarriers == false) {
    					Tile adjTile =  gs.getNeighbour(targetTile,direction);
        				int fireCheck = adjTile.getFire();
        				
        				if(fireCheck == 2) {
        					targetTile.setFire(2);
        					recentAdvFire += "Flashover spread the fire to tile " + tempCoords[0] + "," + tempCoords[1] + ".\n";
        					
        					//resets the targetTile
        					targetTile = gs.returnTile(0,0);
        		
        					break;
        				}
    				}
    			}
    		
    			//halts the loop when the last tile is looked at
        		if(tempCoords[0] == 7 && tempCoords[1] == 9) {
        			break;
        		}
        		
        		//selects the next tile either by getneighbour or the first column of the next row if at the end of the current
        		if(tempCoords[1] == 9) {
        			targetTile = gs.returnTile(tempCoords[0] + 1, 0);
        		}else {
        			targetTile = gs.getNeighbour(targetTile, 2);
        		}
        		
    		} 
    		//the tile being looked at is not smokey, get the next tile
    		else {
    			if(tempCoords[0] == 7 && tempCoords[1] == 9) {
    				break;
    			}
    			if(tempCoords[1] == 9) {
        			targetTile = gs.returnTile(tempCoords[0] + 1, 0);
        		}else {
        			targetTile = gs.getNeighbour(targetTile, 2);
        		}
    		}
    		
    	}
    	
    }
    
    private void resolveHazmatExplosions() {
  		// TODO Auto-generated method stub
    	Tile targetTile = gs.returnTile(0,0);
    	int[] tempCoords = targetTile.getCoords();
    	while(true) {       // all tiles
    		tempCoords = targetTile.getCoords();
    		if(targetTile.getFire() == 2 && targetTile.containsHazmat()) {
    			while(targetTile.containsHazmat()) {
    				Hazmat temp = targetTile.popHazmat();
    				recentAdvFire += "hazmat explosion caused at:  " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1] +"\n";
    				explosion(targetTile);
    			}
    			if(gs.getHotSpot() > 0) {
    				targetTile.setHotSpot(1);
    				gs.setHotSpot(gs.getHotSpot() - 1);
    				recentAdvFire += "hotSpot added to final tile at coords: " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1]  +"\n";
    			}
    			targetTile = gs.returnTile(0, 0);
    			tempCoords = targetTile.getCoords();
    		}
    		if(tempCoords[0] == 7 && tempCoords[1] == 9) {
				break;
			}
			if(tempCoords[1] == 9) {
    			targetTile = gs.returnTile(tempCoords[0] + 1, 0);
    		}else {
    			targetTile = gs.getNeighbour(targetTile, 2);
    		}
    		
    	}
  	}

  //Ben and eric, skeleton code 
    public void checkKnockDowns() {
        /* TODO: No message view defined */
    	recentAdvFire += "\n";
    	//Select Tile
    	Tile targetTile = gs.returnTile(0, 0);
    	
    	//Check Tile contents
    	boolean containsFireFighter = targetTile.containsFirefighter();
    	boolean containsPOI = targetTile.containsPOI();
    	
    	//cycle through all the tile, need a better check.
    	int[] coords = targetTile.getCoords();
    
    	while(true){	
    		int curFire = targetTile.getFire();
    		coords = targetTile.getCoords();
    		boolean fire = false;
    		
    		//knockdown all firefighters on tiles with fire
    		
    		if(containsFireFighter == true) {
    			
    			if(curFire == 2) {
    				
    				
    				fire = true;
    				ParkingSpot respawnTile = targetTile.getNearestAmbulance();
    				if(this.representsLobby.getMode().equals("Experienced")){
    					for(int i = 0; i<4; i++) {
    						if(gs.getAmbulances()[i].getCar()) {
    							respawnTile = gs.getAmbulances()[i];
    						}
    					}
    				} 
    				
    				
    				Firefighter tempFire = targetTile.removeFirstFireFighter();
    				
    				int tile = gs.getRandomNumberInRange(0, 1);
    						
    				Tile target = respawnTile.getTiles()[tile];
    				
    				tempFire.updateLocation(respawnTile);
    				target.addToFirefighterList(tempFire);
    				recentAdvFire += "Firefighter knocked down at tile " + coords[0] + "," + coords[1] + ".\n";
    				
    			}
    		}
    		//kill and remove all POI found on tiles with fire
    		if(containsPOI == true) {
    			if(curFire == 2) {
    				fire = true;
    				POI tempPOI = targetTile.removeFirstPoi();
    				gs.removePOI(tempPOI);
    				if(tempPOI.isVictim()) {
    					gs.updateLostCount(tempPOI);
    					recentAdvFire += "Victim lost on tile " + coords[0] + "," + coords[1] + ".\n";
    				} else {
    					gs.updateRevealPOI(tempPOI);
    					recentAdvFire += "False Alarm revealed on tile " + coords[0] + "," + coords[1] + ".\n";
    				}
    			}
    		}
    		
    		//check if this tile still have POI or firefighters
    		containsFireFighter = targetTile.containsFirefighter();
        	containsPOI = targetTile.containsPOI();
        	
        	//select next tile if current tile is empty
        	if((!containsFireFighter && !containsPOI)|| (containsFireFighter && !fire) || (containsPOI && !fire)) {
        		
        		
        		if(coords[0] == 7 && coords[1] == 9) {
    				break;
    			}
    			if(coords[1] == 9) {
        			targetTile = gs.returnTile(coords[0] + 1, 0);
        		}else {
        			targetTile = gs.getNeighbour(targetTile, 2);
        		}
        		
        		containsFireFighter = targetTile.containsFirefighter();
            	containsPOI = targetTile.containsPOI();
        	}
    		
    	}
    }

  //Ben and eric, skeleton code 
    public void placePOI() {
    	recentAdvFire += "\n";
        int currentPOI = gs.getCurrentPOI();
        Tile targetTile = gs.rollForTile();
        while (currentPOI < 3) {
        	boolean containsPOI = targetTile.containsPOI();
        	boolean containsFireFighter = targetTile.containsFirefighter();
        	int curFire = targetTile.getFire();
        	if(containsPOI == false) {
        		if(this.representsLobby.getMode().equals("Experienced")) {
        			if(curFire > 0 || containsFireFighter) {
        				//targetTile = getNewPOITile(targetTile.getCoords());
        				targetTile = gs.rollForTile();
        			} else {
        				POI newPOI = gs.generatePOI();
            			targetTile.addPoi(newPOI);
            			gs.updatePOI(newPOI);
            			recentAdvFire += "POI placed on tile " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1];
        			}
        		} else {
        			//could skip this if else and just set fire to 0 every time, could also write this to check tile and so on and only add the poi if the tile does not contain a firefighter or if it does and the poi is a victim.
            		if(curFire != 0) {
            			targetTile.setFire(0);
            			POI newPOI = gs.generatePOI();
            			targetTile.addPoi(newPOI);
            			gs.updatePOI(newPOI);
            			recentAdvFire += "POI placed on tile " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1];
            			
            			if(containsFireFighter == true) {
            				newPOI.reveal();
            				recentAdvFire += " and revealed. \n";
            				if(newPOI.isVictim() == false) {
            					recentAdvFire += "The POI was a false alarm and remvoed. \n";
            					targetTile.removeFromPoiList(newPOI);
            					gs.removePOI(newPOI);
            					gs.updateRevealPOI(newPOI);
            				}
            			}
            		}
            		else if(curFire == 0) {
            			POI newPOI = gs.generatePOI();
            			targetTile.addPoi(newPOI);
            			gs.updatePOI(newPOI);
            			recentAdvFire += "POI placed on tile " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1];
            			
            			if(containsFireFighter == true) {
            				newPOI.reveal();
            				recentAdvFire += " and revealed. \n";
            				if(newPOI.isVictim() == false) {
            					recentAdvFire += "The POI was a false alarm and remvoed. \n";
            					targetTile.removeFromPoiList(newPOI);
            					gs.removePOI(newPOI);
            					gs.updateRevealPOI(newPOI);
            				}
            			}
            		}
        		}
        	}
        	currentPOI = gs.getCurrentPOI();
        	if(this.representsLobby.getMode().equals("Experienced")) {

        	} else {
        		targetTile = gs.rollForTile();
        	}
        }
    }
    
    public Tile getNewPOITile(int[] coords) {
    	int row = coords[0];
    	int coloumn = coords[1];
    	//if(row == 0 ||)
    	
    	
    	
    	return null;
    }

    //Ben and eric, skeleton code 
    public void advanceFire(boolean additionalHotspot) {
        /* TODO: No message view defined */
    	recentAdvFire = "";
    	
    	//gs.endTurn();
    	
    	Tile targetTile = gs.rollForTile();
    	boolean initialHotSpot = false;
    	if(this.representsLobby.getMode().equals("Experienced")) {
    		initialHotSpot = targetTile.containsHotSpot();
    	} 
    	
    	int curFire = targetTile.getFire();
    	int[] tempCoords = targetTile.getCoords();
    	if(curFire == 0) {
    		boolean flag = false;
    		for(int direction = 0; direction<4; direction ++) {
    			//checks for if the adj tiles are above/below the map
				if(tempCoords[0] == 0) {
					if(direction == 1) {
						continue;
					}
				} else if(tempCoords[0] == 7) {
					if(direction == 3) {
						continue;
					}
				}
				//checks for if the adj tiles are left or right of the map
				if(tempCoords[1] == 0) {
					if(direction == 0) {
						continue;
					}
				} else if(tempCoords[1] == 9) {
					if(direction == 2) {
						continue;
					}
				}
				//checks if a barrier is in the way, if not it checks if the tile in said direction is on fire and flashes over is so
				boolean checkBarriers = targetTile.checkBarriers(direction);
				if(checkBarriers == false) {
					Tile adjTile =  gs.getNeighbour(targetTile,direction);
    				int fireCheck = adjTile.getFire();
    				
    				if(fireCheck == 2) {
    					flag = true;
    				}
				}
    		}
    		if(flag) {
    			targetTile.setFire(2);
    			if(additionalHotspot) {
    				recentAdvFire += "Another advanced fire triggered\n Tile "+ tempCoords[0] +"," + tempCoords[1] + " turned to smoke and caught Fire.\n";
    			} else {
    				recentAdvFire = "Tile "+ tempCoords[0] +"," + tempCoords[1] + " turned to smoke and caught Fire.\n";
    			}
    		}else {
    			targetTile.setFire(1);
    			if(additionalHotspot) {
    				recentAdvFire += "Another advanced fire triggered\n Tile "+ tempCoords[0] +"," + tempCoords[1] + " turned to smoke.\n";
    			} else {
    				recentAdvFire = "Tile "+ tempCoords[0] +"," + tempCoords[1] + " turned to smoke.\n";
    			}
    			
    		}
    	}else if(curFire == 1) {
    		targetTile.setFire(2);
    		if(additionalHotspot) {
    			recentAdvFire += "Another advanced fire triggered\n Tile "+ tempCoords[0] +"," + tempCoords[1] + " caught Fire.\n";
    		}
    		else {
    			recentAdvFire = "Tile "+ tempCoords[0] +"," + tempCoords[1] + " caught Fire.\n";
    		}
    		
    	}
    	else {
    		if(additionalHotspot) {
    			recentAdvFire += "Another advanced fire triggered\n An explosion occured at tile "+ tempCoords[0] +"," + tempCoords[1] + ". \n";
    		} else {
    			recentAdvFire = "An explosion occured at tile "+ tempCoords[0] +"," + tempCoords[1] + ". \n";
    		}
    		explosion(targetTile);
    		
    	}
    	resolveFlashOver();
    	
    	//checking each tile for if a hazmat is present, causing explosions if yes
    	if(this.representsLobby.getMode().equals("Experienced")) {
    		recentAdvFire += "hazmat Explosion check commenced \n";
    		resolveHazmatExplosions();
    	}
    	
    	
    	//checking if the first rolled tile contains a hotspot
    	if(!initialHotSpot) {
    		if(additionalHotspot) {
    			if(gs.getHotSpot()>0) {
    				targetTile.setHotSpot(1);
        			gs.setHotSpot(gs.getHotSpot() - 1);
        			recentAdvFire += "hotSpot added to final tile at coords: " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1]  +"\n";
    			}
    		}
    		
    		checkKnockDowns();
        	placePOI();
        	clearExteriorFire();
        	
        	
        	
        	int wallCheck = gs.getDamageCounter();//should this running the same time with the main process? @Eric
        	int victimCheck = gs.getLostVictimsList().size();
        	int savedVictimCheck = gs.getSavedVictimsList().size();
        	
        	
        	if(wallCheck >= 24 || victimCheck >= 4) {
        		gs.terminateGame();
        	} else if(savedVictimCheck >= 7) {
        		gs.winGame();
        	}
    	} else {
    		recentAdvFire += "hotSpot triggered another advanceFire \n";
    		advanceFire(true);
    		
    	}
    	
    }

	private void clearExteriorFire() {
		for(int i = 0; i<8; i++) {
			gs.returnTile(i,0).setFire(0);
			gs.returnTile(i, 9).setFire(0);
		}
		for(int i = 0; i<10; i++) {
			gs.returnTile(0,i).setFire(0);
			gs.returnTile(7, i).setFire(0);
		}
		
	}

	public int[] nextTile(int x, int y, int direction) {
    	int[] result = new int[2];
    	if(y==8) {
    		if(x == 6) {
    			result[0] = 1;
    			result [1] = 1;
    			return result;
    		}
    		result[0] = x+1;
    		result[1] = 1;
    		return result;
    	}
    	result[0] = x;
    	result[1] = y+1;
    	return result;
    }

    /*
     * SAVING AND READING
     */
    
    //This one I save for our next meeting. key word *serialization* @matekrk
    public void saveGame() {
    	try {
			FileOutputStream fo = new FileOutputStream(new File("myGameState.txt"));
			ObjectOutputStream oo = new ObjectOutputStream(fo);

			// Write object to file
			oo.writeObject(gs);

			oo.close();
			fo.close();


		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
    }
    
    public GameState loadGame() {
    	try {
    		FileInputStream fi = new FileInputStream(new File("myGameState.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			GameState gs1 = (GameState) oi.readObject();

			System.out.println(gs1.toString());

			oi.close();
			fi.close();
			
			gs.updateGameStateFromObject(gs1);
			return gs1; //if not void
			
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			} catch (IOException e) {
				System.out.println("Error initializing stream");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	return null;
    }
    
    

    //any volunteers? I guess we can do that when GUI is done
    public void setOptions() {
        /* TODO: No message view defined */
    }

	public String getAdvFireMessage() {
		// TODO Auto-generated method stub
		return this.recentAdvFire;
	}
	
	public GameState getGameState() {
		System.out.println("Returning Game State");
		return gs;
	}
    
//    public static void main(String[] args) {
//    	GameManager gm = new GameManager();
//    	gm.runFlashpoint();
////    	gm.gs.toString();
//    	
//    }
 
}

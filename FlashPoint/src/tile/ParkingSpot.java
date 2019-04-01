package tile;

import java.io.Serializable;
import java.util.Arrays;

import token.Vehicle;

/**
 * ParkingSpot class definition.
 * @matekrk
 */
public class ParkingSpot implements Serializable{
    
	private static final long serialVersionUID = 1L;
	protected Vehicle parkingType;
    protected Tile[] tiles;
    boolean isCar;
    
    public ParkingSpot(Vehicle type, boolean b) {
    	this.parkingType  = type;
    	this.isCar = b;
    	this.tiles = new Tile[2];
    }
    
    public ParkingSpot(Tile[] position, Vehicle type, boolean b) {
    	this.tiles = position;
    	this.parkingType  = type;
    	this.isCar = b;
    }

    /*
     * Getters
     */
    public Vehicle getParkingType() {
        return parkingType;
    }
    
    public Tile[] getTiles() {
    	return tiles;
    }
    
    public boolean getCar() {
    	return isCar;
    }
    
    /*
     * Setters
     */
    public void setParkingType(Vehicle parkingType) {
    	this.parkingType = parkingType;
    }
    
    public void setTiles(Tile[] tiles) {
    	this.tiles = tiles;
    }
    
    public void setTile(Tile tile) {
    	if (tiles[0] == null) {
    		tiles[0] = tile;
    	}
    	else {
    		tiles[1] = tile;
    	}
    }
    
    public void setCar(boolean car) {
    	this.isCar = car;
    }

	@Override
	public String toString() {
		return "ParkingSpot [parkingType=" + parkingType.toString() + ", tiles=" + Arrays.toString(tiles) + ", isCar=" + isCar
				+ "]";
	}
    
    
}

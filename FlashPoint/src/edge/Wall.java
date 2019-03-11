package edge;

/**
 * Wall class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Wall extends Edge{
    
    protected int damage; //domain: 2 = undamaged 1 = damaged, 0 = destroyed
    
    public Wall() {
    	this.damage = 2; 
    }
    
    public Wall(int damage) {
    	this.damage = damage;
    }

    /*
     * Getters
     */
    
    public int getDamage() {
        return damage;
    }
    
    /*
     * Setters
     */
    
    //always chopping by 1
    public void chop() {
        if (damage > 0) {
        	damage--;
        }
    }


    public void destroyWall() {
        damage = 0;
    }
    
    /*
     * Questions
     */
    
    public boolean isWall() {
    	return true;
    }
    
    public Wall getWall() {
        return this;
    }
    
	@Override
	public boolean getStatus() {
		return false;
	}

	@Override
	public String toString() {
		return "Wall [damage=" + damage + "]";
	}

	@Override
	public void destroyDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void change() {
		// TODO Auto-generated method stub
		
	}
	
   public boolean isDestroyed() {
        return false;
    }
}

package edge;

// Start of user code for imports
import java.util.*;
// End of user code

/**
 * Wall class definition.
 * Generated by the TouchCORE code generator.
 * and modified by @matekrk
 */
public class Wall extends Edge{
    
    protected int damage; //domain: 0,1,2
    
    public Wall() {
    	this.damage = 0;
    }
    public Wall(int damage) {
    	this.damage = damage;
    }

    public void chop() {
        /* TODO: No message view defined */
    }

    public int getDamage() {
        /* TODO: No message view defined */
        return 0;
    }

    public void destroyWall() {
        /* TODO: No message view defined */
    }
    
    public boolean isWall() {
    	return true;
    }
    
    public Wall getWall() {
        return this;
    }
	@Override
	public boolean getStatus() {
		// TODO Auto-generated method stub
		return false;
	}
}

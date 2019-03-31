package token;

import java.io.Serializable;

public class POI extends Token implements Serializable {
    
    protected boolean revealed;
    protected boolean isVictim;
    protected boolean healed;
    private static final long serialVersionUID = 1L;
    protected Firefighter leader;
    
    /**
     * Constructor for Poi
     * @param isVictim Determines whether or not the POI is in fact a victim
     */
    public POI(boolean isVictim) {
       this.isVictim  = isVictim;
       this.leader = null;
    }

    /*
     * GETTERS
     */
    
    /**
     * Check if the POI is a victim
     * @return isVictim
     */
    public boolean isVictim() {
        return isVictim;
    }
    
    public boolean isRevealed() {
    	return revealed;
    }
    
	public boolean isHealed() {
    	return this.healed;
    }
	
	public void heal() {
		this.healed = !this.healed;
	}
    
    public boolean hasLeader() {
//    	return !follow.equals(null);
    	return !(leader == null);
    }
    
    public Firefighter getLeader() {
    	return leader;
    }
    
    public boolean checkStatus() {
      	return revealed;
    }

    /*
     * SETTERS
     */
    
    public void reveal() {
        //Invoke placing POI
    	System.out.println("REVEALING");
    	revealed = true;
    }
    
    public void setLeader(Firefighter f) {
    	assert isVictim == true;
    	leader = f;
    	//f.setVictim(this);
    }

	@Override
	public String toString() {
		return "POI [revealed=" + revealed + ", isVictim=" + isVictim + ", isHealed=" + healed + ", follow="
				+ leader.toString() + ", x=" + x + ", y=" + y + ", tileOn=" + tileOn.toString() + "]";
	}
}

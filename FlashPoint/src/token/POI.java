package token;

import java.io.Serializable;

public class POI extends Token implements Serializable {
    
    protected boolean revealed;
    protected boolean isVictim;
    protected boolean isRescued;
    private static final long serialVersionUID = 1L;
    protected Firefighter follow;
    
    /**
     * Constructor for Poi
     * @param isVictim Determines whether or not the POI is in fact a victim
     */
    public POI(boolean isVictim) {
       this.isVictim  = isVictim;
       this.follow = null;
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
    
    public boolean isResuscitated() {
    	return !follow.equals(null);
    }
    
    public Firefighter follower() {
    	return follow;
    }
    
    public boolean checkStatus() {
      	return revealed;
    }

    /*
     * SETTERS
     */
    
    public void reveal() {
        //Invoke placing POI
    	revealed = true;
    }
    
    public void setResuscitate(Firefighter f) {
    	assert isVictim == true;
    	follow = f;
    	//f.setVictim(this);
    }

	@Override
	public String toString() {
		return "POI [revealed=" + revealed + ", isVictim=" + isVictim + ", isRescued=" + isRescued + "]";
	}
}

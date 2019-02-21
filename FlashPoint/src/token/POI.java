package token;

public class POI {
    
    protected boolean revealed;
    protected boolean isVictim;
    protected boolean isRescued;
    
    /**
     * Constructor for Poi
     * @param isVictim Determines whether or not the POI is in fact a victim
     */
    public POI(boolean isVictim) {
       this.isVictim  = isVictim;
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

    /*
     * SETTERS
     */
    
    public void reveal() {
        //Invoke placing POI
    	revealed = true;
    }

    public void checkStatus() {
      	return revealed;
    }

	@Override
	public String toString() {
		return "POI [revealed=" + revealed + ", isVictim=" + isVictim + ", isRescued=" + isRescued + "]";
	}
}

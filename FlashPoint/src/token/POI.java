package token;

import java.io.Serializable;

public class POI implements Serializable {
    
    protected boolean revealed;
    protected boolean isVictim;
    protected boolean isRescued;
    private static final long serialVersionUID = 1L;
    
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
    
    public boolean isRevealed() {
    	return revealed;
    }

    /*
     * SETTERS
     */
    
    public void reveal() {
        //Invoke placing POI
    	revealed = true;
    }

    public boolean checkStatus() {
      	return revealed;
    }

	@Override
	public String toString() {
		return "POI [revealed=" + revealed + ", isVictim=" + isVictim + ", isRescued=" + isRescued + "]";
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}

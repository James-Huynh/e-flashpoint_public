package personalizedlisteners;

import java.util.EventListener;

/**
 * Listener class in charge of signaling frames and pages transition in the main launcher
 * @author James
 *
 */
public interface LoginListener extends EventListener {

	/**
	 * Listener that will transition the frame from the login screen to the main menu 
	 */
	public void clickLogin();	
	
	
	// more methods to be added...
	
	
}

package personalizedlisteners.mainMenuListeners;

import java.util.EventListener;
/**
 * 
 * @author zaidyahya
 *
 */
public interface MainMenuListener extends EventListener {
	public void clickCreate();	
	
	/**
	 * Find lobby button from the main menu
	 */
	public void clickFind(); // James
}

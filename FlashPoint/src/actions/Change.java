package actions;

import game.GameState;
import tile.ParkingSpot;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;

/*
 * @matekrk
 */

public class Change extends Action {

	protected Speciality toSpeciality;
	protected ActionList title = ActionList.Change;
	private static final long serialVersionUID = 1L;
	
	//constr
	public Change(Speciality toSpeciality) {
		this.APcost = 2;
		this.toSpeciality = toSpeciality;
	}
	
    public ActionList getTitle() {
    	return title;
    }
	
	@Override
	public void perform(GameState gs) {
		Firefighter current = gs.getPlayingFirefighter();
		gs.getFreeSpecialities().add(current.getSpeciality());
		gs.getFreeSpecialities().remove(toSpeciality);
		gs.setFirefighterSpeciality(current, toSpeciality);
		current.setAP(current.getAP()-this.APcost);
		current.setUsedAP(true);
	}

	@Override
	public boolean validate(GameState gs) {
		Firefighter current = gs.getPlayingFirefighter();
		boolean flag = false;
//
//		if (current.getAP() >= APcost && current.getCurrentPosition().getParkingSpot().getParkingType() == Vehicle.Engine && current.getCurrentPosition().getParkingSpot().getCar() == true) {
//			if (gs.getFreeSpecialities().contains(toSpeciality) || toSpeciality == (null)) {
//				flag = true;
//			}
//		}
		//New one
		if(current.getCurrentPosition().getParkingSpot() != null) {
			ParkingSpot p = current.getCurrentPosition().getParkingSpot();
			if(p.getParkingType() == Vehicle.Engine && p.getCar() == true) {
				if(current.getAP() >= APcost) {
					if (gs.getFreeSpecialities().contains(toSpeciality) || toSpeciality == (null)) {
						flag = true;
					}
				}
			}
		}
		System.out.print("The flag is");
		System.out.println(flag);
		return flag;
	}
	
	@Override
	public void adjustAction(GameState gs) {
		
	}
	
	@Override
	public Speciality getToSpecialty() {
		return this.toSpeciality;
	}
	
	@Override
	public String toString() {
		return "Change [toSpeciality=" + toSpeciality + ", title=" + title + ", APcost=" + APcost + ", direction="
				+ direction + "]";
	}
	
	
}

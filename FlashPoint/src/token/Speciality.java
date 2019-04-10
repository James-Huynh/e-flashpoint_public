package token;

//@matekrk
// default is null (no speciality

public enum Speciality {
		PARAMEDIC,
		CAPTAIN,
		IMAGING_TECHNICIAN,
		CAFS,
		HAZMAT_TECHNICIAN,
		GENERALIST,
		RESCUE_SPECIALIST,
		DRIVER,
		DOG,
		VETERAN,
		BOBTHEBUILDER;
		
		public String toString() {
			if(this == PARAMEDIC) {
				return "Paramedic";
			} else if (this == CAPTAIN) {
				return "Captain";
			} else if (this == IMAGING_TECHNICIAN) {
				return "Imaging Technician";
			} else if (this == CAFS) {
				return "CAFS";
			} else if (this == HAZMAT_TECHNICIAN) {
				return "Hazmat Technician";
			} else if (this == GENERALIST) {
				return "Generalist";
			}  else if (this == RESCUE_SPECIALIST) {
				return "Rescue Specialist";
			} else if (this == DRIVER) {
				return "Driver";
			} else if (this == DOG) {
				return "Dog";
			} else if (this == VETERAN) {
				return "Veteran";
			} else if (this == BOBTHEBUILDER) {
				return "Bob The Builder";
			} else {
				return "None";
			}
		}
}
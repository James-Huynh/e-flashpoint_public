package game;

public class FamilyGame extends TemplateGame {
	
	protected int[][] fireLocationAndPOILocations;
	protected int[][] edgeLocations;
	
	
	public FamilyGame() {
	
	//21/9 10/8
	
	//Set initially no fire & no POI in tile
	for (int i=0; i<=9; i++) {
		for (int j=0; j<=7; j++) {
			fireLocationAndPOILocations[i][j] = 0;
		}
	}
	
	//10 Fire locations
	fireLocationAndPOILocations[2][2] = 1;
	fireLocationAndPOILocations[3][2] = 1;
	fireLocationAndPOILocations[2][3] = 1;
	fireLocationAndPOILocations[3][3] = 1;
	fireLocationAndPOILocations[4][3] = 1;
	fireLocationAndPOILocations[4][4] = 1;
	fireLocationAndPOILocations[5][3] = 1;
	fireLocationAndPOILocations[6][5] = 1;
	fireLocationAndPOILocations[6][6] = 1;
	fireLocationAndPOILocations[7][5] = 1;
	//3 POI locations
	fireLocationAndPOILocations[4][2] = 2;
	fireLocationAndPOILocations[1][5] = 2;
	fireLocationAndPOILocations[8][5] = 2;

	//Set initially with all no edges except bottom edge
		for (int i=0; i<=20; i++) {
			for (int j=0; j<=8; j++) {
			if (j==8 && ((i+1)%2 == 0) ) edgeLocations[i][j] = -1; //if side edges of bottom edge, then set unused edges -1
				else edgeLocations[i][j] = 0;
			}
		}
	
		//51 Wall locations
		edgeLocations[3][1] = 1;
		edgeLocations[5][1] = 1;
		edgeLocations[7][1] = 1;
		edgeLocations[9][1] = 1;
		edgeLocations[11][1] = 1;
		edgeLocations[15][1] = 1;
		edgeLocations[17][1] = 1;
		
		edgeLocations[7][3] = 1;
		edgeLocations[9][3] = 1;
		edgeLocations[11][3] = 1;
		edgeLocations[13][3] = 1;
		edgeLocations[15][3] = 1;
	
		edgeLocations[3][5] = 1;
		edgeLocations[5][5] = 1;
		edgeLocations[7][5] = 1;
		edgeLocations[11][5] = 1;
		edgeLocations[13][5] = 1;
		edgeLocations[15][5] = 1;
		edgeLocations[17][5] = 1;
		
		edgeLocations[3][7] = 1;
		edgeLocations[5][7] = 1;
		edgeLocations[7][7] = 1;
		edgeLocations[9][7] = 1;
		edgeLocations[11][7] = 1;
		edgeLocations[13][7] = 1;
		edgeLocations[15][7] = 1;
		edgeLocations[17][7] = 1;
		
		edgeLocations[2][1] = 1;
		edgeLocations[2][2] = 1;
		edgeLocations[2][4] = 1;
		edgeLocations[2][5] = 1;
		edgeLocations[2][6] = 1;
		
		edgeLocations[18][1] = 1;
		edgeLocations[18][2] = 1;
		edgeLocations[18][3] = 1;
		edgeLocations[18][5] = 1;
		edgeLocations[18][6] = 1;
		
		edgeLocations[1][12] = 1;
		edgeLocations[2][8] = 1;
		edgeLocations[3][14] = 1;
		edgeLocations[4][6] = 1;
		edgeLocations[5][12] = 1;
		edgeLocations[5][16] = 1;
		
		//8 closed door location
		edgeLocations[9][5] = 2;
		edgeLocations[17][3] = 2;
		edgeLocations[3][6] = 2;
		edgeLocations[1][8] = 2;
		edgeLocations[2][12] = 2;
		edgeLocations[4][14] = 2;
		edgeLocations[6][12] = 2;
		edgeLocations[6][16] = 2;
	
	}
	//Getters and setters
	public int[][] getFireLocationAndPOILocations() {
        return fireLocationAndPOILocations;
    }
    public int[][] getEdgeLocations() {
        return edgeLocations;
    }
    
    
}


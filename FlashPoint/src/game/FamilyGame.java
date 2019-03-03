package game;

public class FamilyGame extends TemplateGame {
	
	protected int[][] tokenLocations = new int[8][10];
	protected int[][] edgeLocations = new int[9][21]; 	// subject to change
	
	
	public FamilyGame() {
	
	//21/9 10/8
	
	//Set initially no fire & no POI in tile
	for (int i=0; i<8; i++) {
		for (int j=0; j<10; j++) {
			tokenLocations[i][j] = 0;
		}
	}
	
	//10 Fire locations
	tokenLocations[2][2] = 1;
	tokenLocations[2][3] = 1;
	tokenLocations[3][2] = 1;
	tokenLocations[3][3] = 1;
	tokenLocations[3][4] = 1;
	tokenLocations[4][4] = 1;
	tokenLocations[3][5] = 1;
	tokenLocations[5][6] = 1;
	tokenLocations[6][6] = 1;
	tokenLocations[5][7] = 1;
	//3 POI locations
	tokenLocations[2][4] = 2;
	tokenLocations[5][1] = 2;
	tokenLocations[5][8] = 2;

	//Set initially with all no edges except bottom edge
		for (int i=0; i<9; i++) {
			for (int j=0; j<21; j++) {
				edgeLocations[i][j] = 0;
//			if (j==8 && ((i+1)%2 == 0) ) edgeLocations[i][j] = -1; //if side edges of bottom edge, then set unused edges -1
//				else edgeLocations[i][j] = 0;
			}
		}
	
		//51 Wall locations
		edgeLocations[1][3] = 1;
		edgeLocations[1][5] = 1;
		edgeLocations[1][7] = 1;
		edgeLocations[1][9] = 1;
		edgeLocations[1][11] = 1;
		edgeLocations[1][15] = 1;
		edgeLocations[1][17] = 1;
		
		edgeLocations[3][7] = 1;
		edgeLocations[3][9] = 1;
		edgeLocations[3][11] = 1;
		edgeLocations[3][13] = 1;
		edgeLocations[3][15] = 1;
	
		edgeLocations[5][3] = 1;
		edgeLocations[5][5] = 1;
		edgeLocations[5][7] = 1;
		edgeLocations[5][11] = 1;
		edgeLocations[5][13] = 1;
		edgeLocations[5][15] = 1;
		edgeLocations[5][17] = 1;
		
		edgeLocations[7][3] = 1;
		edgeLocations[7][5] = 1;
		edgeLocations[7][7] = 1;
		edgeLocations[7][9] = 1;
		edgeLocations[7][11] = 1;
		edgeLocations[7][13] = 1;
		edgeLocations[7][15] = 1;
		edgeLocations[7][17] = 1;
		
		edgeLocations[1][2] = 1;
		edgeLocations[2][2] = 1;
		edgeLocations[4][2] = 1;
		edgeLocations[5][2] = 1;
		edgeLocations[6][2] = 1;
		
		edgeLocations[1][18] = 1;
		edgeLocations[2][18] = 1;
		edgeLocations[3][18] = 1;
		edgeLocations[5][18] = 1;
		edgeLocations[6][18] = 1;
		
		edgeLocations[1][12] = 1;
		edgeLocations[2][8] = 1;
		edgeLocations[3][14] = 1;
		edgeLocations[4][6] = 1;
		edgeLocations[5][12] = 1;
		edgeLocations[5][16] = 1;
		
		//house entrences... unclear if they are in the game or no
		edgeLocations[1][13] = 2;
		edgeLocations[4][18] = 2;
		edgeLocations[3][2] = 2;
		edgeLocations[7][7] = 2;
		
		//8 closed door location
		edgeLocations[5][9] = 2;
		edgeLocations[3][17] = 2;
		edgeLocations[3][6] = 2;
		edgeLocations[1][8] = 2;
		edgeLocations[2][12] = 2;
		edgeLocations[4][14] = 2;
		edgeLocations[6][12] = 2;
		edgeLocations[6][16] = 2;
		
		//negtive edges
		edgeLocations[8][0] = -1;
		edgeLocations[8][2] = -1;
		edgeLocations[8][4] = -1;
		edgeLocations[8][6] = -1;
		edgeLocations[8][8] = -1;
		edgeLocations[8][10] = -1;
		edgeLocations[8][12] = -1;
		edgeLocations[8][14] = -1;
		edgeLocations[8][16] = -1;
		edgeLocations[8][18] = -1;
		edgeLocations[8][20] = -1;
		
	}
	//Getters and setters
	public int[][] getTokenLocations() {
        return tokenLocations;
    }
    public int[][] getEdgeLocations() {
        return edgeLocations;
    }
    
    
}


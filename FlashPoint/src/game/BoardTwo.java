package game;

public class BoardTwo extends TemplateGame{

	/**
	 * Junha : 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int[][] edgeLocations = new int[9][21];
	protected int[][] POILocations = new int[8][10];
	protected int[][] fireLocations = new int[8][10];
	private String difficultyLevel; //Family or Experienced
//	private String subLevel; //Recruit, Heroic or Veteran
	
	public BoardTwo(String difficulty) { //Constructor for Family since it will not have subLevel
		this.setDifficultyLevel(difficulty);
		setupBoard();
	}
	
	public void setupBoard() {
		if(this.difficultyLevel.equals("Family")) {
			setEdgeLocations();
			setPOILocations();
			setFireLocations();
		}
		else {
			setEdgeLocations();
		}
	}

	
	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}


	@Override
	public int[][] getFireLocations() {
		// TODO Auto-generated method stub
		return fireLocations;
	}
	
	@Override
	public int[][] getPOILocations() {
		// TODO Auto-generated method stub
		return POILocations;
	}
	
	@Override
	public int[][] getEdgeLocations() {
		// TODO Auto-generated method stub
		return edgeLocations;
	}
	
	public void setPOILocations() {
		POILocations[2][4] = 1;
		POILocations[5][1] = 1;
		POILocations[5][8] = 1;
	}
	
	public void setFireLocations() {
		//10 Fire locations
		fireLocations[2][2] = 2;
		fireLocations[2][3] = 2;
		fireLocations[3][2] = 2;
		fireLocations[3][3] = 2;
		fireLocations[3][4] = 2;
		fireLocations[4][4] = 2;
		fireLocations[3][5] = 2;
		fireLocations[5][6] = 2;
		fireLocations[6][6] = 2;
		fireLocations[5][7] = 2;
	}
	
	public void setEdgeLocations() {
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

}

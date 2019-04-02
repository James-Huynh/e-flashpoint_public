package game;

public class RandomBoardFour extends TemplateGame{

	
private static final long serialVersionUID = 1L;
	
	protected int[][] edgeLocations = new int[9][21];
	protected int[][] POILocations = new int[8][10];
	protected int[][] fireLocations = new int[8][10];
	private String difficultyLevel; //Family or Experienced
//	private String subLevel; //Recruit, Heroic or Veteran
	
	public RandomBoardFour(String difficulty) { //Constructor for Family since it will not have subLevel
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
			System.out.println("helloooo");
			
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
		
		for (int i=0; i<9; i++) {
			for (int j=0; j<21; j++) {
				edgeLocations[i][j] = 0;
			}
		}
	
		//51 Wall locations
				//exterior tile top
				edgeLocations[1][3] = 1;
				edgeLocations[1][5] = 1;
				edgeLocations[1][7] = 1;
				edgeLocations[1][9] = 1;
				//edgeLocations[1][11] = 1;
				edgeLocations[1][13] = 1;
				edgeLocations[1][15] = 1;
				edgeLocations[1][17] = 1;		
				//exterior tile bot
				edgeLocations[7][3] = 1;
				edgeLocations[7][5] = 1;
				edgeLocations[7][7] = 1;
				//edgeLocations[7][9] = 1;
				edgeLocations[7][11] = 1;
				edgeLocations[7][13] = 1;
				edgeLocations[7][15] = 1;
				edgeLocations[7][17] = 1;
				//exterior tile left
				edgeLocations[1][2] = 1;
				edgeLocations[2][2] = 1;
				//edgeLocations[3][2] = 1;
				edgeLocations[4][2] = 1;
				edgeLocations[5][2] = 1;
				edgeLocations[6][2] = 1;
				//exterior tile right
				edgeLocations[1][18] = 1;
				edgeLocations[2][18] = 1;
				edgeLocations[3][18] = 1;
				//edgeLocations[4][18] = 1;
				edgeLocations[5][18] = 1;
				edgeLocations[6][18] = 1;
		
				//number three
		edgeLocations[2][5] = 1;
		edgeLocations[2][7] = 1;
		edgeLocations[2][8] = 1;
		edgeLocations[3][8] = 1;
		edgeLocations[4][8] = 1;
		edgeLocations[5][8] = 1;
		edgeLocations[6][5] = 1;
		edgeLocations[6][7] = 1;
		edgeLocations[4][5] = 1;
		edgeLocations[4][7] = 1;
		
		//number six
		edgeLocations[2][11] = 1;
		edgeLocations[2][13] = 1;
		edgeLocations[2][10] = 1;
		edgeLocations[3][10] = 1;
		edgeLocations[4][10] = 1;
		edgeLocations[5][10] = 1;
		edgeLocations[6][11] = 1;
		edgeLocations[6][13] = 1;
		edgeLocations[5][14] = 1;
		edgeLocations[4][14] = 1;
		edgeLocations[4][11] = 1;
		edgeLocations[4][13] = 1;
		
		//number one
		edgeLocations[2][16] = 1;
		edgeLocations[3][16] = 1;
		edgeLocations[4][16] = 1;
		edgeLocations[5][16] = 1;
		
		
		
		//house entrences... unclear if they are in the game or no
		edgeLocations[3][2] = 2;
		edgeLocations[1][11] = 2;
		edgeLocations[4][18] = 2;
		edgeLocations[7][9] = 2;
		
		// 0 closed door location
		
		
		
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

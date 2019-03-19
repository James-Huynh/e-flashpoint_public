package game;

public class AdvancedGame extends TemplateGame{

	/**
	 * Junha : 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int[][] tokenLocations = new int[8][10];
	protected int[][] edgeLocations = new int[9][21];
	
	public AdvancedGame(String mode, int board) {
		if(board == 0)
	}
	
	@Override
	public int[][] getFireLocations() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int[][] getPOILocations() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int[][] getEdgeLocations() {
		// TODO Auto-generated method stub
		return null;
	}

}

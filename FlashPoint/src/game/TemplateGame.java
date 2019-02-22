package game;

/**
 * TemplateGame class definition.
 * Generated by the TouchCORE code generator.
 */
abstract public class TemplateGame {
     
    /*protected int nbFires;
    protected int nbPoi;
    protected int nbVictims;*/

	protected int[][] fireLocationAndPOILocations;
    protected int[][] edgeLocations;
    
    public abstract int[][] getFireLocationAndPOILocations();

    public abstract int[][] getEdgeLocations();
}

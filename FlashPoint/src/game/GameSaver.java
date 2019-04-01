package game;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import game.GameState;

/**
 * 
 * @author Eric
 *
 */
public class GameSaver {
	private static String defaulGamesPath = "savedGames/";

	private static String filename="save01";
	private static String directory="D:\\save";
	public  void start(GameState gs) throws IOException {
		
		
	//	saveObjectByObjectOutput(gs,createFile(filename));
    
	}


	public static void saveObjectByObjectOutput(Object o/*, File file*/) {
        try {
        	int savedGameNumber = new File(defaulGamesPath).listFiles().length; //check how many games are already saved
        	FileOutputStream f = new FileOutputStream(new File(defaulGamesPath + (savedGameNumber++) + ".txt")); //save it as "savedGame#.txt"
        	ObjectOutputStream ob = new ObjectOutputStream(f);
            ob.writeObject(o);
       
			
			ob.close();
			f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	

}


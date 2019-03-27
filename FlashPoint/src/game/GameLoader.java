package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;

/**
 * 
 * @author Eric
 *
 */
public class GameLoader {

   
	 public static Object getObjectByObjectInput(File file) {
	        try {
	            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
	            Object o = inputStream.readObject();
	            inputStream.close();
	            return o;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


 

}



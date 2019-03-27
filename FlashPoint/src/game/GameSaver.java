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
	/**
	 *
	 * 
	 * @throws IOException
	 */

	private static String filename="save01";
	private static String directory="D:\\save";
	public  void start(GameState gs) throws IOException {
		
		saveObjectByObjectOutput(gs,createFile(filename));

	}



 





    public static File createFile(String name) {
        String separator= File.separator;
        String filename=name;
           
        File file=new File(directory,filename);
        if(file.exists()){
        	 System.out.println("file aleardy exist");
            System.out.println("name:"+file.getAbsolutePath());
            System.out.println("size£º"+file.length());
        }
        else {
   
        	file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   return file;
    }


	public static void saveObjectByObjectOutput(Object o, File file) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	/**
	 * creating file
	 * 
	 * @throws IOException
	 */

	



	/**

	 * writing fire

	 * 

	 * @param newStr

	 *           

	 * @throws IOException

	 */




}


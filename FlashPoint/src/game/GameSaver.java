package game;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStreamReader;

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
	private static String path = "D:/";
	private static String filenameTemp;

	public  void start(GameState gs) throws IOException {
		GameSaver.creatTxtFile("save01");
		GameSaver.writeTxtFile("save01", gs);

	}





	/**
	 * creating file
	 * 
	 * @throws IOException
	 */

	public static boolean creatTxtFile(String name) throws IOException {
		boolean flag = false;
		
		filenameTemp = path + name + ".txt";
		File filename = new File(filenameTemp);
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;

	}



	/**

	 * writing fire

	 * 

	 * @param newStr

	 *           

	 * @throws IOException

	 */

	public static boolean writeTxtFile(String newStr, GameState gs) throws IOException {



		boolean flag = false;



		String temp = "";



		FileInputStream fis = null;

		InputStreamReader isr = null;

		BufferedReader br = null;



		FileOutputStream fos = null;

		PrintWriter pw = null;

		try {



			File file = new File(filenameTemp);
			//
			//			
			//
			//				fis = new FileInputStream(file);
			//
			//				isr = new InputStreamReader(fis);
			//
			//				br = new BufferedReader(isr);
			//
			//				StringBuffer buf = new StringBuffer();
			//
			//	 
			//
			//			
			//
			//				for (int j = 1; (temp = br.readLine()) != null; j++) {
			//
			//					buf = buf.append(temp);
			//
			//				
			//
			//					buf = buf.append(System.getProperty("line.separator"));
			//
			//				}
			//
			//				buf.append(filein);




			fos = new FileOutputStream(file);

			pw = new PrintWriter(fos);

			pw.write(gs.toString().toCharArray());

			pw.flush();

			flag = true;

		} catch (IOException e1) {



			throw e1;

		} finally {

			if (pw != null) {

				pw.close();

			}

			if (fos != null) {

				fos.close();

			}

			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}



}


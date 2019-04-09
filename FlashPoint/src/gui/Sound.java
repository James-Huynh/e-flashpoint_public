package gui;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
	
	   // HOTSPOT("sound/hotspot.wav");   // hotspot
	   
	   public static Volume volume = Volume.LOW;
	   
	   // Each sound effect has its own clip, loaded with its own sound file.
	   private Clip clip;
	   
	   // Constructor to construct each element of the enum with its own sound file.
	   Sound(String soundFileName) {
	      try {
	    	  /*
	         // Use URL (instead of File) to read from disk and JAR.
	         URL url = this.getClass().getClassLoader().getResource(soundFileName);
	         // Set up an audio input stream piped from the sound file.
	         final File audioFile = new File(soundFileName);
	         final InputStream in = new FileInputStream( soundFileName );
	         final AudioInputStream ais = AudioSystem.getAudioInputStream( in );
	         AudioInputStream audioInputStream = ais; // AudioSystem.getAudioInputStream(url);
	         // Get a clip resource.
	         clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioInputStream);
	         */
	    	    File yourFile = new File(soundFileName);
	    	    AudioInputStream stream;
	    	    AudioFormat format;
	    	    DataLine.Info info;
	    	    //Clip clip;

	    	    stream = AudioSystem.getAudioInputStream(yourFile);
	    	    format = stream.getFormat();
	    	    info = new DataLine.Info(Clip.class, format);
	    	    clip = (Clip) AudioSystem.getLine(info);
	    	    clip.open(stream);
	    	    //clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   // Play or Re-play the sound effect from the beginning, by rewinding.
	   public void play() {
	      if (volume != Volume.MUTE) {
	         if (clip.isRunning())
	            clip.stop();   // Stop the player if it is still running
	         clip.setFramePosition(0); // rewind to the beginning
	         clip.start();     // Start playing
	      }
	   }
	   
	   // Optional static method to pre-load all the sound files.
	   static void init() {
	      //values(); // calls the constructor for all the elements
		  Sound HOTSPOT = new Sound("sound/hotspot.wav");
	   }
	   
	   public static Sound getSound() {
		   Sound HOTSPOT = new Sound("sound/hotspot.wav");
		   return HOTSPOT;
	   }
	   
	   
	}
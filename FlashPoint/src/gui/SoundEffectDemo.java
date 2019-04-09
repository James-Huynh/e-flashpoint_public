package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
   
// Testing the SoundEffect enum in a Swing application
public class SoundEffectDemo extends JFrame {
   
   // Constructor
   public SoundEffectDemo() {
      // Pre-load all the sound files
      Sound.init();
      Sound.volume = Volume.MEDIUM;  // un-mute
   
      // Set up UI components
      Container cp = this.getContentPane();
      cp.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
      JButton btnSound1 = new JButton("Sound 1 - Hot Spot");
      btnSound1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
        	 Sound.getSound().play();
         }
      });
      cp.add(btnSound1);
      JButton btnSound2 = new JButton("Sound 2 - Mute");
      btnSound2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
        	 Sound.volume = Volume.MUTE;
        	 Sound.getSound().play();
         }
      });
      cp.add(btnSound2);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("Test SoundEffct");
      this.pack();
      this.setVisible(true);
   }
   
   public static void main(String[] args) {
      new SoundEffectDemo();
   }
}
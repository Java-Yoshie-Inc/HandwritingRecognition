package main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HandwritingRecognition {
	
	private JFrame frame;
	private JPanel panel;
	
	public HandwritingRecognition() {
		frame = new JFrame("Handwriting Recognition Beta");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(3);
		frame.setResizable(false);
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {
				
			}
		};
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new HandwritingRecognition();
	}
	
}

package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HandwritingRecognition {

	private JFrame frame;
	private JPanel panel;

	private BufferedImage image;

	public HandwritingRecognition() {
		setupNetwork();

		frame = new JFrame("Handwriting Recognition Beta");
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
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

	private void setupNetwork() {
		neuralNetwork = new NeuralNetwork(ActivationFunctionType.SIGMOID2, new Range(-1, 1), 0, new int[] {22500, 1000, 300, 50, 10});
	}

	public static void main(String[] args) {
		new HandwritingRecognition();
	}

}

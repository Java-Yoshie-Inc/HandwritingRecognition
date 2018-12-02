package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

import neural_network.ActivationFunctionType;
import neural_network.NeuralNetwork;
import neural_network.Range;

public class HandwritingRecognition {

	private JFrame frame;
	private Font outputFont, font;
	private Timer timer;

	private JPanel panel;
	private JPanel drawPanel;
	private JPanel outputPanel;
	private JLabel outputText;

	private JButton resetButton;

	private BufferedImage image;
	private int penSize = 10;

	private NeuralNetwork neuralNetwork;
	private final int NN_IMAGE_SIZE = 100;

	public HandwritingRecognition() {
		setupNetwork();
		trainNetwork();
		setupFrame();
	}

	private void setupFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		resetImage();
		outputFont = new Font("Arial", Font.PLAIN, 192);
		font = new Font("Arial", Font.PLAIN, 20);

		frame = new JFrame("Handwriting Recognition Beta");
		frame.setSize(545, 360);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);

		panel = new JPanel();
		panel.setLayout(null);

		resetButton = new JButton("Reset canvas");
		resetButton.setBounds(60, 265, 425, 40);
		resetButton.setFont(font);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetImage();
			}
		});

		outputPanel = new JPanel();
		outputPanel.setBounds(300, 40, 200, 200);
		outputPanel.setBackground(Color.WHITE);
		outputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		outputPanel.setLayout(new GridLayout(1, 1));

		outputText = new JLabel("0");
		outputText.setBackground(Color.WHITE);
		outputText.setHorizontalAlignment(JLabel.CENTER);
		outputText.setFont(outputFont);
		outputPanel.add(outputText);

		drawPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 200, 200, null);
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, 200 - 1, 200 - 1);
			}
		};
		drawPanel.setBounds(40, 40, 200, 200);
		drawPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX(), y = e.getY();

				if (x > -penSize && x < 200 && y > -penSize && y < 200) {
					Graphics2D graphics = image.createGraphics();
					graphics.setColor(Color.BLACK);
					graphics.fillOval(x, y, penSize, penSize);
				}
			}
		});

		panel.add(drawPanel);
		panel.add(outputPanel);
		panel.add(resetButton);

		frame.add(panel);
		frame.setVisible(true);

		timer = new Timer(15, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawPanel.repaint();
			}
		});
		timer.start();
	}

	private void setupNetwork() {
		neuralNetwork = new NeuralNetwork(ActivationFunctionType.SIGMOID2, new Range(-1, 1), 0, new int[] { NN_IMAGE_SIZE * NN_IMAGE_SIZE, NN_IMAGE_SIZE, 10 });
	}

	private void trainNetwork() {
		for(int i=0; i<1; i++) {
			for(File file : new File("data").listFiles()) {
				System.out.println(file.toString());
				try {
					BufferedImage image = ImageIO.read(file);

					double[] input = convertArray(convertImage(image));
					double[] target = new double[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0};

					System.out.println("training...");
					neuralNetwork.train(input, target, 0.5);
					System.out.println("end");

					/*for(int y=0; y<colors.length; y++) {
						for(int x=0; x<colors.length; x++) {
							System.out.print(colors[x][y]);
							System.out.print(" ");
						}
						System.out.println();
					}*/
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int[] convertImage(BufferedImage image) {
		int[][] colors = new int[image.getWidth()][image.getHeight()];
		for(int x=0; x<colors.length; x++) {
			for(int y=0; y<colors[x].length; y++) {
				Color color = new Color(image.getRGB(x, y));
				if(color.equals(Color.BLACK)) {
					colors[x][y] = 0;
				} else {
					colors[x][y] = 1;
				}
			}
		}
		return convertArrayDimension(colors);
	}

	private int[] convertArrayDimension(int[][] array) {
		List<Integer> output = new ArrayList<>();
		for(int x=0; x<array.length; x++) {
			for(int y=0; y<array[x].length; y++) {
				output.add(array[x][y]);
			}
		}
		Integer[] output2 = output.toArray(new Integer[0]);
		int[] output3 = new int[output2.length];
		for(int i=0; i < output3.length; i++) {
			output3[i] = output2[i];
		}
		return output3;
	}

	private double[] convertArray(int[] array) {
		double[] output = new double[array.length];
		for(int i=0; i<output.length; i++) {
			output[i] = array[i];
		}
		return output;
	}

	private void resetImage() {
		neuralNetwork.start(convertArray(convertImage(Tools.scaleImage(image, NN_IMAGE_SIZE, NN_IMAGE_SIZE))));
		System.out.println(Arrays.toString(neuralNetwork.getOutput()));

		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 200, 200);
	}

	public static void main(String[] args) {
		new HandwritingRecognition();
	}

}

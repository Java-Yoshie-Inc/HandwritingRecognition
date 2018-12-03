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
import java.util.Arrays;

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
	private int penSize = 15;
	
	private NeuralNetwork neuralNetwork;
	private final int NN_IMAGE_SIZE = 25;

	public HandwritingRecognition() {
		setupNetwork();
		trainNetwork();
		setupFrame();
		resetImage();
	}

	private void setupFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		outputFont = new Font("Arial", Font.PLAIN, 150);
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

		outputText = new JLabel(" ");
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
		File[] files = new File("data").listFiles();
		int loops = 100;
		
		int max = loops * files.length;
		int current = 0;
		double learningRate = 0.1;
		
		for(int i=0; i<loops; i++) {
			File[] shuffledFiles = Tools.shuffleFileArray(files);
			
			for(File file : shuffledFiles) {
				current++;
				
				int percentage = Math.round(100f / ((float) max / current));
				String fileName = file.getName();
				int number = Integer.parseInt(fileName.split("-")[0]);
				
				System.out.println(current + "/" + max + " - " + percentage + "% - training " + number + " with file " + fileName);
				
				try {
					BufferedImage image = Tools.scaleImage(ImageIO.read(file), NN_IMAGE_SIZE, NN_IMAGE_SIZE);
					
					double[] input = Tools.convertArray(Tools.convertImage(image));
					double[] target = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					target[number] = 1;

					neuralNetwork.train(input, target, learningRate);
					
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
		System.out.println();
	}

	private void resetImage() {
		BufferedImage scaledImage = Tools.scaleImage(image, NN_IMAGE_SIZE, NN_IMAGE_SIZE);
		try {
			ImageIO.write(scaledImage, "png", new File("recognized.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		neuralNetwork.start(Tools.convertArray(Tools.convertImage(scaledImage)));
		double[] output = neuralNetwork.getOutput();
		
		double number = 0;
		int index = 0;
		
		for(int i=0; i<output.length; i++) {
			if(output[i] > number) {
				number = output[i];
				index = i;
			}
		}
		
		outputText.setText(String.valueOf(index));
		
		System.out.println(neuralNetwork);
		
		System.out.println(Arrays.toString(output));
		System.out.println(index);
		System.out.println();

		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 200, 200);
	}

	public static void main(String[] args) {
		new HandwritingRecognition();
	}

}

package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;

public class HandwritingRecognition {
	
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	
	private JFrame frame;
	private JPanel panel;
	
	private JPanel ioPanel;
	private JPanel drawPanel;
	private JPanel outputPanel;
	
	private ProbabilityPanel probabilityPanel;
	
	private JPanel optionsPanel;
	
	private JLabel outputText;
	private JButton resetButton;

	private BufferedImage image;
	private Font outputFont, font;
	private Timer timer;
	
	private final AI ai;

	public HandwritingRecognition() {
		this.ai = new AI();
		
		image = null;
		outputFont = new Font("Arial", Font.PLAIN, 150);
		font = new Font("Arial", Font.PLAIN, 20);
		
		setupFrame();
		adaptImage();
		reset();
		loop();
	}

	private void setupFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame("Handwriting Recognition Beta");
		frame.setSize(910, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setBackground(Color.WHITE);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		setupIOPanel();
		setupOptionsPanel();
		this.probabilityPanel = new ProbabilityPanel();
		
		panel.add(ioPanel, BorderLayout.CENTER);
		panel.add(optionsPanel, BorderLayout.SOUTH);
		panel.add(probabilityPanel, BorderLayout.EAST);
		
		frame.add(panel);
		
		frame.setVisible(true);
	}
	
	private void setupIOPanel() {
		ioPanel = new JPanel();
		ioPanel.setLayout(new GridLayout(1, 2));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25), BorderFactory.createLineBorder(Color.BLACK)));
		ioPanel.add(inputPanel, 0, 0);
		
		drawPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				adaptImage();
				g.drawImage(image, 0, 0, drawPanel.getWidth(), drawPanel.getHeight(), null);
			}
		};
		drawPanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX(), y = e.getY();
				
				adaptImage();
				
				int penSize = (int) (((image.getWidth() + image.getHeight()) / 2d) / 13d);
				
				if (x > -penSize && x < image.getWidth() && y > -penSize && y < image.getHeight()) {
					Graphics2D graphics = image.createGraphics();
					graphics.setColor(Color.BLACK);
					graphics.fillOval(x, y, penSize, penSize);
				}
			}
		});
		inputPanel.add(drawPanel, BorderLayout.CENTER);
		
		outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		outputPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25), BorderFactory.createLineBorder(Color.BLACK)));
		ioPanel.add(outputPanel, 0, 1);

		outputText = new JLabel(" ");
		outputText.setBackground(Color.WHITE);
		outputText.setHorizontalAlignment(JLabel.CENTER);
		outputText.setFont(outputFont);
		outputPanel.add(outputText, BorderLayout.CENTER);
	}
	
	private void setupOptionsPanel() {
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout());
		
		JButton trainButton = new JButton("Train Neural Network");
		trainButton.setFont(font);
		trainButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ai.train(1);
				JOptionPane.showMessageDialog(frame, "Training finished");
			}
		});
		optionsPanel.add(trainButton);
		
		resetButton = new JButton("Reset canvas");
		resetButton.setFont(font);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		optionsPanel.add(resetButton);
	}
	
	private void adaptImage() {
		if(image == null) {
			image = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			return;
		} if(image.getWidth() != drawPanel.getWidth() || image.getHeight() != drawPanel.getHeight()) {
			image = Tools.scaleImage(image, drawPanel.getWidth(), drawPanel.getHeight());
			return;
		}
	}
	
	private void loop() {
		timer = new Timer(15, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawPanel.repaint();
			}
		});
		timer.start();
	}

	private void reset() {
		Output output = ai.calculate(image);
		
		if(output.getHighestProbability() >= 0.92) {
			try {
				ImageIO.write(image, "png", new File("data-unchecked/" + output.getNumber() + "-" + System.currentTimeMillis() + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		probabilityPanel.setOutput(output);
		outputText.setText(String.valueOf(output.getNumber()));
		
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
	}

	public static void main(String[] args) {
		new HandwritingRecognition();
	}

}

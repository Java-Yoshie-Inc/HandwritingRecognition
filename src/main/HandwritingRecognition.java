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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class HandwritingRecognition {
	
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	
	private JFrame frame;
	private JPanel panel;
	private JPanel drawPanel;
	private JPanel outputPanel;
	private JLabel outputText;
	private JButton resetButton;

	private BufferedImage image;
	private int penSize = 15;
	private Font outputFont, font;
	private Timer timer;
	
	private final AI ai;

	public HandwritingRecognition() {
		this.ai = new AI();
		ai.train(1);
		
		image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		outputFont = new Font("Arial", Font.PLAIN, 150);
		font = new Font("Arial", Font.PLAIN, 20);
		
		setupFrame();
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
				reset();
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
		int index = ai.calculate(image);
		
		outputText.setText(String.valueOf(index));
		
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, 200, 200);
	}

	public static void main(String[] args) {
		new HandwritingRecognition();
	}

}

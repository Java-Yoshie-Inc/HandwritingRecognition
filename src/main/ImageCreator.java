package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImageCreator {
	
	private final Dimension SIZE = new Dimension(500, 500);
	private final Dimension OUTPUT_SIZE = new Dimension(100, 100);
	
	private JFrame frame;
	private JPanel panel;
	
	private Timer timer;
	
	private BufferedImage image;
	
	private int inputNumber;
	
	private final int penSize = (int) ((SIZE.getWidth() + SIZE.getHeight()) / 25d);
	
	public ImageCreator() {
		String input = JOptionPane.showInputDialog("Do you want to draw every time the same number?");
		if(input.equals("yes")) {
			inputNumber = Integer.parseInt(JOptionPane.showInputDialog("Which number?"));
		} else {
			inputNumber = -1;
		}
		
		setupImage();
		createFrame();
		loop();
	}
	
	private void save() throws IOException {
		int i = (int) (System.currentTimeMillis() / 1000d);
		int number;
		if(inputNumber == -1) {
			number = Integer.parseInt(JOptionPane.showInputDialog("Which number did you draw?"));
		} else {
			number = inputNumber;
		}
		
		ImageIO.write(Tools.scaleImage(image, OUTPUT_SIZE.width, OUTPUT_SIZE.height), "png", new File("data/" + number + "-" + i + ".png"));
	}
	
	private void setupImage() {
		image = new BufferedImage(SIZE.width, SIZE.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
	private void update(MouseEvent e) {
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillOval(e.getX()-penSize/2, e.getY()-penSize/2, penSize, penSize);
	}
	
	private void draw(Graphics2D g) {
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
	
	private void createFrame() {
		frame = new JFrame("Jump and Run Game");
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics graphics) {
				draw((Graphics2D) graphics);
			}
		};
		
		frame.setSize(SIZE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					setupImage();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				update(e);
			}
		});
		
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					try {
						save();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					setupImage();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		panel.setLayout(null);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	private void loop() {
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.repaint();
			}
		});
		timer.start();
	}
	
	public static void main(String[] args) {
		new ImageCreator();
	}
	
}

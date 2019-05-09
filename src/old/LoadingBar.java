package old;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class LoadingBar {
	
	private static JFrame frame;
	private static JPanel panel;
	private static JProgressBar progressBar;
	
	private static int currentMin, currentMax;
	
	public static void startLoading(int min, int max) {
		startLoading(min, max, "Loading...");
	}
	
	public static void startLoading(int min, int max, String title) {
		if(frame != null) {
			frame.dispose();
		}
		currentMax = max;
		currentMin = min;
		frame = new JFrame(title);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(400, 130);
		frame.setLocationRelativeTo(null);
		
		panel = new JPanel();
		panel.setLayout(null);
		
		progressBar = new JProgressBar(currentMin, currentMax);
		progressBar.setBounds(20, 20, 360, 60);
		progressBar.setStringPainted(true);
		
		panel.add(progressBar);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void setProgress(int value) {
		progressBar.setValue(value);
		progressBar.setString((100 * value / currentMax) + "%");
		if(value >= currentMax) {
			frame.dispose();
		}
	}
	
	public static void setProgress(int value, String text) {
		progressBar.setValue(value);
		progressBar.setString(text);
		if(value >= currentMax) {
			frame.dispose();
		}
	}
	
	public static void endLoading() {
		frame.dispose();
	}
	
}

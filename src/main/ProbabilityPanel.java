package main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial")
public class ProbabilityPanel extends JPanel {
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
	
	public ProbabilityPanel() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK), BorderFactory.createEmptyBorder(25, 25, 25, 25)));
		//setPreferredSize(new Dimension(300, 0));
	}
	
	public void setOutput(Output output) {
		removeAll();
		revalidate();
		repaint();
		
		for(int i=0; i<output.getValues().length; i++) {
			double d = output.getValues()[i];
			
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			
			JLabel label = new JLabel(i + ": ");
			label.setFont(new Font("Arial", Font.BOLD, 16));
			panel.add(label);
			
			JLabel label2 = new JLabel(String.valueOf(DECIMAL_FORMAT.format(d*100d)));
			label2.setForeground(new Color((float) sigmoid(d, 0.3), 0f, 0f));
			label2.setFont(new Font("Arial", Font.BOLD, 16));
			panel.add(label2);
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i;
			add(panel, constraints);
		}
	}
	
	private double sigmoid(double x, double a) {
		return -(1+a) * x / (-x-a);
	}

}

package main;

import java.text.DecimalFormat;

public class Output {
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#####");
	
	private final double[] values;
	
	public Output(double... values) {
		this.values = values;
	}
	
	public double[] getValues() {
		return this.values;
	}
	
	public double getNumberProbability(int number) {
		return getValues()[number];
	}
	
	public int getNumber() {
		double maxOutput = 0;
		int number = -1;

		for (int i = 0; i < values.length; i++) {
			if (values[i] > maxOutput) {
				maxOutput = values[i];
				number = i;
			}
		}
		
		return number;
	}
	
	public double getHighestProbability() {
		double probability = 0;
		
		for(double d : values) {
			if(d > probability) probability = d;
		}
		
		return probability;
	}
	
	public void print() {
		for(int i=0; i < values.length; i++) {
			System.out.print(" | ");
			System.out.print(i + ": " + DECIMAL_FORMAT.format(values[i]));
			System.out.print(" | ");
		}
		System.out.println();
		System.out.println("Recognized Number: " + getNumber());
		System.out.println();
	}
	
}

package main;

import neural_network.ActivationFunctionType;
import neural_network.NeuralNetwork;
import neural_network.Range;

public class HandwritingRecognition {
	
	private NeuralNetwork neuralNetwork;
	
	public HandwritingRecognition() {
		setupNetwork();
	}
	
	private void setupNetwork() {
		neuralNetwork = new NeuralNetwork(ActivationFunctionType.SIGMOID2, new Range(-1, 1), 0, new int[] {22500, 1000, 300, 50, 10});
	}
	
	public static void main(String[] args) {
		new HandwritingRecognition();
	}
	
}

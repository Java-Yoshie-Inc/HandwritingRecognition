package main;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;

public class WekaTest {

	public static void main(String[] args) {
		NeuralNetwork neuralNetwork = new Perceptron(2, 1);

		DataSet trainingSet = new DataSet(2, 1);
		trainingSet.addRow(new DataSetRow(new double[] { 0, 0 }, new double[] { 0 }));
		trainingSet.addRow(new DataSetRow(new double[] { 0, 1 }, new double[] { 1 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 0 }, new double[] { 1 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 1 }, new double[] { 1 }));
		
		BackPropagation backPropagation = new BackPropagation();
		backPropagation.setMaxIterations(1000);
		neuralNetwork.learn(trainingSet, backPropagation);
		
		//neuralNetwork.learn(trainingSet);
		
		neuralNetwork.save("or_perceptron.nnet");
		
		NeuralNetwork nn = NeuralNetwork.load("or_perceptron.nnet");
		
		nn.setInput(0, 1);
		nn.calculate();
		
		double[] output = nn.getOutput();
		System.out.println(Arrays.toString(output));
	}

}

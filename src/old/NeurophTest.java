package old;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;

public class NeurophTest {

	public static void main(String[] args) {
		Layer inputLayer = new Layer();
		inputLayer.addNeuron(new Neuron());
		inputLayer.addNeuron(new Neuron());
		
		Layer hiddenLayerOne = new Layer();
		hiddenLayerOne.addNeuron(new Neuron());
		hiddenLayerOne.addNeuron(new Neuron());
		hiddenLayerOne.addNeuron(new Neuron());
		hiddenLayerOne.addNeuron(new Neuron());

		Layer hiddenLayerTwo = new Layer();
		hiddenLayerTwo.addNeuron(new Neuron());
		hiddenLayerTwo.addNeuron(new Neuron());
		hiddenLayerTwo.addNeuron(new Neuron());
		hiddenLayerTwo.addNeuron(new Neuron());

		Layer outputLayer = new Layer();
		outputLayer.addNeuron(new Neuron());

		NeuralNetwork ann = new NeuralNetwork();
		ann.addLayer(0, inputLayer);
		ann.addLayer(1, hiddenLayerOne);
		ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(1));
		ann.addLayer(2, hiddenLayerTwo);
		ConnectionFactory.fullConnect(ann.getLayerAt(1), ann.getLayerAt(2));
		ann.addLayer(3, outputLayer);
		ConnectionFactory.fullConnect(ann.getLayerAt(2), ann.getLayerAt(3));
		ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(ann.getLayersCount() - 1), false);
		
		ann.setInputNeurons(inputLayer.getNeurons());
		ann.setOutputNeurons(outputLayer.getNeurons());

		DataSet ds = new DataSet(2, 1);

		DataSetRow rOne = new DataSetRow(new double[] { 0, 1 }, new double[] { 1 });
		ds.addRow(rOne);
		DataSetRow rTwo = new DataSetRow(new double[] { 1, 1 }, new double[] { 0 });
		ds.addRow(rTwo);
		DataSetRow rThree = new DataSetRow(new double[] { 0, 0 }, new double[] { 0 });
		ds.addRow(rThree);
		DataSetRow rFour = new DataSetRow(new double[] { 1, 0 }, new double[] { 1 });
		ds.addRow(rFour);

		BackPropagation backPropagation = new BackPropagation();
		backPropagation.setMaxIterations(1000);
		ann.learn(ds, backPropagation);

		ann.setInput(1, 0);
		ann.calculate();
		double[] networkOutputOne = ann.getOutput();

		System.out.println(Arrays.toString(networkOutputOne));
	}

}
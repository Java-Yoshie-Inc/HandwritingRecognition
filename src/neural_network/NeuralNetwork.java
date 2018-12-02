package neural_network;

import main.LoadingBar;

public class NeuralNetwork {
	
	private Layer[] layers;
	
	public NeuralNetwork(Layer[] layers, int bias) {
		this.layers = layers;
		for(Layer layer : layers) {
			for(Neuron neuron : layer.getNeurons()) {
				neuron.setBias(bias);
			}
		}
	}
	
	public NeuralNetwork(ActivationFunction activationFunction, Range weightRange, int bias, int... layerNeuronAmount) {
		layers = new Layer[layerNeuronAmount.length];
		for(int currentLayer = layerNeuronAmount.length-1; currentLayer >= 0; currentLayer--) {
			Neuron[] neurons = new Neuron[layerNeuronAmount[currentLayer]];
			LoadingBar.startLoading(0, layerNeuronAmount[currentLayer], "Creating layer " + (currentLayer + 1) + " of neural network...");
			for(int currentNeuron=0; currentNeuron<neurons.length; currentNeuron++) {
				neurons[currentNeuron] = new Neuron(activationFunction);
				neurons[currentNeuron].setBias(bias);
				if(currentLayer < layerNeuronAmount.length-1) {
					neurons[currentNeuron].connect(NeuralNetworkTools.random(weightRange.getMin(), weightRange.getMax()), layers[currentLayer+1].getNeurons());
				}
				LoadingBar.setProgress(currentNeuron);
			}
			layers[currentLayer] = new Layer(neurons);
		}
		
		for(Neuron neuron : getInputLayer().getNeurons()) {
			neuron.setUseBias(false);
			neuron.setUseActivationFunction(false);
		}
	}
	
	public void start(double... inputs) {
		Layer inputLayer = getInputLayer();
		for(int i=0; i<inputLayer.getNeurons().length; i++) {
			inputLayer.getNeurons()[i].input(inputs[i]);
		}
		for(Layer layer : layers) {
			for(Neuron neuron : layer.getNeurons()) {
				neuron.endInput();
			}
			for(Neuron neuron : layer.getNeurons()) {
				neuron.transfer();
			}
		}
	}
	
	public void train(double[] input, double[] target, double eta) {
		start(input);
		backpropError(target);
		updateWeights(eta);
	}
	
	private void backpropError(double[] target) {
		Layer outputLayer = getOutputLayer();
		for(int i=0; i < outputLayer.getNeurons().length; i++) {
			Neuron neuron = outputLayer.getNeurons()[i];
			double errorSignal = (neuron.getOutput() - target[i]) * neuron.getOutputDerivative();
			neuron.setErrorSignal(errorSignal);
		}
		
		for(int i = layers.length-2; i > 0; i--) {
			Layer layer = layers[i];
			for(int j = 0; j < layer.getNeurons().length; j++) {
				Neuron neuron = layer.getNeurons()[j];
				double sum  = 0;
				for(int k = 0; k < layers[i+1].getNeurons().length; k++) {
					Neuron nextNeuron = layers[i+1].getNeurons()[k];
					sum += neuron.getConnection(nextNeuron).getWeight() * nextNeuron.getErrorSignal();
				}
				neuron.setErrorSignal(sum * neuron.getOutputDerivative());
			}
		}
	}
	
	private void updateWeights(double eta) {
		for(int i = 1; i < layers.length; i++) {
			Layer layer = layers[i];
			for(int j = 0; j < layer.getNeurons().length; j++) {
				Neuron neuron = layer.getNeurons()[j];
				double delta = -eta * neuron.getErrorSignal();
				
				for(int k = 0; k < layers[i-1].getNeurons().length; k++) {
					Neuron previousNeuron = layers[i-1].getNeurons()[k];
					double weight = previousNeuron.getConnection(neuron).getWeight() + delta * previousNeuron.getOutput();
					previousNeuron.getConnection(neuron).setWeight(weight);
				}
			}
		}
	}
	
	public double[] getOutput() {
		Layer outputLayer = getOutputLayer();
		double[] output = new double[outputLayer.getNeurons().length];
		for(int i=0; i<output.length; i++) {
			output[i] = outputLayer.getNeurons()[i].getOutput();
		}
		return output;
	}
	
	public Layer getInputLayer() {
		return layers[0];
	}
	
	public Layer getOutputLayer() {
		return layers[layers.length-1];
	}
	
	public Layer[] getLayers() {
		return layers;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Layer layer : layers) {
			sb.append("[");
			sb.append(layer.toString());
			sb.append("]");
			sb.append(" ");
		}
		return sb.toString();
	}
	
}

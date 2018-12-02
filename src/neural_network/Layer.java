package neural_network;

public class Layer {
	
	private Neuron[] neurons;
	
	public Layer(Neuron... neurons) {
		this.neurons = neurons;
	}
	
	public Neuron[] getNeurons() {
		return neurons;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for(Neuron neuron : neurons) {
			sb.append(neuron.toString());
			sb.append(" ");
		}
		return sb.toString();
	}
	
}

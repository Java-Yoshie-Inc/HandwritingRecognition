/*package neural_network;

public class Connection {
	
	private double weight;
	private Neuron neuron;
	
	public Connection(Neuron neuron, double weight) {
		this.weight = weight;
		this.setNeuron(neuron);
	}
	
	public Connection(Neuron neuron) {
		this(neuron, 1f);
	}
	
	public Connection() {
		
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public Neuron getNeuron() {
		return neuron;
	}
	
	public void setNeuron(Neuron neuron) {
		this.neuron = neuron;
	}
	
}*/

package neural_network;

public class Connection {
	
	private double weight;
	private Neuron toNeuron;
	private Neuron fromNeuron;
	
	public Connection(Neuron fromNeuron, Neuron toNeuron, double weight) {
		this.fromNeuron = fromNeuron;
		this.toNeuron = toNeuron;
		this.weight = weight;
	}
	
	public Connection(Neuron fromNeuron, Neuron toNeuron) {
		this(fromNeuron, toNeuron, 1f);
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Neuron getToNeuron() {
		return toNeuron;
	}

	public void setToNeuron(Neuron toNeuron) {
		this.toNeuron = toNeuron;
	}

	public Neuron getFromNeuron() {
		return fromNeuron;
	}

	public void setFromNeuron(Neuron fromNeuron) {
		this.fromNeuron = fromNeuron;
	}
	
	@Override
	public String toString() {
		return String.valueOf(weight);
	}
	
}

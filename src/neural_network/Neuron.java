package neural_network;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	
	private ActivationFunction activationFunction;
	private List<Connection> connections = new ArrayList<>();
	
	private double bias = 0f;
	private boolean useBias = true;
	private boolean useActivationFunction = true;
	
	private double errorSignal;
	private double outputDerivative;
	
	private double inputSum = 0f;
	private double output = 0f;
	
	
	public Neuron(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}
	
	public void input(double value) {
		inputSum += value;
	}
	
	public void endInput() {
		if(useBias) {
			input(bias);
		}
	}
	
	public void transfer() {
		if(useActivationFunction) {
			output = activationFunction.input(inputSum);
		} else {
			output = new Double(inputSum);
		}
		inputSum = 0;
		outputDerivative = activationFunction.getDerivative(output);
		
		for(Connection connection : connections) {
			if(connection.getFromNeuron().equals(this)) {
				connection.getToNeuron().input(output * connection.getWeight());
			}
		}
	}
	
	public Neuron connect(double weight, Neuron... neurons) {
		for(Neuron neuron : neurons) {
			Connection connection = new Connection(this, neuron, weight);
			connections.add(connection);
			neuron.getConnections().add(connection);
		}
		return this;
	}
	
	public Neuron connect(Neuron... neuron) {
		return this.connect(1, neuron);
	}
	
	public Connection getConnection(Neuron neuron) {
		for(Connection connection : connections) {
			if(connection.getToNeuron().equals(neuron)) {
				return connection;
			}
		}
		return null;
	}
	
	public double getOutput() {
		return output;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public double getBias() {
		return bias;
	}
	
	public boolean useBias() {
		return useBias;
	}

	public void setUseBias(boolean useBias) {
		this.useBias = useBias;
	}

	public boolean isUseActivationFunction() {
		return useActivationFunction;
	}

	public void setUseActivationFunction(boolean useActivationFunction) {
		this.useActivationFunction = useActivationFunction;
	}
	
	public List<Connection> getConnections() {
		return this.connections;
	}
	
	@Override
	public String toString() {
		return String.valueOf(output);
	}

	public double getErrorSignal() {
		return errorSignal;
	}

	public void setErrorSignal(double errorSignal) {
		this.errorSignal = errorSignal;
	}

	public double getOutputDerivative() {
		return outputDerivative;
	}

	public void setOutputDerivative(double outputDerivative) {
		this.outputDerivative = outputDerivative;
	}
	
}

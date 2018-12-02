package neural_network;

public class Sample {
	
	private final double[] input, target;
	
	public Sample(double[] input, double[] target) {
		this.input = input;
		this.target = target;
	}

	public double[] getInput() {
		return input;
	}

	public double[] getTarget() {
		return target;
	}
	
}

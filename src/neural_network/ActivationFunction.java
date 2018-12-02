package neural_network;

public interface ActivationFunction {
	
	public abstract double input(double x);
	
	public abstract double getDerivative(double x);
	
}

package neural_network;

/**
 * Created by finne on 02.02.2018.
 */
public interface ErrorFunction {
	
    public abstract double overall_error(Neuron neuron, double[] target);
    
    public abstract void apply(Neuron neuron, double[] target);
    
}

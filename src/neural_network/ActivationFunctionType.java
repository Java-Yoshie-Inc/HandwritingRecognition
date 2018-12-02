package neural_network;

public enum ActivationFunctionType implements ActivationFunction {
	
	SIGMOID() {
		@Override
		public double input(double x) {
			return (double) (1f / (1f + Math.exp(-x)));
		}
		@Override
		public double getDerivative(double x) {
			return input(x) * (1 - input(x));
		}
	}, SIGMOID2() {
		@Override
		public double input(double x) {
			return (double) (1f / (1f + Math.exp(-x)));
		}
		@Override
		public double getDerivative(double x) {
			return x * (1 - x);
		}
	}, IDENTIFY() {
		@Override
		public double input(double x) {
			return x;
		}
		@Override
		public double getDerivative(double x) {
			return 1;
		}
	};
	
	private ActivationFunctionType() {
		
	}
	
}

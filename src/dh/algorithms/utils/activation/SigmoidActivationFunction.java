package dh.algorithms.utils.activation;

public class SigmoidActivationFunction implements AbstractActivationFunction {

	@Override
	public double activate(double t) {
		return 1.0 / (1.0 + Math.exp(-1.0 * t));
	}
	
	@Override
	public String getName() {
		return "sigmoid";
	}

}

package dh.algorithms.utils.activation;

public class Sigmoid2ActivationFunction implements AbstractActivationFunction {

	@Override
	public double activate(double t) {
		return 2.0 / (1 + Math.exp(-1.0 * Math.E * t)) - 1.0;
	}
	
	@Override
	public String getName() {
		return "sigmoid2";
	}

}

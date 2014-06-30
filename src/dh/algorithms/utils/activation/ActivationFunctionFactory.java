package dh.algorithms.utils.activation;

public class ActivationFunctionFactory {
	public static AbstractActivationFunction create(String name) {
		if (name.equals("sigmoid")) {
			return new SigmoidActivationFunction();
		} else if (name.equals("sigmoid2")) {
			return new Sigmoid2ActivationFunction();
		} else {
			throw new RuntimeException("Not supported activation function (" + name + ")...");
		}
	}
}

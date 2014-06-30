package dh.algorithms.utils.activation;

public class ActivationFunctionFactory {
	public static AbstractActivationFunction create(String name) {
		if (name.equals("sigmoid")) {
			return new SigmoidActivationFunction();
		} else {
			throw new RuntimeException("Not supported activation function (" + name + ")...");
		}
	}
}

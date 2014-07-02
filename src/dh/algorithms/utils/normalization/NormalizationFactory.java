package dh.algorithms.utils.normalization;

public class NormalizationFactory {
	public static NormalizationMethod create(String name) {
		if (name.equals("minmax")) {
			return new MinMaxNormalizationMethod();
		}
		
		throw new RuntimeException("Normalization method " + name + " is not supported...");
	}
}

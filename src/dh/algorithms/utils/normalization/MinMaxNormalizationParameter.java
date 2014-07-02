package dh.algorithms.utils.normalization;

public class MinMaxNormalizationParameter extends NormalizationParameter {
	private final double minValue;
	private final double maxValue;
	
	public MinMaxNormalizationParameter(double minValue, double maxValue) {
		super("minmax");
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
	public double getMinValue() {
		return minValue;
	}
	
	@Override
	public String toString() {
		return "MinMaxNormalization(min: " + minValue + ", max: " + maxValue + ")"; 
	}
}

package dh.algorithms.regression.ann;

import java.io.Serializable;
import java.util.Random;

import dh.algorithms.utils.activation.AbstractActivationFunction;

public class Layer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int perceptrons;
	
	private double[][] weights;
	private transient double[] outputs;
	
	public Layer(int inputVariables, int perceptrons, Random random) {
		this.perceptrons = perceptrons; 
		weights = new double[perceptrons][];
		for (int i = 0; i < perceptrons; ++i) {
			weights[i] = new double[inputVariables + 1];
			for (int j = 0; j < inputVariables; ++j) {
				weights[i][j] = random.nextDouble() * 2.0 - 1.0;
			}
		}
		outputs = new double[perceptrons];
		for (int i = 0; i < perceptrons; ++i) {
			outputs[i] = 0.0;
		}
	}
	
	public Layer(Layer layer, int perceptrons, Random random) {
		this(layer.getPerceptrons(), perceptrons, random);
	}
	
	public int getPerceptrons() {
		return perceptrons;
	}
	
	public double[] getOutputs() {
		return outputs;
	}
	
	public void calculateOutputs(double[] data, AbstractActivationFunction activationFunction) {
		for (int i = 0; i < perceptrons; ++i) {
			outputs[i] = 0.0;
			for (int j = 0; j < data.length; ++j) {
				outputs[i] += data[j] * weights[i][j];
			}
			outputs[i] += 1.0 * weights[i][data.length];
			outputs[i] = activationFunction.activate(outputs[i]);
		}
	}
	
	public double[][] getWeights() {
		return weights;
	}
}

package dh.algorithms.regression.ann;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import dh.algorithms.AbstractLearner;
import dh.algorithms.utils.activation.AbstractActivationFunction;
import dh.algorithms.utils.activation.ActivationFunctionFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Model;
import dh.repository.Table;

public class NeuralNetworkLearner extends AbstractLearner {

	double[] target = null;	
	MarkingType[] marking = null;
	double[][] data = null;
	int iteration = 0;
	DoubleDataColumn[] columns;
	AbstractActivationFunction activationFunction;
	LinkedList<Integer> layerConfiguration;
	Random random;
	double learningRate;

	@Override
	public void initializeLearner(Table table, HashMap<String, String> parameters) {

		if (!parameters.containsKey("iteration")) {
			throw new RuntimeException("There is no parameter iteration...");
		}
		try {
			iteration = Integer.parseInt(parameters.get("iteration"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Parameter iteration is not an integer...");
		}
		
		if (!parameters.containsKey("learningrate")) {
			throw new RuntimeException("There is no parameter learningrate...");
		}
		try {
			learningRate = Double.parseDouble(parameters.get("learningrate"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Parameter iteration is not an integer...");
		}
		
		
		if (!parameters.containsKey("activation")) {
			throw new RuntimeException("There is no parameter activation...");
		}
		activationFunction = ActivationFunctionFactory.create(parameters.get("activation"));
		
		if (!parameters.containsKey("layers")) {
			throw new RuntimeException("There is no parameter layers...");
		}
		layerConfiguration = new LinkedList<>();
		if (!parameters.get("layers").equals("")) {
			for (String s : parameters.get("layers").split("\\ ")) {
				try {
					int perceptrons = Integer.parseInt(s);
					layerConfiguration.add(perceptrons);
				} catch (NumberFormatException e) {
					throw new RuntimeException("Layer parameter contains non integer members... (" + s + ")");
				}
			}
		}
		layerConfiguration.add(1);
		
		if (parameters.containsKey("random")) {
			try {
				this.random = new Random(Long.parseLong(parameters.get("random")));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Logistic regression's random must be long...");
			}
		} else {
			random = new Random();
		}	

		// search for the columns and check them
		int dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("double") && column.getRole().equals("")) {
				dataColumnCounter++;
			}
			if (column.getColumnType().equals("double") && column.getRole().equals("target")) {
				target = ((DoubleDataColumn) column).getData();
			}			
			if (column.getColumnType().equals("marking") && column.getRole().equals("marking")) {
				marking = ((MarkingColumn) column).getData();
			}
		}

		if (dataColumnCounter == 0) {
			throw new RuntimeException("There is no double column without role...");
		}

		if (target == null) {
			throw new RuntimeException("There is no target boolean column...");
		}

		columns = new DoubleDataColumn[dataColumnCounter];
		dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("double") && column.getRole().equals("")) {
				columns[dataColumnCounter++] = (DoubleDataColumn)column;
			}
		}

		data = new double[columns.length][];
		for (int i = 0; i < columns.length; ++i) {
			data[i] = columns[i].getData();
		}

		initialized = true;
	}
	
	@Override
	protected Model learn(String modelName, Table table) {
		
		int instances = data[0].length;
		
		// create Layer structure
		Layer[] layers = new Layer[layerConfiguration.size()];
		for (int i = 0; i < layers.length; ++i) {
			if (i == 0) {
				layers[i] = new Layer(data.length, layerConfiguration.get(i), random);
			} else {
				layers[i] = new Layer(layers[i - 1], layerConfiguration.get(i), random);
			}
		}
		
		double[] inputData = new double[data.length];
		
		for (int iter = 0; iter < iteration; ++iter) {
			
			for (int inst = 0; inst < instances; ++inst) {
				
				for (int i = 0; i < inputData.length; ++i) {
					inputData[i] = data[i][inst];
				}
				
				layers[0].calculateOutputs(inputData, activationFunction);
				
				for (int l = 1; l < layers.length; ++l) {
					layers[l].calculateOutputs(layers[l-1].getOutputs(), activationFunction);
				}
				
				double error = target[inst] - layers[layers.length - 1].getOutputs()[0];
				
				// backpropogation
				for (int i = layers.length - 1; i >= 0; --i) {
					double[] inputs = (i == 0) ? inputData : layers[i-1].getOutputs();
					double[] outputs = layers[i].getOutputs();
					for (int p = 0; p < layers[i].getPerceptrons(); ++p) {
						double[] weights = layers[i].getWeights()[p];
						for (int w = 0; w < weights.length; ++w) {
							double dt = learningRate * error * ((w == weights.length - 1) ? 1.0 : inputs[w]) * outputs[p] * (1.0 - outputs[p]);
							weights[w] += dt;	
						}
					}
				}
			}
		}
		
		return new NeuralNetworkModel(columns, layers, activationFunction.getName());
	}	
}

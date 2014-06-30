package dh.algorithms.regression.ann;

import dh.algorithms.regression.RegressionModel;
import dh.algorithms.utils.activation.AbstractActivationFunction;
import dh.algorithms.utils.activation.ActivationFunctionFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;

public class NeuralNetworkModel extends RegressionModel {

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames;
	private Layer[] layers;
	private String activationFunctionName;
	
	public NeuralNetworkModel(AbstractDataColumn[] columns, Layer[] layers, String activationFunctionName) {
		columnNames = new String[columns.length];
		for (int i = 0; i < columns.length; ++i) {
			columnNames[i] = columns[i].getName();
		}
		this.layers = layers;
		this.activationFunctionName = activationFunctionName;
	}

	@Override
	public void apply(Table table) {
		
		AbstractActivationFunction activationFunction = ActivationFunctionFactory.create(activationFunctionName);
		
		double[][] data = new double[columnNames.length][];
		for (int i = 0; i < columnNames.length; ++i) {
			DoubleDataColumn column = table.getColumn(columnNames[i]);
			data[i] = column.getData();
		}
		
		double[] prediction = getPredictionData(table);
		double[] inputData = new double[columnNames.length];
		for (int i = 0; i < prediction.length; ++i) {
			for (int j = 0; j < inputData.length; ++j) {
				inputData[j] = data[j][i];
			}
			layers[0].calculateOutputs(inputData, activationFunction);
			for (int j = 1; j < layers.length; ++j) {
				layers[j].calculateOutputs(layers[j - 1].getOutputs(), activationFunction);
			}
			prediction[i] = layers[layers.length - 1].getOutputs()[0];
		}
	}

}

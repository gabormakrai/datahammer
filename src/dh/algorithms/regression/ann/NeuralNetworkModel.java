package dh.algorithms.regression.ann;

import dh.algorithms.regression.RegressionModel;
import dh.data.column.AbstractDataColumn;
import dh.repository.Table;

public class NeuralNetworkModel extends RegressionModel {

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames;
	private Layer[] layers;
	private String activationFunction;
	
	public NeuralNetworkModel(AbstractDataColumn[] columns, Layer[] layers, String activitaionFunction) {
		
	}

	@Override
	public void apply(Table table) {
	}

}

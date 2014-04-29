package dh.algorithms.classification.bagging;

import dh.algorithms.classification.ClassificationModel;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;
import dh.repository.Model;

public class BaggingModel extends ClassificationModel {

	private static final long serialVersionUID = 1L;

	Model[] models = null;

	public BaggingModel(String name, Model[] models) {
		super(name);
		this.models = models;
	}

	@Override
	public void apply(Table table) {
		double[] numericP = new double[table.getSize()];
		for (int i = 0; i < numericP.length; i++) {
			numericP[i] = 0.0;
		}

		for (Model model : models) {
			model.apply(table);

			// find the numericprediction column
			double[] modelNumericP = null;
			for (AbstractDataColumn column : table.getColumns().values()) {
				if (column.getRole().equals("numericprediction")) {
					modelNumericP = ((DoubleDataColumn) column).getData();
				}
			}
			// add it to the bagging model numericP
			for (int i = 0; i < numericP.length; i++) {
				numericP[i] += modelNumericP[i];
			}
		}

		// get the average
		for (int i = 0; i < numericP.length; i++) {
			numericP[i] /= (double) models.length;
		}

		// set the numeric prediction column data
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("numericprediction")) {
				((DoubleDataColumn) column).setData(numericP);
			}
		}

		// set the prediction column data
		boolean[] p = null;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("prediction")) {
				p = ((BooleanDataColumn) column).getData();
			}
		}
		for (int i = 0; i < p.length; i++) {
			if (numericP[i] < 0) {
				p[i] = false;
			} else {
				p[i] = true;
			}
		}
	}
}

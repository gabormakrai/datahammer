package dh.algorithms.classification.logisticregression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.algorithms.classification.ClassificationModel;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;

public class RegressionModel extends ClassificationModel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RegressionModel.class);

	String[] columnNames = null;
	double[] weights = null;
	boolean intercept = false;

	public RegressionModel() {
	}

	public RegressionModel(String name, String[] columnNames, double[] weights, boolean intercept) {
		super(name);
		this.columnNames = columnNames;
		this.weights = weights;
		this.intercept = intercept;
	}

	@Override
	public void apply(Table table) {
		double[] numericP = getNumericPredictionData(table);
		boolean[] p = getPredictionData(table);

		boolean[] columnExists = new boolean[columnNames.length];
		double[][] x = new double[columnNames.length][];
		for (int i = 0; i < columnExists.length; i++) {
			if (table.getColumns().containsKey(columnNames[i])) {
				columnExists[i] = true;
				x[i] = ((DoubleDataColumn) table.getColumns().get(columnNames[i])).getData();
			} else {
				columnExists[i] = false;
			}
		}

		double pred = 0.0;
		for (int i = 0; i < table.getSize(); i++) {
			if (intercept) {
				pred = weights[0];
			} else {
				pred = 0.0;
			}
			for (int j = 0; j < columnNames.length; j++) {
				if (columnExists[j]) {
					pred += weights[j + 1] * x[j][i];
				}
			}

			pred = sigmoid(pred);

			if (pred > 0.0) {
				p[i] = true;
			}
			if (numericP != null) {
				numericP[i] = pred;
			}

		}
		logger.info("Logistic regression model " + getName() + " is applied on table " + table.getName() + "...");
	}

	public static double sigmoid(double x) {
		return 2 / (1 + Math.exp(-1.0 * Math.E * x)) - 1.0;
	}
}

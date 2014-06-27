package dh.algorithms.regression;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;
import dh.repository.Model;

public abstract class RegressionModel extends Model {

	private static final long serialVersionUID = 1L;

	public RegressionModel() {
		super("");
	}

	public RegressionModel(String name) {
		super(name);
	}

	protected double[] getPredictionData(Table table) {
		for (AbstractDataColumn c : table.getColumns().values()) {
			if (c.getRole().equals("prediction")) {
				return ((DoubleDataColumn) c).getData();
			}
		}

		DoubleDataColumn prediction = new DoubleDataColumn();
		prediction.setName("prediction");
		prediction.setRole("prediction");
		double[] p = new double[table.getSize()];
		for (int i = 0; i < table.getSize(); i++) {
			p[i] = 0.0;
		}
		prediction.setData(p);
		table.getColumns().put(prediction.getName(), prediction);

		return p;
	}
	
}

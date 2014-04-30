package dh.algorithms.classification;

import java.util.HashMap;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Table;
import dh.repository.Model;

public abstract class ClassificationModel extends Model {

	private static final long serialVersionUID = 1L;

	public ClassificationModel() {
		super("");
	}

	public ClassificationModel(String name) {
		super(name);
	}

	protected boolean[] getPredictionData(Table table) {
		for (AbstractDataColumn c : table.getColumns().values()) {
			if (c.getRole().equals("prediction")) {
				return ((BooleanDataColumn) c).getData();
			}
		}

		BooleanDataColumn prediction = new BooleanDataColumn();
		prediction.setName("prediction");
		prediction.setRole("prediction");
		boolean[] nullElements = new boolean[table.getSize()];
		boolean[] p = new boolean[table.getSize()];
		for (int i = 0; i < table.getSize(); i++) {
			p[i] = false;
			nullElements[i] = false;
		}
		prediction.setData(p);
		table.getColumns().put(prediction.getName(), prediction);

		return p;
	}

	protected double[] getNumericPredictionData(Table table) {
		for (AbstractDataColumn c : table.getColumns().values()) {
			if (c.getRole().equals("numericprediction")) {
				return ((DoubleDataColumn) c).getData();
			}
		}

		DoubleDataColumn numericPrediction = new DoubleDataColumn();
		numericPrediction.setName("numericprediction");
		numericPrediction.setRole("numericprediction");
		double[] numericP = new double[table.getSize()];
		boolean[] nullElements = new boolean[table.getSize()];
		for (int i = 0; i < table.getSize(); i++) {
			numericP[i] = 0.0;
			nullElements[i] = false;
		}
		numericPrediction.setData(numericP);
		numericPrediction.setNullElements(nullElements);

		table.getColumns().put(numericPrediction.getName(), numericPrediction);

		return numericP;
	}

	protected int[] getNominalPredictionData(Table table, String[] labels) {
		for (AbstractDataColumn c : table.getColumns().values()) {
			if (c.getRole().equals("nominalprediction")) {
				return ((NominalDataColumn) c).getData();
			}
		}

		NominalDataColumn nominalPrediction = new NominalDataColumn();
		nominalPrediction.setName("nominalprediction");
		nominalPrediction.setRole("nominalprediction");

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		HashMap<Integer, String> reverseMap = new HashMap<Integer, String>();

		for (int i = 0; i < labels.length; ++i) {
			map.put(labels[i], i);
			reverseMap.put(i, labels[i]);
		}

		nominalPrediction.setReverseMapping(reverseMap);
		nominalPrediction.setMapping(map);

		int[] nominalPredictionData = new int[table.getSize()];
		for (int i = 0; i < nominalPredictionData.length; ++i) {
			nominalPredictionData[i] = -1;
		}

		nominalPrediction.setData(nominalPredictionData);

		table.getColumns().put(nominalPrediction.getName(), nominalPrediction);

		return nominalPredictionData;
	}

}

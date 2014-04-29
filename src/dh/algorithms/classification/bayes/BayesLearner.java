package dh.algorithms.classification.bayes;

import java.util.HashMap;
import java.util.LinkedList;

import dh.algorithms.AbstractLearner;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.NominalDataColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;
import dh.repository.Model;

public class BayesLearner extends AbstractLearner {

	boolean[] target = null;
	NominalDataColumn[] columns = null;
	MarkingType[] marking = null;
	double[] weight = null;

	@Override
	public void initializeLearner(Table table, HashMap<String, String> parameters) {

		// search for the columns and check them
		int dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("nominal") && column.getRole().equals("")) {
				dataColumnCounter++;
			}
			if (column.getColumnType().equals("boolean") && column.getRole().equals("target")) {
				target = ((BooleanDataColumn) column).getData();
			}
			if (column.getColumnType().equals("marking") && column.getRole().equals("marking")) {
				marking = ((MarkingColumn) column).getData();
			}
			if (column.getColumnType().equals("double") && column.getRole().equals("weight")) {
				weight = ((DoubleDataColumn) column).getData();
			}
		}

		if (dataColumnCounter == 0) {
			throw new RuntimeException("There is no double column without role...");
		}

		if (target == null) {
			throw new RuntimeException("There is no target boolean column...");
		}

		columns = new NominalDataColumn[dataColumnCounter];
		dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("nominal") && column.getRole().equals("")) {
				columns[dataColumnCounter++] = (NominalDataColumn) column;
			}
		}

		initialized = true;
	}

	@Override
	protected Model learn(String modelName, Table table) {

		HashMap<String, HashMap<String, Probability>> finalMap = new HashMap<String, HashMap<String, Probability>>();
		double weightP = 0.0;
		double weightF = 0.0;
		double smallestWeight = Double.MAX_VALUE;

		for (int i = 0; i < target.length; i++) {
			if (marking[i] == MarkingType.Train) {
				double w = 1.0;
				if (weight != null) {
					w = weight[i];
				}
				if (target[i]) {
					weightP += w;
				} else {
					weightF += w;
				}
				if (w < smallestWeight) {
					smallestWeight = w;
				}
			}
		}

		LinkedList<Probability> probabilites = new LinkedList<Probability>();

		for (NominalDataColumn column : columns) {
			HashMap<String, Probability> map = new HashMap<String, Probability>();
			for (int i = 0; i < target.length; i++) {
				if (marking[i] == MarkingType.Train) {
					String label = column.getElement(i);
					Probability p = map.get(label);
					if (p == null) {
						p = new Probability(0.0, 0.0);
						probabilites.add(p);
						map.put(label, p);
					}
					double w = 1.0;
					if (weight != null) {
						w = weight[i];
					}
					if (target[i]) {
						p.trueP += w;
					} else {
						p.falseP += w;
					}
				}
			}
			finalMap.put(column.getName(), map);
		}

		for (Probability p : probabilites) {
			p.falseP += smallestWeight;
			p.trueP += smallestWeight;
		}

		return new BayesModel(modelName, finalMap, weightP, weightF, smallestWeight);
	}
}

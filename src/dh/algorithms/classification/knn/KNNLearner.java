package dh.algorithms.classification.knn;

import java.util.HashMap;
import dh.algorithms.AbstractLearner;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Model;
import dh.repository.Table;

public class KNNLearner extends AbstractLearner {

	boolean[] target = null;
	NominalDataColumn[] columns = null;
	MarkingType[] marking = null;
	int[][] data = null;
	int k = 0;

	int[] labelCounter = null;

	@Override
	public void initializeLearner(Table table, HashMap<String, String> parameters) {

		if (!parameters.containsKey("k")) {
			throw new RuntimeException("There is no parameter k...");
		}
		try {
			k = Integer.parseInt(parameters.get("k"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Parameter k is not an integer...");
		}

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
		}

		if (dataColumnCounter == 0) {
			throw new RuntimeException("There is no nominal column without role...");
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

		data = new int[columns.length][];
		for (int i = 0; i < columns.length; ++i) {
			data[i] = columns[i].getData();
		}

		initialized = true;
	}

	@Override
	protected Model learn(String modelName, Table table) {

		return new KNNModel(columns, target, marking, k);

	}
}

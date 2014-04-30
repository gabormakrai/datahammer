package dh.command.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Repository;
import dh.repository.Table;

public class AddClassificationWeightColumn extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(AddClassificationWeightColumn.class);

	public AddClassificationWeightColumn(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, double testP, double testN, double trainP, double trainN, double P, double N) {

		Table table = getTable(inputTableName);

		boolean[] target = null;
		MarkingType[] marking = null;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("marking")) {
				marking = ((MarkingColumn) column).getData();
			} else if (column.getRole().equals("target")) {
				target = ((BooleanDataColumn) column).getData();
			}
		}

		if (target == null) {
			throw new RuntimeException("There is no target boolean column in table " + inputTableName + "...");
		}

		double[] data = null;
		boolean createColumn = true;

		for (AbstractDataColumn c : table.getColumns().values()) {
			if (c.getRole().equals("weight")) {
				data = ((DoubleDataColumn) c).getData();
				createColumn = false;
				break;
			}
		}

		if (createColumn) {
			data = new double[table.getSize()];
			DoubleDataColumn weightColumn = new DoubleDataColumn();
			weightColumn.setName("weight");
			weightColumn.setRole("weight");
			boolean[] nullData = new boolean[table.getSize()];
			weightColumn.setNullElements(nullData);
			weightColumn.setData(data);
			for (int i = 0; i < data.length; i++) {
				nullData[i] = false;
			}
			table.getColumns().put(weightColumn.getName(), weightColumn);
		}

		if (marking == null) {
			double sum = 0.0;
			for (int i = 0; i < data.length; i++) {
				if (target[i]) {
					data[i] = P;
					sum += P;
				} else {
					data[i] = N;
					sum += N;
				}
			}
			for (int i = 0; i < data.length; i++) {
				data[i] /= sum;
			}
		} else {

			double trainSum = 0.0;
			double testSum = 0.0;

			for (int i = 0; i < data.length; i++) {
				if (marking[i] == MarkingType.Test) {
					if (target[i]) {
						data[i] = testP;
						testSum += testP;
					} else {
						data[i] = testN;
						testSum += testN;
					}
				} else if (marking[i] == MarkingType.Train) {
					if (target[i]) {
						data[i] = trainP;
						trainSum += trainP;
					} else {
						data[i] = trainN;
						trainSum += trainN;
					}
				}
			}
			for (int i = 0; i < data.length; i++) {
				if (marking[i] == MarkingType.Test) {
					data[i] /= testSum;
				} else {
					data[i] /= trainSum;
				}
			}
		}

		if (createColumn) {
			logger.info("Weight column is added to table " + inputTableName);
		} else {
			logger.info("Weight is recalculated for table " + inputTableName);
		}
	}

}

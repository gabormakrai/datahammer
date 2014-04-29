package dh.command.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Repository;
import dh.repository.Table;

public class AddWeightColumn extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(AddWeightColumn.class);

	public AddWeightColumn(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName) {

		Table table = getTable(inputTableName);

		MarkingType[] marking = null;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("marking")) {
				marking = ((MarkingColumn) column).getData();
				break;
			}
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
			weightColumn.setSize(table.getSize());
			boolean[] nullData = new boolean[table.getSize()];
			weightColumn.setNullElements(nullData);
			weightColumn.setData(data);
			for (int i = 0; i < data.length; i++) {
				nullData[i] = false;
			}
			table.getColumns().put(weightColumn.getName(), weightColumn);
		}

		if (marking == null) {
			double value = 1.0 / table.getSize();
			for (int i = 0; i < data.length; i++) {
				data[i] = value;
			}
		} else {
			int testCount = 0;
			int trainCount = 0;
			for (int i = 0; i < data.length; i++) {
				if (marking[i] == MarkingType.Test) {
					testCount++;
				} else if (marking[i] == MarkingType.Train) {
					trainCount++;
				}
			}
			double testValue = 1.0 / (double) testCount;
			double trainValue = 1.0 / (double) trainCount;
			for (int i = 0; i < data.length; i++) {
				if (marking[i] == MarkingType.Test) {
					data[i] = testValue;
				} else if (marking[i] == MarkingType.Train) {
					data[i] = trainValue;
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

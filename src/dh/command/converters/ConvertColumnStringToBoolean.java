package dh.command.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class ConvertColumnStringToBoolean extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(ConvertColumnStringToBoolean.class);

	public ConvertColumnStringToBoolean(Repository repository) {
		super(repository);
	}

	public void run(String tableName, String inputColumnName, String outputColumnName, String trueLabel) {

		Table table = getTable(tableName);

		AbstractDataColumn inputDataColumn = table.getColumn(inputColumnName);

		boolean overrideColumn = false;

		BooleanDataColumn outputColumn = table.getColumn(inputColumnName);

		if (outputColumn != null) {
			overrideColumn = true;
		}

		boolean[] data = null;

		if (overrideColumn) {
			data = ((BooleanDataColumn) outputColumn).getData();
		} else {
			data = new boolean[inputDataColumn.getSize()];
		}

		for (int i = 0; i < inputDataColumn.getSize(); i++) {
			if (trueLabel.equals(inputDataColumn.getElement(i))) {
				data[i] = true;
			} else {
				data[i] = false;
			}
		}

		if (!overrideColumn) {
			BooleanDataColumn newColumn = new BooleanDataColumn();
			newColumn.setName(outputColumnName);
			newColumn.setRole("");
			newColumn.setData(data);

			table.getColumns().put(newColumn.getName(), newColumn);
		}

		int trueLabels = 0;

		for (int i = 0; i < data.length; i++) {
			if (data[i]) {
				trueLabels++;
			}
		}
		if (overrideColumn) {
			logger.info("Boolean column " + outputColumnName + " has been overriden for table " + tableName + " with " + trueLabels + " T/" + (data.length - trueLabels) + "F (" + data.length + ")");
		} else {
			logger.info("Boolean column " + outputColumnName + " created for table " + tableName + " with " + trueLabels + " T/" + (data.length - trueLabels) + "F (" + data.length + ")");
		}
	}

}

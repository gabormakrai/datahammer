package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;

public class RemoveColumn extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(RemoveColumn.class);

	public RemoveColumn(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String columnName) {

		Table table = getTable(inputTableName);

		if (table.getColumns().get(columnName) == null) {
			throw new RuntimeException("There is no column " + columnName + " in the table " + inputTableName + "...");
		}

		table.getColumns().remove(columnName).dispose();

		System.gc();

		logger.info("Column " + columnName + " is removed from table " + inputTableName);
	}

}

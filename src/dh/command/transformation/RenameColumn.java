package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class RenameColumn extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(RenameColumn.class);

	public RenameColumn(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String originalColumnName, String newColumnName) {

		Table table = getTable(inputTableName);

		if (table.getColumns().get(originalColumnName) == null) {
			throw new RuntimeException("There is no column " + originalColumnName + " in the table " + inputTableName + "...");
		}

		if (table.getColumns().get(newColumnName) != null) {
			throw new RuntimeException("There is a column " + newColumnName + " in the table " + inputTableName + "...");
		}

		AbstractDataColumn column = table.getColumns().remove(originalColumnName);
		column.setName(newColumnName);
		table.getColumns().put(newColumnName, column);

		logger.info("Column " + originalColumnName + " of table " + inputTableName + " is renamed to " + newColumnName);
	}

}

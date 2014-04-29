package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class CopyTable extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(CopyTable.class);

	public CopyTable(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String outputName) {

		Table table = getTable(inputTableName);

		if (repository.getTables().containsKey(outputName)) {
			throw new RuntimeException("There is a table " + outputName + " in the repository...");
		}

		Table newTable = new Table(outputName, table.getSize());

		for (AbstractDataColumn column : table.getColumns().values()) {
			newTable.getColumns().put(column.getName(), column.copy(column.getName()));
		}

		repository.getTables().put(outputName, newTable);

		logger.info("Table " + inputTableName + " copied to table " + outputName + "...");
	}

}

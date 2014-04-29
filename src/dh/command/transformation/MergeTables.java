package dh.command.transformation;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class MergeTables extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(MergeTables.class);

	public MergeTables(Repository repository) {
		super(repository);
	}

	public void run(String baseTableName, String inputTableName) {

		Table baseTable = getTable(baseTableName);
		
		Table inputTable = getTable(inputTableName);
		
		logger.info("Start to merge tables: {} <- {}", baseTableName, inputTableName);
		
		if (baseTable.getColumns().size() != inputTable.getColumns().size()) {
			throw new RuntimeException("Table " + baseTableName + " and table " + inputTableName + " have different set of columns");
		}
		
		// compare columns
		HashSet<String> baseColumns = new HashSet<>();
		
		for (AbstractDataColumn c : baseTable.getColumns().values()) {
			baseColumns.add(c.getName());
		}
		
		for (AbstractDataColumn c : inputTable.getColumns().values()) {
			if (!baseColumns.contains(c.getName())) {
				throw new RuntimeException("Base Table " + baseTableName + " does not contain column " + c.getName());
			}
		}
		
		for (AbstractDataColumn c : baseTable.getColumns().values()) {
			c.merge(inputTable.getColumn(c.getName()));
		}
		
		logger.info("Merging done");

	}

}

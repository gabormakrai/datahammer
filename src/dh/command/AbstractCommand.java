package dh.command;

import dh.repository.Model;
import dh.repository.Repository;
import dh.repository.Table;

public class AbstractCommand {

	protected final Repository repository;

	public AbstractCommand(Repository repository) {
		this.repository = repository;
	}

	protected Table getTable(String table) {
		Table returnTable = repository.getTable(table);
		if (returnTable == null) {
			throw new RuntimeException("There is no table " + table + " in the repository...");
		}
		return returnTable;
	}

	protected Model getModel(String model) {
		Model returnModel = repository.getModel(model);
		if (returnModel == null) {
			throw new RuntimeException("There is no model " + model + " in the repository...");
		}
		return returnModel;
	}

	protected void checkTableDoesNotHaveColumn(Table table, String column) {
		if (table.getColumns().get(column) != null) {
			throw new RuntimeException("There is a column " + column + " in the table " + table.getName() + "...");
		}
	}
}

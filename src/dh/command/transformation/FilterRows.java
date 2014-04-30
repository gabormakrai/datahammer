package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class FilterRows extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(FilterRows.class);

	public FilterRows(Repository repository) {
		super(repository);
	}
	
	public static abstract class Expression {
		Table table = null;
		public void setTable(Table table) {
			this.table = table;
		}
		public abstract boolean loop(int index);
		public boolean[] calculateFilterArray() {
			boolean[] filterArray = new boolean[table.getSize()];
			for (int i = 0; i < filterArray.length; ++i) {
				filterArray[i] = loop(i);
			}
			return filterArray;
		}
	}

	public void run(String inputTableName, Expression expression) {

		// check that table is exist
		Table table = getTable(inputTableName);
		
		expression.setTable(table);

		boolean[] filterArray = expression.calculateFilterArray();

		for (AbstractDataColumn column : table.getColumns().values()) {
			column.filter(filterArray);
		}

		int newSize = 0;
		for (int i = 0; i < filterArray.length; i++) {
			if (filterArray[i]) {
				newSize++;
			}
		}
		table.setSize(newSize);

		logger.info("Table " + inputTableName + " filtered, new size is :" + newSize + "...");

	}

}

package dh.command.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class StatTable extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(StatTable.class);

	public StatTable(Repository repository) {
		super(repository);
	}

	public void run(String tableName) {

		Table table = getTable(tableName);

		logger.info("Table " + table.getName() + " contains " + table.getColumns().size() + " column(s)...");
		logger.info("Table size: " + table.getSize());

		StringBuffer buffer = new StringBuffer();

		for (AbstractDataColumn column : table.getColumns().values()) {

			buffer.setLength(0);
			buffer.append(column.getName());
			buffer.append(", type:");
			buffer.append("" + column.getColumnType());
			buffer.append(", size:");
			buffer.append("" + column.getSize());
			buffer.append(", nulls:");
			buffer.append("" + column.calculateNullStat());

			if (column instanceof BooleanDataColumn) {
				BooleanDataColumn c = (BooleanDataColumn) column;
				int numberOfTrues = 0;
				boolean[] data = c.getData();
				for (int i = 0; i < data.length; ++i) {
					if (data[i]) {
						++numberOfTrues;
					}
				}

				buffer.append(", trues: ");
				buffer.append(numberOfTrues);
				buffer.append(", falses: ");
				buffer.append(c.getSize() - numberOfTrues);
			}

			logger.info(buffer.toString());
		}
	}

}

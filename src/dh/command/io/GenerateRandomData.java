package dh.command.io;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.base.IntegerDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class GenerateRandomData extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(GenerateRandomData.class);

	public GenerateRandomData(Repository repository) {
		super(repository);
	}

	public void run(String outputTableName, int size, int doubleColumns, boolean addId, Random randomParameter) {

		Random random = randomParameter;
		if (random == null) {
			random = new Random();
		}

		if (repository.getTable(outputTableName) != null) {
			throw new RuntimeException("Repository has a table with name " + outputTableName + "...");
		}

		Table table = new Table();
		table.setName(outputTableName);
		table.setSize(size);
		repository.getTables().put(table.getName(), table);

		if (addId) {
			int[] idData = new int[size];
			boolean[] idNullData = new boolean[size];
			for (int i = 0; i < size; ++i) {
				idData[i] = i;
				idNullData[i] = false;
			}
			IntegerDataColumn idColumn = new IntegerDataColumn();
			idColumn.setName("id");
			idColumn.setData(idData);
			idColumn.setSize(size);
			idColumn.setRole("id");
			idColumn.setNullElements(idNullData);
			table.getColumns().put(idColumn.getName(), idColumn);
		}

		for (int i = 0; i < doubleColumns; ++i) {
			double[] data = new double[size];
			boolean[] nullData = new boolean[size];
			for (int j = 0; j < size; ++j) {
				data[j] = random.nextDouble();
				nullData[j] = false;
			}
			DoubleDataColumn column = new DoubleDataColumn();
			column.setData(data);
			column.setNullElements(nullData);
			column.setName("data" + i);
			column.setRole("");
			column.setSize(size);
			table.getColumns().put(column.getName(), column);
		}

		logger.info("Table " + table.getName() + " is generated with " + size + " row(s)...");
	}

}

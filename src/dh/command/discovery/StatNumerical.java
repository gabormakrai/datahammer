package dh.command.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class StatNumerical extends AbstractCommand {
	
	private static final Logger logger = LoggerFactory.getLogger(StatNominal.class);
	
	class Stat {
		public double min;
		public double max;
		public double average;
		public int size;
		public int nulls;
	}

	public StatNumerical(Repository repository) {
		super(repository);
	}
	
	public void run(String inputTableName, String columnName) {
		Table table = repository.getTable(inputTableName);
		AbstractDataColumn column = table.getColumns().get(columnName);
		if (column == null) {
			throw new RuntimeException("There is no column " + columnName + " in table " + inputTableName + "...");
		}
		
		Stat stat = null;
		
		if (column instanceof DoubleDataColumn) {
			stat = calculate((DoubleDataColumn)column);
		} else {
			throw new RuntimeException("Column " + columnName + " is not a numeric column...");
		}
		
		logger.info("Column " + columnName + " min: {}, max: {}, average: {}, size: {}, nulls: {}", stat.min, stat.max, stat.average, stat.size, stat.nulls );
	}
	
	private Stat calculate(DoubleDataColumn column) {
		Stat stat = new Stat();
		stat.size = column.getSize();
		stat.nulls = 0;
		stat.min = Double.MAX_VALUE;
		stat.max = Double.MIN_VALUE;
		stat.average = 0.0;
		double[] data = column.getData();
		boolean[] nullElements = column.getNullElements();
		
		for (int i = 0; i < data.length; ++i) {
			if (nullElements != null && nullElements[i]) {
				++stat.nulls;
				continue;
			}
			stat.average += data[i];
			if (data[i] < stat.min) {
				stat.min = data[i];
			}
			if (data[i] > stat.max) {
				stat.max = data[i];
			}
		}
		
		if (stat.size - stat.nulls != 0) {
			stat.average /= (double)(stat.size - stat.nulls);
		}
		return stat;
	}

}

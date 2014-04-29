package dh.command.converters;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class KeepTopLabels extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(KeepTopLabels.class);

	public KeepTopLabels(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String columnName, int top) {

		Table table = getTable(inputTableName);

		NominalDataColumn column = table.getColumn(columnName);

		// create statistics
		int[] frequency = new int[column.getMapping().size()];
		for (int i = 0; i < frequency.length; i++) {
			frequency[i] = 0;
		}

		int[] data = column.getData();
		for (int i = 0; i < data.length; i++) {
			if (data[i] != -1) {
				frequency[data[i]]++;
			}
		}

		// sort frequency
		Data[] d = new Data[frequency.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = new Data(i, frequency[i]);
		}

		Arrays.sort(d);

		boolean[] keep = new boolean[d.length];
		for (int i = 0; i < keep.length; i++) {
			keep[i] = false;
		}
		for (int i = 0; i < top; i++) {
			keep[d[i].getIndex()] = true;
		}

		for (int i = 0; i < data.length; i++) {
			if (!keep[data[i]]) {
				data[i] = -1;
			}
		}

		logger.info("Column " + columnName + " of table " + table.getName() + " lost some value...");

	}

	public static class Data implements Comparable<Data> {
		private int index;
		private int value;

		public Data(int index, int value) {
			this.index = index;
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public int getIndex() {
			return index;
		}

		@Override
		public int compareTo(Data o) {
			return o.getValue() - value;
		}

		@Override
		public String toString() {
			return "Data(i:" + index + ",v:" + value + ")";
		}
	}

}

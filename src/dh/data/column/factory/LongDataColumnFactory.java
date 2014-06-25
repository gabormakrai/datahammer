package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.LongDataColumn;

public class LongDataColumnFactory extends AbstractColumnFactory {

	long[] data;

	public LongDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed, nullAllowed);
		data = new long[bufferStep];
	}

	@Override
	public void addElement(String element) {

		if (size == data.length) {
			long[] newData = new long[data.length + bufferStep];
			for (int i = 0; i < data.length; i++) {
				newData[i] = data[i];
			}

			if (nullAllowed) {
				boolean[] newNullElements = new boolean[data.length + bufferStep];
				for (int i = 0; i < data.length; i++) {
					newNullElements[i] = nullElements[i];
				}
				nullElements = null;
				nullElements = newNullElements;
			}
			
			data = null;
			data = newData;			
		}

		if (element == null || element.trim().equals("")) {
			if (!nullAllowed) {
				throw new RuntimeException("Null is not allowed on column: " + name + " (long)");
			}
			data[size] = 0;
			nullElements[size] = true;
		} else {
			try {
				data[size] = Long.parseLong(element);
				if (nullAllowed) {
					nullElements[size] = false;
				}
			} catch (NumberFormatException e) {
				data[size] = 0;
				if (nullAllowed) {
					nullElements[size] = true;
				} else {
					throw new RuntimeException("Null is not allowed on column: " + name + " (double)");
				}
			}
		}

		size++;
	}

	@Override
	public AbstractDataColumn finishColumn() {

		long[] columnData = null;
		boolean[] columnNullElements = null;

		if (size != data.length) {
			columnData = new long[size];
			for (int i = 0; i < size; i++) {
				columnData[i] = data[i];
			}
			if (nullAllowed) {
				columnNullElements = new boolean[size];
				for (int i = 0; i < size; i++) {
					columnNullElements[i] = nullElements[i];
				}
			}
		} else {
			columnData = data;
			if (nullAllowed) {
				columnNullElements = nullElements;
			}
		}

		LongDataColumn column = new LongDataColumn();
		column.setName(name);
		column.setRole(role);
		column.setData(columnData);
		if (nullAllowed) {
			column.setNullElements(columnNullElements);
		}
		return column;
	}

}

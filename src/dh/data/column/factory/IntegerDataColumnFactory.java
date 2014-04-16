package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.IntegerDataColumn;

public class IntegerDataColumnFactory extends AbstractColumnFactory {

	int[] data;

	public IntegerDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed);
		data = new int[bufferStep];
	}

	@Override
	public void addElement(String element) {

		if (size == data.length) {
			int[] newData = new int[data.length + bufferStep];
			for (int i = 0; i < data.length; i++) {
				newData[i] = data[i];
			}
			data = null;
			data = newData;

			if (nullAllowed) {
				boolean[] newNullElements = new boolean[data.length + bufferStep];
				for (int i = 0; i < data.length; i++) {
					newNullElements[i] = nullElements[i];
				}
				nullElements = null;
				nullElements = newNullElements;
			}
		}

		if (element == null || element.trim().equals("")) {
			if (!nullAllowed) {
				throw new RuntimeException("Null is not allowed on column: " + name + " (integer)");
			}
			data[size] = 0;
			nullElements[size] = true;
		} else {
			try {
				data[size] = Integer.parseInt(element);
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

		int[] columnData = null;
		boolean[] columnNullElements = null;

		if (size != data.length) {
			columnData = new int[size];
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

		IntegerDataColumn column = new IntegerDataColumn();
		column.setName(name);
		column.setRole(role);
		column.setSize(size);
		column.setData(columnData);
		if (nullAllowed) {
			column.setNullElements(columnNullElements);
		}
		return column;
	}

}

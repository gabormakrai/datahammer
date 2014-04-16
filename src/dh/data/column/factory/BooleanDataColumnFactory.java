package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;

public class BooleanDataColumnFactory extends AbstractColumnFactory {

	boolean[] data;

	public BooleanDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed);
		data = new boolean[bufferStep];
	}

	@Override
	public void addElement(String element) {

		if (size == data.length) {
			boolean[] newData = new boolean[data.length + bufferStep];
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
				throw new RuntimeException("Null is not allowed on column: " + name + " (boolean)");
			}
			data[size] = false;
			nullElements[size] = true;
		} else {
			try {
				data[size] = Boolean.parseBoolean(element);
				if (nullAllowed) {
					nullElements[size] = false;
				}
			} catch (NumberFormatException e) {
				data[size] = false;
				if (nullAllowed) {
					nullElements[size] = true;
				} else {
					throw new RuntimeException("Null is not allowed on column: " + name + " (boolean)");
				}
			}
		}

		size++;
	}

	@Override
	public AbstractDataColumn finishColumn() {

		boolean[] columnData = null;
		boolean[] columnNullElements = null;

		if (size != data.length) {
			columnData = new boolean[size];
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

		BooleanDataColumn column = new BooleanDataColumn();
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

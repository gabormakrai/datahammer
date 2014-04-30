package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.StringDataColumn;

public class StringDataColumnFactory extends AbstractColumnFactory {

	String[] data;

	public StringDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed, false);
		data = new String[bufferStep];
	}

	@Override
	public void addElement(String element) {

		if (size == data.length) {
			String[] newData = new String[data.length + bufferStep];
			for (int i = 0; i < data.length; i++) {
				newData[i] = data[i];
			}
			data = null;
			data = newData;
		}

		if (element == null || element.trim().equals("")) {
			if (!nullAllowed) {
				throw new RuntimeException("Null is not allowed on column: " + name + " (string)");
			}
			data[size] = null;
		} else {
			data[size] = element;
		}

		size++;
	}

	@Override
	public AbstractDataColumn finishColumn() {

		String[] columnData = null;

		if (size != data.length) {
			columnData = new String[size];
			for (int i = 0; i < size; i++) {
				columnData[i] = data[i];
			}
		} else {
			columnData = data;
		}

		StringDataColumn column = new StringDataColumn();
		column.setName(name);
		column.setRole(role);
		column.setData(columnData);
		return column;
	}

}

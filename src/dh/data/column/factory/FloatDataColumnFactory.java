package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.FloatDataColumn;

public class FloatDataColumnFactory extends AbstractColumnFactory {

	float[] data;

	public FloatDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed, nullAllowed);
		data = new float[bufferStep];
	}

	@Override
	public void addElement(String element) {

		if (size == data.length) {
			float[] newData = new float[data.length + bufferStep];
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
				throw new RuntimeException("Null is not allowed on column: " + name + " (double)");
			}
			data[size] = 0.0f;
			nullElements[size] = true;
		} else {
			try {
				data[size] = Float.parseFloat(element);
				if (nullAllowed) {
					nullElements[size] = false;
				}
			} catch (NumberFormatException e) {
				data[size] = 0.0f;
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

		float[] columnData = null;
		boolean[] columnNullElements = null;

		if (size != data.length) {
			columnData = new float[size];
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

		FloatDataColumn column = new FloatDataColumn();
		column.setName(name);
		column.setRole(role);
		column.setData(columnData);
		if (nullAllowed) {
			column.setNullElements(columnNullElements);
		}
		return column;
	}

}

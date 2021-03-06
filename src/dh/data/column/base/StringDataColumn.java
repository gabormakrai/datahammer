package dh.data.column.base;

import java.util.Arrays;

import dh.data.column.AbstractDataColumn;

public class StringDataColumn extends AbstractDataColumn {

	private static final long serialVersionUID = 1L;

	private String[] data;

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	@Override
	public AbstractDataColumn copy(String newColumnName) {
		StringDataColumn newColumn = new StringDataColumn();
		newColumn.setName(newColumnName);
		newColumn.setRole(role);
		newColumn.setData(Arrays.copyOf(data, data.length));
		return newColumn;
	}

	@Override
	public String getElement(int index) {
		if (data[index] == null) {
			return "NULL";
		} else {
			return "" + data[index];
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		data = null;
	}

	@Override
	public String getColumnType() {
		return "string";
	}

	@Override
	public void filter(boolean[] filterArray) {

		int newSize = 0;
		for (int i = 0; i < data.length; i++) {
			if (filterArray[i]) {
				newSize++;
			}
		}

		int newIndex = 0;
		String[] newData = new String[newSize];
		for (int i = 0; i < data.length; i++) {
			if (filterArray[i]) {
				newData[newIndex] = data[i];
				newIndex++;
			}
		}
		data = null;
		data = newData;

		if (nullElements != null) {
			newIndex = 0;
			boolean[] newNullElements = new boolean[newSize];
			for (int i = 0; i < newNullElements.length; i++) {
				if (filterArray[i]) {
					newNullElements[newIndex] = nullElements[i];
					newIndex++;
				}
			}
			nullElements = null;
			nullElements = newNullElements;
		}
	}

	@Override
	public void reorder(int[] order) {

		String[] newData = new String[data.length];
		for (int i = 0; i < newData.length; i++) {
			newData[i] = data[order[i]];
		}
		data = null;
		data = newData;

		if (nullElements != null) {
			boolean[] newNulls = new boolean[getSize()];
			for (int i = 0; i < newNulls.length; i++) {
				newNulls[i] = nullElements[order[i]];
			}
			nullElements = null;
			nullElements = newNulls;
		}
	}

	@Override
	public int compare(int i1, int i2) {
		return data[i1].compareTo(data[i2]);
	}

	@Override
	public void merge(AbstractDataColumn column) {
		if (!(column instanceof LongDataColumn)) {
			throw new RuntimeException("Column " + column + " is not compatible with this column " + this);
		}
		
		StringDataColumn otherColumn = (StringDataColumn)column;
				
		String[] newData = new String[data.length + otherColumn.data.length];
				
		for (int i = 0; i < data.length; ++i) {
			newData[i] = data[i];
		}
		for (int i = 0; i < otherColumn.data.length; ++i) {
			newData[data.length + i] = otherColumn.data[i];
		}
		
		data = null;
		data = newData;
	}
	
	@Override
	public int getSize() {
		return data.length;
	}
	
}

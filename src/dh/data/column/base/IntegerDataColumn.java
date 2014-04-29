package dh.data.column.base;

import java.util.Arrays;

import dh.data.column.AbstractDataColumn;

public class IntegerDataColumn extends AbstractDataColumn {

	private static final long serialVersionUID = 1L;

	private int[] data;

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	@Override
	public AbstractDataColumn copy(String newColumnName) {
		IntegerDataColumn newColumn = new IntegerDataColumn();
		newColumn.setName(newColumnName);
		newColumn.setSize(size);
		newColumn.setRole(role);
		newColumn.setData(Arrays.copyOf(data, data.length));
		if (nullElements != null) {
			newColumn.setNullElements(Arrays.copyOf(nullElements, nullElements.length));
		}
		return newColumn;
	}

	@Override
	public String getElement(int index) {
		if (nullElements == null) {
			return "" + data[index];
		} else {
			if (nullElements[index]) {
				return "NULL";
			} else {
				return "" + data[index];
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		data = null;
	}

	@Override
	public String getColumnType() {
		return "integer";
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
		int[] newData = new int[newSize];
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

		setSize(newSize);
	}

	@Override
	public void reorder(int[] order) {

		int[] newData = new int[size];
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
		if (data[i1] < data[i2]) {
			return -1;
		} else if (data[i1] > data[i2]) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void merge(AbstractDataColumn column) {
		if (!(column instanceof IntegerDataColumn)) {
			throw new RuntimeException("Column " + column + " is not compatible with this column " + this);
		}
		
		IntegerDataColumn otherColumn = (IntegerDataColumn)column;
		
		if (nullElements == null && otherColumn.nullElements != null || nullElements != null && otherColumn.nullElements == null) {
			throw new RuntimeException("You can only merge the same column (with same null feature)");
		}
		
		int[] newData = new int[data.length + otherColumn.data.length];
		boolean[] newNull = null;
		
		if (nullElements != null) {
			newNull = new boolean[nullElements.length + otherColumn.nullElements.length];
		}
		
		for (int i = 0; i < data.length; ++i) {
			newData[i] = data[i];
		}
		for (int i = 0; i < otherColumn.data.length; ++i) {
			newData[data.length + i] = otherColumn.data[i];
		}
		
		data = null;
		data = newData;
		
		if (newNull != null) {
			for (int i = 0; i < data.length; ++i) {
				newData[i] = data[i];
			}
			for (int i = 0; i < otherColumn.data.length; ++i) {
				newData[data.length + i] = otherColumn.data[i];
			}
			
			nullElements = null;
			nullElements = newNull;
		}
	}
}

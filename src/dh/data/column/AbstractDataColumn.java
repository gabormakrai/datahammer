package dh.data.column;

import java.io.Serializable;

public abstract class AbstractDataColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected String role;

	protected boolean[] nullElements = null;

	public abstract String getColumnType();

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public abstract int getSize();

	public boolean[] getNullElements() {
		return nullElements;
	}

	public abstract AbstractDataColumn copy(String newColumnName);

	public boolean elementNull(int index) {
		if (this.nullElements == null) {
			return false;
		} else {
			return nullElements[index];
		}
	}

	public void setElementNull(int index, boolean isNull) {
		if (this.nullElements == null) {
			throw new RuntimeException("Trying to set an element to nullable in NotNullable column...");
		} else {
			this.nullElements[index] = isNull;
		}
	}

	public void setNullElements(boolean[] nullElements) {
		this.nullElements = nullElements;
	}

	public abstract String getElement(int index);

	public void dispose() {
		nullElements = null;
	}

	public abstract void filter(boolean[] filterArray);

	public abstract void reorder(int[] order);
	
	public abstract void merge(AbstractDataColumn column);

	public int calculateNullStat() {
		int nullValues = 0;
		for (int i = 0; i < this.getSize(); i++) {
			if (this.elementNull(i)) {
				nullValues++;
			}
		}
		return nullValues;
	}

	public abstract int compare(int i1, int i2);
}

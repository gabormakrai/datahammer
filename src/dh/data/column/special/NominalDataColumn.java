package dh.data.column.special;

import java.util.Arrays;
import java.util.HashMap;

import dh.data.column.AbstractDataColumn;

public class NominalDataColumn extends AbstractDataColumn {

	private static final long serialVersionUID = 1L;

	int[] data;

	HashMap<String, Integer> mapping = new HashMap<>();

	HashMap<Integer, String> reverseMapping = new HashMap<>();

	public void setData(int[] data) {
		this.data = data;
	}

	public int[] getData() {
		return data;
	}

	public HashMap<String, Integer> getMapping() {
		return mapping;
	}

	public void setMapping(HashMap<String, Integer> mapping) {
		this.mapping = mapping;
	}

	public HashMap<Integer, String> getReverseMapping() {
		return reverseMapping;
	}

	public void setReverseMapping(HashMap<Integer, String> reverseMapping) {
		this.reverseMapping = reverseMapping;
	}

	public String getString(int index) {
		return reverseMapping.get(data[index]);
	}

	public void setString(int index, String value) {
		if (mapping.containsKey(value)) {
			data[index] = mapping.get(value);
		} else {
			int id = mapping.size();
			mapping.put(value, id);
			reverseMapping.put(id, value);
			data[index] = id;
		}
	}

	@Override
	public AbstractDataColumn copy(String newColumnName) {
		NominalDataColumn newColumn = new NominalDataColumn();
		newColumn.setName(newColumnName);
		newColumn.setSize(this.getSize());
		newColumn.setRole(this.getRole());
		newColumn.setData(Arrays.copyOf(this.data, this.data.length));
		return newColumn;
	}

	@Override
	public String getElement(int index) {
		if (data[index] == -1) {
			return "NULL";
		} else {
			return getString(index);
		}
	}

	@Override
	public void dispose() {
		data = null;
		mapping.clear();
		mapping = null;
		reverseMapping.clear();
		reverseMapping = null;
	}

	@Override
	public String getColumnType() {
		return "nominal";
	}

	@Override
	public void filter(boolean[] filterArray) {

		int newSize = 0;

		for (int i = 0; i < data.length; i++) {
			if (filterArray[i]) {
				newSize++;
			}
		}

		int[] newData = new int[newSize];

		int newIndex = 0;
		for (int i = 0; i < data.length; i++) {
			if (filterArray[i]) {
				newData[newIndex] = data[i];
				newIndex++;
			}
		}

		data = null;
		data = newData;

		// TODO: data update (-1 null, 0-x continous number series)

		// TODO: mapping update ...

		// TODO: inverseMapping update ...

		setSize(newSize);
	}

	@Override
	public boolean elementNull(int index) {
		return data[index] == -1;
	}

	@Override
	public void reorder(int[] order) {

		int[] newData = new int[size];
		for (int i = 0; i < newData.length; i++) {
			newData[i] = data[order[i]];
		}
		data = null;
		data = newData;

	}

	@Override
	public int compare(int i1, int i2) {
		return reverseMapping.get(data[i1]).compareTo(reverseMapping.get(data[i2]));
	}
}

package dh.data.column.factory;

import java.util.HashMap;

import dh.data.column.AbstractDataColumn;
import dh.data.column.special.NominalDataColumn;

public class NominalDataColumnFactory extends AbstractColumnFactory {

	int[] data;
	HashMap<String, Integer> mapping;
	HashMap<Integer, String> reverseMapping;

	public NominalDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
		super(name, role, bufferStep, nullAllowed, false);

		data = new int[bufferStep];
		mapping = new HashMap<>();
		reverseMapping = new HashMap<>();
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

		}

		if (element == null || element.trim().equals("")) {
			if (!nullAllowed) {
				throw new RuntimeException("Null is not allowed on column: " + name + " (nominal)");
			}
			data[size] = -1;
		} else {
			Integer id = null;
			if (!mapping.containsKey(element)) {
				id = mapping.size();
				mapping.put(element, id);
				reverseMapping.put(id, element);
			} else {
				id = mapping.get(element);
			}
			data[size] = id;
		}

		++size;
	}

	@Override
	public AbstractDataColumn finishColumn() {

		int[] columnData = null;

		if (size != data.length) {
			columnData = new int[size];
			for (int i = 0; i < size; i++) {
				columnData[i] = data[i];
			}
		} else {
			columnData = data;
		}

		NominalDataColumn column = new NominalDataColumn();
		column.setName(name);
		column.setRole(role);
		column.setData(columnData);
		column.setMapping(mapping);
		column.setReverseMapping(reverseMapping);

		return column;
	}

}

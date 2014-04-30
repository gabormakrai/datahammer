package dh.command.converters;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class ConvertColumnStringToNominal extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(ConvertColumnStringToNominal.class);

	public ConvertColumnStringToNominal(Repository repository) {
		super(repository);
	}

	public void run(String tableName, String inputColumnName, String outputColumnName) {

		Table table = getTable(tableName);

		AbstractDataColumn inputDataColumn = table.getColumn(inputColumnName);

		checkTableDoesNotHaveColumn(table, outputColumnName);

		int[] data = new int[inputDataColumn.getSize()];

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		HashMap<Integer, String> mapInverse = new HashMap<Integer, String>();

		for (int i = 0; i < data.length; i++) {
			String element = inputDataColumn.getElement(i);
			if (element == null) {
				data[i] = -1;
			} else {
				if (map.containsKey(element)) {
					data[i] = map.get(element);
				} else {
					int id = map.size();
					map.put(element, id);
					mapInverse.put(id, element);
					data[i] = id;
				}
			}
		}

		NominalDataColumn newColumn = new NominalDataColumn();
		newColumn.setName(outputColumnName);
		newColumn.setRole("");
		newColumn.setData(data);
		newColumn.setMapping(map);
		newColumn.setReverseMapping(mapInverse);

		table.getColumns().put(newColumn.getName(), newColumn);

		logger.info("Column " + inputColumnName + " is converted (nominal) to column " + outputColumnName);
	}

}

package dh.command.nulls;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class NominalNulls extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(NominalNulls.class);

	public NominalNulls(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String inputColumn) {

		Table table = getTable(inputTableName);

		NominalDataColumn column = table.getColumn(inputColumn);

		// check that is there any null in the data
		int[] data = column.getData();

		boolean noNull = true;

		for (int i = 0; i < data.length; ++i) {
			if (data[i] == -1) {
				noNull = false;
				break;
			}
		}

		if (noNull) {
			logger.info("There is no NULL value in the column " + inputColumn + "...");
			return;
		}

		HashMap<String, Integer> mapping = column.getMapping();
		HashMap<Integer, String> reverseMapping = column.getReverseMapping();

		String nullLabel = "NULL";
		int nullId = -1;

		while (true) {
			if (!mapping.containsKey(nullLabel)) {
				break;
			} else {
				nullLabel += "_";
			}
		}

		nullId = mapping.size();
		mapping.put(nullLabel, nullId);
		reverseMapping.put(nullId, nullLabel);

		for (int i = 0; i < data.length; ++i) {
			if (data[i] == -1) {
				data[i] = nullId;
			}
		}

		logger.info("Nominal column {} nulls are get a new label...", inputColumn);
	}

}

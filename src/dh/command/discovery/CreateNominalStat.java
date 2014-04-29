package dh.command.discovery;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.base.IntegerDataColumn;
import dh.data.column.base.StringDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class CreateNominalStat extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(CreateNominalStat.class);

	public CreateNominalStat(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, String columnName, String outputTableName) {

		if (repository.getTable(outputTableName) != null) {
			throw new RuntimeException("There is a table " + outputTableName + " in the repository...");
		}

		Table table = getTable(inputTableName);

		NominalDataColumn column = table.getColumn(columnName);

		int[] frequency = new int[column.getMapping().size()];
		boolean[] frequencyNullElements = new boolean[frequency.length];
		for (int i = 0; i < frequency.length; i++) {
			frequency[i] = 0;
			frequencyNullElements[i] = false;
		}

		int[] data = column.getData();

		for (int i = 0; i < data.length; i++) {
			frequency[data[i]]++;
		}

		String[] labels = new String[column.getMapping().size()];
		HashMap<Integer, String> reverseMapping = column.getReverseMapping();
		for (int i = 0; i < labels.length; i++) {
			labels[i] = reverseMapping.get(i);
		}

		Table result = new Table(outputTableName, labels.length);

		StringDataColumn labelsColumn = new StringDataColumn();
		labelsColumn.setName("labels");
		labelsColumn.setData(labels);
		labelsColumn.setSize(labels.length);

		IntegerDataColumn frequencyColumn = new IntegerDataColumn();
		frequencyColumn.setName("frequency");
		frequencyColumn.setData(frequency);
		frequencyColumn.setNullElements(frequencyNullElements);
		frequencyColumn.setSize(labels.length);

		result.getColumns().put(labelsColumn.getName(), labelsColumn);
		result.getColumns().put(frequencyColumn.getName(), frequencyColumn);

		repository.getTables().put(outputTableName, result);

		logger.info("Nominal stat table " + outputTableName + " is added to repository...");
	}

}

package dh.command.marking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Repository;
import dh.repository.Table;

public class HomogenMarking extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(HomogenMarking.class);

	public HomogenMarking(Repository repository) {
		super(repository);
	}

	public void run(String inputTableName, MarkingType mark) {

		Table table = getTable(inputTableName);

		boolean remark = false;
		MarkingType[] data = null;

		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("marking")) {
				remark = true;
				data = ((MarkingColumn) column).getData();
			}
		}

		if (!remark) {
			data = new MarkingType[table.getSize()];
			MarkingColumn marking = new MarkingColumn();
			marking.setName("marking");
			marking.setRole("marking");
			marking.setData(data);
			table.getColumns().put("marking", marking);
		}

		for (int i = 0; i < data.length; i++) {
			data[i] = mark;
		}

		if (remark) {
			logger.info("Table " + table.getName() + " is REmarked with " + table.getSize() + " " + mark.toString() + "...");
		} else {
			logger.info("Table " + table.getName() + " is marked with " + table.getSize() + " " + mark.toString() + "...");
		}
	}

}

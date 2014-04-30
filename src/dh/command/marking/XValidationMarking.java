package dh.command.marking;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.IntegerDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Repository;
import dh.repository.Table;

public class XValidationMarking extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(XValidationMarking.class);

	public XValidationMarking(Repository repository) {
		super(repository);
	}

	public void main(String inputTableName, int fold, int f, Random randomParameter, boolean remark) {

		Table table = getTable(inputTableName);

		if (f >= fold) {
			throw new RuntimeException("Parameter f must be greater than fold...");
		}

		Random random = randomParameter;
		if (random == null) {
			random = new Random();
		}

		if (remark) {
			remark = false;
			for (AbstractDataColumn c : table.getColumns().values()) {
				if (c.getRole().equals("groups")) {
					remark = true;
				}
			}
		}

		MarkingType[] marking = null;
		int[] groups = null;

		if (remark) {
			for (AbstractDataColumn c : table.getColumns().values()) {
				if (c.getRole().equals("marking")) {
					marking = ((MarkingColumn) c).getData();
				} else if (c.getRole().equals("groups")) {
					groups = ((IntegerDataColumn) c).getData();
				}
			}
		} else {
			MarkingColumn markingColumn = new MarkingColumn();
			markingColumn.setName("marking");
			markingColumn.setRole("marking");
			marking = new MarkingType[table.getSize()];
			markingColumn.setData(marking);
			table.getColumns().put(markingColumn.getName(), markingColumn);

			IntegerDataColumn groupsColumn = new IntegerDataColumn();
			groupsColumn.setName("groups");
			groupsColumn.setRole("groups");
			groups = new int[table.getSize()];
			boolean[] groupsNull = new boolean[table.getSize()];

			int[] counter = new int[fold];
			for (int i = 0; i < counter.length; i++) {
				counter[i] = 0;
			}
			for (int i = 0; i < marking.length; i++) {
				groupsNull[i] = false;
				groups[i] = random.nextInt(fold);
				counter[groups[i]]++;
			}
			groupsColumn.setData(groups);
			groupsColumn.setNullElements(groupsNull);
			table.getColumns().put(groupsColumn.getName(), groupsColumn);

			logger.info("Group sizes:");
			for (int i = 0; i < counter.length; i++) {
				logger.info("\t" + i + ". group: " + counter[i]);
			}
		}

		int testSize = 0;
		int trainSize = 0;

		for (int i = 0; i < marking.length; i++) {
			if (groups[i] == f) {
				marking[i] = MarkingType.Test;
				testSize++;
			} else {
				marking[i] = MarkingType.Train;
				trainSize++;
			}
		}
		if (remark) {
			logger.info("Table " + table.getName() + " is REmarked with " + trainSize + " trainMark and " + testSize + " testMark...");
		} else {
			logger.info("Table " + table.getName() + " is marked with " + trainSize + " trainMark and " + testSize + " testMark...");
		}
	}
}

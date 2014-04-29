package dh.algorithms.utils.trainorder;

import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;

public class NormalTrainOrder extends AbstractTrainOrder {

	int[] order;
	MarkingType[] marking;

	public NormalTrainOrder(Table table) {
		super(table);
		order = new int[table.getSize()];
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("marking")) {
				marking = ((MarkingColumn) column).getData();
				break;
			}
		}
	}

	@Override
	public int[] generateOrder() {
		for (int i = 0; i < order.length; i++) {
			if (marking[i] == MarkingType.Train) {
				order[i] = i;
			} else {
				order[i] = -1;
			}
		}
		return order;
	}

}

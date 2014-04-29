package dh.algorithms.utils.trainorder;

import java.util.Random;

import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;

public class ShuffleRandomTrainOrder extends AbstractTrainOrder {

	Random random;
	int[] order;
	int length;
	MarkingType[] marking;

	public ShuffleRandomTrainOrder(Table table, Random random) {
		super(table);
		this.random = random;
		this.length = table.getSize();
		order = new int[length];
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
		for (int i = 0; i < length / 2; i++) {
			int i1 = random.nextInt(length);
			int i2 = random.nextInt(length);
			int t = order[i1];
			order[i1] = order[i2];
			order[i2] = t;
		}
		return order;
	}
}

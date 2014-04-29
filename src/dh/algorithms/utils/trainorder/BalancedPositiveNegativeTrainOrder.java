package dh.algorithms.utils.trainorder;

import java.util.Random;

import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.repository.Table;

public class BalancedPositiveNegativeTrainOrder extends AbstractTrainOrder {

	int positive;
	int negative;
	int[] order;
	ShuffleRandomTrainOrder orderGenerator;
	boolean[] data;

	public BalancedPositiveNegativeTrainOrder(Table table, Random random) {
		super(table);
		orderGenerator = new ShuffleRandomTrainOrder(table, random);
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals("target")) {
				BooleanDataColumn targetColumn = (BooleanDataColumn) column;
				data = targetColumn.getData();
				break;
			}
		}
		for (int i = 0; i < data.length; i++) {
			if (data[i]) {
				positive++;
			} else {
				negative++;
			}
		}
		if (positive < negative) {
			order = new int[positive * 2];
		} else if (positive >= negative) {
			order = new int[negative * 2];
		}
	}

	@Override
	public int[] generateOrder() {
		int localPositive = 0;
		int localNegative = 0;
		int[] generatedOrder = orderGenerator.generateOrder();
		for (int i = 0; i < generatedOrder.length; i++) {
			if (localNegative + localPositive == order.length) {
				break;
			}
			if (negative > positive && localNegative == positive && !data[generatedOrder[i]]) {
				continue;
			}
			if (positive > negative && localPositive == negative && data[generatedOrder[i]]) {
				continue;
			}
			order[localPositive + localNegative] = generatedOrder[i];
			if (data[generatedOrder[i]]) {
				localPositive++;
			} else {
				localNegative++;
			}
		}
		return order;
	}

}

package dh.algorithms.utils.trainorder;

import java.util.Random;

import dh.repository.Table;

public class FullRandomTrainOrder extends AbstractTrainOrder {

	Random random;
	boolean[] selected;
	int[] order;
	int length;

	public FullRandomTrainOrder(Table table, Random random) {
		super(table);
		this.random = random;
		this.length = table.getSize();
		selected = new boolean[length];
		order = new int[length];
	}

	@Override
	public int[] generateOrder() {
		for (int i = 0; i < length; i++) {
			selected[i] = false;
		}
		for (int i = 0; i < length; i++) {
			while (true) {
				int candidate = random.nextInt(length);
				if (selected[candidate] == false) {
					selected[candidate] = true;
					order[i] = candidate;
				} else {
					continue;
				}
				break;
			}
		}

		// for (int i=0;i<length;i++) {
		// order[i] = i;
		// }
		// for (int i=0;i<length/2;i++) {
		// int i1 = random.nextInt(length);
		// int i2 = random.nextInt(length);
		// int t = order[i1];
		// order[i1] = order[i2];
		// order[i2] = t;
		// }

		return order;
	}

}

package dh.algorithms.utils.trainorder;

import java.util.Random;

import dh.repository.Table;

public class TrainOrderFactory {
	public static AbstractTrainOrder create(String name, Table table, Random random) {
		if (name.equals("normal")) {
			return new NormalTrainOrder(table);
			// } else if (name.equals("fullrandom")) {
			// return new FullRandomTrainOrder(table, random);
		} else if (name.equals("shufflerandom")) {
			return new ShuffleRandomTrainOrder(table, random);
			// } else if (name.equals("balancedTF")) {
			// return new BalancedPositiveNegativeTrainOrder(table, random);
		} else {
			return null;
		}
	}
}

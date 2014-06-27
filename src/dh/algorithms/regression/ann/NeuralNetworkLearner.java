package dh.algorithms.regression.ann;

import java.util.HashMap;

import dh.algorithms.AbstractLearner;
import dh.repository.Model;
import dh.repository.Table;

public class NeuralNetworkLearner extends AbstractLearner {

	@Override
	protected Model learn(String modelName, Table table) {
		return null;
	}

	@Override
	public void initializeLearner(Table table, HashMap<String, String> parameters) {
	}

}

package dh.algorithms.classification.featureselection;

import java.util.HashMap;
import java.util.LinkedList;

import dh.algorithms.AbstractLearner;
import dh.algorithms.LearnerFactory;
import dh.algorithms.evaluation.AbstractEvaluator;
import dh.algorithms.evaluation.classification.ClassificationEvaluationFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;
import dh.repository.Model;

public class FeatureSelectionLearner extends AbstractLearner {

	private static final String skipRole = "skip";
	private static final String learnRole = "";

	AbstractLearner underlyingAlgorithm = null;

	int maximumColumns = 0;

	AbstractEvaluator evaluationMethod = null;

	HashMap<String, String> parameters = null;

	@Override
	public void initializeLearner(Table table, HashMap<String, String> parameters) {

		this.parameters = parameters;

		// underlying algorithm
		if (!parameters.containsKey("learner")) {
			throw new RuntimeException("FeatureSelectorLearner parameter learner is missing...");
		}

		underlyingAlgorithm = LearnerFactory.create(parameters.get("learner"));
		if (underlyingAlgorithm == null) {
			throw new RuntimeException("FeatureSelectorLearner learner algorithm (" + parameters.get("learner") + ") is not supported...");
		}

		underlyingAlgorithm.initializeLearner(table, parameters);
		if (!underlyingAlgorithm.isInitialized()) {
			throw new RuntimeException("Problem while initializing underlying algorithm...");
		}

		if (!parameters.containsKey("maximumcolumn")) {
			throw new RuntimeException("FeatureSelectorLearner must have maximumcolumn parameter...");
		}
		try {
			this.maximumColumns = Integer.parseInt(parameters.get("maximumcolumn"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("FeatureSelectorLearner maximumcolumn must be integer...");
		}

		if (!parameters.containsKey("evaluationmethod")) {
			throw new RuntimeException("FeatureSelectorLearner must have evaluationmethod parameter...");
		}
		String evaluationMethodName = parameters.get("evaluationmethod");
		if (evaluationMethodName.startsWith("classification")) {
			evaluationMethodName = evaluationMethodName.substring(evaluationMethodName.indexOf('.') + 1);
			evaluationMethod = ClassificationEvaluationFactory.createEvaluator(evaluationMethodName);
			if (evaluationMethod == null) {
				throw new RuntimeException("Classification evaluation method " + evaluationMethodName + " is not supported by FeatureSelectorLearner...");
			}
		} else {
			throw new RuntimeException("Only classification evaluation methods is supported by FeatureSelectorLearner...");
		}

		initialized = true;

	}

	@Override
	protected Model learn(String modelName, Table table) {

		// find all of the candidates...
		LinkedList<AbstractDataColumn> columns = new LinkedList<AbstractDataColumn>();
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole().equals(learnRole)) {
				columns.add(column);
			}
		}

		Model bestModel = null;

		for (int i = 0; i < maximumColumns; ++i) {

			AbstractDataColumn bestColumn = null;
			double bestEvaluationValue = Double.MIN_VALUE;

			for (AbstractDataColumn columnToAdd : columns) {
				for (AbstractDataColumn column : columns) {
					if (column == columnToAdd) {
						column.setRole(learnRole);
					} else {
						column.setRole(skipRole);
					}
				}

				underlyingAlgorithm.initializeLearner(table, parameters);
				Model m = underlyingAlgorithm.learnModel(modelName, table);
				m.apply(table);
				double evaluationValue = evaluationMethod.evaluate(table, MarkingType.Train).getValue();

				// testing
				// String output = "";
				// for (AbstractDataColumn c : table.getColumns().values()) {
				// if (c.getRole() == learnRole) {
				// output += c.getName() + ",";
				// }
				// }
				// output += "->" + evaluationValue;
				// logger.log(output);
				if (bestColumn == null) {
					bestColumn = columnToAdd;
					bestEvaluationValue = evaluationValue;
					if (i == maximumColumns - 1) {
						bestModel = m;
					}
				}
				if (bestEvaluationValue > evaluationValue) {
					bestColumn = columnToAdd;
					bestEvaluationValue = evaluationValue;
					if (i == maximumColumns - 1) {
						bestModel = m;
					}
				}
			}

			bestColumn.setRole(learnRole);
			columns.remove(bestColumn);
		}

		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getRole() == skipRole) {
				column.setRole(learnRole);
			}
		}

		return bestModel;
	}

}

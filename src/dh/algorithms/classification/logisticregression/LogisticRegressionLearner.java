package dh.algorithms.classification.logisticregression;

import java.util.Random;

import dh.algorithms.AbstractLearner;
import dh.algorithms.utils.trainorder.AbstractTrainOrder;
import dh.algorithms.utils.trainorder.TrainOrderFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;
import dh.repository.Model;

public class LogisticRegressionLearner extends AbstractLearner {
	double learningRate;
	int iteration;

	DoubleDataColumn[] columns = null;
	double[] weights = null;
	BooleanDataColumn target = null;
	AbstractTrainOrder generator = null;
	double[] trainWeights = null;

	boolean intercept;
	Random random;

	@Override
	public void initializeLearner(Table table, java.util.HashMap<String, String> parameters) {
		if (!parameters.containsKey("learningrate")) {
			throw new RuntimeException("Logistic regression must have learningrate parameter...");
		}
		try {
			this.learningRate = Double.parseDouble(parameters.get("learningrate"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Logistic regression's learningrate must be double...");
		}

		if (!parameters.containsKey("iteration")) {
			throw new RuntimeException("Logistic regression must have iteration parameter...");
		}
		try {
			this.iteration = Integer.parseInt(parameters.get("iteration"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("Logistic regression's iteration must be int...");
		}

		if (parameters.containsKey("random")) {
			try {
				this.random = new Random(Long.parseLong(parameters.get("random")));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Logistic regression's random must be long...");
			}
		} else {
			random = new Random();
		}

		if (!parameters.containsKey("intercept")) {
			throw new RuntimeException("Logistic regression must have intercept parameter...");
		} else {
			if (parameters.get("intercept").equals("true")) {
				this.intercept = true;
			} else if (parameters.get("intercept").equals("false")) {
				this.intercept = false;
			} else {
				throw new RuntimeException("Logistic regression's random must be true/false...");
			}
		}
		String trainOrderString = null;
		if (!parameters.containsKey("trainorder")) {
			throw new RuntimeException("Logistic regression must have trainorder parameter...");
		} else {
			trainOrderString = parameters.get("trainorder");
		}

		generator = TrainOrderFactory.create(trainOrderString, table, random);

		if (generator == null) {
			throw new RuntimeException("Train order generator " + trainOrderString + " is not supported...");
		}

		// search for the columns and check them
		int dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("double") && column.getRole().equals("")) {
				dataColumnCounter++;
			}
			if (column.getColumnType().equals("boolean") && column.getRole().equals("target")) {
				target = (BooleanDataColumn) column;
			}
			if (column.getRole().equals("weight")) {
				trainWeights = ((DoubleDataColumn) column).getData();
			}
		}

		if (dataColumnCounter == 0) {
			throw new RuntimeException("There is no double column without role...");
		}

		if (target == null) {
			throw new RuntimeException("There is no target boolean column...");
		}

		columns = new DoubleDataColumn[dataColumnCounter];
		dataColumnCounter = 0;
		for (AbstractDataColumn column : table.getColumns().values()) {
			if (column.getColumnType().equals("double") && column.getRole().equals("")) {
				columns[dataColumnCounter++] = (DoubleDataColumn) column;
			}
		}

		weights = new double[columns.length + 1];

		initialized = true;
	}

	public static int counter = 0;

	@Override
	protected Model learn(String modelName, Table table) {

		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble();
		}

		double[][] x = new double[columns.length][];
		for (int i = 0; i < columns.length; i++) {
			x[i] = columns[i].getData();
		}
		boolean[] yBoolean = target.getData();

		double y = 0.0;
		double pred = 0.0;
		double expression = 0.0;

		int[] order = null;

		for (int i = 0; i < iteration; i++) {
			order = generator.generateOrder();
			for (int j = 0; j < order.length; j++) {
				if (order[j] == -1) {
					continue;
				}
				if (yBoolean[order[j]]) {
					y = 1.0;
				} else {
					y = -1.0;
				}
				if (intercept) {
					pred = weights[0];
				} else {
					pred = 0.0;
				}
				for (int k = 0; k < columns.length; k++) {
					pred += weights[k + 1] * x[k][order[j]];
				}
				if (trainWeights == null) {
					expression = learningRate * (y - sigmoid(pred));
				} else {
					expression = trainWeights[order[j]] * learningRate * (y - sigmoid(pred));
				}

				if (intercept) {
					weights[0] += expression * 1.0;
				}
				for (int k = 0; k < columns.length; k++) {
					weights[k + 1] += expression * x[k][order[j]];
				}
			}
		}

		String[] columnNames = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			columnNames[i] = columns[i].getName();
		}

		return new RegressionModel(modelName, columnNames, weights, intercept);
	}

	private double sigmoid(double x) {
		return 2 / (1 + Math.exp(-1.0 * Math.E * x)) - 1.0;
	}

}

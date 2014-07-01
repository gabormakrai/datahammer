package dh.algorithms.evaluation.regression;

import dh.algorithms.evaluation.AbstractRegressionEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class RootMeanSquareError extends AbstractRegressionEvaluator {
	public static class RootMeanSquareErrorResult extends EvaluationResult {
		double error;
		int instances;

		public RootMeanSquareErrorResult(double error, int instances) {
			this.error = error;
			this.instances = instances;
		}

		@Override
		public String toString() {
			return "Regression root mean square error: " + error + " (" + instances + ")";
		}

		@Override
		public double getValue() {
			return error;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Regression root mean square error";
		}
	}

	@Override
	protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
		return super.check(targetColumn, predictionColumn, numericPredicition, weightColumn);
	}

	@Override
	protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {

		DoubleDataColumn target = (DoubleDataColumn) targetColumn;
		DoubleDataColumn prediction = (DoubleDataColumn) predictionColumn;

		MarkingType[] marking = null;
		if (markingColumn != null) {
			marking = ((MarkingColumn) markingColumn).getData();
		}

		double[] targetData = target.getData();
		double[] predictionData = prediction.getData();

		double error = 0.0;
		int instances = 0;
		
		for (int i = 0; i < target.getSize(); i++) {
			if (marking == null || marking[i] == markingType) {
				error += Math.pow(targetData[i] - predictionData[i], 2.0);
				++instances;
			}
		}
		return new RootMeanSquareErrorResult(error / (double)instances, instances);
	}
}

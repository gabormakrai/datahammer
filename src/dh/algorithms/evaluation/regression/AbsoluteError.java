package dh.algorithms.evaluation.regression;

import dh.algorithms.evaluation.AbstractRegressionEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class AbsoluteError extends AbstractRegressionEvaluator {
	public static class AbsoluteErrorResult extends EvaluationResult {
		double error;

		public AbsoluteErrorResult(double error) {
			this.error = error;
		}

		@Override
		public String toString() {
			return "Regression absolute error: " + error;
		}

		@Override
		public double getValue() {
			return error;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Regression absolute error";
		}
	}

	@Override
	protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
		return super.check(targetColumn, predictionColumn, numericPredicition, weightColumn);
	}

	@Override
	protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType,
			AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {
		double error = 0.0;

		DoubleDataColumn target = (DoubleDataColumn) targetColumn;
		DoubleDataColumn prediction = (DoubleDataColumn) predictionColumn;

		MarkingType[] marking = null;
		if (markingColumn != null) {
			marking = ((MarkingColumn) markingColumn).getData();
		}

		double[] targetData = target.getData();
		double[] predictionData = prediction.getData();

		for (int i = 0; i < target.getSize(); i++) {
			if (marking == null || marking[i] == markingType) {
				error += Math.abs(targetData[i] - predictionData[i]);
			}
		}
		return new AbsoluteErrorResult(error);
	}
}

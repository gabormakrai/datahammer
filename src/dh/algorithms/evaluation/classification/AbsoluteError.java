package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class AbsoluteError extends AbstractClassificationEvaluator {
	public static class AbsoluteErrorResult extends EvaluationResult {
		double error;;

		public AbsoluteErrorResult(double error) {
			this.error = error;
		}

		@Override
		public String toString() {
			return "Classification absolute error: " + error + ")";
		}

		@Override
		public double getValue() {
			return error;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification absolute error";
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

		BooleanDataColumn target = (BooleanDataColumn) targetColumn;
		DoubleDataColumn nPrediction = (DoubleDataColumn) numericPredicition;

		MarkingType[] marking = null;
		if (markingColumn != null) {
			marking = ((MarkingColumn) markingColumn).getData();
		}

		boolean[] targetData = target.getData();
		double[] numericPredictionData = nPrediction.getData();

		double targetValue = 0.0;

		for (int i = 0; i < target.getSize(); i++) {
			if (marking == null || marking[i] == markingType) {
				if (targetData[i]) {
					targetValue = 1.0;
				} else {
					targetValue = -1.0;
				}
				error += Math.abs(targetValue - numericPredictionData[i]);
			}
		}
		return new AbsoluteErrorResult(error);
	}
}

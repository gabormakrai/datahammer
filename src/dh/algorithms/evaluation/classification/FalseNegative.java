package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class FalseNegative extends AbstractClassificationEvaluator {

	public static class FalseNegativeResult extends EvaluationResult {
		int fn = 0;
		int count = 0;

		public FalseNegativeResult(int fn, int count) {
			this.fn = fn;
			this.count = count;
		}

		@Override
		public String toString() {
			return "Classification FN: " + ((double) fn / (double) count) + "(" + fn + "/" + count + ")";
		}

		@Override
		public double getValue() {
			return (double) fn / (double) count;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification FalseNegative";
		}
	}

	@Override
	protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType,
			AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {
		int hit = 0;
		int count = 0;

		BooleanDataColumn target = (BooleanDataColumn) targetColumn;
		BooleanDataColumn prediction = (BooleanDataColumn) predictionColumn;

		MarkingType[] marking = null;
		if (markingColumn != null) {
			marking = ((MarkingColumn) markingColumn).getData();
		}

		boolean[] targetData = target.getData();
		boolean[] predictionData = prediction.getData();

		for (int i = 0; i < target.getSize(); i++) {
			if (marking == null || marking[i] == markingType) {
				if (!targetData[i]) {
					count++;
					if (predictionData[i]) {
						hit++;
					}
				}
			}
		}
		return new FalseNegativeResult(hit, count);
	}
}

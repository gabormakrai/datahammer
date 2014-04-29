package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class Accuracy extends AbstractClassificationEvaluator {
	public static class AccuracyResult extends EvaluationResult {
		int hit = 0;
		int count = 0;

		public AccuracyResult(int hit, int count) {
			this.hit = hit;
			this.count = count;
		}

		@Override
		public String toString() {
			return "Classification accuracy: " + ((double) hit / (double) count) + "(" + hit + "/" + count + ")";
		}

		@Override
		public double getValue() {
			return (double) hit / (double) count;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification accuracy";
		}
	}

	@Override
	protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
		return super.check(targetColumn, predictionColumn, numericPredicition, weightColumn);
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
				count++;
				if (targetData[i] == predictionData[i]) {
					hit++;
				}
			}
		}
		return new AccuracyResult(hit, count);
	}
}

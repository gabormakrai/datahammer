package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class Recall extends AbstractClassificationEvaluator {

	public static class RecallResult extends EvaluationResult {
		int tp = 0;
		int fn = 0;

		public RecallResult(int tp, int fn) {
			this.tp = tp;
			this.fn = fn;
		}

		@Override
		public String toString() {
			return "Classification recall: " + ((double) tp / (double) (tp + fn));
		}

		@Override
		public double getValue() {
			return (double) tp / (double) (tp + fn);
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification Recall";
		}
	}

	@Override
	protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType,
			AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {

		int tp = 0;
		int fn = 0;

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
				if (targetData[i] && predictionData[i]) {
					tp++;
				} else if (!targetData[i] && predictionData[i]) {
					fn++;
				}
			}
		}
		return new RecallResult(tp, fn);
	}
}

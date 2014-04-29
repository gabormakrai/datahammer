package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class TruePositive extends AbstractClassificationEvaluator {

	public static class TruePositiveResult extends EvaluationResult {
		int tp = 0;
		int count = 0;

		public TruePositiveResult(int tp, int count) {
			this.tp = tp;
			this.count = count;
		}

		@Override
		public String toString() {
			return "Classification TP: " + ((double) tp / (double) count) + "(" + tp + "/" + count + ")";
		}

		@Override
		public double getValue() {
			return (double) tp / (double) count;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification TruePositive";
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
				if (targetData[i]) {
					count++;
					if (targetData[i] == predictionData[i]) {
						hit++;
					}
				}
			}
		}
		return new TruePositiveResult(hit, count);
	}
}

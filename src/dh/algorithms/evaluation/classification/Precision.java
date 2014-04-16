package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class Precision extends AbstractClassificationEvaluator {

	public static class PrecisionResult extends EvaluationResult {
		int tp = 0;
		int fp = 0;

		public PrecisionResult(int tp, int fp) {
			this.tp = tp;
			this.fp = fp;
		}

		@Override
		public String toString() {
			return "Classification precision: " + ((double) tp / (double) (tp + fp));
		}

		@Override
		public double getValue() {
			return (double) tp / (double) (tp + fp);
		}

		@Override
		public String getEvaluationMethodName() {
			return "Classification Precision";
		}
	}

	@Override
	protected EvaluationResult evaluate(AbstractDataColumn targetColumn,
			AbstractDataColumn predictionColumn,
			AbstractDataColumn markingColumn, MarkingType markingType,
			AbstractDataColumn numericPredicition,
			AbstractDataColumn weightColumn,
			AbstractDataColumn nominalPrediction) {
		
		int tp = 0;
		int fp = 0;

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
				} else if (!targetData[i] && !predictionData[i]) {
					fp++;
				}
			}
		}
		return new PrecisionResult(tp, fp);
	}
}

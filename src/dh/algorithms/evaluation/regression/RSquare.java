package dh.algorithms.evaluation.regression;

import dh.algorithms.evaluation.AbstractRegressionEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;

public class RSquare extends AbstractRegressionEvaluator {
	public static class RSquareResult extends EvaluationResult {
		double rSquare;

		public RSquareResult(double rSquare) {
			this.rSquare = rSquare;
		}

		@Override
		public String toString() {
			return "Regression R-Sqaure: " + rSquare;
		}

		@Override
		public double getValue() {
			return rSquare;
		}

		@Override
		public String getEvaluationMethodName() {
			return "Regression R-Sqaure";
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
		
		double average = 0.0;
		int instances = 0;

		for (int i = 0; i < targetData.length; ++i) {
			if (marking == null || marking[i] == markingType) {
				++instances;
				average += targetData[i]; 
			}
		}
		
		average /= (double)instances;
		
		double sumErrorSqaure = 0.0;
		double sumDiffAverage = 0.0;
		
		for (int i = 0; i < target.getSize(); ++i) {
			if (marking == null || marking[i] == markingType) {
				sumErrorSqaure += Math.pow(predictionData[i] - average, 2.0);
				sumDiffAverage += Math.pow(targetData[i] - average, 2.0);
			}
		}
		
		return new RSquareResult(sumErrorSqaure / sumDiffAverage);
	}
}

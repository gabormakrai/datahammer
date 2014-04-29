package dh.algorithms.evaluation;

import dh.data.column.AbstractDataColumn;

public class AbstractClassificationEvaluator extends AbstractEvaluator {

	@Override
	protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
		if (!predictionColumn.getColumnType().equals("boolean")) {
			throw new RuntimeException("Prediction column is not boolean data column...");
		}
		// if (!targetColumn.getColumnType().equals("boolean")) {
		// logger.log("Target column is not boolean data column...");
		// return false;
		// }
		if (numericPredicition != null && !numericPredicition.getColumnType().equals("double")) {
			throw new RuntimeException("Numeric prediction column has to be double...");
		}
		return true;
	}
}

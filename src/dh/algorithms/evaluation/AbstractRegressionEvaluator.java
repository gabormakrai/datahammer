package dh.algorithms.evaluation;

import dh.data.column.AbstractDataColumn;

public class AbstractRegressionEvaluator extends AbstractEvaluator {

	@Override
	protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
		if (!predictionColumn.getColumnType().equals("double")) {
			throw new RuntimeException("Prediction column is not double data column...");
		}
		if (!targetColumn.getColumnType().equals("double")) {
			throw new RuntimeException("Target column is not double data column...");
		}
		return true;
	}
}

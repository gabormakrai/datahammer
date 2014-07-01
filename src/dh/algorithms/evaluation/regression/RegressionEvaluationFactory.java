package dh.algorithms.evaluation.regression;

import dh.algorithms.evaluation.AbstractEvaluator;

public class RegressionEvaluationFactory {
	public static AbstractEvaluator createEvaluator(String name) {
		if (name.equals("absoluteerror")) {
			return new MeanAbsoluteError();
		} else if (name.equals("rsquare")) {
			return new RSquare();
		}
		return null;
	}
}

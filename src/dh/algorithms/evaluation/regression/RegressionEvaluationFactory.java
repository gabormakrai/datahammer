package dh.algorithms.evaluation.regression;

import dh.algorithms.evaluation.AbstractEvaluator;

public class RegressionEvaluationFactory {
	public static AbstractEvaluator createEvaluator(String name) {
		if (name.equals("mae") || name.equals("meanabsoluteerror")) {
			return new MeanAbsoluteError();
		} else if (name.equals("rsquare")) {
			return new RSquare();
		} else if (name.equals("rmse") || name.equals("rootmeansquareerror")) {
			return new RootMeanSquareError();
		}
		return null;
	}
}

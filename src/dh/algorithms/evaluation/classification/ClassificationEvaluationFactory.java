package dh.algorithms.evaluation.classification;

import dh.algorithms.evaluation.AbstractEvaluator;

public class ClassificationEvaluationFactory {
	public static AbstractEvaluator createEvaluator(String name) {
		if (name.equals("accuracy")) {
			return new Accuracy();
		} else if (name.equals("weightedaccuracy")) {
			return new WeightedAccuracy();
		} else if (name.equals("TP")) {
			return new TruePositive();
		} else if (name.equals("TN")) {
			return new TrueNegative();
		} else if (name.equals("FP")) {
			return new FalsePositive();
		} else if (name.equals("FN")) {
			return new FalseNegative();
		} else if (name.equals("AuC")) {
			return new AuC();
		} else if (name.equals("absoluteerror")) {
			return new AbsoluteError();
		} else if (name.equals("multilabelaccuracy")) {
			return new MultiLabelAccuracy();
		} else if (name.equals("precision")) {
			return new Precision();
		} else if (name.equals("recall")) {
			return new Recall();
		} else if (name.equals("fmeasure")) {
			return new FMeasure();
		}
		return null;
	}
}

package dh.algorithms;

import dh.algorithms.classification.bagging.BaggingLearner;
import dh.algorithms.classification.bayes.BayesLearner;
import dh.algorithms.classification.decisionstumps.FastDecisionStumpLearner;
import dh.algorithms.classification.featureselection.FeatureSelectionLearner;
import dh.algorithms.classification.kmeans.KMeansLearner;
import dh.algorithms.classification.knn.KNNLearner;
import dh.algorithms.classification.logisticregression.LogisticRegressionLearner;
import dh.algorithms.regression.ann.NeuralNetworkLearner;

public class LearnerFactory {

	public static AbstractLearner create(String name) {
		if (name.equals("classification.lr")) {
			return new LogisticRegressionLearner();
		} else if (name.equals("classification.fastdecisonstump")) {
			return new FastDecisionStumpLearner();
		} else if (name.equals("classification.bayes")) {
			return new BayesLearner();
		} else if (name.equals("classification.bagging")) {
			return new BaggingLearner();
		} else if (name.equals("classification.featureselection")) {
			return new FeatureSelectionLearner();
		} else if (name.equals("classification.kmeans")) {
			return new KMeansLearner();
		} else if (name.equals("classification.knn")) {
			return new KNNLearner();
		} else if (name.equals("regression.neuralnetwork")) {
			return new NeuralNetworkLearner();
		} else {
			throw new RuntimeException("Not supported learner...(" + name + ")");
		}
	}
}

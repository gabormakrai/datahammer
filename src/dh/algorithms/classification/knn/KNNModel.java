package dh.algorithms.classification.knn;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import dh.algorithms.classification.ClassificationModel;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Table;

public class KNNModel extends ClassificationModel {

	private static final long serialVersionUID = 1L;

	NominalDataColumn[] trainColumns = null;
	boolean[] target = null;
	MarkingType[] marking = null;
	int k;

	public KNNModel(NominalDataColumn[] columns, boolean[] target, MarkingType[] marking, int k) {
		this.k = k;
		this.trainColumns = columns;
		this.target = target;
		if (marking != null) {
			this.marking = new MarkingType[marking.length];
			for (int i = 0; i < marking.length; ++i) {
				this.marking[i] = marking[i];
			}
		}
	}

	@Override
	public void apply(Table table) {

		boolean[] prediction = getPredictionData(table);
		double[] numericPrediction = getNumericPredictionData(table);

		// create mapping conversion
		int[][] map = new int[trainColumns.length][];

		int[][] trainData = new int[trainColumns.length][];
		int[][] testData = new int[trainColumns.length][];

		for (int i = 0; i < trainColumns.length; ++i) {
			NominalDataColumn trainColumn = trainColumns[i];
			NominalDataColumn testColumn = (NominalDataColumn) table.getColumns().get(trainColumn.getName());
			if (testColumn == null) {
				continue;
			}
			HashMap<Integer, String> testReverseMapping = testColumn.getReverseMapping();
			HashMap<String, Integer> trainMapping = trainColumn.getMapping();
			map[i] = new int[testReverseMapping.size()];
			for (int j = 0; j < map[i].length; ++j) {
				String testLabel = testReverseMapping.get(j);
				int trainIndex = (trainMapping.containsKey(testLabel)) ? trainMapping.get(testLabel) : -1;
				map[i][j] = trainIndex;
			}
			trainData[i] = trainColumn.getData();
			testData[i] = testColumn.getData();
		}

		LinkedList<SimilarityObject> similarity = new LinkedList<>();

		for (int testIndex = 0; testIndex < table.getSize(); ++testIndex) {

			similarity.clear();

			for (int trainIndex = 0; trainIndex < trainColumns[0].getSize(); ++trainIndex) {
				similarity.add(calculateSimilarity(trainData, testData, map, target, trainIndex, testIndex));
				if (similarity.size() > k) {
					Collections.sort(similarity);
					similarity.removeLast();
				}
			}

			int p = 0;
			for (SimilarityObject s : similarity) {
				if (s.target) {
					++p;
				} else {
					--p;
				}
			}

			if (p >= 0) {
				prediction[testIndex] = true;
				numericPrediction[testIndex] = 1.0;
			} else {
				prediction[testIndex] = false;
				numericPrediction[testIndex] = -1.0;
			}
		}
	}

	private SimilarityObject calculateSimilarity(int[][] trainData, int[][] testData, int[][] map, boolean[] target, int trainIndex, int testIndex) {

		int sim = 0;

		for (int c = 0; c < trainData.length; ++c) {
			if (testData[c] == null) {
				continue;
			}
			int testLabel = testData[c][testIndex];
			int trainLabel = trainData[c][trainIndex];
			if (map[c][trainLabel] == testLabel) {
				++sim;
			}
		}

		return new SimilarityObject(sim, target[trainIndex]);
	}

	private class SimilarityObject implements Comparable<SimilarityObject> {
		int similarity;
		boolean target;

		public SimilarityObject(int similarity, boolean target) {
			this.similarity = similarity;
			this.target = target;
		}

		@Override
		public int compareTo(SimilarityObject o) {
			return (similarity < o.similarity) ? +1 : ((similarity == o.similarity) ? 0 : -1);
		}

		@Override
		public String toString() {
			return "Similarity(" + ((target) ? "+" : "-") + ",sim:" + similarity + ")";
		}
	}

}

package dh.algorithms.classification.ensemble;

import dh.algorithms.classification.ClassificationModel;
import dh.repository.Model;
import dh.repository.Table;

public class EnsembleModel extends ClassificationModel {

	private static final long serialVersionUID = 1L;

	Model[] models;

	public EnsembleModel(String name, Model[] models) {
		super(name);
		this.models = models;
	}

	@Override
	public void apply(Table table) {
		int[] p = new int[table.getSize()];
		for (int i = 0; i < p.length; ++i) {
			p[i] = 0;
		}
		for (Model model : models) {
			model.apply(table);
			boolean[] pred = getPredictionData(table);
			for (int i = 0; i < pred.length; ++i) {
				if (pred[i]) {
					++p[i];
				} else {
					--p[i];
				}
			}
		}
		boolean[] pred = getPredictionData(table);
		double[] numericPred = getNumericPredictionData(table);

		for (int i = 0; i < p.length; ++i) {
			if (p[i] >= 0) {
				pred[i] = true;
				numericPred[i] = 1.0;
			} else {
				pred[i] = false;
				numericPred[i] = 0.0;
			}
		}

	}

}

package dh.command.ml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.algorithms.evaluation.AbstractEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.algorithms.evaluation.classification.ClassificationEvaluationFactory;
import dh.command.AbstractCommand;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Repository;
import dh.repository.Table;

public class ModelEvaluation extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(ModelEvaluation.class);

	public ModelEvaluation(Repository repository) {
		super(repository);
	}

	public EvaluationResult run(String inputTableName, String evalMethod, MarkingType marking) {

		EvaluationResult mainEvaluationResul = null;

		Table table = getTable(inputTableName);

		if (evalMethod.startsWith("classification")) {
			String[] methods = evalMethod.substring(0, evalMethod.length() - 1).substring(evalMethod.indexOf("[") + 1).split("\\,");
			for (String m : methods) {
				AbstractEvaluator eval = ClassificationEvaluationFactory.createEvaluator(m);
				if (eval == null) {
					throw new RuntimeException("Classification evaluator: " + m + " is not supported...");
				}
				EvaluationResult res = eval.evaluate(table, marking);
				if (mainEvaluationResul == null) {
					mainEvaluationResul = res;
				}
				logger.info("Result: " + res.toString());
			}
		}

		logger.info("Model evaluation is completed...");

		return mainEvaluationResul;
	}

}

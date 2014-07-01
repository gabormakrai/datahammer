package dh.algorithms.regression.ann;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import dh.algorithms.evaluation.EvaluationResult;
import dh.algorithms.evaluation.regression.RegressionEvaluationFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Model;
import dh.repository.Table;

public class RegressionNeuralNetworkTest {
	
	Table testTable = null;
	
	private DoubleDataColumn createColumn(String name, double[] data) {
		DoubleDataColumn column = new DoubleDataColumn();
		column.setName(name);
		column.setRole("");
		column.setData(data);
		return column;
	}
	
	@Before
	public void setUp() {
		testTable = new Table("train", 4);
		double[] a1 = new double[] { 1.0, 0.1, 0.5, 0.25 };
		double[] a2 = new double[] { 0.1, 0.25, 0.75, 1.0 };
		double[] a3 = new double[] { 0.5, 0.5, 1.0, 0.25 };
		double[] a4 = new double[] { 0.0, 0.0, 0.0, 0.0 };
		MarkingType[] markings = new MarkingType[] { MarkingType.Test, MarkingType.Test, MarkingType.Test, MarkingType.Test };
		for (int i = 0; i < 4; ++i) {
			a4[i] = (1.0 * a1[i] + 2.0 * a2[i]) / 3.0; 
		}
		
		AbstractDataColumn a1Column = createColumn("a1", a1);
		AbstractDataColumn a2Column = createColumn("a2", a2);
		AbstractDataColumn a3Column = createColumn("a3", a3);
		AbstractDataColumn a4Column = createColumn("a4", a4);
		a4Column.setRole("target");
		
		MarkingColumn markingColumn = new MarkingColumn();
		markingColumn.setName("marking");
		markingColumn.setRole("marking");
		markingColumn.setData(markings);
		
		testTable.getColumns().put(a1Column.getName(), a1Column);
		testTable.getColumns().put(a2Column.getName(), a2Column);
		testTable.getColumns().put(a3Column.getName(), a3Column);
		testTable.getColumns().put(a4Column.getName(), a4Column);
		testTable.getColumns().put(markingColumn.getName(), markingColumn);
	}

	@Test
	public void testLearnCase1() {
		NeuralNetworkLearner learner = new NeuralNetworkLearner();
		
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("iteration", "10");
		parameters.put("activation", "sigmoid");
		parameters.put("layers", "5");
		parameters.put("learningrate", "0.3");
		parameters.put("random", "42");
		
		learner.initializeLearner(testTable, parameters);
		
		Model model = learner.learnModel("model", testTable);
		
		if (model instanceof NeuralNetworkModel == false) {
			fail("Produced model is not a Neural Network regression model");
		}
		NeuralNetworkModel nnModel = (NeuralNetworkModel)model;
		
		nnModel.apply(testTable);
		
		EvaluationResult result = RegressionEvaluationFactory.createEvaluator("rsquare").evaluate(testTable, MarkingType.Test);
		System.out.println(result);
		
		result = RegressionEvaluationFactory.createEvaluator("absoluteerror").evaluate(testTable, MarkingType.Test);
		System.out.println(result);
		
//		double[] target = ((DoubleDataColumn)testTable.getColumn("a4")).getData();
//		double[] prediction = ((DoubleDataColumn)testTable.getColumn("prediction")).getData();
		
	}
	
	
	
}

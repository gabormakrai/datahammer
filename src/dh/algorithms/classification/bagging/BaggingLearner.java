package dh.algorithms.classification.bagging;
 
import java.util.HashMap;
import java.util.Random;
 
import dh.algorithms.AbstractLearner;
import dh.algorithms.LearnerFactory;
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;
import dh.repository.Model;
 
 
public class BaggingLearner extends AbstractLearner{
   
    AbstractLearner underlyingAlgorithm = null;
    int numberOfBags = -1;
    double bagSize = 0.0;
    Long randomLong = null;
    MarkingType[] marking = null;
 
    @Override
    public void initializeLearner(Table table, HashMap<String, String> parameters) {
 
        // underlying algorithm
        if (!parameters.containsKey("learner")) {
                throw new RuntimeException("BaggingLearner's parameter learner is missing...");
        }
       
        underlyingAlgorithm = LearnerFactory.create(parameters.get("learner"));
        if (underlyingAlgorithm == null) {
                throw new RuntimeException("BaggingLearner's learner algorithm (" + parameters.get("learner") + ") is not supported...");
        }
       
        underlyingAlgorithm.initializeLearner(table, parameters);
        if (!underlyingAlgorithm.isInitialized()) {
                throw new RuntimeException("Problem while initializing underlying algorithm...");
        }
       
        // bag size
        if (!parameters.containsKey("bagsize")) {
                throw new RuntimeException("BaggingLearner must have bagsize parameter...");
        }
        try {
            this.bagSize = Double.parseDouble(parameters.get("bagsize"));
        } catch (NumberFormatException e) {
                throw new RuntimeException("BaggingLearner's bagsize must be double...");
        }       
        
        // number of bags
        if (!parameters.containsKey("bagnumber")) {
                throw new RuntimeException("BaggingLearner must have bagnumber parameter...");
        }
        try {
            this.numberOfBags = Integer.parseInt(parameters.get("bagnumber"));
        } catch (NumberFormatException e) {
                throw new RuntimeException("BaggingLearner's bagnumber must be int...");
        }
       
        // random
        if (!parameters.containsKey("random")) {
                throw new RuntimeException("BaggingLearner must have random parameter...");
        }
        try {
            this.randomLong = Long.parseLong(parameters.get("random"));
        } catch (NumberFormatException e) {
                throw new RuntimeException("BaggingLearner's random must be long...");
        }
       
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getColumnType().equals("marking") && column.getRole().equals("marking")) {
                marking = ((MarkingColumn)column).getData();
            }
        }       
        
        initialized = true;
    }
   
    @Override
    protected Model learn(String modelName, Table table) {
       
        Model[] models = new Model[numberOfBags];
       
        Random random = new Random(randomLong);
       
        for (int b=0;b<numberOfBags;b++) {
            // make a bag
            for (int i=0;i<marking.length;i++) {
                if (marking[i] == MarkingType.Train && random.nextDouble() > bagSize) {
                    marking[i] = MarkingType.Skip;
                }
            }
           
            // run the underlying algorithm
            models[b] = underlyingAlgorithm.learnModel("bag" + b, table);
           
            // set back the marking
            for (int i=0;i<marking.length;i++) {
                if (marking[i] == MarkingType.Skip) {
                    marking[i] = MarkingType.Train;
                }
            }
        }
       
        return new BaggingModel(modelName, models);
    }
}

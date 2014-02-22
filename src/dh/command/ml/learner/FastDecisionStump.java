package dh.command.ml.learner;
//package hu.dh.command.ml;
//
//import java.util.HashMap;
//import java.util.Random;
//
//import hu.dh.logging.Logger;
//import hu.dh.repository.DataTable;
//import hu.dh.repository.DataTableRepository;
//import hu.dh.service.command.AbstractCommand;
//
//public class FastDecisionStump implements AbstractCommand {
//
//    @Override
//    public String[] getParameters() {
//        return null;
//    }
//
//    @Override
//    public String getCommand() {
//        return "decision_stump";
//    }
//
//    @Override
//    public void main(HashMap<String, String> parameters, Logger logger, DataTableRepository repository) throws Throwable {
//       
//        String thresholdString = "";    
//        if (parameters.get("threshold") == null) {
//            logger.log("Missing parameter threshold...");
//            return;
//        }
//        thresholdString = parameters.get("threshold");
//        double threshold = -1.0;
//        try {
//            threshold = Double.parseDouble(thresholdString);
//        } catch (NumberFormatException e)  {
//            logger.log("Parameter threshold must be a double...");
//            return;
//        }
//
//        String tableName = "";     
//        if (parameters.get("tablename") == null) {
//            logger.log("Missing parameter tablename...");
//            return;
//        }
//        tableName = parameters.get("tablename");
//       
//        String modelName = "";     
//        if (parameters.get("modelname") == null) {
//            logger.log("Missing parameter modelname...");
//            return;
//        }
//        modelName = parameters.get("modelname");
//       
//        if (repository.getTables().get(tableName) == null) {
//            logger.log("There is no table " + tableName + " in the repository...");
//            return;        
//        }
//       
//        DataTable table = repository.getTables().get(tableName);
//       
//        hu.dh.algorithms.classification.decisionstumps.FastDecisionStump stump = new hu.dh.algorithms.classification.decisionstumps.FastDecisionStump(table, logger, threshold);
//
//        if (stump.checkInput()) {
//            repository.getModels().put(modelName, stump.train(modelName));
//            logger.log("DecisionStump model " + modelName + " is saved to repository...");
//        }
//       
//    }
//
//}

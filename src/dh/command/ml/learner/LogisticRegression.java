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
//public class LogisticRegression implements AbstractCommand {
//
//            @Override
//            public String[] getParameters() {
//                            return null;
//            }
//
//            @Override
//            public String getCommand() {
//                            return "logistic_regression";
//            }
//
//            @Override
//            public void main(HashMap<String, String> parameters, Logger logger, DataTableRepository repository) throws Throwable {
//               
//        String trainOrder = "";     
//        if (parameters.get("trainorder") == null) {
//            logger.log("Missing parameter trainorder...");
//            return;
//        }
//        trainOrder = parameters.get("trainorder");
//       
//        String interceptString = "";      
//        if (parameters.get("intercept") == null) {
//            logger.log("Missing parameter intercept...");
//            return;
//        }
//        interceptString = parameters.get("intercept");
//       
//        boolean intercept = false;
//        if (interceptString.equals("true")) {
//            intercept = true;
//        } else if (interceptString.equals("false")) {
//            intercept = false;
//        } else {
//            logger.log("Intercept must be: true/false...");
//            return;
//        }
//
//                            String tableName = "";                  
//                            if (parameters.get("tablename") == null) {
//                                            logger.log("Missing parameter tablename...");
//                                            return;
//                            }
//                            tableName = parameters.get("tablename");
//                           
//                            String modelName = "";                               
//                            if (parameters.get("modelname") == null) {
//                                            logger.log("Missing parameter modelname...");
//                                            return;
//                            }
//                            modelName = parameters.get("modelname");
//                           
//                            if (repository.getTables().get(tableName) == null) {
//                                            logger.log("There is no table " + tableName + " in the repository...");
//                                            return;                                 
//                            }
//                           
//                            DataTable table = repository.getTables().get(tableName);
//
//                            String iterationString = "";                           
//                            if (parameters.get("iteration") == null) {
//                                            logger.log("Missing parameter iteration...");
//                                            return;
//                            }
//                            iterationString = parameters.get("iteration");
//                            int iteration = -1;
//                            try {
//                                            iteration = Integer.parseInt(iterationString);
//                            } catch (NumberFormatException e)  {
//                                            logger.log("Parameter iteration must be a integer...");
//                                            return;
//                            }
//                           
//                            String learningString = "";                            
//                            if (parameters.get("learningrate") == null) {
//                                            logger.log("Missing parameter learningrate...");
//                                            return;
//                            }
//                            learningString = parameters.get("learningrate");
//                            double learningRate = -1.0;
//                            try {
//                                            learningRate = Double.parseDouble(learningString);
//                            } catch (NumberFormatException e)  {
//                                            logger.log("Parameter learningrate must be a double...");
//                                            return;
//                            }
//                           
//                            hu.dh.algorithms.classification.logisticregression.LogisticRegressionLearner regression = new hu.dh.algorithms.classification.logisticregression.LogisticRegressionLearner(table, logger, learningRate, iteration, new Random(42), intercept, trainOrder);
//                            if (regression.checkInput()) {
//                                            repository.getModels().put(modelName, regression.train(modelName));
//                                            logger.log("Regression model " + modelName + " is saved to repository...");
//                            }
//                           
//            }
//
//}

//package dh.command.ml.learner;
//
//import java.util.HashMap;
//
//import hu.dh.logging.Logger;
//import hu.dh.repository.DataTable;
//import hu.dh.repository.DataTableRepository;
//import hu.dh.service.command.AbstractCommand;
//
//public class BayesLearner implements AbstractCommand {
//
//    @Override
//    public String[] getParameters() {
//        return null;
//    }
//
//    @Override
//    public String getCommand() {
//        return "bayes_learner";
//    }
//
//    @Override
//    public void main(HashMap<String, String> parameters, Logger logger, DataTableRepository repository) throws Throwable {
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
//        DataTable table = repository.getTables().get(tableName);
//       
//        hu.dh.algorithms.classification.bayes.BayesLearner bayes = new hu.dh.algorithms.classification.bayes.BayesLearner(table, logger);
//       
//        if (bayes.checkInput()) {
//            repository.getModels().put(modelName, bayes.train(modelName));
//            logger.log("Bayes learner creates a model " + modelName + " is saved to repository...");
//        }
//       
//    }
//
//}

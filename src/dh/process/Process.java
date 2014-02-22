package dh.process;
 
public class Process extends CommandLine {
               
//            private static Logger logger = LoggerFactory.getLogger(DataHammerProcess.class);
//   
//    public static final String EVALUATION_METHODS   = "evaluation_methods";
//    public static final String XVALIDATION_LIMIT    = "xvalidation_limit";
//    public static final String XVALIDATION_CYCLE    = "xvalidation_cycle";
//    public static final String PARAMETER_SELECTION  = "parameter_selection";
//    public static final String LEARNING_ALGORITHM   = "learning_algorithm";
//       
//    protected final String testTable = "test";
//    protected final String trainTable = "train";
//    protected final String modelName = "m1";
//   
//    protected final HashMap<String, String> defaultParameter = new HashMap<String, String>();
//   
//    public static enum DataType {
//        Test, Train
//    }
//   
//    protected void loadData(DataType dataType) throws Throwable {
//        //
//    }
//    protected void preprocessData(DataType dataType, HashMap<String, String> parameter) throws Throwable {
//        //
//    }
//    protected void postprocessData() throws Throwable {
//        //
//    }
//    protected void learn(HashMap<String, String> parameters) throws Throwable {
//        //
//    }
//   
//    protected ParameterGenerator getParameterGeneartor() {
//        return new SimpleParameterGenerator(defaultParameter);
//    }
//   
//    private EvaluationResult[] evaluate(HashMap<String, String> parameters) {
//        Table table = repository.getTables().get(trainTable);
//        Model model = repository.getModels().get(modelName);
//        String method = parameters.get(EVALUATION_METHODS);
//        EvaluationResult[] result = null;
//        if (method.startsWith("classification")) {
//            String[] methods = method.substring(0, method.length() - 1).substring(method.indexOf("[") + 1).split("\\,");
//            model.apply(table);
//            result = new EvaluationResult[methods.length];
//            for (int i=0;i<methods.length;i++) {
//                String m = methods[i];
//                AbstractEvaluator eval = ClassificationEvaluationFactory.createEvaluator(m);
//                if (eval == null) {
//                    logger.warn("Classification evaluator: " + m + " is not supported...");
//                    continue;
//                }
//                EvaluationResult res = eval.evaluate(table, MarkingType.Test);
//                result[i] = res;
//            }
//        }
//        return result;
//    }
//   
//    public double runXValidation(HashMap<String, String> parameters) throws Throwable {
//       
//        int validationLimit = -1;
//        try {
//            validationLimit = Integer.parseInt(parameters.get(XVALIDATION_LIMIT));
//        } catch (NumberFormatException e) {
//            throw new Throwable("XValidation limit parameter is not an integer...");
//        }
//       
//        int crossValidationCycle = -1;
//        try {
//            crossValidationCycle = Integer.parseInt(parameters.get(XVALIDATION_CYCLE));
//        } catch (NumberFormatException e) {
//            throw new Throwable("XValidation cycle parameter is not an integer...");
//        }
//       
//        // results
//        EvaluationResult[][] results = new EvaluationResult[validationLimit][];
//       
//        for (int xValidation=0;xValidation<validationLimit;xValidation++) {
//           
//            xValidationMarking(trainTable, "" + crossValidationCycle, "" + xValidation, "true", "42");
//           
//            learn(parameters);
//           
//            results[xValidation] = evaluate(parameters);
//           
//            logger.info("" + xValidation + ". validation...");
//            for (int i=0;i<results[xValidation].length;i++) {
//                logger.info(results[xValidation][i].toString());
//            }
//        }
//       
//        silentLogger.log("Calculate average performance...");
//        double finalRes = 0.0;
//        for (int i=0;i<results[0].length;i++) {
//            double res = 0.0;
//            for (int j=0;j<results.length;j++) {
//                res += results[j][i].getValue();
//            }
//            res /= (double)results.length;
//            if (i == 0) {
//                finalRes = res;
//            }
//            silentLogger.log("Average " + results[0][i].getEvaluationMethodName() + ": " + res);
//        }
//       
//        return finalRes;
//    }
//   
//    protected static enum ParameterSelectionMode {
//        FindMinimum, FindMaximum;
//    }
//   
//    protected ParameterSelectionMode getParameterSelectionMode() {
//        return null;
//    }
//   
//    public void run() throws Throwable {
//       
//        // load the train data
//        loadData(DataType.Train);
//       
//        ParameterGenerator parameterGenerator = getParameterGeneartor();
//       
//        if (parameterGenerator == null) {
//            throw new Throwable("Missing ParameterGenerator for the process...");
//        }
//       
//        double bestPerf = 0.0;
//        if (getParameterSelectionMode() == ParameterSelectionMode.FindMaximum) {
//            bestPerf = Double.NEGATIVE_INFINITY;
//        } else {
//            bestPerf = Double.POSITIVE_INFINITY;
//        }
//        Parameter bestParameter = null;
//
//        while (parameterGenerator.hasNext()) {
//            Parameter p = parameterGenerator.next();
//            // preprocess the train data
//            preprocessData(DataType.Train, p.getParameter());       
//           
//            System.out.println(p.getParameter().toString());
//           
//            double performance = runXValidation(p.getParameter());
//           
//            System.out.println(performance);
//           
//            if (getParameterSelectionMode() == ParameterSelectionMode.FindMaximum) {
//                if (bestPerf < performance) {
//                    bestParameter = p;
//                    bestPerf = performance;
//                }
//            } else {
//                if (bestPerf > performance) {
//                    bestParameter = p;
//                    bestPerf = performance;
//                }
//            }
//        }
//       
//        logger.log("Best parameter so far:");
//        logger.log(bestParameter.getParameter().toString());
//        logger.log("" + bestPerf);
//       
//    }
}

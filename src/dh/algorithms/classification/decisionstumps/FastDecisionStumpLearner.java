package dh.algorithms.classification.decisionstumps;
 
import java.util.HashMap;
 
import dh.algorithms.AbstractLearner;
import dh.algorithms.utils.splitmaking.AbstractSplitCalculation;
import dh.algorithms.utils.splitmaking.EntropySplit;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;
import dh.repository.Model;
 
 
public class FastDecisionStumpLearner extends AbstractLearner {
   
    BooleanDataColumn target = null;
    double threshold;
    DoubleDataColumn[] columns = null;
    MarkingType[] marking = null;
    double[] weight = null;
   
    AbstractSplitCalculation split = null;
 
    @Override
    public void initializeLearner(Table table, HashMap<String, String> parameters) {
       
        String thresholdString = parameters.get("threshold");
        if (thresholdString == null) {
            throw new RuntimeException("FastDecisionStump must have threshold parameter...");
        }
        try {
            this.threshold = Double.parseDouble(thresholdString);
        } catch (NumberFormatException e) {
                throw new RuntimeException("FastDecisionStump's threshold has to be double...");
        }
       
        // search for the columns and check them
        int dataColumnCounter = 0;
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getColumnType().equals("double") && column.getRole().equals("")) {
                dataColumnCounter++;
            }
            if (column.getColumnType().equals("boolean") && column.getRole().equals("target")) {
                target = (BooleanDataColumn) column;
            }
            if (column.getColumnType().equals("marking") && column.getRole().equals("marking")) {
                marking = ((MarkingColumn)column).getData();
            }
            if (column.getColumnType().equals("double") && column.getRole().equals("weight")) {
                weight = ((DoubleDataColumn)column).getData();
            }
        }
       
        if (dataColumnCounter == 0) {
                throw new RuntimeException("There is no double column without role...");
        }
       
        if (target == null) {
                throw new RuntimeException("There is no target boolean column...");
        }
       
        columns = new DoubleDataColumn[dataColumnCounter];
        dataColumnCounter = 0;
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getColumnType().equals("double") && column.getRole().equals("")) {
                columns[dataColumnCounter++] = (DoubleDataColumn) column;
            }
        }
       
        split = new EntropySplit();
        
        initialized = true;
    }
   
    @Override
    protected Model learn(String modelName, Table table) {
       
        double[][] x = new double[columns.length][];
        for (int i=0;i<columns.length;i++) {
            x[i] = columns[i].getData();
        }
       
        boolean[] targetData = target.getData();
       
        double[] result = new double[columns.length];
        boolean[] leftLabel = new boolean[columns.length];
        boolean[] rightLabel = new boolean[columns.length];
        double[] leftConfidence = new double[columns.length];
        double[] rightConfidence = new double[columns.length];
       
        for (int i=0;i<result.length;i++) {
            double leftPositive = 0.0;
            double rightPositive = 0.0;
            double leftNegative = 0.0;
            double rightNegative = 0.0;
            double leftSize = 0.0;
            double rightSize = 0.0;
           
            for (int j=0;j<table.getSize();j++) {
                if (marking != null && marking[j] != MarkingType.Train) {
                                continue;
                }
                if (x[i][j] < threshold) {
                                if (weight != null) {
                                                leftSize += weight[i];
                                } else {
                                                leftSize += 1.0;
                                }
                    if (targetData[j]) {
                                if (weight != null) {
                                                leftPositive += weight[i];
                                } else {
                                                leftPositive += 1.0;
                                }
                    } else {
                                if (weight != null) {
                                                leftNegative += weight[i];
                                } else {
                                                leftNegative += 1.0;
                                }
                    }
                } else {
                                if (weight != null) {
                                                rightSize += weight[i];
                                } else {
                                                rightSize += 1.0;
                                }
                    if (targetData[j]) {
                                if (weight != null) {
                                                rightPositive += weight[i];
                                } else {
                                                rightPositive += 1.0;
                                }
                    } else {
                                if (weight != null) {
                                                rightNegative += weight[i];
                                } else {
                                                rightNegative += 1.0;
                                }
                    }
                }
            }
            if (leftPositive > leftNegative) {
                leftLabel[i] = true;
                leftConfidence[i] = leftPositive / (leftSize);
            } else {
                leftLabel[i] = false;
                leftConfidence[i] = leftNegative / (leftSize);
            }
            if (rightPositive > rightNegative) {
                rightLabel[i] = true;
                rightConfidence[i] = rightPositive / (rightSize);
            } else {
                rightLabel[i] = false;
                rightConfidence[i] = rightNegative / (rightSize);
            }
           
            result[i] = split.calculateSplit(leftPositive, leftNegative, leftSize, rightPositive, rightNegative, rightSize);
        }
       
        // search the largest split value
        double maxSplit = Double.MIN_VALUE;
        int maxIndex = 0;
        for (int i=0;i<result.length;i++) {
            if (result[i] > maxSplit) {
                maxIndex = i;
                maxSplit = result[i];
            }
        }
       
        return new DecisionStumpModel(modelName, columns[maxIndex].getName(), threshold, leftLabel[maxIndex], rightLabel[maxIndex], leftConfidence[maxIndex], rightConfidence[maxIndex]);
    }
}

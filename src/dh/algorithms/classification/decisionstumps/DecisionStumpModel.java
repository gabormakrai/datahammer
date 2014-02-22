package dh.algorithms.classification.decisionstumps;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.algorithms.classification.ClassificationModel;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;
 
public class DecisionStumpModel extends ClassificationModel {
               
                private static final Logger logger = LoggerFactory.getLogger(DecisionStumpModel.class);
               
                private static final long serialVersionUID = 1L;
               
                String columnName;
    double threshold;
    boolean leftLabel;
    boolean rightLabel;
    double leftConfidence;
    double rightConfidence;
   
    public DecisionStumpModel(String modelName, String columnName, double threshold, boolean leftLabel, boolean rightLabel, double leftConfidence, double rightConfidence) {
        super(modelName);
        this.columnName = columnName;
        this.threshold = threshold;
        this.leftLabel = leftLabel;
        this.rightLabel = rightLabel;
        this.leftConfidence = leftConfidence;
        this.rightConfidence = rightConfidence;
    }
   
    public String getColumnName() {
                                return columnName;
                }
   
    public boolean getLeftLabel() {
        return leftLabel;
    }
   
    public boolean getRightLabel() {
        return rightLabel;
    }
   
    @Override
    public void apply(Table table) {
       
        double[] numericP = getNumericPredictionData(table);
        boolean[] p = getPredictionData(table);
       
        double[] x = ((DoubleDataColumn)table.getColumns().get(columnName)).getData();
       
        for (int i=0;i<table.getSize();i++) {
            if (threshold < x[i]) {
                p[i] = rightLabel;
                if (numericP != null) {
                    if (rightLabel) {
                        numericP[i] = rightConfidence;
                    } else {
                        numericP[i] = 1.0 - rightConfidence;
                    }
                }
            } else {
                p[i] = leftLabel;
                if (numericP != null) {
                    if (leftLabel) {
                        numericP[i] = leftConfidence;
                    } else {
                        numericP[i] = 1.0 - leftConfidence;
                    }
                }
               
            }
        }
       
        logger.info("Decision stump applied on table " + table.getName());
    }
}

package dh.algorithms.evaluation.classification;
 
import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
 
public class AdaBoostingError extends AbstractClassificationEvaluator {
    public static class AdaBoostingErrorResult extends EvaluationResult {
        double error = 0.0;
        public AdaBoostingErrorResult(double error) {
            this.error = error;
        }
        @Override
        public String toString() {
            return "AdaBoosting error: " + error;
        }
                                @Override
                                public double getValue() {
                                                return error;
                                }
        @Override
        public String getEvaluationMethodName() {
            return "AdaBoostingError";
        }
    }
   
    @Override
    protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
                if (weightColumn == null) {
                                throw new RuntimeException("There must be a weight column...");
                }
                return super.check(targetColumn, predictionColumn, numericPredicition, weightColumn);
    }
   
    @Override
    protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {
                double error = 0.0;
       
                                BooleanDataColumn target = (BooleanDataColumn) targetColumn;
                                BooleanDataColumn prediction = (BooleanDataColumn) predictionColumn;
                               
                                MarkingType[] marking = null;
                                if (markingColumn != null) {
                                                marking = ((MarkingColumn)markingColumn).getData();
                                }
                               
                                boolean[] targetData = target.getData();
                                boolean[] predictionData = prediction.getData();
                               
                                double[] weight = ((DoubleDataColumn)weightColumn).getData();
                               
                                for (int i=0;i<target.getSize();i++) {
                                                if (marking == null || marking[i] == markingType) {
                                                    if (targetData[i] != predictionData[i]) {
                                                        error += 1.0 * weight[i];
                }
                                                }
                                }
                                return new AdaBoostingErrorResult(error);
    }
}

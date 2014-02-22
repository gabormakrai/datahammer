package dh.algorithms.evaluation.classification;
 
import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
 
public class WeightedTruePositive extends AbstractClassificationEvaluator {
   
    public static class WeightedTruePositiveResult extends EvaluationResult {
        double tp = 0;
        double count = 0;
        public WeightedTruePositiveResult(double tp, double count) {
            this.tp = tp;
            this.count = count;
        }
        @Override
        public String toString() {
            return "Classification WTP: " + ((double)tp/(double)count) + "(" + tp + "/" + count + ")";
        }
                                @Override
                                public double getValue() {
                                                return (double)tp/(double)count;
                                }
        @Override
        public String getEvaluationMethodName() {
            return "Classification WeightedTruePositive";
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
                double hit = 0;
                                double count = 0;
                               
                                BooleanDataColumn target = (BooleanDataColumn) targetColumn;
                                BooleanDataColumn prediction = (BooleanDataColumn) predictionColumn;
                                DoubleDataColumn weight = (DoubleDataColumn) weightColumn;
                               
                                MarkingType[] marking = null;
                                if (markingColumn != null) {
                                                marking = ((MarkingColumn)markingColumn).getData();
                                }
                               
                                boolean[] targetData = target.getData();
                                boolean[] predictionData = prediction.getData();
                                double[] weightData = weight.getData();
                               
                                for (int i=0;i<target.getSize();i++) {
                                                if (marking == null || marking[i] == markingType) {
                                                                if (targetData[i]) {
                                                                                count += weightData[i];
                                                                    if (targetData[i] == predictionData[i]) {
                                                                        hit += weightData[i];
                                                                    }
                                                                }
                                                }
                                }
                                return new WeightedTruePositiveResult(hit,count);
    }
}

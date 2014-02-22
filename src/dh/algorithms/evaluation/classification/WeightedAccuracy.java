package dh.algorithms.evaluation.classification;
 
import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
 
public class WeightedAccuracy extends AbstractClassificationEvaluator {
    public static class WeightedAccuracyResult extends EvaluationResult {
        double hitWeight = 0.0;
        double countWeight = 0.0;
        public WeightedAccuracyResult(double hitWeight, double countWeight) {
            this.countWeight = countWeight;
            this.hitWeight = hitWeight;
        }
        @Override
        public String toString() {
            return "Classification weighted accuracy: " + ((double)hitWeight/(double)countWeight) + "(" + hitWeight + "/" + countWeight + ")";
        }
                                @Override
                                public double getValue() {
                                                return (double)hitWeight/(double)countWeight;
                                }
        @Override
        public String getEvaluationMethodName() {
            return "Classification WeightedAccuracy";
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
                               
                                MarkingType[] marking = null;
                                if (markingColumn != null) {
                                                marking = ((MarkingColumn)markingColumn).getData();
                                }
                               
                                boolean[] targetData = target.getData();
                                boolean[] predictionData = prediction.getData();
                               
                                double[] weight = ((DoubleDataColumn)weightColumn).getData();
                               
                                for (int i=0;i<target.getSize();i++) {
                                                if (marking == null || marking[i] == markingType) {
                                                                count += weight[i];
                                                    if (targetData[i] == predictionData[i]) {
                                                        hit += weight[i];
                                                    }
                                                }
                                }
                                return new WeightedAccuracyResult(hit,count);
    }
}

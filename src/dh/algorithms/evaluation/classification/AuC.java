package dh.algorithms.evaluation.classification;
 
import java.util.Arrays;
 
import dh.algorithms.evaluation.AbstractClassificationEvaluator;
import dh.algorithms.evaluation.EvaluationResult;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
 
public class AuC extends AbstractClassificationEvaluator {
    public static class AuCResult extends EvaluationResult {
        double auc = 0.0;
        public AuCResult(double auc) {
            this.auc = auc;
        }
        @Override
        public String toString() {
            return "Classification AuC: " + auc;
        }
                                @Override
                                public double getValue() {
                                                return auc;
                                }
        @Override
        public String getEvaluationMethodName() {
            return "Classification AuC";
        }
    }
   
    @Override
    protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
        if (numericPredicition == null) {
                throw new RuntimeException("There must be a numeric prediction column...");
        }
                return super.check(targetColumn, predictionColumn, numericPredicition, weightColumn);
    }
   
    public static class DataObject implements Comparable<DataObject> {
        double pred;
        boolean target;
        public DataObject(double pred, boolean target) {
            this.pred = pred;
            this.target = target;
        }
        @Override
        public int compareTo(DataObject o) {
            if (o.pred < pred) {
                return -1;
            } else if (o.pred > pred) {
                return +1;
            } else {
                return 0;
            }
        }
    }
   
    DataObject[] cache = null;
   
    @Override
    protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {
                MarkingType[] marking = ((MarkingColumn)markingColumn).getData();
                                boolean[] targetData = ((BooleanDataColumn) targetColumn).getData();
                                double[] numericPredictionData = ((DoubleDataColumn)numericPredicition).getData();
                               
                                if (cache == null) {
                                    cache = new DataObject[marking.length];
                                    for (int i=0;i<cache.length;i++) {
                                        cache[i] = new DataObject(0.0, false);
                                    }
                                }
                               
                                // count the necessary size for the dataobject array
                                int size = 0;
                                for (int i=0;i<marking.length;i++) {
                                    if (marking[i] == markingType) {
                                        size++;
                                    }
                                }
                               
                                DataObject[] array = new DataObject[size];
                                for (int i=0;i<array.length;i++) {
                                    array[i] = cache[i];
                                }
                               
                                size = 0;
                                for (int i=0;i<marking.length;i++) {
                                    if (marking[i] == markingType) {
                                        array[size].pred = numericPredictionData[i];
                                        array[size].target = targetData[i];
                                        size++;
                                    }
                                }
                               
                                // sort array
                                Arrays.sort(array);
                               
                                int P = 0;
                                int N = 0;
                                int FP = 0;
                                int TP = 0;
                                int FPprev = 0;
                                int TPprev = 0;
                                double predprev = Double.NEGATIVE_INFINITY;
                                double AuC = 0.0;
                               
                                for (int i=0;i<array.length;i++) {
                                    if (predprev != array[i].pred) {
                                        AuC += trapezoidArea(FP, FPprev, TP, TPprev);
                                        predprev = array[i].pred;
                                        FPprev = FP;
                                        TPprev = TP;
                                    }
                                    if (array[i].target) {
                                        TP++;
                                        P++;
                                    } else {
                                        FP++;
                                        N++;
                                    }
                                }
                               
                                if (N == 0 || P == 0) {
                                    return new AuCResult(1.0);  
                                }
                               
                                AuC += trapezoidArea(N, FPprev, N, TPprev);
                                AuC /= (P * N);
                               
                                return new AuCResult(AuC);
    }
   
    private double trapezoidArea(double x1, double x2, double y1, double y2) {
        return Math.abs(x1 - x2) * ((y1 + y2) / 2.0);
    }
}

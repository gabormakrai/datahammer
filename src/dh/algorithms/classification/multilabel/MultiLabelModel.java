package dh.algorithms.classification.multilabel;
 
import dh.algorithms.classification.ClassificationModel;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;
import dh.repository.Model;
 
public class MultiLabelModel extends ClassificationModel {
               
                public static enum LabelSelectionMethod { Best_Numeric_Prediction };
               
                private static final long serialVersionUID = 1L;
               
                String[] labels;
                Model[] models;
                LabelSelectionMethod labelSelectionMethod;
               
                public MultiLabelModel() {
                }
               
                public MultiLabelModel(String name, LabelSelectionMethod labelSelectionMethod, String[] labels, Model[] models) {
                                super(name);
                                this.labels = labels;
                                this.models = models;
                                this.labelSelectionMethod = labelSelectionMethod;
                }
               
    @Override
    public void apply(Table table) {
                int[] nominalPredictionData = getNominalPredictionData(table, labels);
               
                                double[] bestNumericPrediction = new double[table.getSize()];
                                double[] numericPredictionData = null;
                                String[] bestPrediction = new String[table.getSize()];
                               
                                for (int i = 0; i < models.length; ++i) {
                                                models[i].apply(table);
                                               
                                                if (i == 0) {
                                                                for (AbstractDataColumn column : table.getColumns().values()) {
                                                                                if (column.getRole().equals("numericprediction")) {
                                                                                                numericPredictionData = ((DoubleDataColumn)column).getData();
                                                                                                break;
                                                                                }
                                                                }
                                                                for (int j = 0; j < numericPredictionData.length; ++j) {
                                                                                bestNumericPrediction[j] = numericPredictionData[j];
                                                                                bestPrediction[j] = labels[i];
                                                                                nominalPredictionData[j] = i;
                                                                }
                                                } else {
                                                                for (int j = 0; j < numericPredictionData.length; ++j) {
                                                                                if (numericPredictionData[j] > bestNumericPrediction[j]) {
                                                                                                bestNumericPrediction[j] = numericPredictionData[j];
                                                                                                bestPrediction[j] = labels[i];
                                                                                                nominalPredictionData[j] = i;
                                                                                }
                                                                }
                                                }
                                }
                               
                               
                }
               
}

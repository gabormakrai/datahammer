package dh.algorithms.classification.kmeans;
 
import dh.algorithms.classification.ClassificationModel;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Table;
 
public class KMeansModel extends ClassificationModel {
 
                private static final long serialVersionUID = 1L;
               
                String[] columnNames;
               
                boolean[] targets;
                String[][] centerLabels;
               
                public KMeansModel(NominalDataColumn[] columns, int[][] centers, boolean[] targets) {
                                columnNames = new String[columns.length];
                                for (int i=0;i<columns.length;++i) {
                                                columnNames[i] = columns[i].getName();
                                }
                                centerLabels = new String[centers.length][];
                                for (int i=0;i<centerLabels.length;++i) {
                                                centerLabels[i] = new String[centers[i].length];
                                                for (int j=0;j<centers[i].length;++j) {
                                                                centerLabels[i][j] = columns[j].getReverseMapping().get(centers[i][j]);
                                                }
                                }
                                this.targets = new boolean[targets.length];
                                for (int i = 0; i < targets.length; ++i) {
                                                this.targets[i] = targets[i];
                                }
                }
 
                @Override
                public void apply(Table table) {
                               
                                boolean[] prediction = getPredictionData(table);
                                double[] numericPrediction = getNumericPredictionData(table);
                               
                                int[][] centers = new int[centerLabels.length][];
                               
                                int[][] data = new int[columnNames.length][];
                               
                                for (int i = 0; i < centers.length; ++i) {
                                                centers[i] = new int[columnNames.length];
                                }
                               
                                for (int i = 0; i < columnNames.length;++i) {
                                                NominalDataColumn column = table.getColumn(columnNames[i]);
                                                data[i] = column.getData();
                                                for (int j=0;j<centers.length;++j) {
                                                                centers[j][i] = column.getMapping().get(centerLabels[j][i]);
                                                }
                                }
                               
                               
                                int[] row = new int[columnNames.length];
                                for (int i=0;i<table.getSize();++i) {
                                               
                                                for (int j=0;j<data.length;++j) {
                                                                row[j] = data[j][i];
                                                }
                                                int maxSimilarity = Integer.MIN_VALUE;
                                                int group = -1;
                                                for (int j = 0; j < centers.length; ++j) {
                                                                int sim = calculateSimilarity(centers[j], row);
                                                                if (sim > maxSimilarity) {
                                                                                maxSimilarity = sim;
                                                                                group = j;
                                                                }
                                                }
                                               
                                                if (targets[group]) {
                                                                prediction[i] = true;
                                                                numericPrediction[i] = 1.0;
                                                } else {
                                                                prediction[i] = false;
                                                                numericPrediction[i] = 0.0;
                                                }
                                }
                }
               
                private int calculateSimilarity(int[] e1, int[] e2) {
                                int sim = 0;
                                for (int i = 0; i < e1.length; ++i) {
                                                if (e1[i] == e2[i]) {
                                                                ++sim;
                                                }
                                }
                                return sim;
                }
               
}

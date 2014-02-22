package dh.algorithms.classification.kmeans;
 
import java.util.HashMap;
import java.util.Random;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.algorithms.AbstractLearner;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.special.MarkingColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Model;
import dh.repository.Table;
 
public class KMeansLearner extends AbstractLearner {
               
                private static final Logger logger = LoggerFactory.getLogger(KMeansLearner.class);
               
    boolean[] target = null;
    NominalDataColumn[] columns = null;
    MarkingType[] marking = null;
    int[][] data = null;
                Random random = null;
                int iterations = 10;
    int k = 0;
 
    int[] labelCounter = null;
   
                @Override
                public void initializeLearner(Table table, HashMap<String, String> parameters) {
                               
                                if (!parameters.containsKey("k")) {
                                                throw new RuntimeException("There is no parameter k...");
                                }
                                try {
                                                k = Integer.parseInt(parameters.get("k"));
                                } catch (NumberFormatException e) {
                                                throw new RuntimeException("Parameter k is not an integer...");
                                }
                               
                                if (!parameters.containsKey("random")) {
                                                random = new Random();
                                } else {
                                                try {
                                                                random = new Random(Integer.parseInt(parameters.get("random")));
                                                } catch (NumberFormatException e) {
                                                                throw new RuntimeException("Parameter k is not an integer...");
                                                }                             
                                }
                               
        // search for the columns and check them
        int dataColumnCounter = 0;
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getColumnType().equals("nominal") && column.getRole().equals("")) {
                dataColumnCounter++;
            }
            if (column.getColumnType().equals("boolean") && column.getRole().equals("target")) {
                target = ((BooleanDataColumn) column).getData();
            }
            if (column.getColumnType().equals("marking") && column.getRole().equals("marking")) {
                marking = ((MarkingColumn)column).getData();
            }
        }
       
        if (dataColumnCounter == 0) {
                throw new RuntimeException("There is no nominal column without role...");
        }
       
        if (target == null) {
                throw new RuntimeException("There is no target boolean column...");
        }
       
        columns = new NominalDataColumn[dataColumnCounter];
        dataColumnCounter = 0;
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getColumnType().equals("nominal") && column.getRole().equals("")) {
                columns[dataColumnCounter++] = (NominalDataColumn) column;
            }
        }
       
        data = new int[columns.length][];
        for (int i = 0; i < columns.length; ++i) {
                data[i] = columns[i].getData();
        }
       
        int largestLabelCount = 0;
        for (NominalDataColumn column : columns) {
                if (column.getMapping().size() > largestLabelCount) {
                                largestLabelCount = column.getMapping().size();
                }
        }
        labelCounter = new int[largestLabelCount];
       
        initialized = true;                     
                }
               
                @Override
                protected Model learn(String modelName, Table table) {
                               
                                int [][] centers = findCenters(random);
                               
                                logger.trace("Initial centers: {}", (Object)centers);
                               
                                int[] groups = new int[table.getSize()];
                               
                                for (int iter = 0; iter < iterations; ++iter) {
                                               
                                                findGroups(groups, centers);
                                               
                                                updateCenters(groups, centers);
                                               
                                                logger.trace("{}. iterations' centers: {}", iter, (Object)centers);
                                }
                               
                                boolean[] targets = findTargetForCenters(groups);
                               
                                return new KMeansModel(columns, centers, targets);
                }
               
                private int[][] findCenters(Random random) {
                               
                                int[][] centers = new int[k][];
                               
                                for (int i=0;i<k;++i) {
                                               
                                                centers[i] = new int[columns.length];
                                               
                                                while (true) {
                                                                int index = random.nextInt(columns[0].getSize());
                                                                if (marking != null && marking[index] != MarkingType.Train) {
                                                                                continue;
                                                                }
                                                                for (int j = 0; j < columns.length; ++j) {
                                                                                centers[i][j] = data[j][index];
                                                                }
                                                                boolean sameElement = false;
                                                                for (int j = 0; j < i; ++j) {
                                                                                if (calculateSimilarity(centers[j], centers[i]) == columns.length) {
                                                                                                sameElement = true;
                                                                                }
                                                                }
                                                                if (!sameElement) {
                                                                                break;
                                                                }                                                             
                                                }
                                }
                               
                                return centers;
                }
               
                private void findGroups(int[] groups, int[][] centers) {
                               
                                int[] row = new int[columns.length];
                               
                                for (int i = 0; i < groups.length; ++i) {
                                                if (marking != null && marking[i] != MarkingType.Train) {
                                                                continue;
                                                }                                             
                                                for (int j = 0; j < row.length; ++j) {
                                                                row[j] = data[j][i];
                                                }
                                                int maxSimilarity = Integer.MIN_VALUE;
                                                int group = -1;
                                                for (int j = 0; j < k; ++j) {
                                                                int sim = calculateSimilarity(centers[j], row);
                                                                if (sim > maxSimilarity) {
                                                                                maxSimilarity = sim;
                                                                                group = j;
                                                                }
                                                }
                                                groups[i] = group;
                                }
                               
                }
               
                private void updateCenters(int[] groups, int[][] centers) {
                                for (int c = 0; c < k; ++c) {
                                                for (int i = 0; i < columns.length; ++i) {
                                                                for (int j = 0; j < labelCounter.length; ++j) {
                                                                                labelCounter[j] = 0;
                                                                }
                                                                for (int j = 0; j < groups.length; ++j) {
                                                                                if (marking != null && marking[j] != MarkingType.Train) {
                                                                                                continue;
                                                                                }
                                                                                if (groups[j] == c) {
                                                                                                ++labelCounter[data[i][j]];
                                                                                }
                                                                }
                                                                int maxLabel = Integer.MIN_VALUE;
                                                                for (int j = 0; j < labelCounter.length; ++j) {
                                                                                if (labelCounter[j] > maxLabel) {
                                                                                                centers[c][i] = j;
                                                                                                maxLabel = labelCounter[j];
                                                                                }
                                                                }
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
               
                private boolean[] findTargetForCenters(int[] groups) {
                                int[] p = new int[k];
                                for (int i = 0; i < k; ++i) {
                                                p[i] = 0;
                                }
                                for (int i = 0; i < groups.length; ++i) {
                                                if (target[i]) {
                                                                ++p[groups[i]];
                                                } else {
                                                                --p[groups[i]];
                                                }
                                }
                                boolean[] targets = new boolean[k];
                                for (int i = 0; i < k; ++i) {
                                                targets[i] = p[i] >= 0;
                                }
                                return targets;
                }
 
}

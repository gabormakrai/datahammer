package dh.algorithms.evaluation;
 
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.MarkingColumn.MarkingType;
import dh.repository.Table;
 
public class AbstractEvaluator {
   
    protected EvaluationResult evaluate(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn markingColumn, MarkingType markingType, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn, AbstractDataColumn nominalPrediction) {
        return null;
    }
   
    protected boolean check(AbstractDataColumn targetColumn, AbstractDataColumn predictionColumn, AbstractDataColumn numericPredicition, AbstractDataColumn weightColumn) {
                return false;
    }
    
    public EvaluationResult evaluate(Table table, MarkingType markingType) {
        AbstractDataColumn targetColumn = null;
        AbstractDataColumn predictionColumn = null;
        AbstractDataColumn markingColumn = null;
        AbstractDataColumn numericPredicition = null;
        AbstractDataColumn weightColumn = null;
        AbstractDataColumn nominalPredicion = null;
       
        for (AbstractDataColumn column : table.getColumns().values()) {
            if (column.getRole().equals("target")) {
                targetColumn = column;
            } else if (column.getRole().equals("prediction")) {
                predictionColumn = column;
            } else if (column.getRole().equals("marking")) {
                markingColumn = column;
            } else if (column.getRole().equals("numericprediction")) {
                numericPredicition = column;
            } else if (column.getRole().equals("weight")) {
                weightColumn = column;
            } else if (column.getRole().equals("nominalprediction")) {
                nominalPredicion = column;
            }
        }
        if (targetColumn == null) {
                throw new RuntimeException("There is no target column in table " + table.getName() + "...");
        } else if (predictionColumn == null) {
                throw new RuntimeException("There is no prediction column in table " + table.getName() + "...");
        } else if (markingColumn != null && !markingColumn.getColumnType().equals("marking")) {
                throw new RuntimeException("Marking column has to be makring column...");
        } else if (markingColumn != null && markingType == null) {
                throw new RuntimeException("Please specify the marking type...");
        } else if (weightColumn != null && !weightColumn.getColumnType().equals("double")) {
                throw new RuntimeException("Weight column has to be double data column...");
        } else if (!check(targetColumn, predictionColumn, numericPredicition, weightColumn)) {
                return null;
        } else {
            return evaluate(targetColumn, predictionColumn, markingColumn, markingType, numericPredicition, weightColumn, nominalPredicion);
        }
    }
}

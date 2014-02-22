package dh.command.converters;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;
 
public class ExplodeNominal extends AbstractCommand {
               
                private static final Logger logger = LoggerFactory.getLogger(ExplodeNominal.class);
               
                public ExplodeNominal(Repository repository) {
                                super(repository);
                }
 
    public void run(String tableName, String columnName, boolean addNullColumn) {
                       
        Table table = getTable(tableName);
       
        AbstractDataColumn abstractColumn = table.getColumn(columnName);
       
        if (!abstractColumn.getColumnType().equals("nominal")) {
                throw new RuntimeException("Column " + columnName + " is not nominal column (table:" + table.getName() + ")...");
        }
       
        NominalDataColumn column = (NominalDataColumn) abstractColumn;
       
        // calculate freq to filter out pointless columns
        int[] frequency = new int[column.getMapping().size()];
        int nullFrequency = 0;
        for (int i=0;i<frequency.length;i++) {
            frequency[i] = 0;
        }
       
        int[] data = column.getData();
        for (int i=0;i<data.length;i++) {
            if (data[i] != -1) {
                frequency[data[i]]++;
            } else {
                nullFrequency++;
            }
        }
       
        int newColumnCounter = 0;
        int[] indexArray = new int[frequency.length];
        for (int i=0;i<frequency.length;i++) {
            if (frequency[i] != 0) {
                indexArray[i] = newColumnCounter++;
            } else {
                indexArray[i] = -1;
            }
        }
       
        DoubleDataColumn[] newColumns = new DoubleDataColumn[newColumnCounter];
        for (int i=0;i<frequency.length;i++) {
            if (indexArray[i] != -1) {
                newColumns[indexArray[i]] = createNewColumn(columnName + "_" + column.getReverseMapping().get(i), data.length);
            }
        }
        DoubleDataColumn nullColumn = null;
        if (addNullColumn && nullFrequency != 0) {
            nullColumn = createNewColumn(columnName + "_NULL", data.length);
        }
       
        for (int i=0;i<data.length;i++) {
            int v = column.getData()[i];
            if (v == -1) {
                if (nullColumn != null) {
                    nullColumn.getData()[i] = 1.0;
                }
            } else if (indexArray[v] != -1) {
              newColumns[indexArray[v]].getData()[i] = 1.0; 
            }
        }
       
        for (DoubleDataColumn c : newColumns) {
            table.getColumns().put(c.getName(), c);
        }
        if (nullColumn != null) {
            table.getColumns().put(nullColumn.getName(), nullColumn);
        }
       
        logger.info("Explosion is done...");
    }
   
    private DoubleDataColumn createNewColumn(String name, int length) {
        DoubleDataColumn c = new DoubleDataColumn();
        c.setName(name);
        c.setSize(length);
        c.setRole("");
        double[] data = new double[length];
       boolean[] nullElements = new boolean[length];
        for (int i=0;i<length;i++) {
            data[i] = -1.0;
            nullElements[i] = false;
        }
        c.setData(data);
        c.setNullElements(nullElements);
        return c;
    }
}

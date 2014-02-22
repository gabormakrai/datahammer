package dh.command.converters;
 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.data.column.base.StringDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;
 
public class KeepLabels extends AbstractCommand {
               
                private static final Logger logger = LoggerFactory.getLogger(KeepLabels.class);
 
                public KeepLabels(Repository repository) {
                                super(repository);
                }
 
                public void run(String inputTableName, String labelsTableName, String inputColumnName, String labelsColumnName) {
 
                                Table inputTable = getTable(inputTableName);
                               
                                Table labelsTable = getTable(labelsTableName);
                               
                                NominalDataColumn inputColumn = inputTable.getColumn(inputColumnName);
                               
                                StringDataColumn labelColumn = labelsTable.getColumn(labelsColumnName);
       
        String[] labelsData = labelColumn.getData();
       
        int[] data = inputColumn.getData();
        HashMap<String, Integer> map = inputColumn.getMapping();
       
        boolean[] keepArray = new boolean[map.size()];
        for (int i=0;i<keepArray.length;i++) {
                keepArray[i] = false;
        }
       
        HashSet<String> keepLabels = new HashSet<String>();
        for (int i=0;i<labelsData.length;i++) {
                keepLabels.add(labelsData[i]);
        }
       
        for (Entry<String, Integer> entry : map.entrySet()) {
                if (keepLabels.contains(entry.getKey())) {
                                keepArray[entry.getValue()] = true;
                }
        }
       
        for (int i=0;i<data.length;i++) {
                if (!keepArray[data[i]]) {
                                data[i] = -1;
                }
        }
       
        logger.info("Table " + inputTableName + "'s " + inputColumnName + " column has lost some labels...");
                }
 
}

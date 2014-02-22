package dh.command.discovery;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
 
public class StatNominal extends AbstractCommand {
 
                private static final Logger logger = LoggerFactory.getLogger(StatNominal.class);
 
                public StatNominal(Repository repository) {
                                super(repository);
                }             
               
                public void run(String inputTableName, String columnName) {
                               
                                NominalDataColumn column = getTable(inputTableName).getColumn(columnName);
                               
                                logger.info("Column " + column.getName() + " statistics:");
                               
                                int[] frequency = new int[column.getMapping().size()];
                                int nullFrequency = 0;
                                for (int i=0;i<frequency.length;i++) {
                                                frequency[i] = 0;
                                }
                                for (int i=0;i<column.getSize();i++) {
                                                if (column.getData()[i] == -1) {
                                                                nullFrequency++;
                                                } else {
                                                                frequency[column.getData()[i]]++;
                                                }
                                }
                                for (int i=0;i<frequency.length;i++) {
                                                if (frequency[i] != 0) {
                                                                logger.info(column.getReverseMapping().get(i) + ": " + frequency[i]);
                                                }
                                }
                                logger.info("NULL: " + nullFrequency);
                }
 
}

package dh.command.transformation;
 
import java.util.Arrays;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
 
public class Sort extends AbstractCommand {
 
                private static final Logger logger = LoggerFactory.getLogger(Sort.class);
 
                public Sort(Repository repository) {
                                super(repository);
                }
 
                public void run(String inputTableName, String columnName, boolean ascending) {
                               
                                final int orderValue;
                                if (ascending) {
                                                orderValue = 1;
                                } else {
                                                orderValue = -1;
                                }
                               
                                final AbstractDataColumn column = repository.getTables().get(inputTableName).getColumns().get(columnName);
                               
                                DataEntry[] entries = new DataEntry[column.getSize()];
                                for (int i=0;i<entries.length;i++) {
                                                entries[i] = new DataEntry(i);
                                }
                               
                                Arrays.sort(entries, new Comparator<DataEntry>() {
                                                @Override
                                                public int compare(DataEntry o1, DataEntry o2) {
                                                                return orderValue * column.compare(o1.index, o2.index);
                                                }                                             
                                });
                               
                                int order[] = new int[entries.length];
                                for (int i=0;i<order.length;i++) {
                                                order[i] = entries[i].index;
                                                entries[i] = null;
                                }
                               
                                for (AbstractDataColumn c : repository.getTables().get(inputTableName).getColumns().values()) {
                                                c.reorder(order);
                                }
                               
                                logger.info("Ordering completed...");
                }
               
                public class DataEntry {
                                public int index;
                                public int getIndex() {
                                                return index;
                                }
                                public void setIndex(int index) {
                                                this.index = index;
                                }
                                public DataEntry(int index) {
                                                this.index = index;
                                }
                }
 
}

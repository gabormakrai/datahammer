package dh.command.transformation;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;
 
public class RemoveTable extends AbstractCommand {
 
                private static final Logger logger = LoggerFactory.getLogger(RemoveTable.class);
 
                public RemoveTable(Repository repository) {
                                super(repository);
                }
 
                public void run(String tableName) {
                               
                                Table table = getTable(tableName);
                               
                                repository.getTables().remove(tableName);
                               
                                table.dispose();
                               
                                System.gc();
                               
                                logger.info("Table " + tableName + " is removed from repository...");
                               
                }
 
}

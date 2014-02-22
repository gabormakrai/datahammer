package dh.command.discovery;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;
 
public class ShowTables extends AbstractCommand {
               
                private static final Logger logger = LoggerFactory.getLogger(ShowTables.class);
 
                public ShowTables(Repository repository) {
                                super(repository);
                }
 
                public void run() {
                               
                                if (repository.getTables().size() == 0) {
                                                logger.info("There is no table in the repository...");
                                } else {
                                                logger.info("There is " + repository.getTables().size() + " table(s) in the repository...");
                                                for (Table table : repository.getTables().values()) {
                                                                logger.info(table.getName());
                                                }
                                }                             
                               
                }
}

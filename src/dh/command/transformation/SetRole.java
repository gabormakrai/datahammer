package dh.command.transformation;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.repository.Repository;
 
public class SetRole extends AbstractCommand {
 
                private static final Logger logger = LoggerFactory.getLogger(SetRole.class);
 
                public SetRole(Repository repository) {
                                super(repository);
                }
 
                public void run(String inputTableName, String columnName, String role) {
                               
                                getTable(inputTableName).getColumn(columnName).setRole(role);
                                                               
                                logger.info("Role has been modified...");
                }
 
}

package dh.command.ml;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;
import dh.repository.Model;
 
public class ModelApply extends AbstractCommand {
               
                private static final Logger logger = LoggerFactory.getLogger(ModelApply.class);
 
                public ModelApply(Repository repository) {
                                super(repository);
                }
 
                public void run(String tableName, String modelName) {
                               
                                Table table = getTable(tableName);
                                                               
                                Model model = getModel(modelName);
                               
                                model.apply(table);
                               
                                logger.info("Model application is completed...");
                }
 
}

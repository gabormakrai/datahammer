package dh.algorithms;
 
import java.util.HashMap;
 
import dh.repository.Table;
import dh.repository.Model;
 
public abstract class AbstractLearner {
   
    protected boolean initialized = false;
   
    public boolean isInitialized() {
        return initialized;
    }
   
    protected abstract Model learn(String modelName, Table table);
   
    public abstract void initializeLearner(Table table, HashMap<String, String> parameters);
   
    public Model learnModel(String modelName, Table table) {
        if (!initialized) {
                throw new RuntimeException("Learner not initialized or not initialized correctly...");
        } else {
            return learn(modelName, table);
        }
    }
}

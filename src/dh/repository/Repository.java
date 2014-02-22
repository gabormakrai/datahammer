package dh.repository;
 
import java.io.Serializable;
import java.util.HashMap;
 
public class Repository implements Serializable {
 
                private static final long serialVersionUID = 1L;
               
                private final HashMap<String, Table> tables = new HashMap<String, Table>();
               
                public HashMap<String, Table> getTables() {
                                return tables;
                }
                               
                public Table getTable(String table) {
                                return tables.get(table);
                }
               
                private final HashMap<String, Model> models = new HashMap<String, Model>();
               
                public HashMap<String, Model> getModels() {
                                return models;
                }
               
                public Model getModel(String model) {
                                return models.get(model);
                }
}

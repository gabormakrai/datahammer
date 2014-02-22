package dh.algorithms.classification.bayes;
 
import java.util.HashMap;
import java.util.LinkedList;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.algorithms.classification.ClassificationModel;
import dh.data.column.AbstractDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Table;
 
public class BayesModel extends ClassificationModel {
               
                private static final long serialVersionUID = 1L;
 
                private static final Logger logger = LoggerFactory.getLogger(BayesModel.class);
 
    HashMap<String, HashMap<String, Probability>> map;
    double probT;
    double probF;
    double smallestProb;
   
    public BayesModel(String name, HashMap<String, HashMap<String, Probability>> map, double probT, double probF, double smallestProb) {
        super(name);
        this.map = map;
        this.probF = probF;
        this.probT = probT;
        this.smallestProb = smallestProb;
    }
   
    @Override
    public void apply(Table table) {
        double[] numericP = getNumericPredictionData(table);
        boolean[] p = getPredictionData(table);
       
        LinkedList<NominalDataColumn> columns = new LinkedList<NominalDataColumn>();
        for (AbstractDataColumn c : table.getColumns().values()) {
            if (c.getRole().equals("") && c.getColumnType().equals("nominal")) {
               columns.add((NominalDataColumn)c);
            }
        }
       
        for (int i=0;i<table.getSize();i++) {
            double trueP = probT;
            double falseP = probF;
           
            for (NominalDataColumn column : columns) {
                HashMap<String, Probability> m = map.get(column.getName());
                if (m != null) {
                    Probability prob = m.get(column.getElement(i));
                    if (prob != null) {
                         trueP *= prob.trueP;
                         falseP *= prob.falseP;
                    } else {
                        // maybe
                         trueP *= smallestProb;
                         falseP *= smallestProb;
                    }
                } else {
                    // maybe
                    // trueP *= smallestProb;
                    // falseP *= smallestProb;
                }
            }
           
            if (trueP > falseP) {
                p[i] = true;
                numericP[i] = 1.0;
            } else {
                p[i] = false;
                numericP[i] = -1.0;
            }
        }
       
        logger.info("BayesModel applied on table " + table.getName());
    }
}

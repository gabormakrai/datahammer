package dh.algorithms.utils.trainorder;
 
import dh.repository.Table;
 
public abstract class AbstractTrainOrder {
    Table table;
    public AbstractTrainOrder(Table table) {
        this.table = table;
    }
   
    public abstract int[] generateOrder();
}

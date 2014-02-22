package dh.data.column.factory;
 
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
 
public class DoubleDataColumnFactory extends AbstractColumnFactory {
               
                double[] data;
               
                public DoubleDataColumnFactory(String name, String role, int bufferStep, boolean nullAllowed) {
                                super(name, role, bufferStep, nullAllowed);
                                data = new double[bufferStep];
                }
 
                @Override
                public void addElement(String element) {
                               
                                if (size == data.length) {
                                                double[] newData = new double[data.length + bufferStep];
                                                for (int i=0;i<data.length;i++) {
                                                                newData[i] = data[i];
                                                }
                                                data = null;
                                                data = newData;
                                               
                                                if (nullAllowed) {
                                                                boolean[] newNullElements = new boolean[data.length + bufferStep];
                                                                for (int i=0;i<data.length;i++) {
                                                                                newNullElements[i] = nullElements[i];
                                                                }
                                                                nullElements = null;
                                                                nullElements = newNullElements;
                                                }
                                }
                               
                                if (element == null || element.trim().equals("")) {
                                                if (!nullAllowed) {
                                                                throw new RuntimeException("Null is not allowed on column: " + name + " (double)");
                                                }
                                                data[size] = 0.0;
                                                nullElements[size] = true;
                                } else {
                                                try {
                                                                data[size] = Double.parseDouble(element);
                                                                if (nullAllowed) {
                                                                                nullElements[size] = false;
                                                                }
                                                } catch (NumberFormatException e) {
                                                                data[size] = 0.0;
                                                                if (nullAllowed) {
                                                                                nullElements[size] = true;                                                           
                                                                } else {
                                                                                throw new RuntimeException("Null is not allowed on column: " + name + " (double)");
                                                                }
                                                }
                                }
                               
                                size++;
                }
 
                @Override
                public AbstractDataColumn finishColumn() {                      
                               
                                double[] columnData = null;
                                boolean[] columnNullElements = null;
                               
                                if (size != data.length) {
                                                columnData = new double[size];
                                                for (int i=0;i<size;i++) {
                                                                columnData[i] = data[i];
                                                }
                                                if (nullAllowed) {
                                                                columnNullElements = new boolean[size];
                                                                for (int i=0;i<size;i++) {
                                                                                columnNullElements[i] = nullElements[i];
                                                                }
                                                }
                                } else {
                                                columnData = data;
                                                if (nullAllowed) {
                                                                columnNullElements = nullElements;
                                                }
                                }
                                                               
                                DoubleDataColumn column = new DoubleDataColumn();
                                column.setName(name);
                                column.setRole(role);
                                column.setSize(size);
                                column.setData(columnData);
                                if (nullAllowed) {
                                                column.setNullElements(columnNullElements);
                                }
                                return column;
                }
 
}

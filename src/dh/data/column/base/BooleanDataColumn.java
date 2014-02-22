package dh.data.column.base;
 
import java.util.Arrays;
 
import dh.data.column.AbstractDataColumn;
 
public class BooleanDataColumn extends AbstractDataColumn {
 
                private static final long serialVersionUID = 1L;
               
                private boolean[] data;
               
                public boolean[] getData() {
                                return data;
                }
               
                public void setData(boolean[] data) {
                                this.data = data;
                }
                               
                @Override
                public AbstractDataColumn copy(String newColumnName) {
                                BooleanDataColumn newColumn = new BooleanDataColumn();
                                newColumn.setName(newColumnName);
                                newColumn.setSize(size);
                                newColumn.setRole(role);
                                newColumn.setData(Arrays.copyOf(data, data.length));
                                if (nullElements != null) {
                                                newColumn.setNullElements(Arrays.copyOf(nullElements, nullElements.length));
                                }
                                return newColumn;
                }
               
                @Override
                public String getElement(int index) {
                                if (nullElements == null) {
                                                return "" + data[index];                                               
                                } else {
                                                if (nullElements[index]) {
                                                                return "NULL";
                                                } else {
                                                                return "" + data[index];
                                                }
                                }
                }
                               
                @Override
                public void dispose() {
                                super.dispose();
                                data = null;
                }
               
                @Override
                public String getColumnType() {
                                return "boolean";
                }
               
                @Override
                public void filter(boolean[] filterArray) {
                               
                                int newSize = 0;
                                for (int i=0;i<data.length;i++) {
                                                if (filterArray[i]) {
                                                                newSize++;
                                                }
                                }
                               
                                int newIndex = 0;
                                boolean[] newData = new boolean[newSize];
                                for (int i=0;i<data.length;i++) {
                                                if (filterArray[i]) {
                                                                newData[newIndex] = data[i];
                                                                newIndex++;
                                                }
                                }
                                data = null;
                                data = newData;
                               
                                if (nullElements != null) {
                                                newIndex = 0;
                                                boolean[] newNullElements = new boolean[newSize];
                                                for (int i=0;i<newNullElements.length;i++) {
                                                                if (filterArray[i]) {
                                                                                newNullElements[newIndex] = nullElements[i];
                                                                                newIndex++;
                                                                }
                                                }
                                                nullElements = null;
                                                nullElements = newNullElements;
                                }                             
                               
                                setSize(newSize);
                }
               
                @Override
                public void reorder(int[] order) {
                               
                                boolean[] newData = new boolean[size];
                                for (int i=0;i<newData.length;i++) {
                                                newData[i] = data[order[i]];
                                }
                                data = null;
                                data = newData;
                               
                                if (nullElements != null) {
                                                boolean[] newNulls = new boolean[getSize()];
                                                for (int i=0;i<newNulls.length;i++) {
                                                                newNulls[i] = nullElements[order[i]];
                                                }
                                                nullElements = null;
                                                nullElements = newNulls;                                            
                                }
                }
               
                @Override
                public int compare(int i1, int i2) {
                                return Boolean.compare(data[i1], data[i2]);
                }
}

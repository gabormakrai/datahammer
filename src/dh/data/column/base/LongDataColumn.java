package dh.data.column.base;
 
import java.util.Arrays;
 
import dh.data.column.AbstractDataColumn;
 
public class LongDataColumn extends AbstractDataColumn {
 
                private static final long serialVersionUID = 1L;
               
                private long[] data;
               
                public long[] getData() {
                                return data;
                }
               
                public void setData(long[] data) {
                                this.data = data;
                }
                               
                @Override
                public AbstractDataColumn copy(String newColumnName) {
                                LongDataColumn newColumn = new LongDataColumn();
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
                                return "long";
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
                                long[] newData = new long[newSize];
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
                               
                                long[] newData = new long[size];
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
                                if (data[i1] < data[i2]) {
                                                return -1;
                                } else if (data[i1] > data[i2]) {
                                                return 1;
                                } else {
                                                return 0;
                                }
                }
}
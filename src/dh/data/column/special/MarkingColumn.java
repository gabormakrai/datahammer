package dh.data.column.special;
 
 
import java.util.Arrays;
 
import dh.data.column.AbstractDataColumn;
 
public class MarkingColumn extends AbstractDataColumn {
   
    public static enum MarkingType {
        Train, Test, Skip
    }
 
    private static final long serialVersionUID = 1L;
   
    @Override
    public String getColumnType() {
        return "marking";
    }  
    
    MarkingType[] data;
    public void setData(MarkingType[] data) {
        this.data = data;
    }
    public MarkingType[] getData() {
        return data;
    }
   
    @Override
    public AbstractDataColumn copy(String newColumnName) {
        MarkingColumn newColumn = new MarkingColumn();
        newColumn.setName(newColumnName);
        newColumn.setSize(this.getSize());
        newColumn.setRole(this.getRole());
        newColumn.setData(Arrays.copyOf(this.data, this.data.length));
        return newColumn;
    }
   
    @Override
    public String getElement(int index) {
        return data[index].toString();
    }
   
    @Override
    public void dispose() {
                super.dispose();
        data = null;
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
                                MarkingType[] newData = new MarkingType[newSize];
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
                               
                                MarkingType[] newData = new MarkingType[size];
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
                return data[i1].toString().compareTo(data[i2].toString());
    }
 
}
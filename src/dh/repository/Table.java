package dh.repository;
 
import java.io.Serializable;
import java.util.HashMap;
 
import dh.data.column.AbstractDataColumn;
 
 
public class Table implements Serializable{
               
                private static final long serialVersionUID = 1L;
 
                String name;
               
                HashMap<String, AbstractDataColumn> columns = new HashMap<String, AbstractDataColumn>();
               
                int size;
               
                public Table() {
                }
               
                public Table(String name, int size) {
                    this.name = name;
                                this.size = size;
                }
               
                public String getName() {
                                return name;
                }
                public void setName(String name) {
                                this.name = name;
                }
                public HashMap<String, AbstractDataColumn> getColumns() {
                                return columns;
                }
               
                @SuppressWarnings("unchecked")
                public <T extends AbstractDataColumn> T getColumn(String column) {
                                AbstractDataColumn returnColumn = columns.get(column);
                                if (returnColumn == null) {
                                                throw new RuntimeException("There is no column " + column + " in the table " + name + "...");
                                }
                               
                                try {
                                                return (T)returnColumn;
                                } catch (ClassCastException e) {
                                                throw new RuntimeException("Table " + name + " column " + column + " is " + returnColumn.getColumnType(), e);
                                }
                }
               
                public void setColumns(HashMap<String, AbstractDataColumn> columns) {
                                this.columns = columns;
                }
                public int getSize() {
                                return size;
                }
                public void setSize(int size) {
                                this.size = size;
                }
               
                public void dispose() {
                                for (AbstractDataColumn column : columns.values()) {
                                                column.dispose();
                                }
                }
}

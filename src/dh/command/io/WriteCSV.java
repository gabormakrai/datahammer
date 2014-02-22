package dh.command.io;
 
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.repository.Repository;
import dh.repository.Table;
 
public class WriteCSV extends AbstractCommand {
 
                private static final Logger logger = LoggerFactory.getLogger(WriteCSV.class);
 
                public WriteCSV(Repository repository) {
                                super(repository);
                }
               
                public static enum WriteCSVMode { File, Console };
 
                public void run(String tableName, String[] columnNames, WriteCSVMode mode, String outputFileName, char separator, char quote, char escape, boolean writeHeader, Integer top) {
                                                               
                                Table table = getTable(tableName);
                                                               
                                AbstractDataColumn[] columns = new AbstractDataColumn[columnNames.length];
                                for (int i=0;i<columnNames.length;i++) {
                                                AbstractDataColumn c = table.getColumns().get(columnNames[i]);
                                                if (c == null) {
                                                                throw new RuntimeException("Table " + tableName + " does not have column " + columnNames[i] + "...");
                                                }
                                                columns[i] = c;
                                }
                               
                                BufferedWriter ow = null;
                                try {
                                                if (mode == WriteCSVMode.File) {
                                                                if (outputFileName == null) {
                                                                                throw new RuntimeException("Outputmode is file, but there is no outputFileName...");
                                                                }
                                                               
                                                                ow = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));
                                                }
                                               
                                                StringBuffer buffer = new StringBuffer();
                                               
                                                if (writeHeader) {
                                                                for (int i=0;i<columns.length;i++) {
                                                                                buffer.append(columns[i].getName());
                                                                                if (i != columns.length - 1) {
                                                                                                buffer.append(separator);
                                                                                }
                                                                }
                                                }
                                               
                                                if (mode == WriteCSVMode.File) {
                                                                buffer.append('\n');
                                                                ow.write(buffer.toString());
                                                } else {
                                                                logger.info(buffer.toString());
                                                }
                                               
                                                int limit = table.getSize();
                                                if (top != null && top < limit) {
                                                                limit = top;
                                                }
                                               
                                                for (int i=0;i<limit;i++) {
                                                                buffer.setLength(0);
                                                                for (int j=0;j<columns.length;j++) {
                                                                                buffer.append(columns[j].getElement(i));
                                                                                if (j != columns.length - 1) {
                                                                                                buffer.append(separator);
                                                                                }
                                                                }
                                                                if (mode == WriteCSVMode.File) {
                                                                                buffer.append('\n');
                                                                                ow.write(buffer.toString());
                                                                } else {
                                                                                logger.info(buffer.toString());
                                                                }                                             
                                                }
                                               
                                                if (mode == WriteCSVMode.File) {
                                                   ow.close();
                                                }
                                } catch (IOException e) {
                                                throw new RuntimeException("IO problem...", e);
                                }
                }
}

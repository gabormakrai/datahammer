package dh.command.tools;
 
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.BooleanDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.base.FloatDataColumn;
import dh.data.column.base.IntegerDataColumn;
import dh.data.column.base.LongDataColumn;
import dh.data.column.base.StringDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
 
public class RuntimeMappingTools {
 
                /**
                *
                 * Mapping ->
                 * ${ColumnName}Column -> typesafe column
                * ${ColumnName} -> data array
                * ${ColumnName}Null -> nullElements (be carefull, it can be null...)
                *
                 * @param sb StringBuilder
                * @param repository Repository
                * @param tableName Table to Map
                * @return StringBuilder
                */
                public static StringBuilder mapColumns(StringBuilder sb, Repository repository, String tableName) {
                                for (AbstractDataColumn column : repository.getTables().get(tableName).getColumns().values()) {
                                                String dataType = null;
                                                String columnType = null;
                                               
                                                if (column instanceof DoubleDataColumn) {
                                                                dataType = "double[]";
                                                                columnType = "DoubleDataColumn";
                                                } else if (column instanceof NominalDataColumn) {
                                                                dataType = "int[]";
                                                                columnType = "NominalDataColumn";
                                                } else if (column instanceof FloatDataColumn) {
                                                                dataType = "float[]";
                                                                columnType = "FloatDataColumn";
                                                } else if (column instanceof LongDataColumn) {
                                                                dataType = "long[]";
                                                                columnType = "LongDataColumn";
                                                } else if (column instanceof IntegerDataColumn) {
                                                                dataType = "int[]";
                                                                columnType = "IntegerDataColumn";                                                                    
                                                } else if (column instanceof StringDataColumn) {
                                                                dataType = "String[]";
                                                                columnType = "StringDataColumn";
                                                } else if (column instanceof BooleanDataColumn) {
                                                                dataType = "boolean[]";
                                                                columnType = "BooleanDataColumn";
                                                } else {
                                                                throw new RuntimeException("Not supported column in mapColumns... (" + column.getColumnType() + ")");
                                                }
                                               
                                                sb.append(dataType);
                                                sb.append(" ");
                                                sb.append(column.getName());
                                                sb.append(" = ((");
                                                sb.append(columnType);
                                                sb.append(")repository.getTables().get(tableName).getColumns().get(\"");
                                                sb.append(column.getName());
                                                sb.append("\")).getData();\n");
                                               
                                                sb.append(columnType);
                                                sb.append(" ");
                                                sb.append(column.getName());
                                                sb.append("Column = (");
                                                sb.append(columnType);
                                                sb.append(")repository.getTables().get(tableName).getColumns().get(\"");
                                                sb.append(column.getName());
                                                sb.append("\");\n");                                     
                                               
                                                // sb.append("\n");
                                }
                                return sb;
                }
               
                public static StringBuilder createImport(StringBuilder sb) {
                                sb.append("import dh.data.column.AbstractDataColumn;\n");
                                sb.append("import dh.data.column.base.*;\n");
                                sb.append("import dh.data.column.special.*;\n");
                                sb.append("import dh.repository.Repository;\n\n");
                                return sb;
                }
               
                public static StringBuilder createClassHeader(StringBuilder sb, String baseClassName) {
                                sb.append("public class ");
                                sb.append(baseClassName);
                                sb.append(System.nanoTime());
                                sb.append(" {\n\n");
                                return sb;
                }
 
}

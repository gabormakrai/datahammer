//package dh.command.transformation;
// 
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
// 
//import com.helionprime.jruntime.RuntimeClass;
// 
//import dh.command.AbstractCommand;
//import dh.command.tools.RuntimeMappingTools;
//import dh.data.column.AbstractDataColumn;
//import dh.repository.Repository;
// 
///**
//*
// * columnType: type|[nullable|notnullable]
//*
// * @author makrai
//*
//*/
//public class AddColumn extends AbstractCommand {
// 
//                private static final Logger logger = LoggerFactory.getLogger(AddColumn.class);
// 
//                public AddColumn(Repository repository) {
//                                super(repository);
//                }
//               
//                public void run(String inputTableName, String columnName, String columnType, String loopInside, String beforeLoop, String afterLoop) {
//                                run(inputTableName, columnName, columnType, loopInside, false, beforeLoop, afterLoop);
//                }
//               
//                public void runDebug(String inputTableName, String columnName, String columnType, String loopInside) {
//                                run(inputTableName, columnName, columnType, loopInside, true, null, null);
//                }
//               
//                public void run(String inputTableName, String columnName, String columnType, String loopInside) {
//                                run(inputTableName, columnName, columnType, loopInside, false, null, null);
//                }
//               
//                public void run(String inputTableName, String columnName, String columnType, String loopInside, boolean debug, String beforeLoop, String afterLoop) {
// 
//                                // check that table is exist
//                                // and column is not
//                                checkTableDoesNotHaveColumn(getTable(inputTableName), columnName);
//                               
//                                StringBuilder sb = new StringBuilder();
//                               
//                                RuntimeMappingTools.createImport(sb);
//                               
//                                RuntimeMappingTools.createClassHeader(sb, "AddColumn");
//                               
//                                sb.append("public static AbstractDataColumn doWork(Repository repository, String tableName) {\n\n");
//                               
//                                RuntimeMappingTools.mapColumns(sb, repository, inputTableName);
//                               
//                                createNewColumn(sb, columnName, columnType);
//                               
//                                if (beforeLoop != null) {
//                                                sb.append(beforeLoop);
//                                                sb.append("\n");
//                                }
//                               
//                                sb.append("for (int i=0;i<repository.getTables().get(tableName).getSize();i++) {\n");
//                                sb.append(loopInside);
//                                sb.append("}\n");
// 
//                                if (afterLoop != null) {
//                                                sb.append(afterLoop);
//                                                sb.append("\n");
//                                }
//                               
//                                sb.append("\nreturn ").append(columnName).append("Column;\n}\n}\n\n");
//                               
//                                String classDef = sb.toString();
//                                                               
//                                if (debug) {
//                                                logger.debug(classDef);
//                                }
//                               
//                                AbstractDataColumn newColumn = null;
//                               
//                                try {
//                                               
//                                                RuntimeClass<Class<?>> runtimeClass = new RuntimeClass<Class<?>>(classDef);
//                               
//                                                newColumn = (AbstractDataColumn)runtimeClass.clazz().getDeclaredMethod("doWork", Repository.class, String.class).invoke(null, repository, inputTableName);
//                                               
//                                } catch (Exception e) {
//                                                throw new RuntimeException("Problem with AddColumn...", e);
//                                }
//                               
//                                repository.getTables().get(inputTableName).getColumns().put(newColumn.getName(), newColumn);
//                               
//                                logger.info("Column " + columnName + " added to table " + inputTableName+ "...");
//                }
//               
//                private StringBuilder createNewColumn(StringBuilder sb, String columnName, String columnTypeParameter) {
//                               
//                                String dataType = null;
//                                String columnType = null;
//                                boolean nullable = true;
//                               
//                                if (columnTypeParameter.startsWith("double")) {
//                                                dataType = "double";
//                                                columnType = "DoubleDataColumn";
//                                } else if (columnTypeParameter.startsWith("integer")) {
//                                                dataType = "int";
//                                                columnType = "IntegerDataColumn";
//                                } else if (columnTypeParameter.startsWith("long")) {
//                                                dataType = "long";
//                                                columnType = "LongDataColumn";
//                                } else if (columnTypeParameter.startsWith("float")) {
//                                                dataType = "float";
//                                                columnType = "FloatDataColumn";
//                                } else if (columnTypeParameter.startsWith("boolean")) {
//                                                dataType = "boolean";
//                                                columnType = "BooleanDataColumn";
//                                } else if (columnTypeParameter.startsWith("string")) {
//                                                dataType = "String";
//                                                columnType = "StringDataColumn";
//                                                nullable = false;
//                                } else {
//                                                throw new RuntimeException("Column type " + columnTypeParameter + " is not supported...");
//                                }
//                               
//                                if (nullable && columnTypeParameter.endsWith("notnullable")) {
//                                                nullable = false;
//                                } else {
//                                                nullable = true;
//                                }
//                               
//                                sb.append(dataType).append("[] ").append(columnName).append(" = new ").append(dataType).append("[repository.getTables().get(tableName).getSize()];\n");
//                               
//                                if (nullable) {
//                                                sb.append("boolean[] ").append(columnName).append("Null = new boolean[repository.getTables().get(tableName).getSize()];\n");
//                                }
//                               
//                                sb.append(columnType).append(" ").append(columnName).append("Column = new ").append(columnType).append("();\n");
//                                sb.append(columnName).append("Column.setName(\"").append(columnName).append("\");\n");
//                                sb.append(columnName).append("Column.setRole(\"\");\n");
//                                sb.append(columnName).append("Column.setSize(repository.getTables().get(tableName).getSize());\n");
//                                sb.append(columnName).append("Column.setName(\"").append(columnName).append("\");\n");
//                                sb.append(columnName).append("Column.setData(").append(columnName).append(");\n");
//                                if (nullable) {
//                                                sb.append(columnName).append("Column.setNullElements(").append(columnName).append("Null);\n");
//                                }
//                                return sb;
//                }
// 
//}

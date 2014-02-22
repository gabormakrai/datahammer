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
//import dh.repository.Table;
// 
//public class FilterRows extends AbstractCommand {
// 
//                private static final Logger logger = LoggerFactory.getLogger(FilterRows.class);
// 
//                public FilterRows(Repository repository) {
//                                super(repository);
//                }
//               
//                public void run(String inputTableName, String filterExpression) {
//                                run(inputTableName, filterExpression, null, false);
//                }
//               
//                public void runDebug(String inputTableName, String filterExpression) {
//                                run(inputTableName, filterExpression, null, true);
//                }
//               
//                public void run(String inputTableName, String filterExpression, boolean debug) {
//                                run(inputTableName, filterExpression, null, true);
//                }
//               
//                public void runDebug(String inputTableName, String filterExpression, String beforeLoop) {
//                                run(inputTableName, filterExpression, beforeLoop, true);
//                }
//               
//                public void run(String inputTableName, String filterExpression, String beforeLoop) {
//                                run(inputTableName, filterExpression, beforeLoop, false);
//                }
//               
//                public void run(String inputTableName, String filterExpression, String beforeLoop, boolean debug) {
// 
//                                // check that table is exist
//                                Table table = getTable(inputTableName);
//                               
//                                StringBuilder sb = new StringBuilder();
//                               
//                                RuntimeMappingTools.createImport(sb);
//                               
//                                RuntimeMappingTools.createClassHeader(sb, "FilterRows");
//                               
//                                sb.append("public static boolean[] doWork(Repository repository, String tableName) {\n\n");
//                               
//                                RuntimeMappingTools.mapColumns(sb, repository, inputTableName);
//                               
//                                if (beforeLoop != null) {
//                                                sb.append(beforeLoop);
//                                                sb.append("\n");
//                                }
//                               
//                                sb.append("boolean[] returnArray = new boolean[repository.getTables().get(tableName).getSize()];\n");
//                               
//                                sb.append("for (int i=0;i<repository.getTables().get(tableName).getSize();i++) {\n");
//                                sb.append("returnArray[i]=(");
//                                sb.append(filterExpression);
//                                sb.append(");\n");
//                                sb.append("}\n");
//                               
//                                sb.append("return returnArray;\n}\n;}\n");
// 
//                                String classDef = sb.toString();
//                                                               
//                                if (debug) {
//                                                logger.debug(classDef);
//                                }
//                               
//                                boolean[] filterArray = null;
//                               
//                                try {
//                                               
//                                                RuntimeClass<Class<?>> runtimeClass = new RuntimeClass<Class<?>>(classDef);
//                               
//                                                filterArray = (boolean[])runtimeClass.clazz().getDeclaredMethod("doWork", Repository.class, String.class).invoke(null, repository, inputTableName);
//                                               
//                                } catch (Exception e) {
//                                                throw new RuntimeException("Problem with FilterRows...", e);
//                                }
//                               
//                                for (AbstractDataColumn column : table.getColumns().values()) {
//                                                column.filter(filterArray);
//                                }
//               
//                                int newSize = 0;
//                                for (int i=0;i<filterArray.length;i++) {
//                                                if (filterArray[i]) {
//                                                                newSize++;
//                                                }
//                                }
//                                table.setSize(newSize);
//                               
//                                logger.info("Table " + inputTableName + " filtered, new size is :" + newSize + "...");
//                               
//                }
// 
//}

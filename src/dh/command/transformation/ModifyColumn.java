//package dh.command.transformation;
// 
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
// 
//import com.helionprime.jruntime.RuntimeClass;
// 
//import dh.command.AbstractCommand;
//import dh.command.tools.RuntimeMappingTools;
//import dh.repository.Repository;
// 
//public class ModifyColumn extends AbstractCommand {
// 
//                private static final Logger logger = LoggerFactory.getLogger(ModifyColumn.class);
// 
//                public ModifyColumn(Repository repository) {
//                                super(repository);
//                }
//               
//                public void run(String inputTableName, String loopInside) {
//                                run(inputTableName, loopInside, null, null, false);
//                }
//               
//                public void runDebug(String inputTableName, String loopInside) {
//                                run(inputTableName, loopInside, null, null, true);
//                }
//               
//                public void run(String inputTableName, String loopInside, String beforeLoop) {
//                                run(inputTableName, loopInside, beforeLoop, null, false);
//                }
//               
//                public void runDebug(String inputTableName, String loopInside, String beforeLoop) {
//                                run(inputTableName, loopInside, beforeLoop, null, true);
//                }
//               
//                public void run(String inputTableName, String loopInside, String beforeLoop, String afterLoop, boolean debug) {
//                               
//                                getTable(inputTableName);
//                               
//                                StringBuilder sb = new StringBuilder();
//                               
//                                RuntimeMappingTools.createImport(sb);
//                               
//                                RuntimeMappingTools.createClassHeader(sb, "ModifyColumn");
//                               
//                                sb.append("public static void doWork(Repository repository, String tableName) {\n\n");
//                               
//                                RuntimeMappingTools.mapColumns(sb, repository, inputTableName);
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
//                                sb.append("}\n}\n\n");
//                               
//                                String classDef = sb.toString();
//                                                               
//                                if (debug) {
//                                                logger.debug(classDef);
//                                }
//                               
//                                try {
//                                               
//                                                RuntimeClass<Class<?>> runtimeClass = new RuntimeClass<Class<?>>(classDef);
//                               
//                                                runtimeClass.clazz().getDeclaredMethod("doWork", Repository.class, String.class).invoke(null, repository, inputTableName);
//                                               
//                                } catch (Exception e) {
//                                                throw new RuntimeException("Problem with ModifyColumn...", e);
//                                }
//                               
//                                logger.info("Table " + inputTableName + " has just modified...");
//                }
// 
//}

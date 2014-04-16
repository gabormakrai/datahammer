package dh.data.column.factory;
 
public class ColumnFactoryFactory {
               
                public static AbstractColumnFactory getFactory(String name, String role, String type, boolean nullAllowed, int bufferStep) {
                               
                                if (type.equals("double")) {
                                                return new DoubleDataColumnFactory(name, role, bufferStep, nullAllowed);
                                } else if (type.equals("integer")) {
                                                return new IntegerDataColumnFactory(name, role, bufferStep, nullAllowed);
                                } else if (type.equals("long")) {
                                                return new LongDataColumnFactory(name, role, bufferStep, nullAllowed);
                                } else if (type.equals("nominal")) {
                                                return new NominalDataColumnFactory(name, role, bufferStep, nullAllowed);
                                } else if (type.equals("float")) {
                                                return new FloatDataColumnFactory(name, role, bufferStep, nullAllowed);
                                } else if (type.equals("boolean")) {
                                    return new BooleanDataColumnFactory(name, role, bufferStep, nullAllowed);
                                
//                            } else if (type.equals("string")) {
//                                            return new StringDataColumnFactory(bufferStep);
                                }
                               
                                throw new RuntimeException("Not supported column type " + type + "!");
                }
               
}

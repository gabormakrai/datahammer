package dh.algorithms.utils.splitmaking;
 
public class EntropySplit implements AbstractSplitCalculation {
   
    private static final double logBase = Math.log(2.0);
   
    private double x_log2_x(double x) {
        if (x == 0.0) {
            return 0.0;
        }
        return x * Math.log(x) / logBase;
    }
   
    @Override
    public double calculateSplit(double leftPositive, double leftNegative, double leftSize, double rightPositive, double rightNegative, double rigthSize) {
       
        double res = 1.0;
       
        if (leftSize != 0.0) {
            res += (leftSize / (leftSize + rigthSize)) * ( x_log2_x((double)leftPositive / (double)leftSize) + x_log2_x((double)(leftNegative) / (double)leftSize));
        }
       
        if (rigthSize != 0.0) {
            res += (rigthSize / (leftSize + rigthSize)) * (x_log2_x((double)rightPositive / (double)rigthSize) + x_log2_x((double)(rightNegative) / (double)rigthSize));
        }
        return res;
    }
}

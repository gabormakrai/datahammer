package dh.algorithms.utils.splitmaking;
 
public class NegativeWeightedSplitCalculation implements AbstractSplitCalculation {
   
    @Override
    public double calculateSplit(double leftPositive, double leftNegative, double leftSize, double rightPositive, double rightNegative, double rigthSize) {
       
        double res = 0.0;
       
        if (leftNegative > rightNegative ) {
            // left leaf will be the negative labels
            res = 0.5 * (leftNegative / leftSize + rightPositive / rigthSize);
        } else {
            // left lead will be the positive labels
            res = 0.5 * (leftPositive / leftSize + rightNegative / rigthSize);
        }
       
//        System.out.println(leftPositive + "," + leftNegative + "," + leftSize + " " + rightPositive + "," + rightNegative + "," + rigthSize);
//        System.out.println(res);
       
        return res;
    }
}

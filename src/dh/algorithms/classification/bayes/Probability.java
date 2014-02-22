package dh.algorithms.classification.bayes;
 
public class Probability {
    double trueP;
    double falseP;
    public Probability(double trueP, double falseP) {
        this.trueP = trueP;
        this.falseP = falseP;
    }
    public void setFalseP(double falseP) {
        this.falseP = falseP;
    }
    public void setTrueP(double trueP) {
        this.trueP = trueP;
    }
    public double getFalseP() {
        return falseP;
    }
    public double getTrueP() {
        return trueP;
    }
    @Override
    public String toString() {
        return "Prob(T=" + trueP + ",F=" + falseP + ")";
    }
}

package br.jorge.codebind;

public class Diet {

    public double minimumFat;
    public double minimumCarbs;
    public double minimumProtein;

    public double maxPortions;

    public Diet(double minimumFat, double minimumCarbs, double minimumProtein, double maxPortions) {
        this.minimumFat = minimumFat;
        this.minimumCarbs = minimumCarbs;
        this.minimumProtein = minimumProtein;
        this.maxPortions = maxPortions;
    }
}

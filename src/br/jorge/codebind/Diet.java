package br.jorge.codebind;

class Diet {

    double minimumFat;
    double minimumCarbs;
    double minimumProtein;

    double maxPortions;

    Diet(double minimumFat, double minimumCarbs, double minimumProtein, double maxPortions) {
        this.minimumFat = minimumFat;
        this.minimumCarbs = minimumCarbs;
        this.minimumProtein = minimumProtein;
        this.maxPortions = maxPortions;
    }
}

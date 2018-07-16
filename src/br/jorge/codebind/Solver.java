package br.jorge.codebind;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;

public class Solver {
    public static void main(String[] args) throws Exception {
        Diet testDiet = new Diet(24, 36, 4, 5);
        Product p1 = new Product(0.2, 8, 12, 2);
        Product p2 = new Product(0.3, 12, 12, 1);

        OptimizedDiet diet = doMath(testDiet, p1, p2);
    }

    public static OptimizedDiet doMath(Diet diet, Product p1, Product p2) throws JOptimizerException {

        // Função objetiva (minimização)
        LinearMultivariateRealFunction objectiveFunction = new LinearMultivariateRealFunction(new double[] {p1.cost, p2.cost}, 0);

        // Sujeito a...
        ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[6];
        inequalities[0] = new LinearMultivariateRealFunction(new double[]{-1.0, 0.00}, 0.0);
        inequalities[1] = new LinearMultivariateRealFunction(new double[]{0.0, -1.00}, 0.0);
        inequalities[2] = new LinearMultivariateRealFunction(new double[]{-p1.fat, -p2.fat}, diet.minimumFat);
        inequalities[3] = new LinearMultivariateRealFunction(new double[]{-p1.carbs, -p2.carbs}, diet.minimumCarbs);

        inequalities[4] = new LinearMultivariateRealFunction(new double[]{-p1.protein, -p2.protein}, diet.minimumProtein);

        inequalities[5] = new LinearMultivariateRealFunction(new double[]{1.0, 1.00}, -diet.maxPortions);

        // Define o problema de otimização
        OptimizationRequest or = new OptimizationRequest();
        or.setF0(objectiveFunction);
        or.setFi(inequalities);
        or.setToleranceFeas(1.E-9);
        or.setTolerance(1.E-9);

        // Calcula
        JOptimizer opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        opt.optimize();

        double[] sol = opt.getOptimizationResponse().getSolution();

        long amoutOf1 = Math.round(sol[0]);
        long amoutOf2 = Math.round(sol[1]);

        // Constroi o objeto da solução
        return new OptimizedDiet(amoutOf1, amoutOf2, Math.round(p1.cost * amoutOf1 + p2.cost * amoutOf2));
    }
}

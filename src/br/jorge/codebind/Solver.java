package br.jorge.codebind;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;

public class Solver {
    public static void main(String[] args) throws Exception {
        // usado só para teste com valores de exemplo

        Diet testDiet = new Diet(24, 36, 4, 5);
        Product p1 = new Product(0.2, 8, 12, 2);
        Product p2 = new Product(0.3, 12, 12, 1);

        OptimizedDiet diet = doMath(testDiet, p1, p2);
    }

    /**
     * Otimiza um problema de dieta com dois produtos, restrições e custos
     * @param diet Exigências da dieta
     * @param p1 Produto A
     * @param p2 Produto B
     * @return Solução ótima (se possível)
     * @throws JOptimizerException
     */
    static OptimizedDiet doMath(Diet diet, Product p1, Product p2) throws JOptimizerException {

        // Função objetiva (minimização)
        LinearMultivariateRealFunction objectiveFunction = new LinearMultivariateRealFunction(new double[] {p1.cost, p2.cost}, 0);

        // Sujeito a...
        ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[6];

        // Exigências para que x >= 0
        // Note que o sinal é pra converter a restrição para -x <= 0
        inequalities[0] = new LinearMultivariateRealFunction(new double[]{-1.0, 0.00}, 0.0);
        inequalities[1] = new LinearMultivariateRealFunction(new double[]{0.0, -1.00}, 0.0);

        // Observe os sinais das variáveis. Em alguns casos é negativo pois devemos tornar todas as inequações no formato Ax <= b
        // Dessa forma invertemos as que estão no formato Ax >= b
        // O vetor b deve estar do lado esquerdo das inequações. Ou seja, acabamos colocando tudo no formato Ax + b <= 0
        // Isso se deve a exigências do JOptimizer
        inequalities[2] = new LinearMultivariateRealFunction(new double[]{-p1.fat, -p2.fat}, diet.minimumFat);
        inequalities[3] = new LinearMultivariateRealFunction(new double[]{-p1.carbs, -p2.carbs}, diet.minimumCarbs);
        inequalities[4] = new LinearMultivariateRealFunction(new double[]{-p1.protein, -p2.protein}, diet.minimumProtein);
        inequalities[5] = new LinearMultivariateRealFunction(new double[]{1.0, 1.00}, -diet.maxPortions);

        // Define o problema de otimização
        OptimizationRequest or = new OptimizationRequest();
        or.setF0(objectiveFunction);
        or.setFi(inequalities);
        or.setToleranceFeas(JOptimizer.DEFAULT_FEASIBILITY_TOLERANCE);
        or.setTolerance(JOptimizer.DEFAULT_TOLERANCE);

        // Calcula a solução ótima
        JOptimizer opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        opt.optimize();

        // Obtém os valores da solução ótima
        double[] sol = opt.getOptimizationResponse().getSolution();

        double amoutOf1 = sol[0];
        double amoutOf2 = sol[1];

        // Constroi e retorna o objeto qu representa a solução
        return new OptimizedDiet(amoutOf1, amoutOf2, p1.cost * amoutOf1 + p2.cost * amoutOf2);
    }
}

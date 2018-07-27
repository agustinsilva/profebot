package ar.com.profebot.ia;

import ia.module.genetic.algorithm.GeneticAlgorithm;

import java.util.concurrent.*;

public class GeneticAlgorithmExecutor {

    private static String baseExpression;
    private GeneticAlgorithm geneticAlgorithm;

    public GeneticAlgorithmExecutor(String aBaseExpression){
        baseExpression = aBaseExpression;
        this.geneticAlgorithm = null != baseExpression && !"".equals(baseExpression) ? new GeneticAlgorithm(baseExpression) : null;
    }

    public Expression execute(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Expression> future = executor.submit(new Task());
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            future.cancel(true);
        }
        return new Expression("", 0.0);
    }

    public Expression getMostSimilarExpression(){
        if(geneticAlgorithm != null) {
            String mostSimilarExpression = geneticAlgorithm.getExpressionMostSimilar();
            return new Expression(mostSimilarExpression, geneticAlgorithm.getSimilarExpressionCalculator().similarityWith(mostSimilarExpression));
        }
        return new Expression("", 0.0);
    }

    class Task implements Callable<Expression> {
        @Override
        public Expression call() throws Exception {
            return new GeneticAlgorithmExecutor(baseExpression).getMostSimilarExpression();
        }
    }
}

package ar.com.profebot.ia;

import ia.module.genetic.algorithm.GeneticAlgorithm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class GeneticAlgorithmExecutor {

    public static List<ExpressionResponse> execute(String aTermExpression, String aContextExpression){
        return getMostSimilarExpressionTo(aTermExpression, "".equals(aContextExpression) ? aTermExpression : aContextExpression);
    }

    private static List<ExpressionResponse> getMostSimilarExpressionTo(String aTermExpression, String aContextExpression){
        ExecutorService executor = Executors.newSingleThreadExecutor();

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(aTermExpression));
        tasks.add(new Task(aContextExpression));
        tasks.add(new Task(aContextExpression));
        tasks.add(new Task(aContextExpression));

        List<ExpressionResponse> responses = new ArrayList<>();

        try {
            List<Future<ExpressionResponse>> futures = executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
            for(Future future : futures){
                responses.add((ExpressionResponse) future.get());
            }
            return responses;
        } catch (Exception e) {
            System.out.println("\n\n\n\nTimeout\n\n\n\n");
            for(Thread thread : Thread.getAllStackTraces().keySet()){
                if(thread.getName().contains("pool-") && thread.getName().contains("thread-")){
                    System.out.println("Thread stopped: " + thread.getName());
                    thread.stop();
                }
            }
            responses = new ArrayList<>();
            responses.add(ExpressionResponse.empty());
            return responses;
        }
    }

    static class Task implements Callable<ExpressionResponse> {

        private String baseExpression;

        public Task(String aBaseExpression){
            baseExpression = aBaseExpression;
        }

        @Override
        public ExpressionResponse call() throws Exception {
            if(null != this.baseExpression && !"".equals(this.baseExpression)){
                GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(this.baseExpression);
                String mostSimilarExpression = geneticAlgorithm.getExpressionMostSimilar();
                return new ExpressionResponse(mostSimilarExpression, geneticAlgorithm.getSimilarExpressionCalculator().similarityWith(mostSimilarExpression));
            }
            return new ExpressionResponse("", 0.0);
        }
    }
}

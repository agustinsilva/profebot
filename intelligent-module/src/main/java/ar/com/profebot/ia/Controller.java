package ar.com.profebot.ia;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class Controller {

    @RequestMapping(method = RequestMethod.GET)
    public String getMostSimilarExpressionTo() {
        String baseExpression = "x^2+2x+1";
        GeneticAlgorithmExecutor executor = new GeneticAlgorithmExecutor(baseExpression);
        Expression result = executor.execute();
        return "Expresión: " + result.getExpressionAsInfix() + " Similarity: " + result.getSimilarity();
    }
}

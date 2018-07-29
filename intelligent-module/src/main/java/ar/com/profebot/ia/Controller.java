package ar.com.profebot.ia;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/")
@RestController
public class Controller {

    @RequestMapping(method = RequestMethod.GET)
    public List<ExpressionResponse> getMostSimilarExpressionTo() {
        String termExpression = "3(x+1)";
        String contextExpression = "3(x+1)+5";
        return GeneticAlgorithmExecutor.execute(termExpression, contextExpression);
    }
}

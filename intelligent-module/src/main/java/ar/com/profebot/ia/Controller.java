package ar.com.profebot.ia;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class Controller {

    @RequestMapping(method = RequestMethod.GET)
    public EquationsResponse newEquationsSimilarTo() {
        return GeneticAlgorithmExecutor.execute("3(x+1)", "3(x+1)+5", "=");
    }

    @RequestMapping(method = RequestMethod.POST)
    public EquationsResponse newEquationsSimilarTo(@RequestBody IAModuleParams params) {
        return GeneticAlgorithmExecutor.execute(params.getTerm(), params.getContext(), params.getRoot());
    }
}

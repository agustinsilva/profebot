package ar.com.profebot.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EquationsResponse {

    private String equations;

    public EquationsResponse(List<ExpressionResponse> expressionResponse, String root) {
        List<String> equationsList = new ArrayList<>();
        if(!expressionResponse.isEmpty()){
            if(expressionResponse.get(0).isValid()){
                equationsList.add(newEquation(expressionResponse.get(0), root));
            }

            if(expressionResponse.get(1).isValid()){
                equationsList.add(newEquation(expressionResponse.get(1), root));
            }

            if(expressionResponse.get(2).isValid() && expressionResponse.get(3).isValid()){
                equationsList.add(newEquation(expressionResponse.get(2), expressionResponse.get(3), root));
            }
        }

        equations = String.join(";", equationsList);
    }

    private String newEquation(ExpressionResponse expressionResponse, String root){
        return expressionResponse.getExpressionAsInfix() + root + getRandomNumber();
    }

    private String newEquation(ExpressionResponse expressionResponse1, ExpressionResponse expressionResponse2, String root){
        return expressionResponse1.getExpressionAsInfix() + root + expressionResponse2.getExpressionAsInfix();
    }

    private Integer getRandomNumber(){
        return new Random().nextInt(10) + 1;
    }

    public String getEquations(){
        return this.equations;
    }
}

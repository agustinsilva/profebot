package ar.com.profebot.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EquationsResponse {

    @JsonProperty("equations")
    private List<String> equations;

    public EquationsResponse(List<ExpressionResponse> expressionResponse, String root) {
        equations = new ArrayList<>();
        if(!expressionResponse.isEmpty()){
            if(expressionResponse.get(0).isValid()){
                equations.add(newEquation(expressionResponse.get(0), root));
            }

            if(expressionResponse.get(1).isValid()){
                equations.add(newEquation(expressionResponse.get(1), root));
            }

            if(expressionResponse.get(2).isValid() && expressionResponse.get(3).isValid()){
                equations.add(newEquation(expressionResponse.get(2), expressionResponse.get(3), root));
            }
        }
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
}

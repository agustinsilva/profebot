package ar.com.profebot.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class EquationsResponse {

    @JsonProperty("equations")
    private String equations;

    public EquationsResponse(ExpressionResponse expressionResponse) {

        equations = "";
    }

    private String newEquation(ExpressionResponse expressionResponse, Integer random, String root){
        return expressionResponse.getExpressionAsInfix() + root + random;
    }

    private String newEquation(ExpressionResponse expressionResponse1, ExpressionResponse expressionResponse2, String root){
        return expressionResponse1.getExpressionAsInfix() + root + expressionResponse2.getExpressionAsInfix();
    }

    private Boolean isValidExpression(ExpressionResponse expressionResponse){
        return !expressionResponse.getExpressionAsInfix().equals("") || expressionResponse.getSimilarity() >= 0.7;
    }

    private Integer getRandomNumber(){
        return new Random().nextInt(10) + 1;
    }
}

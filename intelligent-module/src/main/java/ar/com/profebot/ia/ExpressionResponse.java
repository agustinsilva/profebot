package ar.com.profebot.ia;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpressionResponse {

    @JsonProperty("expression")
    private String expressionAsInfix;
    @JsonProperty("similarity")
    private Double similarity;

    public ExpressionResponse(String expressionAsInfix, Double similarity) {
        this.expressionAsInfix = expressionAsInfix;
        this.similarity = similarity;
    }

    public static ExpressionResponse empty(){
        return new ExpressionResponse("", 0.0);
    }

    public String getExpressionAsInfix() {
        return expressionAsInfix;
    }

    public Double getSimilarity() {
        return similarity;
    }
}

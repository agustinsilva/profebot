package ar.com.profebot.ia;

public class Expression {

    private String expressionAsInfix;
    private Double similarity;

    public Expression(String expressionAsInfix, Double similarity) {
        this.expressionAsInfix = expressionAsInfix;
        this.similarity = similarity;
    }

    public String getExpressionAsInfix() {
        return expressionAsInfix;
    }

    public Double getSimilarity() {
        return similarity;
    }
}

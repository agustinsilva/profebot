package ar.com.profebot.ia;

public class ExpressionResponse {

    private String expressionAsInfix;
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

    public Boolean isValid(){
        return !this.expressionAsInfix.equals("") && this.similarity >= 0.75;
    }
}

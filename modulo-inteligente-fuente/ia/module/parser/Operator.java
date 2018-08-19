package ia.module.parser;

import java.util.*;
import ia.module.config.*;

public class Operator
{
    public static final int N = 0;
    public static final int N_PLUS_N = 1;
    public static final int N_MINUS_N = 2;
    public static final int N_BY_N = 3;
    public static final int N_BY_X = 4;
    public static final int N_DIVIDED_N = 5;
    public static final int N_RAISED_TO_MINUS_N = 6;
    public static final int ROOT_OF_N = 7;
    public static final int X = 8;
    public static final int PLUS_OR_MINUS_TERM_WITH_X = 9;
    public static final int BY_TERM_WITH_X = 10;
    public static final int TERM_WITH_X_DIVIDED_N = 11;
    public static final int TERM_WITH_X_BY_TERM_WITH_X = 12;
    public static final int DIVIDED_TERM_WITH_X = 13;
    public static final int TERM_WITH_X_RAISED_TO_MINUS_1 = 14;
    public static final int TERM_WITH_X_RAISED_TO_MINUS_N = 15;
    public static final int ROOT_OF_TERM_WITH_X = 16;
    public static final int TRIGONOMETRIC = 17;
    public static final int RAISED_TO_TERM_WITH_X = 18;
    public static final int LOGARITHM = 19;
    public static final int DERIVATIVE = 20;
    public static final int INTEGRAL = 21;
    public static List<Long> FIBONACCI;
    private Integer operator;
    private Integer degree;
    
    public Integer getOperator() {
        return this.operator;
    }
    
    public Integer getDegree() {
        return this.degree;
    }
    
    public static final List<Integer> equivalentTokens() {
        final List<Integer> tokens = new ArrayList<Integer>();
        tokens.add(10);
        tokens.add(11);
        return tokens;
    }
    
    public Operator(final Integer operator, final Integer degree) {
        this.operator = operator;
        this.degree = degree;
    }
    
    public static Operator newToken(final Integer operator, final Integer degree) {
        return new Operator(operator, degree);
    }
    
    @Override
    public String toString() {
        return "(" + this.tokenName() + ", " + this.degree + ")";
    }
    
    public boolean equals(final Operator operator1) {
        return this.operator.equals(operator1.getOperator());
    }
    
    public boolean totallyEquals(final Operator operator1) {
        return this.equals(operator1) && this.degree.equals(operator1.getDegree());
    }
    
    public String tokenName() {
        switch (this.operator) {
            case 0: {
                return "N";
            }
            case 1: {
                return "N_PLUS_N";
            }
            case 2: {
                return "N_MINUS_N";
            }
            case 3: {
                return "N_BY_N";
            }
            case 4: {
                return "N_BY_X";
            }
            case 5: {
                return "N_DIVIDED_N";
            }
            case 6: {
                return "N_RAISED_TO_MINUS_N";
            }
            case 7: {
                return "ROOT_OF_N";
            }
            case 8: {
                return "X";
            }
            case 9: {
                return "PLUS_OR_MINUS_TERM_WITH_X";
            }
            case 10: {
                return "BY_TERM_WITH_X";
            }
            case 11: {
                return "TERM_WITH_X_DIVIDED_N";
            }
            case 12: {
                return "TERM_WITH_X_BY_TERM_WITH_X";
            }
            case 13: {
                return "DIVIDED_TERM_WITH_X";
            }
            case 14: {
                return "TERM_WITH_X_RAISED_TO_MINUS_1";
            }
            case 15: {
                return "TERM_WITH_X_RAISED_TO_MINUS_N";
            }
            case 16: {
                return "ROOT_OF_TERM_WITH_X";
            }
            case 17: {
                return "TRIGONOMETRIC";
            }
            case 18: {
                return "RAISED_TO_TERM_WITH_X";
            }
            case 19: {
                return "LOGARITHM";
            }
            case 20: {
                return "DERIVATIVE";
            }
            default: {
                return "INTEGRAL";
            }
        }
    }
    
    public Long getFibonacciWeight() {
        final Integer token = this.getOperator();
        if (Operator.FIBONACCI == null) {
            (Operator.FIBONACCI = new ArrayList<Long>()).add(1L);
            Operator.FIBONACCI.add(1L);
            Operator.FIBONACCI = this.recursiveFibonacci(Operator.FIBONACCI);
        }
        final Integer additional = (this.operator == 12) ? this.degree : 0;
        return Operator.FIBONACCI.get((token > 10) ? (token - 1) : ((int)token)) + additional;
    }
    
    private List<Long> recursiveFibonacci(final List<Long> fibonacci) {
        if (fibonacci.size() >= NeuralNetworkConfig.INPUTS) {
            return fibonacci;
        }
        fibonacci.add(fibonacci.get(fibonacci.size() - 1) + fibonacci.get(fibonacci.size() - 2));
        return this.recursiveFibonacci(fibonacci);
    }
    
    public static Long getFibonacciWeight(final Integer index) {
        final Operator operator = new Operator(index, 0);
        return operator.getFibonacciWeight();
    }
    
    public static Long getFibonacciWeightsSum() {
        Long sum = 0L;
        for (int i = 0; i < 21; ++i) {
            sum += getFibonacciWeight(i);
        }
        return sum;
    }
    
    public static double[] getFibonacci(final Integer size) {
        final double[] fibonacci = new double[(int)size];
        for (int i = 0; i < size; ++i) {
            fibonacci[i] = getFibonacciWeight(i);
        }
        return fibonacci;
    }
}

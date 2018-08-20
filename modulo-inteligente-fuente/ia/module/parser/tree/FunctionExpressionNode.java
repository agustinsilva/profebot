package ia.module.parser.tree;

import com.sun.tools.corba.se.idl.constExpr.*;
import java.text.*;
import ia.module.parser.*;
import java.util.*;

public class FunctionExpressionNode extends AbstractExpressionNode implements ExpressionNode
{
    public static final int SIN = 1;
    public static final int COS = 2;
    public static final int TAN = 3;
    public static final int ASIN = 4;
    public static final int ACOS = 5;
    public static final int ATAN = 6;
    public static final int SQRT = 7;
    public static final int EXP = 8;
    public static final int LN = 9;
    public static final int LOG = 10;
    public static final int LOG2 = 11;
    public static final int DERIVATIVE = 12;
    public static final int INTEGRAL = 13;
    private int function;
    private ExpressionNode argument;
    
    public FunctionExpressionNode(final int function, final ExpressionNode argument) {
        this.function = function;
        this.argument = argument;
    }
    
    @Override
    public int getType() {
        return 6;
    }
    
    @Override
    public double getValue() throws EvaluationException {
        switch (this.function) {
            case 1: {
                return Math.sin(this.argument.getValue());
            }
            case 2: {
                return Math.cos(this.argument.getValue());
            }
            case 3: {
                return Math.tan(this.argument.getValue());
            }
            case 4: {
                return Math.asin(this.argument.getValue());
            }
            case 5: {
                return Math.acos(this.argument.getValue());
            }
            case 6: {
                return Math.atan(this.argument.getValue());
            }
            case 7: {
                return Math.sqrt(this.argument.getValue());
            }
            case 8: {
                return Math.exp(this.argument.getValue());
            }
            case 9: {
                return Math.log(this.argument.getValue());
            }
            case 10: {
                return Math.log(this.argument.getValue()) * 0.4342944819032518;
            }
            case 11: {
                return Math.log(this.argument.getValue()) * 1.4426950408889634;
            }
            default: {
                throw new EvaluationException("Invalid function id " + this.function + "!");
            }
        }
    }
    
    @Override
    public Boolean isEven() {
        try {
            return this.getValue() % 2.0 == 0.0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static int stringToFunction(final String str) throws ParseException {
        if (str.equals("sin")) {
            return 1;
        }
        if (str.equals("cos")) {
            return 2;
        }
        if (str.equals("tan")) {
            return 3;
        }
        if (str.equals("asin")) {
            return 4;
        }
        if (str.equals("acos")) {
            return 5;
        }
        if (str.equals("atan")) {
            return 6;
        }
        if (str.equals("sqrt")) {
            return 7;
        }
        if (str.equals("exp")) {
            return 8;
        }
        if (str.equals("ln")) {
            return 9;
        }
        if (str.equals("log")) {
            return 10;
        }
        if (str.equals("log2b")) {
            return 11;
        }
        if (str.equals("dx")) {
            return 12;
        }
        if (str.equals("int")) {
            return 13;
        }
        throw new ParseException("Unexpected Function " + str + " found!", 0);
    }
    
    public static String getAllFunctions() {
        return "sin|cos|tan|asin|acos|atan|sqrt|exp|ln|log|log2b|int|dx";
    }
    
    @Override
    public Boolean hasVariable() {
        return (this.function < 12 && this.argument.hasVariable()) || (this.function == 12 && this.argument.hasVariable() && !this.argument.isLineal()) || (this.function == 13 && !this.argument.isZero());
    }
    
    @Override
    public Boolean isVariable() {
        return (this.function == 12 && this.argument.isQuadraticX()) || (this.function == 13 && this.argument.isNumber());
    }
    
    @Override
    public Boolean isNumber() {
        return (this.function <= 12 && this.argument.isNumber()) || (this.function == 12 && this.argument.isLineal()) || (this.function == 13 && this.argument.isZero());
    }
    
    @Override
    public Boolean isLineal() {
        return (this.function < 12 && this.argument.isNumber()) || (this.function == 12 && this.argument.isLineal()) || (this.function == 13 && this.argument.isNumber());
    }
    
    @Override
    public Integer getLevel() {
        final Boolean argumentHasVariable = this.argument.hasVariable();
        Integer functionLevel = null;
        switch (this.function) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                functionLevel = 6;
                break;
            }
            case 7:
            case 8: {
                functionLevel = (argumentHasVariable ? 5 : 2);
                break;
            }
            case 9:
            case 10:
            case 11: {
                functionLevel = 8;
                break;
            }
            case 12: {
                functionLevel = 9;
                break;
            }
            case 13: {
                functionLevel = 10;
                break;
            }
            default: {
                functionLevel = 0;
                break;
            }
        }
        return Math.max(functionLevel, this.argument.getLevel());
    }
    
    @Override
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        expressionsWithArgumentStructures.addExpressionWithArguments(this.getToken(), this.argument.getToken());
        return this.argument.getStructureOf(expressionsWithArgumentStructures);
    }
    
    @Override
    public Integer getToken() {
        final Boolean argumentHasVariable = this.argument.hasVariable();
        Integer token = null;
        switch (this.function) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                token = 17;
                break;
            }
            case 7:
            case 8: {
                token = (argumentHasVariable ? 16 : 7);
                break;
            }
            case 9:
            case 10:
            case 11: {
                token = 19;
                break;
            }
            case 12: {
                token = 20;
                break;
            }
            case 13: {
                token = 21;
                break;
            }
            default: {
                token = 0;
                break;
            }
        }
        return token;
    }
    
    @Override
    public ExpressionNode normalize() {
        this.argument = this.argument.normalize();
        return this;
    }
    
    @Override
    public List<Operator> getListOfTokens() {
        final List<Operator> tokens = new ArrayList<Operator>();
        tokens.add(Operator.newToken(this.getToken(), this.getDegree()));
        tokens.addAll(this.argument.getListOfTokens());
        return tokens;
    }
    
    @Override
    public ExpressionNode simplify() {
        final ExpressionNode argumentSimplified = this.argument.simplify();
        if (argumentSimplified.isNumber() && this.function != 13) {
            try {
                return new ConstantExpressionNode(argumentSimplified.getValue());
            }
            catch (Exception e) {
                return new FunctionExpressionNode(this.function, argumentSimplified);
            }
        }
        return new FunctionExpressionNode(this.function, argumentSimplified);
    }
}

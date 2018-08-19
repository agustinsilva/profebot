package ia.module.parser;

import java.util.*;
import io.jenetics.ext.util.*;
import io.jenetics.prog.op.*;
import sun.tools.jstat.*;
import java.text.*;
import ia.module.parser.tree.*;

public class Parser
{
    LinkedList<Token> tokens;
    Token lookahead;
    
    public ExpressionNode parse(final TreeNode<Op<Double>> expression) throws ParserException, ParseException {
        return this.parse(this.getAsInfix(expression));
    }
    
    public String getAsInfix(final TreeNode<Op<Double>> expression) {
        try {
            return Double.valueOf(expression.toString()).toString();
        }
        catch (Exception e) {
            final String name;
            final String currentExpression = name = ((Op)expression.getValue()).name();
            switch (name) {
                case "ADD": {
                    return this.sumOrMinusAsInfix(expression, "+");
                }
                case "SUB": {
                    return this.sumOrMinusAsInfix(expression, "-");
                }
                case "MUL": {
                    return this.mulOrDivOperation(expression, "*");
                }
                case "DIV": {
                    return this.mulOrDivOperation(expression, "/");
                }
                case "POW": {
                    return this.powOperation(expression);
                }
                case "SQRT": {
                    return this.functionOperation(expression, "sqrt");
                }
                case "LN": {
                    return this.functionOperation(expression, "ln");
                }
                case "LOG": {
                    return this.functionOperation(expression, "log");
                }
                case "LOG2B": {
                    return this.functionOperation(expression, "log2b");
                }
                case "SIN": {
                    return this.functionOperation(expression, "sin");
                }
                case "COS": {
                    return this.functionOperation(expression, "cos");
                }
                case "TAN": {
                    return this.functionOperation(expression, "tan");
                }
                case "INTEGRAL": {
                    return this.functionOperation(expression, "int");
                }
                case "DERIVATIVE": {
                    return this.functionOperation(expression, "dx");
                }
                default: {
                    return currentExpression;
                }
            }
        }
    }
    
    private String sumOrMinusAsInfix(final TreeNode<Op<Double>> expression, final String operator) {
        String sum = "";
        final Integer childCount = expression.childCount();
        for (int i = 0; i < childCount; ++i) {
            sum = sum + this.getAsInfix((TreeNode<Op<Double>>)expression.getChild(i)) + operator;
        }
        return sum.substring(0, sum.length() - 1);
    }
    
    private String mulOrDivOperation(final TreeNode<Op<Double>> expression, final String operator) {
        String result = "";
        final Integer childCount = expression.childCount();
        for (int i = 0; i < childCount; ++i) {
            result = result + "(" + this.getAsInfix((TreeNode<Op<Double>>)expression.getChild(i)) + ")" + operator;
        }
        return result.substring(0, result.length() - 1);
    }
    
    private String powOperation(final TreeNode<Op<Double>> expression) {
        String result = "";
        final Integer childCount = expression.childCount();
        for (int i = 0; i < childCount; ++i) {
            result = result + "(" + this.getAsInfix((TreeNode<Op<Double>>)expression.getChild(i)) + ")^";
        }
        return result.substring(0, result.length() - 1);
    }
    
    private String functionOperation(final TreeNode<Op<Double>> expression, final String operator) {
        String result = "";
        final Integer childCount = expression.childCount();
        for (int i = 0; i < childCount; ++i) {
            result = result + operator + "(" + this.getAsInfix((TreeNode<Op<Double>>)expression.getChild(i)) + ")";
        }
        return result;
    }
    
    public ExpressionNode parse(final String expression) throws ParserException, ParseException {
        final Tokenizer tokenizer = Tokenizer.getExpressionTokenizer();
        tokenizer.tokenize(this.cleanFormatOf(expression));
        final LinkedList<Token> tokens = tokenizer.getTokens();
        return this.parse(tokens);
    }
    
    public String cleanFormatOf(final String expression) {
        String expressionCleaned = this.replaceComplexOperatorsNames(expression);
        expressionCleaned = this.addMultiplicationSymbols(expressionCleaned);
        return expressionCleaned.replaceAll("e", "2.718281828459045235360").replaceAll("pi", "3.14159265358979323846").replaceAll("\\)x", ")*x").replaceAll("\\)\\(", ")*(").replaceAll("x\\(", "x*(").replaceAll("dx\\*\\(", "dx(");
    }
    
    private String replaceComplexOperatorsNames(final String expression) {
        return expression.replaceAll("derivative", "dx").replaceAll("integral", "int");
    }
    
    private String addMultiplicationSymbols(String expression) {
        for (int i = 0; i <= 9; ++i) {
            expression = expression.replaceAll(i + "\\(", i + "*(").replaceAll(i + "sqrt", i + "*sqrt").replaceAll(i + "sin", i + "*sin").replaceAll(i + "cos", i + "*cos").replaceAll(i + "tan", i + "*tan").replaceAll(i + "ln", i + "*ln").replaceAll(i + "log", i + "*log").replaceAll(i + "log2b", i + "*log2b").replaceAll(i + "dx", i + "*dx").replaceAll(i + "int", i + "*int").replaceAll(i + "x", i + "*x");
        }
        return expression;
    }
    
    public ExpressionNode parse(final LinkedList<Token> tokens) throws ParseException, ParserException {
        this.tokens = (LinkedList<Token>)tokens.clone();
        this.lookahead = this.tokens.getFirst();
        final ExpressionNode expr = this.expression();
        if (this.lookahead.token != 0) {
            throw new ParseException("Unexpected symbol " + this.lookahead + " found", 0);
        }
        return expr;
    }
    
    private void nextToken() {
        this.tokens.pop();
        if (this.tokens.isEmpty()) {
            this.lookahead = new Token(0, "", -1);
        }
        else {
            this.lookahead = this.tokens.getFirst();
        }
    }
    
    private ExpressionNode expression() throws ParseException {
        final ExpressionNode expr = this.signedTerm();
        return this.sumOp(expr);
    }
    
    private ExpressionNode sumOp(final ExpressionNode expr) throws ParseException {
        if (this.lookahead.token == 1) {
            AdditionExpressionNode sum;
            if (expr.getType() == 3) {
                sum = (AdditionExpressionNode)expr;
            }
            else {
                sum = new AdditionExpressionNode(expr, true);
            }
            final boolean positive = this.lookahead.sequence.equals("+");
            this.nextToken();
            final ExpressionNode t = this.term();
            sum.add(t, positive);
            return this.sumOp(sum);
        }
        return expr;
    }
    
    private ExpressionNode signedTerm() throws ParseException {
        if (this.lookahead.token != 1) {
            return this.term();
        }
        final boolean positive = this.lookahead.sequence.equals("+");
        this.nextToken();
        final ExpressionNode t = this.term();
        if (positive) {
            return t;
        }
        return new AdditionExpressionNode(t, false);
    }
    
    private ExpressionNode term() throws ParseException {
        final ExpressionNode f = this.factor();
        return this.termOp(f);
    }
    
    private ExpressionNode termOp(final ExpressionNode expression) throws ParseException {
        if (this.lookahead.token == 2) {
            MultiplicationExpressionNode prod;
            if (expression.getType() == 4) {
                prod = (MultiplicationExpressionNode)expression;
            }
            else {
                prod = new MultiplicationExpressionNode(expression, true);
            }
            final boolean positive = this.lookahead.sequence.equals("*");
            this.nextToken();
            final ExpressionNode f = this.signedFactor();
            prod.add(f, positive);
            return this.termOp(prod);
        }
        return expression;
    }
    
    private ExpressionNode signedFactor() throws ParseException {
        if (this.lookahead.token != 1) {
            return this.factor();
        }
        final boolean positive = this.lookahead.sequence.equals("+");
        this.nextToken();
        final ExpressionNode t = this.factor();
        if (positive) {
            return t;
        }
        return new AdditionExpressionNode(t, false);
    }
    
    private ExpressionNode factor() throws ParseException {
        final ExpressionNode a = this.argument();
        return this.factorOp(a);
    }
    
    private ExpressionNode factorOp(final ExpressionNode expression) throws ParseException {
        if (this.lookahead.token == 3) {
            this.nextToken();
            final ExpressionNode exponent = this.signedFactor();
            return new ExponentiationExpressionNode(expression, exponent);
        }
        return expression;
    }
    
    private ExpressionNode argument() throws ParseException {
        if (this.lookahead.token == 4) {
            final int function = FunctionExpressionNode.stringToFunction(this.lookahead.sequence);
            this.nextToken();
            final ExpressionNode expr = this.argument();
            return new FunctionExpressionNode(function, expr);
        }
        if (this.lookahead.token != 5) {
            return this.value();
        }
        this.nextToken();
        final ExpressionNode expr2 = this.expression();
        if (this.lookahead.token != 6) {
            throw new ParseException("Closing brackets expected: " + this.lookahead, 0);
        }
        this.nextToken();
        return expr2;
    }
    
    private ExpressionNode value() throws ParseException {
        if (this.lookahead.token == 7) {
            final ExpressionNode expr = new ConstantExpressionNode(this.lookahead.sequence);
            this.nextToken();
            return expr;
        }
        if (this.lookahead.token == 8) {
            final ExpressionNode expr = new VariableExpressionNode(this.lookahead.sequence);
            this.nextToken();
            return expr;
        }
        if (this.lookahead.token == 0) {
            throw new ParseException("Unexpected end of input", 0);
        }
        throw new ParseException("Unexpected symbol " + this.lookahead + " found", 0);
    }
}

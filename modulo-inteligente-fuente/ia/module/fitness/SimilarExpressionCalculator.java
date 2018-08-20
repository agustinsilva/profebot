package ia.module.fitness;

import ia.module.parser.tree.*;
import ia.module.parser.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class SimilarExpressionCalculator
{
    protected String originalExpression;
    
    public abstract Double similarityWith(final String p0);
    
    SimilarExpressionCalculator(final String expression) {
        this.originalExpression = expression;
    }
    
    protected List<Operator> getListOfTokensOfNormalizedExpression(final ExpressionNode expressionNode) {
        return this.removeDuplicates(expressionNode.normalize().getListOfTokens());
    }
    
    protected List<Operator> removeDuplicates(final List<Operator> operators) {
        final List<Operator> operatorsWithoutDuplicates = new ArrayList<Operator>();
        for (final Operator operator2 : operators) {
            if (operatorsWithoutDuplicates.stream().noneMatch(operator1 -> operator1.equals(operator2))) {
                operatorsWithoutDuplicates.add(new Operator(operator2.getOperator(), operator2.getDegree()));
            }
        }
        return this.chooseTermWithVariableByTermWithVariableDegree(operators, operatorsWithoutDuplicates, true);
    }
    
    private List<Operator> chooseTermWithVariableByTermWithVariableDegree(final List<Operator> reference, final List<Operator> result, final Boolean chooseMax) {
        final Integer maxDegree = reference.stream().filter(operator -> operator.getOperator().equals(12)).map((Function<? super Object, ? extends Integer>)Operator::getDegree).reduce(0, Math::max);
        if (chooseMax) {
            final Integer degreeToUse = maxDegree;
        }
        else {
            final Integer degreeToUse = reference.stream().filter(operator -> operator.getOperator().equals(12)).map((Function<? super Object, ? extends Integer>)Operator::getDegree).reduce(maxDegree, Math::min);
        }
        final Integer degree;
        final Operator operator2;
        return result.stream().map(operator -> {
            if (operator.getOperator().equals(12)) {
                // new(ia.module.parser.Operator.class)
                new Operator(operator.getOperator(), degree);
            }
            else {
                operator2 = operator;
            }
            return operator2;
        }).collect((Collector<? super Object, ?, List<Operator>>)Collectors.toList());
    }
}

package ia.module.fitness;

import ia.module.parser.tree.*;
import java.util.stream.*;
import java.util.*;
import ia.module.parser.*;

public class ProceduralSimilarExpressionCalculator extends SimilarExpressionCalculator
{
    private ExpressionNode originalExpressionTree;
    private Parser parser;
    
    @Override
    public Double similarityWith(final String candidateExpression) {
        try {
            return this.getSimilarity(this.originalExpressionTree, this.parser.parse(candidateExpression));
        }
        catch (Exception e) {
            System.out.println();
            return 0.0;
        }
    }
    
    public Double getSimilarity(final ExpressionNode originalExpression, final ExpressionNode candidateExpressionTree) {
        final Double levelSimilarity = this.getLevelSimilarityBetween(originalExpression, candidateExpressionTree);
        final Double structureSimilarity = this.getStructureSimilarityBetween(originalExpression, candidateExpressionTree);
        final Double complexitySimilarity = this.getComplexitySimilarity(originalExpression, candidateExpressionTree);
        final Double syntacticSimilarity = this.getSyntacticSimilarity(originalExpression, candidateExpressionTree);
        return levelSimilarity * structureSimilarity * complexitySimilarity * syntacticSimilarity;
    }
    
    public Double getSyntacticSimilarity(final ExpressionNode originalExpressionTree, final ExpressionNode candidateExpressionTree) {
        final List<Operator> originalExpressionTokens = this.ignoreNumberValues(originalExpressionTree.getListOfTokens());
        final List<Operator> candidateExpressionTokens = this.ignoreNumberValues(candidateExpressionTree.getListOfTokens());
        if (originalExpressionTokens.size() == candidateExpressionTokens.size() && originalExpressionTokens.stream().allMatch(operator -> candidateExpressionTokens.stream().anyMatch(candidateOperator -> candidateOperator.totallyEquals(operator)))) {
            return 0.0;
        }
        return 1.0;
    }
    
    public List<Operator> ignoreNumberValues(final List<Operator> operators) {
        final Operator operator2;
        return operators.stream().map(operator -> {
            if (operator.getOperator().equals(0)) {
                // new(ia.module.parser.Operator.class)
                new Operator(operator.getOperator(), 0);
            }
            else {
                operator2 = operator;
            }
            return operator2;
        }).collect((Collector<? super Object, ?, List<Operator>>)Collectors.toList());
    }
    
    public Double getComplexitySimilarity(final ExpressionNode originalExpressionTree, final ExpressionNode candidateExpressionTree) {
        final List<Operator> originalExpressionTokens = this.getListOfTokensOfNormalizedExpression(originalExpressionTree);
        final List<Operator> candidateExpressionTokens = this.getListOfTokensOfNormalizedExpression(candidateExpressionTree);
        final List<Operator> intersection = this.getIntersection(originalExpressionTokens, candidateExpressionTokens);
        final List<Operator> union = this.getUnion(originalExpressionTokens, candidateExpressionTokens);
        return (Double)(this.getWeight(intersection) / this.getWeight(union));
    }
    
    private Long getWeight(final List<Operator> operators) {
        return operators.stream().map(operator -> operator.getFibonacciWeight()).reduce(0L, (total, nextFibonacciValue) -> total + nextFibonacciValue);
    }
    
    private List<Operator> getIntersection(final List<Operator> originalExpressionTokens, final List<Operator> candidateExpressionTokens) {
        final Operator operator;
        return originalExpressionTokens.stream().filter(token -> candidateExpressionTokens.stream().anyMatch(token1 -> token1.equals(token))).map(token -> {
            if (token.getOperator().equals(12)) {
                // new(ia.module.parser.Operator.class)
                new Operator(12, this.getIntersectionOfTermWithXByTermWithXDegrees(originalExpressionTokens, candidateExpressionTokens));
            }
            else {
                operator = token;
            }
            return operator;
        }).collect((Collector<? super Object, ?, List<Operator>>)Collectors.toList());
    }
    
    private Integer getIntersectionOfTermWithXByTermWithXDegrees(final List<Operator> originalExpressionTokens, final List<Operator> candidateExpressionTokens) {
        final Integer originalExpressionMaxDegree = this.maxDegreeOf(originalExpressionTokens);
        final Integer candidateExpressionMaxDegree = this.maxDegreeOf(candidateExpressionTokens);
        return Math.min(originalExpressionMaxDegree, candidateExpressionMaxDegree);
    }
    
    private Integer maxDegreeOf(final List<Operator> operators) {
        return operators.stream().map(token -> token.getOperator().equals(12) ? token.getDegree() : 0).reduce(0, (max, nextDegree) -> Math.max(max, nextDegree));
    }
    
    private List<Operator> getUnion(final List<Operator> originalExpressionTokens, final List<Operator> candidateExpressionTokens) {
        final List<Operator> union = new ArrayList<Operator>();
        union.addAll(originalExpressionTokens);
        union.addAll(candidateExpressionTokens);
        return this.removeDuplicates(union);
    }
    
    public Double getStructureSimilarityBetween(final ExpressionNode originalExpressionTree, final ExpressionNode candidateExpressionTree) {
        final ExpressionsWithArgumentStructures originalExpressionsStructures = originalExpressionTree.getStructureOf(new ExpressionsWithArgumentStructures());
        final ExpressionsWithArgumentStructures candidateExpressionsStructures = candidateExpressionTree.getStructureOf(new ExpressionsWithArgumentStructures());
        final Double structureExistenceSimilarity = this.getStructureExistenceSimilarity(originalExpressionsStructures, candidateExpressionsStructures);
        final Double structuresCountSimilarity = this.getStructuresCountSimilarity(originalExpressionsStructures, candidateExpressionsStructures);
        return structureExistenceSimilarity * structuresCountSimilarity;
    }
    
    private Double getStructureExistenceSimilarity(final ExpressionsWithArgumentStructures originalExpressionsStructures, final ExpressionsWithArgumentStructures candidateExpressionsStructures) {
        return this.toRatio(originalExpressionsStructures.getCountOfStructuresNotIncludedInto(candidateExpressionsStructures));
    }
    
    private Double getStructuresCountSimilarity(final ExpressionsWithArgumentStructures originalExpressionsStructures, final ExpressionsWithArgumentStructures candidateExpressionsStructures) {
        return this.toRatio(candidateExpressionsStructures.getCountOfStructuresWithCountOfOccurrencesOutOfBoundOf(originalExpressionsStructures));
    }
    
    private Double toRatio(final Long value) {
        return 1.0 / (1.0 + value);
    }
    
    public Double getLevelSimilarityBetween(final ExpressionNode originalExpressionTree, final ExpressionNode candidateExpressionTree) {
        final Integer originalExpressionTreeLevel = originalExpressionTree.getLevel();
        final Integer candidateExpressionTreeLevel = candidateExpressionTree.getLevel();
        final Integer difference = Math.abs(originalExpressionTreeLevel - candidateExpressionTreeLevel);
        return (10.0 - difference) / 10.0;
    }
    
    public ProceduralSimilarExpressionCalculator(final String originalExpression) {
        super(originalExpression);
        this.parser = new Parser();
        try {
            this.originalExpressionTree = this.parser.parse(originalExpression);
        }
        catch (Exception e) {
            System.out.println("Expresi\u00f3n patr\u00f3n inv\u00e1lida: " + this.originalExpression + " Excepti\u00f3n: " + e);
        }
    }
    
    public ProceduralSimilarExpressionCalculator() {
        super(null);
        this.parser = new Parser();
    }
}

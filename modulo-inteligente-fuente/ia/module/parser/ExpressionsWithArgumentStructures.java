package ia.module.parser;

import java.util.*;
import java.util.stream.*;

public class ExpressionsWithArgumentStructures
{
    private List<ExpressionWithArgumentStructure> expressionsWithArgument;
    
    public ExpressionsWithArgumentStructures() {
        this.expressionsWithArgument = new ArrayList<ExpressionWithArgumentStructure>();
    }
    
    public ExpressionsWithArgumentStructures addExpressionWithArguments(final Integer dadToken, final Integer childToken) {
        final List<ExpressionWithArgumentStructure> expressions = this.expressionsWithArgument.stream().filter(expressionWithArgumentStructure -> dadToken.equals(expressionWithArgumentStructure.getDadToken()) && childToken.equals(expressionWithArgumentStructure.getChildToken())).collect((Collector<? super Object, ?, List<ExpressionWithArgumentStructure>>)Collectors.toList());
        if (expressions.isEmpty()) {
            this.expressionsWithArgument.add(new ExpressionWithArgumentStructure(dadToken, childToken));
        }
        else {
            expressions.get(0).incrementCountOfOccurrences();
        }
        return this;
    }
    
    public Long getCountOfStructuresNotIncludedInto(final ExpressionsWithArgumentStructures structures) {
        return (long)this.expressionsWithArgument.stream().filter(structure -> !structures.has(structure)).map(structure -> structure.getDiffOfOccurrences(structures)).reduce(0, (total, diff) -> total + diff);
    }
    
    public Long getCountOfStructuresWithCountOfOccurrencesOutOfBoundOf(final ExpressionsWithArgumentStructures structures) {
        return (long)this.expressionsWithArgument.stream().filter(structure -> structure.hasOccurrencesOutOfBoundsOf(structures)).map(structure -> structure.getDiffOfOccurrences(structures)).reduce(0, (total, diff) -> total + diff);
    }
    
    public Boolean has(final ExpressionWithArgumentStructure expressionWithArgumentStructure) {
        return this.expressionsWithArgument.stream().anyMatch(structure -> (structure.getDadToken().equals(expressionWithArgumentStructure.getDadToken()) && structure.getChildToken().equals(expressionWithArgumentStructure.getChildToken())) || this.haveEquivalentTokens(structure, expressionWithArgumentStructure));
    }
    
    public ExpressionWithArgumentStructure find(final ExpressionWithArgumentStructure expressionWithArgumentStructure) {
        final List<ExpressionWithArgumentStructure> structures = this.expressionsWithArgument.stream().filter(structure -> (structure.getDadToken().equals(expressionWithArgumentStructure.getDadToken()) && structure.getChildToken().equals(expressionWithArgumentStructure.getChildToken())) || this.haveEquivalentTokens(structure, expressionWithArgumentStructure)).collect((Collector<? super Object, ?, List<ExpressionWithArgumentStructure>>)Collectors.toList());
        if (structures.isEmpty()) {
            return null;
        }
        return structures.get(0);
    }
    
    private Boolean haveEquivalentTokens(final ExpressionWithArgumentStructure structure1, final ExpressionWithArgumentStructure structure2) {
        return this.hasEquivalentToken(structure1) && this.hasEquivalentToken(structure2);
    }
    
    private Boolean hasEquivalentToken(final ExpressionWithArgumentStructure structure) {
        return Operator.equivalentTokens().contains(structure.getDadToken()) && Operator.equivalentTokens().contains(structure.getChildToken());
    }
}

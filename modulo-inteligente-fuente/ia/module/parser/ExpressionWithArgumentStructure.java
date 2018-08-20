package ia.module.parser;

public class ExpressionWithArgumentStructure
{
    private Integer dadToken;
    private Integer childToken;
    private Integer countOfOccurrences;
    
    public ExpressionWithArgumentStructure(final Integer dadToken, final Integer childToken) {
        this.dadToken = dadToken;
        this.childToken = childToken;
        this.countOfOccurrences = 1;
    }
    
    public Integer getDadToken() {
        return this.dadToken;
    }
    
    public Integer getChildToken() {
        return this.childToken;
    }
    
    public Integer getCountOfOccurrences() {
        return this.countOfOccurrences;
    }
    
    public void incrementCountOfOccurrences() {
        ++this.countOfOccurrences;
    }
    
    public Boolean hasOccurrencesOutOfBoundsOf(final ExpressionsWithArgumentStructures structures) {
        final ExpressionWithArgumentStructure expressionWithArgumentStructureFound = structures.find(this);
        if (expressionWithArgumentStructureFound == null) {
            return true;
        }
        return this.countOfOccurrences > expressionWithArgumentStructureFound.getCountOfOccurrences();
    }
    
    public Integer getDiffOfOccurrences(final ExpressionsWithArgumentStructures structures) {
        final ExpressionWithArgumentStructure expressionWithArgumentStructureFound = structures.find(this);
        if (expressionWithArgumentStructureFound == null) {
            return this.countOfOccurrences;
        }
        return Math.abs(this.countOfOccurrences - expressionWithArgumentStructureFound.getCountOfOccurrences());
    }
}

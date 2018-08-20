package ia.module.parser.tree;

import java.util.function.*;
import ia.module.parser.*;
import java.util.stream.*;
import java.util.*;

public abstract class SequenceExpressionNode extends AbstractExpressionNode implements ExpressionNode
{
    protected List<Term> terms;
    
    public SequenceExpressionNode() {
        this.terms = new LinkedList<Term>();
    }
    
    public SequenceExpressionNode(final ExpressionNode a, final boolean positive) {
        (this.terms = new LinkedList<Term>()).add(new Term(positive, a));
    }
    
    public void add(final ExpressionNode a, final boolean positive) {
        this.terms.add(new Term(positive, a));
    }
    
    @Override
    public Boolean hasVariable() {
        return this.terms.stream().anyMatch(Term::hasVariable);
    }
    
    @Override
    public Boolean isNumber() {
        return this.terms.stream().allMatch(Term::isNumber);
    }
    
    @Override
    public Boolean isPositiveNumber() {
        try {
            return this.isNumber() && this.getValue() >= 0.0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean isEven() {
        try {
            return this.isNumber() && this.getValue() % 2.0 == 0.0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public Integer maxLevelAgainst(final Integer level) {
        return this.terms.stream().map((Function<? super Object, ? extends Integer>)Term::getLevel).reduce(level, Math::max);
    }
    
    protected Integer getLevelFromBases(final Integer lowerBase, final Integer upperBase) {
        final Boolean existsTermWithVariable = this.terms.stream().anyMatch(Term::hasVariable);
        final Integer base = existsTermWithVariable ? upperBase : lowerBase;
        return this.maxLevelAgainst(base);
    }
    
    private Boolean is(final Integer number) {
        if (this.hasVariable()) {
            return false;
        }
        try {
            return this.getValue() == number;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private Boolean isGreaterThan(final Integer number, final Boolean positive) {
        if (this.hasVariable()) {
            return false;
        }
        try {
            final Double value = this.getValue();
            return Math.abs(value) >= number && (positive ? (value >= 0.0) : (value < 0.0));
        }
        catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean isMinusOne() {
        return this.is(-1);
    }
    
    @Override
    public Boolean isMinusN() {
        return this.isGreaterThan(2, false);
    }
    
    @Override
    public Boolean isOne() {
        return this.is(1);
    }
    
    @Override
    public Boolean isTwo() {
        return this.is(2);
    }
    
    @Override
    public Boolean isZero() {
        return this.is(0);
    }
    
    @Override
    public Boolean isN() {
        return this.isGreaterThan(2, true);
    }
    
    @Override
    public Boolean isFractionalNumber() {
        if (this.hasVariable()) {
            return false;
        }
        try {
            final Double value = this.getValue();
            return Math.abs(value - (int)(Object)value) > 0.0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected Boolean allPositives() {
        return this.terms.stream().allMatch(term -> term.positive);
    }
    
    @Override
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        for (final Term term : this.terms) {
            term.getStructureOf(expressionsWithArgumentStructures);
        }
        return expressionsWithArgumentStructures;
    }
    
    @Override
    public List<Operator> getListOfTokens() {
        final List<Operator> tokens = new ArrayList<Operator>();
        tokens.add(Operator.newToken(this.getToken(), this.getDegree()));
        for (final Term term : this.terms) {
            tokens.addAll(term.getListOfTokens());
        }
        return tokens;
    }
    
    @Override
    public ExpressionNode normalize() {
        this.terms = this.terms.stream().map(term -> new Term(term.positive, term.normalize())).collect((Collector<? super Object, ?, List<Term>>)Collectors.toList());
        return this;
    }
    
    @Override
    public Boolean contains(final Integer operator) {
        return this.getToken().equals(operator) || this.terms.stream().anyMatch(term -> term.contains(operator));
    }
    
    @Override
    public ExpressionNode simplify() {
        if (this.isNumber()) {
            try {
                return new ConstantExpressionNode(this.getValue());
            }
            catch (Exception e) {
                return this.simplifySequenceWithVariables();
            }
        }
        return this.simplifySequenceWithVariables();
    }
    
    private ExpressionNode simplifySequenceWithVariables() {
        final List<Term> termsSimplified = this.terms.stream().map((Function<? super Object, ?>)Term::simplify).collect((Collector<? super Object, ?, List<Term>>)Collectors.toList());
        final Map<String, Map<Term, Integer>> countOfTermsBySign = new HashMap<String, Map<Term, Integer>>();
        for (final Term term : termsSimplified) {
            final Map<Term, Integer> value = new HashMap<Term, Integer>();
            Integer count = 0;
            if (countOfTermsBySign.containsKey(term.getListOfTokens().toString())) {
                final Term termToTake = countOfTermsBySign.get(term.getListOfTokens().toString()).keySet().stream().findFirst().get();
                count = countOfTermsBySign.get(term.getListOfTokens().toString()).get(termToTake);
                countOfTermsBySign.remove(term.getListOfTokens().toString());
            }
            value.put(term, count + (term.positive ? 1 : -1));
            countOfTermsBySign.put(term.getListOfTokens().toString(), value);
        }
        final List<Term> newTerms = new ArrayList<Term>();
        for (final String tokensKey : countOfTermsBySign.keySet()) {
            final Term newTerm = countOfTermsBySign.get(tokensKey).keySet().stream().findFirst().get();
            if (countOfTermsBySign.get(tokensKey).get(newTerm) != 0) {
                newTerms.add(newTerm);
            }
        }
        return this.newSequenceWithTerms(newTerms);
    }
    
    public abstract SequenceExpressionNode newSequenceWithTerms(final List<Term> p0);
}

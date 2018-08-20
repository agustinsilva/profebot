package ia.module.parser.tree;

import java.util.*;
import com.sun.tools.corba.se.idl.constExpr.*;
import java.util.function.*;

public class MultiplicationExpressionNode extends SequenceExpressionNode
{
    public MultiplicationExpressionNode(final ExpressionNode a, final boolean positive) {
        super(a, positive);
    }
    
    public MultiplicationExpressionNode(final List<Term> terms) {
        this.terms = terms;
    }
    
    @Override
    public int getType() {
        return 4;
    }
    
    @Override
    public double getValue() throws EvaluationException {
        double prod = 1.0;
        for (final Term t : this.terms) {
            if (t.positive) {
                prod *= t.expression.getValue();
            }
            else {
                prod /= t.expression.getValue();
            }
        }
        return prod;
    }
    
    @Override
    public Integer getDegree() {
        return this.terms.stream().map((Function<? super Object, ? extends Integer>)Term::getDegree).reduce(0, (total, aDegree) -> total + aDegree);
    }
    
    @Override
    public Integer getLevel() {
        return this.getLevelFromBases(1, 4);
    }
    
    @Override
    public Integer getToken() {
        if (!this.hasVariable()) {
            if (this.allPositives()) {
                return 3;
            }
            return 5;
        }
        else {
            if (this.terms.size() == 2 && this.onlyOneTermIsVariable()) {
                return 8;
            }
            if (this.onlyOneTermIsVariable() || this.onlyOneTermIsLineal()) {
                return 4;
            }
            if (this.onlyOneTermHasVariableAsFactor() && !this.onlyOneTermHasVariableAsDividend()) {
                return 10;
            }
            if (this.onlyOneTermHasVariableAsDividend()) {
                return 11;
            }
            if (this.towOrMoreTermsWithVariableAsFactors()) {
                return 12;
            }
            if (this.anyTermWithVariableAsQuotient()) {
                return 13;
            }
            return 0;
        }
    }
    
    private Boolean onlyOneTermIsVariable() {
        return this.terms.stream().filter(Term::hasVariable).count() == 1L && this.terms.stream().filter(term -> term.isVariable() && term.positive).count() == 1L;
    }
    
    private Boolean onlyOneTermIsLineal() {
        return this.terms.stream().filter(Term::hasVariable).count() == 1L && this.terms.stream().filter(term -> term.hasVariable() && term.isLineal() && term.positive).count() == 1L;
    }
    
    private Long countOfTermsWithVariableAsFactor() {
        return this.terms.stream().filter(term -> term.hasVariable() && term.positive).count();
    }
    
    private Boolean onlyOneTermHasVariableAsFactor() {
        return this.countOfTermsWithVariableAsFactor() == 1L && this.terms.stream().filter(term -> term.hasVariable() && !term.positive).count() == 0L;
    }
    
    private Boolean onlyOneTermHasVariableAsDividend() {
        return this.onlyOneTermHasVariableAsFactor() && this.terms.stream().anyMatch(term -> (term.isNumber() && !term.positive) || term.isFractionalNumber());
    }
    
    @Override
    public Boolean isQuadraticX() {
        return (this.terms.stream().filter(Term::hasVariable).count() == 1L && this.terms.stream().filter(Term::isQuadraticX).count() == 1L) || (this.terms.stream().filter(Term::hasVariable).count() == 2L && this.terms.stream().filter(Term::isVariable).count() == 2L);
    }
    
    private Boolean towOrMoreTermsWithVariableAsFactors() {
        return this.countOfTermsWithVariableAsFactor() >= 2L;
    }
    
    private Boolean anyTermWithVariableAsQuotient() {
        return this.terms.stream().filter(term -> term.hasVariable() && !term.positive).count() >= 1L;
    }
    
    @Override
    public Boolean isLineal() {
        return this.terms.stream().filter(Term::hasVariable).count() <= 1L && this.terms.stream().filter(term -> term.hasVariable() && term.isLineal()).count() == 1L;
    }
    
    @Override
    public Boolean isZero() {
        return this.terms.stream().anyMatch(Term::isZero);
    }
    
    @Override
    public MultiplicationExpressionNode newSequenceWithTerms(final List<Term> terms) {
        return new MultiplicationExpressionNode(terms);
    }
}

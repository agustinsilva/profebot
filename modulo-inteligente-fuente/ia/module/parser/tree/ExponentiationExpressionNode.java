package ia.module.parser.tree;

import com.sun.tools.corba.se.idl.constExpr.*;
import ia.module.parser.*;
import java.util.*;

public class ExponentiationExpressionNode extends AbstractExpressionNode implements ExpressionNode
{
    private ExpressionNode base;
    private ExpressionNode exponent;
    
    public ExponentiationExpressionNode(final ExpressionNode base, final ExpressionNode exponent) {
        this.base = base;
        this.exponent = exponent;
    }
    
    @Override
    public int getType() {
        return 5;
    }
    
    @Override
    public double getValue() throws EvaluationException {
        return Math.pow(this.base.getValue(), this.exponent.getValue());
    }
    
    @Override
    public Boolean hasVariable() {
        return this.base.hasVariable() || this.exponent.hasVariable();
    }
    
    @Override
    public Boolean isNumber() {
        return this.base.isNumber() && this.exponent.isNumber();
    }
    
    @Override
    public Boolean isPositiveNumber() {
        return this.base.isPositiveNumber() || this.exponent.isEven();
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
    
    @Override
    public Integer getLevel() {
        final Boolean baseHasVariable = this.base.hasVariable();
        final Boolean exponentHasVariable = this.exponent.hasVariable();
        Integer minLevel = 7;
        if (!baseHasVariable && !exponentHasVariable) {
            minLevel = 2;
        }
        if (baseHasVariable && !exponentHasVariable) {
            minLevel = 5;
        }
        return Math.max(minLevel, Math.max(this.base.getLevel(), this.exponent.getLevel()));
    }
    
    @Override
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        return expressionsWithArgumentStructures;
    }
    
    @Override
    public Integer getToken() {
        final Boolean baseHasVariable = this.base.hasVariable();
        final Boolean baseIsNumber = this.base.isNumber();
        final Boolean exponentHasVariable = this.exponent.hasVariable();
        final Boolean exponentIsPositiveNumber = this.exponent.isPositiveNumber();
        final Boolean exponentIsMinusOne = this.exponent.isMinusOne();
        final Boolean exponentIsMinusN = this.exponent.isMinusN();
        final Boolean exponentIsFractionalNumber = this.exponent.isFractionalNumber();
        if (baseIsNumber && exponentIsPositiveNumber && !exponentIsFractionalNumber) {
            return 3;
        }
        if (baseIsNumber && (exponentIsMinusN || exponentIsMinusOne)) {
            return 6;
        }
        if (baseIsNumber && exponentIsFractionalNumber) {
            return 7;
        }
        if (baseHasVariable && exponentIsPositiveNumber) {
            return 12;
        }
        if (baseHasVariable && exponentIsMinusOne) {
            return 14;
        }
        if (baseHasVariable && exponentIsMinusN) {
            return 15;
        }
        if (baseHasVariable && exponentIsFractionalNumber) {
            return 16;
        }
        if (exponentHasVariable) {
            return 18;
        }
        return 0;
    }
    
    @Override
    public Boolean isLineal() {
        return (this.base.isNumber() && this.exponent.isNumber()) || (this.base.isLineal() && this.exponent.isOne());
    }
    
    @Override
    public Boolean isQuadraticX() {
        return this.base.isVariable() && this.exponent.isTwo();
    }
    
    @Override
    public Boolean isZero() {
        return this.base.isZero() && this.exponent.isPositiveNumber();
    }
    
    @Override
    public ExpressionNode normalize() {
        if (this.base.hasVariable() && this.exponent.isPositiveNumber() && !this.exponent.isFractionalNumber()) {
            try {
                final Integer exponentValue = (int)this.exponent.getValue();
                if (exponentValue != 0) {
                    final MultiplicationExpressionNode multiplication = new MultiplicationExpressionNode(this.base, true);
                    for (int i = 2; i <= exponentValue; ++i) {
                        multiplication.add(this.base, true);
                    }
                    return multiplication;
                }
                return new ConstantExpressionNode(1.0);
            }
            catch (Exception e) {
                this.base = this.base.normalize();
                this.exponent = this.exponent.normalize();
                return this;
            }
        }
        this.base = this.base.normalize();
        this.exponent = this.exponent.normalize();
        return this;
    }
    
    @Override
    public List<Operator> getListOfTokens() {
        final List<Operator> tokens = new ArrayList<Operator>();
        tokens.add(Operator.newToken(this.getToken(), this.getDegree()));
        tokens.addAll(this.base.getListOfTokens());
        tokens.addAll(this.exponent.getListOfTokens());
        return tokens;
    }
    
    @Override
    public Boolean contains(final Integer operator) {
        return this.base.contains(operator) || this.exponent.contains(operator);
    }
    
    @Override
    public Integer getDegree() {
        final Integer baseDegree = this.base.hasVariable() ? this.base.getDegree() : 0;
        Integer exponentDegree;
        try {
            exponentDegree = (this.exponent.hasVariable() ? this.exponent.getDegree() : ((int)this.exponent.getValue()));
        }
        catch (Exception e) {
            exponentDegree = 1;
        }
        return baseDegree * exponentDegree;
    }
    
    @Override
    public ExpressionNode simplify() {
        final ExpressionNode baseSimplified = this.base.simplify();
        final ExpressionNode exponentSimplified = this.exponent.simplify();
        if (exponentSimplified.isZero()) {
            return new ConstantExpressionNode(1.0);
        }
        return new ExponentiationExpressionNode(baseSimplified, exponentSimplified);
    }
}

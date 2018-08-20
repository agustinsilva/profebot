package ia.module.parser.tree;

import ia.module.parser.*;
import java.util.*;

public class ConstantExpressionNode extends AbstractExpressionNode implements ExpressionNode
{
    private Double value;
    
    public ConstantExpressionNode(final double value) {
        this.value = value;
    }
    
    public ConstantExpressionNode(final String value) {
        this.value = Double.valueOf(value);
    }
    
    @Override
    public double getValue() {
        return this.value;
    }
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public Integer getLevel() {
        return 0;
    }
    
    @Override
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        return expressionsWithArgumentStructures;
    }
    
    @Override
    public Integer getToken() {
        return 0;
    }
    
    @Override
    public Boolean isNumber() {
        return true;
    }
    
    @Override
    public Boolean isTwo() {
        return this.value == 2.0;
    }
    
    @Override
    public Boolean isOne() {
        return this.value == 1.0;
    }
    
    @Override
    public Boolean isZero() {
        return this.value == 0.0;
    }
    
    @Override
    public Boolean isN() {
        return this.value >= 2.0;
    }
    
    @Override
    public Boolean isFractionalNumber() {
        return this.value - (int)(Object)this.value > 0.0;
    }
    
    @Override
    public Boolean isPositiveNumber() {
        return this.value > 0.0;
    }
    
    @Override
    public Boolean isEven() {
        return this.value % 2.0 == 0.0;
    }
    
    @Override
    public Boolean isLineal() {
        return true;
    }
    
    @Override
    public ExpressionNode normalize() {
        return this;
    }
    
    @Override
    public List<Operator> getListOfTokens() {
        final List<Operator> tokens = new ArrayList<Operator>();
        tokens.add(Operator.newToken(this.getToken(), (int)(Object)this.value));
        return tokens;
    }
    
    @Override
    public ExpressionNode simplify() {
        return new ConstantExpressionNode(this.value);
    }
}

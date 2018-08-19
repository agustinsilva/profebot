package ia.module.parser.tree;

import com.sun.tools.corba.se.idl.constExpr.*;
import ia.module.parser.*;
import java.util.*;

public class VariableExpressionNode extends AbstractExpressionNode implements ExpressionNode
{
    private String name;
    private double value;
    private boolean valueSet;
    
    public VariableExpressionNode(final String name) {
        this.name = name;
        this.valueSet = false;
    }
    
    @Override
    public int getType() {
        return 1;
    }
    
    public void setValue(final double value) {
        this.value = value;
        this.valueSet = true;
    }
    
    @Override
    public double getValue() throws EvaluationException {
        if (this.valueSet) {
            return this.value;
        }
        throw new EvaluationException("Variable '" + this.name + "' was not initialized.");
    }
    
    @Override
    public Integer getLevel() {
        return 3;
    }
    
    @Override
    public Boolean hasVariable() {
        return true;
    }
    
    @Override
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        return expressionsWithArgumentStructures;
    }
    
    @Override
    public Integer getToken() {
        return 8;
    }
    
    @Override
    public Boolean isVariable() {
        return true;
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
    public Integer getDegree() {
        return 1;
    }
    
    @Override
    public List<Operator> getListOfTokens() {
        final List<Operator> tokens = new ArrayList<Operator>();
        tokens.add(Operator.newToken(this.getToken(), this.getDegree()));
        return tokens;
    }
    
    @Override
    public ExpressionNode simplify() {
        return new VariableExpressionNode(this.name);
    }
}

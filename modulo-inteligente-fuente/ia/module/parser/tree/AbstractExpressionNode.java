package ia.module.parser.tree;

import ia.module.parser.*;
import ia.module.config.*;
import java.util.*;

public abstract class AbstractExpressionNode
{
    public Boolean isNumber() {
        return false;
    }
    
    public Boolean isVariable() {
        return false;
    }
    
    public Boolean hasVariable() {
        return false;
    }
    
    public Boolean isMinusOne() {
        return false;
    }
    
    public Boolean isMinusN() {
        return false;
    }
    
    public Boolean isOne() {
        return false;
    }
    
    public Boolean isTwo() {
        return false;
    }
    
    public Boolean isN() {
        return false;
    }
    
    public Boolean isFractionalNumber() {
        return false;
    }
    
    public Boolean isPositiveNumber() {
        return false;
    }
    
    public Boolean isEven() {
        return false;
    }
    
    public Boolean isZero() {
        return false;
    }
    
    public Boolean isQuadraticX() {
        return false;
    }
    
    public abstract ExpressionNode normalize();
    
    public ExpressionsWithArgumentStructures getStructureOf(final ExpressionsWithArgumentStructures expressionsWithArgumentStructures) {
        return expressionsWithArgumentStructures;
    }
    
    public Integer getDegree() {
        return 0;
    }
    
    public Boolean contains(final Integer operator) {
        return false;
    }
    
    public abstract List<Operator> getListOfTokens();
    
    public double[] extractFeaturesForExpression() {
        double[] features = new double[NeuralNetworkConfig.INPUTS];
        for (final Operator operator : this.normalize().getListOfTokens()) {
            final Integer index = (operator.getOperator() > 10) ? (operator.getOperator() - 1) : operator.getOperator();
            features = this.incrementFeaturesUntil(features, index);
        }
        return this.normalizeFeatures(features);
    }
    
    private double[] incrementFeaturesUntil(final double[] features, final Integer index) {
        for (int i = 0; i <= index; ++i) {
            final int n = i;
            ++features[n];
        }
        return features;
    }
    
    private double[] normalizeFeatures(final double[] features) {
        final Long maxOrder = this.getMaxOrder(features);
        return Arrays.stream(features).map(feature -> feature / maxOrder).toArray();
    }
    
    private Long getMaxOrder(final double[] features) {
        Double max;
        Long quotient;
        for (max = Arrays.stream(features).reduce(0.0, Math::max), quotient = 1L; max / quotient >= 1.0; quotient *= 10L) {}
        return quotient;
    }
}

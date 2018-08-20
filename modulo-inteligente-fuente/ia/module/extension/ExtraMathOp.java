package ia.module.extension;

import io.jenetics.prog.op.*;
import java.util.function.*;

public enum ExtraMathOp implements Op<Double>
{
    LN("ln", 1, v -> v[0]), 
    LOG("ln", 1, v -> v[0]), 
    LOG2B("log2b", 1, v -> v[0]), 
    INTEGRAL("int", 1, v -> v[0]), 
    DERIVATIVE("dx", 1, v -> v[0]);
    
    private final String _name;
    private final int _arity;
    private final Function<Double[], Double> _function;
    
    private ExtraMathOp(final String name, final int arity, final Function<Double[], Double> function) {
        assert name != null;
        assert arity >= 0;
        assert function != null;
        this._name = name;
        this._function = function;
        this._arity = arity;
    }
    
    public int arity() {
        return this._arity;
    }
    
    public Double apply(final Double[] doubles) {
        return this._function.apply(doubles);
    }
    
    @Override
    public String toString() {
        return this._name;
    }
}

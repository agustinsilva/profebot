package ia.module.config;

import io.jenetics.util.*;
import ia.module.extension.*;
import io.jenetics.prog.op.*;

public class ExpressionsConfig
{
    public static final int TREE_MAX_DEPTH = 3;
    public static final ISeq<Op<Double>> OPERATIONS;
    public static final Op<Double> VAR_X;
    public static final ISeq<Op<Double>> TERMINALS;
    
    public static final Op<Double> anyNumber() {
        return (Op<Double>)EphemeralConst.of(() -> RandomRegistry.getRandom().nextInt(10) + 1.0);
    }
    
    static {
        OPERATIONS = ISeq.of((Object[])new Op[] { MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV, MathOp.POW, MathOp.SQRT, MathOp.COS, MathOp.SIN, MathOp.TAN, ExtraMathOp.LN, ExtraMathOp.INTEGRAL, ExtraMathOp.DERIVATIVE, ExtraMathOp.LN, ExtraMathOp.LOG, ExtraMathOp.LOG2B });
        VAR_X = (Op)Var.of("x", 0);
        TERMINALS = ISeq.of((Object[])new Op[] { ExpressionsConfig.VAR_X, anyNumber() });
    }
}

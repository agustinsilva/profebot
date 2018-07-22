package ar.com.profebot.resolutor.service;

import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.NodeStatus;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class SimplifyService {

    private static final Integer CONSTANT_1  = 1;
    private static final Integer CONSTANT_1_NEG  = -1;
    private static final Integer CONSTANT_0  = 0;

    /**
     // Given an expression node, steps through simplifying the expression.
     // Returns a list of details about each step.
     * @param node Nodo a evaluar
     * @return Lista de pasos
     */
    public List<NodeStatus> stepThrough(TreeNode node) {

        List<NodeStatus> steps = new ArrayList<>();
        final Integer MAX_STEP_COUNT = 20;
        Integer iters = 0;

        String originalExpressionStr = node.toExpression();
        Log.d("debugTag","\n\nSimplifying: " + originalExpressionStr);

        // Now, step through the math expression until nothing changes
        NodeStatus nodeStatus = step(node);
        while (nodeStatus.hasChanged()) {
            logSteps(nodeStatus);

            steps.add(nodeStatus);

            node = NodeStatus.resetChangeGroups(nodeStatus.getNewNode());
            nodeStatus = step(node);

            if (MAX_STEP_COUNT.equals(iters++)) {
                // eslint-disable-next-line
                Log.e("errorTag","Math error: Potential infinite loop for expression: " +
                        originalExpressionStr + ", returning no steps");
                return new ArrayList<>();
            }
        }

        return steps;
    }

    /**
     // Given a expression node, performs a single step to simplify the
     // expression. Returns a Node.Status object.
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus step(TreeNode node) {

        NodeStatus nodeStatus;

        node = TreeUtils.flattenOperands(node);

        // Basic simplifications that we always try first e.g. (...)^0 => 1
        nodeStatus = basicSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // Simplify any division chains so there's at most one division operation.
        // e.g. 2/x/6 -> 2/(x*6)        e.g. 2/(x/6) => 2 * 6/x
        nodeStatus = divisionSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // Adding fractions, cancelling out things in fractions
        nodeStatus = fractionsSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. addition of polynomial terms: 2x + 4x^2 + x => 4x^2 + 3x
        // e.g. multiplication of polynomial terms: 2x * x * x^2 => 2x^3
        // e.g. multiplication of constants: 10^3 * 10^2 => 10^5
        nodeStatus = collectAndCombineSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. 2 + 2 => 4
        nodeStatus = arithmeticSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. (2 + x) / 4 => 2/4 + x/4
        nodeStatus = breakUpNumeratorSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. 3/x * 2x/5 => (3 * 2x) / (x * 5)
        nodeStatus = multiplyFractionsSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. (2x + 3)(x + 4) => 2x^2 + 11x + 12
        nodeStatus = distributeSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        // e.g. abs(-4) => 4
        nodeStatus = functionsSearch(node);
        node = TreeUtils.flattenOperands(nodeStatus.getNewNode());
        if (nodeStatus.hasChanged()){nodeStatus.setNewNode(node.clone()); return nodeStatus;}

        return NodeStatus.noChange(node);
    }

    private void logSteps(NodeStatus nodeStatus) {
        Log.d("debugTag", nodeStatus.getChangeType().getDescrip());
        Log.d("debugTag",  nodeStatus.getNewNode().toExpression() + "\n");

        if (nodeStatus.getSubsteps() != null){
            for (NodeStatus status: nodeStatus.getSubsteps()) {
                Log.d("debugTag","\nSubpasos:");
                logSteps(status);
            }
        }
    }

    /**
     // evaluates arithmetic (e.g. 2+2 or 3*5*2) on an operation node.
     // Returns a Node.Status object.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus arithmeticSearch(TreeNode treeNode) {

        // TODO Busqueda postOrder
        // Buscar una suma, producto o potencia de constantes
        if (treeNode == null || !treeNode.esAditivo() || !treeNode.esMultiplicativo() || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        // Todas constantes
        for(TreeNode child: treeNode.getArgs()){
            if (!TreeUtils.isConstant(child, true)){
                return NodeStatus.noChange(treeNode);
            }
        }

        // we want to eval each arg so unary minuses around constant nodes become
        // constant nodes with negative values
        int i = 0;
        for(TreeNode child: treeNode.getArgs()){
            if (child.isUnaryMinus()){
                treeNode.setChild(i, TreeNode.createConstant(child.getLeftNode().getIntegerValue() * -1));
            }
            i++;
        }

        // Only resolve division of integers if we get an integer result.
        if (TreeUtils.isIntegerFraction(treeNode)){
            Integer numeratorValue = treeNode.getLeftNode().getIntegerValue();
            Integer denominatorValue = treeNode.getRightNode().getIntegerValue();
            if (numeratorValue % denominatorValue == 0) {
                TreeNode newNode = TreeNode.createConstant(numeratorValue / denominatorValue);
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC, treeNode, newNode);
            }else{
                // Elr esultado no es entero
                return NodeStatus.noChange(treeNode);
            }
        }else{
            TreeNode newNode = TreeNode.createConstant(treeNode.getOperationResult());
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC, treeNode, newNode);
        }
    }

    /**
     * Performs simpifications that are more basic and overaching like (...)^0 => 1
     * These are always the first simplifications that are attempted.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus basicSearch(TreeNode treeNode){
        NodeStatus nodeStatus;

        // TODO Busqueda preOrder

        // multiplication by 0 yields 0
        nodeStatus = reduceMultiplicationByZero(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // division of 0 by something yields 0
        nodeStatus = reduceZeroDividedByAnything(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // ____^0 --> 1
                nodeStatus = reduceExponentByZero(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // Check for x^1 which should be reduced to x
        nodeStatus = removeExponentByOne(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // Check for 1^x which should be reduced to 1
        // if x can be simplified to a constant
        nodeStatus = removeExponentBaseOne(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // - - becomes +
        nodeStatus = simplifyDoubleUnaryMinus(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // If this is a + node and one of the operands is 0, get rid of the 0
        nodeStatus = removeAdditionOfZero(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // If this is a * node and one of the operands is 1, get rid of the 1
        nodeStatus = removeMultiplicationByOne(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // In some cases, remove multiplying by -1
        nodeStatus = removeMultiplicationByNegativeOne(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // If this is a / node and the denominator is 1 or -1, get rid of it
        nodeStatus = removeDivisionByOne(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. x*5 -> 5x
        nodeStatus = rearrangeCoefficient(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        return NodeStatus.noChange(treeNode);
    }

    /**
     // If `node` is a fraction with a numerator that is a sum, breaks up the
     // fraction e.g. (2+x)/5 -> (2/5 + x/5)
     // Returns a Node.Status object
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus breakUpNumeratorSearch(TreeNode treeNode){
        // TODO Busqueda postOrder

        // Buscar una division
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        // TODO breakUpNumeratorSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     // Given an operator node, maybe collects and then combines if possible
     // e.g. 2x + 4x + y => 6x + y
     // e.g. 2x * x^2 * 5x => 10 x^4
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus collectAndCombineSearch(TreeNode treeNode){
        // TODO Busqueda postOrder

        // TODO collectAndCombineSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     // Distributes through parenthesis.
     // e.g. 2(x+3) -> (2*x + 2*3)
     // e.g. -(x+5) -> (-x + -5)
     // Returns a Node.Status object.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus distributeSearch(TreeNode treeNode){
        // TODO Busqueda postOrder

        // TODO distributeSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     // Searches for and simplifies any chains of division or nested division.
     // Returns a Node.Status object
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus divisionSearch(TreeNode treeNode){
        // TODO Busqueda preOrder

        // TODO divisionSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     // Performs simpifications on fractions: adding and cancelling out.
     // Returns a Node.Status object
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus fractionsSearch(TreeNode treeNode){
        NodeStatus nodeStatus;

        // TODO Busqueda preOrder

        // e.g. 2/3 + 5/6
        nodeStatus = addConstantFractions(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. 4 + 5/6 or 4.5 + 6/8
        nodeStatus = addConstantAndFraction(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. 2/-9  ->  -2/9      e.g. -2/-9  ->  2/9
        nodeStatus = simplifyFractionSigns(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. 8/12  ->  2/3 (divide by GCD 4)
        nodeStatus = divideByGCD(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. 2x/4 -> x/2 (divideByGCD but for coefficients of polynomial terms)
        nodeStatus = simplifyPolynomialFraction(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        // e.g. (2x * 5) / 2x  ->  5
        nodeStatus = cancelLikeTerms(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        return NodeStatus.noChange(treeNode);
    }


    /**
     // Searches through the tree, prioritizing deeper nodes, and evaluates
     // functions (e.g. R(25)) if possible.
     // Returns a Node.Status object.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus functionsSearch(TreeNode treeNode){
        // TODO Busqueda postOrder

        // TODO functionsSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     // If `node` is a product of terms where:
     // 1) at least one is a fraction
     // 2) either none are polynomial terms, OR
     //    at least one has a symbol in the denominator
     // then multiply them together.
     // e.g. 2 * 5/x -> (2*5)/x
     // e.g. 3 * 1/5 * 5/9 = (3*1*5)/(5*9)
     // e.g. 2x * 1/x -> (2x*1) / x
     // Returns a Node.Status object.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus multiplyFractionsSearch(TreeNode treeNode){
        // TODO Busqueda postOrder

        // TODO multiplyFractionsSearch Resolver esto
        throw new UnsupportedOperationException();
    }

    /**
     * (algo) ^ 0 = 1
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus reduceExponentByZero(TreeNode treeNode){

        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        // Si se encuentra, verificar si el exponente es 0
        TreeNode exponentNode =  treeNode.getRightNode();
        if (TreeUtils.isConstant(exponentNode) && TreeUtils.zeroValue(exponentNode)){

            // De ser así, reemplazar tod o el subárbol con la constante 1.
            TreeNode newNode = TreeNode.createConstant(CONSTANT_1);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REDUCE_EXPONENT_BY_ZERO, treeNode, newNode);
        }else{
            return NodeStatus.noChange(treeNode);
        }
    }

    /**
     * 0 * (algo) = 0
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus reduceMultiplicationByZero(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        // If `node` is a multiplication node with 0 as one of its operands,
        // reduce the node to 0. Returns a Node.Status object.
        Boolean hasZeroIndex = false;
        for(TreeNode child: treeNode.getArgs()){
            if (TreeUtils.isConstant(child) && TreeUtils.zeroValue(child)) {
                hasZeroIndex =true;
                break;
            }else if(TreeUtils.isPolynomialTerm(child) && CONSTANT_0.equals(child.getCoefficient())){
                hasZeroIndex =true;
                break;
            }
        }

        // Si se encuentra, verificar si algún operadorando es 0
        if (hasZeroIndex){
            // De ser así, reemplazar el subárbol con la constante 0.
            TreeNode newNode =TreeNode.createConstant(0);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO, treeNode, newNode);
        }else {
            return NodeStatus.noChange(treeNode);
        }
    }


    /**
     * 0 / (algo) = 0
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus reduceZeroDividedByAnything(TreeNode treeNode){

        // Buscar un nodo con /
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        // Si se encuentra, verificar si el numerador es 0
        TreeNode exponentNode =  treeNode.getLeftNode();
        if (TreeUtils.isConstant(exponentNode) && TreeUtils.zeroValue(exponentNode)){

            // De ser así, reemplazar tod o el subárbol con la constante 0.
            TreeNode newNode = TreeNode.createConstant(0);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REDUCE_ZERO_NUMERATOR, treeNode, newNode);
        }else{
            return NodeStatus.noChange(treeNode);
        }
    }


    /**
     * (algo) + 0 = (algo)
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeAdditionOfZero(TreeNode treeNode){

        // Buscar un nodo con + o -
        if (treeNode == null || !treeNode.esAditivo()) {
            return NodeStatus.noChange(treeNode);
        }

        int zeroIndex = -1;
        int i =0;
        for(TreeNode child: treeNode.getArgs()){
            if (TreeUtils.isConstant(child) && TreeUtils.zeroValue(child)){
                zeroIndex = i;
                break;
            }
            i++;
        }

        if (zeroIndex >= 0) {
            TreeNode newNode = treeNode.cloneDeep();
            // remove the 0 node
            treeNode.removeChild(zeroIndex);

            // if there's only one operand left, there's nothing left to add it to,
            // so move it up the tree
            if (newNode.getArgs().size() == 1) {
                newNode = newNode.getChild(0);
            }

            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_ADDING_ZERO, treeNode, newNode);
        }

        return NodeStatus.noChange(treeNode);
    }


    /**
     * (algo) / 1 = (algo)
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeDivisionByOne(TreeNode treeNode){
        // Buscar un nodo con /
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode denominatorNode =  treeNode.getRightNode();
        if (!TreeUtils.isConstant(denominatorNode)){
            return NodeStatus.noChange(treeNode);
        }

        Integer denominatorValue = denominatorNode.getIntegerValue();
        if (CONSTANT_1_NEG.equals(denominatorValue)){

            TreeNode numeratorNode =  treeNode.getLeftNode();
            NodeStatus.ChangeTypes changeType = TreeUtils.isNegative(numeratorNode)?
                    NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS :
                    NodeStatus.ChangeTypes.DIVISION_BY_NEGATIVE_ONE;

            numeratorNode = TreeUtils.negate(numeratorNode, false);

            // De ser así, reemplazar el subárbol con la el numerador
            return NodeStatus.nodeChanged(
                    changeType, treeNode, numeratorNode.clone());

        }else if (CONSTANT_1.equals(denominatorValue)){
            // De ser así, reemplazar el subárbol con la el numerador
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DIVISION_BY_ONE, treeNode, treeNode.getLeftNode().clone());
        }

        return NodeStatus.noChange(treeNode);
    }

    /**
     * 1^(algo) = 1
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeExponentBaseOne(TreeNode treeNode){
        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode baseNode = treeNode.getLeftNode();
        if (TreeUtils.isConstant(baseNode) &&
                CONSTANT_1.equals(baseNode.getIntegerValue())){

            TreeNode node = TreeNode.createConstant(1);
            // De ser así, reemplazar el subárbol con 1
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE, treeNode, node);
        }


        return NodeStatus.noChange(treeNode);
    }

    protected NodeStatus simplifyDoubleUnaryMinus(TreeNode treeNode) {
        // TODO simplifyDoubleUnaryMinus
        throw new UnsupportedOperationException();
    }

    /**
     * (algo)^1  = (algo)
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeExponentByOne(TreeNode treeNode){
        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode exponentNode = treeNode.getLeftNode();
        if (TreeUtils.isConstant(exponentNode) &&
                CONSTANT_1.equals(exponentNode.getIntegerValue())){

            TreeNode node = TreeNode.createConstant(1);
            // De ser así, reemplazar el subárbol con 1
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE, treeNode, node);
        }


        return NodeStatus.noChange(treeNode);
    }

    /**
     * (algo) * (-1) = - (algo)
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeMultiplicationByNegativeOne(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) return NodeStatus.noChange(treeNode);

        int i = 0;
        int minusOneIndex = -1;
        for(TreeNode child: treeNode.getArgs()){
            if (TreeUtils.isConstant(child) && TreeUtils.hasValue(child, "-1")) {
                minusOneIndex = i;
                break;
            }
            i++;
        }

        if (minusOneIndex == -1) return NodeStatus.noChange(treeNode);

        // We might merge/combine the negative one into another node. This stores
        // the index of that other node in the arg list.
        int nodeToCombineIndex;
        // If minus one is the last term, maybe combine with the term before
        if (minusOneIndex + 1 == treeNode.getArgs().size()) {
            nodeToCombineIndex = minusOneIndex - 1;
        }
        else {
            nodeToCombineIndex = minusOneIndex + 1;
        }

        TreeNode nodeToCombine = treeNode.getChild(nodeToCombineIndex);
        // If it's a constant, the combining of those terms is handled elsewhere.
        if (TreeUtils.isConstant(nodeToCombine)) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode newNode = treeNode.cloneDeep();

        // Get rid of the -1
        nodeToCombine = TreeUtils.negate(nodeToCombine.cloneDeep());

        // replace the node next to -1 and remove -1
        newNode.setChild(nodeToCombineIndex, nodeToCombine);
        newNode.removeChild(minusOneIndex);

        // if there's only one operand left, move it up the tree
        if (newNode.getArgs().size() == 1) {
            newNode = newNode.getChild(0);
        }
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE, treeNode, newNode);
    }

    /**
     * (algo) * 1 = algo
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus removeMultiplicationByOne(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        int i = 0;
        int oneIndex = -1;
        for(TreeNode child: treeNode.getArgs()){
            if (TreeUtils.isConstant(child) && TreeUtils.hasValue(child, "-1")) {
                oneIndex = i;
                break;
            }
            i++;
        }

        if (oneIndex >= 0) {
            TreeNode newNode = treeNode.cloneDeep();
            // remove the 1 node
            newNode.removeChild(oneIndex);
            // if there's only one operand left, there's nothing left to multiply it
            // to, so move it up the tree
            if (newNode.getArgs().size()== 1) {
                newNode = newNode.getChild(0);
            }
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE, treeNode, newNode);
        }

        return NodeStatus.noChange(treeNode);
    }

    /**
     * Arreglar coeficientes: ej: X*5 = 5X
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus rearrangeCoefficient(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        if (!TreeUtils.canRearrangeCoefficient(treeNode)) {
            return NodeStatus.noChange(treeNode);
        }

        // Tiene que ser 1 de los 2 nodos constante, y el otro una X (En ese caso agrupo)
        TreeNode leftNode = treeNode.getLeftNode();
        TreeNode rightNode = treeNode.getLeftNode();

        TreeNode newNode = leftNode.clone();
        newNode.multiplyCoefficient(rightNode.getValue());
        // De ser así, reemplazar el subárbol el otro nodo
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.REARRANGE_COEFF, treeNode, newNode);

    }


    /**
     * Adds a constant to a fraction by:
     * - collapsing the fraction to decimal if the constant is not an integer
     *   e.g. 5.3 + 1/2 -> 5.3 + 0.2
     * - turning the constant into a fraction with the same denominator if it is
     *   an integer, e.g. 5 + 1/2 -> 10/2 + 1/2
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus addConstantAndFraction(TreeNode treeNode){

        // Buscar un nodo con + o -
        if (treeNode == null || !treeNode.esAditivo() || treeNode.getArgs().size()!=2) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode leftNode = treeNode.getLeftNode();
        TreeNode rightNode = treeNode.getRightNode();

        TreeNode constNode;
        TreeNode fractionNode;
        if (TreeUtils.isConstant(leftNode)){
            if (TreeUtils.isConstantFraction(rightNode)) {
                constNode = leftNode;
                fractionNode = rightNode;
            }else {
                return NodeStatus.noChange(treeNode);
            }
        }else if (TreeUtils.isConstant(rightNode)) {
            if (TreeUtils.isConstantFraction(leftNode)) {
                constNode = rightNode;
                fractionNode = leftNode;
            }else {
                return NodeStatus.noChange(treeNode);
            }
        }else {
            return NodeStatus.noChange(treeNode);
        }


        TreeNode newNode = treeNode.cloneDeep();
        List<NodeStatus> substeps = new ArrayList<>();

        TreeNode denominatorNode = fractionNode.getRightNode();
        Integer denominatorValue = denominatorNode.getIntegerValue();
        Integer constNodeValue = constNode.getIntegerValue();
        TreeNode newNumeratorNode = TreeNode.createConstant(
                constNodeValue * denominatorValue);

        TreeNode newConstNode = TreeNode.createOperator (
                "/", newNumeratorNode, denominatorNode);
        TreeNode newFractionNode = fractionNode;

        // Conservo la posición de los nodos iniciales
        if (TreeUtils.isConstant(leftNode)) {
            newNode.setLeftNode(newConstNode);
            newNode.setRightNode(newFractionNode);
        }
        else {
            newNode.setLeftNode(newFractionNode);
            newNode.setRightNode(newConstNode);
        }

        substeps.add(NodeStatus.nodeChanged(NodeStatus.ChangeTypes.CONVERT_INTEGER_TO_FRACTION, treeNode, newNode));
        newNode = NodeStatus.resetChangeGroups(newNode);

        // If we changed an integer to a fraction, we need to add the steps for
        // adding the fractions.
        NodeStatus addFractionStatus = addConstantFractions(newNode);
        substeps.addAll(addFractionStatus.getSubsteps());


        NodeStatus lastStep = substeps.get(substeps.size()-1);
        newNode = NodeStatus.resetChangeGroups(lastStep.getNewNode());

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC, treeNode, newNode, substeps);
    }

    /**
     // Adds constant fractions -- can start from either step 1 or 2
     // 1A. Find the LCD if denominators are different and multiplies to make
     //     denominators equal, e.g. 2/3 + 4/6 --> (2*2)/(3*2) + 4/6
     // 1B. Multiplies out to make constant fractions again
     //     e.g. (2*2)/(3*2) + 4/6 -> 4/6 + 4/6
     // 2A. Combines numerators, e.g. 4/6 + 4/6 ->  e.g. 2/5 + 4/5 --> (2+4)/5
     // 2B. Adds numerators together, e.g. (2+4)/5 -> 6/5
     // Returns a Node.Status object with substeps
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus addConstantFractions(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        // Buscar un nodo operador
        if (!node.esAditivo()) {
            return NodeStatus.noChange(node);
        }

        for(TreeNode child: node.getArgs()){
            if (!TreeUtils.isIntegerFraction(child)){
                return NodeStatus.noChange(node);
            }
        }

        Set<Integer> denominators = new HashSet<>();
        for(TreeNode child: node.getArgs()){
            denominators.add(child.getRightNode().getIntegerValue());
        }

        List<NodeStatus> substeps = new ArrayList<>();
        NodeStatus status;

        // 1A. First create the common denominator if needed
        // e.g. 2/6 + 1/4 -> (2*2)/(6*2) + (1*3)/(4*3)
        if (denominators.size() != 1 ){
            status = makeCommonDenominator(newNode, denominators);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());

            // 1B. Multiply out the denominators
            status = evaluateDenominators(newNode);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());

            // 1B. Multiply out the numerators
            status = evaluateNumerators(newNode);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        // 2A. Now that they all have the same denominator, combine the numerators
        // e.g. 2/3 + 5/3 -> (2+5)/3
        status = combineNumeratorsAboveCommonDenominator(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // 2B. Finally, add the numerators together
        status = addNumeratorsTogether(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // 2C. If the numerator is 0, simplify to just 0
        status = reduceNumerator(newNode);
        if (status.hasChanged()) {
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        // 2D. If we can simplify the fraction, do so
        status = divideByGCD(newNode);
        if (status.hasChanged()) {
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.ADD_FRACTIONS, node, newNode,  substeps);
    }

    /**
     // Given a + operation node with a list of fraction nodes as args that all have
     // the same denominator, add them together. e.g. 2/3 + 5/3 -> (2+5)/3
     // Returns the new node.
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus combineNumeratorsAboveCommonDenominator(TreeNode node) {

        TreeNode commonDenominator = TreeNode.createConstant(node.getLeftNode().getRightNode().getIntegerValue());

        List<TreeNode> numeratorArgs = new ArrayList<>();
        for(TreeNode child: node.getArgs()){
            numeratorArgs.add(child.getLeftNode());
        }

        // Genero el nodo (numeradorIzq + NumeradorDer)
        TreeNode newNumerator = TreeNode.createOperator("+", numeratorArgs);

        // Finalmente: (numeradorIzq + NumeradorDer) / comunDenominador
        TreeNode newNode = TreeNode.createOperator("/", newNumerator, commonDenominator);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COMBINE_NUMERATORS, node, newNode);
    }

    /**
     // Given a node with a numerator that is an addition node, will add
     // all the numerators and return the result
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus addNumeratorsTogether(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        TreeNode numeratorAditionNode = newNode.getLeftNode();
        newNode.setLeftNode(TreeNode.createConstant(numeratorAditionNode.getOperationResult()));

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.ADD_NUMERATORS, node, newNode);
    }

    private NodeStatus reduceNumerator(TreeNode node) {
        // Numerador en 0?
        if (CONSTANT_0.equals(node.getLeftNode().getIntegerValue())) {
            TreeNode newNode = TreeNode.createConstant(CONSTANT_0);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REDUCE_ZERO_NUMERATOR, node, newNode);
        }

        return NodeStatus.noChange(node);
    }

    /**
     // Takes `node`, a sum of fractions, and returns a node that's a sum of
     // fractions with denominators that evaluate to the same common denominator
     // e.g. 2/6 + 1/4 -> (2*2)/(6*2) + (1*3)/(4*3)
     // Returns the new node.
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus makeCommonDenominator(TreeNode node, Set<Integer> denominators) {

        TreeNode newNode = node.cloneDeep();

        Integer commonDenominator = 1;
        for (Integer denominator: denominators){
            commonDenominator = calculateLCM(commonDenominator, denominator);
        }

        int i =0 ;
        for(TreeNode child: newNode.getArgs()) {
            // missingFactor is what we need to multiply the top and bottom by
            // so that the denominator is the LCD
            Integer missingFactor = commonDenominator / child.getRightNode().getIntegerValue();
            if (!CONSTANT_1.equals(missingFactor)) {
                // new numerador: (num * missingFactor)
                TreeNode newNumerator = TreeNode.createOperator("*",
                        TreeNode.createConstant(child.getLeftNode().getIntegerValue()),
                        TreeNode.createConstant(missingFactor));

                // new denominator: (num * missingFactor)
                TreeNode newDenominator = TreeNode.createOperator("*",
                        TreeNode.createConstant(child.getRightNode().getIntegerValue()),
                        TreeNode.createConstant(missingFactor));

                // new fraction
                newNode.setChild(i, TreeNode.createOperator("/", newNumerator, newDenominator));
            }
            i++;
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COMMON_DENOMINATOR, node, newNode);
    }

    /**
     * (2*2)/(6*2) + (1*3)/(4*3) -> (2*2)/12 + (1*3)/12
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus evaluateDenominators(TreeNode node) {

        TreeNode newNode = node.cloneDeep();
        for(TreeNode child: newNode.getArgs()){
            child.setRightNode(TreeNode.createConstant(child.getRightNode().getOperationResult()));
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_DENOMINATORS, node, newNode);
    }

    /**
     * (2*2)/12 + (1*3)/12 -> 4/12 + 3/12
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus evaluateNumerators(TreeNode node) {
        TreeNode newNode = node.cloneDeep();
        for(TreeNode child: newNode.getArgs()){
            child.setLeftNode(TreeNode.createConstant(child.getLeftNode().getOperationResult()));
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_NUMERATORS, node, newNode);
    }

    /**
     * Asociar términos sumados con X. Ejemplo: 2x + 4x => 6x
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus addLikeTerms(TreeNode treeNode, Boolean polynomialOnly){

        // Buscar un nodo operador
        if (treeNode == null || !treeNode.esOperador()) {
            return NodeStatus.noChange(treeNode);
        }

        NodeStatus nodeStatus;
        if (!polynomialOnly) {
            nodeStatus = evaluateConstantSum(treeNode);
            if (nodeStatus.hasChanged()) {
                return nodeStatus;
            }
        }

        nodeStatus = addLikePolynomialTerms(treeNode);
        if (nodeStatus.hasChanged()) {
            return nodeStatus;
        }

        nodeStatus = addLikeNthRootTerms(treeNode);
        if (nodeStatus.hasChanged()) {
            return nodeStatus;
        }

        return NodeStatus.noChange(treeNode);
    }

    /**
     // Evaluates a sum of constant numbers and integer fractions to a single
     // constant number or integer fraction. e.g. e.g. 2/3 + 5 + 5/2 => 49/6
     // Returns a Node.Status object.
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus evaluateConstantSum(TreeNode treeNode) {

        // Buscar un suma o resta
        if (treeNode == null || !treeNode.esAditivo()) {
            return NodeStatus.noChange(treeNode);
        }

        // Alguno de los hijos debe ser constante o fraccion
        for (TreeNode child: treeNode.getArgs()){
            if (!TreeUtils.isConstantOrConstantFraction(child)){
                return NodeStatus.noChange(treeNode);
            }
        }

        // functions needed to evaluate the sum
        NodeStatus nodeStatus = null;

        nodeStatus = arithmeticSearch(treeNode);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}

        nodeStatus = addConstantFractions(treeNode);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}

        nodeStatus = addConstantAndFraction(treeNode);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}


        TreeNode newNode = treeNode.cloneDeep();
        List<NodeStatus> substeps = new ArrayList<>();
        NodeStatus status;


        // STEP 1: group fractions and constants separately
        status = groupConstantsAndFractions(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // TODO evaluateConstantSum: esto se complica por no estra achatado, pensar bien como resolverlo
        /*
  const constants = newNode.args[0];
  const fractions = newNode.args[1];

        // STEP 2A: evaluate arithmetic IF there's > 1 constant
        // (which is the case if it's a list surrounded by parenthesis)
        if (Node.Type.isParenthesis(constants)) {
    const constantList = constants.content;
    const evaluateStatus = arithmeticSearch(constantList);
            status = Node.Status.childChanged(newNode, evaluateStatus, 0);
            substeps.push(status);
            newNode = Node.Status.resetChangeGroups(status.getNewNode());
        }

        // STEP 2B: add fractions IF there's > 1 fraction
        // (which is the case if it's a list surrounded by parenthesis)
        if (Node.Type.isParenthesis(fractions)) {
    const fractionList = fractions.content;
    const evaluateStatus = addConstantFractions(fractionList);
            status = Node.Status.childChanged(newNode, evaluateStatus, 1);
            substeps.push(status);
            newNode = Node.Status.resetChangeGroups(status.getNewNode());
        }

        // STEP 3: combine the evaluated constant and fraction
        // the fraction might have simplified to a constant (e.g. 1/3 + 2/3 -> 2)
        // so we just call evaluateConstantSum again to cycle through
        status = evaluateConstantSum(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC, treeNode, newNode, substeps);
*/
        throw new UnsupportedOperationException();
    }

    /**
     // If we can't combine using one of those functions, there's a mix of > 2
     // fractions and constants. So we need to group them together so we can later
     // add them.
     // Expects a node that is a sum of integer fractions and constants.
     // Returns a Node.Status object.
     // e.g. 2/3 + 5 + 5/2 => (2/3 + 5/2) + 5
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus groupConstantsAndFractions(TreeNode node) {

        // TODO verificar esto en caso de modificar el arbol al achatar

        // TODO groupConstantsAndFractions: esto se complica por no estra achatado, pensar bien como resolverlo
        /*
        let fractions = node.args.filter(Node.Type.isIntegerFraction);
        let constants = node.args.filter(Node.Type.isConstant);

        if (fractions.length === 0 || constants.length === 0) {
            throw Error('expected both integer fractions and constants, got ' + node);
        }

        if (fractions.length + constants.length !== node.args.length) {
            throw Error('can only evaluate integer fractions and constants');
        }

        constants = constants.map(node => {
                // set the changeGroup - this affects both the old and new node
                node.changeGroup = 1;
        // clone so that node and newNode aren't stored in the same memory
        return node.cloneDeep();
  });
        // wrap in parenthesis if there's more than one, to group them
        if (constants.length > 1) {
            constants = Node.Creator.parenthesis(Node.Creator.operator('+', constants));
        }
        else {
            constants = constants[0];
        }

        fractions = fractions.map(node => {
                // set the changeGroup - this affects both the old and new node
                node.changeGroup = 2;
        // clone so that node and newNode aren't stored in the same memory
        return node.cloneDeep();
  });
        // wrap in parenthesis if there's more than one, to group them
        if (fractions.length > 1) {
            fractions = Node.Creator.parenthesis(Node.Creator.operator('+', fractions));
        }
        else {
            fractions = fractions[0];
        }

        TreeNode newNode = TreeNode.createOperator("+", constants, fractions);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_LIKE_TERMS, node, newNode);
*/
        throw new UnsupportedOperationException();
    }

    protected NodeStatus addLikePolynomialTerms(TreeNode treeNode) {
        // TODO addLikePolynomialTerms
        throw new UnsupportedOperationException();
    }

    protected NodeStatus addLikeNthRootTerms(TreeNode treeNode) {
        // TODO addLikeNthRootTerms
        throw new UnsupportedOperationException();
    }

    protected NodeStatus multiplyLikeTerms(TreeNode treeNode){
        // TODO multiplyLikeTerms: Multiplicar terminos con X. Ejemplo: 2x * x^2 * 5x => 10 x^4
        throw new UnsupportedOperationException();
    }

    /**
     // Simplifies negative signs if possible
     // e.g. -1/-3 --> 1/3   4/-5 --> -4/5
     // Note that -4/5 doesn't need to be simplified.
     // Note that our goal is for the denominator to always be positive. If it
     // isn't, we can simplify signs.
     // Returns a Node.Status object
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus simplifyFractionSigns(TreeNode treeNode) {

        // Buscar una division
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode oldFraction = treeNode.cloneDeep();
        TreeNode numerator = treeNode.getLeftNode();
        TreeNode denominator = treeNode.getRightNode();
        // The denominator should never be negative.
        if (TreeUtils.isNegative(denominator)) {
            denominator = TreeUtils.negate(denominator, false);
            NodeStatus.ChangeTypes changeType = TreeUtils.isNegative(numerator) ?
                    NodeStatus.ChangeTypes.CANCEL_MINUSES :
                    NodeStatus.ChangeTypes.SIMPLIFY_SIGNS;
            numerator = TreeUtils.negate(numerator, false);
            TreeNode newFraction = TreeNode.createOperator("/", numerator, denominator);
            return NodeStatus.nodeChanged(changeType, oldFraction, newFraction);
        } else {
            return NodeStatus.noChange(treeNode);
        }
    }

    protected NodeStatus cancelLikeTerms(TreeNode treeNode){
        // TODO cancelLikeTerms: Simplificar términos iguales en divisiones. Ejemplo: (2x^2 * 5) / 2x^2 => 5 / 1
        throw new UnsupportedOperationException();
    }

    /**
     * Simplifies a polynomial term with a fraction as its coefficients.
     * e.g. 2x/4 --> x/2    10x/5 --> 2x
     * Also simplified negative signs
     * e.g. -y/-3 --> y/3   4x/-5 --> -4x/5
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion the new simplified node in a Node.Status object
     */
    protected NodeStatus simplifyPolynomialFraction(TreeNode node){

        if (!TreeUtils.isPolynomialTerm(node)) {
            return NodeStatus.noChange(node);
        }

        // TODO simplifyPolynomialFraction
        throw new UnsupportedOperationException();
    }

    protected NodeStatus simplifyLikeBaseDivision(TreeNode treeNode){
        // TODO simplifyLikeBaseDivision: Simplificar términos divididos con la misma base. Ejemplo: (2x+5)^8 / (2x+5)^2 = (2x+5)^6
        throw new UnsupportedOperationException();
    }

    /**
     * Simplificar coeficientes en divisiones. Ejemplo: 2 / 4 -> 1 / 2
     * @param treeNode Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus divideByGCD(TreeNode treeNode){

        if (!TreeUtils.isConstantFraction(treeNode)){
            return NodeStatus.noChange(treeNode);
        }

        List<NodeStatus> substeps = new ArrayList<>();
        TreeNode newNode = treeNode.cloneDeep();

        TreeNode numeratorNode =  treeNode.getLeftNode();
        TreeNode denominatorNode =  treeNode.getLeftNode();

        Integer numeratorValue = numeratorNode.getIntegerValue();
        Integer denominatorValue = denominatorNode.getIntegerValue();
        Integer gcd = calculateGCD(numeratorValue, denominatorValue);

        if (denominatorValue < 0) {
            gcd *= -1;
        }

        if (CONSTANT_1.equals(gcd)) {
            return NodeStatus.noChange(treeNode);
        }

        // STEP 1: Find GCD
        // e.g. 15/6 -> (5*3)/(2*3)
        NodeStatus status = findGCD(newNode, gcd, numeratorValue, denominatorValue);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // STEP 2: Cancel GCD
        // (5*3)/(2*3) -> 5/2
        status = cancelGCD(newNode, gcd, numeratorValue, denominatorValue);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.SIMPLIFY_FRACTION, treeNode, newNode, substeps);
    }

    /**
     * Calculates the  greatest common divisor
     * @param a First value
     * @param b Second Value
     * @return El estado de la simplificacion
     */
    private Integer calculateGCD(Integer a, Integer b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    /**
     * calculates the least common multiple
     * @param a First value
     * @param b Second Value
     * @return El estado de la simplificacion
     */
    private Integer calculateLCM(Integer a, Integer b) {
        return a * (b / calculateGCD(a, b));
    }

    /**
     * Returns a substep where the GCD is factored out of numerator and denominator. e.g. 15/6 -> (5*3)/(2*3)
     * @param node Nodo a evaluar
     * @param gcd Greatest common divisor
     * @param numeratorValue Numerator
     * @param denominatorValue Denominator
     * @return El estado de la simplificacion
     */
    private NodeStatus findGCD(TreeNode node, Integer gcd, Integer numeratorValue, Integer denominatorValue) {

        // manually set change group of the GCD nodes to be the same
        TreeNode gcdNode = TreeNode.createConstant(gcd);
        // gcdNode.changeGroup = 1;

        TreeNode intermediateNumerator = TreeNode.createOperator("*",
                TreeNode.createConstant(numeratorValue/gcd),
                gcdNode);

        TreeNode intermediateDenominator = TreeNode.createOperator("*",
                TreeNode.createConstant(denominatorValue/gcd),
                gcdNode);

        TreeNode newNode = TreeNode.createOperator("/",
                intermediateNumerator, intermediateDenominator);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.FIND_GCD, node, newNode);
    }

    /**
     * Returns a substep where the GCD is cancelled out of numerator and denominator. e.g. (5*3)/(2*3) -> 5/2
     * @param node Nodo a evaluar
     * @param gcd greastes common divisor
     * @param numeratorValue Numerador
     * @param denominatorValue Denominador
     * @return El estado de la simplificacion
     */
    private NodeStatus  cancelGCD(TreeNode node, Integer gcd, Integer numeratorValue, Integer denominatorValue) {
        TreeNode newNode;
        TreeNode newNumeratorNode = TreeNode.createConstant(numeratorValue/gcd);
        TreeNode newDenominatorNode = TreeNode.createConstant(denominatorValue/gcd);

        if (CONSTANT_1.equals(newDenominatorNode.getIntegerValue())) {
            newNode = newNumeratorNode;
        }else {
            newNode = TreeNode.createOperator(
                    "/", newNumeratorNode, newDenominatorNode);
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.CANCEL_GCD, node, newNode);
    }

}

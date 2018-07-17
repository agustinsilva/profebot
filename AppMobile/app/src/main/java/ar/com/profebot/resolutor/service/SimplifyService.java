package ar.com.profebot.resolutor.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.NodeStatus;
import ar.com.profebot.resolutor.container.ResolutionStep;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class SimplifyService {

    private static final Integer CONSTANT_1  = new Integer(1);
    private static final Integer CONSTANT_1_NEG  = new Integer(-1);
    private static final Integer CONSTANT_0  = new Integer(0);

    private List<ResolutionStep> resolutionStepList;

    public SimplifyService(List<ResolutionStep> resolutionStepList) {
            this.resolutionStepList = resolutionStepList;
    }

    /**
     * (algo) ^ 0 = 1
     * @param treeNode
     * @return
     */
    public NodeStatus reduceExponentByZero(TreeNode treeNode){

        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        // Si se encuentra, verificar si el exponente es 0
        TreeNode exponentNode =  treeNode.getRightNode();
        if (TreeUtils.esConstante(exponentNode) && TreeUtils.zeroValue(exponentNode)){

            // De ser así, reemplazar tod o el subárbol con la constante 1.
            TreeNode newNode = new TreeNode("1");
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REDUCE_EXPONENT_BY_ZERO, treeNode, newNode);
        }else{
            return NodeStatus.noChange(treeNode);
        }
    }

    /**
     * 0 * (algo) = 0
     * @param treeNode
     * @return
     */
    public NodeStatus reduceMultiplicationByZero(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        // TODO verificar esto en caso de modificar el arbol al achatar
        // Si se encuentra, verificar si algún operadorando es 0
        Integer zeroIndex = 0;
        TreeNode node =  treeNode.getLeftNode();
        if (TreeUtils.esConstante(node) && TreeUtils.zeroValue(node)) {
            zeroIndex++;
        }
        node =  treeNode.getRightNode();
        if (TreeUtils.esConstante(node) && TreeUtils.zeroValue(node)) {
            zeroIndex++;
        }

        if (zeroIndex > 0){
            // De ser así, reemplazar el subárbol con la constante 0.
            TreeNode newNode = new TreeNode("0");
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO, treeNode, newNode);
        }else {
            return NodeStatus.noChange(treeNode);
        }
    }


    /**
     * 0 / (algo) = 0
     * @param treeNode
     * @return
     */
    public NodeStatus reduceZeroDividedByAnything(TreeNode treeNode){

        // Buscar un nodo con /
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        // Si se encuentra, verificar si el numerador es 0
        TreeNode exponentNode =  treeNode.getLeftNode();
        if (TreeUtils.esConstante(exponentNode) && TreeUtils.zeroValue(exponentNode)){

            // De ser así, reemplazar tod o el subárbol con la constante 0.
            TreeNode newNode = new TreeNode("0");
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REDUCE_ZERO_NUMERATOR, treeNode, newNode);
        }else{
            return NodeStatus.noChange(treeNode);
        }
    }


    /**
     * (algo) + 0 = (algo)
     * @param treeNode
     * @return
     */
    public NodeStatus removeAdditionOfZero(TreeNode treeNode){

        // Buscar un nodo con + o -
        if (treeNode == null || !treeNode.esAditivo()) {
            return NodeStatus.noChange(treeNode);
        }

        // TODO verificar esto en caso de modificar el arbol al achatar
        // Si se encuentra, verificar si algún operadorando es 0
        TreeNode node =  treeNode.getLeftNode();
        if (TreeUtils.esConstante(node) && TreeUtils.zeroValue(node)) {
            // De ser así, reemplazar el subárbol con la el otro sumando
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_ADDING_ZERO, treeNode, treeNode.getRightNode().clone());
        }else {
            node = treeNode.getRightNode();
            if (TreeUtils.esConstante(node) && TreeUtils.zeroValue(node)) {
                // De ser así, reemplazar el subárbol con la el otro sumando
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.REMOVE_ADDING_ZERO, treeNode, treeNode.getLeftNode().clone());
            }
        }


        return NodeStatus.noChange(treeNode);
    }


    /**
     * (algo) / 1 = (algo)
     * @param treeNode
     * @return
     */
    public NodeStatus removeDivisionByOne(TreeNode treeNode){
        // Buscar un nodo con /
        if (treeNode == null || !treeNode.esDivision()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode denominatorNode =  treeNode.getRightNode();
        if (!TreeUtils.esConstante(denominatorNode)){
            return NodeStatus.noChange(treeNode);
        }

        Double denominatorValue = denominatorNode.getDoubleValue();
        if (CONSTANT_1_NEG.equals(denominatorValue)){

            TreeNode numeratorNode =  treeNode.getLeftNode();
            NodeStatus.ChangeTypes changeType = TreeUtils.esNegativo(numeratorNode)?
                    NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS :
                    NodeStatus.ChangeTypes.DIVISION_BY_NEGATIVE_ONE;

            numeratorNode = TreeUtils.negate(numeratorNode);

            // De ser así, reemplazar el subárbol con la el numerador
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DIVISION_BY_ONE, treeNode, numeratorNode.clone());

        }else if (CONSTANT_1.equals(denominatorValue)){
            // De ser así, reemplazar el subárbol con la el numerador
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DIVISION_BY_ONE, treeNode, treeNode.getLeftNode().clone());
        }

        return NodeStatus.noChange(treeNode);
    }

    /**
     * 1^(algo) = 1
     * @param treeNode
     * @return
     */
    public NodeStatus removeExponentBaseOne(TreeNode treeNode){
        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode baseNode = treeNode.getLeftNode();
        if (TreeUtils.esConstante(baseNode) &&
                CONSTANT_1.equals(baseNode.getDoubleValue())){

            TreeNode node = new TreeNode("1");
            // De ser así, reemplazar el subárbol con 1
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE, treeNode, node);
        }


        return NodeStatus.noChange(treeNode);
    }

    /**
     * (algo)^1  = (algo)
     * @param treeNode
     * @return
     */
    public NodeStatus removeExponentByOne(TreeNode treeNode){
        // Buscar un nodo con ^
        if (treeNode == null || !treeNode.esPotencia()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode exponentNode = treeNode.getLeftNode();
        if (TreeUtils.esConstante(exponentNode) &&
                CONSTANT_1.equals(exponentNode.getDoubleValue())){

            TreeNode node = new TreeNode("1");
            // De ser así, reemplazar el subárbol con 1
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE, treeNode, node);
        }


        return NodeStatus.noChange(treeNode);
    }

    /**
     * (algo) * (-1) = - (algo)
     * @param treeNode
     * @return
     */
    public NodeStatus removeMultiplicationByNegativeOne(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        // TODO verificar esto en caso de modificar el arbol al achatar
        // Si se encuentra, verificar si algún operadorando es -1
        TreeNode node =  treeNode.getLeftNode();
        if (TreeUtils.esConstante(node) && TreeUtils.hasValue(node, "-1")) {
            // De ser así, reemplazar el subárbol el otro nodo negado
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE, treeNode, TreeUtils.negate(treeNode.getRightNode()));
        }else{
            node =  treeNode.getRightNode();
            if (TreeUtils.esConstante(node) && TreeUtils.hasValue(node, "-1")) {
                // De ser así, reemplazar el subárbol el otro nodo negado
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE, treeNode, TreeUtils.negate(treeNode.getLeftNode()));
            }
        }

        return NodeStatus.noChange(treeNode);

    }

    /**
     * (algo) * 1 = algo
     * @param treeNode
     * @return
     */
    public NodeStatus removeMultiplicationByOne(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        // TODO verificar esto en caso de modificar el arbol al achatar
        // Si se encuentra, verificar si algún operadorando es 1
        TreeNode node =  treeNode.getLeftNode();
        if (TreeUtils.esConstante(node) && TreeUtils.hasValue(node, "1")) {
            // De ser así, reemplazar el subárbol el otro nodo
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE, treeNode, treeNode.getRightNode().clone());
        }else{
            node =  treeNode.getRightNode();
            if (TreeUtils.esConstante(node) && TreeUtils.hasValue(node, "1")) {
                // De ser así, reemplazar el subárbol el otro nodo
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE, treeNode, treeNode.getLeftNode().clone());
            }
        }

        return NodeStatus.noChange(treeNode);
    }

    /**
     * Arreglar coeficientes: ej: X*5 = 5X
     * @param treeNode
     * @return
     */
    public NodeStatus rearrangeCoefficient(TreeNode treeNode){

        // Buscar un nodo con *
        if (treeNode == null || !treeNode.esProducto()) {
            return NodeStatus.noChange(treeNode);
        }

        // Tiene que ser 1 de los 2 nodos constante, y el otro una X (En ese caso agrupo)
        TreeNode leftNode = treeNode.getLeftNode();
        TreeNode rightNode = treeNode.getLeftNode();
        if (TreeUtils.esConstante(leftNode) && TreeUtils.esIncognita(rightNode) ){
            TreeNode newNode = rightNode.clone();
            newNode.multiplyCoefficient(leftNode.getValue());
            // De ser así, reemplazar el subárbol el otro nodo
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REARRANGE_COEFF, treeNode, newNode);

        } else if (TreeUtils.esConstante(rightNode) && TreeUtils.esIncognita(leftNode) ){
            TreeNode newNode = leftNode.clone();
            newNode.multiplyCoefficient(rightNode.getValue());
            // De ser así, reemplazar el subárbol el otro nodo
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.REARRANGE_COEFF, treeNode, newNode);
        }


        return NodeStatus.noChange(treeNode);
    }

    public NodeStatus simplifyFractions(TreeNode treeNode){
        NodeStatus nodeStatus = null;

        nodeStatus = simplifyIntFractions(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        nodeStatus = resolveFractionNumeratorAndDenominator(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        nodeStatus = reduceFractions(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        nodeStatus = resolveConstantFractionAdition(treeNode);
        if (nodeStatus.hasChanged()){return nodeStatus;}

        return NodeStatus.noChange(treeNode);
    }

    private NodeStatus simplifyIntFractions(TreeNode treeNode){
        // TODO simplifyIntFractions: Buscar fracciones, y solo simplificarlas si dan un número entero (ej: 8/4 = 2).

        throw new UnsupportedOperationException();
    }

    private NodeStatus resolveFractionNumeratorAndDenominator(TreeNode treeNode){
        // TODO resolveFractionNumeratorAndDenominator: Buscar fracciones y sumar su numerador, o denominador (si son enteros).
        throw new UnsupportedOperationException();
    }

    private NodeStatus reduceFractions(TreeNode treeNode){
        // TODO reduceFractions: Simplificar fracciones. Ej:  2/4 --> 1/2 ; -1/-3 --> 1/3
        // Buscar el máximo común divisor.
        // Dividir numerador y denominador por ese valor. (o mostrar el subpaso de mostrar numerador y denominador multiplicando el MCD, ejemplo: 15/6 -> (5*3)/(2*3))

        throw new UnsupportedOperationException();
    }

    /**
     * Adds a constant to a fraction by:
     * - collapsing the fraction to decimal if the constant is not an integer
     *   e.g. 5.3 + 1/2 -> 5.3 + 0.2
     * - turning the constant into a fraction with the same denominator if it is
     *   an integer, e.g. 5 + 1/2 -> 10/2 + 1/2
     * @param treeNode
     * @return
     */
    private NodeStatus resolveConstantFractionAdition(TreeNode treeNode){

        // Buscar un nodo con + o -
        if (treeNode == null || !treeNode.esAditivo()) {
            return NodeStatus.noChange(treeNode);
        }

        TreeNode leftNode = treeNode.getLeftNode();
        TreeNode rightNode = treeNode.getRightNode();

        TreeNode constNode;
        TreeNode fractionNode;
        if (TreeUtils.esConstante(leftNode)){
            if (TreeUtils.esFraccion(rightNode)) {
                constNode = leftNode;
                fractionNode = rightNode;
            }else {
                return NodeStatus.noChange(treeNode);
            }
        }else if (TreeUtils.esConstante(rightNode)) {
            if (TreeUtils.esFraccion(leftNode)) {
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
        if (TreeUtils.esConstante(leftNode)) {
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
     * @param node
     * @return
     */
    private NodeStatus addConstantFractions(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        // Buscar un nodo operador
        if (node == null || !node.esAditivo()) {
            return NodeStatus.noChange(node);
        }

        // Si los dos nodos no son fracciones, salgo
        if (!TreeUtils.esFraccion(node.getLeftNode()) || !TreeUtils.esFraccion(node.getRightNode())) {
            return NodeStatus.noChange(node);
        }

        List<NodeStatus> substeps = new ArrayList<>();
        NodeStatus status;

        // 1A. First create the common denominator if needed
        // e.g. 2/6 + 1/4 -> (2*2)/(6*2) + (1*3)/(4*3)
        if (!node.getLeftNode().getIntegerValue().equals(node.getRightNode().getIntegerValue()) ){
            status = makeCommonDenominator(newNode);
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
     * @param node
     * @return
     */
    private NodeStatus combineNumeratorsAboveCommonDenominator(TreeNode node) {

        TreeNode commonDenominator = TreeNode.createConstant(node.getLeftNode().getRightNode().getIntegerValue());

        // Genero el nodo (numeradorIzq + NumeradorDer)
        TreeNode newNumerator = TreeNode.createOperator("+",
            TreeNode.createConstant(node.getLeftNode().getLeftNode().getIntegerValue()),
            TreeNode.createConstant(node.getLeftNode().getRightNode().getIntegerValue()));

        // Finalmente: (numeradorIzq + NumeradorDer) / comunDenominador
        TreeNode newNode = TreeNode.createOperator("/", newNumerator, commonDenominator);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COMBINE_NUMERATORS, node, newNode);
    }

    /**
     // Given a node with a numerator that is an addition node, will add
     // all the numerators and return the result
     * @param node
     * @return
     */
    private NodeStatus addNumeratorsTogether(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        TreeNode numeratorAditionNode = node.getLeftNode();
        newNode.setLeftNode(TreeNode.createConstant(
                numeratorAditionNode.getLeftNode().getIntegerValue() + numeratorAditionNode.getRightNode().getIntegerValue()));

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
     * @param node
     * @return
     */
    private NodeStatus makeCommonDenominator(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        TreeNode leftFraction = node.getLeftNode();
        TreeNode leftDenominator = leftFraction.getRightNode();

        TreeNode rightFraction = node.getRightNode();
        TreeNode rightDenominator = rightFraction.getRightNode();
        Integer commonDenominator = calculateLCM(leftDenominator.getIntegerValue(), rightDenominator.getIntegerValue());

        // missingFactor is what we need to multiply the top and bottom by
        // so that the denominator is the LCD
        Integer missingFactor = commonDenominator / leftDenominator.getIntegerValue();
        if (!CONSTANT_1 .equals(missingFactor)) {
            // new numerador: (num * missingFactor)
            TreeNode newNumerator = TreeNode.createOperator("*",
                    TreeNode.createConstant(leftFraction.getLeftNode().getIntegerValue()),
                    TreeNode.createConstant(missingFactor));

            // new denominator: (num * missingFactor)
            TreeNode newDenominator = TreeNode.createOperator("*",
                    TreeNode.createConstant(leftFraction.getRightNode().getIntegerValue()),
                    TreeNode.createConstant(missingFactor));

            // new left fraction
            newNode.setLeftNode(TreeNode.createOperator("/", newNumerator, newDenominator));
        }

        // Right fraction
        missingFactor = commonDenominator / rightDenominator.getIntegerValue();
        if (!CONSTANT_1 .equals(missingFactor)) {
            // new numerador: (num * missingFactor)
            TreeNode newNumerator = TreeNode.createOperator("*",
                    TreeNode.createConstant(rightFraction.getLeftNode().getIntegerValue()),
                    TreeNode.createConstant(missingFactor));

            // new denominator: (num * missingFactor)
            TreeNode newDenominator = TreeNode.createOperator("*",
                    TreeNode.createConstant(rightFraction.getRightNode().getIntegerValue()),
                    TreeNode.createConstant(missingFactor));

            // new left fraction
            newNode.setRightNode(TreeNode.createOperator("/", newNumerator, newDenominator));
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COMMON_DENOMINATOR, node, newNode);
    }

    /**
     * (2*2)/(6*2) + (1*3)/(4*3) -> (2*2)/12 + (1*3)/12
     * @param node
     * @return
     */
    private NodeStatus evaluateDenominators(TreeNode node) {

        TreeNode newNode = node.cloneDeep();

        TreeNode leftFraction = newNode.getLeftNode();
        TreeNode rightFraction = newNode.getRightNode();

        // leftFraction multiply denominator
        TreeNode newDenominator =  TreeNode.createConstant(leftFraction.getRightNode().getLeftNode().getIntegerValue()
                * leftFraction.getRightNode().getRightNode().getIntegerValue());
        leftFraction.setRightNode(newDenominator);

        // rightFraction multiply denominator
        newDenominator =  TreeNode.createConstant(rightFraction.getRightNode().getLeftNode().getIntegerValue()
                * rightFraction.getRightNode().getRightNode().getIntegerValue());
        rightFraction.setRightNode(newDenominator);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_DENOMINATORS, node, newNode);
    }

    /**
     * (2*2)/12 + (1*3)/12 -> 4/12 + 3/12
     * @param node
     * @return
     */
    private NodeStatus evaluateNumerators(TreeNode node) {
        TreeNode newNode = node.cloneDeep();

        TreeNode leftFraction = newNode.getLeftNode();
        TreeNode rightFraction = newNode.getRightNode();

        // leftFraction multiply numerator
        TreeNode newNumerator =  TreeNode.createConstant(leftFraction.getLeftNode().getLeftNode().getIntegerValue()
                * leftFraction.getLeftNode().getRightNode().getIntegerValue());
        leftFraction.setLeftNode(newNumerator);

        // rightFraction multiply numerator
        newNumerator =  TreeNode.createConstant(rightFraction.getLeftNode().getLeftNode().getIntegerValue()
                * rightFraction.getLeftNode().getRightNode().getIntegerValue());
        rightFraction.setLeftNode(newNumerator);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_NUMERATORS, node, newNode);
    }

    /**
     * Asociar términos sumados con X. Ejemplo: 2x + 4x => 6x
     * @param treeNode
     * @return
     */
    public NodeStatus addLikeTerms(TreeNode treeNode, Boolean polynomialOnly){

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

    private NodeStatus evaluateConstantSum(TreeNode treeNode) {
        throw new UnsupportedOperationException();
    }

    private NodeStatus addLikePolynomialTerms(TreeNode treeNode) {
        throw new UnsupportedOperationException();
    }

    private NodeStatus addLikeNthRootTerms(TreeNode treeNode) {
        throw new UnsupportedOperationException();
    }

    public NodeStatus multiplyLikeTerms(TreeNode treeNode){
        // TODO multiplyLikeTerms: Multiplicar terminos con X. Ejemplo: 2x * x^2 * 5x => 10 x^4
        throw new UnsupportedOperationException();
    }

    public NodeStatus cancelLikeTerms(TreeNode treeNode){
        // TODO cancelLikeTerms: Simplificar términos iguales en divisiones. Ejemplo: (2x^2 * 5) / 2x^2 => 5 / 1
        throw new UnsupportedOperationException();
    }

    /**
     * Simplifies a polynomial term with a fraction as its coefficients.
     * e.g. 2x/4 --> x/2    10x/5 --> 2x
     * Also simplified negative signs
     * e.g. -y/-3 --> y/3   4x/-5 --> -4x/5
     * @param node
     * @return the new simplified node in a Node.Status object
     */
    public NodeStatus simplifyPolynomialFraction(TreeNode node){

        if (!TreeUtils.isPolynomialTerm(node)) {
            return NodeStatus.noChange(node);
        }

        throw new UnsupportedOperationException();
    }

    public NodeStatus simplifyLikeBaseDivision(TreeNode treeNode){
        // TODO simplifyLikeBaseDivision: Simplificar términos divididos con la misma base. Ejemplo: (2x+5)^8 / (2x+5)^2 = (2x+5)^6
        throw new UnsupportedOperationException();
    }

    /**
     * Simplificar coeficientes en divisiones. Ejemplo: 2 / 4 -> 1 / 2
     * @param treeNode
     * @return
     */
    private NodeStatus divideByGCD(TreeNode treeNode){

        if (!TreeUtils.esFraccion(treeNode)){
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
     * @param a
     * @param b
     * @return
     */
    private Integer calculateGCD(Integer a, Integer b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    /**
     * calculates the least common multiple
     * @param a
     * @param b
     * @return
     */
    private Integer calculateLCM(Integer a, Integer b) {
        return a * (b / calculateGCD(a, b));
    }

    /**
     * Returns a substep where the GCD is factored out of numerator and denominator. e.g. 15/6 -> (5*3)/(2*3)
     * @param node
     * @param gcd
     * @param numeratorValue
     * @param denominatorValue
     * @return
     */
    private NodeStatus findGCD(TreeNode node, Integer gcd, Integer numeratorValue, Integer denominatorValue) {

        TreeNode newNode = node.cloneDeep();

        // manually set change group of the GCD nodes to be the same
        TreeNode gcdNode = TreeNode.createConstant(gcd);
        // gcdNode.changeGroup = 1;

        TreeNode intermediateNumerator = TreeNode.createOperator("*",
                TreeNode.createConstant(numeratorValue/gcd),
                gcdNode);

        TreeNode intermediateDenominator = TreeNode.createOperator("*",
                TreeNode.createConstant(denominatorValue/gcd),
                gcdNode);

        newNode = TreeNode.createOperator("/",
                intermediateNumerator, intermediateDenominator);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.FIND_GCD, node, newNode);
    }

    /**
     * Returns a substep where the GCD is cancelled out of numerator and denominator. e.g. (5*3)/(2*3) -> 5/2
     * @param node
     * @param gcd
     * @param numeratorValue
     * @param denominatorValue
     * @return
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

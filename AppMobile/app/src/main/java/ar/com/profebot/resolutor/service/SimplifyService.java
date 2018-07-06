package ar.com.profebot.resolutor.service;

import java.util.List;

import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.NodeStatus;
import ar.com.profebot.resolutor.container.ResolutionStep;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class SimplifyService {

    private static final Double CONSTANT_1  = new Double(1);
    private static final Double CONSTANT_1_NEG  = new Double(-1);
    private static final Double CONSTANT_0  = new Double(0);

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

    public void simplifyFractions(TreeNode treeNode){
        // TODO simplifyFractions
        simplifyIntFractions(treeNode);
        resolveFractionNumeratorAndDenominator(treeNode);
        reduceFractions(treeNode);
        resolveConstantFractionAdition(treeNode);
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

    private NodeStatus resolveConstantFractionAdition(TreeNode treeNode){
        // TODO resolveConstantFractionAdition: Buscar sumas de constantes/fracciones con fracciones, y generar 3 pasos:
        // Buscar el común denominador (mínimo común divisor), y multiplicar numerador y denominador según corresponda. 2/6 + 1/4 -> (2*2)/(6*2) + (1*3)/(4*3)
        // Simplificar la multiplicación en el numerador y denominador.
        // Simplificar la suma del numerador.
        // Verificar si se puede simplificar la fracción.

        throw new UnsupportedOperationException();
    }

    public NodeStatus addLikeTerms(TreeNode treeNode){
        // TODO addLikeTerms: Asociar términos sumados con X. Ejemplo: 2x + 4x => 6x
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

    public NodeStatus simplifyPolynomialFraction(TreeNode treeNode){
        // TODO simplifyPolynomialFraction:  2x/4 --> x/2    10x/5 --> 2x
        throw new UnsupportedOperationException();
    }

    public NodeStatus simplifyLikeBaseDivision(TreeNode treeNode){
        // TODO simplifyLikeBaseDivision: Simplificar términos divididos con la misma base. Ejemplo: (2x+5)^8 / (2x+5)^2 = (2x+5)^6
        throw new UnsupportedOperationException();
    }

    private NodeStatus divideByGCD(TreeNode treeNode){
        // TODO divideByGCD: Simplificar coeficientes en divisiones. Ejemplo: 2 / 4x -> 1 / 2x
        throw new UnsupportedOperationException();
    }

}

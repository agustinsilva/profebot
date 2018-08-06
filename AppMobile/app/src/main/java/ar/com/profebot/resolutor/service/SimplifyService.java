package ar.com.profebot.resolutor.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.CancelOutStatus;
import ar.com.profebot.resolutor.container.NodeStatus;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class SimplifyService {

    private static final Integer CONSTANT_1  = 1;
    private static final Integer CONSTANT_1_NEG  = -1;
    private static final Integer CONSTANT_0  = 0;

    private static final String CONSTANT = "constant";
    private static final String CONSTANT_FRACTION = "constantFraction";
  //  private static final String NTH_ROOT = "nthRoot";
    private static final String OTHER = "other";
    private static final String POLYNOMIAL_TERM = "PolynomialTerm";
    private static final String NTH_ROOT_TERM = "NthRootTerm";

    /**
     // Dado un nodo expresion, dada una lista de paso a paso simplificando la expresion.
     // Retorna una lista de detalles acerca del paso.
     * @param node Nodo a evaluar
     * @return Lista de pasos
     */
    public List<NodeStatus> stepThrough(TreeNode node) {

        List<NodeStatus> steps = new ArrayList<>();
        final Integer MAX_STEP_COUNT = 20;
        Integer iters = 0;

        String originalExpressionStr = node.toExpression();

        // Ahora, simplifica la expresion matematica hasta que no pueda cambiar más.
        NodeStatus nodeStatus = step(node);
        while (nodeStatus.hasChanged()) {
            logSteps(nodeStatus);

            steps.add(nodeStatus);
            TreeNode newNode = nodeStatus.getNewNode();
            node = NodeStatus.resetChangeGroups(newNode);
            nodeStatus = step(node);

            if (MAX_STEP_COUNT.equals(iters++)) {
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
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus breakUpNumeratorSearch(TreeNode node){
        // TODO Busqueda postOrder

        // Buscar una division
        if (node == null || !node.esDivision()) {
            return NodeStatus.noChange(node);
        }

        TreeNode numerator = node.getChild(0);
        if (!numerator.esOperador() || !numerator.esSuma()) {
            return NodeStatus.noChange(node);
        }

        // At this point, we know that node is a fraction and its numerator is a sum
        // of terms that can't be collected or combined, so we should break it up.
        List<TreeNode> fractionList = new ArrayList<>();
        TreeNode denominator = node.getRightNode();
        for(TreeNode child: numerator.getArgs()){
            TreeNode newFraction = TreeNode.createOperator("/", child, denominator);
            fractionList.add(newFraction);
        }

        TreeNode newNode = TreeNode.createOperator("+", fractionList);
        // Wrap in parens for cases like 2*(2+3)/5 => 2*(2/5 + 3/5)
        newNode = TreeNode.createParenthesis(newNode);
        node.setChangeGroup(1);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.BREAK_UP_FRACTION, node, newNode);

    }

    /**
     // Given an operator node, maybe collects and then combines if possible
     // e.g. 2x + 4x + y => 6x + y
     // e.g. 2x * x^2 * 5x => 10 x^4
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus collectAndCombineSearch(TreeNode node){
        // TODO Busqueda postOrder

        if (node.esSuma()) {
            NodeStatus status = collectAndCombineOperation(node);
            if (status.hasChanged()) {
                return status;
            }
            // we might also be able to just combine if they're all the same term
            // e.g. 2x + 4x + x (doesn't need collecting)
            return addLikeTerms(node, true);
        } else if (node.esProducto()) {
            // collect and combine involves there being coefficients pulled the front
            // e.g. 2x * x^2 * 5x => (2*5) * (x * x^2 * x) => ... => 10 x^4
            if (TreeUtils.canMultiplyLikeTermConstantNodes(node)) {
                return multiplyLikeTerms(node, true);
            }
            NodeStatus status = collectAndCombineOperation(node);
            if (status.hasChanged()) {
                return status;
            }
            // we might also be able to just combine polynomial terms
            // e.g. x * x^2 * x => ... => x^4
            return multiplyLikeTerms(node, true);
        } else {
            return NodeStatus.noChange(node);
        }
    }

    /**
     // Collects and combines (if possible) the arguments of an addition or
     // multiplication
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus collectAndCombineOperation(TreeNode node) {
        List<NodeStatus> substeps = new ArrayList<>();

        NodeStatus status = collectLikeTerms(node.cloneDeep());
        if (!status.hasChanged()) {
            return status;
        }

        // STEP 1: collect like terms, e.g. 2x + 4x^2 + 5x => 4x^2 + (2x + 5x)
        substeps.add(status);
        TreeNode newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // STEP 2 onwards: combine like terms for each group that can be combined
        // e.g. (x + 3x) + (2 + 2) has two groups
        List<NodeStatus> combineSteps = combineLikeTerms(newNode);
        if (combineSteps.size() > 0) {
            substeps.addAll(combineSteps);
            NodeStatus lastStep = combineSteps.get(combineSteps.size()- 1);
            newNode = NodeStatus.resetChangeGroups(lastStep.getNewNode());
        }

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_AND_COMBINE_LIKE_TERMS,
                node, newNode, substeps);
    }

    private NodeStatus collectLikeTerms(TreeNode node) {
        if (!canCollectLikeTerms(node)) {
            return NodeStatus.noChange(node);
        }

        Map<String, List<TreeNode>> terms;
        if (node.esSuma()) {
            terms = getTermsForCollectingAddition(node);
        } else if (node.esProducto()) {
            terms = getTermsForCollectingMultiplication(node);
        } else {
            throw new Error("Operation not supported: " + node.getValue());
        }

        // List the symbols alphabetically
        LinkedList<String> termTypesSorted = new LinkedList<>();
        for(String t: terms.keySet()){
            if (!CONSTANT.equals(t)  && !CONSTANT_FRACTION.equals(t) && !OTHER.equals(t)){
                termTypesSorted.add(t);
            }
        }
        Collections.sort(termTypesSorted);


        // Then add const
        if (terms.get(CONSTANT) != null) {
            // at the end for addition (since we'd expect x^2 + (x + x) + 4)
            if (node.esSuma()) {
                termTypesSorted.addLast(CONSTANT);
            }
            // for multipliation it should be at the front (e.g. (3*4) * x^2)
            if (node.esProducto()) {
                termTypesSorted.addFirst(CONSTANT);
            }
        }
        if (terms.get(CONSTANT_FRACTION)!=null) {
            termTypesSorted.addLast(CONSTANT_FRACTION);
        }

        // Collect the new operands under op.
        List<TreeNode> newOperands = new ArrayList<>();

        int changeGroup = 1;
        for(String termType: termTypesSorted){
            List<TreeNode> termsOfType = terms.get(termType);
            if (termsOfType.size() == 1){
                TreeNode singleTerm = termsOfType.get(0).cloneDeep();
                newOperands.add(singleTerm);
            }else{
                TreeNode termList = TreeNode.createParenthesis(
                        TreeNode.createOperator(node.getValue(), termsOfType)).cloneDeep();
                newOperands.add(termList);
            }
            for(TreeNode t: termsOfType){
                t.setChangeGroup(changeGroup);
            }
            changeGroup++;

        }

        // then stick anything else (paren nodes, operator nodes) at the end
        if (terms.get(OTHER)!=null) {
            newOperands.addAll(terms.get(OTHER));
        }

        TreeNode newNode = node.cloneDeep();
        newNode.setArgs(newOperands);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_LIKE_TERMS, node, newNode);
    }

    // Given an expression tree, returns true if there are terms that can be
    // collected
    private boolean canCollectLikeTerms(TreeNode node) {
        // We can collect like terms through + or through *
        // Note that we never collect like terms with - or /, those expressions will
        // always be manipulated in flattenOperands so that the top level operation is
        // + or *.
        if (!(node.esSuma() || node.esProducto())) {
            return false;
        }

        Map<String, List<TreeNode>> terms;
        if (node.esSuma()) {
            terms = getTermsForCollectingAddition(node);
        }

        else if ( node.esProducto()) {
            terms = getTermsForCollectingMultiplication(node);
        }
        else {
            throw new Error("Operation not supported: " + node.getValue());
        }

        // Conditions we need to meet to decide to to reorganize (collect) the terms:
        // - more than 1 term type
        // - more than 1 of at least one type (not including other)
        // (note that this means x^2 + x + x + 2 -> x^2 + (x + x) + 2,
        // which will be recorded as a step, but doesn't change the order of terms)
        Set<String> termTypes =  terms.keySet();
        Boolean hasSomeCollectNodes = false;
        for(String t: termTypes){
            if (!t.equals(OTHER)) {
                if (terms.get(t).size() > 1){
                    hasSomeCollectNodes = true;
                }
            }
        }

        return (termTypes.size() > 1 &&
                hasSomeCollectNodes);
    }

    // Collects like terms in an addition expression tree into categories.
    // Returns a dictionary of termname to lists of nodes with that name
    // e.g. 2x + 4 + 5x would return {'x': [2x, 5x], CONSTANT: [4]}
    // (where 2x, 5x, and 4 would actually be expression trees)
    private Map<String, List<TreeNode>> getTermsForCollectingAddition(TreeNode node) {

        Map<String, List<TreeNode>> terms = new HashMap<>();

        for (int i = 0; i < node.getArgs().size(); i++) {
            TreeNode child = node.getChild(i);

            if (TreeUtils.isPolynomialTerm(child)) {
                String termName = "X";
                if (child.getExponent() != 1) {
                    termName += "^" + child.getExponent().toString();
                }
                appendToArrayInObject(terms, termName, child);
            }
            //  else if (Node.NthRootTerm.isNthRootTerm(child)) {
            //     String termName = getTermName(child, Node.NthRootTerm, '+');
            //     terms = appendToArrayInObject(terms, termName, child);
            // }
            else if (TreeUtils.isIntegerFraction(child)) {
                appendToArrayInObject(terms, CONSTANT_FRACTION, child);
            } else if (TreeUtils.isConstant(child)) {
                appendToArrayInObject(terms, CONSTANT, child);
            } else if (node.esOperador() ||
                    node.isParenthesis() ||
                    node.isUnaryMinus()) {
                appendToArrayInObject(terms, OTHER, child);
            } else {
                // Note that we shouldn't get any symbol nodes in the switch statement
                // since they would have been handled by isPolynomialTerm
                throw new Error("Unsupported node type: " + child.getValue());
            }
        }
        return terms;
    }

    private void appendToArrayInObject(Map<String, List<TreeNode>> terms, String termName, TreeNode child) {

        List<TreeNode> nodes = terms.get(termName);
        if (nodes == null){
            nodes = new ArrayList<>();
            terms.put(termName, nodes);
        }
        nodes.add(child);

    }
    private Map<String,List<TreeNode>> getTermsForCollectingMultiplication(TreeNode node) {

        Map<String, List<TreeNode>> terms = new HashMap<>();

        for (int i = 0; i < node.getArgs().size(); i++) {
            TreeNode child = node.getChild(i);

            if (child.isUnaryMinus()) {
                appendToArrayInObject(terms, CONSTANT, TreeNode.createConstant(CONSTANT_1_NEG));
                child = child.getChild(0);
            }
            if (TreeUtils.isPolynomialTerm(child)) {

                String termName = "X";
                if (child.getExponent() != 1) {
                    termName += "^" + child.getExponent().toString();
                }


                if (child.getCoefficient() == 1) {
                    appendToArrayInObject(terms, termName, child);
                }else{
                    // En este caso separo los terminos así se multiplican las constantes
                    appendToArrayInObject(terms, termName, new TreeNode(termName));
                    appendToArrayInObject(terms, CONSTANT, TreeNode.createConstant(child.getCoefficient()));
                }
            }
           // else if (Node.Type.isFunction(child, 'nthRoot')) {
           //    terms = addToTermsforNthRootMultiplication(terms, child);
           // }
            else if (TreeUtils.isIntegerFraction(child)) {
                appendToArrayInObject(terms, CONSTANT, child);
            }
            else if (TreeUtils.isConstant(child)) {
                appendToArrayInObject(terms, CONSTANT, child);
            }
            else if (node.esOperador() ||
                    node.isParenthesis()) {
                appendToArrayInObject(terms, OTHER, child);
            }
            else {
                // Note that we shouldn't get any symbol nodes in the switch statement
                // since they would have been handled by isPolynomialTerm
                throw new Error("Unsupported node type: " + child.getValue());
            }
        }
        return terms;
    }

    // step 2 onwards for collectAndCombineOperation
    // combine like terms for each group that can be combined
    // e.g. (x + 3x) + (2 + 2) has two groups
    // returns a list of combine steps
    private List<NodeStatus> combineLikeTerms(TreeNode node) {
        List<NodeStatus> steps = new ArrayList<>();
        TreeNode newNode = node.cloneDeep();

        for (int i = 0; i < node.getArgs().size(); i++) {
            TreeNode child = node.getChild(i);
            // All groups of terms will be surrounded by parenthesis
            if (!child.isParenthesis()) {
                continue;
            }
            child = child.getChild(0);
            NodeStatus childStatus;
            if (newNode.esSuma()){
                childStatus = addLikeTerms(child, false);
            }else if (newNode.esProducto()){
                childStatus = multiplyLikeTerms(child, false);
            }else{
                throw new Error("Operador no soportado: " + newNode.getValue());
            }

            if (childStatus.hasChanged()) {
                NodeStatus status = NodeStatus.childChanged(newNode, childStatus, i);
                steps.add(status);
                newNode = NodeStatus.resetChangeGroups(status.getNewNode());
            }
        }

        return steps;
    }

    /**
     // Distributes through parenthesis.
     // e.g. 2(x+3) -> (2*x + 2*3)
     // e.g. -(x+5) -> (-x + -5)
     // Returns a Node.Status object.
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus distributeSearch(TreeNode node){
        // TODO Busqueda postOrder

        if (node.isUnaryMinus()) {
            return distributeUnaryMinus(node);
        }
        else if (node.esProducto()) {
            return distributeAndSimplifyMultiplication(node);
        }
        else if (node.esPotencia()) {
            return expandBase(node);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Distributes unary minus into a parenthesis node.
    // e.g. -(4*9*x^2) --> (-4 * 9  * x^2)
    // e.g. -(x + y - 5) --> (-x + -y + 5)
    // Returns a Node.Status object.
    private NodeStatus distributeUnaryMinus(TreeNode node) {
        if (!node.isUnaryMinus()) {
            return NodeStatus.noChange(node);
        }
        TreeNode unaryContent = node.getChild(0);
        if (!unaryContent.isParenthesis()) {
            return NodeStatus.noChange(node);
        }
        TreeNode content = unaryContent.getChild(0);
        if (!content.esOperador()) {
            return NodeStatus.noChange(node);
        }
        TreeNode newContent = content.cloneDeep();
        node.setChangeGroup(1);
        // For multiplication and division, we can push the unary minus in to
        // the first argument.
        // e.g. -(2/3) -> (-2/3)    -(4*9*x^2) --> (-4 * 9  * x^2)
        if (content.esProducto() || content.esDivision()) {
            newContent.setChild(0, TreeUtils.negate(newContent.getChild(0)));
            newContent.getChild(0).setChangeGroup(1);
            TreeNode newNode = TreeNode.createParenthesis(newContent);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DISTRIBUTE_NEGATIVE_ONE, node, newNode);
        }  else if (content.esSuma()) {
            // Now we know `node` is of the form -(x + y + ...).
            // We want to now return (-x + -y + ....)
            // If any term is negative, we make it positive it right away
            // e.g. -(2-4) => -2 + 4
            List<TreeNode> newArgs = new ArrayList<>();
            for(TreeNode child: newContent.getArgs() ){
                TreeNode newArg = TreeUtils.negate(child);
                newArg.setChangeGroup(1);
                newArgs.add(newArg);
            }

            newContent.setArgs(newArgs);
            TreeNode newNode = TreeNode.createParenthesis(newContent);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DISTRIBUTE_NEGATIVE_ONE, node, newNode);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Distributes a pair of terms in a multiplication operation, if a pair
    // can be distributed. To be distributed, there must be two terms beside
    // each other, and at least one of them must be a parenthesis node.
    // e.g. 2*(3+x) or (4+x^2+x^3)*(x+3)
    // Returns a Node.Status object with substeps
    private NodeStatus distributeAndSimplifyMultiplication(TreeNode node) {
        if (!node.esOperador() || node.esProducto()) {
            return NodeStatus.noChange(node);
        }

        // STEP 1: distribute with `distributeTwoNodes`
        // e.g. x*(2+x) -> x*2 + x*x
        // STEP 2: simplifications of each operand in the new sum with `simplify`
        // e.g. x*2 + x*x -> ... -> 2x + x^2
        for (int i = 0; i+1 < node.getArgs().size(); i++) {
            if (!isParenthesisOfAddition(node.getChild(i)) &&
                    !isParenthesisOfAddition(node.getChild(i+1))) {
                continue;
            }
            TreeNode newNode = node.cloneDeep();
            List<NodeStatus> substeps = new ArrayList<>();
            NodeStatus status;

            TreeNode combinedNode = distributeTwoNodes(newNode.getChild(i), newNode.getChild(i+1));
            node.getChild(i).setChangeGroup(1);
            node.getChild(i+1).setChangeGroup(1);
            combinedNode.setChangeGroup(1);

            if (newNode.getArgs().size() > 2) {
                newNode.setChild(i, combinedNode);
                newNode.removeChild(i+1);
                newNode.getChild(i).setChangeGroup(1);
            } else {
                newNode = combinedNode;
                newNode.setChangeGroup(1);
            }

            status = NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DISTRIBUTE, node, newNode);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());

            // case 1: there were more than two operands in this multiplication
            // e.g. 3*7*(2+x)*(3+x)*(4+x) is a multiplication node with 5 children
            // and the new node will be 3*(14+7x)*(3+x)*(4+x) with 4 children.
            if (newNode.esProducto()) {
                NodeStatus childStatus = simplifyWithParens(newNode.getChild(0));
                if (childStatus.hasChanged()) {
                    status = NodeStatus.childChanged(newNode, childStatus, i);
                    substeps.add(status);
                    newNode = NodeStatus.resetChangeGroups(status.getNewNode());
                }
            }
            // case 2: there were only two operands and we multiplied them together.
            // e.g. 7*(2+x) -> (7*2 + 7*x)
            // Now we can just simplify it.
            else if (newNode.isParenthesis()){
                status = simplifyWithParens(newNode);
                if (status.hasChanged()) {
                    substeps.add(status);
                    newNode = NodeStatus.resetChangeGroups(status.getNewNode());
                }
            }
            else {
                throw new Error("Unsupported node type for distribution: " + node);
            }

            if (substeps.size() == 1) {
                return substeps.get(0);
            }

            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.DISTRIBUTE, node, newNode, substeps);
        }
        return NodeStatus.noChange(node);
    }

    // Distributes two nodes together. At least one node must be parenthesis node
    // e.g. 2*(x+3) -> (2*x + 2*3)       (5+x)*x -> 5*x + x*x
    // e.g. (5+x)*(x+3) -> (5*x + 5*3 + x*x + x*3)
    // Returns a node.
    private TreeNode distributeTwoNodes(TreeNode firstNode, TreeNode secondNode) {
        // lists of terms we'll be multiplying together from each node
        List<TreeNode> firstArgs, secondArgs;
        if (isParenthesisOfAddition(firstNode)) {
            firstArgs = firstNode.getChild(0).getArgs();
        } else {
            firstArgs =  Collections.singletonList(firstNode);
        }

        if (isParenthesisOfAddition(secondNode)) {
            secondArgs = secondNode.getChild(0).getArgs();
        }
        else {
            secondArgs = Collections.singletonList(secondNode);
        }
        // the new operands under addition, now products of terms
        List<TreeNode> newArgs = new ArrayList<>();

        int numFractions = 0;
        for(TreeNode node: firstArgs){
            if (node.esDivision()){
                numFractions++;
            }
        }
        for(TreeNode node: secondArgs){
            if (node.esDivision()){
                numFractions++;
            }
        }

        // if exactly one group contains at least one fraction, multiply the
        // non-fraction group into the numerators of the fraction group
        if (CONSTANT_1.equals(numFractions)) {
            Boolean firstArgsHasFraction = hasFraction(firstArgs);
            List<TreeNode> fractionNodes = firstArgsHasFraction ? firstArgs : secondArgs;
            TreeNode nonFractionTerm = firstArgsHasFraction ? secondNode : firstNode;

            for(TreeNode node: fractionNodes) {
                TreeNode arg;
                if (node.esDivision()) {
                    TreeNode numerator = TreeNode.createOperator("*",node.getChild(0), nonFractionTerm);
                    numerator = TreeNode.createParenthesis(numerator);
                    arg = TreeNode.createOperator("/", numerator, node.getChild(1));
                } else {
                    arg = TreeNode.createOperator("*",node, nonFractionTerm);
                }
                arg.setChangeGroup(1);
                newArgs.add(arg);
            }
        }
        // e.g. (4+x)(x+y+z) will become 4(x+y+z) + x(x+y+z) as an intermediate
        // step.
        else if (firstArgs.size() > 1 && secondArgs.size() > 1) {

            for(TreeNode leftArg : firstArgs){
                TreeNode arg = TreeNode.createOperator("*", leftArg, secondNode);
                arg.setChangeGroup(1);
                newArgs.add(arg);
            }
        }
        else {
            // a list of all pairs of nodes between the two arg lists
            for(TreeNode leftArg: firstArgs){
                for(TreeNode rightArg: secondArgs){
                    TreeNode arg = TreeNode.createOperator("*", leftArg, rightArg);
                    arg.setChangeGroup(1);
                    newArgs.add(arg);
                }
            }
        }
        return TreeNode.createParenthesis(TreeNode.createOperator("+", newArgs));
    }

    private Boolean hasFraction(List<TreeNode> args) {
        for(TreeNode node: args){
            if (node.esDivision()){ return true;}
        }
        return false;
    }

    // Simplifies a sum of terms (a result of distribution) that's in parens
    // (note that all results of distribution are in parens)
    // e.g. 2x*(4 + x) distributes to (2x*4 + 2x*x)
    // This is a separate function from simplify to make the flow more readable,
    // but this is literally just a wrapper around 'simplify'.
    // Returns a Node.Status object
    private NodeStatus simplifyWithParens(TreeNode node) {
        if (!node.isParenthesis()) {
            throw new Error("expected " + node + " to be a parenthesis node");
        }

        NodeStatus status = simplify(node.getChild(0));
        if (status.hasChanged()) {
            return NodeStatus.childChanged(node, status, 0);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Simplifies a sum of terms that are a result of distribution.
    // e.g. (2x+3)*(4x+5) -distribute-> 2x*(4x+5) + 3*(4x+5) <- 2 terms to simplify
    // e.g. 2x*(4x+5) --distribute--> 2x*4x + 2x*5 --simplify--> 8x^2 + 10x
    // Returns a Node.Status object.
    private NodeStatus simplify(TreeNode node) {
        List<NodeStatus> substeps = new ArrayList<>();

        TreeNode newNode = node.cloneDeep();
        for (int i = 0; i < newNode.getArgs().size(); i++) {

            // e.g. 2*9 -> 18
            NodeStatus childStatus = arithmeticSearch(node);
            if (childStatus.hasChanged()) {
                NodeStatus status = NodeStatus.childChanged(newNode, childStatus, i);
                substeps.add(status);
                newNode = NodeStatus.resetChangeGroups(status.getNewNode());
            }

            // e.g. x*5 -> 5x
            childStatus = rearrangeCoefficient(node);
            if (childStatus.hasChanged()) {
                NodeStatus status = NodeStatus.childChanged(newNode, childStatus, i);
                substeps.add(status);
                newNode = NodeStatus.resetChangeGroups(status.getNewNode());
            }

            // e.g 2x*4x -> 8x^2
            childStatus = collectAndCombineSearch(node);
            if (childStatus.hasChanged()) {
                NodeStatus status = NodeStatus.childChanged(newNode, childStatus, i);
                substeps.add(status);
                newNode = NodeStatus.resetChangeGroups(status.getNewNode());
            }

            // e.g. (2+x)(3+x) -> 2*(3+x) recurses
            childStatus = distributeAndSimplifyMultiplication(node);
            if (childStatus.hasChanged()) {
                NodeStatus status = NodeStatus.childChanged(newNode, childStatus, i);
                substeps.add(status);
                newNode = NodeStatus.resetChangeGroups(status.getNewNode());
            }
        }

        // possible in cases like 2(x + y) -> 2x + 2y -> doesn't need simplifying
        if (substeps.size() == 0) {
            return NodeStatus.noChange(node);
        }
        else {
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.SIMPLIFY_TERMS, node, newNode, substeps);
        }
    }

    // returns true if `node` is of the type (node + node + ...)
    private boolean isParenthesisOfAddition(TreeNode node) {
        if (!node.isParenthesis()) {
            return false;
        }
        TreeNode content = node.getChild(0);
        return content.esSuma();
    }

    // Expand a power node with a non-constant base and a positive exponent > 1
    // e.g. (nthRoot(x, 2))^2 -> nthRoot(x, 2) * nthRoot(x, 2)
    // e.g. (2x + 3)^2 -> (2x + 3) (2x + 3)
    private NodeStatus expandBase(TreeNode node) {
        // Must be a power node and the exponent must be a constant
        // Base must either be an nthRoot or sum of terms
        if (!node.esPotencia()) {
            return NodeStatus.noChange(node);
        }

        TreeNode base =  node.getChild(0).isParenthesis()
                ? node.getChild(0).getChild(0)
                : node.getChild(0);

        TreeNode exponent = node.getChild(1).isParenthesis()
                ? node.getChild(1).getChild(0)
                : node.getChild(1);

        Integer exponentValue = exponent.getIntegerValue();

        // Exponent should be a positive integer
        if (exponentValue<= 0) {
            return NodeStatus.noChange(node);
        }

        if (!base.esRaiz() && !base.esSuma()) {
            return NodeStatus.noChange(node);
        }

        // If the base is an nthRoot node, it doesn't need the parenthesis
        TreeNode expandedBase = base.esRaiz()
                ? base
                : node.getChild(0);

        List<TreeNode> newArgs = new ArrayList<>();
        for(int i =0; i < exponentValue; i++){
            newArgs.add(expandedBase);
        }

        TreeNode expandedNode = TreeNode.createOperator("*", newArgs);

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.EXPAND_EXPONENT, node, expandedNode);
    }

    /**
     // Searches for and simplifies any chains of division or nested division.
     // Returns a Node.Status object
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus divisionSearch(TreeNode node){
        // TODO Busqueda preOrder

        if (!node.esOperador() || !node.esDivision()) {
            return NodeStatus.noChange(node);
        }
        // e.g. 2/(x/6) => 2 * 6/x
        NodeStatus nodeStatus =  multiplyByInverse(node);
        if (nodeStatus.hasChanged()) {
            return nodeStatus;
        }
        // e.g. 2/x/6 -> 2/(x*6)
        nodeStatus = simplifyDivisionChain(node);
        if (nodeStatus.hasChanged()) {
            return nodeStatus;
        }
        return NodeStatus.noChange(node);
    }

    // If `node` is a fraction with a denominator that is also a fraction, multiply
    // by the inverse.
    // e.g. x/(2/3) -> x * 3/2
    private NodeStatus multiplyByInverse(TreeNode node) {
        TreeNode denominator = node.getChild(1);
        if (denominator.isParenthesis()) {
            denominator = denominator.getChild(0);
        }
        if (!denominator.esOperador() || !denominator.esDivision()) {
            return NodeStatus.noChange(node);
        }
        // At this point, we know that node is a fraction and denonimator is the
        // fraction we need to inverse.
        TreeNode inverseNumerator = denominator.getChild(1);
        TreeNode inverseDenominator = denominator.getChild(0);
        TreeNode inverseFraction = TreeNode.createOperator(
                    "/", inverseNumerator, inverseDenominator);

        TreeNode newNode = TreeNode.createOperator("*", node.getChild(0), inverseFraction);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_BY_INVERSE, node, newNode);
    }

    // Simplifies any chains of division into a single division operation.
    // e.g. 2/x/6 -> 2/(x*6)
    // Returns a Node.Status object
    private NodeStatus simplifyDivisionChain(TreeNode node) {
        // check for a chain of division
        LinkedList<TreeNode> denominatorList = getDenominatorList(node);
        // one for the numerator, and at least two terms in the denominator
        if (denominatorList.size() > 2) {
            TreeNode numerator = denominatorList.pollFirst();
            // the new single denominator is all the chained denominators
            // multiplied together, in parentheses.
            TreeNode denominator = TreeNode.createParenthesis(
                    TreeNode.createOperator("*", denominatorList));
            TreeNode newNode = TreeNode.createOperator("/", numerator, denominator);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.SIMPLIFY_DIVISION, node, newNode);
        }
        return NodeStatus.noChange(node);
    }

    // Given a the denominator of a division node, returns all the nested
    // denominator nodess. e.g. 2/3/4/5 would return [2,3,4,5]
    // (note: all the numbers in the example are actually constant nodes)
    private LinkedList<TreeNode> getDenominatorList(TreeNode denominator) {
        TreeNode node = denominator;
        LinkedList<TreeNode> denominatorList = new LinkedList<>();
        while (node.esDivision()) {
            // unshift the denominator to the front of the list, and recurse on
            // the numerator
            denominatorList.addFirst(node.getChild(1));
            node = node.getChild(0);
        }
        // unshift the final node, which wasn't a / node
        denominatorList.addFirst(node);
        return denominatorList;
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
        if(TreeUtils.isConstant(treeNode) || TreeUtils.isSymbol(treeNode,false)) {
            return NodeStatus.noChange(treeNode);
        }
        if(treeNode.isUnaryMinus()) {
            NodeStatus status = functionsSearch(treeNode.getLeftNode());
            if (status.hasChanged()) {
                return NodeStatus.childChanged(treeNode, status);
            }
        }
        // TODO preguntar aca si nodo es funcion
        if(treeNode.esOperador()){
            for (TreeNode arg: treeNode.getArgs()) {
                NodeStatus status = functionsSearch(arg);
                if(status.hasChanged()){
                    return NodeStatus.childChanged(arg,status);
                }
            }
        }
        if(treeNode.isParenthesis()){
            NodeStatus status = functionsSearch(treeNode.getLeftNode());
            if(status.hasChanged()){
                return NodeStatus.childChanged(treeNode,status);
            }
        }

        return NodeStatus.noChange(treeNode);
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
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    protected NodeStatus multiplyFractionsSearch(TreeNode node){
        // TODO Busqueda postOrder

        if (!node.esOperador() || !node.esProducto()) {
            return NodeStatus.noChange(node);
        }

        // we need to use the verbose syntax for `some` here because isFraction
        // can take more than one parameter
        Boolean atLeastOneFraction = false;
        Boolean hasPolynomialTerms = false;
        Boolean hasPolynomialInDenominatorTerms = false;
        for(TreeNode child: node.getArgs()){
            if (TreeUtils.isFraction(child)){
                atLeastOneFraction = true;
            }else if (TreeUtils.isPolynomialTerm(child)){
                hasPolynomialTerms = true;
            }else if (TreeUtils.hasPolynomialInDenominator(child)){
                hasPolynomialInDenominatorTerms = true;
            }
        }

        if (!atLeastOneFraction || (hasPolynomialTerms && !hasPolynomialInDenominatorTerms)) {
            return NodeStatus.noChange(node);
        }

        List<TreeNode> numeratorArgs = new ArrayList<>();
        List<TreeNode> denominatorArgs = new ArrayList<>();
        for(TreeNode child: node.getArgs()){
            if (TreeUtils.isFraction(child)){
                TreeNode fraction = TreeUtils.getFraction(child);
                numeratorArgs.add(fraction.getChild(0));
                denominatorArgs.add(fraction.getChild(1));
            }else{
                numeratorArgs.add(child);
            }
        }

        TreeNode newNumerator = TreeNode.createParenthesis(
                TreeNode.createOperator("*", numeratorArgs));
        TreeNode newDenominator = denominatorArgs.size() == 1
                ? denominatorArgs.get(0)
                : TreeNode.createParenthesis(TreeNode.createOperator("*", denominatorArgs));

        TreeNode newNode = TreeNode.createOperator("/", newNumerator, newDenominator);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.MULTIPLY_FRACTIONS, node, newNode);
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

            TreeNode numeratorNode =  treeNode.getLeftNode().cloneDeep();
            if (numeratorNode.esOperador()){
                numeratorNode = TreeNode.createParenthesis(numeratorNode);
            }
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

    // Simplifies two unary minuses in a row by removing both of them.
    // e.g. -(- 4) --> 4
    protected NodeStatus simplifyDoubleUnaryMinus(TreeNode node) {
        if (!node.isUnaryMinus()) {
            return NodeStatus.noChange(node);
        }

        TreeNode unaryArg = node.getChild(0);
        // e.g. in - -x, -x is the unary arg, and we'd want to reduce to just x
        if (unaryArg.isUnaryMinus()) {
            TreeNode newNode = unaryArg.getChild(0).cloneDeep();
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS, node, newNode);
        }
        // e.g. - -4, -4 could be a constant with negative value
        else if (TreeUtils.isConstant(unaryArg) && unaryArg.getIntegerValue() < 0) {
            TreeNode newNode = TreeNode.createConstant(unaryArg.getIntegerValue() * -1);
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS, node, newNode);
        }
        // e.g. -(-(5+2))
        else if (unaryArg.isParenthesis()) {
            TreeNode parenthesisContent = unaryArg.getChild(0);
            if (parenthesisContent.isUnaryMinus()) {
                TreeNode newNode = TreeNode.createParenthesis(parenthesisContent.getChild(0));
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS, node, newNode);
            }
        }
        return NodeStatus.noChange(node);
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
        TreeNode newNumerator = TreeNode.createParenthesis(
                TreeNode.createOperator("+", numeratorArgs));

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
                TreeNode newNumerator = TreeNode.createParenthesis(
                        TreeNode.createOperator("*",
                        TreeNode.createConstant(child.getLeftNode().getIntegerValue()),
                        TreeNode.createConstant(missingFactor)));

                // new denominator: (num * missingFactor)
                TreeNode newDenominator = TreeNode.createParenthesis(
                        TreeNode.createOperator("*",
                        TreeNode.createConstant(child.getRightNode().getIntegerValue()),
                        TreeNode.createConstant(missingFactor)));

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
     * @param node Nodo a evaluar
     * @return El estado de la simplificacion
     */
    private NodeStatus evaluateConstantSum(TreeNode node) {

        if (node.isParenthesis()) {
            node = node.getChild(0);
        }

        // Buscar un suma o resta
        if (!node.esAditivo()) {
            return NodeStatus.noChange(node);
        }

        // Alguno de los hijos debe ser constante o fraccion
        for (TreeNode child: node.getArgs()){
            if (!TreeUtils.isConstantOrConstantFraction(child)){
                return NodeStatus.noChange(node);
            }
        }

        // functions needed to evaluate the sum
        NodeStatus nodeStatus;

        nodeStatus = arithmeticSearch(node);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}

        nodeStatus = addConstantFractions(node);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}

        nodeStatus = addConstantAndFraction(node);
        if (nodeStatus.hasChanged() && TreeUtils.isConstantOrConstantFraction(nodeStatus.getNewNode())){return nodeStatus;}


        TreeNode newNode = node.cloneDeep();
        List<NodeStatus> substeps = new ArrayList<>();
        NodeStatus status;


        // STEP 1: group fractions and constants separately
        status = groupConstantsAndFractions(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        TreeNode constants = newNode.getChild(0);
        TreeNode fractions = newNode.getChild(1);

        // STEP 2A: evaluate arithmetic IF there's > 1 constant
        // (which is the case if it's a list surrounded by parenthesis)
        if (constants.isParenthesis()) {
            TreeNode constantList = constants.getChild(0);
            NodeStatus evaluateStatus = arithmeticSearch(constantList);
            status = NodeStatus.childChanged(newNode, evaluateStatus, 0);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        // STEP 2B: add fractions IF there's > 1 fraction
        // (which is the case if it's a list surrounded by parenthesis)
        if (fractions.isParenthesis()) {
            TreeNode fractionList = fractions.getChild(0);
            NodeStatus evaluateStatus = addConstantFractions(fractionList);
            status = NodeStatus.childChanged(newNode, evaluateStatus, 1);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        // STEP 3: combine the evaluated constant and fraction
        // the fraction might have simplified to a constant (e.g. 1/3 + 2/3 -> 2)
        // so we just call evaluateConstantSum again to cycle through
        status = evaluateConstantSum(newNode);
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC, node, newNode, substeps);
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

        List<TreeNode> fractions = new ArrayList<>();
        List<TreeNode> constants = new ArrayList<>();
        for(TreeNode child: node.getArgs()){
            if (TreeUtils.isIntegerFraction(child)){
                fractions.add(child);
            }else if (TreeUtils.isConstant(child)){
                constants.add(child);
            }
        }

        if (fractions.isEmpty() || constants.isEmpty()) {
            throw new Error("expected both integer fractions and constants, got " + node.toExpression());
        }

        if (fractions.size() + constants.size() != node.getArgs().size()) {
            throw new Error("can only evaluate integer fractions and constants");
        }


        List<TreeNode> clonedConstants = new ArrayList<>();
        for (TreeNode constant: constants){
            // set the changeGroup - this affects both the old and new node
            constant.setChangeGroup(1);
            // clone so that node and newNode aren't stored in the same memory
            clonedConstants.add(constant.cloneDeep());
        }

        // wrap in parenthesis if there's more than one, to group them
        TreeNode constantNode;
        if (clonedConstants.size() > 1) {
            constantNode = TreeNode.createParenthesis(TreeNode.createOperator("+", clonedConstants));

        } else {
            constantNode = clonedConstants.get(0);
        }

        List<TreeNode> clonedFractions = new ArrayList<>();
        for (TreeNode fraction: fractions){
            // set the changeGroup - this affects both the old and new node
            fraction.setChangeGroup(2);
            // clone so that node and newNode aren't stored in the same memory
            clonedFractions.add(fraction.cloneDeep());
        }

        // wrap in parenthesis if there's more than one, to group them
        TreeNode fractionNode;
        if (clonedFractions.size() > 1) {
            fractionNode = TreeNode.createParenthesis(TreeNode.createOperator("+", clonedFractions));
        }
        else {
            fractionNode = clonedFractions.get(0);
        }

        TreeNode newNode = TreeNode.createOperator("+", constantNode, fractionNode);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_LIKE_TERMS, node, newNode);
    }

    protected NodeStatus addLikePolynomialTerms(TreeNode node) {
        if (!canAddLikeTermPolynomialNodes(node)) {
            return NodeStatus.noChange(node);
        }

        return addLikeTermNodes(
                node, POLYNOMIAL_TERM, NodeStatus.ChangeTypes.ADD_POLYNOMIAL_TERMS);
    }

    // Returns true if the nodes are polynomial terms that can be added together.
    private boolean canAddLikeTermPolynomialNodes(TreeNode node) {
        return canAddLikeTermNodes(node, NTH_ROOT_TERM);
    }

    protected NodeStatus addLikeNthRootTerms(TreeNode node) {
        if (!canAddLikeTermNthRootNodes(node)) {
            return NodeStatus.noChange(node);
        }

        return addLikeTermNodes(
                node, NTH_ROOT_TERM, NodeStatus.ChangeTypes.ADD_NTH_ROOTS);
    }

    // Returns true if the nodes are nth roots that can be added together
    private boolean canAddLikeTermNthRootNodes(TreeNode node) {
        return canAddLikeTermNodes(node, NTH_ROOT_TERM);
    }

    // Returns true if the nodes are terms that can be added together.
    // The nodes need to have the same base and exponent
    // e.g. 2x + 5x, 6x^2 + x^2, nthRoot(4,2) + nthRoot(4,2)
    private boolean canAddLikeTermNodes(TreeNode node, String termSubclass) {
        if (!node.esSuma()) {
            return false;
        }
        List<TreeNode> args = node.getArgs();
   //     if (!args.every(n => Node.Term.isTerm(n, termSubclass.baseNodeFunc))) {
   //        return false;
   //     }
        if (args.size() == 1) {
            return false;
        }

        // to add terms, they must have the same base *and* exponent
        TreeNode firstTerm = args.get(0);
        Integer sharedExponentNode = firstTerm.getExponent();
        // Integer sharedBase = firstTerm.getBase();
        // TODO Es necesario calcular la base?
        for(TreeNode child: args){
            if (!sharedExponentNode.equals(child.getExponent())){
                return false;
            }
        }

        return true;
    }

    // Helper function for adding together a list of nodes
    // belonging to a subclass of Term
    protected NodeStatus addLikeTermNodes(TreeNode node, String termSubclass, NodeStatus.ChangeTypes changeType) {
        List<NodeStatus> substeps = new ArrayList<>();
        TreeNode newNode = node.cloneDeep();

        // STEP 1: If any nodes have no coefficient, make it have coefficient 1
        // (this step only happens under certain conditions and later steps might
        // happen even if step 1 does not)
        NodeStatus status = addPositiveOneCoefficient(newNode, termSubclass);
        if (status.hasChanged()) {
            substeps.add(status);
            TreeNode statusNewNode = status.getNewNode();
            newNode = NodeStatus.resetChangeGroups(statusNewNode);
        }

        // STEP 2: If any nodes have a unary minus, make it have coefficient -1
        // (this step only happens under certain conditions and later steps might
        // happen even if step 2 does not)
        status = addNegativeOneCoefficient(newNode, termSubclass);
        if (status.hasChanged()) {
            substeps.add(status);
            TreeNode statusNewNode = status.getNewNode();
            newNode = NodeStatus.resetChangeGroups(statusNewNode);
        }

        // STEP 3: group the coefficients in a sum
        status = groupCoefficientsForAdding(newNode, termSubclass);
        substeps.add(status);
        TreeNode statusNewNode = status.getNewNode();
        newNode = NodeStatus.resetChangeGroups(statusNewNode);

        // STEP 4: evaluate the sum (could include fractions)
        status = evaluateCoefficientSum(newNode, termSubclass);
        substeps.add(status);
        TreeNode evalateStatusNewNode = status.getNewNode();
        newNode = NodeStatus.resetChangeGroups(evalateStatusNewNode);

        return NodeStatus.nodeChanged(
                changeType, node, newNode, substeps);
    }

    // Given a sum of like terms, changes any term with no coefficient
    // into a term with an explicit coefficient of 1. This is for pedagogy, and
    // makes the adding coefficients step clearer.
    // e.g. 2x + x -> 2x + 1x
    // Returns a Node.Status object.
    private NodeStatus addPositiveOneCoefficient(TreeNode node, String termSubclass) {
        TreeNode newNode = node.cloneDeep();
        Boolean change = false;

        Integer changeGroup = 1;
        int i = 0;
        for(TreeNode child: newNode.getArgs()){
            if (CONSTANT_1.equals(child.getCoefficient())) {
                TreeNode newChildNode = child.clone();
                newChildNode.setExplicitCoeff(true);
                newNode.getChild(i).setChangeGroup(changeGroup);
                node.getChild(i).setChangeGroup(changeGroup); // note that this is the "oldNode"

                change = true;
                changeGroup++;
            }
            i++;
        }

        if (change) {
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.ADD_COEFFICIENT_OF_ONE, node, newNode);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Given a sum of like terms, changes any term with a unary minus
    // coefficient into a term with an explicit coefficient of -1. This is for
    // pedagogy, and makes the adding coefficients step clearer.
    // e.g. 2x - x -> 2x - 1x
    // Returns a Node.Status object.
    private NodeStatus addNegativeOneCoefficient(TreeNode node, String termSubclass) {
        TreeNode newNode = node.cloneDeep();
        Boolean change = false;

        Integer changeGroup = 1;
        int i = 0;
        for(TreeNode child: newNode.getArgs()){
            if (CONSTANT_1_NEG.equals(child.getCoefficient())) {
                TreeNode newChildNode = child.clone();
                newChildNode.setExplicitCoeff(true);
                newNode.getChild(i).setChangeGroup(changeGroup);
                node.getChild(i).setChangeGroup(changeGroup); // note that this is the "oldNode"

                change = true;
                changeGroup++;
            }
            i++;
        }

        if (change) {
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.UNARY_MINUS_TO_NEGATIVE_ONE, node, newNode);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Given a sum of like terms, groups the coefficients
    // e.g. 2x^2 + 3x^2 + 5x^2 -> (2+3+5)x^2
    // Returns a Node.Status object.
    private NodeStatus groupCoefficientsForAdding(TreeNode node, String termSubclass) {

        TreeNode newNode = node.cloneDeep();
        List<TreeNode> coefficientList = new ArrayList<>();
        for(TreeNode child: newNode.getArgs()){
            coefficientList.add(TreeNode.createConstant(child.getCoefficient()));
        }

        TreeNode sumOfCoefficents = TreeNode.createParenthesis(
                TreeNode.createOperator("+", coefficientList));

        sumOfCoefficents.setChangeGroup(1);

        // terms that can be added together must share the same base
        // name and exponent. Get that base and exponent from the first term
        TreeNode firstTerm = node.getChild(0).clone();
        firstTerm.setCoefficient(1);
        firstTerm.setExplicitCoeff(false);
        newNode = TreeNode.createOperator("*",
                sumOfCoefficents, firstTerm);

            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.GROUP_COEFFICIENTS, node, newNode);
    }

    // Given a node of the form (2 + 4 + 5)x -- ie the coefficients have been
    // grouped for adding -- add the coefficients together to make a new coeffient
    // that is a constant or constant fraction.
    private NodeStatus evaluateCoefficientSum(TreeNode node, String termSubclass) {
        // the node is now always a * node with the left child the coefficent sum
        // e.g. (2 + 4 + 5) and the right node the symbol part e.g. x or y^2
        // so we want to evaluate args[0]
        TreeNode coefficientSum = node.cloneDeep().getChild(0);
        NodeStatus childStatus = evaluateConstantSum(coefficientSum);
        return NodeStatus.childChanged(node, childStatus, 0);
    }

    // Multiplies a list of nodes that are polynomial or constant power like terms.
    // Returns a node.
    // The polynomial nodes should *not* have coefficients. (multiplying
    // coefficients is handled in collecting like terms for multiplication)
    protected NodeStatus multiplyLikeTerms(TreeNode node, Boolean polynomialOnly){
        // Multiplicar terminos con X. Ejemplo: 2x * x^2 * 5x => 10 x^4

        if (!node.esOperador()) {
            return NodeStatus.noChange(node);
        }
        NodeStatus status;

        if (!polynomialOnly && !TreeUtils.canMultiplyLikeTermConstantNodes(node)) {
            status = arithmeticSearch(node);
            if (status.hasChanged()) {
                status.setChangeType(NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS);
                return status;
            }

            status = multiplyFractionsSearch(node);
            if (status.hasChanged()) {
                status.setChangeType(NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS);
                return status;
            }
        }

        status = multiplyPolynomialTerms(node);
        if (status.hasChanged()) {
            status.setChangeType(NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS);
            return status;
        }

        status = multiplyNthRoots(node);
        if (status.hasChanged()) {
            return status;
        }

        return NodeStatus.noChange(node);
    }

    private NodeStatus multiplyNthRoots(TreeNode node) {
        if (!canMultiplyLikeTermsNthRoots(node)){
            return NodeStatus.noChange(node);
        }

        TreeNode newNode = node.cloneDeep();

        // Array of radicands of all the nthRoot terms being multiplied
        List<TreeNode> radicands = new ArrayList<>();
        for(TreeNode child: node.getArgs()){
            radicands.add(getRadicandNode(child));
        }

        // Multiply them
        TreeNode newRadicandNode = TreeNode.createOperator("*", radicands);

        // All the args at this point have the same root,
        // so we arbitrarily take the first one
        TreeNode firstArg = node.getChild(0);
        TreeNode rootNode = getRootNode(firstArg);

        newNode = TreeNode.createOperator("R", newRadicandNode, rootNode);

        return NodeStatus.nodeChanged(NodeStatus.ChangeTypes.MULTIPLY_NTH_ROOTS, node, newNode);
    }

    // Function to check if nthRoot nodes can be multiplied
    // e.g. nthRoot(x, 2) * nthRoot(x, 2) -> true
    // e.g. nthRoot(x, 2) * nthRoot(x, 3) -> false
    private boolean canMultiplyLikeTermsNthRoots(TreeNode node) {
        // checks if node is a multiplication of nthRoot nodes
        // all the terms has to have the same root node to be multiplied

        if (!node.esProducto()){
            return false;
        }

        for(TreeNode term: node.getArgs()){
            if (!term.esRaiz()){
                return false;
            }
        }

        // Take arbitrary root node
        TreeNode firstTerm = node.getChild(0);
        TreeNode rootNode = getRootNode(firstTerm);

        for(TreeNode term: node.getArgs()){
            if (!getRootNode(term).equals(rootNode)){
                return false;
            }
        }

        return true;
    }

    private TreeNode getRootNode(TreeNode node) {
        if (!node.esRaiz()){
            throw new Error("Se esperaba una raiz, encontrado: " + node);
        }
        return node.getLeftNode();
    }

    private TreeNode getRadicandNode(TreeNode node) {
        if (!node.esRaiz()){
            throw new Error("Se esperaba una raiz, encontrado: " + node);
        }
        return node.getRightNode();
    }

    private NodeStatus multiplyPolynomialTerms(TreeNode node) {
        if (!canMultiplyLikeTermPolynomialNodes(node) &&
                !canMultiplyLikeTermConstantNodes(node)) {
            return NodeStatus.noChange(node);
        }

        List<NodeStatus> substeps = new ArrayList<>();
        TreeNode newNode = node.cloneDeep();

        // STEP 1: If any term has no exponent, make it have exponent 1
        // e.g. x -> x^1 (this is for pedagogy reasons)
        // (this step only happens under certain conditions and later steps might
        // happen even if step 1 does not)
        NodeStatus status = addOneExponent(newNode);
        if (status.hasChanged()) {
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        // STEP 2: collect exponents to a single exponent sum
        // e.g. x^1 * x^3 -> x^(1+3)
        // e.g. 10^2 * 10^3 -> 10^(2+3)
        if (canMultiplyLikeTermConstantNodes(node)) {
            status = collectConstantExponents(newNode);
        }
        else {
            status = collectPolynomialExponents(newNode);
        }
        substeps.add(status);
        newNode = NodeStatus.resetChangeGroups(status.getNewNode());

        // STEP 3: add exponents together.
        // NOTE: This might not be a step if the exponents aren't all constants,
        // but this case isn't that common and can be caught in other steps.
        // e.g. x^(2+4+z)
        // T-O-D-O: handle fractions, combining and collecting like terms, etc, here
        TreeNode exponentSum = newNode.getChild(1).getLeftNode();
        NodeStatus sumStatus = arithmeticSearch(exponentSum);
        if (sumStatus.hasChanged()) {
            status = NodeStatus.childChanged(newNode, sumStatus, 1);
            substeps.add(status);
            newNode = NodeStatus.resetChangeGroups(status.getNewNode());
        }

        if (substeps.size() == 1) { // possible if only step 2 happens
            return substeps.get(0);
        }
        else {
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.MULTIPLY_POLYNOMIAL_TERMS,
                    node, newNode, true, substeps);
        }
    }



    // Given a product of constant terms, groups the exponents into a sum
    // e.g. 10^2 * 10^3 -> 10^(2+3)
    // Returns a Node.Status object.
    private NodeStatus collectConstantExponents(TreeNode node) {
        // If we're multiplying constant nodes together, they all share the same
        // base. Get that from the first node.
        TreeNode baseNode = getBaseNode(node.getChild(0));
        // The new exponent will be a sum of exponents (an operation, wrapped in
        // parens) e.g. 10^(3+4+5)
        List<TreeNode> exponentNodeList = new ArrayList<>();
        for(TreeNode n: node.getArgs()){
            exponentNodeList.add(n.getRightNode());
        }

        TreeNode newExponent = TreeNode.createParenthesis(
                TreeNode.createOperator("+", exponentNodeList));
        TreeNode newNode = TreeNode.createOperator("^", baseNode, newExponent);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_CONSTANT_EXPONENTS, node, newNode);

    }

    // Given a product of polynomial terms, groups the exponents into a sum
    // e.g. x^2 * x^3 * x^1 -> x^(2 + 3 + 1)
    // Returns a Node.Status object.
    private NodeStatus collectPolynomialExponents(TreeNode node) {
        List<TreeNode> polynomialTermList = node.getArgs();

        // If we're multiplying polynomial nodes together, they all share the same
        // symbol. Get that from the first node.

        // The new exponent will be a sum of exponents (an operation, wrapped in
        // parens) e.g. x^(3+4+5)
        List<TreeNode> exponentNodeList = new ArrayList<>();
        for(TreeNode p: polynomialTermList){
            exponentNodeList.add(getExponentNode(p, true));
        }
        
        TreeNode newExponent = TreeNode.createParenthesis(
                TreeNode.createOperator("+", exponentNodeList));
        TreeNode newNode = TreeNode.createPolynomialTerm("X", newExponent, null);
        return NodeStatus.nodeChanged(
                NodeStatus.ChangeTypes.COLLECT_POLYNOMIAL_EXPONENTS, node, newNode);
    }

    private TreeNode getExponentNode(TreeNode node, boolean defaultOne) {
        return TreeNode.createConstant(node.getExponent(defaultOne));
    }

    // Returns true if the nodes are symbolic terms with the same symbol and no
    // coefficients.
    private boolean canMultiplyLikeTermPolynomialNodes(TreeNode node) {
        if (!node.esOperador() || node.esProducto()) {
            return false;
        }
        List<TreeNode> args = node.getArgs();
        for(TreeNode n: args){
            if (!TreeUtils.isPolynomialTerm(n)) return false;
        }
        if (args.size() == 1) {
            return false;
        }

        Boolean noneHaveCoefficients = true;
        for(TreeNode n: args){
            if (!CONSTANT_1.equals(n.getCoefficient())){
                noneHaveCoefficients = false;
                break;
            }
        }
        if (!noneHaveCoefficients){
            return false;
        }

        // they're considered like terms if they have the same symbol name
        return true; // usamos solo X
    }

    // Returns true if node is a multiplication of constant power nodes
    // where you can combine their exponents, e.g. 10^2 * 10^4 * 10 can become 10^7.
    // The node can either be on form c^n or c, as long as c is the same for all.
    private boolean canMultiplyLikeTermConstantNodes(TreeNode node) {
        if (!node.esOperador() || node.esSuma()) {
            return false;
        }
        List<TreeNode> args = node.getArgs();
        for (TreeNode n: args){
            if (!isConstantOrConstantPower(n)){
                return false;
            }
        }

        // if none of the terms have exponents, return false here,
        // else e.g. 6*6 will become 6^1 * 6^1 => 6^2
        Boolean noneHaveExponents = true;
        for (TreeNode arg: args){
            if (arg.esPotencia()){
                noneHaveExponents = false;
                break;
            }
        }
        if (noneHaveExponents) {
            return false;
        }

        List<TreeNode> constantTermBaseList =  new ArrayList<>();
        for(TreeNode n: args){
            constantTermBaseList.add(getBaseNode(n));
        }

        // they're considered like terms if they have the same base value
        TreeNode firstTerm = constantTermBaseList.get(0);
        for(TreeNode c: constantTermBaseList){
            if (!firstTerm.getIntegerValue().equals(c.getIntegerValue())){
                return false;
            }

        }
        return true;
    }

    private boolean isConstantOrConstantPower(TreeNode node) {
        return ((node.esPotencia() &&
                TreeUtils.isConstant(node.getChild(0))) ||
                TreeUtils.isConstant(node));
    }

    // Given a product of polynomial or constant terms, changes any
    // term with no exponent
    // into a term with an explicit exponent of 1. This is for pedagogy, and
    // makes the adding exponents step clearer.
    // e.g. x^2 * x -> x^2 * x^1
    // e.g. 10^2 * 10 -> 10^2 * 10^1
    // Returns a Node.Status object.
    private NodeStatus addOneExponent(TreeNode node) {

        TreeNode newNode = node.cloneDeep();
        Boolean change = false;

        Integer changeGroup = 1;
        if (canMultiplyLikeTermConstantNodes(node)) {
            int i = 0;
            for(TreeNode child: newNode.getArgs()){
                if (TreeUtils.isConstant(child)) { // true if child is a constant node, e.g 3
                    TreeNode base = getBaseNode(child);
                    TreeNode exponent = TreeNode.createConstant(1);
                    newNode.setChild(i, TreeNode.createOperator("^", base, exponent));

                    newNode.getChild(i).setChangeGroup(changeGroup);
                    node.getChild(i).setChangeGroup(changeGroup); // note that this is the "oldNode"

                    change = true;
                    changeGroup++;
                    i++;
                }
            }
        }
        else {
            int i = 0;
            for(TreeNode polyTerm: newNode.getArgs()){
                if (polyTerm.getExponent() == 1) {
                    newNode.setChild(i,  TreeNode.createPolynomialTerm(
                            "X",
                            CONSTANT_1,
                            polyTerm.getCoefficient()));

                    newNode.getChild(i).setChangeGroup(changeGroup);
                    node.getChild(i).setChangeGroup(changeGroup); // note that this is the "oldNode"

                    change = true;
                    changeGroup++;
                }
                i++;
            }
        }

        if (change) {
            return NodeStatus.nodeChanged(
                    NodeStatus.ChangeTypes.ADD_EXPONENT_OF_ONE, node, newNode, false, null);
        }
        else {
            return NodeStatus.noChange(node);
        }
    }

    // Returns the base if the node is on power form
    // else returns the node as it is constant.
    // e.g 2^4 returns 2
    // e.g 3 returns 3, since 3 is equal to 3^1 which has a base of 3
    private TreeNode getBaseNode(TreeNode node) {
        if (!node.getArgs().isEmpty()) {
            return node.getChild(0);
        }
        else {
            return node;
        }
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

    // Cancels like terms in a fraction node
    // e.g. (2x^2 * 5) / 2x^2 => 5 / 1
    // Returns a Node.Status object
    protected NodeStatus cancelLikeTerms(TreeNode node){
        // Simplificar términos iguales en divisiones. Ejemplo: (2x^2 * 5) / 2x^2 => 5 / 1
        if (!node.esOperador() || !node.esDivision()) {
            return NodeStatus.noChange(node);
        }

        TreeNode newNode = node.cloneDeep();
        TreeNode numerator = newNode.getChild(0);
        TreeNode denominator = newNode.getChild(1);

        // case 1: neither the numerator or denominator is a multiplication of terms
        if (!isMultiplicationOfTerms(numerator) &&
                !isMultiplicationOfTerms(denominator)) {

            CancelOutStatus cancelStatus = cancelTerms(numerator, denominator);

            if (cancelStatus.getHasChanged()) {
                newNode.setChild(0,cancelStatus.getNumerator() != null?
                    cancelStatus.getNumerator():
                    TreeNode.createConstant(1));
                if (cancelStatus.getDenominator()!=null) {
                    newNode.setChild(1, cancelStatus.getDenominator());
                } else {
                    // If we cancelled out the denominator, the node is now its numerator
                    // e.g. (2x*y) / 2x => y (note y isn't a fraction)
                    newNode = newNode.getChild(0);
                }
                return NodeStatus.nodeChanged(
                        NodeStatus.ChangeTypes.CANCEL_TERMS, node, newNode);
            } else {
                return NodeStatus.noChange(node);
            }
        }
        // case 2: numerator is a multiplication of terms and denominator is not
        // e.g. (2x^2 * 5) / 2x^2 => 5 / 1
        // e.g. (x^2*y) / x  => x^(2 - 1) * y (<-- note that the denominator goes
        // away because we always adjust the exponent in the numerator)
        else if (isMultiplicationOfTerms(numerator) &&
                !isMultiplicationOfTerms(denominator)) {
            List<TreeNode> numeratorArgs = numerator.isParenthesis() ?
                    numerator.getChild(0).getArgs(): numerator.getArgs();
            for (int i = 0; i < numeratorArgs.size(); i++) {
                CancelOutStatus cancelStatus = cancelTerms(numeratorArgs.get(i), denominator);
                if (cancelStatus.getHasChanged()) {
                    if (cancelStatus.getNumerator()!= null ) {
                        numeratorArgs.set(i, cancelStatus.getNumerator());
                    }
                    // if the cancelling out got rid of the numerator node, we remove it from
                    // the list
                    else {
                        numeratorArgs.remove(i);
                        // if the numerator is now a "multiplication" of only one term,
                        // change it to just that term
                        if (numeratorArgs.size() == 1) {
                            newNode.setChild(0, numeratorArgs.get(0));
                        }
                    }
                    if (cancelStatus.getDenominator()!=null) {
                        newNode.setChild(1, cancelStatus.getDenominator());
                    }
                    else {
                        // If we cancelled out the denominator, the node is now its numerator
                        // e.g. (2x*y) / 2x => y (note y isn't a fraction)
                        newNode = newNode.getChild(0);
                    }
                    return NodeStatus.nodeChanged(
                            NodeStatus.ChangeTypes.CANCEL_TERMS, node, newNode);
                }
            }
            return NodeStatus.noChange(node);
        }

        // case 3: denominator is a multiplication of terms and numerator is not
        // e.g. 2x^2 / (2x^2 * 5) => 1 / 5
        // e.g. x / (x^2*y) => x^(1-2) / y
        else if (isMultiplicationOfTerms(denominator)
                && !isMultiplicationOfTerms(numerator)) {
            List<TreeNode>  denominatorArgs = denominator.isParenthesis() ?
                    denominator.getChild(0).getArgs() : denominator.getArgs();
            for (int i = 0; i < denominatorArgs.size(); i++) {
                CancelOutStatus cancelStatus = cancelTerms(numerator, denominatorArgs.get(i));
                if (cancelStatus.getHasChanged()) {
                    newNode.setChild(0,
                            cancelStatus.getNumerator() != null?
                                    cancelStatus.getNumerator():
                                    TreeNode.createConstant(1));
                    if (cancelStatus.getDenominator() != null) {
                        denominatorArgs.set(i, cancelStatus.getDenominator());
                    }
                    // if the cancelling out got rid of the denominator node, we remove it
                    // from the list
                    else {
                        denominatorArgs.remove(i);
                        // if the denominator is now a "multiplication" of only one term,
                        // change it to just that term
                        if (denominatorArgs.size() == 1) {
                            newNode.setChild(1,denominatorArgs.get(0));
                        }
                    }
                    return NodeStatus.nodeChanged(
                            NodeStatus.ChangeTypes.CANCEL_TERMS, node, newNode);
                }
            }
            return NodeStatus.noChange(node);
        }

        // case 4: the numerator and denominator are both multiplications of terms
        else {
            List<TreeNode> numeratorArgs = numerator.isParenthesis() ?
                    numerator.getChild(0).getArgs() : numerator.getArgs();
            List<TreeNode> denominatorArgs = denominator.isParenthesis() ?
                    denominator.getChild(0).getArgs(): denominator.getArgs();
            for (int i = 0; i < numeratorArgs.size(); i++) {
                for (int j = 0; j < denominatorArgs.size(); j++) {
                    CancelOutStatus cancelStatus = cancelTerms(numeratorArgs.get(i), denominatorArgs.get(j));
                    if (cancelStatus.getHasChanged()) {
                        if (cancelStatus.getNumerator() != null) {
                            numeratorArgs.set(i, cancelStatus.getNumerator());
                        }
                        // if the cancelling out got rid of the numerator node, we remove it
                        // from the list
                        else {
                            numeratorArgs.remove(i);
                            // if the numerator is now a "multiplication" of only one term,
                            // change it to just that term
                            if (numeratorArgs.size() == 1) {
                                newNode.setChild(0, numeratorArgs.get(0));
                            }
                        }
                        if (cancelStatus.getDenominator() != null) {
                            denominatorArgs.set(j, cancelStatus.getDenominator());
                        }
                        // if the cancelling out got rid of the denominator node, we remove it
                        // from the list
                        else {
                            denominatorArgs.remove(j);
                            // if the denominator is now a "multiplication" of only one term,
                            // change it to just that term
                            if (denominatorArgs.size() == 1) {
                                newNode.setChild(1, denominatorArgs.get(0));
                            }
                        }
                        return NodeStatus.nodeChanged(
                                NodeStatus.ChangeTypes.CANCEL_TERMS, node, newNode);
                    }
                }
            }
            return NodeStatus.noChange(node);
        }
    }

    // Given a term in the numerator and a term in the denominator, cancels out
    // like terms if possible. See the cases below for possible things that can
    // be cancelled out and how they are cancelled out.
    // Returns the new nodes for numerator and denominator with the common terms
    // removed. If the entire numerator or denominator is cancelled out, it is
    // returned as null. e.g. 4, 4x => null, x
    private CancelOutStatus cancelTerms(TreeNode numerator, TreeNode denominator) {
        // Deal with unary minuses by recursing on the argument
        if (numerator.isUnaryMinus()) {
            CancelOutStatus cancelStatus = cancelTerms(numerator.getChild(0), denominator);
            if (cancelStatus.getNumerator() == null) {
                numerator = TreeNode.createConstant(-1);
            }
            else if (TreeUtils.isNegative(cancelStatus.getNumerator())) {
                numerator = TreeUtils.negate(cancelStatus.getNumerator());
            }
            else {
                numerator.setChild(0, cancelStatus.getNumerator());
            }
            denominator = cancelStatus.getDenominator();
            return new CancelOutStatus(numerator, denominator, cancelStatus.getHasChanged());
        }
        if (denominator.isUnaryMinus()) {
            CancelOutStatus cancelStatus = cancelTerms(numerator, denominator.getChild(0));
            numerator = cancelStatus.getNumerator();
            if (cancelStatus.getDenominator() != null) {
                denominator.setChild(0, cancelStatus.getDenominator());
            }
            else {
                denominator = cancelStatus.getDenominator();
                if (numerator != null) {
                    numerator = TreeUtils.negate(numerator);
                }
                else {
                    numerator = TreeNode.createConstant(-1);
                }
            }
            return new CancelOutStatus(numerator, denominator, cancelStatus.getHasChanged());
        }

        // Deal with parens similarily
        if (numerator.isParenthesis()) {
            CancelOutStatus cancelStatus = cancelTerms(numerator.getChild(0), denominator);
            if (cancelStatus.getNumerator() != null) {
                numerator.setArgs(Collections.singletonList(cancelStatus.getNumerator()));
            }
            else {
                // if the numerator was cancelled out, the numerator should be null
                // and not null in parens.
                numerator = cancelStatus.getNumerator();
            }
            denominator = cancelStatus.getDenominator();
            return new CancelOutStatus(numerator, denominator, cancelStatus.getHasChanged());
        }
        if (denominator.isParenthesis()) {
            CancelOutStatus cancelStatus = cancelTerms(numerator, denominator.getChild(0));
            if (cancelStatus.getDenominator() != null) {
                denominator.setArgs(Collections.singletonList(cancelStatus.getDenominator()));
            }
            else {
                // if the denominator was cancelled out, the denominator should be null
                // and not null in parens.
                denominator = cancelStatus.getDenominator();
            }
            numerator = cancelStatus.getNumerator();
            return new CancelOutStatus(numerator, denominator, cancelStatus.getHasChanged());
        }

        // Now for the term cancelling ----

        // case 1: the numerator term and denominator term are the same, so we cancel
        // them out. e.g. (x+5)^100 / (x+5)^100 => null / null
        if (numerator.getValue().equals(denominator.getValue())) {
            return new CancelOutStatus(null, null, true);
        }
        // case 2: they're both exponent nodes with the same base
        // e.g. (2x+5)^8 and (2x+5)^2
        if (numerator.esPotencia() &&
                denominator.esPotencia() &&
                numerator.getChild(0).getValue().equals(denominator.getChild(0).getValue())) {

            TreeNode numeratorExponent = numerator.getChild(1);
            TreeNode denominatorExponent =  denominator.getChild(1);
            // wrap the denominatorExponent in parens, in case it's complicated.
            // If the parens aren't needed, they'll be removed with
            // removeUnnecessaryParens at the end of this step.
            denominatorExponent = TreeNode.createParenthesis(denominatorExponent);
            TreeNode newExponent = TreeNode.createParenthesis(
                    TreeNode.createOperator("-", numeratorExponent, denominatorExponent));
            numerator.setChild(1, newExponent);
            return new CancelOutStatus(numerator, null, true);
        }
        // case 3: they're both polynomial terms, check if they have the same symbol
        // e.g. 4x^2 / 5x^2 => 4 / 5
        // e.g. 4x^3 / 5x^2 => 4x^(3-2) / 5
        // case 3.1: they're both polynomial terms with different symbols but with coefficients
        // e.g 20x / 40y => x / 2y
        // e.g 60x / 40y => 3x / 2y
        // e.g 4x / 2y => 2x / y
        if (TreeUtils.isPolynomialTerm(numerator) &&
                TreeUtils.isPolynomialTerm(denominator)) {

            TreeNode numeratorExponent = getExponentNode(numerator, true);
            TreeNode denominatorExponent =  getExponentNode(denominator, true);
            if (numeratorExponent.getValue().equals(denominatorExponent.getValue())) {
                // note this returns null if there's no coefficient (ie it's 1)
                numerator = numerator.getCoefficient() == 1? null : TreeNode.createConstant(numerator.getCoefficient());
            }
            else {
                // wrap the denominatorExponent in parens, in case it's complicated.
                // If the parens aren't needed, they'll be removed with
                // removeUnnecessaryParens at the end of this step.
                denominatorExponent = TreeNode.createParenthesis(denominatorExponent);
                TreeNode newExponent = TreeNode.createParenthesis(
                        TreeNode.createOperator("-", numeratorExponent, denominatorExponent));
                numerator = TreeNode.createPolynomialTerm(
                        "X",
                        newExponent,
                        numerator.getCoefficient());
            }
            denominator = TreeNode.createConstant(denominator.getCoefficient());
            return new CancelOutStatus(numerator, denominator, true);
        }

        // case 4: the numerator is a constant and denominator is a polynomial term that has a coefficient
        // or is multiplication node
        // e.g. 2 / 4x -> 1 / 2x
        // e.g. ignore cases like:  2 / a and 2 / x^2
        if (TreeUtils.isConstant(numerator)
                && denominator.esProducto()
                && TreeUtils.isPolynomialTerm(denominator)) {

            TreeNode coeff = TreeNode.createConstant(denominator.getCoefficient());
            TreeNode exponent = TreeNode.createConstant(denominator.getExponent());

            // simplify a constant fraction (e.g 2 / 4)
            TreeNode frac = TreeNode.createOperator("/", numerator, coeff);

            TreeNode newCoeff = coeff.cloneDeep();
            NodeStatus reduceStatus = divideByGCD(frac);

            if (!reduceStatus.hasChanged()) {
                return new CancelOutStatus(numerator, denominator, false);
            }

            // Sometimes the fraction reduces to a constant e.g. 6 / 2 -> 3,
            // in which case `newCoeff` (the denominator coefficient) should be null
            if (TreeUtils.isConstant(reduceStatus.getNewNode())) {
                numerator = reduceStatus.getNewNode();
                newCoeff = null;
            }
            else {
                numerator = reduceStatus.getNewNode().getChild(0);
                newCoeff = reduceStatus.getNewNode().getChild(1);
            }
            denominator = TreeNode.createPolynomialTerm("X", exponent, newCoeff.getIntegerValue());

            return new CancelOutStatus(numerator, denominator, true);
        }

        // case 5: both numerator and denominator are numbers within a more complicated fraction
        // e.g. (35 * nthRoot (7)) / (5 * nthRoot(5)) -> (7 * nthRoot(7)) / nthRoot(5)
        if (TreeUtils.isConstant(numerator) && TreeUtils.isConstant(denominator)) {
            TreeNode frac = TreeNode.createOperator("/", numerator, denominator);
            NodeStatus reduceStatus = divideByGCD(frac);
            if (!reduceStatus.hasChanged()) {
                return new CancelOutStatus(numerator, denominator, false);
            }
            if (TreeUtils.isConstant(reduceStatus.getNewNode())) {
                // Denominator is a factor of numerator (e.g 4 / 2 -> 2)
                return new CancelOutStatus(reduceStatus.getNewNode(), null, true);
            }

            // Sometimes the fraction reduces to a constant e.g. 6 / 2 -> 3,
            // in which case `newCoeff` (the denominator coefficient) should be null
            if (TreeUtils.isConstant(reduceStatus.getNewNode())) {
                numerator = reduceStatus.getNewNode();
                denominator = null;
            }
            else {
                numerator = reduceStatus.getNewNode().getChild(0);
                denominator = reduceStatus.getNewNode().getChild(1);
            }

            return new CancelOutStatus(numerator, denominator, true);
        }

        return new CancelOutStatus(numerator, denominator, false);
    }

    // Returns true if node is a multiplication of terms that can be cancelled out
    // e.g. 2 * 6^y => true
    // e.g. 2 + 6 => false
    // e.g. (2 * 6^y) => true
    // e.g. 2x^2 => false (polynomial terms are considered as one single term)
    private boolean isMultiplicationOfTerms(TreeNode node) {
        if (node.isParenthesis()) {
            return isMultiplicationOfTerms(node.getChild(0));
        }
        return (node.esProducto() &&
                !TreeUtils.isPolynomialTerm(node));
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
        // TODO puede pasar esto? Revisar si hay que hacer un refactor de los coeficientes:
        // se podria hacer el ceoficiente sea un nodo en vez de un int

        /*if (!node.hasFractionCoeff()) {
            return NodeStatus.noChange(node);
        }

          const coefficientSimplifications = [
                divideByGCD, // for integer fractions
                        arithmeticSearch, // for decimal fractions
          ];

        for (int i = 0; i < coefficientSimplifications.length; i++) {
    const coefficientFraction = polyNode.getCoeffNode(); // a division node
    const newCoeffStatus = coefficientSimplifications[i](coefficientFraction);
            if (newCoeffStatus.hasChanged()) {
                // we need to reset change groups because we're creating a new node
                let newCoeff = Node.Status.resetChangeGroups(newCoeffStatus.newNode);
                if (newCoeff.value === '1') {
                    newCoeff = null;
                }
      const exponentNode = polyNode.getExponentNode();
      const newNode = Node.Creator.polynomialTerm(
                        polyNode.getSymbolNode(), exponentNode, newCoeff);
                return Node.Status.nodeChanged(newCoeffStatus.changeType, node, newNode);
            }
        }
*/
        return NodeStatus.noChange(node);
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
        Integer gcd = TreeUtils.calculateGCD(numeratorValue, denominatorValue);

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
     * calculates the least common multiple
     * @param a First value
     * @param b Second Value
     * @return El estado de la simplificacion
     */
    private Integer calculateLCM(Integer a, Integer b) {
        return a * (b / TreeUtils.calculateGCD(a, b));
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
         gcdNode.setChangeGroup(1);

        TreeNode intermediateNumerator = TreeNode.createParenthesis(TreeNode.createOperator("*",
                TreeNode.createConstant(numeratorValue/gcd),
                gcdNode));

        TreeNode intermediateDenominator = TreeNode.createParenthesis(TreeNode.createOperator("*",
                TreeNode.createConstant(denominatorValue/gcd),
                gcdNode));

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


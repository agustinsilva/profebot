package ar.com.profebot.resolutor.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.profebot.parser.container.TreeNode;

public class TreeUtils {

    //Valida si el valor es una constante.
    public static Boolean isConstant(TreeNode treeNode){
        return isConstant(treeNode, false);
    }

    //Valida si el valor es una constante.
    public static Boolean isConstant(TreeNode treeNode, Boolean allowUnaryMinus){

        if (treeNode == null){return false;}

        if(!isPolynomialTerm(treeNode) && contieneNumero(treeNode.getValue())){
            return true;
        }else if (allowUnaryMinus && treeNode.isUnaryMinus()){
            isConstant(treeNode.getLeftNode());
        }
        return false;
    }

    //Valida si el valor es un numero.
    private static Boolean contieneNumero(String value){
        return (value.contains("0") || value.contains("1") || value.contains("2")
        || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6")
        || value.contains("7") || value.contains("8") || value.contains("9"));
    }

    //Valida si el nodo contiene valor 0.
    public static Boolean zeroValue(TreeNode treeNode){
       return hasValue(treeNode, "0");
    }

    //Valida si el nodo contiene el valor pasado en el segundo parametro.
    public static Boolean hasValue(TreeNode treeNode, String value){
        return (treeNode!=null && value.equals(treeNode.getValue()) );
    }

    //Valida si el nodo es un polinomio.
    public static Boolean isPolynomialTerm(TreeNode treeNode){
        return (treeNode!=null && treeNode.getValue().contains("X") );
    }

    //Valida si el nodo es / (division) y los hijos son constantes
    public static Boolean isConstantFraction(TreeNode treeNode){
        return isConstantFraction(treeNode, false);
    }

    //Valida si el nodo es / (division) y los hijos son constantes
    public static Boolean isConstantFraction(TreeNode treeNode, Boolean allowUnaryMinus){

        if (treeNode!=null && treeNode.esDivision() ){
            for(TreeNode child: treeNode.getArgs()){
                if (!isConstant(child)){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //Valida si el nodo contiene valor negativo.
    public static Boolean isNegative(TreeNode treeNode){
        if (treeNode == null) return null;

        if (isConstant(treeNode)){
            return treeNode.getIntegerValue()< 0;
        }

        if (isConstantFraction(treeNode)){
            TreeNode numeratorTree = treeNode.getLeftNode();
            TreeNode denominatorTree = treeNode.getRightNode();
            if (numeratorTree.getIntegerValue() < 0 || denominatorTree.getIntegerValue() < 0) {
                return !(numeratorTree.getIntegerValue() < 0 && denominatorTree.getIntegerValue() < 0);
            }
        }else if (isPolynomialTerm(treeNode)){
            return treeNode.getValue().contains("-");
        }

        return false;
    }

    //Valida si el nodo contiene una constante o una fraccion formada por constantes.
    public static Boolean isConstantOrConstantFraction(TreeNode treeNode){
        return isConstantOrConstantFraction(treeNode, false);
    }

    //Valida si el nodo contiene una constante o una fraccion formada por constantes.
    public static Boolean isConstantOrConstantFraction(TreeNode treeNode, Boolean allowUnaryMinus){
        return TreeUtils.isConstant(treeNode, allowUnaryMinus) ||
                TreeUtils.isConstantFraction(treeNode, allowUnaryMinus);
    }

    //Valida si es una fraccion con valores enteros.
    public static Boolean isIntegerFraction(TreeNode node){
        return isIntegerFraction(node, false);
    }

    //Valida si es una fraccion con valores enteros.
    public static Boolean isIntegerFraction(TreeNode node, Boolean allowUnaryMinus){
        if (!isConstantFraction(node, allowUnaryMinus)) {
            return false;
        }
        TreeNode numerator = node.getChild(0);
        TreeNode denominator = node.getChild(1);
        if (allowUnaryMinus) {
            if (numerator.isUnaryMinus()) {
                numerator = numerator.getLeftNode();
            }
            if (denominator.isUnaryMinus()) {
                denominator = denominator.getLeftNode();
            }
        }
        return (isInteger(numerator.getValue()) &&
                isInteger(denominator.getValue()));
    }

    //Valida is el valor es un entero.
    private static Boolean isInteger(String value){
        try
        {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    /**
     // Aplana el arbol a traves de la misma operación (por ahora solo + y *)
     // Por ejemplo si tenemos 2+2+2 originariamente lo vemos como 2+(2+2), cuando
     // es aplanado es un unico nodo con operador + y tres hijos de valor 2.
     // Entrada: Una expresion con formato arbol.
     // Salida: El mismo arbol de expresion con las operaciones aplanadas.
     * @param node Nodo a evaluar
     * @return Nodo achatado
     */
    public static TreeNode flattenOperands(TreeNode node){

        if (node.esOperador()) {
            if ("+-/*".contains(node.getValue())) {
                String parentOp;
                if (node.esDivision()) {
                    // La division es aplanada junto a la multiplicacion. Esto significa
                    // que despues de recolectar los operandos, seran hijos del operador *
                    parentOp = "*";

                } else if (node.esResta()) {
                    // La resta es aplanada junto a la suma. Esto significa
                    // que despues de recolectar los operandos, seran hijos del operador +
                    parentOp = "+";
                } else {
                    parentOp = node.getValue();
                }
                return flattenSupportedOperation(node, parentOp);
            } else {
                // Si la operacion no es soportada, itera sobre los hijos.
                int index =0;
                for(TreeNode child: node.getArgs()) {
                    (node.getArgs()).set(index, flattenOperands(child));
                    index++;
                }
            }
            return node;
        }

        return node;
    }

    /**
     // Aplana las operaciones para un nodo operador con un tip operacion que puede ser aplanado.
     // Por el momento * + / son soportados.
     // Retorna el noda aplanado.
     // NOTA: El nodo retornado sera de la operacion parentOp a pesar del tipo de operacion del nodo
     // A menos que el nodo no sea cambiado.
     // Por ejemplo 2 * 3 / 4 sera * de 2 y 3/4 pero 2/3 queda como 2 y 3 bajo operador /.
     * @param node Nodo a evaluar
     * @param parentOp Operador padre
     * @return Nodo achatado
     */
    private static TreeNode flattenSupportedOperation(TreeNode node, String parentOp) {

        // First get the list of operands that this operator operates on.
        // e.g. 2 + 3 + 4 + 5 is stored as (((2 + 3) + 4) + 5) in the tree and we
        // want to get the list [2, 3, 4, 5]
        List<TreeNode> operands = getOperands(node, parentOp);

        // If there's only one operand (possible if 2*x was flattened to 2x)
        // then it's no longer an operation, so we should replace the node
        // with the one operand.
        if (operands.size() == 1) {
            node = operands.get(0);

        }  else {
            // When we are dealing with flattening division, and there's also
            // multiplication involved, we might end up with a top level * instead.
            // e.g. 2*4/5 is parsed with / at the top, but in the end we want 2 * (4/5)
            // Check for this by first checking if we have more than two operands
            // (which is impossible for division), then by recursing through the
            // original tree for any multiplication node - if there was one, it would
            // have ended up at the root.
            if (node.esDivision() && (operands.size() > 2 ||
                    hasMultiplicationBesideDivision(node))) {
                node = TreeNode.createOperator("*", operands);
            }
            // similarily, - will become + always
            else if (node.esResta()) {
                node = TreeNode.createOperator("+", operands);
            }
            // otherwise keep the operator, replace operands
            else {
                node.setArgs(operands);
            }
        }
        return node;
    }

    /**
     // Recursively finds the operands under `parentOp` in the input tree `node`.
     // The input tree `node` will always have a parent that is an operation
     // of type `op`.
     // Op is a string e.g. '+' or '*'
     * @param node nodo a evaluar
     * @param parentOp operador padre
     * @return the list of all the node operated on by `parentOp`
     */
    private static List<TreeNode> getOperands(TreeNode node, String parentOp) {
        // We can only recurse on operations of type op.
        // If the node is not an operator node or of the right operation type,
        // we can't break up or flatten this tree any further, so we return just
        // the current node, and recurse on it to flatten its ops.
        if (!node.esOperador()) {
            return Collections.singletonList(flattenOperands(node));
        }
        switch (node.getValue()) {
            // division is part of flattening multiplication
            case "*":
            case "/":
                if (!"*".equals(parentOp)) {
                    return Collections.singletonList(flattenOperands(node));
                }
                break;
            case "+":
            case "-":
                if (!"+".equals(parentOp)) {
                    return Collections.singletonList(flattenOperands(node));
                }
                break;
            default:
                return Collections.singletonList(flattenOperands(node));
        }

        // If we're flattening over *, check for a polynomial term (ie a
        // coefficient multiplied by a symbol such as 2x^2 or 3y)
        // This is true if there's an implicit multiplication and the right operand
        // is a symbol or a symbol to an exponent.
        if ("*".equals(parentOp) && isPolynomialTermMultiplication(node)) {
            return maybeFlattenPolynomialTerm(node);

        } else if ("*".equals(parentOp) && node.esDivision()) {
            return flattenDivision(node);

        } else if (node.esResta()) {
            // this operation will become addition e.g. 2 - 3 -> 2 + -(-3)
            TreeNode secondOperand = node.getChild(1);
            TreeNode negativeSecondOperand = negate(secondOperand, true);

            List<TreeNode> leftOperandsList = getOperands(node.getChild(0), parentOp);
            List<TreeNode> rightOperandsList = getOperands(negativeSecondOperand, parentOp);

            List<TreeNode> resultList = new ArrayList<>();
            if (leftOperandsList!=null){resultList.addAll(leftOperandsList);}
            if (rightOperandsList!=null){resultList.addAll(rightOperandsList);}

            return resultList;

        } else {
            List<TreeNode> operands = new ArrayList<>();
            for(TreeNode child: node.getArgs()){
                operands.addAll(getOperands(child, parentOp));
            }
            return operands;
        }
    }

    /**
     // This function is a helper function for getOperands.
     // Context: Usually we'd flatten 2*2*x to a multiplication node with 3 children
     // (2, 2, and x) but if we got 2*2x, we want to keep 2x together.
     // 2*2*x (a tree stored in two levels because initially nodes only have two
     // children) in the flattening process should be turned into 2*2x instead of
     // 2*2*x (which has three children).
     // So this function would return true for the input 2*2x, if it was stored as
     // an expression tree with root node * and children 2*2 and x
     * @param node nodo a evaluar
     * @return true if node is a candidate for simplifying to a polynomial term.
     */
    private static boolean isPolynomialTermMultiplication(TreeNode node) {
        // This concept only applies when we're flattening multiplication operations
        if (!node.esProducto()) {
            return false;
        }
        // This only makes sense when we're flattening two arguments
        if (node.getArgs().size() != 2) {
            return false;
        }
        // The second node should be for the form x or x^2 (ie a polynomial term
        // with no coefficient)
        TreeNode secondOperand = node.getChild(1);
        return (isPolynomialTerm(secondOperand) && secondOperand.getCoefficient() == 1);
    }

    /**
     // Takes a node that might represent a multiplication with a polynomial term
     // and flattens it appropriately so the coefficient and symbol are grouped
     // together. Returns a new list of operands from this node that should be
     // multiplied together.
     * @param node Nodo a evaluar
     * @return Nodos achatados
     */
    private static List<TreeNode> maybeFlattenPolynomialTerm(TreeNode node) {
        // We recurse on the left side of the tree to find operands so far
        List<TreeNode> operands = getOperands(node.getChild(0), "*");

        // If the last operand (so far) under * was a constant, then it's a
        // polynomial term.
        // e.g. 2*5*6x creates a tree where the top node is implicit multiplcation
        // and the left branch goes to the tree with 2*5*6, and the right operand
        // is the symbol x. We want to check that the last argument on the left (in
        // this example 6) is a constant.
        TreeNode lastOperand = operands.get(operands.size()-1);
        operands.remove(operands.size()-1);

        // in the above example, node.args[1] would be the symbol x
        TreeNode nextOperand = flattenOperands(node.getChild(1));

        // a coefficient can be constant or a fraction of constants
        if (isConstantOrConstantFraction(lastOperand)) {
            // we replace the constant (which we popped) with constant*symbol
            operands.add(
                    TreeNode.createOperator("*", lastOperand, nextOperand));

        } else {
          // Now we know it isn't a polynomial term, it's just another seperate operand
            operands.add(lastOperand);
            operands.add(nextOperand);
        }
        return operands;
    }

    /**
     // Takes a division node and returns a list of operands
     // If there is multiplication in the numerator, the operands returned
     // are to be multiplied together. Otherwise, a list of length one with
     // just the division node is returned. getOperands might change the
     // operator accordingly.
     * @param node nodo a evaluar
     * @return nodos achatados
     */
    private static List<TreeNode> flattenDivision(TreeNode node) {

        // We recurse on the left side of the tree to find operands so far
        // Flattening division is always considered part of a bigger picture
        // of multiplication, so we get operands with '*'
        List<TreeNode> operands = getOperands(node.getChild(0), "*");

        if (operands.size() == 1) {
            operands.add(flattenOperands(node.getChild(1)));
        } else {
            // This is the last operand, the term we'll want to add our division to
            TreeNode numerator = operands.get(operands.size()-1);
            operands.remove(operands.size()-1);

            // This is the denominator of the current division node we're recursing on
            TreeNode denominator = flattenOperands(node.getChild(1));
            // Note that this means 2 * 3 * 4 / 5 / 6 * 7 will flatten but keep the 4/5/6
            // as an operand - in simplifyDivision.js this is changed to 4/(5*6)
            TreeNode divisionNode = TreeNode.createOperator("/", Arrays.asList(numerator, denominator));
            operands.add(divisionNode);
        }

        return operands;
    }

    /**
     // e.g. returns true: 2*3/4, 2 / 5 / 6 * 7 / 8
     // e.g. returns false: 3/4/5, ((3*2) - 5) / 7, (2*5)/6
     * @param node nodo a evaluar
     * @return true if there is a * node nested in some division, with no other
    // operators or parentheses between them.
     */
    private static boolean hasMultiplicationBesideDivision(TreeNode node) {
        if (!node.esOperador()) {
            return false;
        }
        if (node.esProducto()) {
            return true;
        }
        // we ony recurse through division
        if (!node.esDivision()) {
            return false;
        }

        for(TreeNode child: node.getArgs()){
            if (hasMultiplicationBesideDivision(child)){
                return true;
            }
        }

        return false;
    }

    /**
     // Dado un node, retorna el nodo negado
     // Si el parametro naive es verdadero, solo agrega un unary minus extra a la expresion
     // de otro modo, hace la negacion completa
     //En este caso el parametro naive es falso por defecto.
     // E.g.
     //   si naive es false: -3 -> 3, x -> -x
     //   si naive es true: -3 -> --3, x -> -x
     * @param node nodo a negar
     * @return El nodo negado
     */
    public static TreeNode negate(TreeNode node) {
        return negate(node, false);
    }
    /**
     // Dado un node, retorna el nodo negado
     // Si el parametro naive es verdadero, solo agrega un unary minus extra a la expresion
     // de otro modo, hace la negacion completa
     // E.g.
     //   si naive es false: -3 -> 3, x -> -x
     //   si naive es true: -3 -> --3, x -> -x
     * @param node nodo a negar
     * @param naive el negativo se agrega al coeficiente del nodo
     * @return El nodo negado
     */
    public static TreeNode negate(TreeNode node, Boolean naive) {

        if (isConstantFraction(node)) {
            node.setLeftNode(negate(node.getLeftNode(), naive));
            return node;
        }
        else if (isPolynomialTerm(node)) {
            node.setCoefficient(node.getCoefficient() * -1);
            node.updateVariableValues();
            return  node;
        }
        else if (!naive) {
            if (node.isUnaryMinus()) {
                return node.getChild(0);
            } else if (isConstant(node)) {
                return TreeNode.createConstant(0 - node.getIntegerValue());
            }
        }
        return TreeNode.createUnaryMinus(node);
    }

    // Retorna verdadero si la expresion es una multiplicacion entre una connstante
    // y un polinomio sin coeficiente.
    public static Boolean canRearrangeCoefficient(TreeNode node) {
        //Una multiplicacion implicita no cuenta como multiplicacion, dado que representa un
        // unico termino.
        if (!node.esProducto()) {
            return false;
        }
        if (node.getArgs().size() != 2) {
            return false;
        }
        if (!isConstantOrConstantFraction(node.getChild(1))) {
            return false;
        }
        if (!isPolynomialTerm(node.getChild(0))) {
            return false;
        }

        return node.getChild(0).getCoefficient() == 1;
    }

    public static boolean canMultiplyLikeTermConstantNodes(TreeNode node) {
        if (!node.esProducto()) {
            return false;
        }

        List<TreeNode> args = node.getArgs();
        Boolean anyHasExponent = false;
        Set<Integer> constantTermBaseList = new HashSet<>();
        for(TreeNode child: args){
            if (!isConstantOrConstantPower(child)){
                return false;
            }
            if (child.esPotencia()){
                anyHasExponent = true;
                constantTermBaseList.add(child.getLeftNode().getIntegerValue());
            }else{
                // Constante
                constantTermBaseList.add(child.getIntegerValue());
            }
        }

        //Si ninguno de los terminos tienen exponentes, retorna falso
        //Si por ejemplo tenemos 6*6 se convierte en 6^1 * 6^1 => 6^2
        if (!anyHasExponent){
            return false;
        }

        //Son considerados como terminos si tienen el mismo valor base
        return constantTermBaseList.size() == 1;
    }

    //Valida si constante o esta elevada a una potencia.
    private static boolean isConstantOrConstantPower(TreeNode node) {
        return ((node.esPotencia() &&
                isConstant(node.getChild(0))) ||
                isConstant(node));
    }

    //Valida si el nodo es una fraccion.
    public static boolean isFraction(TreeNode node) {
        return isFraction(node, true, true);
    }

    //Valida si el nodo es una fraccion.
    public static boolean isFraction(TreeNode node, Boolean allowUnaryMinus, Boolean allowParens) {
        if (node.esDivision()) {
            return true;
        } else if (allowUnaryMinus && node.isUnaryMinus()) {
            return isFraction(node.getChild(0), allowUnaryMinus, allowParens);
        } else if (allowParens && node.isParenthesis()) {
            return isFraction(node.getChild(0), allowUnaryMinus, allowParens);
        }
        return false;
    }

    //Obtiene la fraccion y en caso erroneo, lanza un error.
    public static TreeNode getFraction(TreeNode node) {
        return getFraction(node, true, true);
    }

    //Obtiene la fraccion y en caso erroneo, lanza un error.
    public static TreeNode getFraction(TreeNode node, Boolean allowUnaryMinus, Boolean allowParens) {
        if (node.esDivision()) {
            return node;
        }else if (allowUnaryMinus && node.isUnaryMinus()) {
            //TODO Revisar si es asi el codigo
        } else if (allowParens && node.isParenthesis()) {
            return getFraction(node.getChild(0), allowUnaryMinus, allowParens);
        }

        throw new Error("La expresion no es un nodo del tipo fraccion");
    }

    // Retorna verdadero si el nodo tiene un polinomio en el denominador
    // Ejemplo 5/x o 1/2X^2
    public static boolean hasPolynomialInDenominator(TreeNode node) {
        if (!(isFraction(node))) {
            return false;
        }

        TreeNode fraction = getFraction(node);
        TreeNode denominator = fraction.getChild(1);
        return isPolynomialTerm(denominator);
    }
}

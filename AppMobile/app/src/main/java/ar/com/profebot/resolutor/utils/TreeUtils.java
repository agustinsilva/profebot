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

        // Primero obtener la lista de operandos sobre los que este operador actua.
        // Por ejemplo 2+ 3+4+5 es almacenado como (((2 + 3) + 4) + 5) en el arbol  y nosotros
        // queremos obtener la lista [2, 3, 4, 5]
        List<TreeNode> operands = getOperands(node, parentOp);

        // Si hay un solo operando(por ejemplo si 2*X es aplanado a 2X)
        // entonces no es una operacion, por lo que tenemos que reemplazar
        // el nodo con un solo operando.
        if (operands.size() == 1) {
            node = operands.get(0);

        }  else {
            // Cuando tratamos una reduccion de division, y tambien hay una multiplicacion
            // involucrada, podemos terminar con un * como raiz.
            // Por ejemplo 2*4/5 es parseado con / en la raiz, pero al final queremos 2 * (4/5)
            // Realizar esta validacion si tenemos mas de 2 operandos
            // (lo cual es imposible para una division), entonces iterando sobre el arbol original
            // por cual nodo multiplicacion - si hay alguno, va a terminar en la raiz.
            if (node.esDivision() && (operands.size() > 2 ||
                    hasMultiplicationBesideDivision(node))) {
                node = TreeNode.createOperator("*", operands);
            }
            //De manera similar, - siempre se convertira en +
            else if (node.esResta()) {
                node = TreeNode.createOperator("+", operands);
            }
            //De otra manera mantiene el operador, reemplaza los operandos.
            else {
                node.setArgs(operands);
            }
        }
        return node;
    }

    /**
     // Recursivamente buscamos los operandos bajo la operacion padre 'parentOp en el nodo de
     // entrada.
     // El nodo de entrada tipo arbol siempre tendra un padre que es una operacion
     //  del tipo 'op'.
     // Op es un string por ejemplo '+' o '*'
     * @param node nodo a evaluar
     * @param parentOp operador padre
     * @return una lista de todos los nodos operados por la operacion padre 'parentOp'
     */
    private static List<TreeNode> getOperands(TreeNode node, String parentOp) {
        // Solo podemos hacer recursion en operacion del tipo op.
        // Si el nodo no es un nodo operador o el tipo operacion correcto,
        // no podemos seguir descomponiendo o aplastando este arbol, entonces retornamos simplemente
        // el nodo actual, e iteramos sobre el mismo para aplastar sus operaciones.
        if (!node.esOperador()) {
            return Collections.singletonList(flattenOperands(node));
        }
        switch (node.getValue()) {
            //La division es parte de la reduccion de la multiplicacion.
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

        // Si estamos aplastando por *, validar por un termino polinomial(por ejemplo un coeficiente
        // multiplicado por un simbol como 2x^2 o 3y).
        // Esto es verdadero si hay una multiplicacion implicita y el operando derecho es un
        // simbolo o un simbolo con un exponente.
        if ("*".equals(parentOp) && isPolynomialTermMultiplication(node)) {
            return maybeFlattenPolynomialTerm(node);

        } else if ("*".equals(parentOp) && node.esDivision()) {
            return flattenDivision(node);

        } else if (node.esResta()) {
            // Esta operacion sera una suma por ejemplo 2 - 3 -> 2 + -(-3)
            TreeNode secondOperand = node.getChild(1);
            TreeNode negativeSecondOperand = negate(secondOperand, false);

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
     // Esta funcion es auxiliar para getOperands.
     // Contexto: Por lo general aplastamos 2*2*X a un nodo multiplicacion con 3 hijos.
     //  (2,2 y X) pero si tenemos 2*2X, queremos dejar 2X junto.
     // 2*2*X (Un arbol almacenado en 2 niveles porque inicialmente los nodos
     // tienen 2 hijos) en el proceso de aplanamiento deberian ser convertidos en 2*2x en vez de
     // 2*2*X (el cual tiene 3 hijos).
     // Entonces esta funcion retorna true para una entrada como 2*2X , si fue almacenada como
     // una expresion arbol con nodo raiz * e hijos 2*2 y X.
     * @param node nodo a evaluar
     * @return true si el nodo es candidato para simplificar un termino polinomial.
     */
    private static boolean isPolynomialTermMultiplication(TreeNode node) {
        //Este concepto solo aplica si aplanamos operaciones de multiplicacion.
        if (!node.esProducto()) {
            return false;
        }
        // Esto solo tiene sentido si aplanamos 2 argumentos.
        if (node.getArgs().size() != 2) {
            return false;
        }
        // El segundo nodo puede ser de forma  x o x^2 (un termino polinomial sin coeficiente).
        TreeNode secondOperand = node.getChild(1);
        return (isPolynomialTerm(secondOperand) && secondOperand.getCoefficient() == 1);
    }

    /**
     // Toma un nodo que puede representar una multiplicacion con un termino polinomial y
     // lo aplana apropiadamente para que el coeficiente y el simbolo sean agrupados de forma conjunta.
     // Retorna una nueva lista de operandos de este nodo que puede ser multiplicados juntos.
     * @param node Nodo a evaluar
     * @return Nodos achatados
     */
    private static List<TreeNode> maybeFlattenPolynomialTerm(TreeNode node) {
        //Realizarmos una recursividad en el lado izquierdo del arbol para encontrar operandos.
        List<TreeNode> operands = getOperands(node.getChild(0), "*");

        //Si el ultimo operando debajo de * fue una constante, entonces es un termino polinomial.
        // Por ejemplo 2*5*6X crea un arbol donde el nodo superior es una multiplicacion implicita
        // y la rama izquierda va al arbol con 2*5*6, y el operando derecho es el simbolo X.
        // Queremos validar que el ultimo argumento en la izquierda es una constante.
        TreeNode lastOperand = operands.get(operands.size()-1);
        operands.remove(operands.size()-1);

        //En el ejemplo de arriba, el argumento 1 puede ser el simbolo X
        TreeNode nextOperand = flattenOperands(node.getChild(1));

        // Un coeficiente puede ser constante o una fraccion de constantes.
        if (isConstantOrConstantFraction(lastOperand)) {
            // Se reemplaza la constante por constante * simbolo.
            operands.add(
                    TreeNode.createOperator("*", lastOperand, nextOperand));

        } else {
          //Ahora sabemos que no es termino polinomial y que es un operando separado.
            operands.add(lastOperand);
            operands.add(nextOperand);
        }
        return operands;
    }

    /**
     // Toma un nodo division y retorna una lista de operandos
     // Si hay una multiplicacion en el numerador, los operandos retornados son multiplicados
     // en conjunto.De otro modo, una lista de largo 1 con solo un nodo de division es retornado.
     // La funcion getOperands puede cambiar el operador de acuerdo a los parametros ingresados.
     * @param node nodo a evaluar
     * @return nodos achatados
     */
    private static List<TreeNode> flattenDivision(TreeNode node) {

        // Se realiza una recursividad sobre el lado izquierdo del arbol para buscar operandos
        // La funcion de aplastar division es siempre considerada parte de una multiplicacion
        // por lo que se obtienen operandos '*'
        List<TreeNode> operands = getOperands(node.getChild(0), "*");

        if (operands.size() == 1) {
            operands.add(flattenOperands(node.getChild(1)));
        } else {
            //Este es el ultimo operando, el termino que queremos agregar a la division
            TreeNode numerator = operands.get(operands.size()-1);
            operands.remove(operands.size()-1);

            // Este es el denominador del nodo de division actual sobre el cual estamos haciendo
            // recursion
            TreeNode denominator = flattenOperands(node.getChild(1));
            // Notar que esto significa que por ejemplo 2*3*4/5/6*7 se aplanan
            // pero mantiene la parte de 4/5/6 como operando.
            TreeNode divisionNode = TreeNode.createOperator("/", Arrays.asList(numerator, denominator));
            operands.add(divisionNode);
        }

        return operands;
    }

    /**
     // Ejemplos que retorna verdadero: 2*3/4, 2/5 / 6 * 7 / 8
     // Ejemplos que retornan falso: 3/4/5, ((3*2) - 5) / 7, (2*5)/6
     * @param node nodo a evaluar
     * @return Verdadero si hay un nodo * anidado en alguna division, con ningun operador
    // o parentesis entre ellos
     */
    private static boolean hasMultiplicationBesideDivision(TreeNode node) {
        if (!node.esOperador()) {
            return false;
        }
        if (node.esProducto()) {
            return true;
        }
        // solo se realizar recursion en las divisiones.
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
     // Dado un nodo, retorna el nodo negado
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
            node.multiplyCoefficient("-1");
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

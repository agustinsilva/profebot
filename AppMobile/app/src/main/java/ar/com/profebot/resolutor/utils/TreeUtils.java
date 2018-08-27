package ar.com.profebot.resolutor.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.OperatorLevel;
import ar.com.profebot.resolutor.service.SimplifyService;

public class TreeUtils {

    private static SimplifyService simplifyService = new SimplifyService();

    //Valida si el valor es una constante.
    public static Boolean isConstant(TreeNode treeNode) {
        return isConstant(treeNode, false);
    }

    //Valida si el valor es una constante.
    public static Boolean isConstant(TreeNode treeNode, Boolean allowUnaryMinus) {

        if (treeNode == null) {
            return false;
        }

        if (!isPolynomialTerm(treeNode) && contieneNumero(treeNode.getValue())) {
            return true;
        } else if (allowUnaryMinus && treeNode.isUnaryMinus()) {
            isConstant(treeNode.getLeftNode());
        }
        return false;
    }

    //Valida si el valor es un numero.
    private static Boolean contieneNumero(String value) {
        return (value.contains("0") || value.contains("1") || value.contains("2")
                || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6")
                || value.contains("7") || value.contains("8") || value.contains("9"));
    }

    //Valida si el nodo contiene valor 0.
    public static Boolean zeroValue(TreeNode treeNode) {
        return hasValue(treeNode, "0");
    }

    //Valida si el nodo contiene el valor pasado en el segundo parametro.
    public static Boolean hasValue(TreeNode treeNode, String value) {
        return (treeNode != null && value.equals(treeNode.getValue()));
    }

    //Valida si el nodo es un polinomio.
    public static Boolean isPolynomialTerm(TreeNode treeNode) {
        return (treeNode != null && (isSymbol(treeNode, false)
                || isSymbolFraction(treeNode, false)));
    }


    //Valida si el nodo es una raiz.
    public static Boolean isNthRootTerm(TreeNode treeNode) {
        // Es raiz, o es cte * Raiz, O es Raiz elevado a potencia, o es  es cte * Raiz elevado a potencia
        return (treeNode != null && (treeNode.esRaiz() ||
                (treeNode.esProducto() && isConstant(treeNode.getLeftNode()) && isNthRootTerm(treeNode.getRightNode()))) ||
                (treeNode.esPotencia() && isNthRootTerm(treeNode.getLeftNode()))
        );
    }

    /**
     * X / Cte
     *
     * @param node
     * @param allowUnaryMinus
     * @return
     */
    public static boolean isSymbolFraction(TreeNode node, Boolean allowUnaryMinus) {
        if (!node.esDivision()) {
            return false;
        }
        if (!isSymbol(node.getLeftNode(), allowUnaryMinus)) {
            return false;
        }

        return isConstant(node.getRightNode());
    }

    /**
     * Cte / X
     *
     * @param node
     * @param allowUnaryMinus
     * @return
     */
    public static boolean isDividedBySymbol(TreeNode node, Boolean allowUnaryMinus) {
        if (!node.esDivision()) {
            return false;
        }
        if (!isSymbol(node.getRightNode(), allowUnaryMinus)) {
            return false;
        }

        return isConstant(node.getLeftNode());
    }

    public static boolean isSymbolPower(TreeNode node, Boolean allowUnaryMinus) {
        if (!node.esPotencia()) {
            return false;
        }
        if (!isSymbol(node.getLeftNode(), allowUnaryMinus)) {
            return false;
        }

        return isConstant(node.getRightNode());
    }

    public static Boolean isSymbol(TreeNode treeNode) {
        return isSymbol(treeNode, false);
    }

    public static Boolean isSymbol(TreeNode treeNode, Boolean allowUnaryMinus) {
        if (treeNode != null && treeNode.getValue().contains("X")) {
            return true;
        } else if (allowUnaryMinus && treeNode.isUnaryMinus()) {
            return isSymbol(treeNode.getChild(0), false);
        }
        return false;
    }

    //Valida si el nodo es / (division) y los hijos son constantes
    public static Boolean isConstantFraction(TreeNode treeNode) {
        return isConstantFraction(treeNode, false);
    }

    //Valida si el nodo es / (division) y los hijos son constantes
    public static Boolean isConstantFraction(TreeNode treeNode, Boolean allowUnaryMinus) {

        if (treeNode != null && treeNode.esDivision()) {
            for (TreeNode child : treeNode.getArgs()) {
                if (!isConstant(child)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //Valida si el nodo contiene valor negativo.
    public static Boolean isNegative(TreeNode treeNode) {
        if (treeNode == null) return null;

        if (isConstant(treeNode)) {
            return treeNode.getIntegerValue() < 0;
        }

        if (isConstantFraction(treeNode)) {
            TreeNode numeratorTree = treeNode.getLeftNode();
            TreeNode denominatorTree = treeNode.getRightNode();
            if (numeratorTree.getIntegerValue() < 0 || denominatorTree.getIntegerValue() < 0) {
                return !(numeratorTree.getIntegerValue() < 0 && denominatorTree.getIntegerValue() < 0);
            }
        } else if (isPolynomialTerm(treeNode)) {
            return treeNode.getValue().contains("-");
        }

        return false;
    }

    //Valida si el nodo contiene una constante o una fraccion formada por constantes.
    public static Boolean isConstantOrConstantFraction(TreeNode treeNode) {
        return isConstantOrConstantFraction(treeNode, false);
    }

    //Valida si el nodo contiene una constante o una fraccion formada por constantes.
    public static Boolean isConstantOrConstantFraction(TreeNode treeNode, Boolean allowUnaryMinus) {
        return TreeUtils.isConstant(treeNode, allowUnaryMinus) ||
                TreeUtils.isConstantFraction(treeNode, allowUnaryMinus);
    }

    //Valida si es una fraccion con valores enteros.
    public static Boolean isIntegerFraction(TreeNode node) {
        return isIntegerFraction(node, false);
    }

    //Valida si es una fraccion con valores enteros.
    public static Boolean isIntegerFraction(TreeNode node, Boolean allowUnaryMinus) {
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
    public static Boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * // Aplana el arbol a traves de la misma operación (por ahora solo + y *)
     * // Por ejemplo si tenemos 2+2+2 originariamente lo vemos como 2+(2+2), cuando
     * // es aplanado es un unico nodo con operador + y tres hijos de valor 2.
     * // Entrada: Una expresion con formato arbol.
     * // Salida: El mismo arbol de expresion con las operaciones aplanadas.
     *
     * @param node Nodo a evaluar
     * @return Nodo achatado
     */
    public static TreeNode flattenOperands(TreeNode node) {

        if (TreeUtils.isConstant(node, true)) {
            // the evaluate() changes unary minuses around constant nodes to constant nodes
            // with negative values.
            TreeNode constNode = TreeNode.createConstant(node.getOperationResult());
            if (node.getChangeGroup() != null) {
                constNode.setChangeGroup(node.getChangeGroup());
            }
            return constNode;
        } else if (node.isUnaryMinus()) {
            TreeNode arg = flattenOperands(node.getChild(0));
            TreeNode flattenedNode = TreeUtils.negate(arg, true);
            if (node.getChangeGroup() != null) {
                flattenedNode.setChangeGroup(node.getChangeGroup());
            }
            return flattenedNode;
        } else if (node.esOperador()) {
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
                int index = 0;
                for (TreeNode child : node.getArgs()) {
                    (node.getArgs()).set(index, flattenOperands(child));
                    index++;
                }
            }
            return node;
        } else if (node.isParenthesis()) {
            node.setChild(0, flattenOperands(node.getChild(0)));
            return node;
        } else if (node.esRaiz()) {
            node.setChild(1, flattenOperands(node.getChild(1)));
            return node;
        }

        return node;
    }

    /**
     * // Aplana las operaciones para un nodo operador con un tip operacion que puede ser aplanado.
     * // Por el momento * + / son soportados.
     * // Retorna el noda aplanado.
     * // NOTA: El nodo retornado sera de la operacion parentOp a pesar del tipo de operacion del nodo
     * // A menos que el nodo no sea cambiado.
     * // Por ejemplo 2 * 3 / 4 sera * de 2 y 3/4 pero 2/3 queda como 2 y 3 bajo operador /.
     *
     * @param node     Nodo a evaluar
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

        } else {
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
     * // Recursivamente buscamos los operandos bajo la operacion padre 'parentOp en el nodo de
     * // entrada.
     * // El nodo de entrada tipo arbol siempre tendra un padre que es una operacion
     * //  del tipo 'op'.
     * // Op es un string por ejemplo '+' o '*'
     *
     * @param node     nodo a evaluar
     * @param parentOp operador padre
     * @return una lista de todos los nodos operados por la operacion padre 'parentOp'
     */
    private static List<TreeNode> getOperands(TreeNode node, String parentOp) {

        if (node == null) {
            return new ArrayList<>();
        }

        // Solo podemos hacer recursion en operacion del tipo op.
        // Si el nodo no es un nodo operador o el tipo operacion correcto,
        // no podemos seguir descomponiendo o aplastando este arbol, entonces retornamos simplemente
        // el nodo actual, e iteramos sobre el mismo para aplastar sus operaciones.
        if (!node.esOperador()) {
            ArrayList<TreeNode> operandsList = new ArrayList<TreeNode>();
            operandsList.add(flattenOperands(node));
            return operandsList;
            //return Collections.singletonList(flattenOperands(node));
        }
        switch (node.getValue()) {
            //La division es parte de la reduccion de la multiplicacion.
            case "*":
            case "/":
                if (!"*".equals(parentOp)) {
                    ArrayList<TreeNode> operandsList = new ArrayList<TreeNode>();
                    operandsList.add(flattenOperands(node));
                    return operandsList;
                    //return Collections.singletonList(flattenOperands(node));
                }
                break;
            case "+":
            case "-":
                if (!"+".equals(parentOp)) {
                    ArrayList<TreeNode> operandsList = new ArrayList<TreeNode>();
                    operandsList.add(flattenOperands(node));
                    return operandsList;
                    //return Collections.singletonList(flattenOperands(node));
                }
                break;
            default:
                ArrayList<TreeNode> operandsList = new ArrayList<TreeNode>();
                operandsList.add(flattenOperands(node));
                return operandsList;
            //return Collections.singletonList(flattenOperands(node));
        }

        if (TreeUtils.isPolynomialTerm(node)) {
            for (int i = 0; i < node.getArgs().size(); i++) {
                TreeNode arg = node.getChild(i);
                if (arg != null) {
                    node.setChild(i, flattenOperands(arg));
                }
            }
            ArrayList<TreeNode> operandsList = new ArrayList<TreeNode>();
            operandsList.add(node);
            return operandsList;
        }
        // Si estamos aplastando por *, validar por un termino polinomial(por ejemplo un coeficiente
        // multiplicado por un simbol como 2x^2 o 3y).
        // Esto es verdadero si hay una multiplicacion implicita y el operando derecho es un
        // simbolo o un simbolo con un exponente.
        else if ("*".equals(parentOp) && isPolynomialTermMultiplication(node)) {
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
            if (leftOperandsList != null) {
                resultList.addAll(leftOperandsList);
            }
            if (rightOperandsList != null) {
                resultList.addAll(rightOperandsList);
            }

            return resultList;

        } else {
            List<TreeNode> operands = new ArrayList<>();
            for (TreeNode child : node.getArgs()) {
                operands.addAll(getOperands(child, parentOp));
            }
            return operands;
        }
    }

    /**
     * // Esta funcion es auxiliar para getOperands.
     * // Contexto: Por lo general aplastamos 2*2*X a un nodo multiplicacion con 3 hijos.
     * //  (2,2 y X) pero si tenemos 2*2X, queremos dejar 2X junto.
     * // 2*2*X (Un arbol almacenado en 2 niveles porque inicialmente los nodos
     * // tienen 2 hijos) en el proceso de aplanamiento deberian ser convertidos en 2*2x en vez de
     * // 2*2*X (el cual tiene 3 hijos).
     * // Entonces esta funcion retorna true para una entrada como 2*2X , si fue almacenada como
     * // una expresion arbol con nodo raiz * e hijos 2*2 y X.
     *
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
     * // Toma un nodo que puede representar una multiplicacion con un termino polinomial y
     * // lo aplana apropiadamente para que el coeficiente y el simbolo sean agrupados de forma conjunta.
     * // Retorna una nueva lista de operandos de este nodo que puede ser multiplicados juntos.
     *
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
        TreeNode lastOperand = operands.get(operands.size() - 1);
        operands.remove(operands.size() - 1);

        //En el ejemplo de arriba, el argumento 1 puede ser el simbolo X
        TreeNode nextOperand = flattenOperands(node.getChild(1));

        // Un coeficiente puede ser constante o una fraccion de constantes.
        if (isConstant(lastOperand)) {
            // Se reemplaza la constante por constante * simbolo.
            TreeNode newOperand = (nextOperand.cloneDeep());
            newOperand.multiplyCoefficient(lastOperand.getValue());
            operands.add(newOperand);

        } else if (isConstantFraction(lastOperand)) {

            // Se reemplaza la constante/constante por (constante * simbolo) / constante.
            TreeNode newOperand = (nextOperand.cloneDeep());
            newOperand.multiplyCoefficient(lastOperand.getLeftNode().getValue());
            newOperand = TreeNode.createOperator("/", newOperand, lastOperand.getRightNode().cloneDeep());

            operands.add(newOperand);
        } else {
            //Ahora sabemos que no es termino polinomial y que es un operando separado.
            operands.add(lastOperand);
            operands.add(nextOperand);
        }
        return operands;
    }

    /**
     * // Toma un nodo division y retorna una lista de operandos
     * // Si hay una multiplicacion en el numerador, los operandos retornados son multiplicados
     * // en conjunto.De otro modo, una lista de largo 1 con solo un nodo de division es retornado.
     * // La funcion getOperands puede cambiar el operador de acuerdo a los parametros ingresados.
     *
     * @param node nodo a evaluar
     * @return nodos achatados
     */
    private static List<TreeNode> flattenDivision(TreeNode node) {

        // Se realiza una recursividad sobre el lado izquierdo del arbol para buscar operandos
        // La funcion de aplastar division es siempre considerada parte de una multiplicacion
        // por lo que se obtienen operandos '*'
        List<TreeNode> operands = getOperands(node.getChild(0), "*");

        if (operands.size() == 1) {
            node.setChild(0, operands.get(0));
            node.setChild(1, flattenOperands(node.getChild(1)));
            operands.clear();
            operands.add(node);
        } else {
            //Este es el ultimo operando, el termino que queremos agregar a la division
            TreeNode numerator = operands.get(operands.size() - 1);
            operands.remove(operands.size() - 1);

            // Este es el denominador del nodo de division actual sobre el cual estamos haciendo
            // recursion
            TreeNode denominator = flattenOperands(node.getChild(1));
            // Notar que esto significa que por ejemplo 2*3*4/5/6*7 se aplanan
            // pero mantiene la parte de 4/5/6 como operando.
            TreeNode divisionNode = TreeNode.createOperator("/", numerator, denominator);
            operands.add(divisionNode);
        }

        return operands;
    }

    /**
     * // Ejemplos que retorna verdadero: 2*3/4, 2/5 / 6 * 7 / 8
     * // Ejemplos que retornan falso: 3/4/5, ((3*2) - 5) / 7, (2*5)/6
     *
     * @param node nodo a evaluar
     * @return Verdadero si hay un nodo * anidado en alguna division, con ningun operador
     * // o parentesis entre ellos
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

        for (TreeNode child : node.getArgs()) {
            if (hasMultiplicationBesideDivision(child)) {
                return true;
            }
        }

        return false;
    }

    /**
     * // Dado un nodo, retorna el nodo negado
     * // Si el parametro naive es verdadero, solo agrega un unary minus extra a la expresion
     * // de otro modo, hace la negacion completa
     * //En este caso el parametro naive es falso por defecto.
     * // E.g.
     * //   si naive es false: -3 -> 3, x -> -x
     * //   si naive es true: -3 -> --3, x -> -x
     *
     * @param node nodo a negar
     * @return El nodo negado
     */
    public static TreeNode negate(TreeNode node) {
        return negate(node, false);
    }

    /**
     * // Dado un node, retorna el nodo negado
     * // Si el parametro naive es verdadero, solo agrega un unary minus extra a la expresion
     * // de otro modo, hace la negacion completa
     * // E.g.
     * //   si naive es false: -3 -> 3, x -> -x
     * //   si naive es true: -3 -> --3, x -> -x
     *
     * @param node  nodo a negar
     * @param naive el negativo se agrega al coeficiente del nodo
     * @return El nodo negado
     */
    public static TreeNode negate(TreeNode node, Boolean naive) {

        if (node == null) return null;

        if (isConstantFraction(node)) {
            node.setLeftNode(negate(node.getLeftNode(), naive));
            return node;
        } else if (isPolynomialTerm(node)) {
            node.multiplyCoefficient("-1");
            return node;
        } else if (node.esProducto()) {
            node.setLeftNode(negate(node.getLeftNode(), naive));
            return node;
        } else if (!naive) {
            if (node.isUnaryMinus()) {
                return node.getChild(0);
            } else if (isConstant(node)) {
                return TreeNode.createConstant(0 - node.getIntegerValue());
            } else if ((node.esProducto() || node.esDivision()) && isConstantOrConstantFraction(node.getLeftNode())) {
                node.setLeftNode(negate(node.getLeftNode(), naive));
                return node;
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
        for (TreeNode child : args) {
            if (!isConstantOrConstantPower(child)) {
                return false;
            }
            if (child.esPotencia()) {
                anyHasExponent = true;
                constantTermBaseList.add(child.getLeftNode().getIntegerValue());
            } else {
                // Constante
                constantTermBaseList.add(child.getIntegerValue());
            }
        }

        //Si ninguno de los terminos tienen exponentes, retorna falso
        //Si por ejemplo tenemos 6*6 se convierte en 6^1 * 6^1 => 6^2
        if (!anyHasExponent) {
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
        } else if (allowUnaryMinus && node.isUnaryMinus()) {
            return TreeUtils.negate(getFraction(node.getContent(), allowUnaryMinus, allowParens));
        } else if (allowParens && node.isParenthesis()) {
            return getFraction(node.getContent(), allowUnaryMinus, allowParens);
        }

        throw new Error("La expresion no es un nodo del tipo fraccion");
    }

    // Retorna verdadero si el nodo tiene un polinomio en el denominador
    // Ejemplo 5/x o 1/2X^2
    public static boolean hasPolynomialInDenominator(TreeNode originalNode) {
        if (!(isFraction(originalNode))) {
            return false;
        }

        TreeNode node = originalNode.cloneDeep(); // Se puede negar internamente
        TreeNode fraction = getFraction(node);
        TreeNode denominator = fraction.getChild(1);
        return isPolynomialTerm(denominator);
    }

    public static TreeNode removeUnnecessaryParens(TreeNode node) {
        return removeUnnecessaryParens(node, false);
    }

    /*
      Return true if the equation is of the form factor * factor = 0 or factor^power = 0
      // e.g (x - 2)^2 = 0, x(x + 2)(x - 2) = 0
    */
    public static boolean canFindRoots(Tree equation) {
        TreeNode left = equation.getLeftNode();
        TreeNode right = equation.getRightNode();

        Boolean zeroRightSide = isConstant(right)
                && right.getIntegerValue() == 0;

        Boolean isMulOrPower = left.esProducto() || left.esPotencia();
        Boolean isXWithExponent = TreeUtils.isSymbol(left) && left.getExponent() != 1;

        if (!(zeroRightSide && (isMulOrPower || isXWithExponent))) {
            return false;
        }

        // If the left side of the equation is multiplication, filter out all the factors
        // that do evaluate to constants because they do not have roots. If the
        // resulting array is empty, there is no roots to be found. Do a similiar check
        // for when the left side is a power node.
        // e.g 2^7 and (33 + 89) do not have solutions when set equal to 0

        if (isXWithExponent) {
            return true;
        } else if (left.esProducto()) {
            for (TreeNode arg : left.getArgs()) {
                if (!resolvesToConstant(arg)) {
                    return true;
                }
            }
            return false;
        } else if (left.esPotencia()) {
            return !resolvesToConstant(left);
        } else {
            return false;
        }
    }

    // Returns true if the node is a constant or can eventually be resolved to
    // a constant.
    // e.g. 2, 2+4, (2+4)^2 would all return true. x + 4 would return false
    public static boolean resolvesToConstant(TreeNode node) {

        if (node == null) {
            return false;
        }
        if (node.esOperador()) {

            for (TreeNode child : node.getArgs()) {
                if (child != null) {
                    if (!resolvesToConstant(child)) {
                        return false;
                    }
                }
            }

            return true;
        } else if (node.isParenthesis()) {
            return resolvesToConstant(node.getChild(0));
        } else if (TreeUtils.isConstant(node, true)) {
            return true;
        } else if (TreeUtils.isSymbol(node, true)) {
            return false;
        } else if (node.isUnaryMinus()) {
            return resolvesToConstant(node.getChild(0));
        } else {
            throw new Error("Unsupported node type: " + node.toExpression());
        }
    }

    // Itera a traves de un nodo y retorna el ultimo termino con el nombre del simbolo
    // Retorna nulo si no hay terminos con el simbolo en el nodo.
    //Ejemplo 4x^2 + 2x + y + 2 con nombre de simbolo x retorna 2 x
    public static TreeNode getLastSymbolTerm(TreeNode node, String symbolName) {

        //Primero valida si el mismo es un termino polinomial con un nombre de simbolo
        if (isSymbolTerm(node)) {
            return node;
        }
        // If it's a sum of terms, look through the operands for a term
        // with `symbolName`
        else if (node.esSuma()) {
            for (int i = node.getArgs().size() - 1; i >= 0; i--) {
                TreeNode child = node.getChild(i);
                if (child.esSuma()) {
                    return getLastSymbolTerm(child, symbolName);
                } else if (isSymbolTerm(child)) {
                    return child;
                }
            }
        }
        return null;
    }

    // Returns if `node` is a term with symbol `symbolName`
    public static Boolean isSymbolTerm(TreeNode node) {
        return isPolynomialTerm(node) ||
                hasDenominatorSymbol(node);
    }

    // Return if `node` has a symbol in its denominator
    // e.g. true for 1/(2x)
    // e.g. false for 5x
    private static boolean hasDenominatorSymbol(TreeNode node) {
        if (node.esDivision()) {
            return node.getChild(1).toExpression().contains("X");
        }
        return false;
    }

    // Iterates through a node and returns the denominator if it has a
    // symbolName in its denominator
    // e.g. 1/(2x) with `symbolName=x` would return 2x
    // e.g. 1/(x+2) with `symbolName=x` would return x+2
    // e.g. 1/(x+2) + (x+1)/(2x+3) with `symbolName=x` would return (2x+3)
    public static TreeNode getLastDenominatorWithSymbolTerm(TreeNode node) {

        // First check if the node itself has a symbol in the denominator
        if (hasDenominatorSymbol(node)) {
            return node.getChild(1);
        }
        // Otherwise, it's a sum of terms. e.g. 1/x + 1(2+x)
        // Look through the operands for a
        // denominator term with `symbolName`
        else if (node.esSuma()) {
            for (int i = node.getArgs().size() - 1; i >= 0; i--) {
                TreeNode child = node.getChild(i);
                if (child.esSuma()) {
                    return getLastDenominatorWithSymbolTerm(child);
                } else if (hasDenominatorSymbol(child)) {
                    return child.getChild(1);
                }
            }
        }
        return null;
    }

    // Iterates through a node and returns the last term that does not have the
    // symbolName including other polynomial terms, and constants or constant
    // fractions
    // e.g. 4x^2 with `symbolName=x` would return 4
    // e.g. 4x^2 + 2x + 2/4 with `symbolName=x` would return 2/4
    // e.g. 4x^2 + 2x + y with `symbolName=x` would return y
    public static TreeNode getLastNonSymbolTerm(TreeNode node) {
        if (isPolynomialTerm(node)) {

            if (isSymbolFraction(node, true)) {
                // Genero la fraccion coeficiente
                return TreeNode.createOperator("/",
                        TreeNode.createConstant(node.getCoefficient()), node.getRightNode().cloneDeep());
            } else if (node.getCoefficient() == 1) {
                return null;
            }

            return TreeNode.createConstant(node.getCoefficient());
        } else if (hasDenominatorSymbol(node)) {
            return null;
        } else if (node.esOperador()) {
            for (int i = node.getArgs().size() - 1; i >= 0; i--) {
                TreeNode child = node.getChild(i);
                if (child.esSuma()) {
                    return getLastNonSymbolTerm(child);
                } else if (!child.toExpression().contains("X")) {
                    return child;
                }
            }
        }

        return null;
    }

    // Given a node, will determine if the expression is in the form of a quadratic
    // e.g. `x^2 + 2x + 1` OR `x^2 - 1` but not `x^3 + x^2 + x + 1`
    public static boolean isQuadratic(TreeNode node) {
        if (!node.esSuma()) {
            return false;
        }

        if (node.getArgs().size() > 3) {
            return false;
        }

        List<TreeNode> secondDegreeTerms = new ArrayList<>();
        List<TreeNode> firstDegreeTerms = new ArrayList<>();
        List<TreeNode> constantTerms = new ArrayList<>();

        for (TreeNode child : node.getArgs()) {
            if (isPolynomialTermOfDegree(child, 2)) {
                secondDegreeTerms.add(child);
            } else if (isPolynomialTermOfDegree(child, 1)) {
                firstDegreeTerms.add(child);
            } else if (isConstant(child, true)) {
                constantTerms.add(child);
            }
        }

        // Check that there is one second degree term and at most one first degree
        // term and at most one constant term
        if (secondDegreeTerms.size() != 1 || firstDegreeTerms.size() > 1 ||
                constantTerms.size() > 1) {
            return false;
        }

        // check that there are no terms that don't fall into these groups
        if ((secondDegreeTerms.size() + firstDegreeTerms.size() +
                constantTerms.size()) != node.getArgs().size()) {
            return false;
        }

        return true;
    }

    // Given a degree, returns a function that checks if a node
    // is a polynomial term of the given degree.
    private static boolean isPolynomialTermOfDegree(TreeNode node, int degree) {
        if (isPolynomialTerm(node)) {
            Integer exponent = node.getExponent();
            return exponent != null && exponent.equals(degree);
        }
        return false;
    }

    /**
     * Calculates the  greatest common divisor
     *
     * @param a First value
     * @param b Second Value
     * @return El estado de la simplificacion
     */
    public static Integer calculateGCD(Integer a, Integer b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    // Removes any parenthesis around nodes that can't be resolved further.
    // Input must be a top level expression.
    // Returns a node.
    public static TreeNode removeUnnecessaryParens(TreeNode node, Boolean rootNode) {
        // Parens that wrap everything are redundant.
        // NOTE: removeUnnecessaryParensSearch recursively removes parens that aren't
        // needed, while this step only applies to the very top level expression.
        // e.g. (2 + 3) * 4 can't become 2 + 3 * 4, but if (2 + 3) as a top level
        // expression can become 2 + 3
        if (rootNode) {
            while (node.isParenthesis()) {
                node = node.getChild(0);
            }
        }
        return removeUnnecessaryParensSearch(node);
    }

    // Recursively moves parenthesis around nodes that can't be resolved further if
    // it doesn't change the value of the expression. Returns a node.
    // NOTE: after this function is called, every parenthesis node in the
    // tree should always have an operator node or unary minus as its child.
    private static TreeNode removeUnnecessaryParensSearch(TreeNode node) {
        if (node == null) {
            return null;
        }

        if (node.esRaiz()) {
            return removeUnnecessaryParensInFunctionNode(node);
        } else if (node.esOperador()) {
            return removeUnnecessaryParensInOperatorNode(node);
        } else if (node.isParenthesis()) {
            return removeUnnecessaryParensInParenthesisNode(node);
        } else if (TreeUtils.isConstant(node, true) || TreeUtils.isSymbol(node)) {
            return node;
        } else if (node.isUnaryMinus()) {
            TreeNode content = node.getChild(0);
            node.setChild(0, removeUnnecessaryParensSearch(content));
            return node;
        } else {
            throw new Error("Unsupported node type: " + node.toExpression());
        }
    }

    // Removes unncessary parens for each operator in an operator node, and removes
    // unncessary parens around operators that can't be simplified further.
    // Returns a node.
    private static TreeNode removeUnnecessaryParensInOperatorNode(TreeNode node) {
        // Special case: if the node is an exponent node and the base
        // is an operator, we should keep the parentheses for the base.
        // e.g. (2x)^2 -> (2x)^2 instead of 2x^2
        if (node.esPotencia() && node.getChild(0).isParenthesis()) {
            TreeNode base = node.getChild(0);
            if (base.getChild(0).esOperador()) {
                base.setChild(0, removeUnnecessaryParensSearch(base.getChild(0)));
                node.setChild(1, removeUnnecessaryParensSearch(node.getChild(1)));

                return node;
            }
        }

        for (int i = 0; i < node.getArgs().size(); i++) {
            node.setChild(i, removeUnnecessaryParensSearch(node.getChild(i)));
        }

        // Sometimes, parens are around expressions that have been simplified
        // all they can be. If that expression is part of an addition or subtraction
        // operation, we can remove the parenthesis.
        // e.g. (x+4) + 12 -> x+4 + 12
        if (node.esSuma()) {
            for (int i = 0; i < node.getArgs().size(); i++) {
                TreeNode child = node.getChild(i);
                if (child.isParenthesis() && !canCollectOrCombine(child.getContent())) {
                    // remove the parens by replacing the child node (in its args list)
                    // with its content
                    node.setChild(i, child.getContent());
                }
                node.setChild(i, removeUnnecessaryParensSearch(node.getChild(i)));
            }
        }
        // This is different from addition because when subtracting a group of terms
        //in parenthesis, we want to distribute the subtraction.
        // e.g. `(2 + x) - (1 + x)` => `2 + x - (1 + x)` not `2 + x - 1 + x`
        else if (node.esResta()) {
            if (node.getChild(0).isParenthesis() &&
                    !canCollectOrCombine(node.getChild(0).getContent())) {
                node.setChild(0, node.getChild(0).getContent());
            }
        }

        return node;
    }

    // Returns true if any of the collect or combine steps can be applied to the
    // expression tree `node`.
    private static boolean canCollectOrCombine(TreeNode node) {
        return simplifyService.canCollectLikeTerms(node) ||
                resolvesToConstant(node) ||
                canSimplifyPolynomialTerms(node);
    }

    // Returns true if the node is an operation node with parameters that are
    // polynomial terms that can be combined in some way.
    protected static boolean canSimplifyPolynomialTerms(TreeNode node) {
        return (simplifyService.canAddLikeTermPolynomialNodes(node) ||
                simplifyService.canMultiplyLikeTermPolynomialNodes(node) ||
                canRearrangeCoefficient(node));
    }

    // Parentheses are unnecessary when their content is a constant e.g. (2)
    // or also a parenthesis node, e.g. ((2+3)) - this removes those parentheses.
    // Note that this means that the type of the content of a ParenthesisNode after
    // this step should now always be an OperatorNode (including unary minus).
    // Returns a node.
    private static TreeNode removeUnnecessaryParensInParenthesisNode(TreeNode node) {
        // polynomials terms can be complex trees (e.g. 3x^2/5) but don't need parens
        // around them
        if (TreeUtils.isPolynomialTerm(node.getContent())) {
            // also recurse to remove any unnecessary parens within the term
            // (e.g. the exponent might have parens around it)
            if (node.getContent().getArgs() != null) {
                TreeNode nodeContent = node.getContent();
                for (int i = 0; i < nodeContent.getArgs().size(); i++) {
                    nodeContent.setChild(i, removeUnnecessaryParensSearch(nodeContent.getChild(i)));
                }
            }
            node = node.getContent();
        }
        // If the content is just one symbol or constant, the parens are not
        // needed.
        else if (TreeUtils.isConstant(node.getContent(), true) ||
                TreeUtils.isIntegerFraction(node.getContent()) ||
                TreeUtils.isDividedBySymbol(node.getContent(), false) ||
                TreeUtils.isSymbol(node.getContent())) {
            node = node.getContent();
        }
        // If the content is just one function call, the parens are not needed.
        else if (node.getContent().esRaiz()) {
            node = node.getContent();
            node = removeUnnecessaryParensSearch(node);
        }
        // If there is an operation within the parens, then the parens are
        // likely needed. So, recurse.
        else if (node.getContent().esOperador()) {
            node.setChild(0, removeUnnecessaryParensSearch(node.getContent()));
            // exponent nodes don't need parens around them
            if (node.getContent().esPotencia()) {
                node = node.getContent();
            }
        }
        // If the content is also parens, we have doubly nested parens. First
        // recurse on the child node, then set the current node equal to its child
        // to get rid of the extra parens.
        else if (node.getContent().isParenthesis()) {
            node = removeUnnecessaryParensSearch(node.getContent());
        } else if (node.getContent().isUnaryMinus()) {
            node.setContent(removeUnnecessaryParensSearch(node.getContent()));
        } else {
            throw new Error("Unsupported node type: " + node.getContent().toExpression());
        }

        return node;
    }

    // Removes unncessary parens for each argument in a function node.
    // Returns a node.
    private static TreeNode removeUnnecessaryParensInFunctionNode(TreeNode node) {

        for (int i = 0; i < node.getArgs().size(); i++) {
            TreeNode child = node.getChild(i);
            if (child != null && child.isParenthesis()) {
                child = child.getContent();
            }
            node.setChild(i, removeUnnecessaryParensSearch(child));
        }

        return node;
    }

    public static boolean isDoubleInteger(double val) {
        return (val % (int) val == 0);
    }

    public static boolean isNegativeProduct(TreeNode node) {
        if (!node.esProducto()) {
            return false;
        }
        if (!isNegative(node.getLeftNode())) {
            return false;
        }
        return true;
    }

    public static TreeNode groupConstantCoefficientAndSymbol(TreeNode node) {

        if (node == null) {
            return null;
        }
        if (node.getArgs() != null) {
            for (int i = 0; i < node.getArgs().size(); i++) {
                node.setChild(i, groupConstantCoefficientAndSymbol(node.getChild(i)));
            }
        }
        if (!node.esProducto() && !node.esPotencia()) {
            return node;
        }

        if (node.esProducto()) {
            if (node.getArgs().size() != 2) {
                return node;
            }
            if (!isConstant(node.getLeftNode())) {
                return node;
            }
            if (!isSymbol(node.getRightNode()) &&
                    !(node.getRightNode().esPotencia() && isSymbol(node.getRightNode().getLeftNode()))) {
                return node;
            }

            if (node.getRightNode().esPotencia()) {
                // Si es potencia, genero el temrino
                TreeNode newNode = TreeNode.createPolynomialTerm("X", node.getRightNode().getRightNode().getIntegerValue(), node.getRightNode().getLeftNode().getIntegerValue());
                newNode.multiplyCoefficient(node.getLeftNode().getValue());
                return newNode;

            } else if (node.getRightNode().getValue().contains("X")) {
                TreeNode newNode = null;

                if (new Integer(0).equals(node.getLeftNode().getIntegerValue())) {
                    // Puede quedar en 0, en ese caso devuelvo la constante 0
                    newNode = TreeNode.createConstant(0);
                } else {
                    newNode = node.getRightNode().clone();
                    newNode.multiplyCoefficient(node.getLeftNode().getValue());
                }

                return newNode;
            }
        } else if (node.esPotencia()) {
            if (!isConstant(node.getRightNode()) || !isSymbol(node.getLeftNode())) {
                return node;
            }
            // Si es potencia, genero el temrino con coeficiente 1
            return TreeNode.createPolynomialTerm("X", node.getRightNode().getIntegerValue(), 1);
        }

        return node;
    }

    public static boolean haveSameOperatorLevelCode(TreeNode treeNodeA, TreeNode treeNodeB) {
        return (OperatorLevel.getBySimbol(treeNodeA.getValue()).getCode() ==
                OperatorLevel.getBySimbol(treeNodeB.getValue()).getCode());
    }

    public static boolean hasDifferentLevelAncestors(TreeNode treeNode) {
        boolean hasDiffLevelAnc = false;
        TreeNode node = treeNode;
        while (node != null && !hasDiffLevelAnc) {
            hasDiffLevelAnc = haveSameOperatorLevelCode(treeNode, node.getParentNode());
            node = node.getParentNode();
        }
        return hasDiffLevelAnc;
    }

    public static String inverseComparator(String comparator) {
        if (">".equals(comparator)) {
            return "<";
        } else if (">=".equals(comparator)) {
            return "<=";
        } else if ("<".equals(comparator)) {
            return ">";
        } else if ("<=".equals(comparator)) {
            return ">=";
        } else if ("=".equals(comparator)) {
            return "=";
        } else if ("+".equals(comparator)) {
            return "-";
        } else if ("-".equals(comparator)) {
            return "+";
        } else if ("*".equals(comparator)) {
            return "/";
        } else if ("/".equals(comparator)) {
            return "*";
        } else if ("^".equals(comparator)) {
            return "R";
        } else if ("R".equals(comparator)) {
            return "^";
        } else {
            throw new Error("Comparador no soportado: " + comparator);
        }
    }

    public static List<TreeNode> singletonList(TreeNode node) {
        List<TreeNode> list = new ArrayList<>();
        list.add(node);
        return list;
    }

    public static boolean esReduciblePorOperacionesBasicas(TreeNode node) {
        boolean esReducible = false;
        if (node.esSuma() || node.esResta() || node.esProducto() || node.esDivision()
                || node.esPotencia() || node.esRaiz()) {
            if (TreeUtils.isConstant(node.getLeftNode()) && TreeUtils.isConstant(node.getRightNode())) {
                esReducible = true;
            }
        }
        return esReducible;
    }

    public static boolean esReduciblePorDistributiva(TreeNode node) {
        boolean esReducible = false;
        if (node.esProducto() || node.esDivision()) {
            if (esBinomio(node)) {
                esReducible = true;
            }
        }
        return esReducible;
    }

    private static boolean esBinomio(TreeNode originalNode) {
        boolean esBinomio = false;
        TreeNode node = originalNode.clone();
        if(originalNode.isParenthesis()){
            node = originalNode.getLeftNode();
        }
        if(node.esSuma() || node.esResta()){
            if(false){//TODO el hijo izq debe ser incognita y el resto ctes
                esBinomio = true;
            }
        }
        return esBinomio;
    }

    //TODO
    public static boolean esReduciblePorAsociativa(TreeNode node) {
        boolean esReducible = false;
        return esReducible;
    }

    public static boolean esReduciblePorPotenciaDeBinomio(TreeNode node) {
        boolean esReducible = false;
        return esReducible;
    }
}
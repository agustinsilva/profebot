package ar.com.profebot.resolutor.service;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.InvalidStep;
import ar.com.profebot.resolutor.utils.Reduction;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class InvalidOptionService {

    /***
     * @param originalTree arbol del cual se quiere generar un paso inválido
     * @return devuelve la info del nuevo arbol generado con un pasaje de términos inválido
     **/
    public InvalidStep getFirstInvalidOption(Tree originalTree) {
        InvalidStep.InvalidTypes type;
        // Clonado para evitar modificar el original
        Tree tree = originalTree.clone();

        //1. Elegir rama izquierda o derecha del árbol
        boolean equalsLeftBranch = true;
        TreeNode node = tree.getLeftNode();
        if (chooseRightNode()) {
            node = tree.getRightNode();
            equalsLeftBranch = false;
        }

        //2. Elegir un nodo random del subarbol que sea NO TERMINAL
        //generar un valor random de iteraciones (nivel dentro del subarbol)
        int nodeLevel = getRandomValue(0, tree.getDepth());
        TreeNode randomNode = getRandomNonTerminalNode(node, nodeLevel);

        //3. Si el nodo elegido es hijo del signo Igual, pasar este nodo y su decendencia al otro miembro
        //4. Si el nodo elegido NO es hijo del signo Igual, validar niveles de sus ancestros
        /*4.a Ancestros de distinto nivel: pasar este nodo (inviertiendo operador) y uno de sus hijos/ramas
         * 4.b Ancestros de igual nivel: pasar este nodo (sin invertir el operador) y uno de sus hijos/ramas */
        boolean reverseOperator;
        if (!isEqualsChild(nodeLevel)) {
            reverseOperator = TreeUtils.hasDifferentLevelAncestors(randomNode);
            if (reverseOperator) {
                type = getTipoPasajeTerminoAncestrosDeDistintoNivel(randomNode);
                String newReverseOperator = TreeUtils.inverseComparator(randomNode.getValue());
                randomNode.setValue(newReverseOperator);
            } else {
                type = getTipoPasajeTerminoAncestrosDeMismoNivel(randomNode);
            }
        } else {
            type = getTipoPasajeTerminoPrimerAncestro(randomNode);
        }

        /****Magic begins****/
        //5. Reestructurar el arbol
        // 5.a Sacar el randomNode y una de sus ramas (elijo random). La otra rama, debe enlazarse al padre de randomNode
        boolean chooseRightBranch = chooseRightNode();
        if (isLeftChild(randomNode)) {
            if (chooseRightBranch) {
                randomNode.getParentNode().setLeftNode(randomNode.getLeftNode());
                randomNode.setLeftNode(null);
            } else {
                randomNode.getParentNode().setLeftNode(randomNode.getRightNode());
                randomNode.setRightNode(null);
            }
        } else {
            if (chooseRightBranch) {
                randomNode.getParentNode().setRightNode(randomNode.getLeftNode());
                randomNode.setLeftNode(null);
            } else {
                randomNode.getParentNode().setRightNode(randomNode.getRightNode());
                randomNode.setRightNode(null);
            }
        }
        // 5.b El randomNode se debe enlazar al otro lado del igual. La rama que se encontraba alli
        // sera el nuevo hijo del randomNode
        if (equalsLeftBranch) {
            if (chooseRightBranch) {
                randomNode.setLeftNode(tree.getRightNode());
            } else {
                randomNode.setRightNode(tree.getRightNode());
            }
            tree.setRightNode(randomNode);
        } else {
            if (chooseRightBranch) {
                randomNode.setLeftNode(tree.getLeftNode());
            } else {
                randomNode.setRightNode(tree.getLeftNode());
            }
            tree.setLeftNode(randomNode);
        }

        return new InvalidStep(type, tree);
    }

    private boolean isLeftChild(TreeNode node) {
        return node.getChildIndex() == 0;
    }

    /**
     * Iterar nodos: random izquierdo o derecho mientras que no sea nodo terminal.
     * Si es nodo terminal, elegir el padre. Si no, seguir iterando
     **/
    private TreeNode getRandomNonTerminalNode(TreeNode treeNode, int nodeLevel) {
        TreeNode randomNode = treeNode;
        int i = 1;
        while (i <= nodeLevel) {
            if (chooseRightNode() && randomNode.getRightNode() != null) {
                randomNode = randomNode.getRightNode();
            } else if (randomNode.getLeftNode() != null) {
                randomNode = randomNode.getLeftNode();
            } else {  // es nodo TERMINAL
                nodeLevel = i;
                randomNode = randomNode.getParentNode();
            }
            i++;
        }
        return randomNode;
    }

    /**
     * Si el nivel del nodo es 0, entonces es hijo del signo Igual (o signos Menor, Mayor, whatever...)
     * El valor del nivel del nodo, en este caso, no incluye el nivel del nodo raiz.
     */
    private boolean isEqualsChild(int nodeLevel) {
        return (nodeLevel == 0);
    }

    /**
     * Elige de forma RANDOM el nodo izquierdo o derecho
     **/
    private boolean chooseRightNode() {
        int random = this.getRandomValue(0, 2);
        return (random == 1);
    }

    /***
     * @param origin (inclusive)
     * @param bound (exclusive)
     * */
    private int getRandomValue(int origin, int bound) {
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return generator.nextInt(origin, bound);
    }

    protected InvalidStep.InvalidTypes getTipoPasajeTerminoPrimerAncestro(TreeNode node) {
        if (node.esSuma()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA;
        } else if (node.esResta()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA;
        } else if (node.esDivision()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION;
        } else if (node.esProducto()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION;
        } else if (node.esRaiz()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ;
        } else if (node.esPotencia()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA;
        }
        return InvalidStep.InvalidTypes.CONSTANTE_NO_ENCONTRADA;
    }

    protected InvalidStep.InvalidTypes getTipoPasajeTerminoAncestrosDeMismoNivel(TreeNode node) {
        if (node.esSuma()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_SUMA_COMO_SUMA;
        } else if (node.esResta()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_RESTA_COMO_RESTA;
        } else if (node.esDivision()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_DIVISION_COMO_DIVISION;
        } else if (node.esProducto()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_MULTIPLICACION_COMO_MULTIPLICACION;
        } else if (node.esRaiz()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_RAIZ_COMO_RAIZ;
        } else if (node.esPotencia()) {
            return InvalidStep.InvalidTypes.PASAJE_DE_TERMINO_DE_DESCENDENCIA_POTENCIA_COMO_POTENCIA;
        }
        return InvalidStep.InvalidTypes.CONSTANTE_NO_ENCONTRADA;
    }

    protected InvalidStep.InvalidTypes getTipoPasajeTerminoAncestrosDeDistintoNivel(TreeNode node) {
        if (node.esProducto()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_MULTIPLICACION_COMO_DIVISION_SIENDO_TERMINO_DE_SUMATORIA;
        } else if (node.esDivision()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_DIVISION_COMO_MULTIPLICACION_SIENDO_TERMINO_DE_SUMATORIA;
        } else if (node.esSuma()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_SUMA_COMO_RESTA_SIENDO_TERMINO_MUTIPLICATIVO;
        } else if (node.esResta()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_RESTA_COMO_SUMA_SIENDO_TERMINO_MUTIPLICATIVO;
        } else if (node.esPotencia()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_POTENCIA_COMO_RAIZ;
        } else if (node.esRaiz()) {
            return InvalidStep.InvalidTypes.PASAJE_TERMINO_DE_RAIZ_COMO_POTENCIA;
        }
        return InvalidStep.InvalidTypes.CONSTANTE_NO_ENCONTRADA;
    }

    /***
     * @param originalTree arbol del cual se quiere generar un paso inválido
     * @return devuelve la info del nuevo arbol generado con un pasaje de términos inválido
     **/
    public InvalidStep getSecondInvalidOption(Tree originalTree) {

        // Clonado para evitar modificar el original
        Tree tree = originalTree.clone();
        InvalidStep.InvalidTypes type = null;
        //1. Elegir rama izquierda o derecha del árbol
        // (dejo que revise el arbol entero, y de ahi decida que paso tomar)

        // TODO esta revisando solo los 2 primeros hijos, si lo queremos dejar así deberíamos restringir en las validaciones que tengan 2 argumentos max

        //2. Analizar los posibles subarboles reducibles y guardarlos en la lista
        ArrayList<Reduction> reducibles = getReduciblesList(tree.getRootNode()); // Itera sobre todos los nodos recursivamente

        //3. Elegir uno de forma random y hacer la reduccion
        Reduction selectedReduction = reducibles.get(getRandomValue(0, reducibles.size()));
        Tree reducedTree = resolveExpression(selectedReduction);

        return new InvalidStep(getSecondInvalidOptionInvalidTypes(selectedReduction), reducedTree);
    }

    protected InvalidStep.InvalidTypes getSecondInvalidOptionInvalidTypes(Reduction reduction) {

        TreeNode node = reduction.getTreeNode();
        switch(reduction.getReductionType()){

            case OPERACIONES_BASICAS:
                if (node.esProducto()) {
                    return InvalidStep.InvalidTypes.MULTIPLICACION_RESUELTA_INCORRECTAMENTE;
                } else if (node.esDivision()) {
                    return InvalidStep.InvalidTypes.DIVISION_RESUELTA_INCORRECTAMENTE;
                } else if (node.esSuma()) {
                    return InvalidStep.InvalidTypes.SUMA_RESUELTA_INCORRECTAMENTE;
                } else if (node.esResta()) {
                    return InvalidStep.InvalidTypes.RESTA_RESUELTA_INCORRECTAMENTE;
                } else if (node.esPotencia()) {
                    return InvalidStep.InvalidTypes.POTENCIA_RESUELTA_INCORRECTAMENTE;
                } else if (node.esRaiz()) {
                    return InvalidStep.InvalidTypes.RAIZ_RESUELTA_INCORRECTAMENTE;
                }
                break;
            case DISTRIBUTIVA:
                TreeNode leftNode = node.getLeftNode();
                if (leftNode.isParenthesis()){leftNode = leftNode.getContent();}
                TreeNode rightNode = node.getRightNode();
                if (rightNode.isParenthesis()){rightNode = rightNode.getContent();}

                if (!TreeUtils.esBinomio(leftNode) || !TreeUtils.esBinomio(rightNode)) {
                    return InvalidStep.InvalidTypes.DISTRIBUTIVA_BASICA_MAL_RESUELTA;
                }else{
                    return InvalidStep.InvalidTypes.DISTRIBUTIVA_DOBLE_MAL_RESUELTA;
                }
            case ASOCIATIVA:
                return InvalidStep.InvalidTypes.ASOCIATIVA_MAL_RESUELTA;

            case POTENCIA_DE_BINOMIO:
                return InvalidStep.InvalidTypes.DISTRIBUTIVA_DE_POTENCIA_SOBRE_BINOMIO;
        }


        return InvalidStep.InvalidTypes.CONSTANTE_NO_ENCONTRADA;
    }

    private ArrayList<Reduction> getReduciblesList(TreeNode node) {

        ArrayList<Reduction> reducibles = new ArrayList<>();
        if (node != null){
            if (TreeUtils.esReduciblePorOperacionesBasicas(node)) {
                Reduction r = new Reduction(node, Reduction.ReductionType.OPERACIONES_BASICAS);
                reducibles.add(r);
            } else if (TreeUtils.esReduciblePorDistributiva(node)) {
                Reduction r = new Reduction(node, Reduction.ReductionType.DISTRIBUTIVA);
                reducibles.add(r);
            } else if (TreeUtils.esReduciblePorAsociativa(node)) {
                Reduction r = new Reduction(node, Reduction.ReductionType.ASOCIATIVA);
                reducibles.add(r);
            } else if (TreeUtils.esReduciblePorCuadradoDeBinomio(node)) {
                Reduction r = new Reduction(node, Reduction.ReductionType.POTENCIA_DE_BINOMIO);
                reducibles.add(r);
            }

            // Se fija si alguno de sus hijos es reducible
            if (node.getArgs() != null) {
                for(TreeNode child: node.getArgs()){
                    reducibles.addAll(getReduciblesList(child));
                }
            }
        }


        return reducibles;
    }

    /**
     * Dado una reducción propuesta, se resuelve la expresión y se reconstruye el arbol generado
     * @param reduction
     * @return
     */
    protected Tree resolveExpression(Reduction reduction){

        TreeNode reducedNode = null;

        switch(reduction.getReductionType()){

            case OPERACIONES_BASICAS:
                reducedNode = resolveBasicOperation(reduction.getTreeNode());
                break;
            case DISTRIBUTIVA:
                reducedNode = resolveDistributive(reduction.getTreeNode());
                break;
            case ASOCIATIVA:
                reducedNode = resolveAsociative(reduction.getTreeNode());
                break;
            case POTENCIA_DE_BINOMIO:
                reducedNode = resolveBinomialPower(reduction.getTreeNode());
                break;
        }

        // El arbol se regenera a partir del nodo
        return getTreeFromTreeNode(reducedNode);
    }

    private Tree getTreeFromTreeNode(TreeNode node){
        // El arbol se regenera a partir del nodo
        TreeNode rootNode = node;
        while(!rootNode.getValue().equals("=")) { // TODO faltan los otros simbolos
            rootNode = rootNode.getParentNode();
        }

        Tree tree = new Tree();
        tree.setRootNode(rootNode);

        return tree;
    }

    protected TreeNode resolveBasicOperation(TreeNode originalNode) {
        // si es una operación básica, se peude resolver directamente desed el nodo
        TreeNode inverseNode = originalNode.clone();
        inverseNode.setValue(TreeUtils.inverseComparator(originalNode.getValue())); // Cambio de operador
        TreeNode newNode = TreeNode.createConstant(inverseNode.getOperationResult());
        // No tengo que perder la relación con los demás nodos padres, sino se rompe el arbol
        newNode.setParentNode(originalNode.getParentNode());
        newNode.setChildIndex(originalNode.getChildIndex());

        return newNode;
    }

    protected TreeNode resolveDistributive(TreeNode originalNode) {

        // Comienza con un operador a distribuir: * o /
        TreeNode operatorNode = originalNode.clone();
        TreeNode reducedNode;

        // Pueden ser 2 oplinomios o 1 Cte/X y un polinomio
        if (!TreeUtils.esBinomio(operatorNode.getLeftNode()) || !TreeUtils.esBinomio(operatorNode.getRightNode())){
            // Básica: 2(x+4). En este caso, distribuir sólo a un término del binomio.
            // En este ejemplo, la propuesta sería 2x+4 (la elección de a qué término del binomio distribuir, es random).
            TreeNode simpleNode;
            TreeNode binomioNode;
            if (TreeUtils.esBinomio(operatorNode.getLeftNode())){
                simpleNode = operatorNode.getLeftNode();
                binomioNode = operatorNode.getRightNode();
            }else{
                simpleNode = operatorNode.getRightNode();
                binomioNode = operatorNode.getLeftNode();
            }

            // Remuevo parentesis por si existen
            if (binomioNode.isParenthesis()){binomioNode = binomioNode.getContent();}

            // Termino a elegir del binomio para distribuir
            int childIndex = getRandomValue(0, 2);
            TreeNode leftNode;
            TreeNode rightNode;
            if (childIndex == 0){
                leftNode = TreeNode.createOperator(originalNode.getValue(),
                        binomioNode.getLeftNode(),
                        simpleNode);
                rightNode = binomioNode.getRightNode();
            }else{
                leftNode = binomioNode.getLeftNode();
                rightNode = TreeNode.createOperator(originalNode.getValue(),
                        binomioNode.getRightNode(),
                        simpleNode);
            }

            // Se distribuye 1 solo nodo, queda el operador del binomio como operador principal
            reducedNode = TreeNode.createOperator(binomioNode.getValue(),
                    leftNode, rightNode);
        }else{
            // Doble: (x+1)(x+2). En este caso, distribuir de manera “incompleta”, es decir,
            // agarrar un término del primer binomio y multiplicarlo con un término del segundo binomio,
            // y luego hacer lo mismo con el otro par de términos. En este ejemplo, una propuesta podría ser x2+2 (x*x + 1*2).
            // El agrupamiento de términos sería random.

            TreeNode leftBinomioNode = operatorNode.getLeftNode();
            if (leftBinomioNode.isParenthesis()){leftBinomioNode = leftBinomioNode.getContent();}
            TreeNode rightBinomioNode = operatorNode.getRightNode();
            if (rightBinomioNode.isParenthesis()){rightBinomioNode = rightBinomioNode.getContent();}

            // 1er termino del nodo izquierdo, con el 1ero o 2do del derecho
            int childIndex = getRandomValue(0, 2);
            TreeNode firstNode = TreeNode.createOperator(originalNode.getValue(),
                    leftBinomioNode.getLeftNode(),
                    rightBinomioNode.getChild(childIndex));

            // 2do termino del nodo izquierdo, con el 1ero o 2do del derecho
            TreeNode secondNode = TreeNode.createOperator(originalNode.getValue(),
                    leftBinomioNode.getLeftNode(),
                    rightBinomioNode.getChild(childIndex));

            // queda conformado como 2 nodos distribuidos al azar, sumandose
            reducedNode = TreeNode.createOperator("+",
                    firstNode, secondNode);
        }

        // No tengo que perder la relación con los demás nodos padres, sino se rompe el arbol
        reducedNode.setParentNode(originalNode.getParentNode());
        reducedNode.setChildIndex(originalNode.getChildIndex());

        return reducedNode;
    }


    protected TreeNode resolveAsociative(TreeNode originalNode) {

        TreeNode node = originalNode.clone();
        TreeNode productNode = null;
        TreeNode singleNode = null;
        Integer childIndex; // el hijo que quiero asociar mal, para que visualmente se entienda mejor
        if (node.getLeftNode().esProducto() || node.getLeftNode().esDivision()) {
            productNode = node.getLeftNode();
            singleNode = node.getRightNode();
            childIndex = 1;
        }else {
            productNode = node.getRightNode();
            singleNode = node.getLeftNode();
            childIndex = 0;
        }

        // Ej: 3+5*8   singleNode: 3, productNode = 5*8, tiene que quedar (3+5)*8=8*8
        TreeNode invalidNode = TreeNode.createOperator(originalNode.getValue(),
                singleNode, productNode.getChild(childIndex));

        TreeNode resultNode = TreeNode.createConstant(invalidNode.getOperationResult());

        // Si childIdnex = 0 => Tengo este caso: 3+5*8, el resultado va en la posicion 0
        // Si childIdnex = 1 => Tengo este caso: 3*5+8, el resultado va en la posicion 1
        TreeNode[] orderedNodes = new TreeNode[2];
        orderedNodes[childIndex] = resultNode;
        orderedNodes[1-childIndex] = productNode.getChild(1-childIndex);
        TreeNode reducedNode = TreeNode.createOperator(productNode.getValue(),
                orderedNodes[0], orderedNodes[1]);

        // No tengo que perder la relación con los demás nodos padres, sino se rompe el arbol
        reducedNode.setParentNode(originalNode.getParentNode());
        reducedNode.setChildIndex(originalNode.getChildIndex());

        return reducedNode;
    }


    protected TreeNode resolveBinomialPower(TreeNode originalNode) {

        TreeNode powerNode = originalNode.clone();
        TreeNode binomioNode = powerNode.getLeftNode();
        TreeNode exponent = powerNode.getRightNode();
        if (binomioNode.isParenthesis()){binomioNode = binomioNode.getContent();}

        // Distribuyo el exponente en cada miembro del binomio
        TreeNode leftNode =  TreeNode.createOperator("^",
                binomioNode.getLeftNode(), exponent.clone());

        TreeNode rightNode =  TreeNode.createOperator("^",
                binomioNode.getRightNode(), exponent.clone());

        TreeNode reducedNode = TreeNode.createOperator(binomioNode.getValue(),
                leftNode, rightNode);

        // No tengo que perder la relación con los demás nodos padres, sino se rompe el arbol
        reducedNode.setParentNode(originalNode.getParentNode());
        reducedNode.setChildIndex(originalNode.getChildIndex());

        return reducedNode;
    }
}

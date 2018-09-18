package ar.com.profebot.resolutor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.InvalidStep;
import ar.com.profebot.resolutor.utils.Reduction;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class InvalidOptionService {

    public InvalidStep[] getInvalidSteps(Tree originalTree) {

        InvalidStep[] steps = new InvalidStep[2];

        List<InvalidStep> invalidStepsTypeI = getFirstInvalidOptions(originalTree);
        List<InvalidStep> invalidStepsTypeII = getSecondInvalidOptions(originalTree);

        // Saco 1 de tipo 1 si encontro alguna
        int index = 0;
        if (!invalidStepsTypeI.isEmpty()){
            steps[index] = popRandomElement(invalidStepsTypeI);
            index++;
        }

        // Saco 1 de tipo 2 si encontro alguna
        if (!invalidStepsTypeII.isEmpty()){
            steps[index] = popRandomElement(invalidStepsTypeII);
            index++;
        }

        // Muy raro, no encontro nada, genero 2 triviales
        if (index == 0){
            steps[index] = getTrivialInvalidOption(originalTree);
            steps[++index] = getTrivialInvalidOption(originalTree);

            // Encontro 1 sola
        }else if (index == 1){
            // Busco otra en las de tipo 1 si hay
            if (!invalidStepsTypeI.isEmpty()){
                steps[index] = popRandomElement(invalidStepsTypeI);
                index++;
                // Sino en tipo 2
            }else if (!invalidStepsTypeII.isEmpty()){
                steps[index] = popRandomElement(invalidStepsTypeII);
                index++;
            }

            // Si ya no habia de ningun tipo
            if (index == 1){
                steps[index] = getTrivialInvalidOption(originalTree);
                index++;
            }
        }


       /* steps[0] = getFirstInvalidOption(originalTree);
        steps[1] = getSecondInvalidOption(originalTree);
        */
        return steps;
    }

    /***
     * @param originalTree arbol del cual se quiere generar un paso inválido
     * @return devuelve la info del nuevo arbol generado con un pasaje de términos inválido
     **/
    public List<InvalidStep> getFirstInvalidOptions(Tree originalTree) {

        List<InvalidStep> invalidSteps = new ArrayList<>();

        // Clonado para evitar modificar el original
        Tree tree = originalTree.clone();
        tree.generateTwoWayLinkedTree();

        // 1. Obtengo todos los nodos no temrinales del arbol
        List<TreeNode> nonTerminalNodeList = getNonTerminalNodeList(tree.getRootNode());
        if (nonTerminalNodeList.isEmpty()){return invalidSteps;} // Sin resultados

        // Genero 2 nodos, por si el otro metodo no logra conseguir nada
        TreeNode candidateNode1;
        TreeNode candidateNode2;
        Boolean chooseRightBranch1;
        Boolean chooseRightBranch2;
        if (nonTerminalNodeList.size() ==1){
            candidateNode1 = nonTerminalNodeList.get(0);
            candidateNode2 = nonTerminalNodeList.get(0);

            // Cuando tengo 1 solo resultado, fuerzo a que tome las 2 ramas del mismo nodo
            chooseRightBranch1 = true;
            chooseRightBranch2 = false;
        }else{
            // Selecciona un nodo al azar y lo remueve de la lista
            candidateNode1 = popRandomElement(nonTerminalNodeList);
            candidateNode2 = popRandomElement(nonTerminalNodeList);
            chooseRightBranch1 = chooseRightNode();
            chooseRightBranch2 = chooseRightNode();
        }

        invalidSteps.add(getInvalidOptionFromNode(candidateNode1, chooseRightBranch1, tree));
        invalidSteps.add(getInvalidOptionFromNode(candidateNode2, chooseRightBranch2, tree));

        return invalidSteps;
    }

    private <T> T popRandomElement(List<T> termList) {

        T element;
        int randomIndex = 0;
        if (termList.size() > 1){
            randomIndex = getRandomValue(0, termList.size());
        }

        element = termList.get(randomIndex);
        termList.remove(randomIndex);

        return element;
    }

    private InvalidStep getInvalidOptionFromNode(TreeNode candidateNode, Boolean chooseRightBranch, Tree tree) {

        InvalidStep.InvalidTypes type;
        TreeNode randomNode = candidateNode.cloneDeep();

        // En el caso de que sea una X, voy a dividir el nodo para que pueda seguir la lógica normal
        if (TreeUtils.isSymbol(randomNode)){
            TreeNode symbolNode;
            if (!randomNode.getCoefficient().equals(1)){
                symbolNode = TreeNode.createOperator("*",
                        TreeNode.createConstant(randomNode.getCoefficient()),
                        TreeNode.createPolynomialTerm("X", randomNode.getExponent(), 1));
            } else if (!randomNode.getExponent().equals(1)){
                symbolNode = TreeNode.createOperator("^",
                        TreeNode.createPolynomialTerm("X",1,  randomNode.getCoefficient()),
                        TreeNode.createConstant(randomNode.getExponent()));
            } else{
                symbolNode = randomNode.clone();
            }

            // Actualizo el arbol (como esta clonado puedo)
            symbolNode.setChildIndex(randomNode.getChildIndex());
            symbolNode.setParentNode(randomNode.getParentNode());
            randomNode = symbolNode;
            randomNode.getParentNode().setChild(randomNode.getChildIndex(), symbolNode);
        }

        int nodeLevel = Tree.getNodeDepth(randomNode);
        boolean equalsLeftBranch = nodeBelongsToLeftBranch(candidateNode);

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

    private boolean nodeBelongsToLeftBranch(TreeNode node) {
        if (node == null){return false;}

        while(node.getParentNode() != null && !TreeUtils.isRootNode(node.getParentNode())){
            node = node.getParentNode(); // Busca el nodo anterior a la raiz
        }
        if (node.getParentNode() == null){return false;}

        return node.getChildIndex().equals(0);
    }

    private boolean isLeftChild(TreeNode node) {
        return node.getChildIndex() == 0;
    }

    /**
     * Iterar nodos: random izquierdo o derecho mientras que no sea nodo terminal.
     * Si es nodo terminal, elegir el padre. Si no, seguir iterando
     **/
    private TreeNode getRandomNonTerminalNode(TreeNode treeNode) {

        List<TreeNode> nonTerminalNodeList = getNonTerminalNodeList(treeNode);
        if (nonTerminalNodeList.isEmpty()){return null;}
        // Si hay 1 solo nodo, devuelvo eso
        if (nonTerminalNodeList.size()==1){return nonTerminalNodeList.get(0);}

        // Sino hago un random
        return nonTerminalNodeList.get(getRandomValue(0, nonTerminalNodeList.size()));
    }

    private List<TreeNode> getNonTerminalNodeList(TreeNode node) {
        List<TreeNode> list = new ArrayList<>();

        if (node != null){
            // No terminal
            if (!TreeUtils.isConstant(node) && !TreeUtils.isSingleSymbol(node)){

                // El = es no temrinal pero se ignora
                if (!TreeUtils.isRootNode(node)) {
                    list.add(node);
                }

                if (node.getArgs()!=null){
                    for(TreeNode child: node.getArgs()){
                        list.addAll(getNonTerminalNodeList(child));
                    }
                }
            }
        }

        return list;
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
        } else if (node.esProducto() || TreeUtils.isSymbol(node) ) {
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
    public List<InvalidStep> getSecondInvalidOptions(Tree originalTree) {

        List<InvalidStep> invalidSteps = new ArrayList<>();

        // Clonado para evitar modificar el original
        Tree tree = originalTree.clone();
        InvalidStep.InvalidTypes type = null;
        tree.generateTwoWayLinkedTree();

        //1. Analizar los posibles subarboles reducibles y guardarlos en la lista
        ArrayList<Reduction> reducibles = getReduciblesList(tree.getRootNode()); // Itera sobre todos los nodos recursivamente
        if (reducibles.isEmpty()){return invalidSteps;}

        //2. Elegir dos de forma random (si se puede) y hacer la reduccion
        Reduction reduction1;
        Reduction reduction2 = null;
        if (reducibles.size() == 1){
            reduction1 = reducibles.get(0);
        }else{
            reduction1 = popRandomElement(reducibles);
            reduction2 = popRandomElement(reducibles);
        }

        invalidSteps.add(getSecondInvalidOption(reduction1));
        if (reduction2 != null){
            invalidSteps.add(getSecondInvalidOption(reduction2));
        }

        return invalidSteps;
    }

    private InvalidStep getSecondInvalidOption(Reduction reduction){
        Tree reducedTree = resolveExpression(reduction);
        return new InvalidStep(getSecondInvalidOptionInvalidTypes(reduction), reducedTree);
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

        // Se asigna al padre el mismo, sino sigue apuntando al otro nodo
        reducedNode.getParentNode().setChild(reducedNode.getChildIndex(),reducedNode );

        // El arbol se regenera a partir del nodo
        return getTreeFromTreeNode(reducedNode);
    }

    private Tree getTreeFromTreeNode(TreeNode node){
        // El arbol se regenera a partir del nodo
        TreeNode rootNode = node;
        while(!TreeUtils.isRootNode(rootNode)) {
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

    private InvalidStep getTrivialInvalidOption(Tree originalTree) {

        Tree tree = originalTree.clone();

        // Nodo inventado
        String newOpertator = this.getRandomValue(0, 2) ==0? "+": "*";
        // Random branch
        boolean chooseRightNode = chooseRightNode();

        TreeNode rootNode = tree.getRootNode();
        TreeNode newNode;
        if (chooseRightNode){
            newNode = TreeNode.createOperator(newOpertator,
                    rootNode.getRightNode(),
                    TreeNode.createConstant( this.getRandomValue(1, 15)));

            rootNode.setRightNode(newNode);
            newNode.setParentNode(rootNode);
            newNode.setChildIndex(1);

        }else{
            newNode = TreeNode.createOperator(newOpertator,
                    rootNode.getLeftNode(),
                    TreeNode.createConstant( this.getRandomValue(3, 18)));

            rootNode.setLeftNode(newNode);
            newNode.setParentNode(rootNode);
            newNode.setChildIndex(0);
        }

        InvalidStep.InvalidTypes type;
        if (newNode.esSuma()){
            type = InvalidStep.InvalidTypes.AGREGAR_SUMA_A_MIEMBRO;
        }else{
            type = InvalidStep.InvalidTypes.AGREGAR_PRODUCTO_A_MIEMBRO;
        }

        return new InvalidStep(type, tree);

    }
}

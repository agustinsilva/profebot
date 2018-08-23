package ar.com.profebot.resolutor.service;

import java.util.concurrent.ThreadLocalRandom;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.InvalidStep;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class InvalidOptionService {

    /***
     * @param originalTree arbol del cual se quiere generar un paso inválido
     * @return devuelve la info del nuevo arbol generado con un pasaje de términos inválido
     **/
    public InvalidStep getFirstInvalidOption(Tree originalTree) {

        // Clonado para evitar modificar el original
        Tree tree  = originalTree.clone();

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
                String newReverseOperator = TreeUtils.inverseComparator(randomNode.getValue());
                randomNode.setValue(newReverseOperator);
            }
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
        // TODO Resolver esto, devuelvo un mock por lo pronto
        return new InvalidStep(InvalidStep.InvalidTypes.DISTRIBUTIVA_BASICA_MAL_RESUELTA,  tree);
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

    /***
     * @param originalTree arbol del cual se quiere generar un paso inválido
     * @return devuelve la info del nuevo arbol generado con un pasaje de términos inválido
     **/
    public InvalidStep getSecondInvalidOption(Tree originalTree) {

        // Clonado para evitar modificar el original
        Tree tree  = originalTree.clone();

        // TODO Resolver esto, devuelvo un mock por lo pronto
        return new InvalidStep(InvalidStep.InvalidTypes.DISTRIBUTIVA_BASICA_MAL_RESUELTA,  tree);
    }
}

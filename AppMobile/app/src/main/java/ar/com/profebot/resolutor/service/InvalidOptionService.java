package ar.com.profebot.resolutor.service;

import java.util.concurrent.ThreadLocalRandom;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.utils.TreeUtils;

public class InvalidOptionService {

    private void getFirstInvalidOption(Tree tree){
        //1. Elegir rama izquierda o derecha del árbol
        TreeNode node = tree.getLeftNode();
        if(chooseRightNode()){
            node =  tree.getRightNode();
        }

        //2. Elegir un nodo random del subarbol que sea NO TERMINAL
        //generar un valor random de iteraciones (nivel dentro del subarbol)
        // TODO el random deberia ser entre 0 y la profundidad del árbol
        int nodeLevel = getRandomValue(0,6);
        TreeNode randomNode = getRandomNonTerminalNode(node, nodeLevel);

        //3. Si el nodo elegido es hijo del signo Igual, pasar este nodo y su decendencia al otro miembro
        //4. Si el nodo elegido NO es hijo del signo Igual, validar niveles de sus ancestros
            /*4.a Ancestros de distinto nivel: pasar este nodo (inviertiendo operador) y uno de sus hijos
             * 4.b Ancestros de igual nivel: pasar este nodo (sin invertir el operador) y uno de sus hijos */
        boolean reverseOperator = false;
        if(!isEqualsChild(nodeLevel)){
            reverseOperator =  TreeUtils.hasDifferentLevelAncestors(randomNode);
        }

        /****Magic begins****/
        //5. Reestructurar el arbol
        // Sacar el randomNode y uno de sus hijos. El otro hijo debe enlazarse al padre de randomNode
        // El randomNode se debe enlazar al otro lado del igual. La rama que se encontraba alli
        // sera el nuevo hijo del randomNode

    }

    /** Iterar nodos: random izquierdo o derecho mientras que no sea nodo terminal.
     * Si es nodo terminal, elegir el padre. Si no, seguir iterando **/
    private TreeNode getRandomNonTerminalNode(TreeNode treeNode, int nodeLevel){
        TreeNode randomNode = treeNode;
        int i = 1;
        while(i <= nodeLevel){
            if(chooseRightNode() && randomNode.getRightNode() != null){
                randomNode = randomNode.getRightNode();
            }else if(randomNode.getLeftNode() != null){
                randomNode = randomNode.getLeftNode();
            }else{  // es nodo TERMINAL
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
     * */
    private boolean isEqualsChild(int nodeLevel){
        if(nodeLevel == 0){
            return true;
        }
        return false;
    }

    /**
     * Elige de forma RANDOM el nodo izquierdo o derecho
     **/
    private boolean chooseRightNode(){
        int random = this.getRandomValue(0,2);
        if(1 == random){
            return true;
        }
        return true;
    }

    /***
     * @param origin (inclusive)
     * @param bound (exclusive)
     * */
    private int getRandomValue(int origin, int bound){
        ThreadLocalRandom generator = ThreadLocalRandom.current();
        return generator.nextInt(origin, bound);
    }
}

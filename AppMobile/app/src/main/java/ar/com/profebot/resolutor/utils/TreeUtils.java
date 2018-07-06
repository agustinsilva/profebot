package ar.com.profebot.resolutor.utils;

import java.util.Set;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;

public class TreeUtils {

    public static Boolean esConstante(TreeNode treeNode){
        // TODO esConstante
        throw new UnsupportedOperationException();
    }

    public static Boolean zeroValue(TreeNode treeNode){
       return hasValue(treeNode, "0");
    }

    public static Boolean hasValue(TreeNode treeNode, String value){
        return (treeNode!=null && value.equals(treeNode.getValue()) );
    }

    public static Boolean esIncognita(TreeNode treeNode){
        return (treeNode!=null && treeNode.getValue().contains("X") );
    }

    public static Boolean esFraccion(TreeNode treeNode){
        return (treeNode!=null &&
                treeNode.esDivision() &&
                esConstante(treeNode.getLeftNode()) &&
                esConstante(treeNode.getRightNode())
        );
    }

    public static Boolean esNegativo(TreeNode treeNode){
        if (treeNode == null){return null;}

        if (esConstante(treeNode)){
            return treeNode.getDoubleValue()< 0;
        }

        if (esFraccion(treeNode)){
            TreeNode numeratorTree = treeNode.getLeftNode();
            TreeNode denominatorTree = treeNode.getLeftNode();
            if (numeratorTree.getDoubleValue() < 0 || denominatorTree.getDoubleValue() < 0) {
                return !(numeratorTree.getDoubleValue() < 0 && denominatorTree.getDoubleValue() < 0);
            }
        }else if (esIncognita(treeNode)){
            return treeNode.getValue().contains("-");
        }

        return false;
    }

    public static Boolean esFraccionOConstante(TreeNode treeNode){
        return TreeUtils.esConstante(treeNode) ||
                TreeUtils.esFraccion(treeNode);
    }

    public static Tree achatarArbol(Tree tree){

        // TODO pensar si amerita clonar el arbol para que no se modifique el original
        agruparSumas(tree);
        agruparProductos(tree);

        return tree;
    }

    private static void agruparSumas(Tree tree) {
        // TODO agruparSumas
        //  va a quedar 1 solo nodo con el operador, y como hijos todas las constantes y términos X.
        throw new UnsupportedOperationException();
    }

    private static void agruparProductos(Tree tree) {
        // TODO agruparProductos
        //  revisar cómo afecta a las divisiones, pero varias constantes/X multiplicándose, sería parecido a las sumas y restas.
        throw new UnsupportedOperationException();
    }

    public static Set<String> listaOperadores(TreeNode treeNode){
        // TODO listaOperadores: obtiene el listado de operadores recursivamente para saber si son todos iguales
        throw new UnsupportedOperationException();
    }

    public static TreeNode negate(TreeNode numeratorNode) {
        // TODO negate: negar el nodo (considerar si es X, constante o polinomio
        throw new UnsupportedOperationException();
    }
}

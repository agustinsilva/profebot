package ar.com.profebot.resolutor.utils;

import java.util.HashSet;
import java.util.Set;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;

public class TreeUtils {

    public static Boolean esConstante(TreeNode treeNode){
        return (treeNode!=null && !esIncognita(treeNode) && contieneNumero(treeNode.getValue()));
    }

    private static Boolean contieneNumero(String value){
        return (value.contains("0") || value.contains("1") || value.contains("2")
        || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6")
        || value.contains("7") || value.contains("8") || value.contains("9"));
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

    public static Boolean isNegative(TreeNode treeNode){
        if (treeNode == null){return null;}

        if (esConstante(treeNode)){
            return treeNode.getIntegerValue()< 0;
        }

        if (esFraccion(treeNode)){
            TreeNode numeratorTree = treeNode.getLeftNode();
            TreeNode denominatorTree = treeNode.getRightNode();
            if (numeratorTree.getIntegerValue() < 0 || denominatorTree.getIntegerValue() < 0) {
                return !(numeratorTree.getIntegerValue() < 0 && denominatorTree.getIntegerValue() < 0);
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

    /**
     * obtiene el listado de operadores recursivamente para saber si son todos iguales
     * @param treeNode
     * @return
     */
    public static Set<String> listaOperadores(TreeNode treeNode){
        Set<String> lista = new HashSet<>();
        operadorEnNodo(treeNode, lista);
        return lista;
    }

    private static void operadorEnNodo(TreeNode treeNode, Set<String> listaOperadores){
        if (treeNode ==null){
           return;
        }

        if (treeNode.esOperador()){
            listaOperadores.add(treeNode.getValue());
        }
        operadorEnNodo(treeNode.getLeftNode(), listaOperadores);
        operadorEnNodo(treeNode.getRightNode(), listaOperadores);
    }

    public static TreeNode negate(TreeNode numeratorNode) {
        // TODO negate: negar el nodo (considerar si es X, constante o polinomio
        throw new UnsupportedOperationException();
    }

    public static boolean isPolynomialTerm(TreeNode node) {
        // TODO isPolynomialTerm:
        throw new UnsupportedOperationException();
    }
}

package ar.com.profebot.resolutor.utils;

import java.util.Set;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.container.TreeNode;

public class TreeUtils {

    public static Boolean esOperador(TreeNode treeNode){
        // TODO esOperador
        throw new UnsupportedOperationException();
    }

    public static Boolean esConstante(TreeNode treeNode){
        // TODO esConstante
        throw new UnsupportedOperationException();
    }

    public static Boolean esIncognita(TreeNode treeNode){
        // TODO esIncognita
        throw new UnsupportedOperationException();
    }

    public static Boolean esFraccion(TreeNode treeNode){
        // TODO esFraccion
        throw new UnsupportedOperationException();
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
}

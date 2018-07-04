package ar.com.profebot.resolutor.service;

import java.util.ArrayList;
import java.util.List;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.resolutor.container.ResolutionStep;

public class ResolutorService {

    private List<ResolutionStep> resolutionStepList = new ArrayList<>();

        public Tree resolveExpression(Tree tree){
            // TODO resolveExpression
            SimplifyService simplifyService = new SimplifyService(resolutionStepList);
        // TODO Ejecutar todas las simplificaciones

        throw new UnsupportedOperationException();
    }

    // TODO Agrear rutina: Fracciones con numerador mixto. Ejemplo: (2+x)/5 -> (2/5 + x/5)
    // TODO Agrear rutina: Distribuir constantes * parentesis. Ejemplo: 2(x+3) -> (2*x + 2*3)
    // TODO Agrear rutina: Distribuir signo menos parentesis. Ejemplo:  -(x+5) -> (-x + -5)
    // TODO Agrear rutina: Expandir la base de una potencia. Ejemplo: (2x + 3)^2 -> (2x + 3) (2x + 3)
    // TODO Agrear rutina: Distribuir constante * paréntesis. Ejemplo: 2*(3+x) = (2*3 + 2x)
        // -> Se distribuye la constante por cada nodo.
        // -> Se simplifica, sumando o multiplicando según se pueda.
    // TODO Agrear rutina: Distribuir el producto de dos paréntesis. Ejemplo:  (2x+3)*(4x+5)
        // -> Se distribuye cada nodo, multiplicando al paréntesis. 2x*(4x+5) + 3*(4x+5)
        // -> Se distribuye cada producto. 2x*4x + 2x*5 + 3 * 4x + 3*5
        // -> Se simplifica. 8x^2 + 22x + 15
    // TODO Agrear rutina: Invertir doble divisiones. Ejemplo: x/(2/3) -> x * 3/2

}

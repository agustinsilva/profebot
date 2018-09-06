package ar.com.profebot.resolutor.container;

import ar.com.profebot.parser.container.Tree;

public class InvalidStep {

    public enum InvalidTypes {

        /* Rtas. Incorrectas Tipo I */

        // ejemplo: x=3+5, quiero pasar el 5 sumando
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA,

        // ejemplo: x=3-5, quiero pasar el 5 restando
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA,

        // ejemplo: x=3*5, quiero pasar el 5 multiplicando
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION,

        // ejemplo: x=3/5, quiero pasar el 5 dividiendo
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION,

        // ejemplo: x=3^5, quiero pasar el 5 cómo potencia de x
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA,

        // ejemplo: x=sqrt(3), quiero pasar la raíz cuadrada cómo raíz de x
        PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ,

        // ejemplo: 4+3*5, quiero pasar el 5 dividiendo
        PASAJE_TERMINO_DE_MULTIPLICACION_COMO_DIVISION_SIENDO_TERMINO_DE_SUMATORIA,

        //ejemplo: 4+3/5, quiero pasar el 5 multiplicando. Ejemplo 2: 4*5+6, quiero pasar el 6 restando
        PASAJE_TERMINO_DE_DIVISION_COMO_MULTIPLICACION_SIENDO_TERMINO_DE_SUMATORIA,
        //ejemplo: 4*(3+5), quiero pasar el 5 restando
        PASAJE_TERMINO_DE_SUMA_COMO_RESTA_SIENDO_TERMINO_MUTIPLICATIVO,
        //ejemplo: 4*(3-5), quiero pasar el 5 sumando
        PASAJE_TERMINO_DE_RESTA_COMO_SUMA_SIENDO_TERMINO_MUTIPLICATIVO,
        PASAJE_TERMINO_DE_RAIZ_COMO_POTENCIA,
        PASAJE_TERMINO_DE_POTENCIA_COMO_RAIZ,
        // x=4-5+6, quiero pasar el 6 sumando
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_SUMA_COMO_SUMA,

        // x=4-5-6, quiero pasar el 6 sumando
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_RESTA_COMO_RESTA,

        // x=4/5*6, quiero pasar el 6 multiplicando
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_MULTIPLICACION_COMO_MULTIPLICACION,

        // x=4/5/6, quiero pasar el 6 dividiendo
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_DIVISION_COMO_DIVISION,

        // x=4^5^6, quiero pasar el 6 como potencia
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_POTENCIA_COMO_POTENCIA,

        // x=sqrt(sqrt(3)), quiero pasar la segunda raíz como raíz
        PASAJE_DE_TERMINO_DE_DESCENDENCIA_RAIZ_COMO_RAIZ,


        /* Rtas. Incorrectas Tipo II */

        // operación aritmética mal resuelta.
        // ejemplo: 4+5 da 10
        SUMA_RESUELTA_INCORRECTAMENTE,

        // ejemplo: 4-5 da 0
        RESTA_RESUELTA_INCORRECTAMENTE,

        // ejemplo: 4*5 da 30
        MULTIPLICACION_RESUELTA_INCORRECTAMENTE,

        // ejemplo: 4/5 da 4,5
        DIVISION_RESUELTA_INCORRECTAMENTE,

        // ejemplo: 4^5 da 20
        POTENCIA_RESUELTA_INCORRECTAMENTE,

        // ejemplo: sqrt100 da 50
        RAIZ_RESUELTA_INCORRECTAMENTE,

        // distributiva mal hecha
        // ejemplo: 22+3 da 4+3 → 7
        DISTRIBUTIVA_BASICA_MAL_RESUELTA,

        // ejemplo: 1+12+3 da 2+3 → 5
        DISTRIBUTIVA_DOBLE_MAL_RESUELTA,

        // asociativa mal hecha
        // se da cuando se mezclan precedencias debido a operadores de distinto tipo;
        // ejemplo: 2+3*4 → hago 5*4 → 20, cuando debería dar 14
        ASOCIATIVA_MAL_RESUELTA,

        // distributiva de potencia de un binomio
        // ejemplo: 2+3^2 → hago 2^2 + 3^2
        DISTRIBUTIVA_DE_POTENCIA_SOBRE_BINOMIO,

        CONSTANTE_NO_ENCONTRADA
        ;
    }

    private InvalidTypes type;
    private Tree tree;

    public InvalidStep(InvalidTypes type, Tree tree) {
        this.type = type;
        this.tree = tree;
    }

    public InvalidTypes getType() {
        return type;
    }

    public void setType(InvalidTypes type) {
        this.type = type;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}

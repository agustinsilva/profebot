package ar.com.profebot.resolutor.service;

import java.util.List;

import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.container.ResolutionStep;

public class SimplifyService {

    private List<ResolutionStep> resolutionStepList;

    public SimplifyService(List<ResolutionStep> resolutionStepList) {
            this.resolutionStepList = resolutionStepList;
    }

    public void reduceExponentByZero(TreeNode treeNode){
        // TODO reduceExponentByZero: Buscar recursivamente un nodo con ^
        //Si se encuentra, verificar si el exponente es 0
        //De ser así, reemplazar tod o el subárbol con la constante 1. (Guardar el paso realizado)
        throw new UnsupportedOperationException();
    }

    public void reduceMultiplicationByZero(TreeNode treeNode){
        // TODO reduceMultiplicationByZero: (algo) * 0
        throw new UnsupportedOperationException();
    }


    public void reduceZeroDividedByAnything(TreeNode treeNode){
        // TODO reduceZeroDividedByAnything: 0 / (algo)
        throw new UnsupportedOperationException();
    }


    public void removeAdditionOfZero(TreeNode treeNode){
        // TODO removeAdditionOfZero: (algo) + 0 = (algo)
        throw new UnsupportedOperationException();
    }


    public void removeDivisionByOne(TreeNode treeNode){
        // TODO removeDivisionByOne: (algo) / 1 = (algo)
        throw new UnsupportedOperationException();
    }

    public void removeExponentBaseOne(TreeNode treeNode){
        // TODO removeExponentBaseOne: 1^(algo) = 1
        throw new UnsupportedOperationException();
    }

    public void removeExponentByOne(TreeNode treeNode){
        // TODO removeExponentByOne: (algo)^1  = (algo)
        throw new UnsupportedOperationException();
    }

    public void removeMultiplicationByNegativeOne(TreeNode treeNode){
        // TODO removeMultiplicationByNegativeOne: (algo) * (-1) = - (algo)
        throw new UnsupportedOperationException();
    }

    public void removeMultiplicationByOne(TreeNode treeNode){
        // TODO removeMultiplicationByOne: (algo) * 1 = (algo)
        throw new UnsupportedOperationException();
    }

    public void rearrangeCoefficient(TreeNode treeNode){
        // TODO rearrangeCoefficient: Arreglar coeficientes: ej: X*5 = 5X
        throw new UnsupportedOperationException();
    }

    public void simplifyFractions(TreeNode treeNode){
        // TODO simplifyFractions
        simplifyIntFractions(treeNode);
        resolveFractionNumeratorAndDenominator(treeNode);
        reduceFractions(treeNode);
        resolveConstantFractionAdition(treeNode);
    }

    private void simplifyIntFractions(TreeNode treeNode){
        // TODO simplifyIntFractions: Buscar fracciones, y solo simplificarlas si dan un número entero (ej: 8/4 = 2).
        throw new UnsupportedOperationException();
    }

    private void resolveFractionNumeratorAndDenominator(TreeNode treeNode){
        // TODO resolveFractionNumeratorAndDenominator: Buscar fracciones y sumar su numerador, o denominador (si son enteros).
        throw new UnsupportedOperationException();
    }

    private void reduceFractions(TreeNode treeNode){
        // TODO reduceFractions: Simplificar fracciones. Ej:  2/4 --> 1/2 ; -1/-3 --> 1/3
        // Buscar el máximo común divisor.
        // Dividir numerador y denominador por ese valor. (o mostrar el subpaso de mostrar numerador y denominador multiplicando el MCD, ejemplo: 15/6 -> (5*3)/(2*3))

        throw new UnsupportedOperationException();
    }

    private void resolveConstantFractionAdition(TreeNode treeNode){
        // TODO resolveConstantFractionAdition: Buscar sumas de constantes/fracciones con fracciones, y generar 3 pasos:
        // Buscar el común denominador (mínimo común divisor), y multiplicar numerador y denominador según corresponda. 2/6 + 1/4 -> (2*2)/(6*2) + (1*3)/(4*3)
        // Simplificar la multiplicación en el numerador y denominador.
        // Simplificar la suma del numerador.
        // Verificar si se puede simplificar la fracción.

        throw new UnsupportedOperationException();
    }

    public void addLikeTerms(TreeNode treeNode){
        // TODO addLikeTerms: Asociar términos sumados con X. Ejemplo: 2x + 4x => 6x
        throw new UnsupportedOperationException();
    }

    public void multiplyLikeTerms(TreeNode treeNode){
        // TODO multiplyLikeTerms: Multiplicar terminos con X. Ejemplo: 2x * x^2 * 5x => 10 x^4
        throw new UnsupportedOperationException();
    }

    public void cancelLikeTerms(TreeNode treeNode){
        // TODO cancelLikeTerms: Simplificar términos iguales en divisiones. Ejemplo: (2x^2 * 5) / 2x^2 => 5 / 1
        throw new UnsupportedOperationException();
    }

    public void simplifyPolynomialFraction(TreeNode treeNode){
        // TODO simplifyPolynomialFraction:  2x/4 --> x/2    10x/5 --> 2x
        throw new UnsupportedOperationException();
    }

    public void simplifyLikeBaseDivision(TreeNode treeNode){
        // TODO simplifyLikeBaseDivision: Simplificar términos divididos con la misma base. Ejemplo: (2x+5)^8 / (2x+5)^2 = (2x+5)^6
        throw new UnsupportedOperationException();
    }

    private void divideByGCD(TreeNode treeNode){
        // TODO divideByGCD: Simplificar coeficientes en divisiones. Ejemplo: 2 / 4x -> 1 / 2x
        throw new UnsupportedOperationException();
    }

}

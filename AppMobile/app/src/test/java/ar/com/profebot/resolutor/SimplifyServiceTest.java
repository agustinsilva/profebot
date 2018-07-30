package ar.com.profebot.resolutor;

import org.junit.Before;
import org.junit.Test;

import ar.com.profebot.parser.container.TreeNode;
import ar.com.profebot.resolutor.service.SimplifyService;

public class SimplifyServiceTest extends SimplifyService {

    @Before
    public void setUp() {
        System.out.println(" Setting up Resolutor Test...");
    }

    @Test
    public void stepThrough() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.stepThrough(node);
    }

    @Test
    public void step() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.step(node);
    }

    @Test
    public void arithmeticSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.arithmeticSearch(node);
    }

    @Test
    public void basicSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.basicSearch(node);
    }

    @Test
    public void breakUpNumeratorSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.breakUpNumeratorSearch(node);
    }

    @Test
    public void collectAndCombineSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.collectAndCombineSearch(node);
    }

    @Test
    public void distributeSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.distributeSearch(node);
    }

    @Test
    public void divisionSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.divisionSearch(node);
    }

    @Test
    public void fractionsSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.fractionsSearch(node);
    }

    @Test
    public void functionsSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.functionsSearch(node);
    }

    @Test
    public void multiplyFractionsSearch() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.multiplyFractionsSearch(node);
    }

    @Test
    public void reduceExponentByZero() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.reduceExponentByZero(node);
    }

    @Test
    public void reduceMultiplicationByZero() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.reduceMultiplicationByZero(node);
    }

    @Test
    public void reduceZeroDividedByAnything() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.reduceZeroDividedByAnything(node);
    }

    @Test
    public void removeAdditionOfZero() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeAdditionOfZero(node);
    }

    @Test
    public void removeDivisionByOne() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeDivisionByOne(node);
    }

    @Test
    public void removeExponentBaseOne() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeExponentBaseOne(node);
    }

    @Test
    public void simplifyDoubleUnaryMinus() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.simplifyDoubleUnaryMinus(node);
    }

    @Test
    public void removeExponentByOne() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeExponentByOne(node);
    }

    @Test
    public void removeMultiplicationByNegativeOne() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeMultiplicationByNegativeOne(node);
    }

    @Test
    public void removeMultiplicationByOne() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.removeMultiplicationByOne(node);
    }

    @Test
    public void rearrangeCoefficient() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.rearrangeCoefficient(node);
    }

    @Test
    public void addConstantAndFraction() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.addConstantAndFraction(node);
    }

    @Test
    public void addConstantFractions() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.addConstantFractions(node);
    }

    @Test
    public void addLikeTerms() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.addLikeTerms(node, false);
    }

    @Test
    public void addLikePolynomialTerms() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.addLikePolynomialTerms(node);
    }

    @Test
    public void addLikeNthRootTerms() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.addLikeNthRootTerms(node);
    }

    @Test
    public void addLikeTermNodes() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        String termSubclass = "";
        super.addLikeTermNodes(node, termSubclass, null);
    }

    @Test
    public void multiplyLikeTerms() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.multiplyLikeTerms(node, false);
    }

    @Test
    public void simplifyFractionSigns() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.simplifyFractionSigns(node);
    }

    @Test
    public void cancelLikeTerms() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.cancelLikeTerms(node);
    }

    @Test
    public void simplifyPolynomialFraction() {
        // TODO generar test
        TreeNode node = new TreeNode("");
        super.simplifyPolynomialFraction(node);
    }

}

package ar.com.profebot.parser.service;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionParserServiceTest {

    @Test
    public void isConstant_1() {
        String exp = "2+4-6*7=0";
        testConstantFunction(exp);
    }

    @Test
    public void isConstant_2() {
        String exp = "2^2/4-6*7/8=0";
        testConstantFunction(exp);
    }

    @Test
    public void isNotConstant_1() {
        String exp = "2/4-6X*7/8=0";
        testNonConstantFunction(exp);
    }

    @Test
    public void isNotConstant_2() {
        String exp = "X^2/4-6X*7/8=0";
        testNonConstantFunction(exp);
    }

    @Test
    public void isLinear_1() {
        String exp = "2X+4=0";
        testLinearFunction(exp);
    }

    @Test
    public void isLinear_2() {
        String exp = "2X+4/5-5+(6/7)*X-X=0";
        testLinearFunction(exp);
    }

    @Test
    public void isNotLinear_1() {
        String exp = "(2X+4)/(2X-4)=0";
        testNonLinearFunction(exp);
    }

    @Test
    public void isNotLinear_2() {
        String exp = "X^2/4+4=0";
        testNonLinearFunction(exp);
    }

    @Test
    public void isHomographic_1() {
        String exp = "(2X+4)/(2X-4)=0";
        testHomographicFunction(exp);
    }

    @Test
    public void isHomographic_2() {
        String exp = "(X+4-X+3)/(2X+5X-4)=0";
        testHomographicFunction(exp);
    }

    @Test
    public void isNotHomographic_1() {
        String exp = "X^2/4+4=0";
        testNonHomographicFunction(exp);
    }

    @Test
    public void isNotHomographic_2() {
        String exp = "(X+4-X+3)/5=0";
        testNonHomographicFunction(exp);
    }

    @Test
    public void isQuadratic_1() {
        String exp = "(X^2+3X+4)=0";
        testQuadraticFunction(exp);
    }

    @Test
    public void isQuadratic_2() {
        String exp = "6X^2+16=0";
        testQuadraticFunction(exp);
    }

    @Test
    public void isQuadratic_x2() {
        String exp = "X^2=0";
        testQuadraticFunction(exp);
    }

    @Test
    public void isNotQuadratic_1() {
        String exp = "(6X^2+16)/(X+9)=0";
        testNonQuadraticFunction(exp);
    }

    @Test
    public void isNotQuadratic_2() {
        String exp = "16/(X^2+9)=0";
        testNonQuadraticFunction(exp);
    }

    private void testConstantFunction(String exp) {
        Assert.assertTrue(FunctionParserService.isConstant(exp));
        Assert.assertFalse(FunctionParserService.isLinear(exp));
        Assert.assertFalse(FunctionParserService.isQuadratic(exp));
        Assert.assertFalse(FunctionParserService.isHomographic(exp));

        Assert.assertEquals(FunctionParserService.FunctionType.CONSTANT, FunctionParserService.getFunctionType(exp));
    }

    private void testNonConstantFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isConstant(exp));
        Assert.assertFalse(FunctionParserService.FunctionType.CONSTANT.equals(FunctionParserService.getFunctionType(exp)));
    }

    private void testLinearFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isConstant(exp));
        Assert.assertTrue(FunctionParserService.isLinear(exp));
        Assert.assertFalse(FunctionParserService.isQuadratic(exp));
        Assert.assertFalse(FunctionParserService.isHomographic(exp));

        Assert.assertEquals(FunctionParserService.FunctionType.LINEAR, FunctionParserService.getFunctionType(exp));
    }

    private void testNonLinearFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isLinear(exp));
        Assert.assertFalse(FunctionParserService.FunctionType.LINEAR.equals(FunctionParserService.getFunctionType(exp)));
    }

    private void testHomographicFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isConstant(exp));
        Assert.assertFalse(FunctionParserService.isLinear(exp));
        Assert.assertFalse(FunctionParserService.isQuadratic(exp));
        Assert.assertTrue(FunctionParserService.isHomographic(exp));

        Assert.assertEquals(FunctionParserService.FunctionType.HOMOGRAPHIC, FunctionParserService.getFunctionType(exp));
    }

    private void testNonHomographicFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isHomographic(exp));
        Assert.assertFalse(FunctionParserService.FunctionType.HOMOGRAPHIC.equals(FunctionParserService.getFunctionType(exp)));
    }

    private void testQuadraticFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isConstant(exp));
        Assert.assertFalse(FunctionParserService.isLinear(exp));
        Assert.assertTrue(FunctionParserService.isQuadratic(exp));
        Assert.assertFalse(FunctionParserService.isHomographic(exp));

        Assert.assertEquals(FunctionParserService.FunctionType.QUADRATIC, FunctionParserService.getFunctionType(exp));
    }

    private void testNonQuadraticFunction(String exp) {
        Assert.assertFalse(FunctionParserService.isQuadratic(exp));
        Assert.assertFalse(FunctionParserService.FunctionType.QUADRATIC.equals(FunctionParserService.getFunctionType(exp)));
    }
}
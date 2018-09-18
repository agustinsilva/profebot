package ar.com.profebot.resolutor;

import org.junit.Test;

import java.util.List;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import ar.com.profebot.resolutor.service.InvalidOptionService;
import ar.com.profebot.resolutor.service.ResolutorService;

public class InvalidOptionServiceTest  extends InvalidOptionService {

    private ResolutorService resolutorService = new ResolutorService();

    @Test
    public void getFirstInvalidOption() throws InvalidExpressionException {
        String expression = "3X-5 = 8";
        Tree tree = (new ParserService()).parseExpression(expression);
        super.getFirstInvalidOptions(tree);
    }

    @Test
    public void general_test_1() throws InvalidExpressionException {
        String expression = "3X = 12";
        List<MultipleChoiceStep> steps = resolutorService.resolveExpressionTestWithoutContext(expression);
    }
    @Test
    public void general_test_2() throws InvalidExpressionException {
        String expression = "3/X=1";
        List<MultipleChoiceStep> steps = resolutorService.resolveExpressionTestWithoutContext(expression);
        steps = steps;
    }

    @Test
    public void general_test_2() throws InvalidExpressionException {
        String expression = "3/X = 1";
        List<MultipleChoiceStep> steps = resolutorService.resolveExpressionTestWithoutContext(expression);
    }

    @Test
    public void getTipoPasajeTerminoPrimerAncestro() {
        // TODO
    }

    @Test
    public void getTipoPasajeTerminoAncestrosDeMismoNivel() {
        // TODO
    }

    @Test
    public void getTipoPasajeTerminoAncestrosDeDistintoNivel() {
        // TODO
    }

    @Test
    public void getSecondInvalidOption() {
        // TODO
    }

    @Test
    public void getSecondInvalidOptionInvalidTypes() throws InvalidExpressionException {
        String expression = "3X-5 = 8+2";
        Tree tree = (new ParserService()).parseExpression(expression);
        super.getSecondInvalidOptions(tree);
    }

    @Test
    public void resolveExpression() {
        // TODO
    }

    @Test
    public void resolveBasicOperation() {
        // TODO
    }

    @Test
    public void resolveDistributive() {
        // TODO
    }

    @Test
    public void resolveAsociative() {
    }

    @Test
    public void resolveBinomialPower() {
        // TODO
    }
}
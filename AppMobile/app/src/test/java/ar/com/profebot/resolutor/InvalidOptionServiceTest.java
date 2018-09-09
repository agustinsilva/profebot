package ar.com.profebot.resolutor;

import org.junit.Test;

import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import ar.com.profebot.resolutor.service.InvalidOptionService;
import ar.com.profebot.resolutor.service.ResolutorService;

import static org.junit.Assert.*;

public class InvalidOptionServiceTest  extends InvalidOptionService{

    @Test
    public void getFirstInvalidOption() throws InvalidExpressionException {
        String expression = "3X-5 = 8";
        Tree tree = (new ParserService()).parseExpression(expression);
        super.getFirstInvalidOption(tree);
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
        super.getSecondInvalidOption(tree);
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
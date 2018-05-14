package ar.com.profebot.services;

import org.junit.Test;

import ar.com.profebot.exceptions.InvalidExpressionException;

public class ScannerServiceTest {

	@Test
    public void parse_ok_1()  throws InvalidExpressionException {
        (new ScannerService()).ValidateExpression("x-3=3-x");
    }
	
	@Test
	public void parse_ok_2()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("3x+1=3-(2-2x)");
	}
	
	@Test
	public void parse_ok_3()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("2-(3-2*(x+1)) = 3x+2*(x-(3+2x))");
	}
	
	@Test
	public void parse_ok_4()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("3x/2 +2x/3 = (1+3x)/2");
	}
	
	@Test
	public void parse_ok_5()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("x-3*(2x+1)/2 = -3*(-(3x+9)/3-2+x)-x/2");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_1()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("x-/3=3-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_2()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("x-+3=3-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_3()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("(x)-(+3)=3-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_4()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("(x)-(3))=3-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_5()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("((x)-(3)=3-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_6()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("(x)-(3)=3=-x");
	}
	
	@Test(expected=InvalidExpressionException.class)
	public void parse_invalid_7()  throws InvalidExpressionException {
		(new ScannerService()).ValidateExpression("*(x)-(3)=3-x");
	}
	
	@Test
	public void get_nodes_1()  throws InvalidExpressionException {
		System.out.println((new ScannerService()).getExpressionList("x-3=3-x"));
	}
	
	@Test
	public void get_nodes_2()  throws InvalidExpressionException {
		System.out.println((new ScannerService()).getExpressionList("3x/2 +2x/3 = (1+3x)/2"));
	}
}

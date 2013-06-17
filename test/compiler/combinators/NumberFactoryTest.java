package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberFactoryTest {

	@Test
	public void testReturnsNumber() {
		NumberFactory fac = new NumberFactory();
		
		Number n = (Number) fac.get("25");
		
		assertNotNull(n);
		
		assertEquals("25", n.getName());
	}
	
	@Test
	public void testBadNumberFails() {
		NumberFactory fac = new NumberFactory();
		
		assertNull(fac.get("abcd"));
		assertNull(fac.get("-35"));
		assertNull(fac.get("56agf"));
		assertNull(fac.get("35.5"));
	}

}

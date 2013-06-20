package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

public class LazyCallFactoryTest {

	@Test
	public void testWorks() {
		LazyCallFactory fac = new LazyCallFactory();
		
		Combinator c = fac.get("@f");
		assertNotNull(c);
		assertEquals("f", c.getName());
		
		c = fac.get("@_1566vdbg587HJK");
		assertNotNull(c);
		assertEquals("_1566vdbg587HJK", c.getName());
	}
	
	@Test
	public void testBadNameReturnsNull() {
		LazyCallFactory fac = new LazyCallFactory();
		
		assertNull(fac.get("cbqskjf"));
		assertNull(fac.get("@0vdv"));
		assertNull(fac.get("@@dnslkn"));
	}

}

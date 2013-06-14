package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

public class VariableFactoryTest {

	@Test
	public void testCreatesVariable() {
		VariableFactory fac = new VariableFactory();
		
		String name = "_ncfskjfh89";
		
		Variable v = (Variable) fac.get("$" + name);
		
		assertNotNull(v);
		assertEquals(name, v.getName());
	}
	
	@Test
	public void testBadName() {
		VariableFactory fac = new VariableFactory();
		
		String name = "8_ncfskjfh89";
		
		Variable v = (Variable) fac.get("$" + name);
		
		assertNull(v);
	}
	
	@Test
	public void testNoDollar() {
		VariableFactory fac = new VariableFactory();
		
		String name = "_ncfskjfh89";
		
		Variable v = (Variable) fac.get(name);
		
		assertNull(v);
	}
	
}

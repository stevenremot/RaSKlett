package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.config.ConfigManager;

public class LambdaFactoryTest {

	@Test
	public void testFindsLambda() {
		LambdaFactory fac = new LambdaFactory();
		
		Lambda l = (Lambda) fac.get("lambda");
		
		assertNotNull(l);
		
		assertEquals(ConfigManager.getInstance().getDefaultAbstractionLevel(), l.getLevel());
		
		String name = "lambda";
		for(int i=1; i <= 4; i++) {
			name += "+";
			
			l = (Lambda) fac.get(name);
			
			assertNotNull(l);
			assertEquals(i, l.getLevel());
		}
	}
	
	@Test
	public void testNotLambdaIsNull() {
		LambdaFactory fac = new LambdaFactory();
		
		Lambda l = (Lambda) fac.get("llambda");
		
		assertNull(l);
		
		l = (Lambda) fac.get("lambda+++++");
		
		assertNull(l);
	}

}

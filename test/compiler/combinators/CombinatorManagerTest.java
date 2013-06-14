package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

public class CombinatorManagerTest {
	public class DummyFactory implements CombinatorFactory {
		public Combinator get(String name) {
			return new DummyCombinator(name);
		}
	}
	
	@Test
	public void testAddFactory() {
		CombinatorManager man = CombinatorManager.getInstance();
		man.addFactory(new DummyFactory());
		
		Combinator c1 = man.get("dah");
		
		assertNotNull(c1);
		
		Combinator c2 = man.get("dah");
		
		assertEquals(c1, c2);
	}

}

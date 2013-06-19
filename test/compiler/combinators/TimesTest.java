package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class TimesTest {

	@Test
	public void testTimesWorks() throws CompilerException {
		Combinator times = new Times(), two = new Number(3);
	
		Node n = new Node(
				NodeFieldFactory.create(times),
				NodeFieldFactory.create(two));
		
		Registry reg = new Registry();
		reg.setNode(n);
		
		assertFalse(times.applyReduction(reg));
		
		Node n2 = new Node(
				NodeFieldFactory.create(n),
				NodeFieldFactory.create(two));
		
		assertTrue(times.applyReduction(reg));
		
		assertEquals(n2, reg.getNode());
		
		assertEquals("I", n2.getFunction().getCombinator().getName());
		assertEquals("9", n2.getArgument().getCombinator().getName());
	}

}

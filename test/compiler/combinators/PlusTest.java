package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class PlusTest {

	@Test
	public void testPlusWorks() throws CompilerException {
		Combinator plus = new Plus(), two = new Number(2);
		
		Node n = new Node(
				NodeFieldFactory.create(plus),
				NodeFieldFactory.create(two));
		
		Registry reg = new Registry();
		reg.setNode(n);
		
		assertFalse(plus.applyReduction(reg));
		
		Node n2 = new Node(
				NodeFieldFactory.create(n),
				NodeFieldFactory.create(two));
		
		assertTrue(plus.applyReduction(reg));
		
		assertEquals(n2, reg.getNode());
		
		assertEquals("I", n2.getFunction().getCombinator().getName());
		assertEquals("4", n2.getArgument().getCombinator().getName());
	}

}

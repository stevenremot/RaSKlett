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
		Combinator plus = new Plus(), a = new DummyCombinator("A");
		
		Node n = new Node(
				NodeFieldFactory.create(plus),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(n);
		
		assertTrue(plus.applyReduction(reg));
		
		assertEquals(n, reg.getNode());
		assertEquals(a, n.getFunction().getCombinator());
		
		Node sb = n.getArgument().getNode();
		
		assertNotNull(sb);
		assertEquals("S", sb.getFunction().getCombinator().getName());
		assertEquals("B", sb.getArgument().getCombinator().getName());
	}

}

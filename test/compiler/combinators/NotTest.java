package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class NotTest {

	@Test
	public void testWorksorrectly() throws CompilerException {
		Combinator not = new Not(), a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(not),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertTrue(not.applyReduction(reg));
		
		Node node0 = reg.getNode();
		
		assertEquals(a, node0.getFunction().getCombinator());
		assertEquals("false", node0.getArgument().getCombinator().getName());
		
		assertEquals(node1, node0.getNextNode());
		
		assertEquals("true", node1.getArgument().getCombinator().getName());
	}

}

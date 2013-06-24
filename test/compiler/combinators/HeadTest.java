package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class HeadTest {

	@Test
	public void testReplacesHeadWithI() throws CompilerException {
		Combinator head = new Head(), vec = new Vector(),
				a = new DummyCombinator("A"), b = new DummyCombinator("B");
		
		Node vecNode1 = new Node(
				NodeFieldFactory.create(vec),
				NodeFieldFactory.create(a));
		
		Node vecNode2 = new Node(
				NodeFieldFactory.create(vecNode1),
				NodeFieldFactory.create(b));
		
		Node root = new Node(
				NodeFieldFactory.create(head),
				NodeFieldFactory.create(vecNode2));
		
		Registry reg = new Registry();
		reg.setNode(root);
		
		assertTrue(head.applyReduction(reg));
		
		assertEquals(vecNode1, reg.getNode());
		assertEquals("vec", vecNode1.getFunction().getCombinator().getName());
		assertEquals(a, vecNode1.getArgument().getCombinator());
        assertEquals(b, vecNode2.getArgument().getCombinator());
        assertEquals("K", vecNode2.getNextNode().getArgument().getCombinator().getName());
	}
}

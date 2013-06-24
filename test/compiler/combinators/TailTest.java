package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class TailTest {

	@Test
	public void testReplacesTailWithI() throws CompilerException {
		Combinator tail = new Tail(), vec = new Vector(),
				a = new DummyCombinator("A"), b = new DummyCombinator("B");
		
		Node vecNode1 = new Node(
				NodeFieldFactory.create(vec),
				NodeFieldFactory.create(a));
		
		Node vecNode2 = new Node(
				NodeFieldFactory.create(vecNode1),
				NodeFieldFactory.create(b));
		
		Node root = new Node(
				NodeFieldFactory.create(tail),
				NodeFieldFactory.create(vecNode2));
		
		Registry reg = new Registry();
		reg.setNode(root);
		
		assertTrue(tail.applyReduction(reg));

        assertEquals(vecNode1, reg.getNode());
        assertEquals("vec", vecNode1.getFunction().getCombinator().getName());
        assertEquals(a, vecNode1.getArgument().getCombinator());
        assertEquals(b, vecNode2.getArgument().getCombinator());

        Node kiNode = vecNode2.getNextNode().getArgument().getNode();

        assertEquals("K", kiNode.getFunction().getCombinator().getName());
        assertEquals("I", kiNode.getArgument().getCombinator().getName());
	}

}

package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class KTest {
	
	@Test
	public void testKReducesCorrectly() {
		Combinator k = new K(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"),
				z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(k), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		@SuppressWarnings("unused")
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(k.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(z,n.getArgument().getCombinator());
		assertNotNull(n.getFunction().getNode());
		assertEquals(x, n.getFunction().getNode().getArgument().getCombinator());
		assertEquals("I", n.getFunction().getNode().getFunction().getCombinator().getName());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}

}

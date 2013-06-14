package compiler.combinators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class CStarTest {
	
	@Test
	public void testCStarReducesCorrectly() {
		Combinator c = new CStar(), x = new DummyCombinator("X"), y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(x));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(c.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(y,n.getFunction().getCombinator());
		assertEquals(x, n.getArgument().getCombinator());
		
		Node nextNode = n.getNextNode();
		
		assertNull(nextNode);
	}
	
	
	@Test
	public void testCStarKeepsNextNode() {
		Combinator c = new CStar(), x = new DummyCombinator("X"), y = new DummyCombinator("Y"), z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertTrue(c.applyReduction(registry));
		
		Node n = registry.getNode();
		
		assertEquals(node3, n.getNextNode());
		
	}
	
	@Test
	public void testCStarStopsWith1Argument() {
		Combinator c = new CStar(), x = new DummyCombinator("X");
		
		Node node1 =  new Node(NodeFieldFactory.create(c), NodeFieldFactory.create(x));
		
		Registry registry = new Registry();
		registry.setNode(node1);
		
		assertFalse(c.applyReduction(registry));

		
	}

}

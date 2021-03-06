package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.combinators.Combinator;
import compiler.combinators.S;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class STest {
	
	@Test
	public void testSReducesCorrectly() throws CompilerException {
		Combinator s = new S(), x = new DummyCombinator("X"),
				y = new DummyCombinator("Y"), z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(s), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		
		Registry r = new Registry();
		r.setNode(node1);
		
		assertTrue(s.applyReduction(r));
		
		Node n = r.getNode();
		
		assertEquals(x, n.getFunction().getCombinator());
		assertEquals(z, n.getArgument().getCombinator());
		assertEquals(node3, n.getNextNode());
		
		Node root = n.getNextNode();
		
		assertNotNull(root);
		
		Node argRoot = root.getArgument().getNode();
		
		assertNotNull(argRoot);
		assertEquals(y, argRoot.getFunction().getCombinator());
		assertEquals(z, argRoot.getArgument().getCombinator());
		assertEquals(null, argRoot.getNextNode());
	}
	
	@Test
	public void testSKeepNextNode() throws CompilerException {
		Combinator s = new S(), x = new DummyCombinator("X"),
				y = new DummyCombinator("Y"), z = new DummyCombinator("Z");
		
		Node node1 = new Node(NodeFieldFactory.create(s), NodeFieldFactory.create(x));
		Node node2 = new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		Node node3 = new Node(NodeFieldFactory.create(node2), NodeFieldFactory.create(z));
		Node node4 = new Node(NodeFieldFactory.create(node3), NodeFieldFactory.create(x));
		
		Registry r = new Registry();
		r.setNode(node1);
		
		assertTrue(s.applyReduction(r));
		
		Node n = r.getNode();
		
		assertEquals(node4, n.getNextNode().getNextNode());
	}
	
	@Test
	public void testSStopsWith2Arguments() throws CompilerException {
		Combinator s = new S(), x = new DummyCombinator("X"),
				y = new DummyCombinator("Y");
		
		Node node1 = new Node(NodeFieldFactory.create(s), NodeFieldFactory.create(x));
		new Node(NodeFieldFactory.create(node1), NodeFieldFactory.create(y));
		
		Registry r = new Registry();
		r.setNode(node1);
		
		assertFalse(s.applyReduction(r));
	}
	
	@Test
	public void testSStopsWith1Argument() throws CompilerException {
		Combinator s = new S(), x = new DummyCombinator("X");
		
		Node node1 = new Node(NodeFieldFactory.create(s), NodeFieldFactory.create(x));
		
		Registry r = new Registry();
		r.setNode(node1);
		
		assertFalse(s.applyReduction(r));
	}

}

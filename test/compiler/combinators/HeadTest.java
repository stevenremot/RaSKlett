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
		
		assertEquals(root, reg.getNode());
		assertEquals("I", root.getFunction().getCombinator().getName());
		assertEquals(a, root.getArgument().getCombinator());
	}
	
	@Test
	public void testReplacesHeadWithoutI() throws CompilerException {
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
		
		Node node2 = new Node(
				NodeFieldFactory.create(root),
				NodeFieldFactory.create(b));
		
		
		Registry reg = new Registry();
		reg.setNode(root);
		
		assertTrue(head.applyReduction(reg));
		
		assertEquals(node2, reg.getNode());
		assertEquals(a, node2.getFunction().getCombinator());
		assertEquals(b, node2.getArgument().getCombinator());
	}
}

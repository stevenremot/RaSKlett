package compiler.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.combinators.Combinator;
import compiler.combinators.DummyCombinator;

public class NodeTest {

	@Test
	public void testCopy() {
		Combinator a = new DummyCombinator("A"), b = new DummyCombinator("B"),
				c = new DummyCombinator("C"), d = new DummyCombinator("D");
		
		
		Node arg1 = new Node(
				NodeFieldFactory.create(b),
				NodeFieldFactory.create(c));
		
		Node root = new Node(
				NodeFieldFactory.create(a),
				NodeFieldFactory.create(arg1));
		
		Node arg2 = new Node(
				NodeFieldFactory.create(root),
				NodeFieldFactory.create(d));
		
		Node rootCopy = root.copy();
		
		assertNotSame(root, rootCopy);
		assertNotSame(root.getArgument(), rootCopy.getArgument());
		
		Node arg1Copy = rootCopy.getArgument().getNode();
		
		assertNotSame(arg1, arg1Copy);
		assertEquals(arg1.getFunction(), arg1Copy.getFunction());
		assertEquals(arg1.getArgument(), arg1Copy.getArgument());
		
		Node arg2Copy = rootCopy.getNextNode();
		
		assertNotSame(arg2, arg2Copy);
		assertNotSame(arg2.getFunction(), arg2Copy.getFunction());
		assertEquals(arg2.getArgument(), arg2Copy.getArgument());
	}

}

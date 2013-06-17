package compiler.combinators;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

public class DefinitionTest {

	@Test
	public void testRegistersDefinition() throws CompilerException {
		Combinator def = new Definition(), name = new Var("$f"),
				a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(def),
				NodeFieldFactory.create(name));
		
		new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		assertFalse(def.applyReduction(reg));
		
		CombinatorManager cm = CombinatorManager.getInstance();
		
		Combinator c = cm.get("f");
		
		assertNotNull(c);
		
		Node n = c.getGraph();
		
		assertNotNull(n);
		
		assertEquals("I", n.getFunction().getCombinator().getName());
		assertEquals(a, n.getArgument().getCombinator());
	}
	
	@Test(expected=CompilerException.class)
	public void testBadVariableNameFails() throws CompilerException {
		Combinator def = new Definition(),
				a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(def),
				NodeFieldFactory.create(a));
		
		new Node(
				NodeFieldFactory.create(node1),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		def.applyReduction(reg);
	}
	
	@Test(expected=CompilerException.class)
	public void testNoExpressionFails() throws CompilerException {
		Combinator def = new Definition(),
				a = new DummyCombinator("A");
		
		Node node1 = new Node(
				NodeFieldFactory.create(def),
				NodeFieldFactory.create(a));
		
		Registry reg = new Registry();
		reg.setNode(node1);
		
		def.applyReduction(reg);
	}
}

package compiler.reducer;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.CompilerException;
import compiler.combinators.Combinator;
import compiler.graph.Node;
import compiler.graph.NodeFieldFactory;

public class SKMachineTest {
	
	/**
	 * Implémentation rapide de K infini pour les tests
	 * @author remot
	 *
	 */
	public class KInf implements Combinator {
		public String getName() {
			return "Kinf";
		}
		
		public Node getGraph() {
			return null;
		}
		
		public boolean applyReduction(Registry registry) {
			Node currentNode = registry.getNode(),
					nextNode = currentNode.getNextNode();
			
			if(nextNode == null) {
				return false;
			}
			
			nextNode.setFunction(NodeFieldFactory.create(this));
			registry.setNode(nextNode);
			return true;
		}
	}
	
	@Test
	public void testSKMachineReducesKInfCorrectly() throws CompilerException {
		Combinator comb = new KInf();
		
		Node root = new Node(NodeFieldFactory.create(comb), NodeFieldFactory.create(comb)),
				then = new Node(NodeFieldFactory.create(root), NodeFieldFactory.create(comb));
		root.setNextNode(then);
		
		SKMachine sk = new SKMachine(root);
		
		assertTrue(sk.step());
		
		Node graph = sk.getReducedGraph();
		
		assertNotNull(graph.getFunction().getCombinator());
		assertEquals("Kinf", graph.getFunction().getCombinator().getName());
		
		assertNotNull(graph.getArgument().getCombinator());
		assertEquals("Kinf", graph.getArgument().getCombinator().getName());
		
		assertNull(graph.getNextNode());
	}
	
	@Test
	public void testSKMachineStopsCorrectlyWithKinf() throws CompilerException {
		Combinator comb = new KInf();
		
		Node root = new Node(NodeFieldFactory.create(comb), NodeFieldFactory.create(comb));
		
		
		SKMachine sk = new SKMachine(root);
		
		assertFalse(sk.step());
		
		assertEquals(root, sk.getReducedGraph());
	}

}

package compiler.reducer;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.combinators.Combinator;
import compiler.graph.Node;
import compiler.combinator.graph.NodeFieldFactory;

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
			return trus;
		}
	}
	
	@Test
	public void testSKMachineReducesKInfCorrectly() {
	}

}

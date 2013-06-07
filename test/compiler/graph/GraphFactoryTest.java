package compiler.graph;

import static org.junit.Assert.*;
import org.junit.Test;

import compiler.combinators.CombinatorManager;

public class GraphFactoryTest {
	
	//@Test
	public void simpleGraphTest(){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","I"};
		Node graph = GraphFactory.create(example);
		
		
		
		assertEquals(cmanager.get("I"),graph.getArgument().getCombinator());
		Node firstNode = graph.getFunction().getNode();
		
		assertEquals(cmanager.get("K"), firstNode.getArgument().getCombinator());
		assertEquals(cmanager.get("S"), firstNode.getFunction().getCombinator());
	}
	
	//@Test
	public void simpleButLongerGraphTest(){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","I","S","K","I"};
		Node graph = GraphFactory.create(example);
		
		assertEquals(cmanager.get("I"),graph.getArgument().getCombinator());
		
		while(graph.getFunction().getCombinator() == null) {
			graph = graph.getFunction().getNode();
		}
		assertEquals(cmanager.get("K"), graph.getArgument().getCombinator());
		assertEquals(cmanager.get("S"), graph.getFunction().getCombinator());
	}
	
	@Test
	public void firstParenthesisTest(){
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		String[] example = {"S","K","(","S","K",")"};
		Node graph = GraphFactory.create(example);
		
		graph.getArgument().getNode();
		assertEquals(null,graph.getArgument().getCombinator());
		assertEquals(cmanager.get("K"), graph.getArgument().getNode().getArgument().getCombinator());
	}

}

package compiler.graph;

import java.util.Arrays;
import compiler.combinators.CombinatorManager;

/**
 * @brief Construit un graphe à partir d'un tableau de String
 * Ce tableau contient des noms de combinateurs ainsi que des parenthèses associatives 
 * 
 * @author kazmiero
 *
 */

public class GraphFactory {

	public static Node create(String[] combinators){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
	
		Node currentNode = null;
		Node previousNode = null;
		
		// initialisation
		NodeField currentFunc = null;
		NodeField currentArg = null;
		
		
		if(!combinators[0].equals('('))
			currentFunc = new CombinatorNodeField(cmanager.get(combinators[0]));
		else
			currentFunc = new NodeNodeField(create(Arrays.copyOfRange(combinators, 1, combinators.length)));
		
		if(!combinators[1].equals('('))
			currentArg = new CombinatorNodeField(cmanager.get(combinators[1]));
		
		else
			currentArg = new NodeNodeField(create(Arrays.copyOfRange(combinators,2,combinators.length)));
		
		currentNode = new Node(currentFunc,currentArg);
		previousNode = currentNode;
		
		
		// boucle
		int index = 2;
		
		while(index < combinators.length){
			
			currentFunc = new NodeNodeField(previousNode);
			
			// parenthèses associatives --> appel récursif
			if(combinators[index].equals('('))
				currentArg = new NodeNodeField(create(Arrays.copyOfRange(combinators,index+1,combinators.length)));				
			
			// cas d'arrêt
			else if(combinators[index].equals(')') || index == combinators.length - 1)
				return previousNode;
			
			else{
				currentArg = new CombinatorNodeField(cmanager.get(combinators[index]));
			}
			
			currentNode = new Node(currentFunc,currentArg);	
			previousNode.setNextNode(currentNode);	
			previousNode = currentNode;
			index++;
		}
		
		return null;
	}
	
}

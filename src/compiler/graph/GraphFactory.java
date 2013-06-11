package compiler.graph;

import java.util.Arrays;
import java.util.Stack;
import java.util.List;
import compiler.combinators.CombinatorManager;

/**
 * @brief Construit un graphe à partir d'un tableau de String
 * Ce tableau contient des noms de combinateurs ainsi que des parenthèses associatives 
 * 
 * @author kazmiero
 *
 */

public class GraphFactory {
	
	public static void parseParenthesis(List<String> combinators){
		
		// initialisation : suppression (récursive) des premières parenthèses
		if(combinators.get(0).equals("(")){
		
			int matchingIndex = getMatchingParenthesis(combinators,0);
			combinators.remove(matchingIndex);
			combinators.remove(0);
			parseParenthesis(combinators);
			return;			
		}
		
		else{
			
			int count = 1;
			
			while(count < combinators.size()){
				
				if(combinators.get(count).equals("(")){
					int matchingIndex = getMatchingParenthesis(combinators,count);
						if(matchingIndex - count <= 2 || combinators.get(count-1).equals("(")){
							combinators.remove(matchingIndex);
							combinators.remove(count);
						}
				}
				
				count++;
			}
		}
	}
	
	public static int getMatchingParenthesis(List<String> list, int index){
		
		int i=index+1;
		int otherParenthesisCount = 0;
		
		while(i < list.size()){
			
			if(list.get(i).equals("("))
				otherParenthesisCount++;	
			else if(list.get(i).equals(")") && otherParenthesisCount>0)
				otherParenthesisCount--;
			else if(list.get(i).equals(")") && otherParenthesisCount==0)
				return i;
			
			i++;
		}
		
		return 0;
	}
	
	public static Node create(Stack<String> combinators, Node previousNode) throws EmptyStackException, BadParenthesisException{
		
		if(combinators.empty())
			throw new EmptyStackException();	
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		// initialisation
		Node currentNode = null;
		NodeField currentFunc = null, currentArg = null;
		
		String currentString = combinators.pop();
		
		// cas d'un seul combinateur C : le noeud (I, C) est créé 
		if(combinators.empty()){
			currentFunc = new CombinatorNodeField(cmanager.get("I"));
			if(currentString.equals("(" )||currentString.equals(")"))
				throw new BadParenthesisException();
			else
				currentArg = new CombinatorNodeField(cmanager.get(currentString));
			currentNode = new Node(currentFunc,currentArg);
			currentNode.setNextNode(previousNode);
			return currentNode;
		}
		
		if(currentString.equals("(" )||currentString.equals(")"))
			throw new BadParenthesisException();
		currentFunc = new CombinatorNodeField(cmanager.get(currentString));
		currentNode = new Node(currentFunc);
		
		currentString = combinators.pop();
		
		if(currentString.equals("("))
			currentArg = new NodeNodeField(create(combinators,currentNode));
		else if(currentString.equals(")"))
			throw new BadParenthesisException();
		else
			currentArg = new CombinatorNodeField(cmanager.get(currentString));
		currentNode.setArgument(currentArg);
		currentNode.setNextNode(previousNode);
		
		// utilisation du passage par référence des String
		previousNode = currentNode;
				
		// boucle
		while(!combinators.empty()){
			
			currentFunc = new NodeNodeField(previousNode);
			currentString = combinators.pop();
			currentNode = new Node(currentFunc);
			
			if(currentString.equals("("))
				currentArg = new NodeNodeField(create(combinators,currentNode));
			else if(currentString.equals(")"))
				return previousNode;
			else
				currentArg = new CombinatorNodeField(cmanager.get(currentString));
			
			currentNode.setArgument(currentArg);
			currentNode.setNextNode(previousNode);
			
			previousNode = currentNode;
			
		}
		
		return currentNode;
	}

	public static Node create(String[] combinators){
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
	
		Node currentNode = null;
		Node previousNode = null;
		
		// initialisation
		NodeField currentFunc = null;
		NodeField currentArg = null;
		
		
		if(!combinators[0].equals("("))
			currentFunc = new CombinatorNodeField(cmanager.get(combinators[0]));
		else
			currentFunc = new NodeNodeField(create(Arrays.copyOfRange(combinators, 1, combinators.length)));
		
		if(!combinators[1].equals(")"))
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
			if(combinators[index].equals("(")){
				currentArg = new NodeNodeField(create(Arrays.copyOfRange(combinators,index+1,combinators.length)));				
				currentNode = new Node(currentFunc,currentArg);
				return currentNode;
			}
			// cas d'arrêt
			else if(combinators[index].equals(")")){
				return previousNode;
			}
				
			
			else{
				currentArg = new CombinatorNodeField(cmanager.get(combinators[index]));
			}
			
			currentNode = new Node(currentFunc,currentArg);	
			previousNode.setNextNode(currentNode);	
			previousNode = currentNode;
			index++;
		}
		
		return previousNode;
	}
	
	
}

package compiler.graph;

import java.util.ArrayList;
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
	
	/**
	 * @brief Petit parser pour supprimer les parenthèses inutiles (sans valeur sémantique)
	 * Prend en argument une liste de String correspondant à une expression à réduire
	 * L'expression est déjà supposée syntaxiquement correcte
	 * Le parser supprime :
	 * 	- les parenthèses en tête d'expression
	 * 	- les parenthèses de la forme ( A ) et ( )
	 * 
	 * @param combinators
	 * la liste combinators doit correspondre à une implémentation des listes qui supporte la méthode remove(int) (exemple ArrayList)
	 */
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
	
	/**
	 * Récupère pour la position "(" index, la position de la parenthèse ")" correspondante, dans la liste de String list, supposée syntaxiquement correcte.
	 * Renvoie 0 en cas d'échec, ce qui correspond à une liste d'entrée syntaxiquement incorrecte
	 * @param list
	 * @param index
	 * @return matchingIndex
	 */
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
	
	/**
	 * @brief création de graphe
	 * 
	 * Prend en argument une pile correspondant à l'expression à évaluer
	 * 
	 * Si l'expression n'est constituée que d'un seul combinateur C, le noeud (func : I, Arg : C) est construit 
	 * 
	 * La contruction de graphe se fait en deux étapes :
	 * L'initialisation :
	 * 	le premier noeud est de la forme (func : C, arg) où C est le premier terme de l'expression.
	 * 	C ne peut pas être une parenthèse après application de parseParenthesis. 
	 * 	L'argument arg est soit un combinateur, soit un noeud correspondant à une expression entre parenthèses.
	 * La boucle :
	 * 	L'expression est ensuite traitée combinateur par combinateur. Pour un combinateur Ci, le noeud (func, arg : Ci) est créé
	 * 	Où func est le noeud qui a été créé juste avant.
	 * 
	 * Gestion des expressions entre parenthèses :
	 * Pour chaque parenthèse ouvrante "(", un appel récursif de create(combinators, currentNode) permet de construire le sous-graphe associé à l'expression entre parenthèses
	 * Le passage de l'argument currentNode sert à la définition des "nextNode" utilisés dans l'algortithme de la machine SK.
	 * La détection de la parenthèse fermante "(" sert de condition d'arrêt à la méthode.
	 * 
	 * @param combinators
	 * @param previousNode
	 * @return currentNode
	 * @throws EmptyStackException
	 * @throws BadParenthesisException
	 */
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
			currentFunc = NodeFieldFactory.create(cmanager.get("I"));
			if(currentString.equals("(" )||currentString.equals(")"))
				throw new BadParenthesisException();
			else
				currentArg = NodeFieldFactory.create(cmanager.get(currentString));
			currentNode = new Node(currentFunc,currentArg);
			currentNode.setNextNode(previousNode);
			return currentNode;
		}
		
		if(currentString.equals("(" )||currentString.equals(")"))
			throw new BadParenthesisException();
		currentFunc = NodeFieldFactory.create(cmanager.get(currentString));
		currentNode = new Node(currentFunc);
		
		currentString = combinators.pop();
		
		if(currentString.equals("("))
			currentArg = NodeFieldFactory.create(create(combinators,currentNode));
		else if(currentString.equals(")"))
			throw new BadParenthesisException();
		else
			currentArg = NodeFieldFactory.create(cmanager.get(currentString));
		currentNode.setArgument(currentArg);
		currentNode.setNextNode(previousNode);
		
		// utilisation du passage par référence des String
		previousNode = currentNode;
				
		// boucle
		while(!combinators.empty()){
			
			currentFunc = NodeFieldFactory.create(previousNode);
			currentString = combinators.pop();
			currentNode = new Node(currentFunc);
			
			if(currentString.equals("("))
				currentArg = NodeFieldFactory.create(create(combinators,currentNode));
			else if(currentString.equals(")"))
				return previousNode;
			else
				currentArg = NodeFieldFactory.create(cmanager.get(currentString));
			
			currentNode.setArgument(currentArg);
			currentNode.setNextNode(previousNode);
			
			previousNode = currentNode;
			
		}
		
		return currentNode;
	}

	/**
	 * @brief méthode haut-niveau de la construction du graphe
	 * Prend en argument l'ArrayList envoyée par le compilateur correspondant à l'expression à réduire
	 * Applique parseParenthesis pour supprimer les parenthèses non sémantiques
	 * Construit la pile puis applique la construction de graphe
	 * 
	 * @param combinators
	 * @return graph
	 * @throws EmptyStackException
	 * @throws BadParenthesisException
	 */
	public static Node create(ArrayList<String> combinators) throws EmptyStackException, BadParenthesisException{
		
		parseParenthesis(combinators);
		
		Stack<String> stack = new Stack<String>();
		
		for(int i=combinators.size()-1; i>=0; i--){
			stack.push(combinators.get(i));
		}
		
		return create(stack, null);
	}
}

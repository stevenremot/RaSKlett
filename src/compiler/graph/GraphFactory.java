package compiler.graph;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import compiler.combinators.CombinatorManager;

/**
 * Construit un graphe à partir d'un tableau de String
 * Ce tableau contient des noms de combinateurs ainsi que des parenthèses associatives 
 * 
 * @author kazmiero
 *
 */

public class GraphFactory {
	
	/**
	 * Petit parser pour supprimer les parenthèses inutiles (sans valeur sémantique)
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
     *
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
	 * création de graphe
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
	 * La détection de la parenthèse fermante "(" sert de condition d'arrêt à la méthode.
	 *
	 * @return currentNode
	 * @throws EmptyStackException
	 * @throws BadParenthesisException
	 * @throws CombinatorNotFoundException 
	 */
	public static Node create(Stack<String> combinators) throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException{
		
		if(combinators.empty())
			throw new EmptyStackException();	
		
		// initialisation
		Node currentNode, previousNode;
		NodeField currentFunc, currentArg;
		
		String currentString = combinators.pop();
		
		// cas d'un seul combinateur C : le noeud (I, C) est créé 
		if(combinators.empty()){
			currentFunc = createCombinatorNodeField("I");
			if(currentString.equals("(" )||currentString.equals(")"))
				throw new BadParenthesisException();
			else
				currentArg = createCombinatorNodeField(currentString);
			currentNode = new Node(currentFunc,currentArg);
			return currentNode;
		}
		
		if(currentString.equals("(" )||currentString.equals(")"))
			throw new BadParenthesisException();
		currentFunc = createCombinatorNodeField(currentString);
		currentNode = new Node(currentFunc);
		
		currentString = combinators.pop();
		
		if(currentString.equals("("))
			currentArg = NodeFieldFactory.create(create(combinators));
		else if(currentString.equals(")"))
			throw new BadParenthesisException();
		else
			currentArg = createCombinatorNodeField(currentString);
		currentNode.setArgument(currentArg);
				
		previousNode = currentNode;
				
		// boucle
		while(!combinators.empty()){
			
			currentFunc = NodeFieldFactory.create(previousNode);
			currentString = combinators.pop();
			currentNode = new Node(currentFunc);
			
			if(currentString.equals("("))
				currentArg = NodeFieldFactory.create(create(combinators));
			else if(currentString.equals(")")){
				previousNode.setNextNode(null);
				return previousNode;
			}
			else
				currentArg = createCombinatorNodeField(currentString);
			
			currentNode.setArgument(currentArg);
			previousNode.setNextNode(currentNode);
			
			previousNode = currentNode;
			
		}
		
		return currentNode;
	}

	
	/**
	 * crée un CombinatorNodeField à partir d'un combinateur trouvé dans le CombinatorManager
	 * Si le combinateur n'est pas dans le manager, une exception est renvoyée
     *
	 * @return CombinatorNodeField
	 * @throws CombinatorNotFoundException
	 */
	public static NodeField createCombinatorNodeField(String name) throws CombinatorNotFoundException{
		
		CombinatorManager cmanager = CombinatorManager.getInstance();
		
		if(cmanager.get(name) == null)
			throw new CombinatorNotFoundException(name);
		
		if(cmanager.get(name).getGraph() != null)
			return NodeFieldFactory.create(cmanager.get(name).getGraph());
		
		return NodeFieldFactory.create(cmanager.get(name));
	}
	
	
	/**
	 * méthode haut-niveau de la construction du graphe
	 * Prend en argument l'ArrayList envoyée par le compilateur correspondant à l'expression à réduire
	 * Applique parseParenthesis pour supprimer les parenthèses non sémantiques
	 * Construit la pile puis applique la construction de graphe
	 *
	 * @return graph
	 * @throws EmptyStackException
	 * @throws BadParenthesisException
	 * @throws CombinatorNotFoundException 
	 */
	public static Node create(ArrayList<String> combinators) throws EmptyStackException, BadParenthesisException, CombinatorNotFoundException{
		
		parseParenthesis(combinators);
		
		Stack<String> stack = new Stack<String>();
		
		for(int i=combinators.size()-1; i>=0; i--){
			stack.push(combinators.get(i));
		}
		
		return create(stack);
	}
}

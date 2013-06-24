package compiler.combinators;

import compiler.graph.Node;
import compiler.graph.NodeField;
import compiler.graph.NodeFieldFactory;
import compiler.reducer.Registry;

/**
 * Combinateur S
 * @author remot
 *
 */
public class S implements Combinator {

	@Override
	public Node getGraph() {
		return null;
	}
	
	/**
	 * Applique le combinateur S au graphe
	 * 
	 * On s'assure d'avoir au moins 3 arguments: S X Y Z
	 * Ensuite, on remplace le 3ème noeud (S X Y) Z par (X Z) (Y Z)
	 */
	@Override
	public boolean applyReduction(Registry registry) {
		Node node1 = registry.getNode();
		
		// On s'assure de l'existence de Y et Z
		Node node2 = node1.getNextNode();
		
		if(node2 == null) {
			return false;
		}
		
		Node node3 = node2.getNextNode();
		
		if(node3 == null) {
			return false;
		}

        NodeField thirdArgCopy = node3.getArgument();

        // On copie le noeud pour éviter les boucles infinies en cas de remaniement du graphe
        // Par exemple, avec S I I (S I I)
        // Si on ne copie pas, on a:
        // I (S I I) (I (S I I)) avec les deux (S I I) étant la même référence
        // A l'itération suivante, on obtient S I I (I (S I I)) avec les deux S I I étant encore la même référence, ce qui amène
        // une boucle infinie à l'affichage.
        if(thirdArgCopy.getNode() != null) {
            thirdArgCopy = NodeFieldFactory.create(thirdArgCopy.getNode().getRoot().copy().getLastNode());
        }
		
		// On crée (X Z) et (Y Z)
		Node funcNode = new Node(node1.getArgument(), node3.getArgument());
		Node argNode = new Node(node2.getArgument(), thirdArgCopy);
		
		// On les assemble
		node3.setFunction(NodeFieldFactory.create(funcNode));
		node3.setArgument(NodeFieldFactory.create(argNode));
		
		registry.setNode(funcNode);
		
		return true;
	}

	@Override
	public String getName() {
		return "S";
	}

}

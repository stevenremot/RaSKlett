package compiler;

import java.io.Reader;

import compiler.parser.Parser;
import compiler.reducer.SKMachine;
import compiler.graph.GraphFactory;
import compiler.graph.GraphSerializer;
import compiler.graph.Node;

/**
 * @brief Classe haut niveau effectuant la compilation du code et renvoyant le résultat
 * @author remot
 *
 */
public class Compiler {
	private boolean finished = false;
	private SKMachine sk;
	
	public Compiler(Reader input) {
		String[] symbols = Parser.parse(input);
		Node graph = GraphFactory.create(symbols);
		sk = new SKMachine(graph);
	}
	
	/**
	 * @return le graphe réduit sous forme de chaîne de caractères
	 */
	public String getResult() {
		return GraphSerializer.serialize(sk.getReducedGraph());
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * @brief effectue une nétape de la réduction
	 * @return false si aucune étape n'a pu être effectué et que la réduction est donc finie, true sinon
	 */
	public boolean reduceStep() {
		finished = !sk.step();
		
		return !finished;
	}
	
	/**
	 * @brief effectue la réduction totale
	 */
	public void reduceAll() {
		while(!isFinished()) {
			reduceStep();
		}
	}

}

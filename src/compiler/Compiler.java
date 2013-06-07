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
	private boolean interrupted = false;
	private SKMachine sk;
	private CompilerCallback callback;
	
	public Compiler(Reader input, CompilerCallback callback) {
		this.callback = callback;
		
		try {
			String[] symbols = Parser.parse(input);
			Node graph = GraphFactory.create(symbols);
			sk = new SKMachine(graph);
		}
		catch(CompilerException e) {
			callback.onFailure(e.getMessage());
		}
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
	
	private void step() {
		finished = !sk.step();
	}
	
	/**
	 * @brief effectue une nétape de la réduction
	 * @return false si aucune étape n'a pu être effectué et que la réduction est donc finie, true sinon
	 */
	public boolean reduceStep() {
		step();
		
		callback.onResult(getResult(), isFinished());
		
		return !finished;
	}
	
	/**
	 * @brief effectue la réduction totale
	 */
	public Thread reduceAll() {
		interrupted = false;
		
		final Compiler t = this;
		
		Runnable r = new Runnable() {
			public void run() {
				while(!t.isFinished() && !t.interrupted) {
					t.step();
				}
				
				t.callback.onResult(getResult(), isFinished());
			}
		};
		
		Thread thread = new Thread(r);
		thread.start();
		
		return thread;
	}
	
	/**
	 * @brief stoppe la réduction
	 */
	public void stopReduction() {
		interrupted = true;
	}

}

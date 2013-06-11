package compiler;

import java.io.Reader;

import compiler.parser.Parser;
import compiler.parser.Instruction;
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
	Instruction[] symbols;
	private SKMachine sk;
	private int currentInstruction = 0;
	private CompilerCallback callback;
	
	public Compiler(Reader input, CompilerCallback callback) {
		this.callback = callback;
		
		try {
			symbols = Parser.parse(input);
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
	
	// Compile l'instruction en graphe
	private boolean registerNextInstruction() {
		if(currentInstruction >= symbols.length) {
			finished = true;
			return false;
		}
		
		Node graph;
		
		try {
			graph = symbols[currentInstruction].getInstruction();
		}
		catch(CompilerException e) {
			callback.onFailure(e.getMessage());
			return false;
		}
		
		if(sk == null) {
			sk = new SKMachine(graph);
		}
		else {
			sk.setGraph(graph);
		}
		
		currentInstruction++;
		return true;
	}

	
	// réduit une étape
	private void step() {
		finished = !sk.step();
	}
	
	// Réduit l'instruction suivante
	private void instruction() {
		interrupted = false;
		
		if(registerNextInstruction()) {
			while(!this.isFinished() && !this.interrupted) {
				this.step();
			}
		}
	}
	
	// réduit TOUT
	private void all() {
		interrupted = false;
		
		while(!this.isFinished() && !this.interrupted) {
			this.instruction();
		}
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
	 * @brief effectue la réduction d'une ligne
	 * 
	 * @return le thread qui exécute la réduction
	 */
	public Thread reduceInstruction() {
		final Compiler t = this;
		
		Runnable r = new Runnable() {
			public void run() {
				t.instruction();
				
				t.callback.onResult(t.getResult(), t.isFinished());
			}
		};
		
		Thread thread = new Thread(r);
		thread.start();
		
		return thread;
	}
	
	/**
	 * @brief effectue la réduction totale
	 * 
	 * @return le thread qui exécute la réduction
	 */
	public Thread reduceAll() {
		interrupted = false;
		
		final Compiler t = this;
		
		Runnable r = new Runnable() {
			public void run() {
				t.all();
				
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

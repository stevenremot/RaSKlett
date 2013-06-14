package compiler;

import java.io.Reader;
import java.util.ArrayList;

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
	ArrayList<Instruction> symbols;
	private SKMachine sk;
	private int currentInstructionIndex = 0;
	private Instruction currentInstruction;
	private CompilerCallback callback;
	
	public Compiler(Reader input, CompilerCallback callback) {
		this.callback = callback;
		
		try {
			symbols = Parser.parse(input);
			registerNextInstruction();
		}
		catch(CompilerException e) {
			callback.onFailure(e);
		}
		
		
	}
	
	/**
	 * @return le graphe réduit sous forme de chaîne de caractères
	 */
	public synchronized String getResult() {
		if(sk == null) {
			return "";
		}
		
		return GraphSerializer.serialize(sk.getReducedGraph());
	}
	
	/**
	 * @brief Envoie le résultat au callback
	 */
	public synchronized void sendResult() {
		callback.onResult(getResult(), currentInstruction.getLine(),
				currentInstruction.getPosition(), isFinished());
	}
	
	public synchronized boolean isFinished() {
		return finished;
	}
	
	public synchronized boolean isInterrupted() {
		return interrupted;
	}
	
	// Compile l'instruction en graphe
	private synchronized boolean registerNextInstruction() {
		if(currentInstructionIndex >= symbols.size()) {
			finished = true;
			return false;
		}
		
		Node graph;
		
		currentInstruction = symbols.get(currentInstructionIndex);
		
		try {
			graph = GraphFactory.create(currentInstruction.getInstruction());
		}
		catch(CompilerException e) {
			e.setLine(currentInstruction.getLine());
			e.setPosition(currentInstruction.getPosition());
			
			callback.onFailure(e);
			return false;
		}
		
		if(sk == null) {
			sk = new SKMachine(graph);
		}
		else {
			sk.setGraph(graph);
		}
		
		currentInstructionIndex++;
		return true;
	}

	
	// réduit une étape
	private synchronized void step() {
		if(!finished || registerNextInstruction()) {
			finished = !sk.step();
		}
	}
	
	// Réduit l'instruction suivante
	private synchronized void instruction() {
		do {
			this.step();
		} while(!this.isFinished() && !this.isInterrupted());
		
		registerNextInstruction();
	}
	
	// réduit TOUT
	private synchronized void all() {
		while(!this.isFinished() && !this.isInterrupted()) {
			this.instruction();
		}
	}
	
	/**
	 * @brief effectue une nétape de la réduction
	 * @return false si aucune étape n'a pu être effectué et que la réduction est donc finie, true sinon
	 */
	public boolean reduceStep() {
		if(!finished || registerNextInstruction()) {
			step();
		
			sendResult();
		}
		
		return !finished;
	}
	
	/**
	 * @brief effectue la réduction d'une ligne
	 * 
	 * @return le thread qui exécute la réduction
	 */
	public Thread reduceInstruction() {
		interrupted = false;
		
		final Compiler t = this;
		
		Runnable r = new Runnable() {
			public void run() {
				synchronized(t) {
					t.instruction();
				
					t.sendResult();
				}
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
				synchronized(t) {
					t.all();
				
					t.sendResult();
				}
			}
		};
		
		Thread thread = new Thread(r);
		thread.start();
		
		return thread;
	}
	
	/**
	 * @brief stoppe la réduction
	 */
	public synchronized void stopReduction() {
		interrupted = true;
	}

}

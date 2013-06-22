package compiler;

import java.io.Reader;
import java.util.ArrayList;

import compiler.parser.Parser;
import compiler.parser.Instruction;
import compiler.reducer.SKMachine;
import compiler.combinators.CombinatorManager;
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
	private boolean lineFinished = false;
	ArrayList<Instruction> symbols;
	private SKMachine sk;
	private int currentInstructionIndex = 0;
	private Instruction currentInstruction;
	private CompilerCallback callback;
	private Thread compilationThread;
	
	public Compiler(Reader input, CompilerCallback callback) {
		CombinatorManager.reset();
		
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
	public String getResult() {
		if(sk == null) {
			return "";
		}
		
		return GraphSerializer.serialize(sk.getReducedGraph());
	}
	
	/**
	 * @brief Envoie le résultat au callback
	 */
	public void sendResult() {
		if(currentInstruction == null) {
			currentInstruction = new Instruction();
		}
		
		callback.onResult(getResult(), currentInstruction.getLine(),
				currentInstruction.getPosition(), isFinished());
	}
	
	public boolean isFinished() {
		return finished;
	}

    public boolean isInterrupted() {
        return(compilationThread != null && compilationThread.isInterrupted());
    }
	
	// Compile l'instruction en graphe
	private boolean registerNextInstruction() {
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
		
		lineFinished = false;
		
		currentInstructionIndex++;
		return true;
	}

	
	// réduit une étape
	private void step() throws CompilerException {
		if(!finished && (!lineFinished || registerNextInstruction())) {
			if(sk != null) {
				lineFinished = !sk.step();
			}
			else {
				throw new CompilerException("Le compilateur a été incorrectement initialisé suite à une erreur");
			}
		}
	}
	
	// Réduit l'instruction suivante
	private void instruction() throws CompilerException {
		do {
			step();
		} while(!isFinished() && !lineFinished && !isInterrupted());
	}
	
	// réduit TOUT
	private void all() throws CompilerException {
		while(!isFinished() && !isInterrupted()) {
			instruction();
			sendResult();
		}
	}
	
	/**
	 * @brief effectue une nétape de la réduction
	 * @return false si aucune étape n'a pu être effectué et que la réduction est donc finie, true sinon
	 */
	public boolean reduceStep() {
		if(!finished) {
			try {
				step();
				finished = lineFinished;
				sendResult();
			}
			catch(CompilerException e) {
				e.setLine(currentInstruction.getLine());
				e.setPosition(currentInstruction.getPosition());
				
				callback.onFailure(e);
			}
		}
		
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
				synchronized(t) {
					try {
						t.instruction();
					}
					catch(CompilerException e) {
						e.setLine(currentInstruction.getLine());
						e.setPosition(currentInstruction.getPosition());
						
						callback.onFailure(e);
					}
				}
			}
		};
		
		compilationThread = new Thread(r);
		compilationThread.start();
		
		return compilationThread;
	}
	
	/**
	 * @brief effectue la réduction totale
	 * 
	 * @return le thread qui exécute la réduction
	 */
	public Thread reduceAll() {
		finished = false;
		final Compiler t = this;
		
		Runnable r = new Runnable() {
			public void run() {
				synchronized(t) {
					try {
						t.all();
					}
					catch(CompilerException e) {
						e.setLine(currentInstruction.getLine());
						e.setPosition(currentInstruction.getPosition());
						
						callback.onFailure(e);
					}
				}
			}
		};
		
		compilationThread = new Thread(r);
		compilationThread.start();
		
		return compilationThread;
	}
	
	/**
	 * @brief stoppe la réduction
	 */
	public void stopReduction() {if(compilationThread != null) {
			compilationThread.interrupt();
		}
	}

}

package compiler;

/**
 * Exception de base du compilateur
 * 
 * @author remot
 *
 */
public class CompilerException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int line;
	private int position;
	
	public CompilerException() {
		message = "";
		line = 0;
		position = 0;
	}

	public CompilerException(String message) {
		super(message);
		this.message = message;
		line = 0;
		position = 0;
	}
	
	public CompilerException(String message, int line, int position) {
		super(message);
		this.message = message;
		this.line = line;
		this.position = position;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

}

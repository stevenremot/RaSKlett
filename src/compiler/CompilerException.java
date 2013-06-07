package compiler;

/**
 * @brief Exception de base du compilateur
 * 
 * @author remot
 *
 */
public class CompilerException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public CompilerException() {
		message = "";
	}

	public CompilerException(String message) {
		super(message);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}

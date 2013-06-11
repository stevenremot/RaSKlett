package graphicInterface;

import java.awt.Color;
import java.util.Enumeration;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Editor extends JTextPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Style defaut = this.getStyle("default");
	private Style error = this.addStyle("error", defaut);
	private Style result = this.addStyle("result", defaut);
	private StyledDocument doc;
	
	public Editor(){
		super();
		StyleConstants.setForeground(defaut, Color.BLACK);
	    StyleConstants.setForeground(error, Color.RED);
	    StyleConstants.setForeground(result, Color.GREEN);
	   doc = (StyledDocument) getDocument();
	}

	public void appendText(String s) throws BadLocationException{
		doc.insertString(doc.getLength(), "\n"+s, defaut);
		
	}
	
	public void insertText(String s, int position) throws BadLocationException {
		doc.insertString(position, "\n"+s, defaut);
		
	}	
	
	public void insertError(String s, int position) throws BadLocationException{
		doc.insertString(position, "\n"+s , error);
		
	}
	
	public void insertResult(String s, int position) throws BadLocationException{
		doc.insertString(position, "\n"+s, result);;
	}
	
	public void disableEdition(){
		setEditable(false);
	}
	
	public void enableEdition(){
		setEditable(true);
	}
	
	public String getCleanedText() {
		String text = "";
		int posInit = getCaretPosition();
		for(int pos = 0; pos < getText().length(); pos++) {
			AttributeSet attr = getCharacterAttributes();
		    Enumeration<?> e = attr.getAttributeNames();
		    while (e.hasMoreElements()) {
		      Object name = e.nextElement();
		      Object value = attr.getAttribute(name);
		      if(value == Color.BLACK) {
					text += getText().charAt(pos);
		      }
		    }
			moveCaretPosition(pos);
		}
		moveCaretPosition(posInit);
		return text;
	}
	

}

package graphicInterface;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Editor extends JTextPane {
	
	private Style defaut = this.getStyle("default");
	private Style error = this.addStyle("error", defaut);
	private Style result = this.addStyle("result", defaut);
	private StyledDocument doc;
	
	public Editor(){
		super();
	    StyleConstants.setForeground(error, Color.RED);
	    StyleConstants.setForeground(result, Color.GREEN);
	    doc = (StyledDocument) getDocument();
	}

	public String getText() {
		return this.getText();
	}

	public void setText(String text) {
		this.setText(text);
	}
	
	public void appendText(String s) throws BadLocationException{
		doc.insertString(doc.getLength(), "\n"+s, defaut);
	}
	
	public void insertText(String s, int position) throws BadLocationException{
		doc.insertString(position, "\n"+s, defaut);
	}	
	
	public void insertError(String s, int position) throws BadLocationException{
		doc.insertString(position, "\n"+s, error);
	}
	
	public void insertResult(String s, int position) throws BadLocationException{
		doc.insertString(position, "\n"+s, result);
	}
	
	public void disableEdition(){
		setEditable(false);
	}
	
	public void enableEdition(){
		setEditable(true);
	}
	

}

package graphicInterface;

import java.awt.Color;
import java.util.prefs.Preferences;
import javax.swing.JTextPane;
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
	private Preferences preferences = Preferences.userRoot();
	
	public Editor(){
		super();
		
	    StyleConstants.setFontSize(defaut,  preferences.getInt("textSize", 11));
	    StyleConstants.setFontSize(error, preferences.getInt("textSize", 11));
	    StyleConstants.setFontSize(result, preferences.getInt("textSize", 11));
		
	    StyleConstants.setFontFamily(defaut,  preferences.get("textFont", "Calibri"));
	    StyleConstants.setFontFamily(error, preferences.get("textFont", "Calibri"));
	    StyleConstants.setFontFamily(result, preferences.get("textFont", "Calibri"));
	    
		StyleConstants.setForeground(defaut, Color.BLACK);
	    StyleConstants.setForeground(error, Color.RED);
	    StyleConstants.setForeground(result, Color.GREEN);

	   doc = (StyledDocument) getDocument();
	   doc.setParagraphAttributes(0, 1, defaut, true);
	   
	}

	public void appendText(String s) throws BadLocationException{
		if(getText().length() > 0)
			doc.insertString(doc.getLength(), "\n"+s, defaut);
		else
			doc.insertString(doc.getLength(), s, defaut);
	}
	
	public void insertText(String s, int position) throws BadLocationException {
		if(getText().length() > 0)
			doc.insertString(position, "\n"+s, defaut);
		else
			doc.insertString(doc.getLength(), s, defaut);
		
	}	
	
	public void insertError(String s, int position) throws BadLocationException{
		String er = "\n"+s;
		if(getText().length() > 0){
			doc.insertString(position, er , error);
			doc.insertString(position + er.length(), "\n", defaut);
		}
		else {
			doc.insertString(doc.getLength(), s, error);
			doc.insertString(doc.getLength(), "\n", defaut);
		}
	}
	
	public void insertResult(String s, int position) throws BadLocationException{
		String res = "\n" + s;
		if(getText().length() > 0) {
			doc.insertString(position, "\n"+s, result);
			doc.insertString(position + res.length(), "\n", defaut);
		}
		else {
			doc.insertString(doc.getLength(), s, result);
			doc.insertString(doc.getLength(), "\n", defaut);
		}

	}
	
	public void disableEdition(){
		setEditable(false);
	}
	
	public void enableEdition(){
		setEditable(true);
	}
	
	/**
	 * @brief Méthode renvoyant les intructions écrites dans l'éditeur sans les erreurs et les résultats.  
	 * @return text : une string ne contenant que les instructions.
	 */
	public String getCleanedText() {
		String text = "";
//		int posInit = getCaretPosition();
		String textInit = getText();
		String[] lines = textInit.split("\n");
		for(int i = 0; i < lines.length; i++) {
			// Chaque message de résultat de compilation débute par ">>>" et chaque erreur par "!!!".
			// Pour obtenir les instructions, on élimine les lignes qui commencent ainsi.
			if(!(lines[i].indexOf(">>>") == 0 || lines[i].indexOf("!!!") == 0))
				if(i == 0)
					text += lines[i];
				else
					text += "\n" + lines[i];
		}
//		for(int pos = 0; pos < getText().length(); pos++) {
//			AttributeSet attr = getCharacterAttributes();
//			if(doc.getForeground(attr) == Color.BLACK)
//				text += getText().charAt(pos);
//		    Enumeration<?> e = attr.getAttributeNames();
//		    while (e.hasMoreElements()) {
//		      Object name = e.nextElement();
//		      Object value = attr.getAttribute(name);
//		      System.out.println(value + " "+ name);
//		      if(value == Color.BLACK) {
//					text += getText().charAt(pos);
//		      }
//		    }
//			moveCaretPosition(pos);
//		}
//		moveCaretPosition(posInit);
		return text;
	}
	
	public void update(){
		
		
		  StyleConstants.setFontSize(defaut,  preferences.getInt("textSize", 11));
		  StyleConstants.setFontSize(error, preferences.getInt("textSize", 11));
		  StyleConstants.setFontSize(result, preferences.getInt("textSize", 11));
		  
		  StyleConstants.setFontFamily(defaut,  preferences.get("textFont", "Calibri"));
		  StyleConstants.setFontFamily(error,  preferences.get("textFont", "Calibri"));
		  StyleConstants.setFontFamily(result,  preferences.get("textFont", "Calibri"));
		  
		  for (int pos = 0; pos < getText().length(); pos++){
			  moveCaretPosition(pos);
			  System.out.println(pos);
			  Color couleur = (doc.getForeground(getCharacterAttributes()));
			  System.out.println(couleur);
			  if (couleur == Color.BLACK) doc.setCharacterAttributes(pos, 1, defaut, true);
			  else if (couleur == Color.RED) doc.setCharacterAttributes(pos, 1, error, true);
			  else doc.setCharacterAttributes(pos, 1, result, true);
		  }
		  
	}
	

}

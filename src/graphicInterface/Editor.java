package graphicInterface;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Classe de léditeur de texte
 *
 * Propose des fonctions spécialisées, comme ajouter un résultat, ou une erreur
 */
public class Editor extends JTextPane {

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
        StyleConstants.setForeground(result, new Color(0, 128, 0));

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
			doc.insertString(position + er.length(), " ", defaut);
		}
		else {
			doc.insertString(doc.getLength(), s, error);
			doc.insertString(doc.getLength(), " ", defaut);
		}
	}
	
	public void insertResult(String s, int position) throws BadLocationException{
		String res = "\n" + s;
		if(getText().length() > 0) {
			doc.insertString(position, "\n"+s, result);
			doc.insertString(position + res.length(), " ", defaut);
		}
		else {
			doc.insertString(doc.getLength(), s, result);
			doc.insertString(doc.getLength(), " ", defaut);
		}

	}

    /**
     * @return une liste avec 3 arguments: le début de l'instruction avant la sélection, la sélection, puis la fin de l'instruction, ou null si pas de sélection.
     */
    public List<String> getSelectedInstruction() {
        String selectionText = getSelectedText().trim(),
                text = getText();

        if(selectionText == null || selectionText.isEmpty()) {
            return null;
        }

        // Pour trouver le début, on cherche le premier point virgule ou le début du texte, et à partir de là on passe les espaces, les commentaires, les résultats et les erreurs
        int selectionStart = getSelectionStart(),
                start = selectionStart;

        while(start > 0 && text.charAt(start) != ';') {
            // Si on sélectionn l'expression d'un résultat, la suite risque de sauter le début sans cette condition
            if(text.substring(start).startsWith(">>>")) {
                start += 3;
                break;
            }
            start--;
        }
        
        if(text.charAt(start) == ';') {
        	start++;
        }
        
        if(start < selectionStart) {
        	boolean isStart = false;
        	while(!isStart) {
        		while(" \n\t".contains(Character.toString(text.charAt(start)))) start++;

        		String subText = text.substring(start);

        		if(subText.startsWith("#") ||
        				subText.startsWith(">>>") ||
        				subText.startsWith("!!!")) {

        			while(start < selectionStart && text.charAt(start) != '\n') start++;

        		}
        		else {
        			isStart = true;
        		}
        	}
        }
        else {
        	start = selectionStart;
        }

        String startText = text.substring(start, selectionStart);

        // Pour trouver la fin, on cherche le prochain point-virgule ou la fin du texte
        int selectionEnd = getSelectionEnd(),
                end = selectionEnd;

        if(selectionText.charAt(selectionText.length() - 1) != ';') {

            while(end < text.length() && text.charAt(end) != ';') end++;

        }

        String endText = text.substring(selectionEnd, end);
        return Arrays.asList(startText.trim().replace('\n', ' ').replace('\t', ' '),
                selectionText,
                endText.trim().replace('\n', ' ').replace('\t', ' '));
    }

    /**
     * @return La ligne de l'instruction sélectionnée, ou -1
     */
    public int getSelectedLineNumber() {
        if(getSelectedText() == null) {
            return -1;
        }

        String t = getText();
        int end = getSelectionEnd();
        int line = 0;

        boolean afterEnd  = false;
        boolean atEndOfInstruction = false;

        for(int c = 0; c <t.length(); c++) {
        	int ch = t.charAt(c);
            
        	if(ch == '\n') {
                line++;
            }
            
            if(c == end) {
            	
            	if(ch == '\n') {
            		line--;
            	}
            	
                if(atEndOfInstruction) {
                    break;
                }
                afterEnd = true;
            }

            if(ch == ';') {
                atEndOfInstruction = true;

                if(afterEnd) {
                    break;
                }
            }
            else if(atEndOfInstruction && !" \n\t".contains("" + (char)ch)) {
                atEndOfInstruction = false;
            }
        }

        return line;
    }
	
	public void disableEdition(){
		setEditable(false);
	}
	
	public void enableEdition(){
		setEditable(true);
	}
	
	/**
	 * Méthode renvoyant les intructions écrites dans l'éditeur sans les erreurs et les résultats.
	 * @return text : une string ne contenant que les instructions.
	 */
	public String getCleanedText() {
		String text = "";

		String textInit = getText();
		String[] lines = textInit.split("\n");
		for(int i = 0; i < lines.length; i++) {
			
			if(!(lines[i].indexOf(">>>") == 0 || lines[i].indexOf("!!!") == 0))
				if(i == 0)
					text += lines[i];
				else
					text += "\n" + lines[i];
		}

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
			  Color couleur = (doc.getForeground(getCharacterAttributes()));
			  if (couleur == Color.BLACK) doc.setCharacterAttributes(pos, 1, defaut, true);
			  else if (couleur == Color.RED) doc.setCharacterAttributes(pos, 1, error, true);
			  else doc.setCharacterAttributes(pos, 1, result, true);
		  }
		  
	}
	

}

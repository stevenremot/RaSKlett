package graphicInterface;

import javax.swing.text.BadLocationException;

/**
 * Classe de test
 * @author landeau
 *
 */
public class Test {
	/**
	 * Méthode main
	 * @param args
	 * @throws BadLocationException 
	 */
	public static void main(String[] args) throws BadLocationException{
		MainWindow fenetre = new MainWindow();
		Editor editor = fenetre.getEditor();
		editor.appendText("Bande de cons !                                  ");
		editor.insertError("J't'emmerde !          ", 20);
		editor.insertResult("1 + 1 = 3", 40);
	}

}

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
		new MainWindow();
		//MainWindow fenetre = new MainWindow();
		//Editor editor = fenetre.getEditor();
//		try {
//			editor.insertText("Bande de cons !;Coucou;",editor.getText().length());
//			editor.insertText("J't'emmerde !;Test;",editor.getText().length());
//			editor.insertText("1 + 1 = 3;", editor.getText().length());
//			editor.insertText("K K X;", editor.getText().length());
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//		}
	}

}

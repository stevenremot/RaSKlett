package graphicInterface;
/**
 * Classe de test
 * @author landeau
 *
 */
public class Test {
	/**
	 * Méthode main
	 * @param args
	 */
	public static void main(String[] args){
		MainWindow fenetre = new MainWindow();
		Editor editor = fenetre.getEditor();
		editor.append("Bande de cons !                    ");
		editor.insertError("J't'emmerde !", 20);
	}

}

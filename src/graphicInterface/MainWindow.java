package graphicInterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;


public class MainWindow extends JFrame{

	private final static String newline = "\n";
	private static final long serialVersionUID = 1L;
	private Editor editor = null;
	private JButton create = null;
	private JButton open = null;
	private JButton save = null;
	private JButton compileAll = null;
	private JButton compileStepByStep = null;
	private JButton nextStep = null;
	private JButton nextLine = null;
	private JButton toEnd = null;
	private JMenuBar menuBar = null;
	private JMenu menu = null;
	private JToolBar toolBar = null;
	private Image createImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons/Create.png"));
	private ArrayList<String> combinators;
	
	/**
	 * Constructeur de la classe Fenetre
	 */
	public MainWindow(){
		combinators = new ArrayList<String>();
		combinators.add("B := S (K S) K");
		combinators.add("W := S S (K I)");
		
		create = new JButton("Create");
		
		
		editor = new Editor();
		menuBar = new JMenuBar();
		menu = new JMenu();
		toolBar = new JToolBar();
		
		toolBar.add(create);
		
		add(toolBar, BorderLayout.NORTH);
        
		editor.setEditable(true);
		JScrollPane panneauTexte = new JScrollPane(editor);

        add(panneauTexte,  BorderLayout.CENTER);
        
        JPanel combinatorPanel = new JPanel(new GridLayout(0, 1));
        Border border = BorderFactory.createTitledBorder("Native combinators");
        combinatorPanel.setBorder(border);  
        CombinatorPanel test = new CombinatorPanel(combinators, "test : ", false);
        CombinatorPanel test2 = new CombinatorPanel(combinators, "test2 : ", true);
        combinatorPanel.add(test2);
        combinatorPanel.add(test);
//        JCheckBox check;
//        for (String s : combinators){
//        	check = new JCheckBox(s);
//        	combinatorPanel.add(check);
//        }
        add(combinatorPanel,  BorderLayout.WEST); 

	    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("RaSKlett");
		pack();
		setVisible(true);
	}
	
	/**
	 * Méthode permettant de rajouter du texte à la zone de texte
	 * @param texte
	 * @throws BadLocationException 
	 */
	public void ecrire(String texte) throws BadLocationException{
		editor.appendText(texte + newline);
	}
	
	public Editor getEditor(){
		return editor;
	}
	
}

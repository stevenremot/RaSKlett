package graphicInterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
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
	private JButton stop = null;
	private JMenuBar menuBar = null;
	private JMenu file = null;
	private JMenu compilation = null;
	private JMenu tools = null;
	private JMenu help = null;
	private JMenuItem iOpen = null;
	private JMenuItem iCreate = null;
	private JMenuItem iSave = null;
	private JMenuItem iCompileAll = null;
	private JMenuItem iCompileStepByStep = null;
	private JMenuItem iNextStep = null;
	private JMenuItem iNextLine = null;
	private JMenuItem iToEnd = null;
	private JMenuItem iInterrupt = null;
	private JMenuItem iCombinators = null;
	private JMenuItem iTools = null;
	private JMenuItem iHelp = null;



	private JToolBar toolBar = null;
	private Image createImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons/Create.png"));
	private ArrayList<String> combinators;
	
	/**
	 * Constructeur de la classe Fenetre
	 */
	public MainWindow(){
		combinators = new ArrayList<String>();
		combinators.add(" B := S (K S) K");
		combinators.add("W := S S (K I)");
		
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
		        "File menu");
		menuBar.add(file);
		
		iOpen = new JMenuItem("open");
		iCreate = new JMenuItem("create");
		iSave = new JMenuItem("save");
		
		file.add(iOpen);
		file.add(iCreate);
		file.add(iSave);		

		compilation = new JMenu("Compilation");
		compilation.setMnemonic(KeyEvent.VK_C);
		compilation.getAccessibleContext().setAccessibleDescription(
		        "Menu with compilation tools");
		menuBar.add(compilation);
		
		iCompileAll = new JMenuItem("compile all");
		iCompileStepByStep = new JMenuItem("compile step by step");
		iNextStep = new JMenuItem("compile next step");
		iNextLine = new JMenuItem("compile next line");
		iToEnd = new JMenuItem("compile to end");

		
		compilation.add(iCompileAll);
		compilation.add(iCompileStepByStep);
		compilation.add(iNextStep);
		compilation.add(iNextLine);
		compilation.add(iToEnd);
		
		tools = new JMenu("Tools");
		tools.setMnemonic(KeyEvent.VK_T);
		tools.getAccessibleContext().setAccessibleDescription(
		        "Tools menu");
		menuBar.add(tools);
		
		iCombinators = new JMenuItem("combinators");
		iTools = new JMenuItem("tools");
		
		tools.add(iCombinators);
		tools.add(iTools);		

		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		help.getAccessibleContext().setAccessibleDescription(
		        "Help menu");
		menuBar.add(help);
		
		iHelp = new JMenuItem("Help");
		
		help.add(iHelp);
		
		setJMenuBar(menuBar);

		
		create = new JButton("Create");
		
		open = new JButton("Open");
		open.addActionListener(new ControleurOpen());
		
		save = new JButton("Save");
		
		compileAll = new JButton("Compile all");
		ControleurCompileAll cCompileAll = new ControleurCompileAll();
		compileAll.addActionListener(cCompileAll);
		
		compileStepByStep = new JButton("Compile step by step");
		ControleurCompileStepByStep cCompileStepByStep = new ControleurCompileStepByStep();
		compileStepByStep.addActionListener(cCompileStepByStep);
		
		nextStep = new JButton("Next step");
		
		nextLine = new JButton("Next line");
		
		toEnd = new JButton("Compile to end");
		
		stop = new JButton("Stop");	
		ControleurStop cStop = new ControleurStop();
		stop.addActionListener(cStop);
		
		
		
		nextStep.setEnabled(false);
		nextLine.setEnabled(false);
		toEnd.setEnabled(false);


		
		editor = new Editor();
		toolBar = new JToolBar();		
		
		toolBar.add(create);
		toolBar.add(open);
		toolBar.add(save);
		toolBar.add(compileAll);
		toolBar.add(compileStepByStep);
		toolBar.add(nextStep);
		toolBar.add(nextLine);
		toolBar.add(toEnd);
		toolBar.add(stop);

		
		add(toolBar, BorderLayout.NORTH);
        
		editor.setEditable(true);
		JScrollPane panneauTexte = new JScrollPane(editor);
        add(panneauTexte,  BorderLayout.CENTER);
        
        JPanel combinatorPanel = new JPanel(new GridLayout(0, 1));
        combinatorPanel.setPreferredSize(new Dimension(150,0));

        Border border = BorderFactory.createTitledBorder("Native combinators");
        combinatorPanel.setBorder(border);    
//        JCheckBox check;
//        for (String s : combinators){
//        	check = new JCheckBox(s);
//        	combinatorPanel.add(check);
//        }
        CombinatorPanel test = new CombinatorPanel(combinators, "test : ", false);
        CombinatorPanel test2 = new CombinatorPanel(combinators, "test2 : ", true);
        combinatorPanel.add(test2);
        combinatorPanel.add(test);
        add(combinatorPanel,  BorderLayout.WEST); 

        setPreferredSize(new Dimension(800, 600));
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
	
	public void startCompilationStepByStep(){
		nextStep.setEnabled(true);
		nextLine.setEnabled(true);
		toEnd.setEnabled(true);
		editor.disableEdition();
		//et le reste
	}
	
	public void startCompilation(){
		editor.disableEdition();
		//et le reste
	}
	
	public void stopCompilation(){
		editor.enableEdition();
		nextStep.setEnabled(false);
		nextLine.setEnabled(false);
		toEnd.setEnabled(false);
	}
	
	public class ControleurCompileAll implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilation();
			
		}
		
	}
	
	public class ControleurCompileStepByStep implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilationStepByStep();
			
		}
		
	}
	
	public class ControleurStop implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			stopCompilation();
			
		}
		
	}
	
	public class ControleurOpen implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    JFileChooser chooser = new JFileChooser();
		    //FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    //    "JPG & GIF Images", "jpg", "gif");
		    //chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            chooser.getSelectedFile().getName());
		       File file = chooser.getSelectedFile();
		       try {
				FileReader reader = new FileReader(file);
				char[] buffer = new char[(int) file.length()];
				reader.read(buffer);
				reader.close();
				String text = new String(buffer);
				editor.setText(text);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    }
		}
		
	}
	
	public class ControleurSave implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
						
		}
		
	}
	
}

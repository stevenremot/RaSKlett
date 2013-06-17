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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;



public class MainWindow extends JFrame{

	private String filename = null;
	private String dir = null;

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
	private JMenuItem iStop = null;
	private JMenuItem iCombinators = null;
	private JMenuItem iPreferences = null;
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
		
		create = new JButton(new ImageIcon("icons/create.png"));
		create.setToolTipText("Open a new file");
		create.addActionListener(new ControleurCreate());

		open = new JButton(new ImageIcon("icons/open.png"));
		open.setToolTipText("Open an existing file");
		ControleurOpen cOpen = new ControleurOpen();

		save = new JButton(new ImageIcon("icons/save.png"));
		save.setToolTipText("Save the current file");
		save.addActionListener(new ControleurSave());

		compileAll = new JButton(new ImageIcon("icons/compile.png"));
		compileAll.setToolTipText("Compile all the code");
		compileAll.addActionListener(new ControleurCompileAll());

		compileStepByStep = new JButton(new ImageIcon("icons/compile_sbs.png"));
		compileStepByStep.setToolTipText("Compile the code step by step");
		compileStepByStep.addActionListener(new ControleurCompileStepByStep());

		nextStep = new JButton(new ImageIcon("icons/next.png"));
		nextStep.setToolTipText("Compile next instruction");

		nextLine = new JButton(new ImageIcon("icons/next_line.png"));
		nextLine.setToolTipText("Compile next line");

		toEnd = new JButton(new ImageIcon("icons/to_end.png"));
		toEnd.setToolTipText("Compile the rest of the code");

		stop = new JButton(new ImageIcon("icons/stop.png"));	
		stop.setToolTipText("Interrupt compilation");
		stop.addActionListener( new ControleurStop());
		

		nextStep.setEnabled(false);
		nextLine.setEnabled(false);
		toEnd.setEnabled(false);
		stop.setEnabled(false);

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

		menuBar = new JMenuBar();

		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
				"File menu");
		menuBar.add(file);

		iOpen = new JMenuItem("Open");
		iOpen.addActionListener(new ControleurOpen());

		iCreate = new JMenuItem("Create");
		iCreate.addActionListener(new ControleurCreate());
		
		iSave = new JMenuItem("Save");
		iSave.addActionListener(new ControleurSave());

		file.add(iOpen);
		file.add(iCreate);
		file.add(iSave);	

		compilation = new JMenu("Compilation");
		compilation.setMnemonic(KeyEvent.VK_C);
		compilation.getAccessibleContext().setAccessibleDescription(
				"Menu with compilation tools");
		menuBar.add(compilation);

		iCompileAll = new JMenuItem("Compile all");
		iCompileAll.addActionListener(new ControleurCompileAll());
		iCompileStepByStep = new JMenuItem("Compile step by step");
		iCompileStepByStep.addActionListener(new ControleurCompileStepByStep());
		iNextStep = new JMenuItem("Compile next step");
		iNextLine = new JMenuItem("Compile next line");
		iToEnd = new JMenuItem("Compile to end");
		iStop = new JMenuItem("Stop compilation");
		iStop.addActionListener(new ControleurStop());

		iNextStep.setEnabled(false);
		iNextLine.setEnabled(false);
		iToEnd.setEnabled(false);
		iStop.setEnabled(false);

		compilation.add(iCompileAll);
		compilation.add(iCompileStepByStep);
		compilation.add(iNextStep);
		compilation.add(iNextLine);
		compilation.add(iToEnd);
		compilation.add(iStop);

		tools = new JMenu("Tools");
		tools.setMnemonic(KeyEvent.VK_T);
		tools.getAccessibleContext().setAccessibleDescription(
				"Tools menu");
		menuBar.add(tools);

		iCombinators = new JMenuItem("Combinators");
		iPreferences = new JMenuItem("Preferences");
		iPreferences.addActionListener(new ControleurPreferences());

		tools.add(iCombinators);
		tools.add(iPreferences);	

		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		help.getAccessibleContext().setAccessibleDescription(
				"Help menu");
		menuBar.add(help);

		iHelp = new JMenuItem("Help");

		help.add(iHelp);

		setJMenuBar(menuBar);

		editor.setEditable(true);
		
		JScrollPane panneauTexte = new JScrollPane(editor);
		add(panneauTexte, BorderLayout.CENTER);
		
		TextLineNumbers tln = new TextLineNumbers(editor);
		panneauTexte.setRowHeaderView( tln );


		JPanel combinatorPanel = new JPanel(new GridLayout(0, 1));
		combinatorPanel.setPreferredSize(new Dimension(150,0));

		Border border = BorderFactory.createTitledBorder("Native combinators");
		combinatorPanel.setBorder(border);    

		CombinatorPanel test = new CombinatorPanel(combinators, "test : ", false);
		CombinatorPanel test2 = new CombinatorPanel(combinators, "test2 : ", true);
		combinatorPanel.add(test2);
		combinatorPanel.add(test);
		add(combinatorPanel, BorderLayout.WEST);

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

	public void compile(String code){
		
	}
	
	public void startCompilationStepByStep(){
		nextStep.setEnabled(true);
		nextLine.setEnabled(true);
		toEnd.setEnabled(true);
		stop.setEnabled(true);
		iNextStep.setEnabled(true);
		iNextLine.setEnabled(true);
		iToEnd.setEnabled(true);
		iStop.setEnabled(true);
		editor.disableEdition();
		//et le reste
	}

	public void startCompilation(){
		editor.disableEdition();
		stop.setEnabled(true);
		iStop.setEnabled(true);
		//compile();
		//et le reste
	}

	public void stopCompilation(){
		editor.enableEdition();
		nextStep.setEnabled(false);
		nextLine.setEnabled(false);
		toEnd.setEnabled(false);
		stop.setEnabled(false);
		iNextStep.setEnabled(false);
		iNextLine.setEnabled(false);
		iToEnd.setEnabled(false);
		iStop.setEnabled(false);
		//compile();
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

	public class ControleurPreferences implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			PreferencesDialog dialog = new PreferencesDialog();
	        dialog.createAndShowGUI();

		}

	}
	/**
	 * @brief Ouvre une boîte de dialogue pour sélectionner le fichier à ouvrir.
	 * @author lagrange
	 *
	 */
	public class ControleurOpen implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"rsk files", "rsk");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				try {
					FileReader reader = new FileReader(file);
					char[] buffer = new char[(int) file.length()];
					reader.read(buffer);
					reader.close();
					String text = new String(buffer);
					editor.setText(text);
					filename = file.getName();
					dir = file.getPath();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public class ControleurCreate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			filename = null;
			dir = null;
			editor.setText(null);
		}

	}

	/**
	 * @brief Listener pour le bouton "save"
	 * Ouvre un dialogue pour choisir le fichier dans lequel écrire si on n'a pas encore sauvegardé.
	 * Si c'est le cas, sauvegarde dans le fichier que l'on a précisé à la première sauvegarde.
	 * @author lagrange
	 *
	 */
	public class ControleurSave implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(filename == null) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"rsk files", "rsk");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();		
					filename = file.getName();
					dir = file.getPath();
				}
			}

			try {
				if(dir != null) {
					FileWriter writer = new FileWriter(new File(dir));
					String text = editor.getCleanedText();
					char[] buffer = text.toCharArray();
					writer.write(buffer);
					writer.close();
					System.out.println("save");}
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}

	public static class PreferencesDialog extends JPanel{
		
		private ImageIcon textPreferences = null;
		private ImageIcon combinatorPreferences = null;
	    
	    private JComboBox sizeList;
	    private JCheckBox lineNumbers;

		
		private Preferences preferences = Preferences.userRoot();


		/**
		 * 
		 */
		private static final long serialVersionUID = 5325511632318062715L;

		public PreferencesDialog() {
			// TODO Auto-generated constructor stub
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JTabbedPane tabbedPane = new JTabbedPane();
			
			textPreferences = new ImageIcon("icons/textPreferences.png");
			combinatorPreferences = new ImageIcon("icons/combinators.png");
			
			
			JPanel textPanel = new JPanel(new GridLayout(0, 1));		
		    
			Object[] numbers = {6, 7, 8, 9, 10, 11, 12, 14, 16, 18};
			sizeList = new JComboBox(numbers);
			sizeList.setEditable(true);
			sizeList.setMaximumSize(new Dimension(100,10));
			
			JPanel sizePanel = new JPanel(new GridLayout(0, 1));
			sizePanel.add(new JLabel("Change text size"));
			sizePanel.add(sizeList);
			textPanel.add(sizePanel);
			
			lineNumbers = new JCheckBox("Add line numbers");
			textPanel.add(lineNumbers);
			
			textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
			
		    tabbedPane.addTab("Text preferences", textPreferences, textPanel,
	                "Editor's text preferences");
		    
		    
			JPanel combinatorsPanel = new JPanel(new GridLayout(1, 1));
			combinatorsPanel.setLayout(new BoxLayout(combinatorsPanel, BoxLayout.PAGE_AXIS));
			JLabel availableCombinators = new JLabel("Available combinators");
			combinatorsPanel.add(availableCombinators);
			
			
			tabbedPane.addTab("Available combinators", combinatorPreferences, combinatorsPanel,
		            "Natively available combinators");
			
	        //Add the tabbed pane to this panel.
	        add(tabbedPane);
	         
	        //The following line enables to use scrolling tabs.
	        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);	
	        tabbedPane.setBorder(new EmptyBorder(3, 3, 3, 3));

	        JButton apply = new JButton("Apply");
	        apply.addActionListener(new ControleurApply());
	        add(apply);
		    	    
		}
	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from
	     * the event dispatch thread.
	     */
	    private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("TabbedPaneDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         
	        //Add content to the window.
	        frame.add(new PreferencesDialog(), BorderLayout.CENTER);
	        frame.setPreferredSize(new Dimension(400,250)) ;
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }
	    /* 
	    public static void main(String[] args) {
	        //Schedule a job for the event dispatch thread:
	        //creating and showing this application's GUI.
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                //Turn off metal's use of bold fonts
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
	        createAndShowGUI();
	            }
	        });
	    }
	    */
		public class ControleurApply implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				preferences.putInt("textSize", (Integer) sizeList.getSelectedItem());
				preferences.put("lineNumbers", new Boolean(lineNumbers.isSelected()).toString());
			}

		}
	}
}

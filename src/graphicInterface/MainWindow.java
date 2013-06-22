package graphicInterface;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import compiler.Compiler;
import compiler.CompilerCallback;
import compiler.CompilerException;
import compiler.config.ConfigManager;

public class MainWindow extends JFrame implements CompilerCallback{

	private Compiler compiler;
	
	private String filename = null;
	private String dir = null;

	private static final long serialVersionUID = 1L;
	private static Editor editor = null;
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
	private JMenuItem iPreferences = null;
	private JMenuItem iHelp = null;
    private boolean inStepByStepCompilation = false;

	private  JScrollPane panneauTexte ;
	private TextLineNumbers tln ;
	private  Preferences preferences = Preferences.userRoot();

	private JToolBar toolBar = null;
	
	private int offset = 0;

	/**
	 * Constructeur de la classe Fenetre
	 */
	public MainWindow(){

		ArrayList<String> combinators = new ArrayList<String>();
		combinators.add("B : B F G X = F ( G X )");
		combinators.add("W : W F X = F X X");
		combinators.add("C : C F X Y = F Y X");
		combinators.add("CStar : CStar X Y = Y X");
		
		ArrayList<String> combinatorsBool = new ArrayList<String>();
		combinatorsBool.add("true");
		combinatorsBool.add("false");
		combinatorsBool.add("and ");
		combinatorsBool.add("or");
		
		ArrayList<String> combinatorsNumbers = new ArrayList<String>();
		combinatorsNumbers.add("+  -  *  /");
		combinatorsNumbers.add("=  !=");
		combinatorsNumbers.add("<  <=  >  > =");
		
		ArrayList<String> combinatorsLists = new ArrayList<String>();
		combinatorsLists.add("vecteur : [X,Y ... Z]");
		combinatorsLists.add("head [X,Y,Z] -> X");
		combinatorsLists.add("tail [X,Y,Z] -> [Y,Z]");


		
		create = new JButton(new ImageIcon("icons/create.png"));
		create.setToolTipText("Créer un nouveau fichier");
		create.addActionListener(new ControleurCreate());

		open = new JButton(new ImageIcon("icons/open.png"));
		open.setToolTipText("Ouvrir un fchier existant");
		open.addActionListener(new ControleurOpen());

		save = new JButton(new ImageIcon("icons/save.png"));
		save.setToolTipText("Sauver le fichier courant");
		save.addActionListener(new ControleurSave());

		compileAll = new JButton(new ImageIcon("icons/compile.png"));
		compileAll.setToolTipText("Compiler le code en entier");
		compileAll.addActionListener(new ControleurCompileAll());

		compileStepByStep = new JButton(new ImageIcon("icons/compile_sbs.png"));
		compileStepByStep.setToolTipText("Compiler le code pas-à-pas");
		compileStepByStep.addActionListener(new ControleurCompileStepByStep());

		nextStep = new JButton(new ImageIcon("icons/next.png"));
		nextStep.setToolTipText("Compiler l'étape");
		nextStep.addActionListener(new ControleurToNextStep());

		nextLine = new JButton(new ImageIcon("icons/next_line.png"));
		nextLine.setToolTipText("Compiler l'instruction");
		nextLine.addActionListener(new ControleurToNextInstruction());

		toEnd = new JButton(new ImageIcon("icons/to_end.png"));
		toEnd.setToolTipText("Compiler le reste du code");
		toEnd.addActionListener(new ControleurCompileAll());

		stop = new JButton(new ImageIcon("icons/stop.png"));	
		stop.setToolTipText("Interrompre la compilation");
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

		file = new JMenu("Fichier");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
				"Fichier");
		menuBar.add(file);

		iOpen = new JMenuItem("Ouvrir");
		iOpen.addActionListener(new ControleurOpen());

		iCreate = new JMenuItem("Créer");
		iCreate.addActionListener(new ControleurCreate());
		
		iSave = new JMenuItem("Sauver");
		iSave.addActionListener(new ControleurSave());

		file.add(iOpen);
		file.add(iCreate);
		file.add(iSave);	

		compilation = new JMenu("Compilation");
		compilation.setMnemonic(KeyEvent.VK_C);
		compilation.getAccessibleContext().setAccessibleDescription(
				"Commandes de compilation");
		menuBar.add(compilation);

		iCompileAll = new JMenuItem("Compiler tout le code");
		iCompileAll.addActionListener(new ControleurCompileAll());
		iCompileStepByStep = new JMenuItem("Compiler le code pas-à-pas");
		iCompileStepByStep.addActionListener(new ControleurCompileStepByStep());
		iNextStep = new JMenuItem("Compiler l'étape");
        iNextStep.addActionListener(new ControleurToNextStep());
		iNextLine = new JMenuItem("Compiler l'instruction");
        iNextLine.addActionListener(new ControleurToNextInstruction());
		iToEnd = new JMenuItem("Compiler le reste du code");
        iToEnd.addActionListener(new ControleurToEnd());
		iStop = new JMenuItem("Interrompre la compilation");
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

		tools = new JMenu("Outils");
		tools.setMnemonic(KeyEvent.VK_O);
		tools.getAccessibleContext().setAccessibleDescription(
				"Menu des outils");
		menuBar.add(tools);

		iPreferences = new JMenuItem("Préférences");
		iPreferences.addActionListener(new ControleurPreferences(this));

		tools.add(iPreferences);	

		help = new JMenu("Aide");
		help.setMnemonic(KeyEvent.VK_A);
		help.getAccessibleContext().setAccessibleDescription(
				"Menu d'aide");
		menuBar.add(help);

		iHelp = new JMenuItem("Manuel");
		iHelp.addActionListener(new ControleurManual(this));

		help.add(iHelp);

		setJMenuBar(menuBar);

		editor.setEditable(true);
		
		panneauTexte = new JScrollPane(editor);
		add(panneauTexte, BorderLayout.CENTER);
		
		tln = new TextLineNumbers(editor);
		if (Boolean.valueOf(preferences.get("lineNumbers", "true"))) panneauTexte.setRowHeaderView( tln );

		JPanel combinatorPanel = new JPanel(new GridLayout(0, 1));
		combinatorPanel.setPreferredSize(new Dimension(170,0));

		Border border = BorderFactory.createTitledBorder("Combinateurs natifs");
		combinatorPanel.setBorder(border);    

		CombinatorPanel basic = new CombinatorPanel(combinators, "Basiques : ", ConfigManager.BASIC_COMBINATORS, false);
		CombinatorPanel bool = new CombinatorPanel(combinatorsBool, "Booléens : ", ConfigManager.BOOLEANS, false);
		CombinatorPanel numbers = new CombinatorPanel(combinatorsNumbers, "Nombres : ", ConfigManager.NUMBERS, false);
		CombinatorPanel lists = new CombinatorPanel(combinatorsLists, "Listes : ", ConfigManager.LISTS, false);
		combinatorPanel.add(basic);
		combinatorPanel.add(bool);
		combinatorPanel.add(numbers);
		combinatorPanel.add(lists);
		add(combinatorPanel, BorderLayout.WEST);

		

		
		setIconImage((new ImageIcon("icons/cheese.png")).getImage());
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("RaSKlett");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public Editor getEditor(){
		return editor;
	}
	
	private void initCompilationEnvironment() {
		offset = 0;
		editor.disableEdition();
		editor.setText(editor.getCleanedText());
		stop.setEnabled(true);
		iStop.setEnabled(true);
		

		String code = editor.getCleanedText();
		Reader reader = new StringReader(code);
		compiler = new Compiler(reader,this);
	}
	
	private void startCompilationStepByStep(){
		initCompilationEnvironment();

        inStepByStepCompilation = true;

		nextStep.setEnabled(true);
		nextLine.setEnabled(true);
		toEnd.setEnabled(true);
		iNextStep.setEnabled(true);
		iNextLine.setEnabled(true);
		iToEnd.setEnabled(true);
		
	}

    private void startCompilation(){
		initCompilationEnvironment();
        inStepByStepCompilation = true;

		enableCompilation(false);
		compiler.reduceAll();

	}

    private void toNextStep() {
		if(compiler != null)
			compiler.reduceStep();

	}

    private void toNextInstruction() {
		compiler.reduceInstruction();
	}

    private void toEnd() {
		compiler.reduceAll();
		editor.enableEdition();
		stop.setEnabled(false);
		iStop.setEnabled(false);
	}

    private void stopCompilation(){
		
		editor.enableEdition();
        inStepByStepCompilation = false;

        if(compiler != null) {
		    compiler.stopReduction();
        }
		
		compiler = null;
		enableCompilation(true);
		

	}

    private void enableCompilation(boolean b)
	{
		if(!b)
			editor.disableEdition();
		else
			editor.enableEdition();

        nextStep.setEnabled(inStepByStepCompilation);
        nextLine.setEnabled(inStepByStepCompilation);
        toEnd.setEnabled(inStepByStepCompilation);
        iNextStep.setEnabled(inStepByStepCompilation);
        iNextLine.setEnabled(inStepByStepCompilation);
        iToEnd.setEnabled(inStepByStepCompilation);

		iCompileAll.setEnabled(b);
		compileAll.setEnabled(b);

		stop.setEnabled(!b);
		iStop.setEnabled(!b);
		
		
	}


    private class ControleurCompileAll implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilation();
		}

	}

    private class ControleurCompileStepByStep implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilationStepByStep();
		}

	}

    private class ControleurToNextStep implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextStep();
		}
	}

    private class ControleurToNextInstruction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextInstruction();
		}
	}

    private class ControleurToEnd implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toEnd();
		}
	}


    private class ControleurStop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			stopCompilation();
		}

	}


    private class ControleurPreferences implements ActionListener {

		private MainWindow window;

		public ControleurPreferences(MainWindow window) {
		this.window = window;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
		PreferencesDialog.createAndShowGUI(window);
		}
	}
	/**
	 * @brief Ouvre une boîte de dialogue pour sélectionner le fichier à  ouvrir.
	 * @author lagrange
	 *
	 */
    private class ControleurOpen implements ActionListener{

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
					try {
						// Pour avoir un code uniquement écrit en noir.
						editor.setText(null);
						editor.insertText(text, 0);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
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

    private class ControleurCreate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			filename = null;
			dir = null;
			try {
				// L'éditeur garde le style du dernier caractère écrit.
				// Pour retrouver le style par défaut, on ajoute une chaîne de caractères à la fin à l'aide de appendText, qui écrit avec le style par défaut (en noir).
				// C'est vraiment très moche, mais ça marche.
				editor.appendText("blabla");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			editor.setText(null);
		}

	}

	/**
	 * @brief Listener pour le bouton "save"
	 * Ouvre un dialogue pour choisir le fichier dans lequel Ã©crire si on n'a pas encore sauvegardÃ©.
	 * Si c'est le cas, sauvegarde dans le fichier que l'on a prÃ©cisÃ© Ã  la premiÃ¨re sauvegarde.
	 * @author lagrange
	 *
	 */
    private class ControleurSave implements ActionListener {

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
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
	}

    private class ControleurManual implements ActionListener {
		private MainWindow parent;
		
		public ControleurManual(MainWindow parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				new DocViewerDialog(parent, "data/manuel.html", "Manuel d'utilisation");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @brief Méthode calculant la position d'une erreur ou d'un rÃ©sultat dans l'Ã©diteur de texte aprÃ¨s la compilation.
	 * @param line la ligne de l'instruction correspondant Ã  l'erreur ou au rÃ©sultat.
	 * @param position la  position de l'instruction au sein d'une ligne d'instructions. 
	 * @return pos la position oÃ¹ l'on va insÃ©rer le texte
	 */
    private int getPos(int line, int position) {
		String s = editor.getText();
		// On transforme le code en un tableau de lignes.
		String[] instructions = s.split("\n");
		int pos = 1 - position;

		// line correspond au numéro de ligne AVANT l'insertion de résultats ou d'erreurs.
		// L'offset prend en compte le décalage occasionné par les insertions précédentes de résultats de compilation.
		// line correspond au numéro de ligne AVANT l'insertion de résultats ou d'erreurs.
		// L'offset prend en compte le décalage occasionné par les insertions précédentes de résultats de compilation.
		for(int i = 0; i < line + offset + 1; i++) {
			pos += instructions[i].length();
		}
		
		return pos+position;
	}

	@Override
	public void onResult(String reducedGraph, int line, int position,
			boolean finished) {
		try {
			if(!finished) {
				int pos = getPos(line ,position);
				int l = line + offset + 1;

				editor.insertResult(">>> Résultat de la ligne "+ l +" : "+reducedGraph,pos + l-2);
				offset++;
			}
			else {
				compileStepByStep.setEnabled(false);
				stopCompilation();
				compileStepByStep.setEnabled(true);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onFailure(CompilerException e) {
		int line = e.getLine();
		int l = line + offset + 1;
		int pos = getPos(line,0);
		try {
			editor.insertError("!!! Erreur : " +e.getMessage(),pos + l - 2);
			offset++;
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
        stopCompilation();
	}
	
	public JScrollPane getPanneauText(){
		return this.panneauTexte;
	}
	
	public TextLineNumbers getLineNumbers(){
		return this.tln;
	}

}

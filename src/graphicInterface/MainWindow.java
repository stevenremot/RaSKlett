package graphicInterface;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import compiler.Compiler;
import compiler.CompilerCallback;
import compiler.CompilerException;
import compiler.config.ConfigManager;

public class MainWindow extends JFrame implements CompilerCallback {

	private Compiler compiler;
	
	private String filename = null;
	private String dir = null;

	private static final long serialVersionUID = 1L;
	private static Editor editor = null;
    private Action createAction, openAction, saveAction, saveAsAction,
        compileAllAction, compileStepByStepAction, compileSelectionAction,
        nextStepAction, nextLineAction, toEndAction,
        stopAction, cleanAction, manualAction, preferencesAction;

    private boolean inStepByStepCompilation = false;

	private  JScrollPane panneauTexte ;
	private TextLineNumbers tln ;
	private  Preferences preferences = Preferences.userRoot();

    // Décalage des lignes lors de l'affichage des résultats
	private int offset = 0;

    // Dans le cas d'une compilation de sélection, contient l'avant, l'après, et la sélection
    private List<String> selectedInstruction = null;
    private int selectedInstructionLine = -1;


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
		combinatorsBool.add("true, false");
		combinatorsBool.add("not : !B ");
		combinatorsBool.add("and : B1 && B2 ");
		combinatorsBool.add("or : B1 || B2");
		
		ArrayList<String> combinatorsNumbers = new ArrayList<String>();
		combinatorsNumbers.add("+  -  *  /");
		combinatorsNumbers.add("=  !=");
		combinatorsNumbers.add("<  <=  >  > =");
		
		ArrayList<String> combinatorsLists = new ArrayList<String>();
		combinatorsLists.add("vecteur : [X,Y ... Z]");
		combinatorsLists.add("head [X,Y,Z] -> X");
		combinatorsLists.add("tail [X,Y,Z] -> [Y,Z]");

        createAction = new ControleurCreate();
        openAction = new ControleurOpen();
        saveAction = new ControleurSave();
        saveAsAction = new ControleurSaveAs();
        compileAllAction = new ControleurCompileAll();
        compileStepByStepAction = new ControleurCompileStepByStep();
        compileSelectionAction = new ControleurCompileSelection();
        nextStepAction = new ControleurToNextStep();
        nextLineAction = new ControleurToNextInstruction();
        toEndAction = new ControleurToEnd();
        stopAction = new ControleurStop();
        cleanAction = new ControleurClean();
        manualAction = new ControleurManual(this);
        preferencesAction = new ControleurPreferences(this);

		nextStepAction.setEnabled(false);
		nextLineAction.setEnabled(false);
		toEndAction.setEnabled(false);
		stopAction.setEnabled(false);

		editor = new Editor();
		JToolBar toolBar = new JToolBar();

		toolBar.add(createAction);
		toolBar.add(openAction);
		toolBar.add(saveAction);
		toolBar.add(saveAsAction);
		toolBar.add(compileAllAction);
        toolBar.add(compileSelectionAction);
		toolBar.add(compileStepByStepAction);
		toolBar.add(nextStepAction);
		toolBar.add(nextLineAction);
		toolBar.add(toEndAction);
		toolBar.add(stopAction);
		toolBar.add(cleanAction);


		add(toolBar, BorderLayout.NORTH);

		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("Fichier");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
				"Fichier");
		menuBar.add(file);

        file.add(createAction);
		file.add(openAction);
		file.add(saveAction);
		file.add(saveAsAction);

		JMenu compilation = new JMenu("Compilation");
		compilation.setMnemonic(KeyEvent.VK_C);
		compilation.getAccessibleContext().setAccessibleDescription(
				"Commandes de compilation");
		menuBar.add(compilation);

		compilation.add(compileAllAction);
		compilation.add(compileStepByStepAction);
        compilation.add(compileSelectionAction);
		compilation.add(nextStepAction);
		compilation.add(nextLineAction);
		compilation.add(toEndAction);
		compilation.add(stopAction);
		compilation.add(cleanAction);

		JMenu tools = new JMenu("Outils");
		tools.setMnemonic(KeyEvent.VK_O);
		tools.getAccessibleContext().setAccessibleDescription(
				"Menu des outils");
		menuBar.add(tools);

		tools.add(preferencesAction);

		JMenu help = new JMenu("Aide");
		help.setMnemonic(KeyEvent.VK_A);
		help.getAccessibleContext().setAccessibleDescription(
				"Menu d'aide");
		menuBar.add(help);

		help.add(manualAction);

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
	
	private void clean() {
		String textCleaned = editor.getCleanedText();
		editor.setText(null);
		try {
			editor.appendText(textCleaned);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

    private void initCompilationEnvironment() {
        initCompilationEnvironment(true);
    }

	private void initCompilationEnvironment(boolean cleanText) {
		offset = 0;
		editor.disableEdition();
        if(cleanText) {
		    editor.setText(editor.getCleanedText());
        }

		stopAction.setEnabled(true);

		

		String code = editor.getCleanedText();
		Reader reader = new StringReader(code);
		compiler = new Compiler(reader,this);
	}
	
	private void startCompilationStepByStep(){
		initCompilationEnvironment();

        inStepByStepCompilation = true;

		nextStepAction.setEnabled(true);
		nextLineAction.setEnabled(true);
		toEndAction.setEnabled(true);
		
	}

    private void startCompilation(){
		initCompilationEnvironment();
        inStepByStepCompilation = true;

        if(compiler != null && !compiler.isInterrupted()) {
		    enableCompilation(false);
		    compiler.reduceAll();
        }

	}

    private void startCompileSelection() {
        selectedInstruction = getEditor().getSelectedInstruction();

        if(selectedInstruction != null) {
            selectedInstructionLine = getEditor().getSelectedLineNumber();
            initCompilationEnvironment(false);
            compiler = new Compiler(new StringReader(selectedInstruction.get(1)), this);

            if(!compiler.isInterrupted()) {
                enableCompilation(false);
                compiler.reduceAll();
            }
        }
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
		stopAction.setEnabled(false);
	}

    private void stopCompilation(){
        selectedInstruction = null;
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

        nextStepAction.setEnabled(inStepByStepCompilation);
        nextLineAction.setEnabled(inStepByStepCompilation);
        toEndAction.setEnabled(inStepByStepCompilation);
        

        compileStepByStepAction.setEnabled(b);

		compileAllAction.setEnabled(b);

		stopAction.setEnabled(!b);
		
		cleanAction.setEnabled(b);
		
		
	}
    
    private class ControleurClean extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurClean() {
            super("Nettoyer", new ImageIcon("icons/clean.png"));
            putValue(SHORT_DESCRIPTION, "Effacer les résultats et les erreurs");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			clean();
		}
    	
    }


    private class ControleurCompileAll extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurCompileAll() {
            super("Compiler tout", new ImageIcon("icons/compile.png"));
            putValue(SHORT_DESCRIPTION, "Compiler le code entier");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilation();
		}

	}

    private class ControleurCompileStepByStep extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurCompileStepByStep() {
            super("Compiler pas-à-pas", new ImageIcon("icons/compile_sbs.png"));
            putValue(SHORT_DESCRIPTION, "Compiler le code pas-à-pas");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			startCompilationStepByStep();
		}

	}

    private class ControleurCompileSelection extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurCompileSelection() {
            super("Compiler la sélection", new ImageIcon("icons/compile_selected.png"));
            putValue(SHORT_DESCRIPTION, "Compiler le code sélectionné");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            startCompileSelection();
        }
    }

    private class ControleurToNextStep extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurToNextStep() {
            super("Compiler l'étape", new ImageIcon("icons/next.png"));
            putValue(SHORT_DESCRIPTION, "Effectuer une étape de réduction");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextStep();
		}
	}

    private class ControleurToNextInstruction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurToNextInstruction() {
            super("Compiler l'instruction", new ImageIcon("icons/next_line.png"));
            putValue(SHORT_DESCRIPTION, "Compier l'instruction courante");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));

        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextInstruction();
		}
	}

    private class ControleurToEnd extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurToEnd() {
            super("Compiler le reste", new ImageIcon("icons/to_end.png"));
            putValue(SHORT_DESCRIPTION, "Compiler le reste du code qui ne l'a pas été");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toEnd();
		}
	}


    private class ControleurStop extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurStop() {
            super("Interrompre", new ImageIcon("icons/stop.png"));
            putValue(SHORT_DESCRIPTION, "Interrompre la compilation");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));

        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			stopCompilation();
		}

	}


    private class ControleurPreferences extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private MainWindow window;

        public ControleurPreferences(MainWindow window) {
            super("Préférences");
            putValue(SHORT_DESCRIPTION, "Modifier les options de RaSKlett");
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);

            this.window = window;
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
		PreferencesDialog.createAndShowGUI(window);
		}
	}
	/**
	 * Ouvre une boîte de dialogue pour sélectionner le fichier à  ouvrir.
	 * 
	 *
	 */
    private class ControleurOpen extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurOpen() {
            super("Ouvrir", new ImageIcon("icons/open.png"));
            putValue(SHORT_DESCRIPTION, "Ouvrir un fichier");
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        }

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

    private class ControleurCreate extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurCreate() {
            super("Nouveau", new ImageIcon("icons/create.png"));
            putValue(SHORT_DESCRIPTION, "Créer un nouveau fichier");
            putValue(MNEMONIC_KEY, KeyEvent.VK_N);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        }
        
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
	 * Listener pour le bouton "save"
	 * Ouvre un dialogue pour choisir le fichier dans lequel Ã©crire si on n'a pas encore sauvegardÃ©.
	 * Si c'est le cas, sauvegarde dans le fichier que l'on a prÃ©cisÃ© Ã  la premiÃ¨re sauvegarde.
	 *
	 */
    private class ControleurSave extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurSave() {
            super("Enregistrer", new ImageIcon("icons/save.png"));
            putValue(SHORT_DESCRIPTION, "Sauver le fichier courant");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        }

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
    
    private class ControleurSaveAs extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ControleurSaveAs() {
            super("Enregistrer sous", new ImageIcon("icons/save_as.png"));
            putValue(SHORT_DESCRIPTION, "Enregistrer le fichier courant sous un autre nom");
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
        }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"rsk files", "rsk");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();		
				filename = file.getName();
				dir = file.getPath();
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
    }

    private class ControleurManual extends AbstractAction {

		private static final long serialVersionUID = 1L;
		private MainWindow parent;

        public ControleurManual(MainWindow parent) {
            super("Manuel d'utilisation");
            putValue(SHORT_DESCRIPTION, "Afficher le manuel d'utilisation");
            putValue(MNEMONIC_KEY, KeyEvent.VK_M);

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
	 * Méthode calculant la position d'une erreur ou d'un résultat dans l'éditeur de texte après la compilation.
	 * @param line la ligne de l'instruction correspondant à l'erreur ou au résultat.
	 * @param position la  position de l'instruction au sein d'une ligne d'instructions. 
	 * @return pos la position où l'on va insÃ©rer le texte
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
        if(!finished) {
		    try {
                int l = line + offset + 1;
                String result;

                if(selectedInstruction != null) {
                    result = selectedInstruction.get(0) + " " + reducedGraph + " " + selectedInstruction.get(2);
                    line = selectedInstructionLine;
                    position = 0;
                }
                else {
                    result = reducedGraph;
                }

                int pos = getPos(line ,position);

				editor.insertResult(">>> "+ result,pos + l-2);
				offset++;
			}
            catch (BadLocationException e) {
                e.printStackTrace();
            }
		}
        else {
            stopCompilation();
        }
	}


	@Override
	public void onFailure(CompilerException e) {
		int line = e.getLine();
        int l = line + offset + 1;

        if(selectedInstruction != null) {
            line = selectedInstructionLine;
        }

        int pos = getPos(line , 0);
		try {
			editor.insertError("!!! " +e.getMessage(),pos + l - 2);
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

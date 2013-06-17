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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import compiler.Compiler;
import compiler.CompilerCallback;
import compiler.CompilerException;


public class MainWindow extends JFrame implements CompilerCallback{

	private Compiler compiler;
	
	private String filename = null;
	private String dir = null;

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
	private ArrayList<String> combinators;
	
	private int offset = 0;

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
		open.addActionListener(new ControleurOpen());

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
		nextStep.addActionListener(new ControleurToNextStep());

		nextLine = new JButton(new ImageIcon("icons/next_line.png"));
		nextLine.setToolTipText("Compile next line");
		nextLine.addActionListener(new ControleurToNextInstruction());

		toEnd = new JButton(new ImageIcon("icons/to_end.png"));
		toEnd.setToolTipText("Compile the rest of the code");
		toEnd.addActionListener(new ControleurCompileAll());

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

	public Editor getEditor(){
		return editor;
	}
	
	public void startCompilationStepByStep(){
		editor.setText(editor.getCleanedText());
		nextStep.setEnabled(true);
		nextLine.setEnabled(true);
		toEnd.setEnabled(true);
		stop.setEnabled(true);
		iNextStep.setEnabled(true);
		iNextLine.setEnabled(true);
		iToEnd.setEnabled(true);
		iStop.setEnabled(true);
		editor.disableEdition();
		
		String code = editor.getCleanedText();
		Reader reader = new StringReader(code);
		compiler = new Compiler(reader,this);
	}

	public void startCompilation(){
		editor.setText(editor.getCleanedText());
		editor.disableEdition();
		stop.setEnabled(true);
		iStop.setEnabled(true);
		
		String code = editor.getCleanedText();
		Reader reader = new StringReader(code);
		compiler = new Compiler(reader,this);
		compiler.reduceAll();
	}
	
	public void toNextStep() {
		enableCompilation(false);
		compiler.reduceStep();
		enableCompilation(true);

	}
	
	public void toNextInstruction() {
		compiler.reduceInstruction();
	}
	
	public void toEnd() {
		compiler.reduceAll();
		editor.enableEdition();
		stop.setEnabled(false);
		iStop.setEnabled(false);
	}

	public void stopCompilation(){
		
		editor.enableEdition();
		
//		nextStep.setEnabled(false);
//		nextLine.setEnabled(false);
//		toEnd.setEnabled(false);
//		stop.setEnabled(false);
//		iNextStep.setEnabled(false);
//		iNextLine.setEnabled(false);
//		iToEnd.setEnabled(false);
//		iStop.setEnabled(false);
		
		compiler = new Compiler(new StringReader(""),this);
		compiler.stopReduction();
		enableCompilation(true);
		

	}
	
	public void enableCompilation(boolean b)
	{
		if(compileStepByStep.isEnabled()) {
			nextStep.setEnabled(b);
			nextLine.setEnabled(b);
			toEnd.setEnabled(b);
			iNextStep.setEnabled(b);
			iNextLine.setEnabled(b);
			iToEnd.setEnabled(b);
		}
		iCompileAll.setEnabled(b);
		compileAll.setEnabled(b);
		stop.setEnabled(!b);
		iStop.setEnabled(!b);
		
		
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
	
	public class ControleurToNextStep implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextStep();
		}
	}
	
	public class ControleurToNextInstruction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toNextInstruction();
		}
	}
	
	public class ControleurToEnd implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			toEnd();
		}
	}


	public class ControleurStop implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			stopCompilation();
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
					//editor.setText(text);
					try {
						editor.setText(null);
						editor.insertText(text, 0);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
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

	public class ControleurCreate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			filename = null;
			dir = null;
			editor.setText(null);
			try {
				String[] s = editor.getCleanedText().split("\n");
				int n = s.length;
				System.out.println("nb lignes : "+n);
				for(int i = 0; i < n ; i ++) {
					if(s[i].length() > 0) {
						int k = s[i].split(";").length;
						System.out.println("nb instruct ligne "+i+ " : "+k);
						
						for(int j = 0; j < k; j++) {
							int pos = getPos(i,j);
							editor.insertResult("Résultat ligne "+i+ " instruction "+j +";",pos +offset -2 +i);
						}
						offset += k;
					}
					
				}		
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
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
	
	public int getPos(int line, int position) {
		String s = editor.getText();
		System.out.println(s);
		String[] instructions = s.split("\n");
		int pos = 1;
		System.out.println(line + offset + position);
		for(int i = 0; i < line + offset + position +1; i++) {
			System.out.println("instructions : "+instructions[i]);
			pos += instructions[i].length();
		}
		System.out.println(pos);
		return pos+position;
	}

	@Override
	public void onResult(String reducedGraph, int line, int position,
			boolean finished) {
		try {
			System.out.println(line+" , "+position);
			int pos = getPos(line ,position);
			System.out.println(reducedGraph);
			editor.insertResult("Résultat de la ligne "+line+" : "+reducedGraph,pos + offset -1 +line);
			offset++;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(CompilerException e) {
		int line = e.getLine();
		try {
			editor.insertError("Erreur ligne "+line+" " +e.getMessage(),line + offset );
			offset++;
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

}

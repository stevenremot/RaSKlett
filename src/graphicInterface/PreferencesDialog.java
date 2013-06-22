package graphicInterface;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;



public class PreferencesDialog extends JPanel implements ActionListener{
	
	private Preferences preferences = Preferences.userRoot();
	
	private static MainWindow parent;
	
	private ImageIcon textPreferences = null;
	private ImageIcon combinatorPreferences = null;
    
    private JComboBox sizeList;
    private JComboBox fontList;
    private JCheckBox lineNumbers;
    
    private static JFrame frame;
	
	
	final String apply = "Apply";
	final String restore = "Restore";
	final String close = "Close";


	/**
	 * 
	 */
	private static final long serialVersionUID = 5325511632318062715L;

	public PreferencesDialog(MainWindow parent) {
		PreferencesDialog.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JTabbedPane tabbedPane = new JTabbedPane();
		
		textPreferences = new ImageIcon("icons/textPreferences.png");
		combinatorPreferences = new ImageIcon("icons/combinators.png");
		
		JPanel textPanel = new JPanel(new GridLayout(0, 1));		
	    
		Object[] numbers = {6, 8, 9, 10, 11, 12, 14, 16, 18, 20};
		sizeList = new JComboBox(numbers);
		sizeList.setEditable(true);
		sizeList.setSelectedItem(preferences.getInt("textSize", 12));
		sizeList.setMaximumSize(new Dimension(100,10));
		
		JPanel sizePanel = new JPanel(new GridLayout(0, 1));
		sizePanel.add(new JLabel("Change text size"));
		sizePanel.add(sizeList);
		textPanel.add(sizePanel);
		
		Object[] fonts = {"Arial", "Calibri", "Comic Sans", "Courier", "Georgia", "Helvetica", "Script", "Times New Roman", "Verdana"};
		fontList = new JComboBox(fonts);
		fontList.setEditable(true);
		fontList.setSelectedItem(preferences.get("textFont", "Calibri"));
		fontList.setMaximumSize(new Dimension(100,10));
		
		JPanel fontPanel = new JPanel(new GridLayout(0, 1));
		fontPanel.add(new JLabel("Change text font"));
		fontPanel.add(fontList);
		textPanel.add(fontPanel);
		
		lineNumbers = new JCheckBox("Add line numbers");
		if (Boolean.valueOf(preferences.get("lineNumbers", "true"))) lineNumbers.setSelected(true);
		textPanel.add(lineNumbers);
		
		textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
	    tabbedPane.addTab("Text preferences", textPreferences, textPanel,
                "Editor's text preferences");
	    
	    /*
		JPanel combinatorsPanel = new JPanel(new GridLayout(1, 1));
		combinatorsPanel.setLayout(new BoxLayout(combinatorsPanel, BoxLayout.PAGE_AXIS));
		JLabel availableCombinators = new JLabel("Available combinators");
		combinatorsPanel.add(availableCombinators);
		
		
		tabbedPane.addTab("Available combinators", combinatorPreferences, combinatorsPanel,
	            "Natively available combinators");
		*/
        //Add the tabbed pane to this panel.
        add(tabbedPane);
         
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);	
        tabbedPane.setBorder(new EmptyBorder(3, 3, 3, 3));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));

        JButton apply = new JButton("Apply");
        apply.setActionCommand("apply");
        apply.addActionListener(this);
        buttonsPanel.add(apply);
        
        JButton reset = new JButton("Reset");
        reset.setActionCommand("reset");
        reset.addActionListener(this);
        buttonsPanel.add(reset);
        
        JButton ok = new JButton("OK");
        ok.setActionCommand("close");
        ok.addActionListener(this);
        buttonsPanel.add(ok);
        
        
        add(buttonsPanel);
	    	    
	}
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
	 public static void createAndShowGUI(MainWindow parent) {
	        //Create and set up the window.
	        frame = new JFrame("TabbedPaneDemo");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        
			frame.setIconImage((new ImageIcon("icons/cheese.png")).getImage());
	         
	        //Add content to the window.
	        frame.add(new PreferencesDialog(parent), BorderLayout.CENTER);
	        frame.setPreferredSize(new Dimension(400,300)) ;
	        //Display the window.
	        frame.pack();
			frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	    }
	    

	public void actionPerformed(ActionEvent e) {
		if("apply".equals(e.getActionCommand())) {
	        preferences.putInt("textSize", (Integer) sizeList.getSelectedItem());
			preferences.put("textFont", (String) fontList.getSelectedItem());
	        preferences.put("lineNumbers", new Boolean(lineNumbers.isSelected()).toString());
			if (lineNumbers.isSelected())  parent.getPanneauText().setRowHeaderView( parent.getLineNumbers() ) ;
			else parent.getPanneauText().setRowHeaderView( null ) ;
			parent.getEditor().update();
			
		} else if ("close".equals(e.getActionCommand())) {
		      frame.dispose();
	    }
		else if ("reset".equals(e.getActionCommand())) {
	        preferences.putInt("textSize", 12);
	        sizeList.setSelectedItem(12);
			preferences.put("textFont", "Calibri");
	        fontList.setSelectedItem("Calibri");
	        preferences.put("lineNumbers", "true");
	        lineNumbers.setSelected(true);
			parent.getEditor().update();
		}
	}
}

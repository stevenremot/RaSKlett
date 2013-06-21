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
	
	private ImageIcon textPreferences = null;
	private ImageIcon combinatorPreferences = null;
    
    private JComboBox<Integer> sizeList;
    private JCheckBox lineNumbers;
    
    private static JFrame frame;
	
	private Preferences preferences = Preferences.userRoot();
	
	final String apply = "Apply";
	final String restore = "Restore";
	final String close = "Close";
	
	private MainWindow parent;


	/**
	 * 
	 */
	private static final long serialVersionUID = 5325511632318062715L;

	public PreferencesDialog(MainWindow parent) {
		this.parent = parent;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JTabbedPane tabbedPane = new JTabbedPane();
		
		textPreferences = new ImageIcon("icons/textPreferences.png");
		combinatorPreferences = new ImageIcon("icons/combinators.png");
		
		
		JPanel textPanel = new JPanel(new GridLayout(0, 1));		
	    
		Integer[] numbers = {12, 6, 7, 8, 9, 10, 11, 12, 14, 16, 18};
		sizeList = new JComboBox<Integer>(numbers);
		sizeList.setEditable(true);
		sizeList.setSelectedItem(12);
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
        apply.setActionCommand("apply");
        apply.addActionListener(this);
        add(apply);
        
        JButton ok = new JButton("OK");
        ok.setActionCommand("close");
        ok.addActionListener(this);
        add(ok);
	    	    
	}
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    public static void createAndShowGUI(MainWindow parent) {
        //Create and set up the window.
        frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        //Add content to the window.
        frame.add(new PreferencesDialog(parent), BorderLayout.CENTER);
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
			parent.getEditor().update();
		}

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if("apply".equals(e.getActionCommand())) {
	        System.out.println("apply button selected");
	        preferences.putInt("textSize", (Integer) sizeList.getSelectedItem());
			preferences.put("lineNumbers", new Boolean(lineNumbers.isSelected()).toString());
			parent.getEditor().update();
		    } else if ("close".equals(e.getActionCommand())) {
		      System.out.println("upgrade button selected");
		      frame.dispose();
		    }
	}
}
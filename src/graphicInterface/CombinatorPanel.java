package graphicInterface;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class CombinatorPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JButton button;
	private boolean enabled;
	
	public CombinatorPanel(ArrayList<String> combinators, String category, boolean enabled) {
		super(new GridLayout(0,1));
		text = new JTextArea();
		this.enabled = enabled;
		Border border = BorderFactory.createTitledBorder(category);
        setBorder(border);  
		for(String c: combinators)
		{
			text.append("\n" + c);
		}
		button = new JButton();
		button.addActionListener(new ButtonListener());
		if(!this.enabled) {
			text.setEnabled(false);
			button.setText("Activer");
		}
		else
			button.setText("Désactiver");
		add(text);
		add(button);

	}
	
	public void toggleCombinators(boolean b) {
		System.out.println("toggle combinators");
	}
	
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toggleCombinators(enabled);
			enabled = !enabled;
			text.setEnabled(enabled);
			if(enabled) {
				button.setText("Désactiver");
			}
			else
				button.setText("Activer");
		}
		
	}
	

}

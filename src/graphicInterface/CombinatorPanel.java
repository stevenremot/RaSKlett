package graphicInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import compiler.config.ConfigManager;

/**
 * Classe créant un composant contenant un champ de texte affichant un groupe de combinateurs et un bouton permettant de les activer ou non
 */
public class CombinatorPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JButton button;
	private boolean enabled;
	private ConfigManager manager;
	private String feature;
	
	/**
	 * Le constructeur de CombinatorPanel
	 * @param combinators liste des combinateurs qui seront natifs ou non à l'aide de ce composant
	 * @param category catégorie des combinateurs (ex : opérateurs logiques)
	 * @param enabled les combinateurs sont natifs par défaut si enabled vaut true
	 */
	public CombinatorPanel(ArrayList<String> combinators, String category, String feature, boolean enabled) {
		super();
		
		this.feature = feature;
		text = new JTextArea();
		manager = ConfigManager.getInstance();
		this.enabled = enabled;
		Border border = BorderFactory.createTitledBorder(category);
        setBorder(border);  
        
		for(String c: combinators) {
			//pour ne pas avoir un saut de ligne dès le début
			if(combinators.indexOf(c) != 0)
				text.append("\n" + c);
			else
				text.append(c);
		}
		
		button = new JButton();
		button.addActionListener(new ButtonListener());
		
		if(!this.enabled) {
			text.setEnabled(false);
			button.setText("Enable");
		}
		else
			button.setText("Disable");
		
		setLayout(new BorderLayout());
		add(text,BorderLayout.CENTER);
		add(button,BorderLayout.SOUTH);
		
		manager.toggle(feature, enabled);
	}
	
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			enabled = !enabled;
			manager.toggle(feature, enabled);
			text.setEnabled(enabled);
			if(enabled) {
				button.setText("Désactiver");
			}
			else
				button.setText("Activer");
		}
	}
}

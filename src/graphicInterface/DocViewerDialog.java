package graphicInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

/**
 * Dialogue permettant d'afficher un document HTML
 * @author remot
 *
 */
public class DocViewerDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public DocViewerDialog(MainWindow owner, String doc, String title) throws IOException {
		super(owner, title);
		
		JTextPane view = new JTextPane();
		view.setEnabled(false);
		view.setContentType("text/html");
		
		BufferedReader r = new BufferedReader(new FileReader(doc));
		String content = "";
		int c;
		
		while((c = r.read()) > 0) {
			content += (char)c;
		}
		
		r.close();
		
		view.setText(content);

		JScrollPane scroll = new JScrollPane(view);
		add(scroll);
		pack();
		
		setSize(700, 500);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}

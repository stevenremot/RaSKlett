package graphicInterface;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

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
        view.setDisabledTextColor(Color.black);
        view.setCaretPosition(0);
		
		BufferedReader r = new BufferedReader(new FileReader(doc));
		String content = "";
		int c;
		
		while((c = r.read()) > 0) {
			content += (char)c;
		}
		
		r.close();
		
		view.setText(content);

		final JScrollPane scroll = new JScrollPane(view);
		add(scroll);
		pack();
		
		setSize(700, 500);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scroll.getVerticalScrollBar().setValue(0);
            }
        });
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}

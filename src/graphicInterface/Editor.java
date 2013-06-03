package graphicInterface;

import java.awt.Color;

import javax.swing.JTextArea;

public class Editor extends JTextArea {
	
	public Editor(){
		super();
	}

	public String getText() {
		return this.getText();
	}

	public void setText(String text) {
		this.setText(text);
	}
	
	public void insertError(String s, int position){
		setForeground(Color.red);
		insert('\n' + s, position);
		//setForeground(Color.black);
	}
	
	public void insertResult(String s, int position){
		setForeground(Color.green);
		insert('\n' + s, position);
		setForeground(Color.black);
	}
	
	

}

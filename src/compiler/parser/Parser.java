package compiler.parser;
/**
 * @author qchen
 * For the call of lexicalAnalyser and SemanticalAnalyser
 * 03/06/2013
 */
public class Parser {
	public String resultString;
	/**
	 * constructor
	 */
	public Parser(){		
	}
	
	/**
	 * to be a interface for call lexicalAnalyser and SemanticalAnalyser
	 */
	public void driverParser() {
		lexicalAnalyser lA = new lexicalAnalyser();
		//String fileName = textField.getText();
		String fileName = "./test.java"; 

		//lA.openCFile(fileName);
		lA.readRowByRow();
		resultString = lA.result;
		//textArea.setText(resultString);
		System.out.println(resultString);

	}
	public static void main(String[] args) {
		Parser myparser=new Parser();
		myparser.driverParser();
	}
}

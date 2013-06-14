package compiler.parser;
import java.util.ArrayList;

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
		SemanticalAnalyser sA=new SemanticalAnalyser();
		//String fileName = textField.getText();

		//lA.openCFile(fileName);
		lA.readRowByRow();
		int line=0;
		ArrayList<String> tempArrayList=new ArrayList<String>();

		for(int i=0;i<lA.resInstruArrayList.size();i++)
		{
			System.out.println("============================START===========================================");

			
			line=lA.resInstruArrayList.get(i).getLine();
			tempArrayList=lA.resInstruArrayList.get(i).getInstruction();
			System.out.println("LineNumber:   "+line+"   Content:   "+tempArrayList);

			sA.semanticAnalysis(lA.resInstruArrayList.get(i));

			for (int j=0;j<sA.myresult.size();j++) {				
				System.out.println(" ("+sA.myresult.get(j)+" )");
			}
			
			
			
			System.out.println("------------------------------END--------------------------------------------");
			System.out.println("");




			
		}
		//textArea.setText(resultString);

	}
	public static void main(String[] args) {
		Parser myparser=new Parser();
		myparser.driverParser();
	}
}

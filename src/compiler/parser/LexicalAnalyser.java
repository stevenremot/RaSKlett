package compiler.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import compiler.CompilerException;

/**
 * @brief Analyse lexicale du code
 * 
 * Sépare le code en instructions, caractères par caractères
 * 
 * @author Qianqian CHEN parser lexicalAnalyser 31/05/2013
 */
public class LexicalAnalyser {
	private ArrayList<Instruction> resInstruArrayList = new ArrayList<Instruction>();
	private Instruction currentInstruction;
	private Reader input;
	private int lineCount;

	public LexicalAnalyser(Reader input) throws CompilerException {
		this.input = input;
		readRowByRow();
	}
	
	public ArrayList<Instruction> getSymbols() {
		return resInstruArrayList;
	}
	
	private void registerInstruction() {
		if(!currentInstruction.getInstruction().isEmpty()) {
			resInstruArrayList.add(currentInstruction);
		}
	}
	
	private void readInstructionByInstruction(String row) {
		
		for(int i=0; i < row.length(); i++) {
			char c = row.charAt(i);
			
			if(c == ';') {
				i++;
				
				Instruction next = new Instruction();
				next.setPosition(currentInstruction.getPosition() + 1);
				next.setLine(lineCount);
				
				if(i < row.length()) {
					char nc = row.charAt(i);
					if(nc == ';') {
						currentInstruction.addInstruction(";");
						currentInstruction.addInstruction(";");
					}
					else {
						next.addInstruction(Character.toString(nc));
					}
				}
				
				registerInstruction();
				
				currentInstruction = next;
			}
			else {
				currentInstruction.addInstruction(Character.toString(c));
			}
		}
	}
	
	private void readRowByRow() throws CompilerException {
		currentInstruction = new Instruction();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(input);
			
			String targetString;
			lineCount = 0;
			
			while((targetString = br.readLine()) != null) {
				String line = targetString.trim();
				
				if(line.length() > 0) {
					readInstructionByInstruction(line);
					
					if(currentInstruction.getInstruction().isEmpty()) {
						currentInstruction.setLine(currentInstruction.getLine() + 1);
						currentInstruction.setPosition(0);
					}
				}
				
				lineCount++;
			}
			
			registerInstruction();

		} catch (IOException e) {
			throw new CompilerException("Cannot read buffer", lineCount, 0);
		}
		
		try {
			br.close();
		} catch (IOException e) {
			throw new CompilerException("Could not close buffer", lineCount, 0);
		}

	}
	public ArrayList<Instruction> getResInstruArrayList() {
		return resInstruArrayList;
	}

	public void setResInstruArrayList(ArrayList<Instruction> resInstruArrayList) {
		this.resInstruArrayList = resInstruArrayList;
	}

}
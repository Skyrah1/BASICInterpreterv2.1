/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

import java.util.Hashtable;
import javax.swing.JTextArea;

/**
 *
 * @author ASUS
 */
public class Interpreter extends Program {

    Parser parser;
    SyntaxChecker checker;
    Executor executor;
    
    //Shared variables (only shared with SyntaxChecker and Executor
    static Hashtable varStorage;
    static int currentLineIndex;

    public Interpreter() {
        parser = new Parser();
        checker = new SyntaxChecker();
        executor = new Executor();
        varStorage = new Hashtable();
    }

    //Accepts the pre-processed text and interprets it using syntax checker and executor
    public void interpretCode(String[] codeArray, JTextArea outputArea) {
        String[] currentLine;
        String[] lineNumbers = new String[codeArray.length];
        currentLineIndex = 0;
        boolean validCode = true;
        boolean notEnd = true;
        //Fill lineNumbers with all the line numbers
        for (int i = 0; i < codeArray.length; i++) {
            currentLine = Parser.lineParser(codeArray[i]);
            lineNumbers[i] = currentLine[0];
        }

        while (currentLineIndex < codeArray.length && validCode && notEnd) {

            currentLine = Parser.lineParser(codeArray[currentLineIndex]);
            //Program.printArrayAsSingleString(currentLine);
            if (currentLine.equals("") == false) {
                validCode = checker.checkSyntax(currentLine, lineNumbers);
                notEnd = executor.executeLine(currentLine, lineNumbers, validCode, outputArea);
            }
            currentLineIndex++;
        }
        //Gets rid of variables after code has finished running
        varStorage.clear();
    }

}

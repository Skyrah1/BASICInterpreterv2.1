/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author ASUS
 */
public class BASICInterpreterv21 extends JFrame {

    private JTextArea codeArea;
    private JTextArea outputArea;
    private JButton button;

    PreParser preParser;
    Interpreter interpreter;
    //Parser parser;
    //SyntaxChecker checker;
    //Executor executor;

    private static int width = 2048;
    private static int height = 1920;

    /**
     * @param args the command line arguments
     */
    public BASICInterpreterv21() {
        super("Interpreter");
        // so Java will end when GUI is closed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        preParser = new PreParser();
        interpreter = new Interpreter();
        //parser = new Parser();
        //checker = new SyntaxChecker();
        //executor = new Executor();
        JPanel window = new JPanel();
        window.setLayout(new GridLayout(3, 1));

        codeArea = new JTextArea();
        codeArea.setFont(new Font("Times Roman", Font.PLAIN, 30));

        window.add(codeArea);
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Times Roman", Font.PLAIN, 24));
        outputArea.setBorder(BorderFactory.createLineBorder(Color.black));
        
        window.add(outputArea);

        button = new JButton("Run Code");
        button.setFont(new Font("Times Roman", Font.PLAIN, 72));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO code that runs when button is pushed.
                
                outputArea.setText("");
                
                String[] codeArray = preParser.preParseCode(codeArea);
                interpreter.interpretCode(codeArray, outputArea);
                
                /*String[] currentLine;
                int currentLineNum = 0;
                boolean validCode = true;
                
                while (currentLineNum < codeArray.length && validCode) {
                    
                    currentLine = Parser.lineParser(codeArray[currentLineNum]);
                    //Program.printArrayAsSingleString(currentLine);
                    if (currentLine.equals("") == false){
                        validCode = checker.checkSyntax(currentLine);
                        executor.executeLine(currentLine, validCode, outputArea);
                    }
                    currentLineNum++;
                }
                //Gets rid of variables after code has finished running
                executor.varStorage.clear();
*/
            }
        });
        
        window.add(button);

        add(window);
    }

    private static void createWindow() {
        JFrame mainFrame = new BASICInterpreterv21();
        mainFrame.setPreferredSize(new Dimension(width, height));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        createWindow();

    }

}

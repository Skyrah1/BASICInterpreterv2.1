/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

import javax.swing.JTextArea;

/**
 *
 * @author ASUS
 */
public class SyntaxChecker extends Program {
    
    public SyntaxChecker() {

    }
    
    //Checks for the presence of a line number and a recognised keyword
    public boolean checkSyntax(String[] line, String[] lineNumbers) {
        boolean validCode = true;
        int lineNum = 0;
        String keyword = "";

        if (line.length != 0 && line[0].equals("") == false) {
            try {
                lineNum = Integer.parseInt(line[0]);
            } catch (NumberFormatException e) {
                validCode = false;
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            try {
                keyword = line[1];
                validCode = checkKeyword(keyword, line, lineNumbers);
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }

        return validCode;
    }

    public boolean checkKeyword(String keyword, String[] line, String[] lineNumbers) {
        boolean validCode = true;
        switch (keyword) {
            case "LET":
                if (line[4].contains("\"")) {
                    try {
                        if (line[3].equals("=") == false) {
                            validCode = false;
                        }
                        //If a string is inputted and there is no end of the string
                        if (line[line.length - 1].charAt(line[line.length - 1].length() - 1) != '"') {
                            validCode = false;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        validCode = false;
                    }
                } else if ((isInteger(line[4]) == false) && (Interpreter.varStorage.containsKey(line[4]) == false)){
                    validCode = false;
                }
                break;
            case "PRINT":
                //test to see if there is anything to print
                try {
                    String testString = line[2];
                } catch (ArrayIndexOutOfBoundsException e) {
                    validCode = false;
                }
                break;
            case "REM":
                //Who's Rem?
                //(Keyword for comment, error detection not needed)
                break;
            case "IF":
                if (line[1].equals("IF") && line[5].equals("THEN")){
                    //Everything's fine. Nothing bad ever happened. EVEEEEERRRRRRRR.
                } else {
                    validCode = false;
                }
                break;
            case "GOTO":
                if (isInteger(line[2])){
                    //Again, nothing to see here. The code is valid.
                } else {
                    validCode = false;
                }
                break;
            case "FOR":
                if (isInteger(line[4]) && isInteger(line[6]) && line[3].equals("=") && line[5].equals("TO")){
                    //For loop is assumed to be valid
                } else {
                    validCode = false;
                }
                break;
            case "NEXT":
                break;
            case "GOSUB":
                //Checks to see if the argument is an existing line number
                if (isInteger(line[2]) && isInArray(line[2], lineNumbers)){
                    //This is empty and this is intended. There are no bugs in Code Sing Se.
                } else {
                    validCode = false;
                }
                break;
            case "RETURN":
                //Checks to see if the argument is an existing line number
                if (isInteger(line[2]) && isInArray(line[2], lineNumbers)){
                    //Here we are safe. Here we are free.
                } else {
                    validCode = false;
                }
                break;
            case "END":
                break;
            default:
                validCode = false;
                System.out.println("OMGWTFBBQ");
                break;
        }
        return validCode;
    }

}

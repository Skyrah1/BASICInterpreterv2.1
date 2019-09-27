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
public class Executor extends Program {

    private static int loopStartIndex;
    private static int loopNumCurrent;
    private static int loopNumEnd;
    private static boolean looping = false;

    private static int returnIndex;
    private static boolean runSub = false;  //true if subroutine is being executed, false otherwise

    public Executor() {

    }

    //Executes a single line
    public boolean executeLine(String[] line, String[] lineNumbers, boolean validCode, JTextArea outputArea) {
        boolean notEnd = true;
        if (validCode) {
            try {
                String lineNum = line[0];
                String keyword = line[1];
                String parameter = "";

                //Workaround so that NEXT case can be performed
                //(NEXT does not have an argument)
                try {
                    parameter = line[2];
                } catch (ArrayIndexOutOfBoundsException e) {

                }
                String value = "";

                //TODO Fix bug with NEXT
                switch (keyword) {
                    case "LET":
                        storeVariable(parameter, value, line, outputArea);
                        break;
                    case "PRINT":
                        //Check to see if there is a parameter to be printed
                        if (Interpreter.varStorage.containsKey(parameter)) {
                            printValue(parameter, outputArea);
                        } else {
                            printText(line, outputArea);
                        }
                        break;
                    case "IF":
                        ifStatement(line, lineNumbers, validCode, outputArea);
                        break;
                    case "GOTO":
                        gotoLine(lineNumbers, parameter);
                        break;
                    case "FOR":
                        setLoopParameters(line, lineNumbers, lineNum);
                        break;
                    case "NEXT":
                        if (looping == true) {
                            if (loopNumCurrent < loopNumEnd) {
                                gotoLine(lineNumbers, lineNumbers[loopStartIndex]);
                                loopNumCurrent += 1;
                                //System.out.println("Looping...");
                            } else {
                                looping = false;
                                System.out.println("Loop end.");
                            }
                        } else {
                            System.out.println("Test failed.");
                        }
                        break;
                    case "GOSUB":
                        setReturnNumber(line, lineNumbers, lineNum);
                        gotoLine(lineNumbers, parameter);
                        break;
                    case "RETURN":
                        if (runSub) {
                            gotoLine(lineNumbers, lineNumbers[returnIndex]);
                        } else {
                            validCode = false;
                        }
                        break;
                    case "END":
                        notEnd = false;
                        break;
                    default:
                        System.out.println("How did they get past the syntax checker?!");
                        break;
                }

                //in case of empty lines and/or errors that somehow got through
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } else {
            outputArea.append("Error detected at line " + "" + line[0] + ".");
        }
        return notEnd;
    }

    //Changes the current line index in the interpreter
    //NOTE: Requires Executor.java to know currentLineIndex from Interpreter.java
    private void gotoLine(String[] lineNumbers, String lineNum) {
        //System.out.println("...");
        for (int i = 0; i < lineNumbers.length; i++) {
            if (lineNum.equals(lineNumbers[i])) {
                Interpreter.currentLineIndex = i - 1;   //Set to i - 1 because currentLineIndex is incremented once before next execution
                //System.out.println(Interpreter.currentLineIndex);
            }
        }
    }

    private void setLoopParameters(String[] line, String[] lineNumbers, String lineNum) {
        for (int i = 0; i < lineNumbers.length; i++) {
            if (lineNum.equals(lineNumbers[i])) {
                loopStartIndex = i + 1;
            }
        }
        loopNumCurrent = Integer.parseInt(line[4]);
        loopNumEnd = Integer.parseInt(line[6]);

        looping = true;
        System.out.println("Loop set.");
    }

    private void setReturnNumber(String[] line, String[] lineNumbers, String lineNum) {
        for (int i = 0; i < lineNumbers.length; i++) {
            if (lineNum.equals(lineNumbers[i])) {
                returnIndex = i + 1;  //sets the return to the line after the GOSUB to prevent infinite loop
            }
        }
        runSub = true;
    }

    //Method for running if/then/else code line
    //NOTE: Currently only works for assignment, but could be changed to work for others
    private void ifStatement(String[] line, String[] lineNumbers, boolean validCode, JTextArea outputArea) {
        String var1 = line[2];
        String var2 = line[4];
        String operator = line[3];
        String actionLine = "0 LET";   //The 0 is there to act as a sub. line number to make the "action line"
        String[] actionLineArray;
        boolean conditionFilled = performLogic(var1, var2, operator, outputArea);
        if (conditionFilled) {
            //Takes the THEN case, creates an "action line" which contains the
            //THEN case to act as a line of code to be executed.
            //After that, we call executeLine() on the "action line" to perform it.
            int i = 6;
            boolean end = false;
            while (i < line.length && end == false) {
                if (line[i].equals("ELSE")) {
                    end = true;
                } else {
                    actionLine = actionLine + " " + line[i];
                }
                i++;
            }
            //Execute the "action line"
            actionLineArray = actionLine.split("\\s");
            executeLine(actionLineArray, lineNumbers, validCode, outputArea);
        } else {
            boolean elseFound = false;
            int elseIndex = 0;
            for (int i = 6; i < line.length; i++) {
                if (line[i].equals("ELSE")) {
                    elseIndex = i;
                    elseFound = true;
                }
            }
            if (elseFound) {
                //Same thing with the THEN case.
                int i = elseIndex + 1;
                while (i < line.length) {
                    actionLine = actionLine + " " + line[i];
                    i++;
                }
                //Execute the "action line"
                actionLineArray = actionLine.split("\\s");
                executeLine(actionLineArray, lineNumbers, validCode, outputArea);
            } else {
                //Nothing happens if there is no ELSE statement.
            }
        }

    }

    //Performs basic arithmetic on 2 variables
    private String performArithmetic(String var1, String var2, String operator, JTextArea outputArea) {
        String answer = "0";
        int num1 = Integer.parseInt(var1);
        int num2 = Integer.parseInt(var2);
        switch (operator) {
            case "+":
                answer = Integer.toString(num1 + num2);
                break;
            case "-":
                answer = Integer.toString(num1 - num2);
                break;
            case "*":
                answer = Integer.toString(num1 * num2);
                break;
            case "/":
                answer = Integer.toString(num1 / num2);
                break;
            case "%":
                answer = Integer.toString(num1 % num2);
                break;
            default:
                outputArea.append("Error performing arithmetic on " + num1 + " and " + num2);
                break;
        }
        return answer;
    }

    //Performs a check on a logical condition, and returns true if the condition is true
    private boolean performLogic(String var1, String var2, String operator, JTextArea outputArea) {
        boolean answer = false;
        String firstVar = var1;
        String secondVar = var2;

        if (Interpreter.varStorage.containsKey(var1)) {
            firstVar = Interpreter.varStorage.get(var1).toString();
        }
        if (Interpreter.varStorage.containsKey(var2)) {
            secondVar = Interpreter.varStorage.get(var2).toString();
        }

        if (isInteger(firstVar) && isInteger(secondVar)) {
            int num1 = Integer.parseInt(firstVar);
            int num2 = Integer.parseInt(secondVar);
            switch (operator) {
                case "==":
                    if (num1 == num2) {
                        answer = true;
                    }
                    break;
                case "!=":
                    if (num1 != num2) {
                        answer = true;
                    }
                    break;
                case ">":
                    if (num1 > num2) {
                        answer = true;
                    }
                    break;
                case "<":
                    if (num1 < num2) {
                        answer = true;
                    }
                    break;
                case ">=":
                    if (num1 >= num2) {
                        answer = true;
                    }
                    break;
                case "<=":
                    if (num1 <= num2) {
                        answer = true;
                    }
                    break;
                default:
                    outputArea.append("Error performing logic operation on " + firstVar + " and " + secondVar);
                    break;
            }
        } else {
            outputArea.append("Error: logical operation currently only works on integers.");
        }
        return answer;
    }

    //Stores a variable in varStorage until program finishes running
    private void storeVariable(String varName, String varValue, String[] line, JTextArea outputArea) {
        boolean readingString = false;
        boolean validCode = true;
        //If we're inputting a string, do this
        //Second condition checks if we're using a string in storage
        if (line[4].contains("\"") || (Interpreter.varStorage.containsKey(line[4]) && Interpreter.varStorage.get(line[4]).toString().contains("\""))) {
            readingString = true;
            int i = 4;
            boolean notPlus;
            while (i < line.length && readingString) {
                //Check to see if the current token is a '+' sign
                notPlus = !line[i].equals("+");
                if (Interpreter.varStorage.containsKey(line[i])) {
                    varValue = varValue + Interpreter.varStorage.get(line[i]).toString();
                    //stop reading at the end of the string
                } else if (line[i].charAt(line[i].length() - 1) == '"' && i != 4) {
                    readingString = false;
                    varValue = varValue + line[i];
                } else if (notPlus) {
                    varValue = varValue + line[i] + " ";
                }
                i++;
            }
            //Otherwise, do this
        } else {
            //Test to see if it's an integer or if varStorage contains it
            //If not, do not assign a value to the parameter
            if (Interpreter.varStorage.containsKey(line[4])) {
                varValue = Interpreter.varStorage.get(line[4]).toString();
            } else if (isInteger(line[4])) {
                varValue = line[4];
            } else {
                validCode = false;
            }
            //perform arithmetic if necessary
            try {
                String operator = line[5];
                String var1;
                String var2;

                if (Interpreter.varStorage.containsKey(line[4])) {
                    var1 = Interpreter.varStorage.get(line[4]).toString();
                } else {
                    var1 = line[4];
                }
                if (Interpreter.varStorage.containsKey(line[6])) {
                    var2 = Interpreter.varStorage.get(line[6]).toString();
                } else {
                    var2 = line[6];
                }

                if (isInteger(var1) && isInteger(var2)) {
                    varValue = performArithmetic(var1, var2, operator, outputArea);
                } else {
                    validCode = false;
                }
                //does nothing if arithmetic is not necessary
            } catch (ArrayIndexOutOfBoundsException e) {

            }

        }
        if (validCode) {
            varValue = varValue.replaceAll("\"", "");
            Interpreter.varStorage.put(varName, varValue);
        } else {
            outputArea.append("Error assigning value to " + varName + "\n");
        }
    }

    //Prints the value of a parameter
    private void printValue(String parameter, JTextArea outputArea) {
        //The value in the Hashtable is an Object, so it must be converted
        //to a string (even though a string is an object)
        String value = Interpreter.varStorage.get(parameter).toString();
        outputArea.append(value + "\n");
    }

    //Prints out plain text
    private void printText(String[] line, JTextArea outputArea) {
        String text = "";
        boolean notPlus;
        for (int i = 2; i < line.length; i++) {
            notPlus = !line[i].equals("+");

            if (Interpreter.varStorage.containsKey(line[i])) {
                text = text + Interpreter.varStorage.get(line[i]).toString();
            } else if (notPlus) {
                text = text + line[i] + " ";
            }
        }
        text = text.replace("\"", "");
        outputArea.append(text + "\n");
    }

}

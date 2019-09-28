/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTextArea;

/**
 *
 * @author ASUS
 */
public class PreParser extends Program {

    String[] specKeyList;

    public PreParser() {
        specKeyList = "list renumber save load".toUpperCase().split("\\s");
    }

    //Splits program into individual lines
    //Performs special actions like "list"
    public String[] preParseCode(JTextArea codeArea) {
        String[] codeArray = codeArea.getText().split("\\n");
        String specKeyword = "";
        String fileName = "";
        for (int i = 0; i < codeArray.length; i++) {
            for (int j = 0; j < specKeyList.length; j++) {
                if (codeArray[i].contains(specKeyList[j])) {
                    if (codeArray[i].contains("SAVE") || codeArray[i].contains("LOAD")) {
                        String[] lineArray = codeArray[i].split("\\s");
                        specKeyword = lineArray[0];
                        try {
                            fileName = lineArray[1];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("No file was detected.");
                        }
                    } else {
                        specKeyword = codeArray[i];
                    }
                    codeArray[i] = "";  //removes the line from the program
                }
            }
        }
        performSpecialAction(codeArray, specKeyword, fileName, codeArea);
        return codeArray;
    }

    private void performSpecialAction(String[] codeArray, String specKeyword, String fileName, JTextArea codeArea) {
        switch (specKeyword) {
            case "LIST":
                listCode(codeArray, codeArea);
                break;
            case "RENUMBER":
                renumberCode(codeArray, codeArea);
                break;
            case "SAVE":
                saveCode(codeArray, fileName);
                break;
            case "LOAD":
                loadCode(codeArray, fileName, codeArea);
                break;
            default:
                System.out.printf("No special keywords were detected.\n"
                        + "If you didn't input any special keywords such as 'LIST', ignore this message.\n");
                break;
        }
    }

    //Lists and renumbers the code (makes code numbers all multiples of 10) if all non-blank lines are numbered
    private void renumberCode(String[] codeArray, JTextArea codeArea) {
        boolean validCode = true;
        int testInt = 0;
        int currentLineNum = 10;    //Line numbers are 10, 20, 30, etc.
        listCode(codeArray, codeArea);

        //Test to see if lines are numbered
        for (int i = 0; i < codeArray.length; i++) {
            try {
                testInt = Integer.parseInt(codeArray[i].split("\\s")[0]);
                //To detect if a line is not numbered and is not blank
            } catch (NumberFormatException e) {
                if (codeArray[i].equals("") == false) {
                    validCode = false;
                }
            }
        }

        if (validCode) {
            //Renumbers line numbers to multiples of 10
            for (int i = 0; i < codeArray.length; i++) {
                if (codeArray[i].equals("") == false) {
                    String[] codeLine = codeArray[i].split("\\s");
                    codeLine[0] = Integer.toString(currentLineNum);
                    codeArray[i] = returnArrayAsSingleString(codeLine);
                    currentLineNum += 10;
                }
            }
            //Clears the codeArea and reprints listed code
            String newCode = "";
            for (int i = 0; i < codeArray.length; i++) {
                if (codeArray[i].equals("") == false) {
                    newCode = newCode + codeArray[i] + "\n";
                }
            }
            codeArea.setText(newCode);
        }
    }

    private void listCode(String[] codeArray, JTextArea codeArea) {
        int[] numArray = new int[codeArray.length];
        boolean validCode = true;

        //Fill numArray with line numbers
        for (int i = 0; i < codeArray.length; i++) {
            try {
                numArray[i] = Integer.parseInt(codeArray[i].split("\\s")[0]);
                //To detect if a line is not numbered and is not blank
            } catch (NumberFormatException e) {
                if (codeArray[i].equals("") == false) {
                    validCode = false;
                }
            }
        }
	
        if (validCode) {
	    QuickSort quickSort = new QuickSort();
	    quickSort.sort(numArray, codeArray);
            
	    /*
	    //Below is the old sorting code that was used before I knew that
	    //quick sort existed
	    //RIP inefficient code (2017 - 2019)
	    
	    int numSorter;
            String codeSorter;
	    boolean sorted = false;
            while (sorted == false) {
                sorted = true;
                for (int i = 0; i < numArray.length - 1; i++) {
                    if (numArray[i] > numArray[i + 1]) {
                        numSorter = numArray[i + 1];
                        codeSorter = codeArray[i + 1];
                        numArray[i + 1] = numArray[i];
                        codeArray[i + 1] = codeArray[i];
                        numArray[i] = numSorter;
                        codeArray[i] = codeSorter;

                        sorted = false;
                    }
                }
            }*/

            //Clears the codeArea and reprints listed code
            String newCode = "";
            for (int i = 0; i < codeArray.length; i++) {
                if (codeArray[i].equals("") == false) {
                    newCode = newCode + codeArray[i] + "\n";
                }
            }
            codeArea.setText(newCode);
        }

    }
    
    //Saves the program to a text file
    private void saveCode(String[] codeArray, String fileName) {
        File file = new File("C:\\Users\\ASUS\\Documents\\NetBeansProjects\\BASICInterpreterv2.1\\src\\basicinterpreterv2\\pkg1\\" + fileName);
        try {
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("New file created!");
            } else {
                System.out.println("File already exists! Overwriting...");
                FileWriter eraser = new FileWriter(file);
                eraser.write("");
                eraser.close();
            }
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < codeArray.length; i++) {
                writer.write(codeArray[i] + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadCode(String[] codeArray, String fileName, JTextArea codeArea) {
        File file = new File("C:\\Users\\ASUS\\Documents\\NetBeansProjects\\BASICInterpreterv2.1\\src\\basicinterpreterv2\\pkg1\\" + fileName);
        String line = "";
        String newCode = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            boolean reading = true;
            while (reading){
                line = bufferedReader.readLine();
                if (line == null){
                    reading = false;
                } else {
                    newCode = newCode + line + "\n";
                }
            }
            codeArea.setText(newCode);
        }catch (FileNotFoundException e){
            System.out.println("File not found.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}


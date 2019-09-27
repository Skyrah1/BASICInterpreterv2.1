/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public abstract class Program {
    
    protected Scanner userInput;

    Program() {
        userInput = new Scanner(System.in);
    }

    // method to display a prompt string
    // and read a text string from the user
    // Note there is no check for invalid input
    // returns the value of the text entered by the user
    public String getText(String promptStr) {
        String inputStr;
        String valueStr;
        System.out.println(promptStr);
        valueStr = userInput.nextLine();
        return (valueStr);
    }

    // method to display a prompt string
    // and read a single integer from the user
    // Note there is no check for invalid input
    // returns the value of the integer entered by the user
    public int getInteger(String promptStr) {
        String inputStr;
        int value;
        System.out.println(promptStr);
        inputStr = userInput.nextLine();
        value = Integer.parseInt(inputStr);
        return (value);
    }

    // method to display a prompt string and read a single real number from the user
    // Note there is no check for invalid input
    // returns the value of the real number entered by the user as a float type
    public float getReal(String promptStr) {
        String inputStr;
        float value;
        System.out.println(promptStr);
        inputStr = userInput.nextLine();
        value = Float.parseFloat(inputStr);
        return (value);
    }
    
    public double numToPower(double number, int power){
        double answer = 1;
        if (power == 0){
            return 1;
        }else if (power > 0){
            for (int i = 0; i < power; i++){
                answer = answer * number;
            }
        } else if (power < 0){
            //Since the power is negative, i must be decremented until it is less than the power
            for (int i = 0; i > power; i--){
                answer = answer / number;
            }
        }
        return answer;
    }
    
    public static void printArrayAsSingleString(String[] array){
        String text = "";
            for (int i = 0; i < array.length; i++){
                text += array[i] + " ";
            }
        System.out.println(text);
    }
    
    public String returnArrayAsSingleString(String[] array){
        String text = "";
            for (int i = 0; i < array.length; i++){
                text += array[i] + " ";
            }
        return text;
    }
    
    //Returns true if a variable is an int, or false if it's a string
    public boolean isInteger(String var) {
        boolean isInt = true;
        try {
            int testInt = Integer.parseInt(var);
        } catch (NumberFormatException e) {
            isInt = false;
        }
        return isInt;
    }
    
    public boolean isInArray(String element, String[] array){
        boolean answer = false;
        for (int i = 0; i < array.length; i++){
            if (array[i].equals(element)){
                answer = true;
            }
        }
        return answer;
    }
    
    public boolean isInArray(int element, int[] array){
        boolean answer = false;
        for (int i = 0; i < array.length; i++){
            if (array[i] == element){
                answer = true;
            }
        }
        return answer;
    }
}

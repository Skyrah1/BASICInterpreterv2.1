/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicinterpreterv2.pkg1;

/**
 *
 * @author ASUS
 */
public class Parser extends Program {
    
    public Parser(){
        
    }
    
    public static String[] lineParser(String line){
        return line.split("\\s");   //tokens are split by whitespace
    }
    
}

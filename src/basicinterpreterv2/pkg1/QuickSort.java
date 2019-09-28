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
public class QuickSort {
    
    public void sort(int[] intArray, String[] stringArray){
        //Utils.printArray(intArray);
        if (intArray.length == 2){
            if (intArray[0] > intArray[1]){
                switchElements(intArray, 0, 1);
		switchElements(stringArray, 0, 1);
            }
        } else {
            sort(intArray, stringArray, 0, intArray.length - 1);
        }
    }
    
    private void sort(int[] intArray, String[] stringArray, int index1, int index2){
        //System.out.printf("Index1: %d, Index2: %d\n", index1, index2);
        if (index1 == index2 - 1){
            if (intArray[index1] > intArray[index2]){
                switchElements(intArray, index1, index2);
		switchElements(stringArray, index1, index2);
            }
        } else if (index1 < index2) {
            int i = index1 - 1;
            int pivot = intArray[index2];
            int j = index1;
            for (j = index1; j <= index2; j++){
                if (intArray[j] <= pivot){
                    i++;
                    switchElements(intArray, i, j);
		    switchElements(stringArray, i, j);
                }
            }
            sort(intArray, stringArray,  index1, i-1);
            sort(intArray, stringArray, i+1, index2);
            //Utils.printArray(intArray);
        }
    }
    
    private void switchElements(int[] array, int index1, int index2){
        int tempValue = array[index1];
        array[index1] = array[index2];
        array[index2] = tempValue;
    }
    
    private void switchElements(String[] array, int index1, int index2){
        String tempValue = array[index1];
        array[index1] = array[index2];
        array[index2] = tempValue;
    }
    
    
}

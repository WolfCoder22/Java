package chacheshw8;

//IntIO.java
//by Scot Drysdale, modified by THC.
//Modeled after a C++ class demo.

//Definitions of functions to read and write arrays of Integers.

import java.util.Scanner;
import java.io.*;
import javax.swing.*;

public class IntIO {
// Read integers from a file into array a, returning how many were read.
// Doesn't read more than there is room for in the array.
// The second parameter is a String giving the directory that we start from.
public static int readInts(Integer [] a, String directory) {
 int n = 0; // count of items read into array
 Scanner input = null;
 
 // Put up a file chooser window to select a file to read.
 JFileChooser chooser = new JFileChooser(directory);
 int status = chooser.showOpenDialog(null);
 
 boolean fileOpened = true;    // OK unless proven otherwise
 
 do {
   // Get a file from the file chooser.  If the user hits the Cancel button,
   // end the program.
   while (status != JFileChooser.APPROVE_OPTION) {
     if (status == JFileChooser.CANCEL_OPTION)
       System.exit(0);
     else {
       System.out.println("No file chosen.  Try again.");
       status = chooser.showOpenDialog(null);
     }
   }
 
   // We have a file from the file chooser.  We should be able to create a
   // Scanner to read the file.
   File inputFile = chooser.getSelectedFile();
   try {
     input = new Scanner(inputFile);
   }
   catch (FileNotFoundException e) {
     fileOpened = false;
     System.out.println("Could not open file.  Try again.");
   }
 }
 while (!fileOpened);
 
 // Now the file has been opened.  Read it until we read the last integer or
 // we've filled the array a.
 while (input.hasNext() && n < a.length)
   a[n++] = input.nextInt();
 
 return n;
}

// Write the first n elements of array a.
public static void writeInts(Integer [] a, int n) {
 int i;

 System.out.println();
 for (i = 0; i < n; i++) {
   System.out.println(i + "\t" + a[i]);
 }
 System.out.println();
}
}
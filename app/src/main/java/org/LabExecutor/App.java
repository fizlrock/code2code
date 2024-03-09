
package org.LabExecutor;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic;
import org.LabExecutor.LatexFormater.Task1Formatter;

public class App {

  public static void main(String[] args) {
    // String line = "колокольня";
    // System.out.println("Арифметическое кодирование");
    // System.out.println(Arithmetic.code(line));

    String line2 = "ГНННОООМНГ";
    var report = BlockHuffman.task1(line2, 2);
    System.out.println(Task1Formatter.format(report));
    
    System.out.println("Working Directory = " + System.getProperty("user.dir"));
  }
}

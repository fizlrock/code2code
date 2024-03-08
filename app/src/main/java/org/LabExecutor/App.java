
package org.LabExecutor;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic;

public class App {

  public static void main(String[] args) {
    //String line = "колокольня";
    //System.out.println("Арифметическое кодирование");
    //System.out.println(Arithmetic.code(line));

    String line2 = "ГНННОООМНГ";
    System.out.printf("Хаффман с блокирование для строки: %s\n", line2);
    var report = BlockHuffman.task1(line2, 2);
    System.out.println(report);

  }
}

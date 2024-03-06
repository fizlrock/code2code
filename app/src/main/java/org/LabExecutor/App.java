
package org.LabExecutor;

import org.LabExecutor.Algoritms.SinglePass.Arithmetic;

public class App {

  public static void main(String[] args) {
    String line = "колокольня";
    System.out.println("Арифметическое кодирование");
    System.out.println(Arithmetic.code(line));

  }
}

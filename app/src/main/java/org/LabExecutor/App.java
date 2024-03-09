
package org.LabExecutor;

import java.util.List;

import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;

public class App {

  public static void main(String[] args) {
    // String line = "колокольня";
    // System.out.println("Арифметическое кодирование");
    // System.out.println(Arithmetic.code(line));

    Lab3Version vn = new Lab3Version(9,"СОКККККООО", 2, "РОРНРПОООО", "", "", "", "", "");
    var versions = List.of(vn);
    Lab3Executor.executeAndCompilePDF(versions);


    System.out.println("Working Directory = " + System.getProperty("user.dir"));
  }
}

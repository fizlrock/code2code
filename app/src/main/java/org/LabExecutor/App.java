
package org.LabExecutor;

import java.util.List;

import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78.Task53Report;
import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;
import org.LabExecutor.LatexFormater.Lab3Formatter;

public class App {

  public static void main(String[] args) {
    executuAll();
  }

  public static void executuAll() {
    var versions = Lab3Executor.loadVersions();
    Lab3Executor.executeAndCompilePDF(versions);

  }

  public static void temp() {

    Lab3Version vn = new Lab3Version(9, "СОКККККООО", 2, "РОРНРПОООО", "", "", "", "", "");
    var versions = List.of(vn);
    Lab3Executor.executeAndCompilePDF(versions);

    System.out.println("Working Directory = " + System.getProperty("user.dir"));
  }
}

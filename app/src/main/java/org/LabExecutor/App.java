
package org.LabExecutor;

import java.util.List;

import org.LabExecutor.Algoritms.SinglePass.LZXX.LZXXAlgorithms;
import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;

public class App {

  public static void main(String[] args) {
    var report = LZXXAlgorithms.encode("<0,0,в> <0,0,ы> <8,2,в> <6,1,х> <0,0,у> <8,1,о> <0,0,л> <0,0,ь> <0,0, > <1,3,о> <0,0,д>");
    System.out.println(report.lz77().dictRows());
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

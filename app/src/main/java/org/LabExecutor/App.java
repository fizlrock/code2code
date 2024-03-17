
package org.LabExecutor;

import java.util.List;

import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;

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


package org.LabExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ77;
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

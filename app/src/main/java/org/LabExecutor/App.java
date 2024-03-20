
package org.LabExecutor;

import java.util.List;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.SinglePass.LZXX.LZSS;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZSS.Task53Report;
import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;
import org.LabExecutor.LatexFormater.Lab3Formatter;

public class App {

  public static void main(String[] args) {

    var versions = Lab3Executor.loadVersions();
    var rep = versions.stream()
        .map(Lab3Version::line6)
        .map(l -> LZSS.decode(l, 10))
        .map(Task53Report::result)
        .collect(Collectors.joining("\n"));

    var rep2 = versions.stream()
        .map(Lab3Version::line3)
        .map(l -> LZSS.code(l, 10, 6))
        .map(Lab3Formatter::formatTask32Report)
        .collect(Collectors.joining("\n"));

    System.out.println(rep2);

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


package org.LabExecutor;

import java.util.List;
import java.util.StringJoiner;

import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ77;
import org.LabExecutor.Executor.Lab3Executor;
import org.LabExecutor.Executor.Lab3Executor.Lab3Version;
import org.LabExecutor.LatexFormater.Lab3Formatter;

public class App {

  static List<Lab3Version> versions = Lab3Executor.loadVersions();

  public static void main(String[] args) {
    executuAll();
  }

  public static void testLZ77Formating() {
    StringJoiner sj = new StringJoiner("\n");

    sj.add(Lab3Formatter.loadRes("latex_header.tex"));
    versions.stream()
        .map(Lab3Version::line5)
        .map(s -> new LZ77(10, 6).decode(s))
        .map(Lab3Formatter::formatTask52Report)
        .forEach(sj::add);
    sj.add(Lab3Formatter.loadRes("latex_footer.tex"));

    Lab3Executor.writeReport("./doc_src/lz77report.tex", sj.toString());

  }

  public static void testDecodeLZ77() {
    var versions = Lab3Executor.loadVersions();
    versions.stream()
        .map(Lab3Version::line5)
        .map(s -> new LZ77(10, 6).decode(s))
        .forEach(System.out::println);

  }

  public static void testCodeLZ77() {
    var versions = Lab3Executor.loadVersions();
    versions.stream()
        .map(Lab3Version::line3)
        .map(s -> new LZ77(10, 6).encode(s))
        .forEach(System.out::println);

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

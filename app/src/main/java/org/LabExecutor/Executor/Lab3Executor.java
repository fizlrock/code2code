
package org.LabExecutor.Executor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman;
import org.LabExecutor.Algoritms.DoublePass.BlockHuffman.Task1Report;
import org.LabExecutor.LatexFormater.Lab3Formatter;
import org.LabExecutor.LatexFormater.Lab3Formatter.Lab3Report;

public class Lab3Executor {

  public static Lab3Report execute(Lab3Version version) {

    Task1Report t1r = BlockHuffman.task1(version.line1(), version.blockSize());
    Lab3Report l3r = new Lab3Report(version.versionNum(), t1r);
    return l3r;
  }

  public static void executeAndCompilePDF(List<Lab3Version> versions) {
    System.out.printf("Решение лабораторных работ. Кол-во: %d\n", versions.size());
    var reports = versions.stream()
        .map(Lab3Executor::execute)
        .collect(Collectors.toList());
    System.out.println("Всё решено, формирование отчета.");
    var latex_report = Lab3Formatter.format(reports);
    writeReport("./doc_src/report.tex", latex_report);
    executeLatexToPdF();
    executeLatexToPdF();
  }

  private static void executeLatexToPdF() {
    try {
      var pb = new ProcessBuilder("pdflatex", "report.tex");
      pb.directory(new File("./doc_src/"));
      var proc = pb.start();
      proc.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void writeReport(String path, String data) {
    try (FileWriter fw = new FileWriter(path)) {
      fw.write(data);
    } catch (IOException e) {
      throw new RuntimeException("Ошибка записи latex отчета" + e);
    }
  }

  public static record Lab3Version(
      int versionNum,
      String line1, // Задание 1. Хаффман
      int blockSize,
      String line2, // Задание 2. Кодирование Адаптивный хаффман
      String line3, // Задание 3. Кодирование(LZ77, LZSS, LZ78)
      String line4, // Задание 4. Декодирование Адаптивный хаффман
      String line5, // Декодирование LZ77
      String line6, // LZSS
      String line7 // LZ78
  ) {
  }
}

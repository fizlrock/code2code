
package org.LabExecutor.Executor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman;
import org.LabExecutor.Algoritms.DoublePass.BlockHuffman.Task1Report;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task2;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task2.Task2Report;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic.Task4Report;
import org.LabExecutor.LatexFormater.Lab3Formatter;
import org.LabExecutor.LatexFormater.Lab3Formatter.Lab3Report;

public class Lab3Executor {

  public static Lab3Report execute(Lab3Version version) {

    Task1Report t1r = null;
    Task2Report t2r = null;
    Task4Report t4r = null;
    try {
      t1r = BlockHuffman.task1(version.line1(), version.blockSize());
      t2r = new Task2(version.line2()).getReport();
      t4r = Arithmetic.code(version.line2);
    } catch (Exception e) {
      System.out.println("Ошибка выполенения варианта: " + version);
      e.printStackTrace();
    }
    Lab3Report l3r = new Lab3Report(version.versionNum(), t1r, t2r, t4r);
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
    executeLatexToPdF(); // Второй запуск для создания оглавления
  }

  public static List<Lab3Version> loadVersions() {

    List<Lab3Version> versions = new ArrayList<>();

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    var is = classloader.getResourceAsStream("labversions.csv");
    try (Scanner s = new Scanner(is)) {
      while (s.hasNextLine()) {
        try {
          versions.add(parseLine(s.nextLine()));
        } catch (Exception e) {
          System.out.printf("Ошибка парсинга строки" + e);
        }
      }
    }
    return versions;
  }

  public static Lab3Version parseLine(String line) {
    String[] parts = line.split("\\$");
    return new Lab3Version(
        Integer.parseInt(parts[0]),
        parts[1],
        Integer.parseInt(parts[2]),
        parts[3],
        parts[4],
        parts[5],
        parts[6],
        parts[7],
        parts[8]);
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

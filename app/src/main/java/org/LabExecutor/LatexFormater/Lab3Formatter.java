
package org.LabExecutor.LatexFormater;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman.Task1Report;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic.Task4Report;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task2.Task2Report;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task51.Task51Report;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ77.Task31Report;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78.Task33Report;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78.Task54Report;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZSS;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZSS.Task32Report;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZSS.Task53Report;

public class Lab3Formatter {

  public static String format(List<Lab3Report> reports) {
    LatexBuilder lb = new LatexBuilder();
    lb.addText(loadRes("latex_header.tex"));
    reports.stream()
        .map(Lab3Formatter::format)
        .forEach(lb::addText);
    lb.addText(loadRes("latex_footer.tex"));
    return lb.toString();
  }

  public static String format(Lab3Report report) {
    LatexBuilder lb = new LatexBuilder();
    lb.addSubsection("Вариант №" + report.versionNum());

    //lb.addText(formatTask1Report(report.task1report()));
    //lb.addPageBreak();
    //lb.addText(formatTask2Report(report.task2report()));
    //lb.addPageBreak();
    lb.addText(formatTask31Report(report.task31report()));
    //lb.addText(formatTask32Report(report.task32report()));
    //lb.addText(formatTask33Report(report.task33report()));
    //lb.addPageBreak();
    //lb.addText(formatTask4Report(report.task4report()));
    //lb.addPageBreak();
    //lb.addText(formatTask51Report(report.task51report()));
    //lb.addPageBreak();
    //lb.addText(formatTask53Report(report.task53report()));
    //lb.addPageBreak();
    //lb.addText(formatTask54Report(report.task54report()));
    //lb.addPageBreak();
    return lb.toString();
  }

  public static String formatTask53Report(Task53Report report) {

    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 5.3 Декодировать строку(LZSS)\\\\");
    lb.addText("Исходная строка: %s\\\\", report.input_line());
    lb.addTable53(report.steps());
    lb.addText("Результат: %s", report.result());

    return lb.toString();
  }
  public static String formatTask2Report(Task2Report report) {
    if (report == null)
      return "Error";
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 2. Сжать адаптивным хаффманом\\\\");
    lb.addText("Строка: \n" + report.input_line() + "\\\\");
    lb.addText("Результат: " + report.result());
    for (var step : report.tree_states())
      lb.addImage(GraphUtils.compileAndSaveGraph(step), 0.8);

    return lb.toString();
  }

  public static String formatTask31Report(Task31Report report) {
    if (report == null)
      return "";
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 3.1");
    lb.addText("Закодировать сообщение методом LZ77\\\\");
    lb.addText("Строка:" + report.getInputLine().replace("_", "\\_") + "\\\\");
    lb.addText("Результат: " + report.getResult().replace("_", "\\_") + "\\\\");
    lb.addTable31(report.getDictRows(), report.getBufferRows(), report.getTokens());
    return lb.toString();
  }

  public static String formatTask32Report(LZSS.Task32Report report) {
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 3.2");
    lb.addText("Закодировать сообщение методом LZSS\\\\");
    lb.addText("Строка:" + report.input_line().replace("_", "\\_") + "\\\\");
    lb.addText("Результат: " + report.result().replace("_", "\\_") + "\\\\");
    lb.addTable32(report.steps());
    return lb.toString();
  }

  public static String formatTask33Report(Task33Report report) {
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 3.3");
    lb.addText("Закодировать сообщение методом LZ78\\\\");
    lb.addText("Строка:" + report.inputLine().replace("_", "\\_") + "\\\\");
    lb.addTable33(report.steps());
    lb.addText("Результат: " + report.result().replace("_", "\\_") + "\\\\");
    return lb.toString();
  }

  public static String formatTask51Report(Task51Report report) {
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 5.1");
    lb.addText("\\\\ \n");

    lb.addText("Декодировать сообщение методом адаптивного хаффмана \\\\");
    lb.addText("Строка: \n" + report.input() + "\\\\");
    lb.addText("Результат: " + report.result());
    for (var step : report.treeStates())
      lb.addImage(GraphUtils.compileAndSaveGraph(step), 0.8);

    return lb.toString();
  }

  public static String formatTask1Report(Task1Report report) {
    if (report == null)
      return "Error";
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 1. Блочный хаффман \\\\");
    lb.addText("Строка %s, размер блока: %d", report.inputLine(), report.blockSize());
    lb.addLPTable(report.lineMetrics().getAlphabetProbability(), report.huffLetter());

    lb.addText("Энтропия алфавита: %.4f", report.lineMetrics().getEntropy());

    lb.addLPTableString(report.blockp(), report.huffBlock());
    lb.addText("Бит на символ при посимвольном кодировании: %.4f, при блочном: %.4f", report.BBSLetter(),
        report.BBSBlock());
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphLetter()), 0.5);
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphBlock()), 0.9);

    return lb.toString();
  }

  public static String formatTask4Report(Task4Report report) {
    if (report == null)
      return "Error";
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 4. Арифметическое кодирование\\\\");
    lb.addText("Исходная строка: %s\\", report.input_line());
    lb.addLPTable(report.probability());
    lb.addRangeTable(report.ranges());
    lb.addStepsTable(report.steps());
    lb.addText("Результат: %s", report.result());

    return lb.toString();
  }

  public static String formatTask54Report(Task54Report report) {
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 5.4 Декодировать строку(LZ78)\\\\");
    lb.addText("Исходная строка: %s\\\\", report.inputLine());
    lb.addTable54(report.steps());
    lb.addText("Результат: %s", report.result());

    return lb.toString();
  }

  public static String formatLatex(Map<?, ?> map) {
    return map.entrySet().stream()
        .map(e -> {
          return String.format("%s & %s", e.getKey(), e.getValue());
        })
        .collect(Collectors.joining("\\\\\n"));
  }

  public static record Lab3Report(
      int versionNum,
      Task1Report task1report,
      Task2Report task2report,
      Task31Report task31report,
      Task32Report task32report,
      Task33Report task33report,
      Task4Report task4report,
      Task51Report task51report,
      Task53Report task53report,
      Task54Report task54report) {
  }

  public static String loadRes(String resName) {

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    var is = classloader.getResourceAsStream(resName);
    StringJoiner sj = new StringJoiner("\n");
    try (Scanner s = new Scanner(is)) {
      while (s.hasNextLine())
        sj.add(s.nextLine());
    }

    return sj.toString();
  }
}

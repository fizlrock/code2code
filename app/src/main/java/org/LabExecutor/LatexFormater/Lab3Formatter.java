
package org.LabExecutor.LatexFormater;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.StringJoiner;
import java.util.Scanner;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman.Task1Report;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task2.Task2Report;
import org.LabExecutor.Algoritms.SinglePass.AdaptHuffman.Task51.Task51Report;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic.Task4Report;

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

    lb.addText(formatTask1Report(report.task1report()));
    lb.addPageBreak();
    lb.addText(formatTask2Report(report.task2report()));
    lb.addPageBreak();
    lb.addText(formatTask4Report(report.task4report()));
    lb.addPageBreak();
    lb.addText(formatTask51Report(report.task51report()));
    lb.addPageBreak();
    return lb.toString();
  }

  public static String formatTask2Report(Task2Report report) {
    if (report == null)
      return "Error";
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 2");
    lb.addText("Строка: \n" + report.input_line() + "\\\\");
    lb.addText("Результат: " + report.result());
    for (var step : report.tree_states())
      lb.addImage(GraphUtils.compileAndSaveGraph(step), 0.8);

    return lb.toString();
  }

  public static String formatTask51Report(Task51Report report) {
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 5.1 \\\\" + "\n");

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
    lb.addParagraph("Задание 1");
    lb.addText("Строка %s, размер блока: %d", report.inputLine(), report.blockSize());
    lb.addLPTable(report.lineMetrics().getAlphabetProbability(), report.huffLetter());

    lb.addText("Энтропия алфавита: %.2f", report.lineMetrics().getEntropy());

    lb.addLPTableString(report.blockp(), report.huffBlock());
    lb.addText("Бит на символ при посимвольном кодировании: %.2f, при блочном: %.2f", report.BBSLetter(),
        report.BBSBlock());
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphLetter()), 0.5);
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphBlock()), 0.9);

    return lb.toString();
  }

  public static String formatTask4Report(Task4Report report) {
    if (report == null)
      return "Error";
    LatexBuilder lb = new LatexBuilder();

    lb.addParagraph("Задание 4");
    lb.addText("Исходная строка: %s\\", report.input_line());
    lb.addLPTable(report.probability());
    lb.addRangeTable(report.ranges());
    lb.addStepsTable(report.steps());
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

  public static record Lab3Report(int versionNum, Task1Report task1report, Task2Report task2report,
      Task4Report task4report, Task51Report task51report) {
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

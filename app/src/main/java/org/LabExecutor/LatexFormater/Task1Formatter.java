
package org.LabExecutor.LatexFormater;

import java.util.Map;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman.CodeReport;

public class Task1Formatter {

  public static String format(CodeReport report) {
    LatexBuilder lb = new LatexBuilder();
    lb.addParagraph("Задание 1");

    lb.addText("Строка %s, размер блока: %d", report.inputLine(), report.blockSize());
    lb.addLPTable(report.lineMetrics().getAlphabetProbability(), report.huffLetter());

    lb.addText("Энтропия алфавита: %.2f", report.lineMetrics().getEntropy());

    lb.addLPTableString(report.blockp(), report.huffBlock());
    lb.addText("Бит на символ при посимвольном кодировании: %.2f, при блочном: %.2f", report.BBSLetter(),
        report.BBSBlock());
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphBlock()));
    lb.addImage(GraphUtils.compileAndSaveGraph(report.graphLetter()));

    return lb.getResult();
  }

  public static String formatLatex(Map<?, ?> map) {
    return map.entrySet().stream()
        .map(e -> {
          return String.format("%s & %s", e.getKey(), e.getValue());
        })
        .collect(Collectors.joining("\\\\\n"));
  }
}

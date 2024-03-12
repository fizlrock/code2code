
package org.LabExecutor.LatexFormater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.SinglePass.Arithmetic.CodingStep;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic.Range;

public class LatexBuilder {

  public void addParagraph(String title) {
    sj.add(String.format("\\paragraph{%s}\n", title));
  }

  public void addText(String text, Object... args) {
    sj.add(String.format(text, args));
  }

  public void addText(String text) {
    sj.add(text);
  }

  public void addPageBreak(){
    sj.add("\\pagebreak");
  }

  public void addSubsection(String section_name) {
    addText("\\subsection{%s}", section_name);
  }

  /**
   * Добавляет в документ таблицу Символ-Вероятность, сортируюя строки в порядке
   * невозрастания вероятности.
   * 
   * @param table
   */
  public void addLPTable(Map<Character, Double> table, Map<String, String> codes) {

    Comparator<Entry<Character, Double>> c = Comparator.comparing(Entry::getValue, Comparator.reverseOrder());
    c.thenComparing(Entry::getKey);

    String table_body = table.entrySet().stream()
        .sorted(c)
        .map(e -> String.format("%s & %.2f & %s", e.getKey(), e.getValue(), codes.get(String.valueOf(e.getKey()))))
        .collect(Collectors.joining("\\\\\\hline\n"));

    final String table_header = "\\begin{center}\n \\begin{tabular}{ |c|c|l| } \n  \\hline\n     Буква & Вероятность & Код\\\\ \\hline";
    final String table_footer = "\\\\ \\hline \\end{tabular}\n\\end{center}";

    sj.add(table_header);
    sj.add(table_body);
    sj.add(table_footer);

  }

  public void addLPTable(Map<Character, Double> table) {

    Comparator<Entry<Character, Double>> c = Comparator.comparing(Entry::getValue, Comparator.reverseOrder());
    c.thenComparing(Entry::getKey);

    String table_body = table.entrySet().stream()
        .sorted(c)
        .map(e -> String.format("%s & %.2f", e.getKey(), e.getValue()))
        .collect(Collectors.joining("\\\\\\hline\n"));

    final String table_header = "\\begin{center}\n \\begin{tabular}{ |c|c| } \n  \\hline\n     Буква & Вероятность \\\\ \\hline";
    final String table_footer = "\\\\ \\hline \\end{tabular}\n\\end{center}";

    sj.add(table_header);
    sj.add(table_body);
    sj.add(table_footer);

  }

  /**
   * Добавляет в документ таблицу Символ-Вероятность, сортируюя строки в порядке
   * невозрастания вероятности.
   * 
   * @param table
   */
  public void addLPTableString(Map<String, Double> table, Map<String, String> codes) {

    Comparator<Entry<String, Double>> c = Comparator.comparing(Entry::getValue, Comparator.reverseOrder());
    c.thenComparing(Entry::getKey);

    String table_body = table.entrySet().stream()
        .sorted(c)
        .map(e -> String.format("%s & %.2f & %s", e.getKey(), e.getValue(), codes.get(e.getKey())))
        .collect(Collectors.joining("\\\\\\hline\n"));

    final String table_header = "\\begin{center}\n \\begin{tabular}{ |c|c|l| } \n  \\hline\n     Блок & Вероятность & Код\\\\ \\hline";
    final String table_footer = "\\\\ \\hline \\end{tabular}\n\\end{center}";

    sj.add(table_header);
    sj.add(table_body);
    sj.add(table_footer);

  }

  public void addImage(String pathToImage, double size) {
    sj.add(String.format("\n\\includegraphics[width=%.1f\\linewidth]{%s}", size, pathToImage));
  }
  public void addImageWithWidth(String pathToImage, int width, double scale) {
    sj.add(String.format("\n\\includegraphics[height=%dmm, scale=%.2f]{%s}", width, scale, pathToImage));
  }

  private StringJoiner sj = new StringJoiner("\n");

  @Override
  public String toString() {
    return sj.toString();
  }

  public void addGraph(String graphBlock) {
    // dot to jpg
    // jpg to folder ./doc_src/images
    // \includegraphics[width=0.9\linewidth]{graph.jpg} to tex
  }

  public void addRangeTable(Map<Character, Range> ranges) {

    Comparator<Entry<Character, Range>> c = Comparator.comparing(arg0 -> arg0.getValue().start());

    String table_body = ranges.entrySet().stream()
        .sorted(c)
        .map(e -> String.format("%s & %.2f & %.2f", e.getKey(), e.getValue().start(), e.getValue().stop()))
        .collect(Collectors.joining("\\\\\\hline\n"));

    final String table_header = "\\begin{center}\n \\begin{tabular}{ |c|c|c| } \n  \\hline\n     Буква & Начало & Конец \\\\ \\hline";
    final String table_footer = "\\\\ \\hline \\end{tabular}\n\\end{center}";

    sj.add(table_header);
    sj.add(table_body);
    sj.add(table_footer);

  }

  public void addStepsTable(List<CodingStep> steps) {
    String table_body = steps.stream()
        .sorted(Comparator.comparing(CodingStep::delta, Comparator.reverseOrder()))
        .map(e -> String.format("%s & %.10f & %.10f & %.10f", e.letter(), e.delta(), e.min(), e.max()))
        .collect(Collectors.joining("\\\\\\hline\n"));

    final String table_header = "\\begin{center}\n \\begin{tabular}{ |c|c|c|c| } \n  \\hline\n     Буква & delta & min & max \\\\ \\hline";
    final String table_footer = "\\\\ \\hline \\end{tabular}\n\\end{center}";

    sj.add(table_header);
    sj.add(table_body);
    sj.add(table_footer);
  }

}

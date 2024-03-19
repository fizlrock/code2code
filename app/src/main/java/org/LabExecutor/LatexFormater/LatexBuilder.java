
package org.LabExecutor.LatexFormater;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.SinglePass.Arithmetic.CodingStep;
import org.LabExecutor.Algoritms.SinglePass.Arithmetic.Range;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ77.Token;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78.Task33Step;
import org.LabExecutor.Algoritms.SinglePass.LZXX.LZ78.Task53Step;

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

  public void addPageBreak() {
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

  public void addTable(String[] dictRows, String[] bufferRows, List<Token> tokens) {
    final String table_header, table_footer;
    table_header = "\\begin{table}[h!]\n\\centering\n\\begin{tabular}{|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|} \n\\hline\n\\multicolumn{10}{|c|}{Cловарь} & \\multicolumn{6}{c|}{Буфер} & Код  \\\\ \\hline";
    table_footer = "\\end{tabular}\n\\end{table}\n";

    StringJoiner table = new StringJoiner("\n");
    table.add(table_header);

    for (int i = 0; i < tokens.size(); i++) {
      Token t = tokens.get(i);
      int ll = t.getOffset();
      int rl = ll + t.getLength();

      StringJoiner row_parts = new StringJoiner(" & ");
      for (int d = 0; d < 10; d++) {
        char letter = dictRows[i].charAt(d);
        if (!Character.isLetter(letter))
          letter = ' ';
        if (ll <= d & d < rl)
          row_parts.add("\\cellcolor[HTML]{FFFF00} " + letter);
        else
          row_parts.add(String.valueOf(letter));
      }

      for (int b = 0; b < 6; b++) {
        if (b < bufferRows[i].length()) {
          Character symbol = bufferRows[i].charAt(b);
          if (!Character.isLetter(symbol))
            symbol = ' ';
          String color = "";
          if (b < t.getLength())
            color = "\\cellcolor[HTML]{FFFF00} ";
          if (b == t.getLength())
            color = "\\cellcolor[HTML]{8CE4F6} ";
          row_parts.add(color + String.valueOf(symbol));
        } else
          row_parts.add(" ");
      }
      row_parts.add(t.toString());
      table.add(row_parts.toString());
      table.add("\\\\ \\hline");
    }

    table.add(table_footer);
    sj.add(table.toString().replace("_", "\\_"));
  }

  public void addTable53(List<Task53Step> steps) {

    final String table_header, table_footer;
    table_header = "\\begin{table}[h!]\n\\centering\n\\begin{tabular}{|c|c|c|} \n\\hline\n Код & Словарь & Выходной поток \n\\hline\n";
    table_footer = "\\end{tabular}\n\\end{table}\n";

    StringJoiner table = new StringJoiner("\n");
    table.add(table_header);

    for (var step : steps) {
      StringJoiner row = new StringJoiner(" & ");
      row.add(step.code());
      row.add(step.dict().toString());
      row.add(step.output());

      table.add(row.toString());
      table.add("\\\\ \\hline");
    }

    table.add(table_footer);
    sj.add(table.toString().replace("_","\\_"));
  }

  public void addTable33(List<Task33Step> steps) {

    final String table_header, table_footer;
    table_header = "\\begin{table}[h!]\n\\centering\n\\begin{tabular}{|c|c|c|} \n\\hline\n Входная фраза (в словарь) & Код & Позиция словаря \\\\ \\hline\n";
    table_footer = "\\end{tabular}\n\\end{table}\n";

    StringJoiner table = new StringJoiner("\n");
    table.add(table_header);

    for (var step : steps)
      table.add(String.format("%s & %s & %d \\\\ \\hline", step.phrase(), step.code(), step.dictPos()));

    table.add(table_footer);
    sj.add(table.toString().replace("_","\\_"));
  }

}

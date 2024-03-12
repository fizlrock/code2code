
package org.LabExecutor.Algoritms.SinglePass.AdaptHuffman;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Task2 {

  public Task2(String line) {
    System.out.println("Строка: " + line);
    code(line);

  }

  private Task2Report report;

  public Task2Report getReport() {
    return report;
  }

  private StringJoiner result = new StringJoiner(" ");
  private int current_step;

  private EncodingModelRefreshing encodingModel = new EncodingModelRefreshing();

  private void code(String line) {
    var chars = line.toCharArray();
    var steps = new ArrayList<CodeStep>();
    for (int i = 0; i < chars.length; i++) {
      current_step = i + 1;
      steps.add(code(chars[i]));
    }
    report = new Task2Report(line, result.toString(), steps);

  }

  private CodeStep code(char symbol) {
    String result = "";
    if (encodingModel.contains(symbol)) { // если символ уже есть в модели
      result += encodingModel.writeCodeForCharacter((int) symbol); // выдаем на выход код этого символа
    } else { // если символа еще нет,
      result += encodingModel.writeCodeForCharacter(null); // выдаем escape-символ в выходной поток
      result += String.format("<%s>", symbol); // выдаем незакодированный символ в выходной поток
    }
    encodingModel.updateByCharacter(symbol); // обновляем модель текущим символом
    var tree = encodingModel.getTree();
    this.result.add(result);
    return new CodeStep(current_step, String.valueOf(symbol), result, formatTree(tree));
  }

  private static String formatTree(CodeTreeNode tree) {

    StringJoiner sj = new StringJoiner("\n");

    sj.add("digraph BST {");

    Consumer<CodeTreeNode> formater = new Consumer<CodeTreeNode>() {
      @Override
      public void accept(CodeTreeNode n) {
        sj.add(String.format("%d [label=\"%s\"]", n.hashCode(), n.toString()));
        if (n.left != null) {
          sj.add(String.format("%d -> %d", n.hashCode(), n.left.hashCode()));
          sj.add(String.format("%d -> %d", n.hashCode(), n.right.hashCode()));
          this.accept(n.left);
          this.accept(n.right);
        }
      }
    };
    formater.accept(tree);

    sj.add("}");
    return sj.toString();
  }

  public static record Task2Report(
      String input_line,
      String result,
      List<CodeStep> steps) {
  }

  public static record CodeStep(
      int step_num,
      String input,
      String output,
      String tree) {
  }

}

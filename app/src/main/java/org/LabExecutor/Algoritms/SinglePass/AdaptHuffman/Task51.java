
package org.LabExecutor.Algoritms.SinglePass.AdaptHuffman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task51 {

  public Task51(String line_to_decode) {
    encodingModel = new EncodingModelRefreshing();
    currentNode = encodingModel.getTree();
    lineToDecode = line_to_decode.replace(" ", "");
    decode();
  }

  private String lineToDecode;
  private List<String> treeStates = new ArrayList<>();
  private final EncodingModelRefreshing encodingModel;
  private CodeTreeNode currentNode;
  private Task51Report report;

  private void decode() {
    // Пример данных 'О'0'Р'00'П'100'Н'11011001001111
    StringBuffer result = new StringBuffer();
    var parts = Stream.of(lineToDecode.split("’|'"))
        .filter(p -> !p.isBlank())
        .map(Data::new)
        .collect(Collectors.toList());

    for (Data d : parts) {
      if (d.isLetter()) {
        var tree = encodingModel.updateByCharacter(d.letter);
        treeStates.add(tree);
        result.append(d.letter);
        currentNode = encodingModel.getTree();
      } else {
        d.bits.chars().forEach(c -> {
          if ((char) c == '1')
            currentNode = currentNode.right; // делаем шаг по дереву направо
          else
            currentNode = currentNode.left; // иначе делаем шаг по дереву налево

          if (currentNode.content != null) { // если пришли в обычный узел
            var tree = encodingModel.updateByCharacter(currentNode.content); // обновляем модель декодирванным символом
            treeStates.add(tree);
            result.append((char) (int) currentNode.content); // выдаем декодированный символ на выход
            currentNode = encodingModel.getTree(); // возвращаемся в начало дерева
          }
        });

      }

    }
    report = new Task51Report(lineToDecode, result.toString(), treeStates);
  }

  public static Task51Report tryExecute(String to_decode) {
    Task51Report report = null;
    try {
      report = new Task51(to_decode).getReport();
    } catch (Exception e) {
      report = new Task51Report("Ошибка декодирования", "Ошибка декодирования", new ArrayList<String>());
      System.out.println("Ошибка декодирования методом адаптивного хаффмана " + to_decode + " " + e);
    }
    return report;
  }

  public record Task51Report(
      String input,
      String result,
      List<String> treeStates) {
  }

  private static class Data {

    public Data(String line) {
      if (line.length() == 1 & Character.isLetter(line.charAt(0)))
        letter = line.charAt(0);
      else
        bits = line;
    }

    String bits = null;
    Character letter = null;

    public boolean isLetter() {
      return letter != null;
    }

    public boolean isBits() {
      return bits != null;
    }

    @Override
    public String toString() {
      if (isLetter())
        return String.format("Letter: %s", letter);
      else
        return String.format("Bits: %s", bits);

    }
  }

  public Task51Report getReport() {
    return report;
  }

}


package org.LabExecutor.Algoritms.SinglePass.AdaptHuffman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Модель компресии для адаптивного сжатия Хаффмана.
 * Содержит кодовое дерево и логику его обновления по мере поступления новой
 * информации.
 * Отвечает за генерацию и запись оптимальных кодов в выходной поток
 */
public class EncodingModelRefreshing {

  public final CodeTreeNode ZERO_NODE = new CodeTreeNode(null, 0); // узел для escape символа

  private CodeTreeNode tree = ZERO_NODE; // кодовое дерево
  private final List<CodeTreeNode> nodeList = new ArrayList<>(500); // упорядоченый по весам список узлов
  private final BitBuffer bitBuffer = new BitBuffer(1024); // бувер для накопления бит кода очередного символа
  private final CodeTreeNode[] nodeCache = new CodeTreeNode[256]; // таблица для быстрого поиска узла дерева для любого
                                                                  // символа

  public EncodingModelRefreshing() {
    nodeList.add(ZERO_NODE);
  }

  private List<String> steps = new ArrayList<>();
  private String last_message;

  private static String formatSubTree(CodeTreeNode tree, String comment) {

    StringJoiner sj = new StringJoiner("\n");

    int tree_hash = tree.hashCode() + ThreadLocalRandom.current().nextInt(0, 10000000);

    sj.add(String.format("subgraph cluster_%d {", tree_hash));

    Consumer<CodeTreeNode> formater = new Consumer<CodeTreeNode>() {
      @Override
      public void accept(CodeTreeNode n) {
        sj.add(String.format("%d [label=\"%s\"]", n.hashCode() + tree_hash, n.toString()));
        if (n.left != null) {
          sj.add(String.format("%d -> %d", n.hashCode() + tree_hash, n.left.hashCode() + tree_hash));
          sj.add(String.format("%d -> %d", n.hashCode() + tree_hash, n.right.hashCode() + tree_hash));
          this.accept(n.left);
          this.accept(n.right);
        }
      }
    };
    formater.accept(tree);

    sj.add(String.format("label = \"%s\"", comment));
    sj.add("}");
    return sj.toString();
  }

  /**
   * @param value
   * @return Набор состояний дерева во время перстройки в формате dot
   */
  public String updateByCharacter(int value) {
    steps.clear();
    // steps.add(formatSubTree(tree, "Начальное состояние"));

    CodeTreeNode node = nodeCache[((byte) value) & 0xff];
    CodeTreeNode parent;
    if (node != null) { // если уже есть узел для символа value
      node.weight = node.weight + 1; // увеличим его вес на 1
      parent = node.parent;
      last_message = "Увеличение веса узла " + (char) value;
    } else { // если узла нет, его надо создать
      /*
       * создаем промежуточный узел, прикрепляем к нему escape символ и новый
       * созданный узел для текущего символа
       * и вставляем промежуточный узел в дерево на старое место escape символа
       */
      CodeTreeNode zeroNodeParent = ZERO_NODE.parent;
      CodeTreeNode newNode = new CodeTreeNode(value, 1, null, null, null);
      CodeTreeNode intermediate = new CodeTreeNode(null, 0, ZERO_NODE, newNode, zeroNodeParent);
      nodeCache[((byte) value) & 0xff] = newNode;
      newNode.parent = intermediate;
      if (zeroNodeParent != null) {
        zeroNodeParent.left = intermediate;
      } else {
        tree = intermediate;
      }
      // добавляем новые узлы в список с учетом упорядоченности
      nodeList.set(nodeList.size() - 1, intermediate);
      nodeList.add(newNode);
      nodeList.add(ZERO_NODE);

      ZERO_NODE.parent = intermediate;

      parent = newNode.parent;
      last_message = "Добавление нового узла " + (char) value;
    }

    // обновляем веса родительских узлов
    while (parent != null) {
      parent.weight = parent.weight + 1;
      parent = parent.parent;
    }
    steps.add(formatSubTree(tree, last_message));

    // восстанавливаем упорядоченность дерева, если она была нарушена в результате
    // проделаных выше процедур по обновлению дерева
    while (reorderNodes()) {
      tree.updateWeights();
      steps.add(formatSubTree(tree, last_message));
    }

    String temp = steps.stream().collect(Collectors.joining("\n"));

    return "digraph ARST {\n" + temp + "\n}";
  }

  /**
   * упорядочиваем дерево,
   * переставляем узлы, нарушающие упорядоченность
   * 
   * @return
   */
  private boolean reorderNodes() {
    // System.out.println(nodeList);

    int i = nodeList.size() - 2;

    for (; i >= 0; i--) {
      if (nodeList.get(i).weight < nodeList.get(i + 1).weight) {
        i++;
        break;
      }
    }

    while (i > 0 && i < nodeList.size() - 1 && nodeList.get(i).weight == nodeList.get(i + 1).weight)
      i++;

    if (i != -1) {
      CodeTreeNode first = nodeList.get(i);
      int j = 0;
      for (; j < i; j++) { // ищем узел, с которым надо поменять местами узел, нарушающий порядок
        if (nodeList.get(j).weight < first.weight) {
          break;
        }
      }
      // меняем два узла в дереве местами для восстановления порядка
      CodeTreeNode firstParent = first.parent;
      CodeTreeNode second = nodeList.get(j);
      last_message = String.format("Меняем местами %s и %s", first, second);
      if (first.parent == second.parent) {
        if (first.weight < second.weight) {
          firstParent.left = first;
          firstParent.right = second;
        } else {
          firstParent.left = second;
          firstParent.right = first;
        }
      } else {
        if (second.parent != first) {
          if (second.parent.left == second) {
            second.parent.left = first;
          } else {
            second.parent.right = first;
          }
          first.parent = second.parent;
          if (firstParent.left == first) {
            firstParent.left = second;
          } else {
            firstParent.right = second;
          }
          second.parent = firstParent;
        }
      }

      // меняем эти же два узла местами в списке
      nodeList.set(j, first);
      nodeList.set(i, second);
      return true;
    }
    return false;
  }

  /**
   * Метод выдает в выходной поток код символа
   * 
   * @param value     - символ, который надо закодировать
   * @param bitWriter - приемник для битов символа
   * @throws IOException
   */

  public String writeCodeForCharacter(Integer value) {
    CodeTreeNode node;
    if (value != null) {
      node = nodeCache[value.byteValue() & 0xff];
    } else {
      node = ZERO_NODE;
    }
    bitBuffer.setCurrent(0);
    while (node.parent != null) { // идем от листа к корню дерева, собирая биты по пути
      if (node.parent.left == node) {
        bitBuffer.append(0);
      } else {
        bitBuffer.append(1);
      }
      node = node.parent;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = bitBuffer.current - 1; i >= 0; i--) // выдаем биты кода, не забывая развернуть их в обратном порядке
    {
      sb.append(bitBuffer.get(i));
    }
    return sb.toString();
  }

  public CodeTreeNode getTree() {
    return tree;
  }

  public CodeTreeNode getZeroNode() {
    return ZERO_NODE;
  }

  public boolean contains(int value) {
    return nodeCache[((byte) value) & 0xff] != null;
  }

}

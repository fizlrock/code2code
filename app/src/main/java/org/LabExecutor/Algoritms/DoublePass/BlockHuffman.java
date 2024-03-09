
package org.LabExecutor.Algoritms.DoublePass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.LabExecutor.Algoritms.LineMetrics;

public class BlockHuffman {

  public static Task1Report task1(String line, int block_size) {

    var line_metrics = new LineMetrics(line);
    var LP_letters = line_metrics.getAlphabetProbability().entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey().toString(), Entry::getValue));
    var new_alphabet = genWordbook(LP_letters.keySet(), block_size);
    var LP_blocked = calcBlocksProbability(new_alphabet, LP_letters);

    Node t1, t2;
    t1 = buildHuffmanTree(LP_letters);
    t2 = buildHuffmanTree(LP_blocked);
    Map<String, String> c1, c2;
    c1 = buildHuffmanTable(t1);
    c2 = buildHuffmanTable(t2);
    String dot1, dot2;
    dot1 = formatTree(t1);
    dot2 = formatTree(t2);

    double bbs1, bbs2;
    bbs1 = calcBitBySymbol(LP_letters, c1);
    bbs2 = calcBitBySymbol(LP_blocked, c2);
    bbs2 /= 2;

    return new Task1Report(line, block_size, line_metrics, LP_blocked, c1, c2, bbs1, bbs2, dot1, dot2);
  }

  public static record Task1Report(
      String inputLine,
      int blockSize,
      LineMetrics lineMetrics,
      Map<String, Double> blockp,
      Map<String, String> huffLetter,
      Map<String, String> huffBlock,
      double BBSLetter,
      double BBSBlock,
      String graphLetter,
      String graphBlock) {

  }

  public static Map<String, Double> calcBlocksProbability(Set<String> blocks, Map<String, Double> letters_prob) {
    Map<String, Double> newprob = new HashMap<>();

    for (var block : blocks) {
      var letters = block.toCharArray();
      double p = 1;
      for (int i = 0; i < letters.length; i++) {
        p *= letters_prob.get(String.valueOf(letters[i]));
      }
      newprob.put(block, p);
    }

    return newprob;
  }

  public static Set<String> genWordbook(Set<String> sourse_letters, int word_size) {
    String joined_letters = sourse_letters.stream().collect(Collectors.joining());
    Set<String> result = new HashSet<>();
    char[] letters = joined_letters.toCharArray();

    Consumer<String> generator = new Consumer<String>() {
      @Override
      public void accept(String base) {
        if (base.length() >= word_size)
          result.add(base);
        else
          for (char c : letters)
            this.accept(base + c);
      }
    };
    generator.accept("");
    return result;
  }

  public static double calcBitBySymbol(
      Map<String, Double> prob,
      Map<String, String> code_table) {

    return prob.entrySet().stream()
        .map(
            e -> {
              double p = e.getValue();
              String l = e.getKey();
              int length = code_table.get(l).length();
              return length * p;
            })
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  static class Node {

    public Node(double probability, String letter) {
      this.probability = probability;
      this.sign = letter;
    }

    public Node(double appriority) {
      this.probability = appriority;
    }

    public final double probability;

    public double getProbability() {
      return probability;
    }

    Node left, right;
    String sign;

    public String getSign() {
      return sign;
    }
  }

  /**
   * Представляет бинарное дерево на языке dot
   * 
   * @param root
   * @return
   */
  public static String formatTree(Node root) {
    StringJoiner sj = new StringJoiner("\n");

    sj.add("digraph BST {");

    Consumer<Node> formater = new Consumer<BlockHuffman.Node>() {
      @Override
      public void accept(Node n) {

        if (n.sign == null) {
          sj.add(String.format("%d [label=\"%.2f\"]", n.hashCode(), n.probability));
          sj.add(String.format("%d -> %d", n.hashCode(), n.left.hashCode()));
          sj.add(String.format("%d -> %d", n.hashCode(), n.right.hashCode()));
          this.accept(n.left);
          this.accept(n.right);
        } else
          sj.add(String.format("%d [label=\"%s %.2f\", style=filled, fillcolor=\"#78f7c2\"]", n.hashCode(), n.sign,
              n.probability));
      }
    };
    formater.accept(root);

    sj.add("}");
    return sj.toString();
  }

  public static Map<String, String> buildHuffmanTable(Node root) {
    var table = new HashMap<String, String>();

    BiConsumer<Node, String> table_builder = new BiConsumer<BlockHuffman.Node, String>() {
      @Override
      public void accept(Node n, String buffer) {
        if (n.sign != null)
          table.put(n.sign, buffer);
        else {
          this.accept(n.left, buffer + "1");
          this.accept(n.right, buffer + "0");
        }
      }
    };
    table_builder.accept(root, "");

    return table;
  }

  /**
   * Метод строит бинарное дерево для кодирования Хаффмана
   * 
   * @param line
   * @return
   */
  public static Node buildHuffmanTree(Map<String, Double> letter_probability) {
    Comparator<Node> c = Comparator.comparing(Node::getProbability);//.thenComparing(Node::getSign);
    List<Node> nodes = letter_probability.entrySet().stream()
        .map(e -> new Node(e.getValue(), e.getKey()))
        .sorted(c)
        .collect(Collectors.toList());

    int i = 0;
    while (nodes.size() > 1) {
      Node n1, n2, s;
      n1 = nodes.removeFirst();
      n2 = nodes.removeFirst();
      s = new Node(n1.probability + n2.probability);
      s.right = n1;
      s.left = n2;
      nodes.add(s);
      nodes.sort(c);
    }
    var root = nodes.removeFirst();
    return root;
  }
}

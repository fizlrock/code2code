
package org.LabExecutor.Algoritms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class LineMetrics {

  public LineMetrics(String line) {
    this.line = line;
    calcChances();

    alphabet =  alphabetCount.entrySet().stream()
    .sorted(Comparator.comparing(e -> e.getValue()))
    .map(Entry::getKey)
    .collect(Collectors.toList());
  }

  private String line;

  private List<Character> alphabet;

  private Map<Character, Double> alphabetProbability;

  private Map<Character, Integer> alphabetCount;

  private double entropy;

  private void calcChances() {

    Map<Character, Double> probability = new HashMap<>();
    Map<Character, Integer> count = new HashMap<>();

    for (int i = 0; i < line.length(); i++) {
      Character ch = line.charAt(i);
      Integer counter = count.get(ch);
      if (counter == null)
        counter = Integer.valueOf(0);
      count.put(ch, ++counter);
    }

    count.entrySet().stream().forEach(e -> {
      probability.put(e.getKey(), ((double) e.getValue()) / line.length());
    });

    alphabetCount = count;
    alphabetProbability = probability;

  }

  public String getLine() {
    return line;
  }

  /**
   * Алфавит переданной строки в порядке не возростания частот
   *
   */
  public List<Character> getAlphabet() {
    return alphabet;
  }

  /**
   * Словарь буква-вероятность
   *
   */
  public Map<Character, Double> getAlphabetProbability() {
    return alphabetProbability;
  }

  /**
   * Словарь буква-число вхождений в переданную строку
   *
   */
  public Map<Character, Integer> getAlphabetCount() {
    return alphabetCount;
  }

  /**
   * Энтропия алфавита
   *
   */
  public double getEntropy() {
    return entropy;
  }

}


package org.LabExecutor.Algoritms.SinglePass.LZXX;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.*;

public class LZ78 {

  public static record Task33Step(
      String phrase,
      String code,
      int dictPos) {
  }

  public static record Task53Step(
      String code,
      List<String> dict,
      String output) {
  }

  public static record Task33Report(
      String inputLine,
      String result,
      List<LZ78.Task33Step> steps,
      String log) {
  }

  public static record Task53Report(
      String inputLine,
      String result,
      List<LZ78.Task53Step> steps) {
  }

  private static class Token {
    private static final Pattern pattern = Pattern.compile("^(?<number>\\d+)'(?<letter>.)'$");

    public final Character letter;
    public final int number;

    Token(String line) {
      var matcher = pattern.matcher(line);
      if (!matcher.find())
        throw new IllegalArgumentException("Недопустимый формат токена: " + line);
      number = Integer.parseInt(matcher.group("number"));
      letter = matcher.group("letter").charAt(0);
    }

    public String getStringLetter() {
      return String.valueOf(letter);
    }

    @Override
    public String toString() {
      return String.format("%d'%s'", number, letter);
    }
  }

  public static Task33Report code(String line, int dict_size) {
    List<String> dict = new ArrayList<>();
    List<Task33Step> steps = new ArrayList<>();
    StringJoiner logs = new StringJoiner("\n");
    StringJoiner result = new StringJoiner(" ");
    dict.add("");
    steps.add(new Task33Step("", "", 0));

    int last_index = 0;
    int readed = 0;
    int size = 1;

    String part;
    int index;

    while (readed + size <= line.length()) {

      part = line.substring(readed, readed + size);
      index = dict.indexOf(part);
      logs.add(String.format("readed: %d, size: %d, buffer: %s, dict: %s", readed, size, part, dict.toString()));

      if (index > 0) {
        last_index = index;
        size++;
      } else {
        String code = String.format("%d'%s'", last_index, line.charAt(readed + size - 1));
        steps.add(new Task33Step(part, code, dict.size()));
        logs.add("В выходной поток: " + code);
        result.add(code);
        dict.add(part);
        readed += size;
        size = 1;
        last_index = 0;
      }

    }
    return new Task33Report(line, result.toString(), steps, logs.toString());
  }

  public static Task53Report decode(String line) {
    var parts = Stream.of(line.split("\\]|\\["))
        .filter(s -> !s.isBlank())
        .map(Token::new)
        .collect(Collectors.toList());

    List<Task53Step> steps = new ArrayList<>();
    List<String> dict = new ArrayList<>();
    String buffer = "";
    String result = "";

    dict.add("");
    steps.add(new Task53Step("", new ArrayList<String>(dict), ""));

    for (Token token : parts) {
      buffer = dict.get(token.number) + token.letter;
      result += buffer;
      dict.add(buffer);
      steps.add(new Task53Step(token.toString(), new ArrayList<String>(dict), buffer));
    }
    return new Task53Report(line, result, steps);
  }

}

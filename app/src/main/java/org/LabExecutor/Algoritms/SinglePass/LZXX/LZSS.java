
package org.LabExecutor.Algoritms.SinglePass.LZXX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LZSS
 */
public class LZSS {

  public static record Task32Report(String input_line, String result, List<CodeStep> steps, int size) {
  }

  public static record Task53Report(String input_line, String result, List<DecodeStep> steps) {
  }

  public static record DecodeStep(Character[] dict, Token token, String out) {
  }

  public static record CodeStep(char[] dict, char[] buffer, Token token) {
  }

  public static class Token {
    public final Character letter;

    public final int index, length, size;
    private static final Pattern pattern1 = Pattern.compile("^0'(?<letter>.)'$");
    private static final Pattern pattern2 = Pattern.compile("^1<(?<index>\\d),(?<length>\\d)>$");

    public static Token parseToken(String line) {
      var matcher1 = pattern1.matcher(line);
      var matcher2 = pattern2.matcher(line);
      if (matcher1.find())
        return new Token(matcher1.group("letter").charAt(0));
      else if (matcher2.find()) {
        int index = Integer.parseInt(matcher2.group("index"));
        int length = Integer.parseInt(matcher2.group("length"));
        return new Token(index, length);
      } else
        throw new IllegalArgumentException("Недопустимый формат токена: " + line);
    }

    public Token(Character letter) {
      this.letter = letter;
      index = 0;
      length = 1;
      size = 9;
    }

    public Token(int index, int length) {
      letter = null;
      this.index = index;
      this.length = length;
      size = 7;
    }

    @Override
    public String toString() {
      if (letter != null)
        return String.format("0'%s'", letter);
      return String.format("1<%d,%d>", index, length);
    }

    public int getSize() {
      return size;
    }

  }

  private static String genEmptyWindow(int window_size) {
    char[] none_symbols = new char[window_size];
    Arrays.fill(none_symbols, ' ');
    return String.copyValueOf(none_symbols);
  }

  private String line;

  private String window;

  private String buffer, dict;

  private int dictSize, bufferSize;

  private Task32Report report;

  private LZSS(String line, int dict_size, int buffer_size) {
    this.line = line;
    dictSize = dict_size;
    bufferSize = buffer_size;
    window = genEmptyWindow(dict_size + buffer_size);
    code();

  }

  public static Task32Report code(String line, int dicts, int buffs) {
    return new LZSS(line, dicts, buffs).report;
  }

  public static Task53Report decode(String line, int dict_size) {

    // Подготовка отчета
    List<DecodeStep> steps = new ArrayList<>();
    StringBuilder result = new StringBuilder();

    // Парсинг токенов
    var raw_tokens = line.split("\\[|\\]");
    var tokens = Stream.of(raw_tokens)
        .filter(s -> !s.isBlank())
        .map(Token::parseToken)
        .collect(Collectors.toList());

    // Подготовка словаря
    List<Character> dict = new LinkedList<>();
    for (int i = 0; i < dict_size; i++)
      dict.add('\00');

    for (Token t : tokens) {
      String output = "";
      if (t.letter != null) {
        dict.add(t.letter);
        dict.removeFirst();
        output = String.valueOf(t.letter);
      } else {
        StringBuilder sb = new StringBuilder();
        dict.subList(t.index, t.index + t.length).forEach(sb::append);
        output = sb.toString();
        for (int i = 0; i < output.length(); i++) {
          dict.add(output.charAt(i));
          dict.removeFirst();
        }
      }

      Character[] dict_clone = new Character[dict_size];
      dict.toArray(dict_clone);
      steps.add(new DecodeStep(dict_clone, t, output));
      result.append(output);
    }
    return new Task53Report(line, result.toString(), steps);
  }

  void code() {
    if (line.length() < bufferSize)
      throw new IllegalArgumentException("Кодируемая строка не может быть меньше размера буффера");

    List<CodeStep> steps = new ArrayList<>();

    int readed = bufferSize; // Сколько символов уже считано из строки
    // Изначально запоняем буффер
    window += line.substring(0, readed);
    resizeWindow();
    boolean lineEnd = false;

    while (bufferSize > 0) {
      var token = matchToken();
      // Дописываем к окну символы из строки
      int end_index = readed + token.length; // индекс по которому будем обрезать строку

      if (line.length() < end_index) {
        // Если символы кончаются, дописываем оставшиеся, и далее уменьшаем буффер
        int delta = end_index - line.length();

        if (!lineEnd) {
          window += line.substring(readed, end_index - delta);
          bufferSize -= delta;
          lineEnd = true;
        } else
          bufferSize -= token.length;

      } else
        window += line.substring(readed, end_index);
      steps.add(new CodeStep(dict.toCharArray(), buffer.toCharArray(), token));
      readed += token.length;
      resizeWindow();

    }
    String result = steps.stream().map(s -> s.token().toString()).collect(Collectors.joining(" "));
    int size = steps.stream().map(CodeStep::token).map(Token::getSize).mapToInt(Integer::valueOf).sum();

    report = new Task32Report(line, result, steps, size);

  }

  private Token matchToken() {

    Token result = null;
    // Перебираем префиксные подстроки буфера в порядке уменьшения длины.
    for (int i = bufferSize; i > 0; i--) {
      String checked_sub_buffer = window.substring(dictSize, dictSize + i);
      int index = dict.indexOf(checked_sub_buffer);
      if (index >= 0) {
        result = new Token(index, i);
        break;
      }
    }
    if (result == null)
      result = new Token(buffer.charAt(0));

    return result;
  }

  private void resizeWindow() {
    if (window.length() > dictSize + bufferSize)
      window = window.substring(window.length() - (dictSize + bufferSize));
    buffer = window.substring(dictSize);
    dict = window.substring(0, dictSize);
  }
}

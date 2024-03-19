
package org.LabExecutor.Algoritms.SinglePass.LZXX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * LZSS
 */
public class LZSS {

  static record Report(String input_line, String result, List<CodeStep> steps, int size) {
  }

  static record CodeStep(char[] dict, char[] buffer, Token token) {
  }

  public static class Token {
    public final Character letter;

    public final int index, length, size;

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
    Arrays.fill(none_symbols, '\00');
    return String.copyValueOf(none_symbols);
  }

  private String line;

  private String window;

  private String buffer, dict;

  private int dictSize, bufferSize;

  private Report report;

  private LZSS(String line, int dict_size, int buffer_size) {
    this.line = line;
    dictSize = dict_size;
    bufferSize = buffer_size;
    window = genEmptyWindow(dict_size + buffer_size);
    code();

  }

  public static Report code(String line, int dicts, int buffs) {
    return new LZSS(line, dicts, buffs).report;
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
      readed += token.length;
      resizeWindow();

      steps.add(new CodeStep(dict.toCharArray(), buffer.toCharArray(), token));
    }
    String result = steps.stream().map(s -> s.token().toString()).collect(Collectors.joining(" "));
    int size = steps.stream().map(CodeStep::token).map(Token::getSize).mapToInt(Integer::valueOf).sum();

    report = new Report(line, result, steps, size);

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

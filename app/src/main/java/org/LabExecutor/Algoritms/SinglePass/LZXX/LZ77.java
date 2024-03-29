
package org.LabExecutor.Algoritms.SinglePass.LZXX;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LZ77 Compression/Decompression algorithms.
 * 
 * @see <a href=
 *      "https://github.com/TheAlgorithms/Python/blob/master/compression/lz77.py">Python
 *      implementation</a>
 * @author evosome
 */
public class LZ77 {

  private final int windowSize;
  private final int bufferSize;

  public LZ77(int windowSize, int bufferSize) {
    this.windowSize = windowSize;
    this.bufferSize = bufferSize;
  }

  public Task31Report encode(String line) {
    String orig_line = line;

    Task31Report result = new Task31Report();
    result.setInputLine(line);

    // TODO: create LZCharBuffer class, representing character buffer instead using
    // this
    char[] chars = new char[windowSize];
    Arrays.fill(chars, '\00');
    String searchBuffer = String.copyValueOf(chars);

    while (!line.isEmpty()) {
      Token token = null;

      try {
        token = matchToken(line, searchBuffer, windowSize);
      } catch (Exception e) {
        System.out.printf("Ошибка кодирования LZ77: %s \n", orig_line);
        break;
      }

      result.addDictRow(searchBuffer);
      result.addBufferRow(line.substring(0, Math.min(line.length(), bufferSize)));
      result.addToken(token);

      searchBuffer += line.substring(0, token.getLength() + 1);
      if (searchBuffer.length() > windowSize) {
        searchBuffer = searchBuffer.substring(searchBuffer.length() - windowSize);
      }

      line = line.substring(token.getLength() + 1);
    }
    result.setResult(Stream.of(result.getTokensAsString()).collect(Collectors.joining(" ")));

    return result;
  }

  private Token matchToken(String text, String searchBuff, int searchBuffSize) {
    int length = 0, offset = 0, sbSize = searchBuff.length();

    if (!searchBuff.isEmpty() && text.length() > 1) {

      for (int i = 0; i < sbSize; i++) {
        if (searchBuff.charAt(i) != text.charAt(0))
          continue;

        int foundLength = matchMaxLengthOf(text, searchBuff, 0, i);
        if (foundLength > length) {
          offset = i;
          length = foundLength;
        }
      }

    }

    /*
     * This line makes work this algorithm for these strings:
     * ЛЯЛЯЛЯ_ЛЯЛЯ_ЯЛИК_МЯЛ
     * ОСЫ_ОСЫ_СЫПЬ_НАСЫПЬ
     * КУСКУС_ КУСАКА_СОБАКА
     * МУМУМУ_МУКА_МУРКА
     * ПЕС_ПЕСОК_СОКОЛ_СКОЛ
     * РАБ_РАБА_БАК_БАКЕН_БАК
     * ТАРА_ТАРТАР_ТАРЕЛКА_ЕЛКА
     *
     * It's kinda magic :)
     */
    if (length == text.length())
      length--;

    return new Token(offset, length, text.charAt(length));
  }

  public Task52Report decode(String tokenString) {
    List<DecodeStep> steps = new ArrayList<>();

    StringBuilder decodeResult = new StringBuilder();
    Token[] tokens = Token.fromString(tokenString);
    for (Token token : tokens) {

      for (int i = 0; i < token.getLength(); i++)
        decodeResult.append(decodeResult.charAt(decodeResult.length() - (windowSize - token.offset)));


      String out = decodeResult.toString().substring(decodeResult.length() - token.getLength());
      out += token.indicator;
      steps.add(new DecodeStep(getDict(decodeResult.toString()), token, out));
      decodeResult.append(token.getIndicator());
    }
    return new Task52Report(tokenString, decodeResult.toString(), steps);
  }

  public List<Character> getDict(String line) {
    String dictLine = "";
    if (line.length() > windowSize) {
      dictLine = line.substring(line.length() - windowSize, line.length());
    } else {
      dictLine = line;
      while (dictLine.length() < windowSize)
        dictLine = " " + dictLine;
    }
    var result = new ArrayList<Character>();
    for (char c : dictLine.toCharArray())
      result.add(c);
    return result;
  }

  public static record DecodeStep(List<Character> dict, Token token, String out) {
  }

  public static record Task52Report(String inputLine, String result, List<DecodeStep> steps) {
  }

  private int matchMaxLengthOf(
      String text,
      String window,
      int textIndex,
      int windowIndex) {

    if (text.length() <= textIndex || bufferSize - 1 <= textIndex || window.length() <= windowIndex)
      return 0;

    if (text.isEmpty() || text.charAt(textIndex) != window.charAt(windowIndex))
      return 0;

    return 1 + matchMaxLengthOf(
        text,
        window + text.charAt(textIndex),
        textIndex + 1,
        windowIndex + 1);

  }

  public class Task31Report {
    private String inputLine;
    private String result;
    private final List<String> dictRows;
    private final List<String> bufferRows;
    private final List<Token> tokens;

    public Task31Report() {
      dictRows = new ArrayList<>();
      bufferRows = new ArrayList<>();
      tokens = new ArrayList<>();
    }

    public void addDictRow(String row) {
      dictRows.add(row);
    }

    public void addBufferRow(String row) {
      bufferRows.add(row);
    }

    public void addToken(Token token) {
      tokens.add(token);
    }

    public String[] getBufferRows() {
      return bufferRows.toArray(String[]::new);
    }

    public String[] getDictRows() {
      return dictRows.toArray(String[]::new);
    }

    public String[] getTokensAsString() {
      return tokens
          .stream()
          .map(Token::toString)
          .toArray(String[]::new);
    }

    public String getInputLine() {
      return inputLine;
    }

    private void setInputLine(String inputLine) {
      this.inputLine = inputLine;
    }

    public String getResult() {
      return result;
    }

    private void setResult(String result) {
      this.result = result;
    }

    public List<Token> getTokens() {
      return tokens;
    }
  }

  public static class Token {

    private final int offset;
    private final int length;
    private final char indicator;

    private static final Pattern TOKEN_PATTERN = Pattern.compile("<(\\d),(\\d),(.)>");

    public Token(int offset, int length, char indicator) {
      this.offset = offset;
      this.length = length;
      this.indicator = indicator;
    }

    public static Token[] fromString(String tokenString) {
      return TOKEN_PATTERN
          .matcher(tokenString)
          .results()
          .map(matchResult -> new Token(
              Integer.parseInt(matchResult.group(1)),
              Integer.parseInt(matchResult.group(2)),
              matchResult.group(3).charAt(0)))
          .toArray(Token[]::new);
    }

    @Override
    public String toString() {
      return String.format("<%d,%d,%s>", offset, length, indicator);
    }

    public int getOffset() {
      return offset;
    }

    public int getLength() {
      return length;
    }

    public char getIndicator() {
      return indicator;
    }
  }

}

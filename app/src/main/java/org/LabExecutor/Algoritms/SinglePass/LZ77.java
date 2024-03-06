
package org.LabExecutor.Algoritms.SinglePass;

public class LZ77 {
  private LZ77() {
    // Приватный коструктор, ибо нехуй создавать объект класс, где только
    // статические методы.
  }

  public static CodeReport code(String line) {

    return new CodeReport(line, "001001010010");
  }

  public static DecodeReport decode(String line) {

    return new DecodeReport(line, "001001010010");
  }

  static record CodeReport(
      String input_line,
      String result) {
  }

  static record DecodeReport(
      String input_line,
      String result) {
  }

}

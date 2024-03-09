
package org.LabExecutor.LatexFormater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class GraphUtils {

  static final String pathToImages = "./doc_src/images/";

  /**
   * Метод компилирует граф на языке dot в картинку jpg и сохраняет на диске
   * 
   * @param dot
   * @return путь к файлу
   */
  public static String compileAndSaveGraph(String dot) {
    File temp_file;
    try {
      temp_file = File.createTempFile(".dot", null);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    writeString(temp_file, dot);

    File jpg = new File(pathToImages + "/" + dot.hashCode() + ".jpg");
    try {
      var proc = Runtime.getRuntime().exec(new String[] { "dot", "-Tjpg", temp_file.getPath().toString() });
      var in = proc.getInputStream();

      byte[] buffer = new byte[100];
      int readed = -1;

      var fos = new FileOutputStream(jpg);

      while ((readed = in.read(buffer)) > 0)
        fos.write(buffer, 0, readed);

      fos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return jpg.toString();

  }

  private static void writeString(Path p, String line) {
    writeString(p.toFile(), line);
  }

  private static void writeString(File f, String line) {
    try (PrintWriter pw = new PrintWriter(f)) {
      pw.write(line);
      pw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}

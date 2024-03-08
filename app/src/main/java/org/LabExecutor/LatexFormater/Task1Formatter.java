
package org.LabExecutor.LatexFormater;

import java.util.Map;
import java.util.stream.Collectors;

import org.LabExecutor.Algoritms.DoublePass.BlockHuffman;
import  static org.LabExecutor.Algoritms.DoublePass.BlockHuffman;

public class Task1Formatter {


  public static String format(){
    
  }

  public static String formatLatex(Map<?, ?> map) {
    return map.entrySet().stream()
        .map(e -> {
          return String.format("%s & %s", e.getKey(), e.getValue());
        })
        .collect(Collectors.joining("\\\\\n"));
  }
}

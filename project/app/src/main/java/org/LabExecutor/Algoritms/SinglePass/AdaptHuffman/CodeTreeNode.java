package org.LabExecutor.Algoritms.SinglePass.AdaptHuffman;

/**
 * Класс для представления узла кодового дерева
 */
public class CodeTreeNode implements Comparable<CodeTreeNode> {

  Integer content;
  int weight;
  CodeTreeNode left;
  CodeTreeNode right;
  CodeTreeNode parent;

  public CodeTreeNode(Integer content, int weight) {
    this.content = content;
    this.weight = weight;
  }

  public CodeTreeNode(Integer content, int weight, CodeTreeNode left, CodeTreeNode right, CodeTreeNode parent) {
    this.content = content;
    this.weight = weight;
    this.left = left;
    this.right = right;
    this.parent = parent;
  }

  @Override
  public int compareTo(CodeTreeNode o) {
    return o.weight - weight;
  }

  public int updateWeights() {
    if (content != null) {
      return weight;
    } else {
      int w = 0;
      if (right != null) {
        w += right.updateWeights();
      }
      if (left != null) {
        w += left.updateWeights();
      }
      weight = w;
      return weight;
    }
  }

  @Override
  public int hashCode() {
    int result = 0;
    if (left == null)
      result = toString().hashCode();
    else
      result = left.hashCode()*2 + right.hashCode();
    if (result < 0)
      result = -result;

    return result;
  }

  @Override
  public String toString() {
    if (content == null & weight == 0)
      return "0/esc";
    else if (content == null) {
      return Integer.toString(weight);
    } else
      return String.format("%d/%s", weight, (char) (int) content);
  }

}

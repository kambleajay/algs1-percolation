import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Deque;
import java.util.ArrayDeque;

public class PercolationStats {

  private int n;
  private int trials;
  private int[] numberOfOpenSites;
  private double[] thresholds;


  public PercolationStats(int n, int trials) {
    this.n = n;
    this.trials = trials;
    this.numberOfOpenSites = new int[trials];
    thresholds = new double[trials];
  }

  private static int toInt(String s) {
    return Integer.parseInt(s);
  }

  private List<Map<String, Integer>> buildAllSites() {
    List<Map<String, Integer>> allSites = new ArrayList<>();
    for (int row = 1; row <= n; row++) {
      for (int col = 1; col <= n; col++) {
        Map<String, Integer> m = new HashMap<>();
        m.put("row", row);
        m.put("col", col);
        allSites.add(m);
      }
    }
    return allSites;
  }

  private void calculateThresholds() {
    for (int i = 0; i < trials; i++) {
      thresholds[i] = numberOfOpenSites[i] / (double) (n * n);
    }
  }

  private void execute() {
    for (int i = 0; i < trials; i++) {
      Percolation p = new Percolation(n);
      List<Map<String, Integer>> allSites = buildAllSites();
      Collections.shuffle(allSites);
      Deque<Map<String, Integer>> shuffledSites = new ArrayDeque<>(allSites);
      do {
        Map<String, Integer> nextSite = shuffledSites.pop();
        int row = nextSite.get("row");
        int col = nextSite.get("col");
        p.open(row, col);
      } while (!p.percolates());
      this.numberOfOpenSites[i] = p.numberOfOpenSites();
      calculateThresholds();
    }
  }

  public double mean() {
    return StdStats.mean(thresholds);
  }

  public double stddev() {
    return StdStats.stddev(thresholds);
  }

  public double confidenceLo() {
    return mean() - ((1.96 * stddev()) / (Math.sqrt(trials)));
  }

  public double confidenceHi() {
    return mean() + ((1.96 * stddev()) / (Math.sqrt(trials)));
  }

  private String formatForPrint(String name, double value) {
    return String.format("%-15s = %10.10f", name, value);
  }

  private String getStats() {
    StringBuilder s = new StringBuilder();
    s.append(formatForPrint("mean", mean()));
    s.append("\n");
    s.append(formatForPrint("stddev", stddev()));
    s.append("\n");
    s.append(String.format("%-15s = [%10.10f, %10.10f]", "95% confidence interval", confidenceLo(), confidenceHi()));
    return s.toString();
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("required: n and trials is required.");
    }
    PercolationStats ps = new PercolationStats(toInt(args[0]), toInt(args[1]));
    ps.execute();
    System.out.println(ps.getStats());
  }
}

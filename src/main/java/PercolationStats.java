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
  private double mean;
  private double stddev;
  private double confidenceLo;
  private double confidenceHi;


  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("both n and trails should be > 0");
    }
    this.n = n;
    this.trials = trials;
    execute();
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

  private void execute() {
    int[] numberOfOpenSites = new int[trials];
    double[] thresholds = new double[trials];
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
      numberOfOpenSites[i] = p.numberOfOpenSites();
      thresholds[i] = numberOfOpenSites[i] / (double) (n * n);
    }
    this.mean = StdStats.mean(thresholds);
    this.stddev = StdStats.stddev(thresholds);
    this.confidenceLo = this.mean - ((1.96 * stddev()) / (Math.sqrt(this.trials)));
    this.confidenceHi = this.mean + ((1.96 * stddev()) / (Math.sqrt(this.trials)));
  }

  public double mean() {
    return this.mean;
  }

  public double stddev() {
    return this.stddev;
  }

  public double confidenceLo() {
    return this.confidenceLo;
  }

  public double confidenceHi() {
    return this.confidenceHi;
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
    int n = toInt(args[0]);
    int trials = toInt(args[1]);
    PercolationStats ps = new PercolationStats(n, trials);
    System.out.println(ps.getStats());
  }
}

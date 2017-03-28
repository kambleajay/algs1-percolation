import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

public class Percolation {

  private enum Status { BLOCKED, OPEN, CONNECTED_TO_TOP, CONNECTED_TO_BOTTOM }

  private int dim;
  private int openSitesCount = 0;
  private WeightedQuickUnionUF wqu;
  private List<Set<Status>> statusOfSites;
  private boolean percolates = false;

  // create dim-by-dim grid, with all sites blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0!");
    } else {
      this.dim = n;
      this.statusOfSites = new ArrayList<>();
      setInitialStatus();
      wqu = new WeightedQuickUnionUF(n * n);
    }
  }

  private void setInitialStatus() {
    for (int i = 0; i < (dim * dim); i++) {
      Set<Percolation.Status> statusToAdd = EnumSet.of(Percolation.Status.BLOCKED);
      if (i >= 0 && i < dim) {
        statusToAdd.add(Percolation.Status.CONNECTED_TO_TOP);
      }
      if (i >= (dim * (dim - 1)) && i < (dim * dim)) {
        statusToAdd.add(Percolation.Status.CONNECTED_TO_BOTTOM);
      }
      this.statusOfSites.add(i, statusToAdd);
    }
  }

  private int to1D(int row, int col) {
    return (row - 1) * dim + (col - 1);
  }

  private boolean isInvalidIndex(int n) {
    return n <= 0 || n > dim;
  }

  private boolean isValidIndex(int n) {
    return !isInvalidIndex(n);
  }

  private boolean isValidSite(int row, int col) {
    return isValidIndex(row) && isValidIndex(col);
  }

  private void throwIndexOutOfBounds(String errorFor) {
    throw new IndexOutOfBoundsException(errorFor + " must be between 1 and " + dim);
  }

  private void validateIndices(int row, int col) {
    if (isInvalidIndex(row)) {
      throwIndexOutOfBounds("row");
    } else if (isInvalidIndex(col)) {
      throwIndexOutOfBounds("column");
    }
  }

  private void markSiteAsOpen(int row, int col) {
    statusOfSites.get(to1D(row, col)).add(Percolation.Status.OPEN);
  }

  private List<Integer> listOf(int row, int col) {
    return new ArrayList<>(Arrays.asList(row, col));
  }

  private void connectToAdjacentOpenSites(int row, int col) {
    List<List<Integer>> possibleNeighbors = new ArrayList<>(Arrays.asList(
        listOf(row - 1, col),
        listOf(row, col + 1),
        listOf(row + 1, col),
        listOf(row, col - 1)
    ));

    List<List<Integer>> validNeighbors = new ArrayList<>();
    for (List<Integer> neighbor : possibleNeighbors) {
      int neighborRow = neighbor.get(0);
      int neighborCol = neighbor.get(1);
      if (isValidSite(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)) {
        validNeighbors.add(new ArrayList<>(listOf(neighborRow, neighborCol)));
      }
    }

    Set<Percolation.Status> statusForCurrentSite = EnumSet.noneOf(Percolation.Status.class);
    for (List<Integer> validNeighbor : validNeighbors) {
      int validNeighborRow = validNeighbor.get(0);
      int validNeighborCol = validNeighbor.get(1);
      int rootOfThisNeighbor = wqu.find(to1D(validNeighborRow, validNeighborCol));
      Set<Percolation.Status> statusOfNeighbor = statusOfSites.get(rootOfThisNeighbor);
      statusForCurrentSite.addAll(statusOfNeighbor);
      wqu.union(to1D(row, col), to1D(validNeighborRow, validNeighborCol));
    }

    int rootOfCurrentSite = wqu.find(to1D(row, col));
    statusOfSites.get(rootOfCurrentSite).addAll(statusForCurrentSite);
    statusOfSites.get(rootOfCurrentSite).addAll(statusOfSites.get(to1D(row, col)));
    Set<Status> percolationCriteria = EnumSet.of(Status.CONNECTED_TO_TOP, Status.CONNECTED_TO_BOTTOM);
    if (statusOfSites.get(rootOfCurrentSite).containsAll(percolationCriteria)) {
      this.percolates = true;
    }
  }

  // open site (row, col) if it is not open already
  public void open(int row, int col) {
    // wqu - union, upto 4 calls
    validateIndices(row, col);
    if (notOpen(row, col)) {
      markSiteAsOpen(row, col);
      openSitesCount++;
      connectToAdjacentOpenSites(row, col);
    }
  }

  private boolean notOpen(int row, int col) {
    return !isOpen(row, col);
  }

  // is site (row, col) open?
  public boolean isOpen(int row, int col) {
    validateIndices(row, col);
    return statusOfSites.get(to1D(row, col)).contains(Percolation.Status.OPEN);
  }

  // is site (row, col) full?
  public boolean isFull(int row, int col) {
    validateIndices(row, col);
    int root = wqu.find(to1D(row, col));
    return statusOfSites.get(root).contains(Percolation.Status.CONNECTED_TO_TOP) && isOpen(row, col);
  }

  // number of open sites
  public int numberOfOpenSites() {
    return openSitesCount;
  }

  // does the system percolate?
  public boolean percolates() {
    return this.percolates;
  }

  // test client (optional)
  public static void main(String[] args) {

  }

}

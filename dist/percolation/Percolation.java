import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Percolation {

  private static final int VIRTUAL_TOP_SITE = 0;
  private static final int OPEN = 1;

  private int n;
  private int[][] sites;
  private int openSitesCount = 0;
  private WeightedQuickUnionUF wqu;
  private int virtualBottomSite;

  // create n-by-n grid, with all sites blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0!");
    } else {
      this.n = n;
      sites = new int[n][n];
      wqu = new WeightedQuickUnionUF(n * n + 2);
      virtualBottomSite = (n * n) + 1;
      connectVirtualTopSite(n);
      connectVirtualBottomSite(n);
    }
  }

  private void connectVirtualTopSite(int dim) {
    for (int i = 1; i <= dim; i++) {
      wqu.union(VIRTUAL_TOP_SITE, i);
    }
  }

  private void connectVirtualBottomSite(int dim) {
    int startIndex = (dim * (dim - 1)) + 1;
    int endIndex = (startIndex + dim) - 1;
    for (int i = startIndex; i <= endIndex; i++) {
      wqu.union(i, virtualBottomSite);
    }
  }

  private int to1D(int row, int col) {
    return (row - 1) * n + (col - 1) + 1;
  }

  private boolean isInvalidIndex(int n) {
    return n <= 0 || n > this.n;
  }

  private boolean isValidIndex(int n) {
    return !isInvalidIndex(n);
  }

  private boolean isValidSite(int row, int col) {
    return isValidIndex(row) && isValidIndex(col);
  }

  private void throwIndexOutOfBounds(String errorFor) {
    throw new IndexOutOfBoundsException(errorFor + " must be between 1 and " + n);
  }

  private void validateIndices(int row, int col) {
    if (isInvalidIndex(row)) {
      throwIndexOutOfBounds("row");
    } else if (isInvalidIndex(col)) {
      throwIndexOutOfBounds("column");
    }
  }

  private int zeroBased(int index) {
    return index - 1;
  }

  private void markSiteAsOpen(int row, int col) {
    sites[zeroBased(row)][zeroBased(col)] = OPEN;
  }

  private void connectToNeighborIfOpen(int currentRow, int currentCol, int neighborRow, int neighborCol) {
    if (isValidSite(neighborRow, neighborCol) && isOpen(neighborRow, neighborCol)) {
      wqu.union(to1D(currentRow, currentCol), to1D(neighborRow, neighborCol));
    }
  }

  private void connectToAdjacentOpenSites(int row, int col) {
    int[][] possibleNeighbors = new int[][]{
        new int[]{row - 1, col},
        new int[]{row, col + 1},
        new int[]{row + 1, col},
        new int[]{row, col - 1}
    };
    for (int[] neighbor : possibleNeighbors) {
      connectToNeighborIfOpen(row, col, neighbor[0], neighbor[1]);
    }
  }

  // open site (row, col) if it is not open already
  public void open(int row, int col) {
    // wqu - union, upto 4 calls
    validateIndices(row, col);
    if(notOpen(row, col)) {
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
    return sites[zeroBased(row)][zeroBased(col)] == OPEN;
  }

  // is site (row, col) full?
  public boolean isFull(int row, int col) {
    validateIndices(row, col);
    return wqu.connected(VIRTUAL_TOP_SITE, to1D(row, col)) && isOpen(row, col);
  }

  // number of open sites
  public int numberOfOpenSites() {
    return openSitesCount;
  }

  // does the system percolate?
  public boolean percolates() {
    System.out.println(ReflectionToStringBuilder.toString(wqu));
    if(n == 1) {
      return wqu.connected(VIRTUAL_TOP_SITE, virtualBottomSite) && isOpen(1, 1);
    } else {
      return wqu.connected(VIRTUAL_TOP_SITE, virtualBottomSite);
    }
  }

  // test client (optional)
  public static void main(String[] args) {

  }

}

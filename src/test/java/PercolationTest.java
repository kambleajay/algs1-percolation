import org.junit.Test;

import static org.junit.Assert.*;

public class PercolationTest {

  @Test(expected = IllegalArgumentException.class)
  public void testRejectLessThanZeroSize() {
    new Percolation(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRejectZeroSize() {
    new Percolation(0);
  }

  @Test
  public void testNumberOfOpenSites() {
    Percolation p = new Percolation(4);
    assertEquals(p.numberOfOpenSites(), 0);
    p.open(2, 2);
    assertEquals(p.numberOfOpenSites(), 1);
    p.open(3, 3);
    assertEquals(p.numberOfOpenSites(), 2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsZeroAsRow() {
    Percolation p = new Percolation(1);
    p.open(0, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsLessThanZeroAsRow() {
    Percolation p = new Percolation(1);
    p.open(-1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsGreatherThanSizeAsRow() {
    Percolation p = new Percolation(1);
    p.open(10, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsZeroAsColumn() {
    Percolation p = new Percolation(1);
    p.open(1, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsLessThanZeroAsColumn() {
    Percolation p = new Percolation(1);
    p.open(1, -1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testOpenRejectsGreatherThanSizeAsColumn() {
    Percolation p = new Percolation(1);
    p.open(1, 10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsZeroAsRow() {
    Percolation p = new Percolation(1);
    p.isOpen(0, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsLessThanZeroAsRow() {
    Percolation p = new Percolation(1);
    p.isOpen(-1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsGreatherThanSizeAsRow() {
    Percolation p = new Percolation(1);
    p.isOpen(10, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsZeroAsColumn() {
    Percolation p = new Percolation(1);
    p.isOpen(1, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsLessThanZeroAsColumn() {
    Percolation p = new Percolation(1);
    p.isOpen(1, -1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIsOpenRejectsGreatherThanSizeAsColumn() {
    Percolation p = new Percolation(1);
    p.isOpen(1, 10);
  }

  @Test
  public void testIsOpenRespondsYesForOpenSites() {
    Percolation p = new Percolation(2);
    p.open(1, 2);
    assertTrue(p.isOpen(1, 2));
    p.open(1, 1);
    assertTrue(p.isOpen(1, 1));
    p.open(2,2);
    assertTrue(p.isOpen(2, 2));
  }

  @Test
  public void testIsOpenRespondsNoForBlockedSites() {
    Percolation p = new Percolation(2);
    p.open(1, 2);
    assertFalse(p.isOpen(1, 1));
  }

  @Test
  public void testIsFullRespondsYesIfSiteConnectedToTop() {
    Percolation p = new Percolation(3);
    p.open(1, 3);
    p.open(2, 3);
    assertTrue(p.isFull(1, 3));
    assertTrue(p.isFull(2, 3));
  }

  @Test
  public void testIsFullRespondsNoIfSiteNotConnectedToTop() {
    Percolation p = new Percolation(3);
    assertFalse(p.isFull(1, 3));
    assertFalse(p.isFull(2, 3));
  }

}
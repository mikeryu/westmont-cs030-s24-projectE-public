/**
 * Westmont College Spring 2024
 * CS 030 Project E
 *
 * @author Assistant Professor Mike Ryu mryu@westmont.edu
 */

import javax.swing.*;

public abstract class SorterWorker extends SwingWorker<Void, Void> {
  private int[] arrayToSort;
  private JComponent visualizerComp;
  private int renderDelayMillis;
  private Timer elapsedTimer;
  private long startTime;

  /**
   * Takes in an <code>int</code> array to sort it in ascending order. The sorting occurs in-place.
   *
   * @param numsToSort <code>int</code> array to sort in ascending order, in-place.
   */
  abstract public void sort(int[] numsToSort);

  /**
   * Call this method in the implementation of {@link SorterWorker#sort(int[])} to
   * render the current state of the array being sorted; it is recommended that this method
   * only be invoked only once in the implementation of {@link SorterWorker#sort(int[])}.
   */
  public void render() {
    if (renderDelayMillis > 0) {
      delay();
      publish();
    }
  }

  /**
   * Do not call this method in your code.
   */
  public void initialize(int[] arrayToSort, JComponent visualizerComponent,
                         int renderDelayMilliseconds, Timer elapsedTimer) {
    this.arrayToSort = arrayToSort;
    this.visualizerComp = visualizerComponent;
    this.renderDelayMillis = renderDelayMilliseconds;
    this.elapsedTimer = elapsedTimer;
  }

  /**
   * Do not call this method in your code.
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Do not manually call this method in your code.
   */
  @Override
  public Void doInBackground() {
    startTime = System.currentTimeMillis();
    elapsedTimer.start();

    System.out.printf("\n%s is starting the %s ... \n", this,
        renderDelayMillis == 0 ? "time measurement" : "animation");

    sort(arrayToSort);
    return null;
  }

  /**
   * Do not manually call this method in your code.
   */
  @Override
  public void process(java.util.List<Void> chunks) {
    if (renderDelayMillis > 0) {
      visualizerComp.repaint();
    }
  }

  /**
   * Do not manually call this method in your code.
   */
  @Override
  protected void done() {
    elapsedTimer.stop();

    System.out.printf("%s finished after %s.\n", this,
        isCancelled() ? "having being cancelled" : String.format("appx. %d milliseconds%s",
            System.currentTimeMillis() - startTime,
            renderDelayMillis == 0 ? "" : " (not meaningful)"));

    visualizerComp.repaint();
  }

  /**
   * This method should not be altered.
   */
  private void delay() {
    try {
      Thread.sleep(renderDelayMillis);
    } catch (InterruptedException e) {
      System.err.printf("Execution of %s was interrupted during a thread sleep.\n", this);
    }
  }
}

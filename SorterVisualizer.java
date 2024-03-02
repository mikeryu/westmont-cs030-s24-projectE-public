/**
 * Westmont College Spring 2024
 * CS 030 Project E
 *
 * @author Assistant Professor Mike Ryu mryu@westmont.edu
 */

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class SorterVisualizer extends JFrame {
  private static final int WINDOW_WIDTH = 1280;
  private static final int WINDOW_HEIGHT = 720;
  private static final int MAX_BAR_HEIGHT = (int) (WINDOW_HEIGHT * 0.9);

  private static final int ARRAY_SIZE_FIELD_WIDTH = 8;
  private static final int RENDER_DELAY_FIELD_WIDTH = 4;
  private static final int DEFAULT_ARRAY_SIZE = 100;

  private static final int TIMER_LABEL_WIDTH = 200;
  private static final int TIMER_LABEL_HEIGHT = 20;
  private static final int TIMER_UPDATE_PERIOD = 10;

  private final SortingPanel visualizationPanel = new SortingPanel();
  private SorterWorker sorterWorker;
  private int[] arrayToSort;

  public SorterVisualizer() {
    JPanel inputPanel = new JPanel();
    JLabel sizeLabel = new JLabel("Array Size:");
    JFormattedTextField sizeTextField = createIntegerField(ARRAY_SIZE_FIELD_WIDTH, DEFAULT_ARRAY_SIZE);

    JLabel delayLabel = new JLabel("Render Delay:");
    JFormattedTextField delayTextField = createIntegerField(RENDER_DELAY_FIELD_WIDTH, 0);

    JLabel animateLabel = new JLabel("Animate:");
    JButton selectionSortButton = new JButton("Selection Sort");
    JButton insertionSortButton = new JButton("Insertion Sort");
    JButton mergeSortButton = new JButton("Merge Sort");
    JButton quickSortButton = new JButton("Quick Sort");

    JLabel timerLabel = new JLabel("Elapsed: 0 ms");

    selectionSortButton.addActionListener(e -> {
      cancelVisualization();
      // sorterWorker = new SelectionSorterWorker();  TODO: Un-comment this line once your implementation is ready.
      startVisualization(sizeTextField, delayTextField, timerLabel);
    });

    insertionSortButton.addActionListener(e -> {
      cancelVisualization();
      // sorterWorker = new InsertionSorterWorker();  TODO: Un-comment this line once your implementation is ready.
      startVisualization(sizeTextField, delayTextField, timerLabel);
    });

    mergeSortButton.addActionListener(e -> {
      cancelVisualization();
      // sorterWorker = new MergeSorterWorker();  TODO: Un-comment this line once your implementation is ready.
      startVisualization(sizeTextField, delayTextField, timerLabel);
    });

    quickSortButton.addActionListener(e -> {
      cancelVisualization();
      // sorterWorker = new QuickSorterWorker();  TODO: Un-comment this line once your implementation is ready.
      startVisualization(sizeTextField, delayTextField, timerLabel);
    });

    timerLabel.setPreferredSize(new Dimension(TIMER_LABEL_WIDTH, TIMER_LABEL_HEIGHT));

    inputPanel.add(sizeLabel);
    inputPanel.add(sizeTextField);

    inputPanel.add(delayLabel);
    inputPanel.add(delayTextField);

    inputPanel.add(animateLabel);
    inputPanel.add(selectionSortButton);
    inputPanel.add(insertionSortButton);
    inputPanel.add(mergeSortButton);
    inputPanel.add(quickSortButton);

    inputPanel.add(timerLabel);

    setLayout(new BorderLayout());
    add(inputPanel, BorderLayout.NORTH);
    add(visualizationPanel, BorderLayout.CENTER);
    pack();

    setTitle("Sorting Algorithm Visualizer by Boaty McBoatface & Mike Ryu"); // TODO: Write your name.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    setResizable(false);
    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(SorterVisualizer::new);
  }

  private void cancelVisualization() {
    if (sorterWorker != null) sorterWorker.cancel(true);
  }

  private void startVisualization(JTextField sizeTF, JTextField delayTF, JLabel timerLabel) {
    int size = Math.max(Integer.parseInt(sizeTF.getText().replace(",", "")), 10);
    int delay = Math.max(Integer.parseInt(delayTF.getText().replace(",", "")), 0);
    boolean isAnimating = delay != 0;

    Timer timer = new Timer(TIMER_UPDATE_PERIOD, e -> {
      long elapsedTime = System.currentTimeMillis() - sorterWorker.getStartTime();
      if (isAnimating) {
        timerLabel.setText("Elapsed: (not meaningful)");
      } else {
        timerLabel.setText("Elapsed: " + new DecimalFormat("#,###").format(elapsedTime) + " ms");
      }
    });

    initializeArraysToSort(size);

    visualizationPanel.isErase = !isAnimating;
    visualizationPanel.repaint();

    sorterWorker.initialize(arrayToSort, visualizationPanel, delay, timer);
    sorterWorker.execute();
  }

  private void initializeArraysToSort(int size) {
    ArrayList<Integer> values = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      values.add((int) (((i + 1) / (double) size) * MAX_BAR_HEIGHT));
    }

    Collections.shuffle(values);
    arrayToSort = values.stream().mapToInt(i -> i).toArray();
  }

  private Color getColor(int height) {
    float hue = height / (float) WINDOW_HEIGHT;
    return Color.getHSBColor(hue, 1.0f, 1.0f);
  }

  private JFormattedTextField createIntegerField(int columns, int defaultValue) {
    NumberFormat format = NumberFormat.getIntegerInstance();
    JFormattedTextField textField = new JFormattedTextField(format);

    textField.setColumns(columns);
    textField.setValue(defaultValue);

    return textField;
  }

  private class SortingPanel extends JPanel {
    public boolean isErase;

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (arrayToSort != null && !isErase) {
        int barWidth = getWidth() / arrayToSort.length;
        int barHeightMultiplier = getHeight() / MAX_BAR_HEIGHT;

        barWidth = barWidth == 0 ? 1 : barWidth;

        for (int i = 0; i < arrayToSort.length; i++) {
          int barHeight = arrayToSort[i] * barHeightMultiplier;
          int x = (int) (((double) i / arrayToSort.length) * getWidth());

          g.setColor(getColor(barHeight));
          g.fillRect(x, getHeight() - barHeight, barWidth, barHeight);
        }
      }
    }
  }
}

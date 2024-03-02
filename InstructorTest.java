/**
 * Westmont College Spring 2024
 * CS 030 Project E
 *
 * @author Assistant Professor Mike Ryu mryu@westmont.edu
 */

/* ***************************************
 *   DO NOT MODIFY ANYTHING IN THIS FILE
 * **************************************/

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructorTest {

  static int[] seqArr;
  static List<Integer> seqList;

  static int[] randArr;
  static List<Integer> randList;

  @BeforeAll
  static void setUp() {
    seqArr = IntStream.range(0, 100000).toArray();
    seqList = new ArrayList<>(Arrays.stream(seqArr).boxed().toList());

    randArr = IntStream.generate(() -> new Random().nextInt(100000)).limit(100000).toArray();
    randList = Arrays.stream(randArr).boxed().distinct().collect(Collectors.toList());
    randArr = randList.stream().mapToInt(i -> i).toArray();
  }

  @Test
  void mergeSorterWithListsSeq() {
    testMergeSorterWithList(seqList);
  }

  @Test
  void mergeSorterWithListsRand() {
    testMergeSorterWithList(randList);
  }

  @Test
  void selectionSorterWorkerSeq() {
    testSorterWorker(seqList, new SelectionSorterWorker());
  }

  @Test
  void selectionSorterWorkerRand() {
    testSorterWorker(randList, new SelectionSorterWorker());
  }

  @Test
  void insertionSorterWorkerSeq() {
    testSorterWorker(seqList, new InsertionSorterWorker());
  }

  @Test
  void insertionSorterWorkerRand() {
    testSorterWorker(randList, new InsertionSorterWorker());
  }

  @Test
  void mergeSorterWorkerSeq() {
    testSorterWorker(seqList, new MergeSorterWorker());
  }

  @Test
  void mergeSorterWorkerRand() {
    testSorterWorker(randList, new MergeSorterWorker());
  }

  @Test
  void quickSorterWorkerSeq() {
    testSorterWorker(seqList, new QuickSorterWorker());
  }

  @Test
  void quickSorterWorkerRand() {
    testSorterWorker(randList, new QuickSorterWorker());
  }

  private void testMergeSorterWithList(List<Integer> list) {
    Collections.shuffle(list);
    List<Integer> sortedList = MergeSorterWithLists.sort(list);
    Collections.sort(list);

    for (int i = 0; i < list.size(); i++) {
      int expected = list.get(i);
      int actual = sortedList.get(i);
      assertEquals(expected, actual,
          String.format("expected %d at index %d, but found %d instead", expected, i, actual));
    }

    assertEquals(list.size(), sortedList.size(), "sorted list has incorrect size");
  }

  private void testSorterWorker(List<Integer> list, SorterWorker sw) {
    Collections.shuffle(list);
    int[] arr = list.stream().mapToInt(i -> i).toArray();
    int hashCode = arr.hashCode();

    SelectionSorterWorker s = new SelectionSorterWorker();
    sw.sort(arr);
    testSorterWithArray(list, arr, hashCode);
  }

  private void testSorterWithArray(List<Integer> list, int[] arr, int hashCode) {
    Collections.sort(list);

    for (int i = 0; i < arr.length; i++) {
      int expected = list.get(i);
      int actual = arr[i];
      assertEquals(expected, actual,
          String.format("expected %d at index %d, but found %d instead", expected, i, actual));
    }

    assertEquals(hashCode, arr.hashCode(), "sorting process altered the array reference");
  }
}
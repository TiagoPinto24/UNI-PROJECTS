import java.util.*;
import java.io.*;

public class MergeSort {

    public static void main(String[] args) throws Exception {

        int nThreads = 1;
        String filename = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                nThreads = Integer.parseInt(args[++i]);

                int logValue = (int) (Math.log(nThreads) / Math.log(2));
                if (nThreads <= 0 || !(Math.pow(2, logValue) == nThreads)) {
                    System.err.println("The number of threads should be a power of 2");
                    return;
                }

            } else {
                filename = args[i];
            }
        }

        List<Integer> list = new ArrayList<>();

        BufferedReader br;

        if (filename != null) {
            br = new BufferedReader(new FileReader(filename));
        } else {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String line;

        while ((line = br.readLine()) != null) {
            list.add(Integer.parseInt(line.trim()));
        }

        br.close();

        int[] array = list.stream().mapToInt(i -> i).toArray();

        long start = System.currentTimeMillis();

        parallelMergeSort(array, 0, array.length - 1, nThreads);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        for (int value : array) {
            System.out.println(value);
        }
        System.out.println("The algorithm took " + timeElapsed + " ms");
    }

    public static void parallelMergeSort(int[] array, int left, int right, int threads) {

        if (left >= right) {
            return;
        }

        int mid = (left + right) / 2;

        if (threads <= 1) {
            parallelMergeSort(array, left, mid, 1);
            parallelMergeSort(array, mid + 1, right, 1);
            merge(array, left, mid, right);
            return;
        }

        Thread leftThread = new Thread(() -> parallelMergeSort(array, left, mid, threads / 2));

        Thread rightThread = new Thread(() -> parallelMergeSort(array, mid + 1, right, threads / 2));

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(array, left, mid, right);
    }

    public static void merge(int[] array, int left, int mid, int right) {

        int[] temp = new int[right - left + 1];

        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        for (int x = 0; x < temp.length; x++) {
            array[left + x] = temp[x];
        }
    }
}
import java.util.*;
import java.io.*;

public class oddEven {

    static class SortThread extends Thread {
        int[] array;
        int start;
        int end;
        boolean swapped;

        public SortThread(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.swapped = false;
        }

        public void run() {
            for (int i = start; i < end; i += 2) {
                if (i + 1 < array.length && array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                }
            }
        }
    }

    public static void Algorithm(int[] array, int numThreads) throws InterruptedException {
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            // EVEN phase
            sorted = sorted & runPhase(array, 0, numThreads);

            // ODD phase
            sorted = sorted & runPhase(array, 1, numThreads);
        }
    }

    private static boolean runPhase(int[] array, int offset, int numThreads) throws InterruptedException {
        int n = array.length;
        SortThread[] threads = new SortThread[numThreads];

        int chunk = (n / 2) / numThreads + 1;

        for (int t = 0; t < numThreads; t++) {
            int start = offset + t * chunk * 2;
            int end = Math.min(start + chunk * 2, n - 1);

            threads[t] = new SortThread(array, start, end);
            threads[t].start();
        }

        boolean noSwap = true;

        for (SortThread w : threads) {
            w.join();
            if (w.swapped)
                noSwap = false;
        }

        return noSwap;
    }

    public static void main(String[] args) throws Exception {

        int numThreads = 1;
        String filename = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                numThreads = Integer.parseInt(args[++i]);
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

        Algorithm(array, numThreads);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        for (int value : array) {
            System.out.println(value);
        }

        System.out.println("The algorithm took " + timeElapsed + " ms");
    }
}
package Lab7.Task1;

import java.util.*;
import java.util.concurrent.*;

public class TextCounter {

    // Result holder
    public static class Counter {
        public final int textId;
        public final int lines;
        public final int words;
        public final int chars;

        public Counter(int textId, int lines, int words, int chars) {
            this.textId = textId;
            this.lines = lines;
            this.words = words;
            this.chars = chars;
        }

        @Override
        public String toString() {
            return "Counter{" +
                    "textId=" + textId +
                    ", lines=" + lines +
                    ", words=" + words +
                    ", chars=" + chars +
                    '}';
        }
    }

    public static Callable<Counter> getTextCounter(int textId, String text) {
        return () -> {
            int lineCount = text.isEmpty() ? 0 : text.split("\n", -1).length;

            String[] words = text.trim().split("\\s+");
            int wordCount = text.trim().isEmpty() ? 0 : words.length;

            int charCount = text.length();

            return new Counter(textId, lineCount, wordCount, charCount);
        };
    }


    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();

        List<Callable<Counter>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int textId = sc.nextInt();
            sc.nextLine();

            int lines = sc.nextInt();
            sc.nextLine();

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                text.append(sc.nextLine());
                if (j < lines - 1) {
                    text.append("\n");
                }
            }

            tasks.add(getTextCounter(textId, text.toString()));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


        List<Future<Counter>> futures = executor.invokeAll(tasks);


        List<Counter> results = new ArrayList<>();

        for (Future<Counter> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();


        results.sort(Comparator.comparingInt(c -> c.textId));

        for (Counter c : results) {
            System.out.printf(
                    "%d %d %d %d%n",
                    c.textId, c.lines, c.words, c.chars
            );
        }
    }
}
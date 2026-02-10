package Lab4.Task3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

class TermFrequency {

    private HashMap<String, Integer> wordFrequency;
    private int totalWords;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        this.wordFrequency = new HashMap<>();
        this.totalWords = 0;

        Set<String> stopWordSet = new HashSet<>();
        for (String stopWord : stopWords) {
            stopWordSet.add(stopWord.toLowerCase());
        }

        Scanner sc = new Scanner(inputStream);

        while (sc.hasNext()) {
            String word = sc.next().toLowerCase().replaceAll("[,.]", "");

            if (!word.isEmpty() && !stopWordSet.contains(word)) {
                totalWords++;
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }
        sc.close();
    }

    public int countTotal() {
        return totalWords;
    }

    public int countDistinct() {
        return wordFrequency.size();
    }

    public List<String> mostOften(int k) {
        return wordFrequency.entrySet().stream().sorted((w1, w2) -> {
                    int cmp = w2.getValue().compareTo(w1.getValue());
                    if (cmp != 0) return cmp;
                    return w1.getKey().compareTo(w2.getKey());
                })
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

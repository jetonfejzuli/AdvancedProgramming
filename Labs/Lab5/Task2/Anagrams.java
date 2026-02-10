package Lab5.Task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde

        Scanner sc = new Scanner(inputStream);

        TreeMap<String, TreeSet<String>> groups = new TreeMap<>();
        ArrayList<String> keyOrder = new ArrayList<>();

        while (sc.hasNext()) {
            String word = sc.next().trim();
            if (word.isEmpty()) {
                continue;
            }

            char[] charArray = word.toCharArray();
            Arrays.sort(charArray);
            String key = new String(charArray);

            if (!groups.containsKey(key)) {
                keyOrder.add(key);
                groups.put(key, new TreeSet<>());
            }
            groups.get(key).add(word);
        }
        sc.close();

        for (String key : keyOrder) {
            TreeSet<String> words = groups.get(key);
            if (words.size() >= 5) {
                Iterator<String> it = words.iterator();
                StringBuilder sb = new StringBuilder();
                while (it.hasNext()) {
                    sb.append(it.next());
                    if (it.hasNext()) {
                        sb.append(" ");
                    }
                }
                System.out.println(sb.toString());
            }
        }

    }
}


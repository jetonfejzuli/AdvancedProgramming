package Lab5.Task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement the necessary classes

class Book {
    String isbn;
    String title;
    String author;
    int year;

    int totalCopies = 0;
    int availableCopies = 0;
    int totalBorrows = 0;

    Set<String> currentBorrowers = new HashSet<>();
    Queue<String> waitingList = new LinkedList<>();

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

}

class Member {
    String id;
    String name;
    int borrowedNow;
    int totalBorrows;
    Set<String> currentlyBorrowed;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedNow = 0;
        this.totalBorrows = 0;
        this.currentlyBorrowed = new HashSet<>();
    }

}

class LibrarySystem {
    private String name;
    private Map<String, Member> members;
    private Map<String, Book> books;

    public LibrarySystem(String name) {
        this.name = name;
        this.members = new HashMap<>();
        this.books = new HashMap<>();
    }

    public void registerMember(String id, String fullName) {
        members.put(id, new Member(id, fullName));
    }

    public void addBook(String isbn, String title, String author, int year) {
        if (books.containsKey(isbn)) {
            books.get(isbn).totalCopies++;
            books.get(isbn).availableCopies++;
        } else {
            books.put(isbn, new Book(isbn, title, author, year));
            books.get(isbn).totalCopies = 1;
            books.get(isbn).availableCopies = 1;
        }
    }

    public void borrowBook(String memberId, String isbn) {
        Member m = members.get(memberId);
        Book b = books.get(isbn);

        if (m == null || b == null) {
            return;
        }

        if (b.availableCopies > 0) {
            b.availableCopies--;
            b.currentBorrowers.add(memberId);
            b.totalBorrows++;
            m.borrowedNow++;
            m.totalBorrows++;
        } else {
            if (!b.waitingList.contains(memberId)) {
                b.waitingList.add(memberId);
            }
        }
    }

    public void returnBook(String memberId, String isbn) {
        Member m = members.get(memberId);
        Book b = books.get(isbn);

        if (b.currentBorrowers.remove(memberId)) {
            b.availableCopies++;
            m.borrowedNow--;
        }
        if (!b.waitingList.isEmpty() && b.availableCopies > 0) {
            String member = b.waitingList.poll();
            borrowBook(member, isbn);
        }
    }

    public void printMembers() {
        members.values().stream().sorted((a, b) -> {
            if (b.borrowedNow != a.borrowedNow) {
                return Integer.compare(b.borrowedNow, a.borrowedNow);
            }
            return a.name.compareTo(b.name);
        }).forEach(m -> System.out.println(m.name + " (" + m.id + ") - borrowed now: " +
                m.borrowedNow + ", total borrows: " + m.totalBorrows));
    }

    public void printBooks() {
        books.values().stream().sorted((a, b) -> {
            if (b.totalBorrows != a.totalBorrows) {
                return Integer.compare(b.totalBorrows, a.totalBorrows);
            }
            return Integer.compare(a.year, b.year);
        }).forEach(b -> System.out.println(b.isbn + " - \"" + b.title + "\" by " + b.author +
                " (" + b.year + "), available: " + b.availableCopies + ", total borrows: " + b.totalBorrows));
    }

    public void printBookCurrentBorrowers(String isbn) {
        List<String> sorted = books.get(isbn).currentBorrowers.stream().sorted().collect(Collectors.toList());
        System.out.println(String.join(", ", sorted));
    }

    public void printTopAuthors() {
        Map<String, Integer> authorBorrows = new HashMap<>();

        for (Book b : books.values()) {
            authorBorrows.put(b.author, authorBorrows.getOrDefault(b.author, 0) + b.totalBorrows);
        }

        authorBorrows.entrySet()
                .stream()
                .sorted((a, b) -> {
                    if (a.getValue() != b.getValue()) {
                        return Integer.compare(b.getValue(), a.getValue());
                    }
                    return a.getKey().compareTo(b.getKey());
                }).forEach(a -> System.out.println(a.getKey() + " - " + a.getValue()));

    }
}

public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

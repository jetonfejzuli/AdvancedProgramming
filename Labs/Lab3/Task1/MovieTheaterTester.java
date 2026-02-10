package Lab3.Task1;

import java.io.*;
import java.util.*;

class sortByGenreAndTitle implements Comparator<Movie> {

    @Override
    public int compare(Movie m1, Movie m2) {
        return Comparator.comparing(Movie::getGenre).thenComparing(Movie::getTitle).compare(m1, m2);
    }
}

class sortByYearAndTitle implements Comparator<Movie> {
    @Override
    public int compare(Movie m1, Movie m2) {
        return Comparator.comparing(Movie::getYear).thenComparing(Movie::getTitle).compare(m1, m2);
    }
}

class sortByRatingAndTitle implements Comparator<Movie> {
    @Override
    public int compare(Movie m1, Movie m2) {
        if (m1.getAvgRating() == m2.getAvgRating()) {
            return m2.getTitle().compareTo(m1.getTitle());
        }
        return Double.compare(m1.getAvgRating(), m2.getAvgRating());
    }
}

class Movie {
    private String title;
    private String genre;
    private int year;
    private double avgRating;

    public Movie(String title, String genre, int year, double avgRating) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.avgRating = avgRating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getAvgRating() {
        return avgRating;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f", title, genre, year, avgRating);
    }
}

class MovieTheater {
    private List<Movie> movies;

    public MovieTheater() {
        this.movies = new ArrayList<>();
    }

    public void readMovies(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String name = reader.readLine();
            String genre = reader.readLine();
            int year = Integer.parseInt(reader.readLine());
            String[] ratings = reader.readLine().trim().split("\\s+");
            double avg = Arrays.stream(ratings).mapToInt(Integer::parseInt).average().orElse(0.0);
            movies.add(new Movie(name, genre, year, avg));
        }
    }

    public void printByGenreAndTitle() {
        movies.stream().sorted(new sortByGenreAndTitle()).forEach(System.out::println);
    }

    public void printByYearAndTitle() {
        movies.stream().sorted(new sortByYearAndTitle()).forEach(System.out::println);
    }

    public void printByRatingAndTitle() {
        movies.stream().sorted(new sortByRatingAndTitle().reversed()).forEach(System.out::println);
    }
}


public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}

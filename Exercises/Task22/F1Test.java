package Exercises.Task22;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Driver implements Comparable<Driver> {
    String name;
    List<String> laps;

    public Driver(String name, String lap1, String lap2, String lap3) {
        this.name = name;
        laps = new ArrayList<>();
        laps.add(lap1);
        laps.add(lap2);
        laps.add(lap3);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLaps() {
        return laps;
    }

    public void setLaps(List<String> laps) {
        this.laps = laps;
    }

    public static Driver createDriver(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        String lap1 = parts[1];
        String lap2 = parts[2];
        String lap3 = parts[3];
        return new Driver(name, lap1, lap2, lap3);
    }

    private static int getMiliseconds(String lap) {
        String[] laps = lap.split(":");
        return Integer.parseInt(laps[0]) * 1000 * 60 +
                Integer.parseInt(laps[1]) * 1000 +
                Integer.parseInt(laps[2]);
    }

    public String bestLap() {
        int minLap = laps.stream().mapToInt(Driver::getMiliseconds).min().orElse(0);

        return laps.stream().filter(i -> getMiliseconds(i) == minLap).findFirst().orElseGet(() -> laps.get(laps.size() - 1));
    }

    @Override
    public int compareTo(Driver other) {
        return Integer.compare(getMiliseconds(this.bestLap()), getMiliseconds(other.bestLap()));
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", getName(), bestLap());
    }
}


class F1Race {
    // vashiot kod ovde
    List<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        drivers = rd.lines().filter(Objects::nonNull).map(Driver::createDriver).collect(Collectors.toList());
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        Collections.sort(drivers);
        IntStream.range(0, drivers.size())
                .forEach(i -> writer.println(i + 1 + ". " + drivers.get(i).toString()));

        writer.flush();
        writer.close();
    }
}


public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}



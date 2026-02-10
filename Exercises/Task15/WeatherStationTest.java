package Exercises.Task15;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde

class Measurement implements Comparable<Measurement> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Measurement other) {
        long t1 = this.date.getTime();
        long t2 = other.date.getTime();
        if (Math.abs(t1 - t2) < 150 * 1000) {
            return 0;
        }
        return this.date.compareTo(other.date);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                temperature, wind, humidity, visibility, sdf.format(date));
    }
}

class WeatherStation {
    private int days;
    private TreeSet<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new TreeSet<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement m = new Measurement(temperature, wind, humidity, visibility, date);
        long time = m.getDate().getTime();
        Iterator<Measurement> iterator = measurements.iterator();
        if (iterator.hasNext()) {
            Measurement measurement = iterator.next();
            long d = time - measurement.getDate().getTime();
            if (d > days * 24 * 60 * 60 * 1000) {
                iterator.remove();
            }
        }
        measurements.add(m);
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        List<Measurement> filtered = measurements.stream()
                .filter(measurement -> measurement.getDate().compareTo(from) >= 0 &&
                        measurement.getDate().compareTo(to) <= 0)
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            throw new RuntimeException();
        }
        filtered.forEach(System.out::println);

        double averageTemperature = filtered.stream()
                .mapToDouble(Measurement::getTemperature)
                .average()
                .orElseThrow(RuntimeException::new);

        System.out.printf("Average temperature: %.2f\n", averageTemperature);
    }
}

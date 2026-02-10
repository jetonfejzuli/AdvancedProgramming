package Exercises.Task23;

import java.util.*;

class Participant implements Comparable<Participant> {
    private String city;
    private String code;
    private String name;
    private int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Participant o) {
        if (this.name.compareTo(o.name) == 0) {
            if (this.age == o.age) {
                return this.code.compareTo(o.code);
            }
            return this.age - o.age;
        }
        return this.name.compareTo(o.name);
//        return Comparator.comparing(Exercises.Task23.Participant::getName).thenComparing(Exercises.Task23.Participant::getAge).compare(this, o);
    }


    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

class Audition {
    private List<Participant> participants;

    public Audition() {
        participants = new ArrayList<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        for (Participant p : participants) {
            if (p.getCity().equals(city) && p.getCode().equals(code)) {
                return;
            }
        }
        participants.add(new Participant(city, code, name, age));
    }

    public void listByCity(String city) {
        Collections.sort(participants);
        participants.stream().filter(i -> i.getCity().equals(city)).forEach(System.out::println);

    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
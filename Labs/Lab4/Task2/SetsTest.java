package Lab4.Task2;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class StudentAlreadyExistsException extends Exception {
    public StudentAlreadyExistsException(String id) {
        super(String.format("Student with ID %s already exists", id));
    }
}

class Student {
    private String id;
    private List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = new ArrayList<>(grades);
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double getAverageGrade() {
        return grades.stream().mapToDouble(i -> i).sum() / grades.size();
    }

    public int getCoursePassed() {
        return grades.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student{id='").append(id).append("', grades=").append(grades).append("}");
        return sb.toString();
    }
}

class Faculty {
    private HashMap<String, Student> students;

    public Faculty() {
        students = new HashMap<>();
    }


    public void addStudent(String id, List<Integer> grades) throws StudentAlreadyExistsException {
        if (students.containsKey(id)) {
            throw new StudentAlreadyExistsException(id);
        } else {
            students.put(id, new Student(id, grades));
        }
    }

    public void addGrade(String id, int grade) {
        Student student = students.get(id);
        student.getGrades().add(grade);
    }


    public Set<Student> getStudentsSortedByAverageGrade() {
        return students.values()
                .stream()
                .sorted(Comparator.comparing(Student::getAverageGrade).reversed()
                        .thenComparing(Student::getCoursePassed, Comparator.reverseOrder())
                        .thenComparing(Student::getId, Comparator.reverseOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Student> getStudentsSortedByCoursesPassed() {
        return students.values()
                .stream()
                .sorted(Comparator.comparing(Student::getCoursePassed).reversed()
                        .thenComparing(Student::getAverageGrade, Comparator.reverseOrder())
                        .thenComparing(Student::getId, Comparator.reverseOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }
}

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}

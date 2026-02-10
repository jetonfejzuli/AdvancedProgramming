package Lab3.Task3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: Add classes and implement methods

class Applicant {
    private int id;
    private String name;
    private double gpa;
    private List<SubjectWithGrade> subjectsWithGrade;
    private StudyProgramme studyProgramme;

    public Applicant(int id, String name, double gpa, StudyProgramme studyProgramme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.subjectsWithGrade = new ArrayList<>();
        this.studyProgramme = studyProgramme;
    }

    public void addSubjectAndGrade(String subject, int grade) {
        subjectsWithGrade.add(new SubjectWithGrade(subject, grade));
    }

    public List<SubjectWithGrade> getSubjectsWithGrade() {
        return subjectsWithGrade;
    }

    public double calculatePoints() {
        double points = gpa * 12;
        for (SubjectWithGrade subjectWithGrade : subjectsWithGrade) {
            if (studyProgramme != null && studyProgramme.getFaculty() != null &&
                    studyProgramme.getFaculty().getAppropriateSubjects().contains(subjectWithGrade.getSubject())) {
                points += subjectWithGrade.getGrade() * 2;
            } else {
                points += subjectWithGrade.getGrade() * 1.2;
            }
        }
        return points;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                ", Name: " + name +
                ", GPA: " + gpa +
                " - " + Double.toString(calculatePoints());
    }

}

class Faculty {
    private String shortName;
    private List<String> appropriateSubjects;
    private List<StudyProgramme> studyProgrammes;

    public Faculty(String shortName) {
        this.shortName = shortName;
        this.appropriateSubjects = new ArrayList<>();
        this.studyProgrammes = new ArrayList<>();
    }

    public List<String> getAppropriateSubjects() {
        return appropriateSubjects;
    }

    public void addSubject(String subject) {
        appropriateSubjects.add(subject);
    }

    public void addStudyProgramme(StudyProgramme sp) {
        studyProgrammes.add(sp);
    }

    public List<StudyProgramme> getStudyProgrammes() {
        return studyProgrammes;
    }

    @Override
    public String toString() {
        return shortName;
    }
}

class StudyProgramme {
    private String code;
    private String name;
    private int numPublicQuota;
    private int numPrivateQuota;
    private int enrolledInPublicQuota;
    private int enrolledInPrivateQuota;
    private List<Applicant> applicants;
    private Faculty faculty;

    public StudyProgramme(String code, String name, Faculty faculty, int numPublicQuota, int numPrivateQuota) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.numPublicQuota = numPublicQuota;
        this.numPrivateQuota = numPrivateQuota;
        this.applicants = new ArrayList<>();
        this.enrolledInPublicQuota = 0;
        this.enrolledInPrivateQuota = 0;
    }

    public void calculateEnrollmentNumbers() {
        List<Applicant> sorted = applicants.stream().
                sorted(Comparator.comparing(Applicant::calculatePoints).reversed()).
                collect(Collectors.toList());
        enrolledInPublicQuota = Math.min(sorted.size(), numPublicQuota);
        int remaining = sorted.size() - enrolledInPublicQuota;
        enrolledInPrivateQuota = Math.min(numPrivateQuota, remaining);
    }

    public void addApplicant(Applicant applicant) {
        applicants.add(applicant);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public List<Applicant> getApplicants() {
        return applicants;
    }

    public int getNumPublicQuota() {
        return numPublicQuota;
    }

    public int getNumPrivateQuota() {
        return numPrivateQuota;
    }

    public int getEnrolledInPublicQuota() {
        return enrolledInPublicQuota;
    }

    public int getEnrolledInPrivateQuota() {
        return enrolledInPrivateQuota;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Public Quota:").append("\n");

        List<Applicant> sorted = applicants.stream()
                .sorted(Comparator.comparing(Applicant::calculatePoints).reversed()).
                collect(Collectors.toList());
        int total = sorted.size();
        int publicQuota = enrolledInPublicQuota;
        int privateQuota = enrolledInPrivateQuota;

        for (int i = 0; i < publicQuota; i++) {
            sb.append(sorted.get(i)).append("\n");
        }

        sb.append("Private Quota:\n");
        for (int i = publicQuota; i < publicQuota + privateQuota; i++) {
            sb.append(sorted.get(i)).append("\n");
        }

        sb.append("Rejected:\n");
        for (int i = publicQuota + privateQuota; i < total; i++) {
            sb.append(sorted.get(i)).append("\n");
        }

        return sb.toString();
    }

}

class SubjectWithGrade {
    private String subject;
    private int grade;

    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {

        for (Faculty f : faculties) {

            System.out.printf("Faculty: %s%n", f);
            System.out.printf("Subjects: %s%n", f.getAppropriateSubjects());
            System.out.println("Study Programmes:");

            List<StudyProgramme> programmes = f.getStudyProgrammes();

            programmes.sort(Comparator
                    .comparingDouble((StudyProgramme sp) ->
                            sp.getApplicants().stream()
                                    .mapToLong(a -> a.getSubjectsWithGrade().stream()
                                            .filter(swg -> f.getAppropriateSubjects().contains(swg.getSubject()))
                                            .count())
                                    .average()
                                    .orElse(0.0)
                    ).reversed()
                    .thenComparingDouble((StudyProgramme sp) -> {
                        int totalQuota = sp.getNumPublicQuota() + sp.getNumPrivateQuota();
                        int enrolled = sp.getEnrolledInPublicQuota() + sp.getEnrolledInPrivateQuota();
                        return -((double) enrolled / totalQuota * 100);
                    })
                    .thenComparingDouble((StudyProgramme sp) -> {
                        int enrolled = sp.getEnrolledInPublicQuota() + sp.getEnrolledInPrivateQuota();
                        if (enrolled == 0) return 0.0;

                        List<Applicant> sortedApplicants = new ArrayList<>(sp.getApplicants());
                        sortedApplicants.sort(Comparator.comparing(Applicant::calculatePoints).reversed());

                        double avgPoints = sortedApplicants.stream()
                                .limit(enrolled)
                                .mapToDouble(Applicant::calculatePoints)
                                .average()
                                .orElse(0.0);

                        return -avgPoints;
                    })
            );

            for (StudyProgramme sp : programmes) {
                System.out.println(sp);
            }
        }
    }

    public static void readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] parts = line.split(";");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double gpa = Double.parseDouble(parts[2]);

            String code = parts[parts.length - 1];

            StudyProgramme sp = studyProgrammes.stream().
                    filter(s -> s.getCode().equals(code)).
                    findFirst().
                    orElse(null);

            if (sp == null) {
                continue;
            }

            Applicant applicant = new Applicant(id, name, gpa, sp);

            for (int i = 3; i < parts.length - 1; i += 2) {
                String subject = parts[i];
                int grade = Integer.parseInt(parts[i + 1]);
                applicant.addSubjectAndGrade(subject, grade);
            }

            sp.addApplicant(applicant);
        }
    }
}

public class EnrollmentsTest {

    public static void main(String[] args) {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        try {
            EnrollmentsIO.readEnrollments(allProgrammes, System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}


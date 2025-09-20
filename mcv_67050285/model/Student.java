package model;

import java.time.LocalDate;
import java.time.Period;

public class Student {
    private final String studentId;     
    private final String title;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String school;
    private final String email;
    private final String curriculumCode;

    public Student(String studentId, String title, String firstName, String lastName,
                   LocalDate birthDate, String school, String email, String curriculumCode) {
        this.studentId = studentId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.school = school;
        this.email = email;
        this.curriculumCode = curriculumCode;
    }

    public String getStudentId() { return studentId; }
    public String getTitle() { return title; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getSchool() { return school; }
    public String getEmail() { return email; }
    public String getCurriculumCode() { return curriculumCode; }

    public int getAgeYears() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return studentId + " - " + firstName + " " + lastName;
    }
}

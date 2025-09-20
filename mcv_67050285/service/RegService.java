package service;

import model.*;

public class RegService {
    private final DataStore store;

    public RegService(DataStore store) { this.store = store; }

    // ---- Students ----
    public void addStudent(Student s){
        if (s.getStudentId()==null || s.getStudentId().length()!=8 || !s.getStudentId().startsWith("69"))
            throw new IllegalArgumentException("Student ID must be 8 characters and start with '69'.");
        if (store.findStudent(s.getStudentId()).isPresent())
            throw new IllegalArgumentException("Duplicate student ID.");
        if (s.getAgeYears() < 15)
            throw new IllegalArgumentException("Student must be at least 15 years old.");
        if (s.getSchool()==null || s.getSchool().isBlank())
            throw new IllegalArgumentException("School must not be empty.");
        if (s.getEmail()==null || !s.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email format.");
        store.saveStudent(s);
    }

    // ---- Registration ----
    public void register(String sid, String subjCode, int year, int sem){
        Student stu = store.findStudent(sid).orElseThrow(() -> new IllegalArgumentException("Student not found."));
        Subject sb  = store.findSubject(subjCode).orElseThrow(() -> new IllegalArgumentException("Subject not found."));

        if (String.valueOf(year).length()!=4) throw new IllegalArgumentException("Academic year must be 4 digits.");
        if (sem < 1 || sem > 3) throw new IllegalArgumentException("Semester must be 1..3.");
        if (store.existsReg(sid, subjCode, year, sem)) throw new IllegalArgumentException("Already registered in this term.");

        // curriculum
        if (!sb.getCurriculumCode().equals(stu.getCurriculumCode()))
            throw new IllegalArgumentException("Subject is not in the student's curriculum.");

        // seats
        long taken = store.countSeats(subjCode, year, sem);
        if (sb.getMaxSeats() > 0 && taken >= sb.getMaxSeats())
            throw new IllegalArgumentException("Subject seats are full.");

        // age
        if (stu.getAgeYears() < 15) throw new IllegalArgumentException("Student is under 15 years old.");

        // prerequisite
        if (sb.getPrereqCode()!=null) {
            boolean ok = store.regsOf(sid).stream().anyMatch(r ->
                    r.getSubjectCode().equals(sb.getPrereqCode()) &&
                    (r.getAcadYear() < year || (r.getAcadYear()==year && r.getSemester() < sem))
            );
            if (!ok) throw new IllegalArgumentException("Missing prerequisite: " + sb.getPrereqCode() + " (must be taken earlier)");
        }

        store.addReg(new Registration(sid, subjCode, year, sem));
    }
}

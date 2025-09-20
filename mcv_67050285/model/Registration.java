package model;

public class Registration {
    private final String studentId;
    private final String subjectCode;
    private final int acadYear;
    private final int semester;     

    public Registration(String studentId, String subjectCode, int acadYear, int semester) {
        this.studentId = studentId;
        this.subjectCode = subjectCode;
        this.acadYear = acadYear;
        this.semester = semester;
    }

    public String getStudentId() { return studentId; }
    public String getSubjectCode() { return subjectCode; }
    public int getAcadYear() { return acadYear; }
    public int getSemester() { return semester; }
}

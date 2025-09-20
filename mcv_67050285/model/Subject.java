package model;

public class Subject {
    private final String subjectCode;    
    private final String name;
    private final int credit;
    private final boolean required;
    private final int maxSeats;          
    private final String prereqCode;    
    private final String curriculumCode;

    public Subject(String subjectCode, String name, int credit,
                   boolean required, int maxSeats, String prereqCode, String curriculumCode) {
        this.subjectCode = subjectCode;
        this.name = name;
        this.credit = credit;
        this.required = required;
        this.maxSeats = maxSeats;
        this.prereqCode = prereqCode;
        this.curriculumCode = curriculumCode;
    }

    public String getSubjectCode() { return subjectCode; }
    public String getName() { return name; }
    public int getCredit() { return credit; }
    public boolean isRequired() { return required; }
    public int getMaxSeats() { return maxSeats; }
    public String getPrereqCode() { return prereqCode; }
    public String getCurriculumCode() { return curriculumCode; }

    @Override public String toString() {
        return subjectCode + " - " + name + " (" + curriculumCode + ")";
    }
}

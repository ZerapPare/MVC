package model;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private final Map<String, Student> students = new LinkedHashMap<>();
    private final Map<String, Subject> subjects = new LinkedHashMap<>();
    private final List<Registration> registrations = new ArrayList<>();

    public DataStore() {
        // Students 10+
        addStu("69000001","Mr.","Alpha","One",   LocalDate.of(2008,1,1),"ABC High","alpha@mail.com","CS1");
        addStu("69000002","Ms.","Beta","Two",    LocalDate.of(2007,5,15),"XYZ School","beta@mail.com","CS1");
        addStu("69000003","Mr.","Gamma","Three", LocalDate.of(2006,11,20),"LMN School","gamma@mail.com","GN1");
        addStu("69000004","Ms.","Delta","Four",  LocalDate.of(2008,3,10),"ABC High","delta@mail.com","CS1");
        addStu("69000005","Mr.","Epsilon","Five",LocalDate.of(2007,7,7),"XYZ School","eps@mail.com","GN1");
        addStu("69000006","Ms.","Zeta","Six",    LocalDate.of(2008,9,9),"KLM High","zeta@mail.com","GN1");
        addStu("69000007","Mr.","Eta","Seven",   LocalDate.of(2007,2,2),"KLM High","eta@mail.com","CS1");
        addStu("69000008","Ms.","Theta","Eight", LocalDate.of(2008,12,12),"NOP School","theta@mail.com","CS1");
        addStu("69000009","Mr.","Iota","Nine",   LocalDate.of(2007,10,25),"NOP School","iota@mail.com","GN1");
        addStu("69000010","Ms.","Kappa","Ten",   LocalDate.of(2006,8,18),"QRS School","kappa@mail.com","CS1");


        addSub("05506001","CS1-Programming I",           3,true, 30,null,      "CS1");
        addSub("05506002","CS1-Programming II",          3,true, 30,"05506001","CS1");
        addSub("05506003","CS1-Data Structures",         3,true, 30,"05506002","CS1");
        addSub("05506231","CS1-Statistics & Probability",3,true, 40,null,"CS1");
        addSub("05506232","CS1-Discrete Math",           3,true, 40,null,"CS1");
        addSub("05506010","CS1-Web Technology",          3,false,25,null,"CS1");
        addSub("90690001","GN1-Intro IT",                3,true, 50,null,      "GN1");
        addSub("90690002","GN1-Database I",              3,true, 40,"90690001","GN1");
        addSub("90690003","GN1-Database II",             3,true, 30,"90690002","GN1");
        addSub("90694000","GN1-Foundation English 1",    3,true, 60,null,"GN1");
        addSub("90694008","GN1-Foundation English 2",    3,true, 60,"90694000","GN1");
        addSub("90692990","GN1-Charm School",            3,false,20,null,"GN1");

        registrations.add(new Registration("69000001","05506001",2024,1));
        registrations.add(new Registration("69000001","05506002",2024,2));
        registrations.add(new Registration("69000003","90690001",2024,1));
        registrations.add(new Registration("69000003","90690002",2024,2));
        registrations.add(new Registration("69000002","05506232",2024,1));
        registrations.add(new Registration("69000005","90694000",2024,1));
    }

    private void addStu(String id,String t,String f,String l,LocalDate b,String school,String email,String cur) {
        students.put(id, new Student(id,t,f,l,b,school,email,cur));
    }
    private void addSub(String code,String name,int credit,boolean req,int max,String pre,String cur) {
        subjects.put(code, new Subject(code,name,credit,req,max,pre,cur));
    }

    public Collection<Student> allStudents(){ return new ArrayList<>(students.values()); }
    public Collection<Subject> allSubjects(){ return new ArrayList<>(subjects.values()); }

    public Optional<Student> findStudent(String id){ return Optional.ofNullable(students.get(id)); }
    public Optional<Subject> findSubject(String code){ return Optional.ofNullable(subjects.get(code)); }

    public void saveStudent(Student s){ students.put(s.getStudentId(), s); }

    public List<Registration> regsOf(String studentId){
        return registrations.stream().filter(r -> r.getStudentId().equals(studentId)).collect(Collectors.toList());
    }

    public boolean existsReg(String sid, String code, int y, int sem){
        return registrations.stream().anyMatch(r ->
                r.getStudentId().equals(sid) && r.getSubjectCode().equals(code) &&
                r.getAcadYear()==y && r.getSemester()==sem);
    }

    public long countSeats(String code, int y, int sem){
        return registrations.stream().filter(r ->
                r.getSubjectCode().equals(code) && r.getAcadYear()==y && r.getSemester()==sem).count();
    }

    public void addReg(Registration r){ registrations.add(r); }
}

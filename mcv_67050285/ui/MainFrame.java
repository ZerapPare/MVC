package ui;

import model.*;
import service.RegService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private final DataStore store;
    private final RegService service;

    private DefaultTableModel studentModel;

    private JComboBox<Student> cbStudent;
    private JComboBox<Subject> cbSubject;
    private JComboBox<String>  cbCurFilter; 
    private JSpinner spYear, spSem;
    private JButton regBtn;
    private JTextArea status;
    private JLabel lbPrereq;       // วิชาบังคับก่อน
    private JLabel lbSeats;        // ที่นั่ง


    private DefaultTableModel histModel;
    private JComboBox<Student> cbHistStudent;

    private DefaultTableModel missModel;
    private JComboBox<Student> cbMissStudent;

    public MainFrame(DataStore store){
        this.store = store;
        this.service = new RegService(store);

        setTitle("Student Registration (Swing MVC)");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Students", buildStudentsPanel());
        tabs.add("Register", buildRegisterPanel());
        tabs.add("History", buildHistoryPanel());
        tabs.add("Missing (View)", buildMissingPanel());
        add(tabs);
    }
    private JPanel buildStudentsPanel(){
        JPanel p = new JPanel(new BorderLayout(10,10));

        studentModel = new DefaultTableModel(
                new Object[]{"ID","Name","Birth","Age","Curriculum","School","Email"}, 0) {
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable table = new JTable(studentModel);
        refreshStudentsTable();
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(3,6,6,6));
        form.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        JTextField id = new JTextField("69000011");
        JTextField title = new JTextField("Mr.");
        JTextField first = new JTextField("New");
        JTextField last = new JTextField("Student");
        JTextField birth = new JTextField(LocalDate.now().minusYears(16).toString());
        JTextField school = new JTextField("Your School");
        JTextField email  = new JTextField("someone@mail.com");
        JButton addBtn = new JButton("Add Student");

        form.add(new JLabel("ID")); form.add(id);
        form.add(new JLabel("Title")); form.add(title);
        form.add(new JLabel("First Name")); form.add(first);
        form.add(new JLabel("Last Name")); form.add(last);
        form.add(new JLabel("Birth (yyyy-mm-dd)")); form.add(birth);
        form.add(new JLabel("School")); form.add(school);
        form.add(new JLabel("Email"));  form.add(email);
        form.add(new JLabel()); form.add(addBtn);

        addBtn.addActionListener(e -> {
            try{

                Student s = new Student(
                        id.getText().trim(), title.getText().trim(),
                        first.getText().trim(), last.getText().trim(),
                        LocalDate.parse(birth.getText().trim()),
                        school.getText().trim(), email.getText().trim(),
                        ""  
                );
                service.addStudent(s);
                JOptionPane.showMessageDialog(this,"Student added successfully.");
                refreshStudentsTable();
                refreshCombos();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        p.add(form, BorderLayout.SOUTH);
        return p;
    }

    private void refreshStudentsTable(){
        studentModel.setRowCount(0);
        store.allStudents().stream()
            .sorted(Comparator.comparing(Student::getStudentId))
            .forEach(s -> studentModel.addRow(new Object[]{
                s.getStudentId(), s.getFirstName()+" "+s.getLastName(), s.getBirthDate(),
                s.getAgeYears(), s.getCurriculumCode(), s.getSchool(), s.getEmail()
            }));
    }

    private JPanel buildRegisterPanel(){
        JPanel root = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Registration Form"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        // Row 0: Student
        cbStudent = new JComboBox<>(store.allStudents().toArray(new Student[0]));
        g.gridx=0; g.gridy=0; form.add(new JLabel("Student"), g);
        g.gridx=1; g.gridy=0; form.add(cbStudent, g);

        // Row 1: Curriculum Filter
        cbCurFilter = new JComboBox<>();
        g.gridx=0; g.gridy=1; form.add(new JLabel("Curriculum Filter"), g);
        g.gridx=1; g.gridy=1; form.add(cbCurFilter, g);

        // Row 2: Subject + Prereq
        cbSubject = new JComboBox<>();
        lbPrereq = new JLabel("Prerequisite: -");
        g.gridx=0; g.gridy=2; form.add(new JLabel("Subject"), g);
        g.gridx=1; g.gridy=2; form.add(cbSubject, g);
        g.gridx=2; g.gridy=2; g.weightx=0; form.add(lbPrereq, g);
        g.weightx=1;

        // Row 3-4: Year + Semester + Seats
        spYear = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        spSem  = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        lbSeats = new JLabel("Seats: -");
        g.gridx=0; g.gridy=3; form.add(new JLabel("Academic Year"), g);
        g.gridx=1; g.gridy=3; form.add(spYear, g);
        g.gridx=0; g.gridy=4; form.add(new JLabel("Semester"), g);
        g.gridx=1; g.gridy=4; form.add(spSem, g);
        g.gridx=2; g.gridy=3; g.gridheight=2; g.anchor = GridBagConstraints.WEST;
        form.add(lbSeats, g);
        g.gridheight=1; g.anchor = GridBagConstraints.CENTER;

        // Row 5: Button
        regBtn = new JButton("Register");
        g.gridx=2; g.gridy=5; g.anchor = GridBagConstraints.EAST;
        form.add(regBtn, g);

        status = new JTextArea(10,40);
        status.setEditable(false);
        JScrollPane statusPane = new JScrollPane(status);
        statusPane.setBorder(BorderFactory.createTitledBorder("Status"));

        root.add(form, BorderLayout.NORTH);
        root.add(statusPane, BorderLayout.CENTER);

        cbStudent.addActionListener(e -> { updateCurriculumFilter(); updateSubjectCombo(); });
        cbCurFilter.addActionListener(e -> updateSubjectCombo());
        cbSubject.addActionListener(e -> updateInfoLabels());
        spYear.addChangeListener(e -> updateSeatsLabel());
        spSem.addChangeListener(e -> updateSeatsLabel());

        regBtn.addActionListener(e -> {
            Student s = (Student) cbStudent.getSelectedItem();
            Subject sb = (Subject) cbSubject.getSelectedItem();
            int y = (Integer) spYear.getValue();
            int sem = (Integer) spSem.getValue();
            try{
                service.register(s.getStudentId(), sb.getSubjectCode(), y, sem);
                status.append("✅ " + s + " registered " + sb + " (" + y + "/" + sem + ")\n");
                refreshHistoryTable(); refreshMissingTable();
                updateSeatsLabel();
            }catch(Exception ex){
                status.append("❌ Registration failed: " + ex.getMessage() + "\n");
            }
        });

        updateCurriculumFilter();
        updateSubjectCombo();
        return root;
    }
    private void updateCurriculumFilter(){
        Student s = (Student) cbStudent.getSelectedItem();

        List<String> codes = store.allSubjects().stream()
                .map(Subject::getCurriculumCode)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement("Student's");
        m.addElement("All");        // แสดงทุกหลักสูตร
        for (String c : codes) m.addElement(c);
        cbCurFilter.setModel(m);
        cbCurFilter.setSelectedItem("Student's");

        // ถ้า student ไม่มี curriculum ให้ default เป็น All
        if (s != null && (s.getCurriculumCode() == null || s.getCurriculumCode().isBlank())) {
            cbCurFilter.setSelectedItem("All");
        }
    }

    /** กรองรายวิชาตาม filter */
    private void updateSubjectCombo(){
        Student s = (Student) cbStudent.getSelectedItem();
        String filter = (String) cbCurFilter.getSelectedItem();

        List<Subject> list = store.allSubjects().stream()
                .filter(sb -> {
                    if (filter == null || "All".equals(filter)) {
                        return true;
                    } else if ("Student's".equals(filter)) {
                        // ถ้า student ไม่มี curriculum -> เท่ากับ All
                        if (s == null || s.getCurriculumCode() == null || s.getCurriculumCode().isBlank())
                            return true;
                        return sb.getCurriculumCode().equals(s.getCurriculumCode());
                    } else {
                        return sb.getCurriculumCode().equals(filter);
                    }
                })
                .sorted(Comparator.comparing(Subject::getSubjectCode))
                .collect(Collectors.toList());

        cbSubject.setModel(new DefaultComboBoxModel<>(list.toArray(new Subject[0])));
        updateInfoLabels();
    }

    private void updateInfoLabels(){
        Subject sb = (Subject) cbSubject.getSelectedItem();
        if (sb==null){
            lbPrereq.setText("Prerequisite: -");
            lbSeats.setText("Seats: -");
            return;
        }
        String pre = sb.getPrereqCode();
        if (pre==null) {
            lbPrereq.setText("Prerequisite: none");
        } else {
            String pname = store.findSubject(pre).map(Subject::getName).orElse("?");
            lbPrereq.setText("Prerequisite: " + pre + " - " + pname);
        }
        updateSeatsLabel();
    }

    private void updateSeatsLabel(){
        Subject sb = (Subject) cbSubject.getSelectedItem();
        if (sb==null){ lbSeats.setText("Seats: -"); return; }
        int y   = (Integer) spYear.getValue();
        int sem = (Integer) spSem.getValue();
        long taken = store.countSeats(sb.getSubjectCode(), y, sem);
        if (sb.getMaxSeats() <= 0)
            lbSeats.setText("Seats: unlimited (" + taken + " registered)");
        else
            lbSeats.setText("Seats: " + taken + " / " + sb.getMaxSeats());
    }

    // ---------- History panel ----------
    private JPanel buildHistoryPanel(){
        JPanel p = new JPanel(new BorderLayout(8,8));
        histModel = new DefaultTableModel(new Object[]{"Year","Sem","Subject","Name"}, 0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable table = new JTable(histModel);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        cbHistStudent = new JComboBox<>(store.allStudents().toArray(new Student[0]));
        JButton refresh = new JButton("Refresh");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Student")); top.add(cbHistStudent); top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        refresh.addActionListener(e -> refreshHistoryTable());
        refreshHistoryTable();
        return p;
    }
    private void refreshHistoryTable(){
        histModel.setRowCount(0);
        Student s = (Student) cbHistStudent.getSelectedItem();
        if (s==null) return;
        for (Registration r : store.regsOf(s.getStudentId())) {
            Subject sb = store.findSubject(r.getSubjectCode()).orElse(null);
            histModel.addRow(new Object[]{ r.getAcadYear(), r.getSemester(),
                r.getSubjectCode(), sb==null?"?":sb.getName()});
        }
    }

    // ---------- Missing panel ----------
    private JPanel buildMissingPanel(){
        JPanel p = new JPanel(new BorderLayout(8,8));
        missModel = new DefaultTableModel(new Object[]{"Subject","Name","Required?"}, 0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable table = new JTable(missModel);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        cbMissStudent = new JComboBox<>(store.allStudents().toArray(new Student[0]));
        JButton refresh = new JButton("Refresh");
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Student")); top.add(cbMissStudent); top.add(refresh);
        p.add(top, BorderLayout.NORTH);

        refresh.addActionListener(e -> refreshMissingTable());
        refreshMissingTable();
        return p;
    }

    private void refreshMissingTable(){
        missModel.setRowCount(0);
        Student s = (Student) cbMissStudent.getSelectedItem();
        if (s==null) return;

        java.util.Set<String> taken = store.regsOf(s.getStudentId()).stream()
                .map(Registration::getSubjectCode).collect(Collectors.toSet());

        store.allSubjects().stream()
                .filter(sb -> s.getCurriculumCode()==null || s.getCurriculumCode().isBlank()
                        || sb.getCurriculumCode().equals(s.getCurriculumCode()))
                .filter(sb -> !taken.contains(sb.getSubjectCode()))
                .sorted(Comparator.comparing(Subject::getSubjectCode))
                .forEach(sb -> missModel.addRow(new Object[]{
                        sb.getSubjectCode(), sb.getName(), sb.isRequired()?"Yes":"No"
                }));
    }

    private void refreshCombos(){
        cbStudent.setModel(new DefaultComboBoxModel<>(store.allStudents().toArray(new Student[0])));
        cbHistStudent.setModel(new DefaultComboBoxModel<>(store.allStudents().toArray(new Student[0])));
        cbMissStudent.setModel(new DefaultComboBoxModel<>(store.allStudents().toArray(new Student[0])));
    }
}

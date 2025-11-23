import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<Student> students = new ArrayList<>();
    private static DefaultTableModel tableModel;
    private static JTable table;
    private static JTextField searchField;
    
    // DAO объекты
    private static StudentDAO studentDAO;
    private static GradeDAO gradeDAO;
    private static AttendanceDAO attendanceDAO;
    private static DisciplineDAO disciplineDAO;
    private static GroupDAO groupDAO;
    private static FacultyDAO facultyDAO;
    
    private static List<String> facultiesList = new ArrayList<>();
    private static List<String> groupsList = new ArrayList<>();
    private static List<Discipline> disciplinesList = new ArrayList<>();

    public static void main(String[] args) {
        // Инициализация подключения к БД
        try {
            initializeDatabase();
            SwingUtilities.invokeLater(Main::createAndShowGUI);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Ошибка подключения к базе данных:\n" + e.getMessage() + 
                "\n\nПроверьте:\n1. Запущен ли PostgreSQL\n2. Создана ли БД students_db\n3. Правильность параметров подключения в DatabaseConnection.java",
                "Ошибка подключения", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private static void initializeDatabase() throws SQLException {
        studentDAO = new StudentDAO();
        gradeDAO = new GradeDAO();
        attendanceDAO = new AttendanceDAO();
        disciplineDAO = new DisciplineDAO();
        groupDAO = new GroupDAO();
        facultyDAO = new FacultyDAO();
        
        // Загрузка справочников
        facultiesList = facultyDAO.getAllFacultyNames();
        groupsList = groupDAO.getAllGroupNumbers();
        disciplinesList = disciplineDAO.getAllDisciplines();
        
        // Загрузка студентов из БД
        loadStudentsFromDatabase();
    }
    
    private static void loadStudentsFromDatabase() {
        try {
            students.clear();
            students.addAll(studentDAO.getAllStudents());
            
            // Загрузка оценок и посещаемости для каждого студента
            for (Student student : students) {
                student.getGrades().clear();
                student.getAttendanceRecords().clear();
                student.getGrades().addAll(gradeDAO.getGradesByStudentId(student.getId()));
                student.getAttendanceRecords().addAll(attendanceDAO.getAttendanceByStudentId(student.getId()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка загрузки данных: " + e.getMessage(), 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Деканат — Учёт успеваемости и посещаемости студентов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 750);
        frame.setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ФИО", "Зачётка", "Группа", "Факультет", "Статус", "Бюджет"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshTable();

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Поиск
        JPanel topPanel = new JPanel();
        searchField = new JTextField(30);
        JButton searchBtn = new JButton("Поиск");
        searchBtn.addActionListener(e -> filterStudents(searchField.getText()));
        topPanel.add(new JLabel("Поиск:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        frame.add(topPanel, BorderLayout.NORTH);

        // Кнопки
        JPanel panel = new JPanel();
        JButton add = new JButton("Добавить");
        JButton edit = new JButton("Редактировать");
        JButton delete = new JButton("Удалить");
        JButton grades = new JButton("Оценки и посещаемость");
        JButton report = new JButton("Справка");
        JButton exit = new JButton("Выход");

        add.addActionListener(e -> showStudentDialog(frame, null));
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) showStudentDialog(frame, students.get(table.convertRowIndexToModel(row)));
        });
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && JOptionPane.showConfirmDialog(frame, 
                "Удалить студента?\nВсе связанные оценки и записи посещаемости также будут удалены!", 
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Student student = students.get(table.convertRowIndexToModel(row));
                    if (studentDAO.deleteStudent(student.getId())) {
                        loadStudentsFromDatabase();
                        refreshTable();
                        JOptionPane.showMessageDialog(frame, "Студент успешно удалён!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Ошибка при удалении!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка удаления: " + ex.getMessage(), 
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        grades.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) showGradesDialog(frame, students.get(table.convertRowIndexToModel(row)));
        });
        report.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) generateAndSaveReport(students.get(table.convertRowIndexToModel(row)));
        });
        exit.addActionListener(e -> {
            DatabaseConnection.closeConnection();
            System.exit(0);
        });

        panel.add(add); panel.add(edit); panel.add(delete);
        panel.add(grades); panel.add(report); panel.add(exit);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void refreshTable() {
        tableModel.setRowCount(0);
        try {
            for (Student s : students) {
                String groupNumber = groupDAO.getGroupNumberByStudentId(s.getId());
                String facultyName = facultyDAO.getFacultyNameByStudentId(s.getId());
                tableModel.addRow(new Object[]{
                        s.getFio(),
                        s.getRecordBookNumber(),
                        groupNumber != null ? groupNumber : "—",
                        facultyName != null ? facultyName : "—",
                        s.getStatus(),
                        s.isBudget() ? "Да" : "Нет"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка обновления таблицы: " + e.getMessage(), 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void filterStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            refreshTable();
            return;
        }
        
        try {
            List<Student> filtered = studentDAO.searchStudents(query);
            tableModel.setRowCount(0);
            for (Student s : filtered) {
                String groupNumber = groupDAO.getGroupNumberByStudentId(s.getId());
                String facultyName = facultyDAO.getFacultyNameByStudentId(s.getId());
                tableModel.addRow(new Object[]{
                        s.getFio(),
                        s.getRecordBookNumber(),
                        groupNumber != null ? groupNumber : "—",
                        facultyName != null ? facultyName : "—",
                        s.getStatus(),
                        s.isBudget() ? "Да" : "Нет"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка поиска: " + e.getMessage(), 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showStudentDialog(JFrame parent, Student student) {
        boolean isNew = student == null;
        Student temp = isNew ? new Student(0, "", LocalDate.now(), "", "", "", "", "", LocalDate.now(), "", "обучается", true) : student;

        JDialog d = new JDialog(parent, isNew ? "Новый студент" : "Редактирование", true);
        d.setLayout(new GridLayout(0, 2, 10, 10));
        d.setSize(500, 500);

        JTextField fio = new JTextField(temp.getFio());
        JTextField recordBook = new JTextField(temp.getRecordBookNumber());
        JTextField status = new JTextField(temp.getStatus());
        JComboBox<String> groupBox = new JComboBox<>(groupsList.toArray(new String[0]));
        JComboBox<String> facultyBox = new JComboBox<>(facultiesList.toArray(new String[0]));
        JCheckBox budget = new JCheckBox("Бюджет", temp.isBudget());
        
        // Установка текущих значений
        if (!isNew) {
            try {
                String currentGroup = groupDAO.getGroupNumberByStudentId(temp.getId());
                if (currentGroup != null) {
                    groupBox.setSelectedItem(currentGroup);
                }
                String currentFaculty = facultyDAO.getFacultyNameByStudentId(temp.getId());
                if (currentFaculty != null) {
                    facultyBox.setSelectedItem(currentFaculty);
                }
            } catch (SQLException ex) {
                // Игнорируем ошибку
            }
        }

        d.add(new JLabel("ФИО:")); d.add(fio);
        d.add(new JLabel("Зачётка:")); d.add(recordBook);
        d.add(new JLabel("Группа:")); d.add(groupBox);
        d.add(new JLabel("Факультет:")); d.add(facultyBox);
        d.add(new JLabel("Статус:")); d.add(status);
        d.add(new JLabel("Бюджет:")); d.add(budget);

        JButton save = new JButton("Сохранить");
        save.addActionListener(e -> {
            if (fio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(d, "Введите ФИО!");
                return;
            }
            if (recordBook.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(d, "Введите номер зачётной книжки!");
                return;
            }
            
            try {
                String selectedGroup = (String) groupBox.getSelectedItem();
                if (isNew) {
                    Student ns = new Student(0, fio.getText().trim(), LocalDate.now(), "м", "", "",
                            recordBook.getText().trim(), "", LocalDate.now(), "", status.getText().trim(), budget.isSelected());
                    int newId = studentDAO.insertStudent(ns, selectedGroup);
                    if (newId > 0) {
                        ns = studentDAO.getStudentById(newId);
                        students.add(ns);
                        JOptionPane.showMessageDialog(d, "Студент успешно добавлен!");
                    } else {
                        JOptionPane.showMessageDialog(d, "Ошибка при добавлении студента!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    temp.setFio(fio.getText().trim());
                    temp.setRecordBookNumber(recordBook.getText().trim());
                    temp.setStatus(status.getText().trim());
                    temp.setBudget(budget.isSelected());
                    if (studentDAO.updateStudent(temp, selectedGroup)) {
                        JOptionPane.showMessageDialog(d, "Данные успешно обновлены!");
                    } else {
                        JOptionPane.showMessageDialog(d, "Ошибка при обновлении данных!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                loadStudentsFromDatabase();
                refreshTable();
                filterStudents(searchField.getText());
                d.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(d, "Ошибка сохранения: " + ex.getMessage(), 
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        d.add(new JLabel()); d.add(save);
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }

    private static void showGradesDialog(JFrame parent, Student student) {
        final int studentId = student.getId();
        JDialog d = new JDialog(parent, "Оценки и посещаемость: " + student.getFio(), true);
        d.setSize(1200, 700);

        JTabbedPane tabs = new JTabbedPane();

        // Функция для обновления данных студента
        Runnable updateStudentData = () -> {
            try {
                Student updated = studentDAO.getStudentById(studentId);
                if (updated != null) {
                    updated.getGrades().clear();
                    updated.getAttendanceRecords().clear();
                    updated.getGrades().addAll(gradeDAO.getGradesByStudentId(studentId));
                    updated.getAttendanceRecords().addAll(attendanceDAO.getAttendanceByStudentId(studentId));
                    int index = students.indexOf(students.stream().filter(s -> s.getId() == studentId).findFirst().orElse(null));
                    if (index >= 0) {
                        students.set(index, updated);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(d, "Ошибка обновления данных: " + ex.getMessage(), 
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        };

        // Оценки
        DefaultTableModel gm = new DefaultTableModel(new String[]{"Дисциплина", "Оценка", "Дата", "Попытка", "Долг"}, 0);
        Runnable refreshGrades = () -> {
            gm.setRowCount(0);
            Student currentStudent = students.stream().filter(s -> s.getId() == studentId).findFirst().orElse(null);
            if (currentStudent != null) {
                for (Grade g : currentStudent.getGrades()) {
                    gm.addRow(new Object[]{g.getDiscipline().getName(), g.getValue() + " (" + g.getInWords() + ")", g.getDate(), g.getAttemptNumber(), g.isDebt() ? "Да" : "Нет"});
                }
            }
        };
        refreshGrades.run();
        tabs.addTab("Оценки", new JScrollPane(new JTable(gm)));

        // Посещаемость
        DefaultTableModel am = new DefaultTableModel(new String[]{"Дисциплина", "Дата", "Пара", "Статус"}, 0);
        Runnable refreshAttendance = () -> {
            am.setRowCount(0);
            Student currentStudent = students.stream().filter(s -> s.getId() == studentId).findFirst().orElse(null);
            if (currentStudent != null) {
                for (Attendance a : currentStudent.getAttendanceRecords()) {
                    am.addRow(new Object[]{a.getDiscipline().getName(), a.getDate(), a.getLessonNumber(), a.isPresent() ? "присутствовал" : "отсутствовал (" + a.getReasonIfAbsent() + ")"});
                }
            }
        };
        refreshAttendance.run();
        tabs.addTab("Посещаемость", new JScrollPane(new JTable(am)));

        // Добавление оценки
        JPanel gp = new JPanel();
        JButton addGrade = new JButton("Добавить оценку");
        addGrade.addActionListener(e -> {
            JDialog dg = new JDialog(d, "Новая оценка", true);
            dg.setLayout(new GridLayout(0, 2));
            
            String[] discNames = disciplinesList.stream().map(Discipline::getName).toArray(String[]::new);
            JComboBox<String> disc = new JComboBox<>(discNames);
            JComboBox<String> mark = new JComboBox<>(new String[]{"5", "4", "3", "2", "неуд"});
            JTextField date = new JTextField(LocalDate.now().toString());
            JTextField attempt = new JTextField("1");
            JTextField teacher = new JTextField("Преподаватель");
            JCheckBox debt = new JCheckBox("Долг");

            dg.add(new JLabel("Дисциплина:")); dg.add(disc);
            dg.add(new JLabel("Оценка:")); dg.add(mark);
            dg.add(new JLabel("Дата:")); dg.add(date);
            dg.add(new JLabel("Попытка:")); dg.add(attempt);
            dg.add(new JLabel("Преподаватель:")); dg.add(teacher);
            dg.add(new JLabel("Долг:")); dg.add(debt);

            JButton saveG = new JButton("Сохранить");
            saveG.addActionListener(ev -> {
                try {
                    Discipline discObj = disciplinesList.stream()
                        .filter(discipline -> discipline.getName().equals(disc.getSelectedItem()))
                        .findFirst()
                        .orElse(new Discipline("", (String)disc.getSelectedItem(), 72, 0, 0, 0, "экзамен", 1));
                    
                    String word = switch(mark.getSelectedItem().toString()) {
                        case "5" -> "отлично";
                        case "4" -> "хорошо";
                        case "3" -> "удовлетворительно";
                        case "2" -> "неудовлетворительно";
                        default -> "незачёт";
                    };
                    int value = mark.getSelectedItem().toString().equals("неуд") ? 2 : Integer.parseInt(mark.getSelectedItem().toString());
                    Grade g = new Grade(value, word, LocalDate.parse(date.getText()), 
                        Integer.parseInt(attempt.getText()), teacher.getText(), debt.isSelected(), discObj);
                    
                    gradeDAO.insertGrade(g, studentId);
                    updateStudentData.run();
                    refreshGrades.run();
                    dg.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dg, "Ошибка: " + ex.getMessage(), 
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });
            dg.add(saveG);
            dg.pack();
            dg.setVisible(true);
        });
        gp.add(addGrade);

        JButton addAtt = new JButton("Добавить посещение");
        addAtt.addActionListener(e -> {
            JDialog da = new JDialog(d, "Новое посещение", true);
            da.setLayout(new GridLayout(0, 2));
            
            String[] discNames = disciplinesList.stream().map(Discipline::getName).toArray(String[]::new);
            JComboBox<String> disc = new JComboBox<>(discNames);
            JTextField date = new JTextField(LocalDate.now().toString());
            JTextField pair = new JTextField("1");
            JComboBox<String> status = new JComboBox<>(new String[]{"присутствовал", "отсутствовал"});
            JTextField reason = new JTextField();

            da.add(new JLabel("Дисциплина:")); da.add(disc);
            da.add(new JLabel("Дата:")); da.add(date);
            da.add(new JLabel("Пара:")); da.add(pair);
            da.add(new JLabel("Статус:")); da.add(status);
            da.add(new JLabel("Причина (если отсутствовал):")); da.add(reason);

            JButton saveA = new JButton("Сохранить");
            saveA.addActionListener(ev -> {
                try {
                    Discipline discObj = disciplinesList.stream()
                        .filter(discipline -> discipline.getName().equals(disc.getSelectedItem()))
                        .findFirst()
                        .orElse(new Discipline("", (String)disc.getSelectedItem(), 72, 0, 0, 0, "зачёт", 1));
                    
                    Attendance a = new Attendance(LocalDate.parse(date.getText()), Integer.parseInt(pair.getText()),
                            status.getSelectedItem().equals("присутствовал"), reason.getText(), discObj);
                    
                    attendanceDAO.insertAttendance(a, studentId);
                    updateStudentData.run();
                    refreshAttendance.run();
                    da.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(da, "Ошибка: " + ex.getMessage(), 
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            });
            da.add(saveA);
            da.pack();
            da.setVisible(true);
        });
        gp.add(addAtt);

        d.add(gp, BorderLayout.SOUTH);
        d.add(tabs);
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }

    private static void generateAndSaveReport(Student s) {
        final String report = buildReport(s);

        JDialog d = new JDialog((Frame)null, "Справка — " + s.getFio(), true);
        JTextArea ta = new JTextArea(report, 35, 100);
        ta.setEditable(false);
        d.add(new JScrollPane(ta));

        JButton save = new JButton("Сохранить в файл");
        save.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(d) == JFileChooser.APPROVE_OPTION) {
                try (PrintWriter pw = new PrintWriter(fc.getSelectedFile() + ".txt")) {
                    pw.print(report);
                    JOptionPane.showMessageDialog(d, "Сохранено!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(d, "Ошибка!");
                }
            }
        });
        d.add(save, BorderLayout.SOUTH);
        d.pack();
        d.setVisible(true);
    }

    private static String buildReport(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append("АКАДЕМИЧЕСКАЯ СПРАВКА\n".repeat(1));
        sb.append("Студент: ").append(s.getFio()).append("\n");
        sb.append("Зачётная книжка: ").append(s.getRecordBookNumber()).append("\n");
        sb.append("Статус: ").append(s.getStatus()).append("\n");
        sb.append("Форма обучения: ").append(s.isBudget() ? "Бюджетная" : "Контрактная").append("\n\n");
        sb.append("ОЦЕНКИ:\n");
        for (Grade g : s.getGrades()) {
            sb.append(String.format("%s — %d (%s), %s%s\n",
                    g.getDiscipline().getName(), g.getValue(), g.getInWords(), g.getDate(), g.isDebt() ? " [ДОЛГ]" : ""));
        }
        sb.append("\nПОСЕЩАЕМОСТЬ:\n");
        for (Attendance a : s.getAttendanceRecords()) {
            sb.append(String.format("%s | %s | пара %d | %s%s\n",
                    a.getDiscipline().getName(), a.getDate(), a.getLessonNumber(),
                    a.isPresent() ? "присутствовал" : "отсутствовал",
                    a.getReasonIfAbsent().isEmpty() ? "" : " (" + a.getReasonIfAbsent() + ")"));
        }
        return sb.toString();
    }
}
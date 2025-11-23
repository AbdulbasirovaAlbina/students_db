import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String recordBookNumber;
    private String studentIdNumber;
    private LocalDate enrollmentOrderDate;
    private String enrollmentOrderNumber;
    private String status;
    private boolean isBudget;

    private final List<Grade> grades = new ArrayList<>();
    private final List<Attendance> attendanceRecords = new ArrayList<>();

    public Student(int id, String fio, LocalDate birthDate, String gender, String phone, String email,
                   String recordBookNumber, String studentIdNumber, LocalDate enrollmentOrderDate,
                   String enrollmentOrderNumber, String status, boolean isBudget) {
        super(id, fio, birthDate, gender, phone, email);
        this.recordBookNumber = recordBookNumber;
        this.studentIdNumber = studentIdNumber;
        this.enrollmentOrderDate = enrollmentOrderDate;
        this.enrollmentOrderNumber = enrollmentOrderNumber;
        this.status = status;
        this.isBudget = isBudget;
    }

    // геттеры и сеттеры + методы работы со списками
    public String getRecordBookNumber() { return recordBookNumber; }
    public void setRecordBookNumber(String recordBookNumber) { this.recordBookNumber = recordBookNumber; }
    public String getStudentIdNumber() { return studentIdNumber; }
    public void setStudentIdNumber(String studentIdNumber) { this.studentIdNumber = studentIdNumber; }
    public LocalDate getEnrollmentOrderDate() { return enrollmentOrderDate; }
    public void setEnrollmentOrderDate(LocalDate enrollmentOrderDate) { this.enrollmentOrderDate = enrollmentOrderDate; }
    public String getEnrollmentOrderNumber() { return enrollmentOrderNumber; }
    public void setEnrollmentOrderNumber(String enrollmentOrderNumber) { this.enrollmentOrderNumber = enrollmentOrderNumber; }
    public boolean isBudget() { return isBudget; }
    public void setBudget(boolean budget) { isBudget = budget; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void addGrade(Grade grade) { grades.add(grade); }
    public void addAttendance(Attendance att) { attendanceRecords.add(att); }
    public List<Grade> getGrades() { return grades; }
    public List<Attendance> getAttendanceRecords() { return attendanceRecords; }

    @Override
    public String toString() {
        return super.toString() + " | Зачётка: " + recordBookNumber + " | " + (isBudget ? "бюджет" : "контракт");
    }
}
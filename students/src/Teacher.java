import java.time.LocalDate;

public class Teacher extends Person {
    private String tabNumber;
    private String position;
    private String academicDegree;
    private String department;

    public Teacher(int id, String fio, LocalDate birthDate, String gender,
                   String phone, String email, String tabNumber, String position,
                   String academicDegree, String department) {
        super(id, fio, birthDate, gender, phone, email);
        this.tabNumber = tabNumber;
        this.position = position;
        this.academicDegree = academicDegree;
        this.department = department;
    }

    // геттеры и сеттеры
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getAcademicDegree() { return academicDegree; }
    public void setAcademicDegree(String academicDegree) { this.academicDegree = academicDegree; }

    @Override
    public String toString() {
        return super.toString() + " | " + position + (academicDegree.isBlank() ? "" : ", " + academicDegree);
    }
}
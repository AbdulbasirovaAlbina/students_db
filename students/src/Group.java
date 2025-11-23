public class Group {
    private String number;
    private int course;
    private int yearOfAdmission;
    private int graduationYear;
    private String curatorFIO;

    public Group(String number, int course, int yearOfAdmission, int graduationYear, String curatorFIO) {
        this.number = number;
        this.course = course;
        this.yearOfAdmission = yearOfAdmission;
        this.graduationYear = graduationYear;
        this.curatorFIO = curatorFIO;
    }

    // геттеры и сеттеры
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public int getCourse() { return course; }
    public void setCourse(int course) { this.course = course; }
    public int getYearOfAdmission() { return yearOfAdmission; }
    public void setYearOfAdmission(int yearOfAdmission) { this.yearOfAdmission = yearOfAdmission; }
    public int getGraduationYear() { return graduationYear; }
    public void setGraduationYear(int graduationYear) { this.graduationYear = graduationYear; }
    public String getCuratorFIO() { return curatorFIO; }
    public void setCuratorFIO(String curatorFIO) { this.curatorFIO = curatorFIO; }

    @Override
    public String toString() {
        return "Группа " + number + " (" + course + " курс)";
    }
}
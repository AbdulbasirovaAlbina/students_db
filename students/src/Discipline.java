public class Discipline {
    private String index;
    private String name;
    private int totalHours, lectureHours, practiceHours, labHours;
    private String controlType;
    private int semester;

    public Discipline(String index, String name, int totalHours, int lectureHours,
                      int practiceHours, int labHours, String controlType, int semester) {
        this.index = index;
        this.name = name;
        this.totalHours = totalHours;
        this.lectureHours = lectureHours;
        this.practiceHours = practiceHours;
        this.labHours = labHours;
        this.controlType = controlType;
        this.semester = semester;
    }

    // геттеры
    public String getIndex() { return index; }
    public String getName() { return name; }
    public String getControlType() { return controlType; }
    public int getSemester() { return semester; }

    @Override
    public String toString() {
        return index + " " + name + " (" + controlType + ")";
    }
}
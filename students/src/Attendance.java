
import java.time.LocalDate;

public class Attendance {
    private LocalDate date;
    private int lessonNumber;
    private boolean present;
    private String reasonIfAbsent;
    private Discipline discipline;

    public Attendance(LocalDate date, int lessonNumber, boolean present,
                      String reasonIfAbsent, Discipline discipline) {
        this.date = date;
        this.lessonNumber = lessonNumber;
        this.present = present;
        this.reasonIfAbsent = reasonIfAbsent != null ? reasonIfAbsent : "";
        this.discipline = discipline;
    }

    public LocalDate getDate() { return date; }
    public int getLessonNumber() { return lessonNumber; }
    public boolean isPresent() { return present; }
    public String getReasonIfAbsent() { return reasonIfAbsent; }
    public Discipline getDiscipline() { return discipline; }

    @Override
    public String toString() {
        String status = present ? "присутствовал" : "отсутствовал";
        if (!present && !reasonIfAbsent.isEmpty()) status += " (" + reasonIfAbsent + ")";
        return discipline.getName() + " | " + date + " | пара " + lessonNumber + " | " + status;
    }
}
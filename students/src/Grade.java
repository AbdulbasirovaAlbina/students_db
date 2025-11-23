import java.time.LocalDate;

public class Grade {
    private int value;
    private String inWords;
    private LocalDate date;
    private int attemptNumber;
    private String teacherFIO;
    private boolean isDebt;
    private Discipline discipline;

    public Grade(int value, String inWords, LocalDate date, int attemptNumber,
                 String teacherFIO, boolean isDebt, Discipline discipline) {
        this.value = value;
        this.inWords = inWords;
        this.date = date;
        this.attemptNumber = attemptNumber;
        this.teacherFIO = teacherFIO;
        this.isDebt = isDebt;
        this.discipline = discipline;
    }

    public int getValue() { return value; }
    public String getInWords() { return inWords; }
    public LocalDate getDate() { return date; }
    public int getAttemptNumber() { return attemptNumber; }
    public String getTeacherFIO() { return teacherFIO; }
    public boolean isDebt() { return isDebt; }
    public Discipline getDiscipline() { return discipline; }

    @Override
    public String toString() {
        return discipline.getName() + ": " + value + " (" + inWords + ") — " + date +
                " (попытка " + attemptNumber + ")" + (isDebt ? " [ДОЛГ]" : "");
    }
}

public class Specialty {
    private String code;
    private String name;
    private String qualification;
    private String formOfStudy;
    private int durationYears;

    public Specialty(String code, String name, String qualification, String formOfStudy, int durationYears) {
        this.code = code;
        this.name = name;
        this.qualification = qualification;
        this.formOfStudy = formOfStudy;
        this.durationYears = durationYears;
    }

    // геттеры и сеттеры
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getFormOfStudy() { return formOfStudy; }
    public void setFormOfStudy(String formOfStudy) { this.formOfStudy = formOfStudy; }
    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }

    @Override
    public String toString() {
        return code + " — " + name;
    }
}
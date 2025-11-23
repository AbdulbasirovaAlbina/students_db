// Faculty.java
public class Faculty {
    private String code;
    private String fullName;
    private String shortName;
    private String deanFIO;

    public Faculty(String code, String fullName, String shortName, String deanFIO) {
        this.code = code;
        this.fullName = fullName;
        this.shortName = shortName;
        this.deanFIO = deanFIO;
    }

    // геттеры и сеттеры
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public String getDeanFIO() { return deanFIO; }
    public void setDeanFIO(String deanFIO) { this.deanFIO = deanFIO; }

    @Override
    public String toString() {
        return shortName + " (" + fullName + ")";
    }
}

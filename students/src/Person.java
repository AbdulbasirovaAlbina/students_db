
import java.time.LocalDate;

public abstract class Person {
    private int id;
    private String fio;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String email;

    protected Person(int id, String fio, LocalDate birthDate, String gender, String phone, String email) {
        this.id = id;
        this.fio = fio;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }

    public int getId() { return id; }
    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return fio + " (ID: " + id + ")";
    }
}
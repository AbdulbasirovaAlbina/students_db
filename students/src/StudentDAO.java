import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, g.number as group_number, f.short_name as faculty_name " +
                     "FROM students s " +
                     "LEFT JOIN groups g ON s.group_id = g.id " +
                     "LEFT JOIN specialties sp ON g.specialty_id = sp.id " +
                     "LEFT JOIN faculties f ON sp.faculty_id = f.id " +
                     "ORDER BY s.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }
        }
        return students;
    }
    
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT s.*, g.number as group_number, f.short_name as faculty_name " +
                     "FROM students s " +
                     "LEFT JOIN groups g ON s.group_id = g.id " +
                     "LEFT JOIN specialties sp ON g.specialty_id = sp.id " +
                     "LEFT JOIN faculties f ON sp.faculty_id = f.id " +
                     "WHERE s.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }
    
    public List<Student> searchStudents(String query) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, g.number as group_number, f.short_name as faculty_name " +
                     "FROM students s " +
                     "LEFT JOIN groups g ON s.group_id = g.id " +
                     "LEFT JOIN specialties sp ON g.specialty_id = sp.id " +
                     "LEFT JOIN faculties f ON sp.faculty_id = f.id " +
                     "WHERE LOWER(s.fio) LIKE LOWER(?) OR s.record_book_number LIKE ? " +
                     "ORDER BY s.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }
        return students;
    }
    
    public int insertStudent(Student student, String groupNumber) throws SQLException {
        String sql = "INSERT INTO students (fio, birth_date, gender, phone, email, " +
                     "record_book_number, student_id_number, enrollment_order_date, " +
                     "enrollment_order_number, status, is_budget, group_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                     "(SELECT id FROM groups WHERE number = ?)) " +
                     "RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFio());
            stmt.setDate(2, student.getBirthDate() != null ? Date.valueOf(student.getBirthDate()) : null);
            stmt.setString(3, student.getGender());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getRecordBookNumber());
            stmt.setString(7, student.getStudentIdNumber());
            stmt.setDate(8, student.getEnrollmentOrderDate() != null ? Date.valueOf(student.getEnrollmentOrderDate()) : null);
            stmt.setString(9, student.getEnrollmentOrderNumber());
            stmt.setString(10, student.getStatus());
            stmt.setBoolean(11, student.isBudget());
            stmt.setString(12, groupNumber != null ? groupNumber : "ИТ-31");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public boolean updateStudent(Student student, String groupNumber) throws SQLException {
        String sql = "UPDATE students SET fio = ?, birth_date = ?, gender = ?, phone = ?, " +
                     "email = ?, record_book_number = ?, student_id_number = ?, " +
                     "enrollment_order_date = ?, enrollment_order_number = ?, status = ?, " +
                     "is_budget = ?, group_id = (SELECT id FROM groups WHERE number = ?) " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFio());
            stmt.setDate(2, student.getBirthDate() != null ? Date.valueOf(student.getBirthDate()) : null);
            stmt.setString(3, student.getGender());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getRecordBookNumber());
            stmt.setString(7, student.getStudentIdNumber());
            stmt.setDate(8, student.getEnrollmentOrderDate() != null ? Date.valueOf(student.getEnrollmentOrderDate()) : null);
            stmt.setString(9, student.getEnrollmentOrderNumber());
            stmt.setString(10, student.getStatus());
            stmt.setBoolean(11, student.isBudget());
            stmt.setString(12, groupNumber != null ? groupNumber : "ИТ-31");
            stmt.setInt(13, student.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student(
            rs.getInt("id"),
            rs.getString("fio"),
            rs.getDate("birth_date") != null ? rs.getDate("birth_date").toLocalDate() : LocalDate.now(),
            rs.getString("gender"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("record_book_number"),
            rs.getString("student_id_number"),
            rs.getDate("enrollment_order_date") != null ? rs.getDate("enrollment_order_date").toLocalDate() : LocalDate.now(),
            rs.getString("enrollment_order_number"),
            rs.getString("status"),
            rs.getBoolean("is_budget")
        );
        return student;
    }
}


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {
    
    public List<String> getAllFacultyNames() throws SQLException {
        List<String> faculties = new ArrayList<>();
        String sql = "SELECT short_name FROM faculties ORDER BY short_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                faculties.add(rs.getString("short_name"));
            }
        }
        return faculties;
    }
    
    public String getFacultyNameByStudentId(int studentId) throws SQLException {
        String sql = "SELECT f.short_name FROM faculties f " +
                     "JOIN specialties sp ON sp.faculty_id = f.id " +
                     "JOIN groups g ON g.specialty_id = sp.id " +
                     "JOIN students s ON s.group_id = g.id " +
                     "WHERE s.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("short_name");
                }
            }
        }
        return null;
    }
}


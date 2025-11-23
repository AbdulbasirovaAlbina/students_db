import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    
    public List<String> getAllGroupNumbers() throws SQLException {
        List<String> groups = new ArrayList<>();
        String sql = "SELECT number FROM groups ORDER BY number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                groups.add(rs.getString("number"));
            }
        }
        return groups;
    }
    
    public String getGroupNumberByStudentId(int studentId) throws SQLException {
        String sql = "SELECT g.number FROM groups g " +
                     "JOIN students s ON s.group_id = g.id " +
                     "WHERE s.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("number");
                }
            }
        }
        return null;
    }
    
    public Integer getGroupIdByNumber(String groupNumber) throws SQLException {
        String sql = "SELECT id FROM groups WHERE number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, groupNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }
}


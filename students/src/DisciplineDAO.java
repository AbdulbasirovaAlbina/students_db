import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplineDAO {
    
    public List<Discipline> getAllDisciplines() throws SQLException {
        List<Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM disciplines ORDER BY index_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                disciplines.add(mapResultSetToDiscipline(rs));
            }
        }
        return disciplines;
    }
    
    public Discipline getDisciplineById(int id) throws SQLException {
        String sql = "SELECT * FROM disciplines WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDiscipline(rs);
                }
            }
        }
        return null;
    }
    
    public Discipline getDisciplineByName(String name) throws SQLException {
        String sql = "SELECT * FROM disciplines WHERE name = ? OR index_code = ? LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDiscipline(rs);
                }
            }
        }
        return null;
    }
    
    private Discipline mapResultSetToDiscipline(ResultSet rs) throws SQLException {
        return new Discipline(
            rs.getString("index_code"),
            rs.getString("name"),
            rs.getInt("total_hours"),
            rs.getInt("lecture_hours"),
            rs.getInt("practice_hours"),
            rs.getInt("lab_hours"),
            rs.getString("control_type"),
            rs.getInt("semester")
        );
    }
}


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    
    public List<Grade> getGradesByStudentId(int studentId) throws SQLException {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT g.*, d.index_code, d.name as discipline_name, d.control_type, d.semester " +
                     "FROM grades g " +
                     "JOIN disciplines d ON g.discipline_id = d.id " +
                     "WHERE g.student_id = ? " +
                     "ORDER BY g.date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapResultSetToGrade(rs));
                }
            }
        }
        return grades;
    }
    
    public int insertGrade(Grade grade, int studentId) throws SQLException {
        String sql = "INSERT INTO grades (student_id, discipline_id, value, in_words, date, " +
                     "attempt_number, teacher_fio, is_debt) " +
                     "VALUES (?, (SELECT id FROM disciplines WHERE index_code = ? OR name = ? LIMIT 1), " +
                     "?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setString(2, grade.getDiscipline().getIndex());
            stmt.setString(3, grade.getDiscipline().getName());
            stmt.setInt(4, grade.getValue());
            stmt.setString(5, grade.getInWords());
            stmt.setDate(6, Date.valueOf(grade.getDate()));
            stmt.setInt(7, grade.getAttemptNumber());
            stmt.setString(8, grade.getTeacherFIO());
            stmt.setBoolean(9, grade.isDebt());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public boolean updateGrade(Grade grade, int gradeId) throws SQLException {
        String sql = "UPDATE grades SET discipline_id = (SELECT id FROM disciplines WHERE index_code = ? OR name = ? LIMIT 1), " +
                     "value = ?, in_words = ?, date = ?, attempt_number = ?, teacher_fio = ?, is_debt = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, grade.getDiscipline().getIndex());
            stmt.setString(2, grade.getDiscipline().getName());
            stmt.setInt(3, grade.getValue());
            stmt.setString(4, grade.getInWords());
            stmt.setDate(5, Date.valueOf(grade.getDate()));
            stmt.setInt(6, grade.getAttemptNumber());
            stmt.setString(7, grade.getTeacherFIO());
            stmt.setBoolean(8, grade.isDebt());
            stmt.setInt(9, gradeId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteGrade(int gradeId) throws SQLException {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gradeId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Grade mapResultSetToGrade(ResultSet rs) throws SQLException {
        Discipline discipline = new Discipline(
            rs.getString("index_code"),
            rs.getString("discipline_name"),
            0, 0, 0, 0,
            rs.getString("control_type"),
            rs.getInt("semester")
        );
        
        return new Grade(
            rs.getInt("value"),
            rs.getString("in_words"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("attempt_number"),
            rs.getString("teacher_fio"),
            rs.getBoolean("is_debt"),
            discipline
        );
    }
}


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    
    public List<Attendance> getAttendanceByStudentId(int studentId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, d.index_code, d.name as discipline_name, d.control_type, d.semester " +
                     "FROM attendance a " +
                     "JOIN disciplines d ON a.discipline_id = d.id " +
                     "WHERE a.student_id = ? " +
                     "ORDER BY a.date DESC, a.lesson_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attendanceList.add(mapResultSetToAttendance(rs));
                }
            }
        }
        return attendanceList;
    }
    
    public int insertAttendance(Attendance attendance, int studentId) throws SQLException {
        String sql = "INSERT INTO attendance (student_id, discipline_id, date, lesson_number, " +
                     "present, reason_if_absent) " +
                     "VALUES (?, (SELECT id FROM disciplines WHERE index_code = ? OR name = ? LIMIT 1), " +
                     "?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setString(2, attendance.getDiscipline().getIndex());
            stmt.setString(3, attendance.getDiscipline().getName());
            stmt.setDate(4, Date.valueOf(attendance.getDate()));
            stmt.setInt(5, attendance.getLessonNumber());
            stmt.setBoolean(6, attendance.isPresent());
            stmt.setString(7, attendance.getReasonIfAbsent());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public boolean updateAttendance(Attendance attendance, int attendanceId) throws SQLException {
        String sql = "UPDATE attendance SET discipline_id = (SELECT id FROM disciplines WHERE index_code = ? OR name = ? LIMIT 1), " +
                     "date = ?, lesson_number = ?, present = ?, reason_if_absent = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, attendance.getDiscipline().getIndex());
            stmt.setString(2, attendance.getDiscipline().getName());
            stmt.setDate(3, Date.valueOf(attendance.getDate()));
            stmt.setInt(4, attendance.getLessonNumber());
            stmt.setBoolean(5, attendance.isPresent());
            stmt.setString(6, attendance.getReasonIfAbsent());
            stmt.setInt(7, attendanceId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean deleteAttendance(int attendanceId) throws SQLException {
        String sql = "DELETE FROM attendance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attendanceId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Discipline discipline = new Discipline(
            rs.getString("index_code"),
            rs.getString("discipline_name"),
            0, 0, 0, 0,
            rs.getString("control_type"),
            rs.getInt("semester")
        );
        
        return new Attendance(
            rs.getDate("date").toLocalDate(),
            rs.getInt("lesson_number"),
            rs.getBoolean("present"),
            rs.getString("reason_if_absent"),
            discipline
        );
    }
}


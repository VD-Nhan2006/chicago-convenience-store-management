package dao;

import connectDB.ConnectDB;
import entity.ChuongTrinhKhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class KhuyenMaiDAO {
    public List<ChuongTrinhKhuyenMai> findAll() throws SQLException {
        String sql = "SELECT maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKhuyenMai AS giaTriKM, "
                + "thoiGianBatDau, thoiGianKetThuc, trangThai "
                + "FROM dbo.ChuongTrinhKhuyenMai ORDER BY maKM";
        List<ChuongTrinhKhuyenMai> promotions = new ArrayList<>();
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                promotions.add(new ChuongTrinhKhuyenMai(
                        result.getString("maKM"),
                        result.getString("tenChuongTrinh"),
                        result.getString("moTa"),
                        result.getString("loaiKhuyenMai"),
                        result.getBigDecimal("giaTriKM"),
                        result.getTimestamp("thoiGianBatDau").toLocalDateTime(),
                        result.getTimestamp("thoiGianKetThuc").toLocalDateTime(),
                        result.getBoolean("trangThai")));
            }
        }
        return promotions;
    }

    public String insert(String name, String description, String type, java.math.BigDecimal value,
            java.time.LocalDateTime start, java.time.LocalDateTime end, boolean active)
            throws SQLException {
        String sql = "INSERT INTO dbo.ChuongTrinhKhuyenMai "
                + "(maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKhuyenMai, "
                + "thoiGianBatDau, thoiGianKetThuc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectDB.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                String code = CodeGenerator.next(
                        connection, "ChuongTrinhKhuyenMai", "maKM", "KM");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, code);
                    statement.setString(2, name);
                    statement.setString(3, description);
                    statement.setString(4, type);
                    statement.setBigDecimal(5, value);
                    statement.setTimestamp(6, java.sql.Timestamp.valueOf(start));
                    statement.setTimestamp(7, java.sql.Timestamp.valueOf(end));
                    statement.setBoolean(8, active);
                    statement.executeUpdate();
                }
                connection.commit();
                return code;
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    public void update(String code, String name, String description, String type,
            java.math.BigDecimal value,
            java.time.LocalDateTime start, java.time.LocalDateTime end, boolean active)
            throws SQLException {
        String sql = "UPDATE dbo.ChuongTrinhKhuyenMai SET tenChuongTrinh = ?, moTa = ?, "
                + "loaiKhuyenMai = ?, giaTriKhuyenMai = ?, thoiGianBatDau = ?, "
                + "thoiGianKetThuc = ?, trangThai = ? WHERE maKM = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, type);
            statement.setBigDecimal(4, value);
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(start));
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(end));
            statement.setBoolean(7, active);
            statement.setString(8, code);
            statement.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM dbo.ChuongTrinhKhuyenMai WHERE maKM = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            statement.executeUpdate();
        }
    }
}

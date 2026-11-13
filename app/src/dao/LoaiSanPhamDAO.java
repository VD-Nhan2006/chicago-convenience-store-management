package dao;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class LoaiSanPhamDAO {
    public List<LoaiSanPham> findAll() throws SQLException {
        String sql = "SELECT maLoai AS maLoaiSanPham, tenLoai AS tenLoaiSanPham, "
                + "moTa, trangThai FROM dbo.LoaiSanPham ORDER BY maLoai";
        List<LoaiSanPham> categories = new ArrayList<>();
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                categories.add(new LoaiSanPham(
                        result.getString("maLoaiSanPham"),
                        result.getString("tenLoaiSanPham"),
                        result.getString("moTa"),
                        result.getBoolean("trangThai")));
            }
        }
        return categories;
    }

    public List<LoaiSanPham> findActive() throws SQLException {
        String sql = "SELECT maLoai AS maLoaiSanPham, tenLoai AS tenLoaiSanPham, moTa, trangThai "
                + "FROM dbo.LoaiSanPham WHERE trangThai = 1 "
                + "ORDER BY tenLoai";
        List<LoaiSanPham> categories = new ArrayList<>();
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                categories.add(new LoaiSanPham(
                        result.getString("maLoaiSanPham"),
                        result.getString("tenLoaiSanPham"),
                        result.getString("moTa"),
                        result.getBoolean("trangThai")));
            }
        }
        return categories;
    }

    public String insert(String name, String description, boolean active) throws SQLException {
        String sql = "INSERT INTO dbo.LoaiSanPham (maLoai, tenLoai, moTa, trangThai) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectDB.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                String code = CodeGenerator.next(
                        connection, "LoaiSanPham", "maLoai", "LSP");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, code);
                    statement.setString(2, name);
                    statement.setString(3, description);
                    statement.setBoolean(4, active);
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

    public void update(String code, String name, String description, boolean active)
            throws SQLException {
        String sql = "UPDATE dbo.LoaiSanPham SET tenLoai = ?, moTa = ?, trangThai = ? "
                + "WHERE maLoai = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setBoolean(3, active);
            statement.setString(4, code);
            statement.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM dbo.LoaiSanPham WHERE maLoai = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            statement.executeUpdate();
        }
    }
}

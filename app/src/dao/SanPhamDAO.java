package dao;

import connectDB.ConnectDB;
import entity.SanPham;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SanPhamDAO {
    public List<SanPham> findAll() throws SQLException {
        String sql = "SELECT sp.maSP, sp.tenSP, sp.maLoai, "
                + "lsp.tenLoai, sp.donViTinh, "
                + "sp.soLuongTon, sp.giaNhap, sp.mucLoiNhuan, sp.giaBan, sp.trangThai "
                + "FROM dbo.SanPham sp INNER JOIN dbo.LoaiSanPham lsp "
                + "ON lsp.maLoai = sp.maLoai "
                + "ORDER BY sp.maSP";
        List<SanPham> products = new ArrayList<>();
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                products.add(new SanPham(
                        result.getString("maSP"),
                        result.getString("tenSP"),
                        result.getString("maLoai"),
                        result.getString("tenLoai"),
                        result.getString("donViTinh"),
                        result.getInt("soLuongTon"),
                        result.getBigDecimal("giaNhap"),
                        result.getBigDecimal("mucLoiNhuan"),
                        result.getBigDecimal("giaBan"),
                        result.getBoolean("trangThai")));
            }
        }
        return products;
    }

    public String insert(String name, String categoryCode, String unit, int quantity,
            BigDecimal importPrice, BigDecimal margin, boolean active) throws SQLException {
        String sql = "INSERT INTO dbo.SanPham "
                +                 "(maSP, tenSP, maLoai, donViTinh, soLuongTon, "
                + "giaNhap, mucLoiNhuan, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectDB.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                String code = CodeGenerator.next(connection, "SanPham", "maSP", "SP");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, code);
                    statement.setString(2, name);
                    statement.setString(3, categoryCode);
                    statement.setString(4, unit);
                    statement.setInt(5, quantity);
                    statement.setBigDecimal(6, importPrice);
                    statement.setBigDecimal(7, margin);
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

    public void update(String code, String name, String categoryCode, String unit, int quantity,
            BigDecimal importPrice, BigDecimal margin, boolean active) throws SQLException {
        String sql = "UPDATE dbo.SanPham SET tenSP = ?, maLoai = ?, donViTinh = ?, "
                + "soLuongTon = ?, giaNhap = ?, mucLoiNhuan = ?, trangThai = ? "
                + "WHERE maSP = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, categoryCode);
            statement.setString(3, unit);
            statement.setInt(4, quantity);
            statement.setBigDecimal(5, importPrice);
            statement.setBigDecimal(6, margin);
            statement.setBoolean(7, active);
            statement.setString(8, code);
            statement.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM dbo.SanPham WHERE maSP = ?";
        try (Connection connection = ConnectDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            statement.executeUpdate();
        }
    }
}

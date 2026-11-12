package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;

public class DangNhapDAO {
    public TaiKhoan timTaiKhoanHopLe(String maNhanVien, String matKhau) throws SQLException {
        String sql = "SELECT nv.maNhanVien, nv.ho, nv.ten, tk.vaiTro "
                + "FROM dbo.TaiKhoan tk JOIN dbo.NhanVien nv ON nv.maNhanVien = tk.maNhanVien "
                + "WHERE tk.maNhanVien = ? AND nv.trangThai = ? "
                + "AND TRY_CONVERT(varbinary(32), tk.matKhau, 2) = HASHBYTES('SHA2_256', "
                + "CONVERT(varchar(max), ? COLLATE Latin1_General_100_CI_AS_SC_UTF8))";
        PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql);
        try {
            stmt.setString(1, maNhanVien);
            stmt.setNString(2, "Đang hoạt động");
            stmt.setNString(3, matKhau);
            ResultSet rs = stmt.executeQuery();
            try {
                if (!rs.next()) {
                    return null;
                }
                NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"));
                nhanVien.setHo(rs.getString("ho"));
                nhanVien.setTen(rs.getString("ten"));
                return new TaiKhoan(nhanVien, null, VaiTro.fromString(rs.getString("vaiTro")));
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }
}

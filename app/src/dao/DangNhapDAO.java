package dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;
import util.AESGCMUtil;

public class DangNhapDAO {
    private static final String DANG_HOAT_DONG = "Đang hoạt động";

    public TaiKhoan timTaiKhoanHopLe(String maNhanVien, String matKhau) throws SQLException {
        String sql = "SELECT nv.maNhanVien, nv.ho, nv.ten, tk.vaiTro, tk.trangThai, tk.matKhau FROM dbo.TaiKhoan tk JOIN dbo.NhanVien nv ON nv.maNhanVien = tk.maNhanVien WHERE tk.maNhanVien = ? AND nv.trangThai = ? AND tk.trangThai = ?";
        
        PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql);
        try {
            stmt.setString(1, maNhanVien);
            stmt.setNString(2, DANG_HOAT_DONG);
            stmt.setNString(3, DANG_HOAT_DONG);
            ResultSet rs = stmt.executeQuery();
            try {
                if (!rs.next()) {
                    return null;
                }
                String matKhauDaMaHoa = rs.getString("matKhau");
                String matKhauGoc;
                try {
                	matKhauGoc = AESGCMUtil.giaiMa(matKhauDaMaHoa);
                }catch (Exception e) {
                	throw new SQLException("Không thể giải mã mật khẩu tài khoản.", e);
				}
                boolean hopLe = MessageDigest.isEqual(matKhau.getBytes(StandardCharsets.UTF_8), matKhauGoc.getBytes(StandardCharsets.UTF_8));
                if(!hopLe) {
                	return null;
                }
                NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"));
                nhanVien.setHo(rs.getString("ho"));
                nhanVien.setTen(rs.getString("ten"));
                return new TaiKhoan(
                        nhanVien,
                        null, 
                        VaiTro.fromString(rs.getString("vaiTro")),
                        rs.getNString("trangThai"));
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }
}
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
	public KhachHangDAO() {
		// TODO Auto-generated constructor stub
	}
    private static final String CHON_COT =
            "SELECT maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi FROM dbo.KhachHang";

    public ArrayList<KhachHang> getAll() throws SQLException {
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(CHON_COT + " ORDER BY maKhachHang");
                ResultSet rs = stmt.executeQuery()) {
            return docDanhSach(rs);
        }
    }

    public ArrayList<KhachHang> timKiem(String tuKhoa) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String mau = "%" + thoatLike(tuKhoa.trim()) + "%";
        String sql = CHON_COT
                + " WHERE maKhachHang LIKE ? ESCAPE '\\'"
                + " OR (ho + N' ' + ten) LIKE ? ESCAPE '\\'"
                + " OR soDienThoai LIKE ? ESCAPE '\\'"
                + " ORDER BY maKhachHang";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, mau);
            stmt.setNString(2, mau);
            stmt.setString(3, mau);
            try (ResultSet rs = stmt.executeQuery()) {
                return docDanhSach(rs);
            }
        }
    }

    public KhachHang getByMa(String maKhachHang) throws SQLException {
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(CHON_COT + " WHERE maKhachHang = ?")) {
            stmt.setString(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? docMotDong(rs) : null;
            }
        }
    }

    public String themMoi(String ho, String ten, String gioiTinh, String sdt, String diaChi) throws SQLException {
        Connection con = ConnectDB.getConnection();
        boolean autoCu = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            String tienTo = "KH" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
            int stt;
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT COALESCE(MAX(TRY_CONVERT(INT, RIGHT(maKhachHang, 3))), 0) + 1 "
                            + "FROM dbo.KhachHang WITH (UPDLOCK, HOLDLOCK) WHERE maKhachHang LIKE ?")) {
                stmt.setString(1, tienTo + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    stt = rs.getInt(1);
                }
            }
            if (stt > 999) {
                throw new IllegalArgumentException("Đã hết mã khách hàng trong ngày.");
            }
            String ma = tienTo + String.format("%03d", stt);
            KhachHang kh = new KhachHang(ma, ho, ten, gioiTinh, sdt, diaChi);
            kiemTraKhongTrungSdt(con, kh.getSoDienThoai(), "");
            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO dbo.KhachHang (maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi) "
                            + "VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, kh.getMaKhachHang());
                stmt.setNString(2, kh.getHo());
                stmt.setNString(3, kh.getTen());
                stmt.setNString(4, kh.getGioiTinh());
                stmt.setString(5, kh.getSoDienThoai());
                stmt.setNString(6, kh.getDiaChi());
                stmt.executeUpdate();
            }
            con.commit();
            return ma;
        } catch (SQLException | RuntimeException e) {
            huyGiaoDich(con, e);
            throw e;
        } finally {
            con.setAutoCommit(autoCu);
        }
    }

    public void capNhat(KhachHang kh) throws SQLException {
        Connection con = ConnectDB.getConnection();
        boolean autoCu = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            kiemTraKhongTrungSdt(con, kh.getSoDienThoai(), kh.getMaKhachHang());
            try (PreparedStatement stmt = con.prepareStatement(
                    "UPDATE dbo.KhachHang SET ho = ?, ten = ?, gioiTinh = ?, soDienThoai = ?, diaChi = ? "
                            + "WHERE maKhachHang = ?")) {
                stmt.setNString(1, kh.getHo());
                stmt.setNString(2, kh.getTen());
                stmt.setNString(3, kh.getGioiTinh());
                stmt.setString(4, kh.getSoDienThoai());
                stmt.setNString(5, kh.getDiaChi());
                stmt.setString(6, kh.getMaKhachHang());
                if (stmt.executeUpdate() != 1) {
                    throw new IllegalArgumentException("Khách hàng không còn tồn tại.");
                }
            }
            con.commit();
        } catch (SQLException | RuntimeException e) {
            huyGiaoDich(con, e);
            throw e;
        } finally {
            con.setAutoCommit(autoCu);
        }
    }

    public void xoa(String maKhachHang) throws SQLException {
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM dbo.KhachHang WHERE maKhachHang = ?")) {
            stmt.setString(1, maKhachHang);
            if (stmt.executeUpdate() != 1) {
                throw new IllegalArgumentException("Khách hàng không còn tồn tại.");
            }
        }
    }

    private void kiemTraKhongTrungSdt(Connection con, String sdt, String maLoaiTru) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT TOP 1 maKhachHang FROM dbo.KhachHang WITH (UPDLOCK, HOLDLOCK) "
                        + "WHERE soDienThoai = ? AND maKhachHang <> ?")) {
            stmt.setString(1, sdt);
            stmt.setString(2, maLoaiTru);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalArgumentException(
                            "Số điện thoại " + sdt + " đã thuộc khách hàng " + rs.getString(1) + ".");
                }
            }
        }
    }

    private void huyGiaoDich(Connection con, Exception goc) {
        try {
            con.rollback();
        } catch (SQLException loiRollback) {
            goc.addSuppressed(loiRollback);
        }
    }

    private ArrayList<KhachHang> docDanhSach(ResultSet rs) throws SQLException {
        ArrayList<KhachHang> ds = new ArrayList<KhachHang>();
        while (rs.next()) {
            ds.add(docMotDong(rs));
        }
        return ds;
    }

    private KhachHang docMotDong(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getString("maKhachHang"),
                rs.getNString("ho"),
                rs.getNString("ten"),
                rs.getNString("gioiTinh"),
                rs.getString("soDienThoai"),
                rs.getNString("diaChi"));
    }

    private static String thoatLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_").replace("[", "\\[");
    }
}

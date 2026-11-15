package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;

public class ThongKeDAO {

    private static final String DA_THANH_TOAN = "Đã thanh toán";

    public static class TongHop {
        private final int soHoaDon;
        private final BigDecimal doanhThu;
        private final BigDecimal tienGiam;
        private final long soSanPham;

        public TongHop(int soHoaDon, BigDecimal doanhThu, BigDecimal tienGiam, long soSanPham) {
            this.soHoaDon = soHoaDon;
            this.doanhThu = doanhThu;
            this.tienGiam = tienGiam;
            this.soSanPham = soSanPham;
        }

        public int getSoHoaDon() {
            return soHoaDon;
        }

        public BigDecimal getDoanhThu() {
            return doanhThu;
        }

        public BigDecimal getTienGiam() {
            return tienGiam;
        }

        public long getSoSanPham() {
            return soSanPham;
        }
    }

    public static class DongHoaDon {
        private final String maHoaDon;
        private final LocalDateTime thoiGianThanhToan;
        private final String tenKhachHang;
        private final String tenNhanVien;
        private final String phuongThucThanhToan;
        private final BigDecimal tienGiam;
        private final BigDecimal tongThanhToan;

        public DongHoaDon(String maHoaDon, LocalDateTime thoiGianThanhToan, String tenKhachHang, String tenNhanVien,
                String phuongThucThanhToan, BigDecimal tienGiam, BigDecimal tongThanhToan) {
            this.maHoaDon = maHoaDon;
            this.thoiGianThanhToan = thoiGianThanhToan;
            this.tenKhachHang = tenKhachHang;
            this.tenNhanVien = tenNhanVien;
            this.phuongThucThanhToan = phuongThucThanhToan;
            this.tienGiam = tienGiam;
            this.tongThanhToan = tongThanhToan;
        }

        public String getMaHoaDon() {
            return maHoaDon;
        }

        public LocalDateTime getThoiGianThanhToan() {
            return thoiGianThanhToan;
        }

        public String getTenKhachHang() {
            return tenKhachHang;
        }

        public String getTenNhanVien() {
            return tenNhanVien;
        }

        public String getPhuongThucThanhToan() {
            return phuongThucThanhToan;
        }

        public BigDecimal getTienGiam() {
            return tienGiam;
        }

        public BigDecimal getTongThanhToan() {
            return tongThanhToan;
        }
    }

    public TongHop tongHop(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Timestamp tu = Timestamp.valueOf(tuNgay.atStartOfDay());
        Timestamp den = Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay());

        int soHoaDon;
        BigDecimal doanhThu;
        BigDecimal tienGiam;
        String sqlHoaDon = "SELECT COUNT(*) AS soHoaDon, "
                + "COALESCE(SUM(tongThanhToan), 0) AS doanhThu, "
                + "COALESCE(SUM(tienDuocGiam), 0) AS tienGiam "
                + "FROM dbo.HoaDon "
                + "WHERE trangThaiThanhToan = ? AND thoiGianThanhToan >= ? AND thoiGianThanhToan < ?";
        try (PreparedStatement stmt = con.prepareStatement(sqlHoaDon)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, tu);
            stmt.setTimestamp(3, den);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                soHoaDon = rs.getInt("soHoaDon");
                doanhThu = rs.getBigDecimal("doanhThu");
                tienGiam = rs.getBigDecimal("tienGiam");
            }
        }

        long soSanPham;
        String sqlSanPham = "SELECT COALESCE(SUM(ct.soLuong), 0) AS soSanPham "
                + "FROM dbo.ChiTietHoaDon ct JOIN dbo.HoaDon h ON h.maHoaDon = ct.maHoaDon "
                + "WHERE h.trangThaiThanhToan = ? AND h.thoiGianThanhToan >= ? AND h.thoiGianThanhToan < ?";
        try (PreparedStatement stmt = con.prepareStatement(sqlSanPham)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, tu);
            stmt.setTimestamp(3, den);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                soSanPham = rs.getLong("soSanPham");
            }
        }
        return new TongHop(soHoaDon, doanhThu, tienGiam, soSanPham);
    }

    public ArrayList<DongHoaDon> layHoaDon(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT hd.maHoaDon, hd.thoiGianThanhToan, hd.phuongThucThanhToan, "
                + "COALESCE(hd.tienDuocGiam, 0) AS tienGiam, COALESCE(hd.tongThanhToan, 0) AS tongThanhToan, "
                + "kh.ho AS khHo, kh.ten AS khTen, nv.ho AS nvHo, nv.ten AS nvTen "
                + "FROM dbo.HoaDon hd "
                + "JOIN dbo.KhachHang kh ON kh.maKhachHang = hd.maKhachHang "
                + "JOIN dbo.NhanVien nv ON nv.maNhanVien = hd.maNhanVien "
                + "WHERE hd.trangThaiThanhToan = ? AND hd.thoiGianThanhToan >= ? AND hd.thoiGianThanhToan < ? "
                + "ORDER BY hd.thoiGianThanhToan DESC, hd.maHoaDon DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, Timestamp.valueOf(tuNgay.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay()));
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<DongHoaDon> ds = new ArrayList<DongHoaDon>();
                while (rs.next()) {
                    ds.add(new DongHoaDon(
                            rs.getString("maHoaDon"),
                            rs.getTimestamp("thoiGianThanhToan").toLocalDateTime(),
                            (rs.getNString("khHo") + " " + rs.getNString("khTen")).trim(),
                            (rs.getNString("nvHo") + " " + rs.getNString("nvTen")).trim(),
                            rs.getNString("phuongThucThanhToan"),
                            rs.getBigDecimal("tienGiam"),
                            rs.getBigDecimal("tongThanhToan")));
                }
                return ds;
            }
        }
    }
}

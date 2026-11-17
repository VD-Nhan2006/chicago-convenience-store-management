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
import java.util.HashMap;

import connectDB.ConnectDB;

public class ThongKeDAO {
	public ThongKeDAO() {
		// TODO Auto-generated constructor stub
	}

    private static final String DA_THANH_TOAN = "Đã thanh toán";

    public static class TongHop {
        private final int soHoaDon;
        private final BigDecimal doanhThu;
        private final BigDecimal tienGiam;
        private final long soSanPham;
        private final BigDecimal loiNhuan;

        public TongHop(int soHoaDon, BigDecimal doanhThu, BigDecimal tienGiam, long soSanPham, BigDecimal loiNhuan) {
            this.soHoaDon = soHoaDon;
            this.doanhThu = doanhThu;
            this.tienGiam = tienGiam;
            this.soSanPham = soSanPham;
            this.loiNhuan = loiNhuan;
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

        public BigDecimal getLoiNhuan() {
            return loiNhuan;
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

    public static class DiemNgay {
        private final LocalDate ngay;
        private final int soHoaDon;
        private final BigDecimal doanhThu;
        private final BigDecimal loiNhuan;

        public DiemNgay(LocalDate ngay, int soHoaDon, BigDecimal doanhThu, BigDecimal loiNhuan) {
            this.ngay = ngay;
            this.soHoaDon = soHoaDon;
            this.doanhThu = doanhThu;
            this.loiNhuan = loiNhuan;
        }

        public LocalDate getNgay() {
            return ngay;
        }

        public int getSoHoaDon() {
            return soHoaDon;
        }

        public BigDecimal getDoanhThu() {
            return doanhThu;
        }

        public BigDecimal getLoiNhuan() {
            return loiNhuan;
        }
    }

    public static class NhomSanPham {
        private final String tenNhom;
        private final BigDecimal doanhThu;

        public NhomSanPham(String tenNhom, BigDecimal doanhThu) {
            this.tenNhom = tenNhom;
            this.doanhThu = doanhThu;
        }

        public String getTenNhom() {
            return tenNhom;
        }

        public BigDecimal getDoanhThu() {
            return doanhThu;
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
        BigDecimal laiGop;
        String sqlSanPham = "SELECT COALESCE(SUM(ct.soLuong), 0) AS soSanPham, "
                + "COALESCE(SUM(ct.soLuong * (ct.giaBan - sp.giaNhap)), 0) AS laiGop "
                + "FROM dbo.ChiTietHoaDon ct "
                + "JOIN dbo.HoaDon h ON h.maHoaDon = ct.maHoaDon "
                + "JOIN dbo.SanPham sp ON sp.maSanPham = ct.maSanPham "
                + "WHERE h.trangThaiThanhToan = ? AND h.thoiGianThanhToan >= ? AND h.thoiGianThanhToan < ?";
        try (PreparedStatement stmt = con.prepareStatement(sqlSanPham)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, tu);
            stmt.setTimestamp(3, den);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                soSanPham = rs.getLong("soSanPham");
                laiGop = rs.getBigDecimal("laiGop");
            }
        }
        return new TongHop(soHoaDon, doanhThu, tienGiam, soSanPham, laiGop.subtract(tienGiam));
    }

    public ArrayList<DiemNgay> theoNgay(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Connection con = ConnectDB.getConnection();
        Timestamp tu = Timestamp.valueOf(tuNgay.atStartOfDay());
        Timestamp den = Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay());

        HashMap<LocalDate, Integer> soHoaDon = new HashMap<LocalDate, Integer>();
        HashMap<LocalDate, BigDecimal> doanhThu = new HashMap<LocalDate, BigDecimal>();
        HashMap<LocalDate, BigDecimal> tienGiam = new HashMap<LocalDate, BigDecimal>();
        HashMap<LocalDate, BigDecimal> laiGop = new HashMap<LocalDate, BigDecimal>();

        String sqlHoaDon = "SELECT CAST(thoiGianThanhToan AS DATE) AS ngay, COUNT(*) AS soHoaDon, "
                + "COALESCE(SUM(tongThanhToan), 0) AS doanhThu, COALESCE(SUM(tienDuocGiam), 0) AS tienGiam "
                + "FROM dbo.HoaDon "
                + "WHERE trangThaiThanhToan = ? AND thoiGianThanhToan >= ? AND thoiGianThanhToan < ? "
                + "GROUP BY CAST(thoiGianThanhToan AS DATE)";
        try (PreparedStatement stmt = con.prepareStatement(sqlHoaDon)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, tu);
            stmt.setTimestamp(3, den);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate ngay = rs.getObject("ngay", LocalDate.class);
                    soHoaDon.put(ngay, rs.getInt("soHoaDon"));
                    doanhThu.put(ngay, rs.getBigDecimal("doanhThu"));
                    tienGiam.put(ngay, rs.getBigDecimal("tienGiam"));
                }
            }
        }

        String sqlLai = "SELECT CAST(h.thoiGianThanhToan AS DATE) AS ngay, "
                + "COALESCE(SUM(ct.soLuong * (ct.giaBan - sp.giaNhap)), 0) AS laiGop "
                + "FROM dbo.ChiTietHoaDon ct "
                + "JOIN dbo.HoaDon h ON h.maHoaDon = ct.maHoaDon "
                + "JOIN dbo.SanPham sp ON sp.maSanPham = ct.maSanPham "
                + "WHERE h.trangThaiThanhToan = ? AND h.thoiGianThanhToan >= ? AND h.thoiGianThanhToan < ? "
                + "GROUP BY CAST(h.thoiGianThanhToan AS DATE)";
        try (PreparedStatement stmt = con.prepareStatement(sqlLai)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, tu);
            stmt.setTimestamp(3, den);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    laiGop.put(rs.getObject("ngay", LocalDate.class), rs.getBigDecimal("laiGop"));
                }
            }
        }

        ArrayList<DiemNgay> ds = new ArrayList<DiemNgay>();
        for (LocalDate ngay = tuNgay; !ngay.isAfter(denNgay); ngay = ngay.plusDays(1)) {
            BigDecimal giam = tienGiam.getOrDefault(ngay, BigDecimal.ZERO);
            BigDecimal lai = laiGop.getOrDefault(ngay, BigDecimal.ZERO).subtract(giam);
            ds.add(new DiemNgay(ngay, soHoaDon.getOrDefault(ngay, 0),
                    doanhThu.getOrDefault(ngay, BigDecimal.ZERO), lai));
        }
        return ds;
    }

    public ArrayList<NhomSanPham> theoNhomSanPham(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT lsp.tenLoaiSanPham AS tenNhom, SUM(ct.thanhTien) AS doanhThu "
                + "FROM dbo.ChiTietHoaDon ct "
                + "JOIN dbo.HoaDon h ON h.maHoaDon = ct.maHoaDon "
                + "JOIN dbo.SanPham sp ON sp.maSanPham = ct.maSanPham "
                + "JOIN dbo.LoaiSanPham lsp ON lsp.maLoaiSanPham = sp.maLoaiSanPham "
                + "WHERE h.trangThaiThanhToan = ? AND h.thoiGianThanhToan >= ? AND h.thoiGianThanhToan < ? "
                + "GROUP BY lsp.maLoaiSanPham, lsp.tenLoaiSanPham "
                + "ORDER BY SUM(ct.thanhTien) DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setNString(1, DA_THANH_TOAN);
            stmt.setTimestamp(2, Timestamp.valueOf(tuNgay.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay()));
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<NhomSanPham> ds = new ArrayList<NhomSanPham>();
                while (rs.next()) {
                    ds.add(new NhomSanPham(rs.getNString("tenNhom"), rs.getBigDecimal("doanhThu")));
                }
                return ds;
            }
        }
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
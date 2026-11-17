package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

public class TimKiemHoaDonDAO {
	public TimKiemHoaDonDAO() {
		// TODO Auto-generated constructor stub
	}

    private static final String CHON_HOA_DON =
            "SELECT hd.maHoaDon, hd.ngayGioLap, hd.phuongThucThanhToan, hd.trangThaiThanhToan, "
            + "hd.tienKhachDua, hd.thoiGianThanhToan, hd.tienDuocGiam, hd.tongThanhToan, hd.maKM, "
            + "hd.maKhachHang, kh.ho AS khHo, kh.ten AS khTen, "
            + "hd.maNhanVien, nv.ho AS nvHo, nv.ten AS nvTen, "
            + "km.tenChuongTrinh, km.loaiKhuyenMai, km.giaTriKM "
            + "FROM dbo.HoaDon hd "
            + "JOIN dbo.KhachHang kh ON kh.maKhachHang = hd.maKhachHang "
            + "JOIN dbo.NhanVien nv ON nv.maNhanVien = hd.maNhanVien "
            + "LEFT JOIN dbo.ChuongTrinhKhuyenMai km ON km.maKM = hd.maKM ";

    public ArrayList<HoaDon> timKiem(String maHoaDon, LocalDate tuNgay, LocalDate denNgay, String tenKhachHang)
            throws SQLException {
        Connection con = ConnectDB.getConnection();
        StringBuilder sql = new StringBuilder(CHON_HOA_DON).append("WHERE 1 = 1");
        ArrayList<Object> thamSo = new ArrayList<Object>();
        if (maHoaDon != null && !maHoaDon.isEmpty()) {
            sql.append(" AND hd.maHoaDon LIKE ? ESCAPE '\\'");
            thamSo.add("%" + thoatLike(maHoaDon) + "%");
        }
        if (tuNgay != null) {
            sql.append(" AND hd.thoiGianThanhToan >= ?");
            thamSo.add(Timestamp.valueOf(tuNgay.atStartOfDay()));
        }
        if (denNgay != null) {
            sql.append(" AND hd.thoiGianThanhToan < ?");
            thamSo.add(Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay()));
        }
        if (tenKhachHang != null && !tenKhachHang.isEmpty()) {
            sql.append(" AND (kh.ho + N' ' + kh.ten) LIKE ? ESCAPE '\\'");
            thamSo.add("%" + thoatLike(tenKhachHang) + "%");
        }
        sql.append(" ORDER BY hd.thoiGianThanhToan DESC, hd.maHoaDon DESC");

        try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < thamSo.size(); i++) {
                Object giaTri = thamSo.get(i);
                if (giaTri instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) giaTri);
                } else {
                    stmt.setNString(i + 1, (String) giaTri);
                }
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<HoaDon> ds = new ArrayList<HoaDon>();
                while (rs.next()) {
                    ds.add(docHoaDon(rs, null));
                }
                return ds;
            }
        }
    }

    public HoaDon layChiTiet(String maHoaDon) throws SQLException {
        Connection con = ConnectDB.getConnection();
        HoaDon hoaDon;
        try (PreparedStatement stmt = con.prepareStatement(CHON_HOA_DON + "WHERE hd.maHoaDon = ?")) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                ChuongTrinhKhuyenMai km = null;
                String maKM = rs.getString("maKM");
                if (maKM != null) {
                    km = new ChuongTrinhKhuyenMai(maKM);
                    km.setTenChuongTrinh(rs.getNString("tenChuongTrinh"));
                    km.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                    km.setGiaTriKM(rs.getBigDecimal("giaTriKM"));
                }
                hoaDon = docHoaDon(rs, km);
            }
        }
        hoaDon.setListChiTietHoaDon(layDongHang(con, maHoaDon));
        return hoaDon;
    }

    private ArrayList<ChiTietHoaDon> layDongHang(Connection con, String maHoaDon) throws SQLException {
        String sql = "SELECT ct.maSanPham, sp.tenSanPham, sp.donViTinh, ct.soLuong, ct.giaBan "
                + "FROM dbo.ChiTietHoaDon ct JOIN dbo.SanPham sp ON sp.maSanPham = ct.maSanPham "
                + "WHERE ct.maHoaDon = ? ORDER BY ct.maSanPham";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<ChiTietHoaDon> dongHang = new ArrayList<ChiTietHoaDon>();
                while (rs.next()) {
                    SanPham sp = new SanPham(rs.getString("maSanPham"));
                    sp.setTenSanPham(rs.getNString("tenSanPham"));
                    sp.setDonViTinh(rs.getNString("donViTinh"));
                    dongHang.add(new ChiTietHoaDon(sp, rs.getInt("soLuong"), rs.getBigDecimal("giaBan")));
                }
                return dongHang;
            }
        }
    }

    private HoaDon docHoaDon(ResultSet rs, ChuongTrinhKhuyenMai km) throws SQLException {
        NhanVien nv = new NhanVien(rs.getString("maNhanVien"));
        nv.setHo(rs.getNString("nvHo"));
        nv.setTen(rs.getNString("nvTen"));
        KhachHang kh = new KhachHang(rs.getString("maKhachHang"));
        kh.setHo(rs.getNString("khHo"));
        kh.setTen(rs.getNString("khTen"));
        HoaDon hoaDon = new HoaDon(rs.getString("maHoaDon"),
                rs.getTimestamp("ngayGioLap").toLocalDateTime(),
                rs.getNString("phuongThucThanhToan"),
                rs.getNString("trangThaiThanhToan"),
                rs.getBigDecimal("tienKhachDua"),
                rs.getTimestamp("thoiGianThanhToan").toLocalDateTime(),
                null, km, nv, kh);
        hoaDon.setSoTienDaLuu(rs.getBigDecimal("tienDuocGiam"), rs.getBigDecimal("tongThanhToan"));
        return hoaDon;
    }

    private static String thoatLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_").replace("[", "\\[");
    }
}

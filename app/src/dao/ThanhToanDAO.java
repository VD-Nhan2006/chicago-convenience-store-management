package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.NhanVien;

public class ThanhToanDAO {
    private static final String HOAT_DONG = "Đang hoạt động";

    public ArrayList<ChuongTrinhKhuyenMai> layKhuyenMai(LocalDate ngay) throws SQLException {
        String sql = "SELECT * FROM dbo.ChuongTrinhKhuyenMai WHERE trangThai = ? "
                + "AND ngayBatDau <= ? AND ngayKetThuc >= ? ORDER BY tenChuongTrinh, maKM";
        PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql);
        try {
            stmt.setNString(1, HOAT_DONG);
            stmt.setDate(2, java.sql.Date.valueOf(ngay));
            stmt.setDate(3, java.sql.Date.valueOf(ngay));
            ResultSet rs = stmt.executeQuery();
            try {
                ArrayList<ChuongTrinhKhuyenMai> danhSach = new ArrayList<ChuongTrinhKhuyenMai>();
                while (rs.next()) {
                    danhSach.add(new ChuongTrinhKhuyenMai(rs.getString("maKM"),
                            rs.getString("tenChuongTrinh"), rs.getString("moTa"),
                            rs.getString("loaiKhuyenMai"), rs.getBigDecimal("giaTriKM"),
                            rs.getDate("ngayBatDau").toLocalDate(), rs.getDate("ngayKetThuc").toLocalDate(),
                            rs.getString("trangThai")));
                }
                return danhSach;
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }

    public ArrayList<NhanVien> layNhanVienHoatDong() throws SQLException {
        String sql = "SELECT maNhanVien, ho, ten FROM dbo.NhanVien "
                + "WHERE trangThai = ? ORDER BY ten, ho, maNhanVien";
        PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql);
        try {
            stmt.setNString(1, HOAT_DONG);
            ResultSet rs = stmt.executeQuery();
            try {
                ArrayList<NhanVien> danhSach = new ArrayList<NhanVien>();
                while (rs.next()) {
                    NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"));
                    nhanVien.setHo(rs.getString("ho"));
                    nhanVien.setTen(rs.getString("ten"));
                    danhSach.add(nhanVien);
                }
                return danhSach;
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }

    public String luuHoaDon(HoaDon hoaDon, String tienTo) throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (!con.getAutoCommit()) {
            throw new SQLException("Kết nối đang thực hiện giao dịch khác.");
        }
        con.setAutoCommit(false);
        boolean ketThucGiaoDich = false;
        try {
            String ma = taoMa(con, tienTo);
            themHoaDon(con, hoaDon, ma);
            luuDongHang(con, hoaDon, ma);
            con.commit();
            ketThucGiaoDich = true;
            return ma;
        } finally {
            if (!ketThucGiaoDich) {
                try {
                    con.rollback();
                } catch (SQLException loiRollback) {
                    ConnectDB.getInstance().disconnect();
                    throw loiRollback;
                }
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException loiKetNoi) {
                // Hóa đơn đã commit hoặc rollback. Đóng kết nối để lần sau dùng kết nối mới.
                ConnectDB.getInstance().disconnect();
            }
        }
    }

    private String taoMa(Connection con, String tienTo) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT MAX(maHoaDon) FROM dbo.HoaDon WHERE maHoaDon LIKE ?");
        try {
            stmt.setString(1, tienTo + "%");
            ResultSet rs = stmt.executeQuery();
            try {
                rs.next();
                String cuoi = rs.getString(1);
                int tiepTheo = cuoi == null ? 1 : Integer.parseInt(cuoi.substring(tienTo.length())) + 1;
                if (tiepTheo > 9999) {
                    throw new IllegalArgumentException("Đã hết số hóa đơn trong ngày.");
                }
                return tienTo + String.valueOf(10000 + tiepTheo).substring(1);
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }

    private void themHoaDon(Connection con, HoaDon hd, String ma) throws SQLException {
        String sql = "INSERT INTO dbo.HoaDon (maHoaDon, ngayGioLap, phuongThucThanhToan, "
                + "trangThaiThanhToan, tienKhachDua, thoiGianThanhToan, maKM, maNhanVien, maKhachHang, "
                + "tienDuocGiam, tongThanhToan) "
                + "SELECT ?, ?, ?, ?, ?, ?, ?, maNhanVien, ?, ?, ? FROM dbo.NhanVien "
                + "WHERE maNhanVien = ? AND trangThai = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        try {
            stmt.setString(1, ma);
            stmt.setTimestamp(2, Timestamp.valueOf(hd.getNgayGioLap()));
            stmt.setNString(3, hd.getPhuongThucThanhToan());
            stmt.setNString(4, hd.getTrangThaiThanhToan());
            stmt.setBigDecimal(5, hd.getTienKhachDua());
            stmt.setTimestamp(6, Timestamp.valueOf(hd.getThoiGianThanhToan()));
            if (hd.getChuongTrinhKM() == null) {
                stmt.setNull(7, Types.VARCHAR);
            } else {
                stmt.setString(7, hd.getChuongTrinhKM().getMaKM());
            }
            stmt.setString(8, hd.getKhachHang().getMaKhachHang());
            stmt.setBigDecimal(9, hd.tinhTienKhuyenMai(hd.tinhTongTienSP()));
            stmt.setBigDecimal(10, hd.tinhTongTienThanhToan());
            stmt.setString(11, hd.getNhanVien().getMaNhanVien());
            stmt.setNString(12, HOAT_DONG);
            if (stmt.executeUpdate() != 1) {
                throw new IllegalArgumentException("Nhân viên đã ngừng hoạt động hoặc không còn tồn tại.");
            }
        } finally {
            stmt.close();
        }
    }

    private void luuDongHang(Connection con, HoaDon hd, String ma) throws SQLException {
        String sqlTon = "UPDATE dbo.SanPham SET soLuongTon = soLuongTon - ? "
                + "WHERE maSanPham = ? AND trangThai = ? AND soLuongTon >= ?";
        String sqlDong = "INSERT INTO dbo.ChiTietHoaDon (maHoaDon, maSanPham, soLuong, giaBan) "
                + "VALUES (?, ?, ?, ?)";
        PreparedStatement truTon = con.prepareStatement(sqlTon);
        try {
            PreparedStatement themDong = con.prepareStatement(sqlDong);
            try {
                for (ChiTietHoaDon dong : hd.getListChiTietHoaDon()) {
                    String maSP = dong.getSanPham().getMaSanPham();
                    int soLuong = dong.getSoLuong();
                    truTon.setInt(1, soLuong);
                    truTon.setString(2, maSP);
                    truTon.setNString(3, HOAT_DONG);
                    truTon.setInt(4, soLuong);
                    if (truTon.executeUpdate() != 1) {
                        throw new IllegalArgumentException("Sản phẩm " + maSP + " thiếu tồn hoặc đã ngừng hoạt động.");
                    }
                    themDong.setString(1, ma);
                    themDong.setString(2, maSP);
                    themDong.setInt(3, soLuong);
                    themDong.setBigDecimal(4, dong.getGiaBan());
                    themDong.executeUpdate();
                }
            } finally {
                themDong.close();
            }
        } finally {
            truTon.close();
        }
    }
}

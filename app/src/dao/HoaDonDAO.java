package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

public class HoaDonDAO {
    public ArrayList<String> layMaHoaDonDaThanhToan() throws SQLException {
        String sql = "SELECT hd.maHoaDon FROM dbo.HoaDon hd WHERE hd.trangThaiThanhToan = ? "
                + "AND EXISTS (SELECT 1 FROM dbo.ChiTietHoaDon ct WHERE ct.maHoaDon = hd.maHoaDon) "
                + "ORDER BY hd.thoiGianThanhToan DESC, hd.maHoaDon DESC";
        PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql);
        try {
            stmt.setNString(1, "Đã thanh toán");
            ResultSet rs = stmt.executeQuery();
            try {
                ArrayList<String> maHoaDon = new ArrayList<String>();
                while (rs.next()) {
                    maHoaDon.add(rs.getString("maHoaDon"));
                }
                return maHoaDon;
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }

    public HoaDon timHoaDon(String ma) throws SQLException {
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT hd.*, nv.ho AS hoNV, nv.ten AS tenNV, kh.ho AS hoKH, kh.ten AS tenKH, "
                + "kh.soDienThoai, km.loaiKhuyenMai, km.giaTriKM "
                + "FROM dbo.HoaDon hd JOIN dbo.NhanVien nv ON nv.maNhanVien = hd.maNhanVien "
                + "JOIN dbo.KhachHang kh ON kh.maKhachHang = hd.maKhachHang "
                + "LEFT JOIN dbo.ChuongTrinhKhuyenMai km ON km.maKM = hd.maKM WHERE hd.maHoaDon = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        HoaDon hoaDon;
        try {
            stmt.setString(1, ma);
            ResultSet rs = stmt.executeQuery();
            try {
                if (!rs.next()) {
                    return null;
                }
                NhanVien nv = new NhanVien(rs.getString("maNhanVien"));
                nv.setHo(rs.getString("hoNV"));
                nv.setTen(rs.getString("tenNV"));
                KhachHang kh = new KhachHang(rs.getString("maKhachHang"));
                kh.setHo(rs.getString("hoKH"));
                kh.setTen(rs.getString("tenKH"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                ChuongTrinhKhuyenMai km = null;
                String maKM = rs.getString("maKM");
                if (maKM != null) {
                    km = new ChuongTrinhKhuyenMai(maKM);
                    km.setLoaiKhuyenMai(rs.getString("loaiKhuyenMai"));
                    km.setGiaTriKM(rs.getBigDecimal("giaTriKM"));
                }
                hoaDon = new HoaDon(ma, rs.getTimestamp("ngayGioLap").toLocalDateTime(),
                        rs.getString("phuongThucThanhToan"), rs.getString("trangThaiThanhToan"),
                        rs.getBigDecimal("tienKhachDua"), rs.getTimestamp("thoiGianThanhToan").toLocalDateTime(),
                        null, km, nv, kh);
                hoaDon.setSoTienDaLuu(rs.getBigDecimal("tienDuocGiam"), rs.getBigDecimal("tongThanhToan"));
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
        hoaDon.setListChiTietHoaDon(layChiTiet(con, ma));
        return hoaDon;
    }

    private ArrayList<ChiTietHoaDon> layChiTiet(Connection con, String ma) throws SQLException {
        String sql = "SELECT ct.maSanPham, sp.tenSanPham, sp.donViTinh, ct.soLuong, ct.giaBan "
                + "FROM dbo.ChiTietHoaDon ct JOIN dbo.SanPham sp ON sp.maSanPham = ct.maSanPham "
                + "WHERE ct.maHoaDon = ? ORDER BY ct.maSanPham";
        PreparedStatement stmt = con.prepareStatement(sql);
        try {
            stmt.setString(1, ma);
            ResultSet rs = stmt.executeQuery();
            try {
                ArrayList<ChiTietHoaDon> dongHang = new ArrayList<ChiTietHoaDon>();
                while (rs.next()) {
                    SanPham sp = new SanPham(rs.getString("maSanPham"));
                    sp.setTenSanPham(rs.getString("tenSanPham"));
                    sp.setDonViTinh(rs.getString("donViTinh"));
                    dongHang.add(new ChiTietHoaDon(sp, rs.getInt("soLuong"), rs.getBigDecimal("giaBan")));
                }
                return dongHang;
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
    }
}

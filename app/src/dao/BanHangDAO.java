package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.LoaiSanPham;
import entity.SanPham;

public class BanHangDAO {
    public SanPham timSanPham(String maSanPham) throws SQLException {
        String sql = "SELECT maSanPham, tenSanPham, donViTinh, soLuongTon, giaNhap, "
                + "mucLoiNhuan, trangThai, maLoaiSanPham FROM dbo.SanPham WHERE maSanPham = ?";
        PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql);
        try {
            statement.setString(1, maSanPham);
            ResultSet result = statement.executeQuery();
            try {
                if (result.next()) {
                    LoaiSanPham loai = new LoaiSanPham();
                    loai.setMaLoaiSanPham(result.getString("maLoaiSanPham"));
                    return new SanPham(result.getString("maSanPham"), result.getString("tenSanPham"),
                            result.getString("donViTinh"), result.getInt("soLuongTon"),
                            result.getBigDecimal("giaNhap"), result.getBigDecimal("mucLoiNhuan"),
                            result.getString("trangThai"), loai);
                }
                return null;
            } finally {
                result.close();
            }
        } finally {
            statement.close();
        }
    }

    public KhachHang timKhachHang(String tuKhoa) throws SQLException {
        String sql = "SELECT maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi "
                + "FROM dbo.KhachHang WHERE maKhachHang = ? OR soDienThoai = ?";
        PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql);
        try {
            statement.setString(1, tuKhoa);
            statement.setString(2, tuKhoa);
            ResultSet result = statement.executeQuery();
            try {
                if (!result.next()) {
                    return null;
                }
                KhachHang khachHang = new KhachHang(result.getString("maKhachHang"),
                        result.getString("ho"), result.getString("ten"), result.getString("gioiTinh"),
                        result.getString("soDienThoai"), result.getString("diaChi"));
                if (result.next()) {
                    throw new IllegalArgumentException("Số điện thoại trùng. Hãy nhập mã khách hàng.");
                }
                return khachHang;
            } finally {
                result.close();
            }
        } finally {
            statement.close();
        }
    }
}

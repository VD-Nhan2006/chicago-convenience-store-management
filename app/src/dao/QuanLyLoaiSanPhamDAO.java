package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.LoaiSanPham;

public class QuanLyLoaiSanPhamDAO {
	public ArrayList<LoaiSanPham> layDanhSach() throws SQLException {
		String sql = "SELECT maLoaiSanPham, tenLoaiSanPham, moTa, trangThai "
				+ "FROM dbo.LoaiSanPham ORDER BY maLoaiSanPham";
		ArrayList<LoaiSanPham> danhSach = new ArrayList<LoaiSanPham>();
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				danhSach.add(new LoaiSanPham(result.getString("maLoaiSanPham"),
						result.getString("tenLoaiSanPham"), result.getString("moTa"),
						result.getString("trangThai")));
			}
		}
		return danhSach;
	}

	public String sinhMa() throws SQLException {
		String ngayHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
		String sql = "SELECT COALESCE(MAX(CAST(RIGHT(maLoaiSanPham, 3) AS INT)), 0) "
				+ "FROM dbo.LoaiSanPham WHERE maLoaiSanPham LIKE ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, "LSP" + ngayHienTai + "%");
			try (ResultSet result = statement.executeQuery()) {
				result.next();
				int soThuTu = result.getInt(1) + 1;
				if (soThuTu > 999) {
					throw new IllegalStateException("Đã hết mã loại sản phẩm trong ngày hôm nay.");
				}
				return "LSP" + ngayHienTai + String.format("%03d", soThuTu);
			}
		}
	}

	public boolean them(LoaiSanPham loai) throws SQLException {
		String sql = "INSERT INTO dbo.LoaiSanPham (maLoaiSanPham, tenLoaiSanPham, moTa, trangThai) "
				+ "VALUES (?, ?, ?, ?)";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, loai.getMaLoaiSanPham());
			statement.setString(2, loai.getTenLoaiSanPham());
			statement.setString(3, loai.getMoTa());
			statement.setString(4, loai.getTrangThai());
			return statement.executeUpdate() > 0;
		}
	}

	public boolean capNhat(LoaiSanPham loai) throws SQLException {
		String sql = "UPDATE dbo.LoaiSanPham SET tenLoaiSanPham = ?, moTa = ?, trangThai = ? "
				+ "WHERE maLoaiSanPham = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, loai.getTenLoaiSanPham());
			statement.setString(2, loai.getMoTa());
			statement.setString(3, loai.getTrangThai());
			statement.setString(4, loai.getMaLoaiSanPham());
			return statement.executeUpdate() > 0;
		}
	}

	public boolean ngungHoatDong(String maLoai) throws SQLException {
		String sql = "UPDATE dbo.LoaiSanPham SET trangThai = ? WHERE maLoaiSanPham = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, "Ngừng hoạt động");
			statement.setString(2, maLoai);
			return statement.executeUpdate() > 0;
		}
	}
}

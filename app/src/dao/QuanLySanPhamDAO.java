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
import entity.SanPham;

public class QuanLySanPhamDAO {
	public ArrayList<SanPham> layDanhSach() throws SQLException {
		String sql = "SELECT sp.maSanPham, sp.tenSanPham, sp.donViTinh, sp.soLuongTon, "
				+ "sp.giaNhap, sp.mucLoiNhuan, sp.trangThai, l.maLoaiSanPham, "
				+ "l.tenLoaiSanPham, l.moTa, l.trangThai AS trangThaiLoai "
				+ "FROM dbo.SanPham sp JOIN dbo.LoaiSanPham l "
				+ "ON l.maLoaiSanPham = sp.maLoaiSanPham ORDER BY sp.maSanPham";
		ArrayList<SanPham> danhSach = new ArrayList<SanPham>();
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				LoaiSanPham loai = new LoaiSanPham(result.getString("maLoaiSanPham"),
						result.getString("tenLoaiSanPham"), result.getString("moTa"),
						result.getString("trangThaiLoai"));
				danhSach.add(new SanPham(result.getString("maSanPham"), result.getString("tenSanPham"),
						result.getString("donViTinh"), result.getInt("soLuongTon"), result.getBigDecimal("giaNhap"),
						result.getBigDecimal("mucLoiNhuan"), result.getString("trangThai"), loai));
			}
		}
		return danhSach;
	}

	public ArrayList<LoaiSanPham> layDanhSachLoai() throws SQLException {
		String sql = "SELECT maLoaiSanPham, tenLoaiSanPham, moTa, trangThai "
				+ "FROM dbo.LoaiSanPham ORDER BY tenLoaiSanPham";
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
		return sinhMa("SP", "maSanPham", "dbo.SanPham");
	}

	public boolean them(SanPham sanPham) throws SQLException {
		String sql = "INSERT INTO dbo.SanPham "
				+ "(maSanPham, tenSanPham, donViTinh, soLuongTon, giaNhap, mucLoiNhuan, trangThai, maLoaiSanPham) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, sanPham.getMaSanPham());
			statement.setString(2, sanPham.getTenSanPham());
			statement.setString(3, sanPham.getDonViTinh());
			statement.setInt(4, sanPham.getSoLuongTon());
			statement.setBigDecimal(5, sanPham.getGiaNhap());
			statement.setBigDecimal(6, sanPham.getMucLoiNhuan());
			statement.setString(7, sanPham.getTrangThai());
			statement.setString(8, sanPham.getLoaiSanPham().getMaLoaiSanPham());
			return statement.executeUpdate() > 0;
		}
	}

	public boolean capNhat(SanPham sanPham) throws SQLException {
		String sql = "UPDATE dbo.SanPham SET tenSanPham = ?, donViTinh = ?, soLuongTon = ?, "
				+ "giaNhap = ?, mucLoiNhuan = ?, trangThai = ?, maLoaiSanPham = ? WHERE maSanPham = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, sanPham.getTenSanPham());
			statement.setString(2, sanPham.getDonViTinh());
			statement.setInt(3, sanPham.getSoLuongTon());
			statement.setBigDecimal(4, sanPham.getGiaNhap());
			statement.setBigDecimal(5, sanPham.getMucLoiNhuan());
			statement.setString(6, sanPham.getTrangThai());
			statement.setString(7, sanPham.getLoaiSanPham().getMaLoaiSanPham());
			statement.setString(8, sanPham.getMaSanPham());
			return statement.executeUpdate() > 0;
		}
	}

	public boolean ngungHoatDong(String maSanPham) throws SQLException {
		String sql = "UPDATE dbo.SanPham SET trangThai = ? WHERE maSanPham = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, "Ngừng hoạt động");
			statement.setString(2, maSanPham);
			return statement.executeUpdate() > 0;
		}
	}

	private String sinhMa(String tienTo, String cotMa, String bang) throws SQLException {
		String ngayHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
		String sql = "SELECT COALESCE(MAX(CAST(RIGHT(" + cotMa + ", 3) AS INT)), 0) FROM " + bang
				+ " WHERE " + cotMa + " LIKE ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, tienTo + ngayHienTai + "%");
			try (ResultSet result = statement.executeQuery()) {
				result.next();
				int soThuTu = result.getInt(1) + 1;
				if (soThuTu > 999) {
					throw new IllegalStateException("Đã hết mã sản phẩm trong ngày hôm nay.");
				}
				return tienTo + ngayHienTai + String.format("%03d", soThuTu);
			}
		}
	}
}

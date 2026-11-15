package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChuongTrinhKhuyenMai;

public class QuanLyKhuyenMaiDAO {
	public ArrayList<ChuongTrinhKhuyenMai> layDanhSach() throws SQLException {
		String sql = "SELECT maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKM, "
				+ "ngayBatDau, ngayKetThuc, trangThai FROM dbo.ChuongTrinhKhuyenMai "
				+ "ORDER BY maKM";
		ArrayList<ChuongTrinhKhuyenMai> danhSach = new ArrayList<ChuongTrinhKhuyenMai>();
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				danhSach.add(new ChuongTrinhKhuyenMai(result.getString("maKM"),
						result.getString("tenChuongTrinh"), result.getString("moTa"),
						result.getString("loaiKhuyenMai"), result.getBigDecimal("giaTriKM"),
						result.getDate("ngayBatDau").toLocalDate(), result.getDate("ngayKetThuc").toLocalDate(),
						result.getString("trangThai")));
			}
		}
		return danhSach;
	}

	public String sinhMa() throws SQLException {
		String ngayHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
		String sql = "SELECT COALESCE(MAX(CAST(RIGHT(maKM, 3) AS INT)), 0) "
				+ "FROM dbo.ChuongTrinhKhuyenMai WHERE maKM LIKE ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, "KM" + ngayHienTai + "%");
			try (ResultSet result = statement.executeQuery()) {
				result.next();
				int soThuTu = result.getInt(1) + 1;
				if (soThuTu > 999) {
					throw new IllegalStateException("Đã hết mã khuyến mãi trong ngày hôm nay.");
				}
				return "KM" + ngayHienTai + String.format("%03d", soThuTu);
			}
		}
	}

	public boolean them(ChuongTrinhKhuyenMai khuyenMai) throws SQLException {
		String sql = "INSERT INTO dbo.ChuongTrinhKhuyenMai "
				+ "(maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKM, ngayBatDau, ngayKetThuc, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			ganThamSo(statement, khuyenMai);
			return statement.executeUpdate() > 0;
		}
	}

	public boolean capNhat(ChuongTrinhKhuyenMai khuyenMai) throws SQLException {
		String sql = "UPDATE dbo.ChuongTrinhKhuyenMai SET tenChuongTrinh = ?, moTa = ?, "
				+ "loaiKhuyenMai = ?, giaTriKM = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThai = ? "
				+ "WHERE maKM = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, khuyenMai.getTenChuongTrinh());
			statement.setString(2, khuyenMai.getMoTa());
			statement.setString(3, khuyenMai.getLoaiKhuyenMai());
			statement.setBigDecimal(4, khuyenMai.getGiaTriKM());
			statement.setDate(5, Date.valueOf(khuyenMai.getNgayBatDau()));
			statement.setDate(6, Date.valueOf(khuyenMai.getNgayKetThuc()));
			statement.setString(7, khuyenMai.getTrangThai());
			statement.setString(8, khuyenMai.getMaKM());
			return statement.executeUpdate() > 0;
		}
	}

	public boolean ngungHoatDong(String maKM) throws SQLException {
		String sql = "UPDATE dbo.ChuongTrinhKhuyenMai SET trangThai = ? WHERE maKM = ?";
		try (Connection connection = ConnectDB.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, "Ngừng hoạt động");
			statement.setString(2, maKM);
			return statement.executeUpdate() > 0;
		}
	}

	private void ganThamSo(PreparedStatement statement, ChuongTrinhKhuyenMai khuyenMai) throws SQLException {
		statement.setString(1, khuyenMai.getMaKM());
		statement.setString(2, khuyenMai.getTenChuongTrinh());
		statement.setString(3, khuyenMai.getMoTa());
		statement.setString(4, khuyenMai.getLoaiKhuyenMai());
		statement.setBigDecimal(5, khuyenMai.getGiaTriKM());
		statement.setDate(6, Date.valueOf(khuyenMai.getNgayBatDau()));
		statement.setDate(7, Date.valueOf(khuyenMai.getNgayKetThuc()));
		statement.setString(8, khuyenMai.getTrangThai());
	}
}

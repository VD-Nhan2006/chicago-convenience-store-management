package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;

public class QuanLyTaiKhoanDAO {
	
	
	public QuanLyTaiKhoanDAO() {
		}
	public ArrayList<TaiKhoan> getAllTaiKhoan() {		
		ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<TaiKhoan>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT tk.maNhanVien, nv.ho, nv.ten,tk.matKhau, tk.vaiTro, tk.trangThai "
	                + "FROM dbo.TaiKhoan tk JOIN dbo.NhanVien nv ON nv.maNhanVien = tk.maNhanVien "
	                + "ORDER BY tk.maNhanVien";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql.toString());
			try {
				while(rs.next()) {
					NhanVien nv = new NhanVien(rs.getString(1));
					nv.setHo(rs.getString(2));
					nv.setTen(rs.getString(3));
					TaiKhoan tk = new TaiKhoan(nv, rs.getString(4),VaiTro.fromString(rs.getString(5)), rs.getString(6));
					dsTaiKhoan.add(tk);
				}
			}finally {
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return dsTaiKhoan;
	}
	
	public boolean themTaiKhoan(TaiKhoan tk) {
		StringBuilder sql = new StringBuilder("INSERT INTO dbo.TaiKhoan (maNhanVien, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?)");
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			PreparedStatement statement = con.prepareStatement(sql.toString());
			try {
				statement.setString(1, tk.getNhanVien().getMaNhanVien());
				statement.setString(2, tk.getMatKhau());
				statement.setString(3, tk.getVaiTro().getTenHienThi());
				statement.setString(4, tk.getTrangThai());
				return statement.executeUpdate() > 0;
			}finally {
				statement.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean doiMatKhau(String maNV, String matKhauMoi) {
		StringBuilder sql = new StringBuilder("UPDATE dbo.TaiKhoan SET matKhau = ? WHERE maNhanVien = ?");
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			PreparedStatement prestm = con.prepareStatement(sql.toString());
			try {
				prestm.setString(1, matKhauMoi);
				prestm.setString(2, maNV);
				
				return prestm.executeUpdate()>0;
			}finally {
				prestm.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

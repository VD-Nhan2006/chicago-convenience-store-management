package dao;

import java.sql.Connection;
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
}

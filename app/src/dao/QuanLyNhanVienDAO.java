package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;
import connectDB.ConnectDB;

public class QuanLyNhanVienDAO {
	public QuanLyNhanVienDAO() {
		
	}
	public ArrayList<NhanVien> getAllNhanVien() {
		ArrayList<NhanVien> ds = new ArrayList<NhanVien>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql =  """
		            SELECT 
	                nv.maNhanVien,
	                nv.ho,
	                nv.ten,
	                nv.soDienThoai,
	                nv.diaChi,
	                nv.trangThai,
	                tk.vaiTro
	            FROM NhanVien nv
	            LEFT JOIN TaiKhoan tk
	                ON nv.maNhanVien = tk.maNhanVien
	            """;
			PreparedStatement statment = con.prepareStatement(sql.toString());
			ResultSet rs = statment.executeQuery();
			while(rs.next()) {
				NhanVien nv = new NhanVien();
				
				nv.setMaNhanVien(rs.getString("maNhanVien"));
	            nv.setHo(rs.getString("ho"));
	            nv.setTen(rs.getString("ten"));
	            nv.setSoDienThoai(rs.getString("soDienThoai"));
	            nv.setDiaChi(rs.getString("diaChi"));
	            nv.setTrangThai(rs.getString("trangThai"));

	            String vaiTro = rs.getString("vaiTro");

	            if (vaiTro != null) {
	                TaiKhoan tk = new TaiKhoan(nv);
	                tk.setVaiTro(VaiTro.fromString(vaiTro));
	                nv.setTaiKhoan(tk);
	            }

	            ds.add(nv);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
}	

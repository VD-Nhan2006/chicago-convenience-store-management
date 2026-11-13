package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

	public boolean themNhanVien(NhanVien nv) {
	
	    try {
	        ConnectDB.getInstance();
	        Connection con = ConnectDB.getConnection();
	
	        StringBuilder sql = new StringBuilder();
	        sql.append("INSERT INTO NhanVien ");
	        sql.append("(maNhanVien, ho, ten, soDienThoai, diaChi, trangThai) ");
	        sql.append("VALUES (?, ?, ?, ?, ?, ?)");
	
	        PreparedStatement statement =
	                con.prepareStatement(sql.toString());
	
	        statement.setString(1, nv.getMaNhanVien());
	        statement.setString(2, nv.getHo());
	        statement.setString(3, nv.getTen());
	        statement.setString(4, nv.getSoDienThoai());
	        statement.setString(5, nv.getDiaChi());
	        statement.setString(6, nv.getTrangThai());
	
	        return statement.executeUpdate() > 0;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public String sinhMaNhanVien() {
	
	    try {
	        // Lấy ngày hiện tại theo định dạng ddMMyy
	        String ngayHienTai = LocalDate.now()
	                .format(DateTimeFormatter.ofPattern("ddMMyy"));
	
	        ConnectDB.getInstance();
	        Connection con = ConnectDB.getConnection();
	
	        // Tìm số thứ tự lớn nhất của nhân viên trong ngày
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT MAX(CAST(RIGHT(maNhanVien, 3) AS INT)) ");
	        sql.append("FROM NhanVien ");
	        sql.append("WHERE maNhanVien LIKE ?");
	
	        PreparedStatement statement =
	                con.prepareStatement(sql.toString());
	
	        statement.setString(1, "NV" + ngayHienTai + "%");
	
	        ResultSet rs = statement.executeQuery();
	
	        int soThuTu = 1;
	
	        if (rs.next()) {
	            soThuTu = rs.getInt(1) + 1;
	        }
	
	        // Kiểm tra giới hạn 999 mã trong ngày
	        if (soThuTu > 999) {
	            throw new IllegalStateException(
	                    "Đã hết mã nhân viên trong ngày hôm nay!");
	        }
	
	        // Định dạng số thứ tự thành 3 chữ số
	        String maThuTu = String.format("%03d", soThuTu);
	
	        // Ghép thành mã nhân viên hoàn chỉnh
	        return "NV" + ngayHienTai + maThuTu;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}


	
	
}	

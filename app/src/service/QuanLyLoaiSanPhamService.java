package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.QuanLyLoaiSanPhamDAO;
import entity.LoaiSanPham;

public class QuanLyLoaiSanPhamService {
	private final QuanLyLoaiSanPhamDAO loaiSanPhamDAO = new QuanLyLoaiSanPhamDAO();

	public ArrayList<LoaiSanPham> layDanhSach() throws SQLException {
		return loaiSanPhamDAO.layDanhSach();
	}

	public String sinhMa() throws SQLException {
		return loaiSanPhamDAO.sinhMa();
	}

	public boolean them(LoaiSanPham loai) throws SQLException {
		return loaiSanPhamDAO.them(loai);
	}

	public boolean capNhat(LoaiSanPham loai) throws SQLException {
		return loaiSanPhamDAO.capNhat(loai);
	}

	public boolean ngungHoatDong(String maLoai) throws SQLException {
		return loaiSanPhamDAO.ngungHoatDong(maLoai);
	}
}

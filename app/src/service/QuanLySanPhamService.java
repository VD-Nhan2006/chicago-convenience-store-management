package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.QuanLySanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;

public class QuanLySanPhamService {
	private final QuanLySanPhamDAO sanPhamDAO = new QuanLySanPhamDAO();

	public ArrayList<SanPham> layDanhSach() throws SQLException {
		return sanPhamDAO.layDanhSach();
	}

	public ArrayList<LoaiSanPham> layDanhSachLoai() throws SQLException {
		return sanPhamDAO.layDanhSachLoai();
	}

	public String sinhMa() throws SQLException {
		return sanPhamDAO.sinhMa();
	}

	public boolean them(SanPham sanPham) throws SQLException {
		return sanPhamDAO.them(sanPham);
	}

	public boolean capNhat(SanPham sanPham) throws SQLException {
		return sanPhamDAO.capNhat(sanPham);
	}

	public boolean ngungHoatDong(String maSanPham) throws SQLException {
		return sanPhamDAO.ngungHoatDong(maSanPham);
	}
}

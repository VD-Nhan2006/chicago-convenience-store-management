package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.QuanLyKhuyenMaiDAO;
import entity.ChuongTrinhKhuyenMai;

public class QuanLyKhuyenMaiService {
	private final QuanLyKhuyenMaiDAO khuyenMaiDAO = new QuanLyKhuyenMaiDAO();

	public ArrayList<ChuongTrinhKhuyenMai> layDanhSach() throws SQLException {
		return khuyenMaiDAO.layDanhSach();
	}

	public String sinhMa() throws SQLException {
		return khuyenMaiDAO.sinhMa();
	}

	public boolean them(ChuongTrinhKhuyenMai khuyenMai) throws SQLException {
		return khuyenMaiDAO.them(khuyenMai);
	}

	public boolean capNhat(ChuongTrinhKhuyenMai khuyenMai) throws SQLException {
		return khuyenMaiDAO.capNhat(khuyenMai);
	}

	public boolean ngungHoatDong(String maKM) throws SQLException {
		return khuyenMaiDAO.ngungHoatDong(maKM);
	}
}

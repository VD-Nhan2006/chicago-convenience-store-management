package service;

import java.util.ArrayList;

import dao.QuanLyNhanVienDAO;
import entity.NhanVien;

public class QuanLyNhanVienService {
	public QuanLyNhanVienService() {
		
	}
	private QuanLyNhanVienDAO nhanVienDAO = new QuanLyNhanVienDAO();
	public ArrayList<NhanVien> getAllNhanVien(){
		return nhanVienDAO.getAllNhanVien();
	}
}

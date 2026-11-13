package service;

import java.util.ArrayList;

import dao.QuanLyNhanVienDAO;
import entity.NhanVien;

public class QuanLyNhanVienService {
	private QuanLyNhanVienDAO nhanVienDAO = new QuanLyNhanVienDAO();
	
	public QuanLyNhanVienService() {		
	}
	
	public ArrayList<NhanVien> getAllNhanVien(){
		return nhanVienDAO.getAllNhanVien();
	}
	
	public boolean themNhanVien(NhanVien nv) {
		if(nv == null) {
			return false;
		}
		return nhanVienDAO.themNhanVien(nv);
	}
	
	public String sinhMaNhanVien() {
		return nhanVienDAO.sinhMaNhanVien();
	}
}

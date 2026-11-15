package service;

import java.util.ArrayList;

import dao.QuanLyTaiKhoanDAO;
import entity.TaiKhoan;

public class QuanLyTaiKhoanService {
	
	private QuanLyTaiKhoanDAO taiKhoanDAO = new QuanLyTaiKhoanDAO();
	public ArrayList<TaiKhoan> getAllTaiKhoan(){
		return taiKhoanDAO.getAllTaiKhoan();
	}
	
	public boolean themTaiKhoan(TaiKhoan tk) {
		if(tk == null || tk.getNhanVien() == null) {
			return false;
		}
		return taiKhoanDAO.themTaiKhoan(tk);
	}
}

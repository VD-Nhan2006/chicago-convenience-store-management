package service;

import java.util.ArrayList;

import dao.QuanLyTaiKhoanDAO;
import entity.TaiKhoan;

public class QuanLyTaiKhoanService {
	
	private QuanLyTaiKhoanDAO taiKhoanDAO = new QuanLyTaiKhoanDAO();
	public ArrayList<TaiKhoan> getAllTaiKhoan(){
		return taiKhoanDAO.getAllTaiKhoan();
	}
}

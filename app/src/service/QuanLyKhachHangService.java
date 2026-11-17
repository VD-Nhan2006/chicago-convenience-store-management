package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.KhachHangDAO;
import entity.KhachHang;

public class QuanLyKhachHangService {

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public ArrayList<KhachHang> layDanhSach() throws SQLException {
        return khachHangDAO.getAll();
    }

    public ArrayList<KhachHang> timKiem(String tuKhoa) throws SQLException {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return khachHangDAO.getAll();
        }
        return khachHangDAO.timKiem(tuKhoa.trim());
    }

    public KhachHang layTheoMa(String maKhachHang) throws SQLException {
        return khachHangDAO.getByMa(maKhachHang);
    }

    public String themMoi(String ho, String ten, String gioiTinh,
            String soDienThoai, String diaChi) throws SQLException {
        return khachHangDAO.themMoi(
                ho, ten, gioiTinh, soDienThoai, diaChi);
    }

    public void capNhat(KhachHang khachHang) throws SQLException {
        khachHangDAO.capNhat(khachHang);
    }

    public void xoa(String maKhachHang) throws SQLException {
        khachHangDAO.xoa(maKhachHang);
    }
}
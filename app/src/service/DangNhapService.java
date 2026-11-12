package service;

import java.sql.SQLException;

import dao.DangNhapDAO;
import entity.TaiKhoan;

public class DangNhapService {
    private final DangNhapDAO dao;

    public DangNhapService(DangNhapDAO dao) {
        this.dao = dao;
    }

    public TaiKhoan xacThuc(String maNhanVien, String matKhau) throws SQLException {
        if (maNhanVien.isEmpty() || matKhau.isEmpty()) {
            throw new IllegalArgumentException("Nhập mã nhân viên và mật khẩu.");
        }
        TaiKhoan taiKhoan = dao.timTaiKhoanHopLe(maNhanVien, matKhau);
        if (taiKhoan == null) {
            throw new IllegalArgumentException("Mã nhân viên hoặc mật khẩu sai, hoặc tài khoản đã ngừng hoạt động.");
        }
        return taiKhoan;
    }
}

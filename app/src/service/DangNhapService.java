package service;

import java.sql.SQLException;

import dao.DangNhapDAO;
import entity.TaiKhoan;

public class DangNhapService {
    private final DangNhapDAO dangNhapDAO = new DangNhapDAO();

    public DangNhapService() {
    }

    public TaiKhoan xacThuc(String maNhanVien, String matKhau) throws SQLException {
        if (maNhanVien.isEmpty() || matKhau.isEmpty()) {
            throw new IllegalArgumentException("Nhập mã nhân viên và mật khẩu.");
        }
        TaiKhoan taiKhoan = dangNhapDAO.timTaiKhoanHopLe(maNhanVien, matKhau);
        if (taiKhoan == null) {
            throw new IllegalArgumentException("Mã nhân viên hoặc mật khẩu sai, hoặc tài khoản đã ngừng hoạt động.");
        }
        return taiKhoan;
    }
}

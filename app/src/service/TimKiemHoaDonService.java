package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import dao.TimKiemHoaDonDAO;
import entity.HoaDon;

public class TimKiemHoaDonService {

    private final TimKiemHoaDonDAO timKiemHoaDonDAO =
            new TimKiemHoaDonDAO();

    public ArrayList<HoaDon> timKiem(
            String maHoaDon,
            LocalDate tuNgay,
            LocalDate denNgay,
            String tenKhachHang) throws SQLException {

        return timKiemHoaDonDAO.timKiem(
                maHoaDon, tuNgay, denNgay, tenKhachHang);
    }

    public HoaDon layChiTiet(String maHoaDon) throws SQLException {
        return timKiemHoaDonDAO.layChiTiet(maHoaDon);
    }
}
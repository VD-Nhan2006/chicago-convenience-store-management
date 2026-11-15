package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import dao.ThongKeDAO;

public class ThongKeService {

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    public ThongKeDAO.TongHop tongHop(
            LocalDate tuNgay, LocalDate denNgay) throws SQLException {

        return thongKeDAO.tongHop(tuNgay, denNgay);
    }

    public ArrayList<ThongKeDAO.DongHoaDon> layHoaDon(
            LocalDate tuNgay, LocalDate denNgay) throws SQLException {

        return thongKeDAO.layHoaDon(tuNgay, denNgay);
    }
}
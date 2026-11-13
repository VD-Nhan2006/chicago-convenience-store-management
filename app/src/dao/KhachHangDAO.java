package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {

    public ArrayList<KhachHang> getAll() {
        ArrayList<KhachHang> ds = new ArrayList<KhachHang>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi "
                       + "FROM dbo.KhachHang ORDER BY maKhachHang";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("ho"),
                        rs.getString("ten"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"));
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<KhachHang> timKiem(String tuKhoa) {
        ArrayList<KhachHang> ds = new ArrayList<KhachHang>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String kw = "%" + tuKhoa.trim().replace("'", "''") + "%";
            String sql = "SELECT maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi "
                       + "FROM dbo.KhachHang "
                       + "WHERE maKhachHang LIKE '" + kw + "' "
                       + "   OR (ho + N' ' + ten) LIKE N'" + kw + "' "
                       + "   OR soDienThoai LIKE '" + kw + "' "
                       + "ORDER BY maKhachHang";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("ho"),
                        rs.getString("ten"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"));
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public KhachHang getByMa(String maKhachHang) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi "
                       + "FROM dbo.KhachHang "
                       + "WHERE maKhachHang = '" + maKhachHang + "'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("ho"),
                        rs.getString("ten"),
                        rs.getString("gioiTinh"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String phatSinhMa() {
        String prefix = "KH" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        int stt = 1;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT MAX(maKhachHang) FROM dbo.KhachHang "
                       + "WHERE maKhachHang LIKE '" + prefix + "%'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next() && rs.getString(1) != null) {
                stt = Integer.parseInt(rs.getString(1).substring(8)) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + String.format("%03d", stt);
    }

    public boolean them(KhachHang kh) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO dbo.KhachHang(maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi) "
                       + "VALUES ('" + kh.getMaKhachHang() + "', "
                       + "N'" + kh.getHo()          + "', "
                       + "N'" + kh.getTen()          + "', "
                       + "N'" + kh.getGioiTinh()     + "', "
                       + "'"  + kh.getSoDienThoai()  + "', "
                       + "N'" + kh.getDiaChi()       + "')";
            Statement statement = con.createStatement();
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhat(KhachHang kh) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE dbo.KhachHang "
                       + "SET ho = N'"          + kh.getHo()          + "', "
                       + "    ten = N'"          + kh.getTen()         + "', "
                       + "    gioiTinh = N'"     + kh.getGioiTinh()    + "', "
                       + "    soDienThoai = '"   + kh.getSoDienThoai() + "', "
                       + "    diaChi = N'"       + kh.getDiaChi()      + "' "
                       + "WHERE maKhachHang = '" + kh.getMaKhachHang() + "'";
            Statement statement = con.createStatement();
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Không thể kết nối cơ sở dữ liệu!\n" + e.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean xoa(String maKhachHang) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "DELETE FROM dbo.KhachHang "
                       + "WHERE maKhachHang = '" + maKhachHang + "'";
            Statement statement = con.createStatement();
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
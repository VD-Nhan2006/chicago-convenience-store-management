
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

public class TimKiemHoaDonDAO {

    public TimKiemHoaDonDAO() {
   
    }


    private static final String CHON_HOA_DON =
            "SELECT hd.maHoaDon, hd.ngayGioLap, "
            + "hd.phuongThucThanhToan, hd.trangThaiThanhToan, "
            + "hd.tienKhachDua, hd.thoiGianThanhToan, "
            + "hd.tienDuocGiam, hd.tongThanhToan, hd.maKM, "
            + "hd.maKhachHang, kh.ho AS khHo, kh.ten AS khTen, "
            + "hd.maNhanVien, nv.ho AS nvHo, nv.ten AS nvTen, "
            + "km.tenChuongTrinh, km.loaiKhuyenMai, km.giaTriKM "
            + "FROM dbo.HoaDon hd "
            + "LEFT JOIN dbo.KhachHang kh "
            + "ON kh.maKhachHang = hd.maKhachHang "
            + "LEFT JOIN dbo.NhanVien nv "
            + "ON nv.maNhanVien = hd.maNhanVien "
            + "LEFT JOIN dbo.ChuongTrinhKhuyenMai km "
            + "ON km.maKM = hd.maKM ";



    public ArrayList<HoaDon> timKiem(
            String maHoaDon,
            LocalDate tuNgay,
            LocalDate denNgay,
            String tenKhachHang) throws SQLException {

        Connection con = ConnectDB.getConnection();

        StringBuilder sql = new StringBuilder(CHON_HOA_DON);
        sql.append(" WHERE 1 = 1 ");

        ArrayList<Object> thamSo = new ArrayList<>();


        if (maHoaDon != null && !maHoaDon.trim().isEmpty()) {
            sql.append(
                    " AND UPPER(LTRIM(RTRIM(hd.maHoaDon))) LIKE ? "
            );

            thamSo.add(
                    "%" + maHoaDon.trim().toUpperCase() + "%"
            );
        }


        if (tuNgay != null) {
            sql.append(" AND hd.ngayGioLap >= ? ");

            thamSo.add(
                    Timestamp.valueOf(tuNgay.atStartOfDay())
            );
        }


        if (denNgay != null) {
            sql.append(" AND hd.ngayGioLap < ? ");

            thamSo.add(
                    Timestamp.valueOf(
                            denNgay.plusDays(1).atStartOfDay()
                    )
            );
        }


        if (tenKhachHang != null && !tenKhachHang.trim().isEmpty()) {
            sql.append(
                    " AND UPPER(CONCAT(kh.ho, N' ', kh.ten)) LIKE ? "
            );

            thamSo.add(
                    "%" + tenKhachHang.trim().toUpperCase() + "%"
            );
        }

 
        sql.append(
                " ORDER BY hd.ngayGioLap DESC, hd.maHoaDon DESC"
        );

        try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {


            for (int i = 0; i < thamSo.size(); i++) {

                Object giaTri = thamSo.get(i);

                if (giaTri instanceof Timestamp) {
                    stmt.setTimestamp(
                            i + 1,
                            (Timestamp) giaTri
                    );
                } else {
                    stmt.setString(
                            i + 1,
                            (String) giaTri
                    );
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {

                ArrayList<HoaDon> ds = new ArrayList<>();

                while (rs.next()) {
                    ds.add(docHoaDon(rs, null));
                }

                return ds;
            }
        }
    }


    public HoaDon layChiTiet(String maHoaDon) throws SQLException {

        Connection con = ConnectDB.getConnection();

        HoaDon hoaDon;

        try (PreparedStatement stmt = con.prepareStatement(
                CHON_HOA_DON + " WHERE hd.maHoaDon = ?")) {

            stmt.setString(1, maHoaDon);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                ChuongTrinhKhuyenMai km = null;

                String maKM = rs.getString("maKM");

                if (maKM != null) {
                    km = new ChuongTrinhKhuyenMai(maKM);

                    km.setTenChuongTrinh(
                            rs.getNString("tenChuongTrinh")
                    );

                    km.setLoaiKhuyenMai(
                            rs.getString("loaiKhuyenMai")
                    );

                    km.setGiaTriKM(
                            rs.getBigDecimal("giaTriKM")
                    );
                }

                hoaDon = docHoaDon(rs, km);
            }
        }

        hoaDon.setListChiTietHoaDon(
                layDongHang(con, maHoaDon)
        );

        return hoaDon;
    }

 

    private ArrayList<ChiTietHoaDon> layDongHang(
            Connection con,
            String maHoaDon) throws SQLException {

        String sql =
                "SELECT ct.maSanPham, sp.tenSanPham, "
                + "sp.donViTinh, ct.soLuong, ct.giaBan "
                + "FROM dbo.ChiTietHoaDon ct "
                + "JOIN dbo.SanPham sp "
                + "ON sp.maSanPham = ct.maSanPham "
                + "WHERE ct.maHoaDon = ? "
                + "ORDER BY ct.maSanPham";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);

            try (ResultSet rs = stmt.executeQuery()) {

                ArrayList<ChiTietHoaDon> dongHang =
                        new ArrayList<>();

                while (rs.next()) {

                    SanPham sp = new SanPham(
                            rs.getString("maSanPham")
                    );

                    sp.setTenSanPham(
                            rs.getNString("tenSanPham")
                    );

                    sp.setDonViTinh(
                            rs.getNString("donViTinh")
                    );

                    ChiTietHoaDon chiTiet = new ChiTietHoaDon(
                            sp,
                            rs.getInt("soLuong"),
                            rs.getBigDecimal("giaBan")
                    );

                    dongHang.add(chiTiet);
                }

                return dongHang;
            }
        }
    }



    private HoaDon docHoaDon(
            ResultSet rs,
            ChuongTrinhKhuyenMai km) throws SQLException {

 
        String maNV = rs.getString("maNhanVien");

        NhanVien nv = maNV != null
                ? new NhanVien(maNV)
                : null;

        if (nv != null) {
            nv.setHo(rs.getNString("nvHo"));
            nv.setTen(rs.getNString("nvTen"));
        }

        String maKH = rs.getString("maKhachHang");

        KhachHang kh = maKH != null
                ? new KhachHang(maKH)
                : null;

        if (kh != null) {
            kh.setHo(rs.getNString("khHo"));
            kh.setTen(rs.getNString("khTen"));
        }
        Timestamp ngayGioLap =
                rs.getTimestamp("ngayGioLap");

        Timestamp thoiGianTT =
                rs.getTimestamp("thoiGianThanhToan");

        HoaDon hoaDon = new HoaDon(
                rs.getString("maHoaDon"),

                ngayGioLap != null
                        ? ngayGioLap.toLocalDateTime()
                        : null,

                rs.getNString("phuongThucThanhToan"),

                rs.getNString("trangThaiThanhToan"),

                rs.getBigDecimal("tienKhachDua"),

                thoiGianTT != null
                        ? thoiGianTT.toLocalDateTime()
                        : null,

                null,
                km,
                nv,
                kh
        );


        hoaDon.setSoTienDaLuu(
                rs.getBigDecimal("tienDuocGiam"),
                rs.getBigDecimal("tongThanhToan")
        );

        return hoaDon;
    }
}

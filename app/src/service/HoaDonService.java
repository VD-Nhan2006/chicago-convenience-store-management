package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

public class HoaDonService {
    private final HoaDonDAO dao;

    public HoaDonService(HoaDonDAO dao) {
        this.dao = dao;
    }

    public HoaDon layHoaDonDaThanhToan(String ma) throws SQLException {
        HoaDon hoaDon = dao.timHoaDon(ma);
        if (hoaDon == null || !"Đã thanh toán".equals(hoaDon.getTrangThaiThanhToan())
                || hoaDon.getListChiTietHoaDon().isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn đã thanh toán có dòng hàng.");
        }
        return hoaDon;
    }

    public static File xuatHoaDonVaoDuAn(HoaDon hd) throws IOException {
        File tep = new File("HoaDon/" + hd.getMaHoaDon() + ".txt");
        xuatHoaDonTxt(hd, tep);
        return tep;
    }

    public static void xuatHoaDonTxt(HoaDon hd, File tep) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(tep));
        try {
            ghiDong(out, "CỬA HÀNG TIỆN LỢI CHICAGO");
            ghiDong(out, "HÓA ĐƠN BÁN HÀNG");
            out.write("\r\n");
            ghiDong(out, "Mã hóa đơn: " + hd.getMaHoaDon());
            ghiDong(out, "Ngày lập: " + hd.dinhDangNgayLap());
            ghiDong(out, "Nhân viên: " + hd.getNhanVien().getHo() + " " + hd.getNhanVien().getTen()
                    + " (" + hd.getNhanVien().getMaNhanVien() + ")");
            ghiDong(out, "Khách hàng: " + hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen());
            ghiDong(out, "SĐT khách hàng: " + hd.getKhachHang().getSoDienThoai());
            ghiDong(out, "Phương thức: " + hd.getPhuongThucThanhToan());
            ghiDong(out, "Trạng thái: " + hd.getTrangThaiThanhToan());
            out.write("\r\n");
            ghiDong(out, "STT | Mã SP | Tên sản phẩm | ĐVT | SL | Đơn giá (đ) | Thành tiền (đ)");
            int stt = 1;
            long tongSoLuong = 0;
            for (ChiTietHoaDon dong : hd.getListChiTietHoaDon()) {
                SanPham sp = dong.getSanPham();
                ghiDong(out, stt + " | " + sp.getMaSanPham() + " | " + sp.getTenSanPham() + " | "
                        + sp.getDonViTinh() + " | " + dong.getSoLuong() + " | " + dong.getGiaBan().toPlainString()
                        + " | " + dong.tinhThanhTien().toPlainString());
                stt++;
                tongSoLuong += dong.getSoLuong();
            }
            out.write("\r\n");
            ghiDong(out, "Tổng số lượng: " + tongSoLuong);
            ghiDong(out, "Tổng tiền hàng: " + hd.tinhTongTienSP().toPlainString() + " đ");
            ghiDong(out, "Tiền được giảm: " + hd.tinhTienKhuyenMai(hd.tinhTongTienSP()).toPlainString() + " đ");
            ghiDong(out, "Tiền khách đưa: " + hd.getTienKhachDua().toPlainString() + " đ");
            ghiDong(out, "Tiền thối: " + hd.tinhTienThoi().toPlainString() + " đ");
            ghiDong(out, "TỔNG THANH TOÁN: " + hd.tinhTongTienThanhToan().toPlainString() + " đ");
        } finally {
            out.close();
        }
    }

    private static void ghiDong(BufferedWriter out, String noiDung) throws IOException {
        out.write(noiDung);
        out.write("\r\n");
    }
}

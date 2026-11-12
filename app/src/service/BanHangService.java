package service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.BanHangDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.SanPham;

public class BanHangService {
    private final BanHangDAO dao;
    private final HoaDon hoaDon = new HoaDon();
    private final ArrayList<ChiTietHoaDon> dongHang = new ArrayList<ChiTietHoaDon>();

    public BanHangService(BanHangDAO dao) {
        this.dao = dao;
        hoaDon.setListChiTietHoaDon(dongHang);
    }

    public KhachHang chonKhachHang(String tuKhoa) throws SQLException {
        hoaDon.setKhachHang(null);
        if (tuKhoa.isEmpty()) {
            throw new IllegalArgumentException("Nhập mã khách hàng hoặc số điện thoại.");
        }
        KhachHang khachHang = dao.timKhachHang(tuKhoa);
        if (khachHang == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng.");
        }
        hoaDon.setKhachHang(khachHang);
        return khachHang;
    }

    public SanPham timSanPham(String ma) throws SQLException {
        if (ma.isEmpty()) {
            throw new IllegalArgumentException("Nhập mã sản phẩm.");
        }
        SanPham sanPham = dao.timSanPham(ma);
        if (sanPham == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm.");
        }
        if (!"Đang hoạt động".equals(sanPham.getTrangThai())) {
            throw new IllegalArgumentException("Sản phẩm đã ngừng hoạt động.");
        }
        return sanPham;
    }

    public void themDong(String ma, int soLuong) throws SQLException {
        kiemTraSoLuongDuong(soLuong);
        SanPham sanPham = timSanPham(ma);
        for (ChiTietHoaDon dong : dongHang) {
            if (sanPham.equals(dong.getSanPham())) {
                kiemTraTon(sanPham, (long) dong.getSoLuong() + soLuong);
                dong.setSoLuong(dong.getSoLuong() + soLuong);
                dong.setSanPham(sanPham);
                return;
            }
        }
        kiemTraTon(sanPham, soLuong);
        dongHang.add(new ChiTietHoaDon(sanPham, soLuong, sanPham.getGiaBan()));
    }

    public void suaSoLuong(int viTri, int soLuong) throws SQLException {
        kiemTraSoLuongDuong(soLuong);
        ChiTietHoaDon dong = dongHang.get(viTri);
        SanPham sanPham = timSanPham(dong.getSanPham().getMaSanPham());
        kiemTraTon(sanPham, soLuong);
        dong.setSanPham(sanPham);
        dong.setSoLuong(soLuong);
    }

    public void xoaDong(int viTri) {
        dongHang.remove(viTri);
    }

    public ArrayList<ChiTietHoaDon> layDongHang() {
        return new ArrayList<ChiTietHoaDon>(dongHang);
    }

    public BigDecimal tinhTongTienHang() {
        return hoaDon.tinhTongTienSP();
    }

    private void kiemTraSoLuongDuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
    }

    private void kiemTraTon(SanPham sanPham, long soLuong) {
        if (soLuong > sanPham.getSoLuongTon()) {
            throw new IllegalArgumentException("Số lượng vượt tồn kho (" + sanPham.getSoLuongTon() + ").");
        }
    }
}

package service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import dao.BanHangDAO;
import dao.ThanhToanDAO;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

public class BanHangService {
    private static final int GIOI_HAN_GOI_Y = 20;
    private final BanHangDAO dao;
    private final ThanhToanDAO thanhToanDAO;
    private final HoaDon hoaDon = new HoaDon();
    private final ArrayList<ChiTietHoaDon> dongHang = new ArrayList<ChiTietHoaDon>();
    private ArrayList<KhachHang> danhMucKhachHang;

    public BanHangService(BanHangDAO dao) {
        this(dao, new ThanhToanDAO());
    }

    public BanHangService(BanHangDAO dao, ThanhToanDAO thanhToanDAO) {
        this.dao = dao;
        this.thanhToanDAO = thanhToanDAO;
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

    public ArrayList<KhachHang> goiYKhachHang(String tuKhoa) throws SQLException {
        if (danhMucKhachHang == null) {
            danhMucKhachHang = dao.layKhachHang();
        }
        Pattern mau = Pattern.compile(Pattern.quote(tuKhoa), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        ArrayList<KhachHang> ketQua = new ArrayList<KhachHang>();
        for (KhachHang kh : danhMucKhachHang) {
            if (tuKhoa.equalsIgnoreCase(kh.getMaKhachHang())) {
                ketQua.clear();
                ketQua.add(kh);
                return ketQua;
            }
            if (ketQua.size() < GIOI_HAN_GOI_Y && (mau.matcher(kh.getMaKhachHang()).find()
                    || mau.matcher(kh.getSoDienThoai()).find() || mau.matcher(kh.getHo() + " " + kh.getTen()).find())) {
                ketQua.add(kh);
            }
        }
        return ketQua;
    }

    public void napDanhMucTraCuu() throws SQLException {
        danhMucKhachHang = null;
        danhMucKhachHang = dao.layKhachHang();
    }

    public void chonKhachHangGoiY(KhachHang khachHang) {
        hoaDon.setKhachHang(khachHang);
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

    public ArrayList<ChuongTrinhKhuyenMai> layKhuyenMai() throws SQLException {
        return thanhToanDAO.layKhuyenMai(LocalDate.now());
    }

    public void chonKhuyenMai(ChuongTrinhKhuyenMai khuyenMai) {
        hoaDon.setChuongTrinhKM(khuyenMai);
    }

    public BigDecimal tinhTienGiam() {
        return hoaDon.tinhTienKhuyenMai(tinhTongTienHang());
    }

    public BigDecimal tinhTongThanhToan() {
        return hoaDon.tinhTongTienThanhToan();
    }

    public BigDecimal tinhTienThoi(BigDecimal tienKhachDua) {
        return tienKhachDua.subtract(tinhTongThanhToan()).max(BigDecimal.ZERO);
    }

    public boolean coDongHang() {
        return !dongHang.isEmpty();
    }

    public boolean daChonKhachHang(String tuKhoa) {
        KhachHang khachHang = hoaDon.getKhachHang();
        return khachHang != null && (tuKhoa.equalsIgnoreCase(khachHang.getMaKhachHang())
                || tuKhoa.equals(khachHang.getSoDienThoai()));
    }

    public String thanhToan(String phuongThuc, BigDecimal tienKhachDua, NhanVien nhanVien) throws SQLException {
        if (dongHang.isEmpty() || hoaDon.getKhachHang() == null || nhanVien == null) {
            throw new IllegalArgumentException("Cần có dòng hàng, khách hàng và nhân viên.");
        }
        LocalDateTime thoiDiem = LocalDateTime.now().withNano(0);
        ChuongTrinhKhuyenMai daChon = hoaDon.getChuongTrinhKM();
        if (daChon != null) {
            boolean conHieuLuc = false;
            for (ChuongTrinhKhuyenMai hienTai : thanhToanDAO.layKhuyenMai(thoiDiem.toLocalDate())) {
                if (daChon.equals(hienTai) && daChon.getLoaiKhuyenMai().equals(hienTai.getLoaiKhuyenMai())
                        && daChon.getGiaTriKM().compareTo(hienTai.getGiaTriKM()) == 0) {
                    conHieuLuc = true;
                    break;
                }
            }
            if (!conHieuLuc) {
                throw new IllegalArgumentException("Khuyến mãi đã đổi hoặc hết hiệu lực. Chọn lại khuyến mãi.");
            }
        }
        BigDecimal tong = tinhTongThanhToan();
        if ("Chuyển khoản".equals(phuongThuc)) {
            tienKhachDua = tong;
        } else if (!"Tiền mặt".equals(phuongThuc)) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ.");
        }
        if (tienKhachDua == null || tienKhachDua.compareTo(tong) < 0) {
            throw new IllegalArgumentException("Tiền khách đưa chưa đủ.");
        }
        if (tienKhachDua.stripTrailingZeros().scale() > 0 || tienKhachDua.compareTo(
                new BigDecimal("999999999999999999")) > 0) {
            throw new IllegalArgumentException("Số tiền phải là số nguyên tối đa 18 chữ số.");
        }
        HoaDon hoaDonLuu = new HoaDon(null, thoiDiem, phuongThuc, "Đã thanh toán", tienKhachDua, thoiDiem,
                layDongHang(), daChon, nhanVien, hoaDon.getKhachHang());
        String tienTo = "HD" + thoiDiem.format(DateTimeFormatter.ofPattern("ddMMyy"));
        String ma = thanhToanDAO.luuHoaDon(hoaDonLuu, tienTo);
        dongHang.clear();
        hoaDon.setKhachHang(null);
        hoaDon.setChuongTrinhKM(null);
        return ma;
    }

    private void kiemTraSoLuongDuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
    }

    private void kiemTraTon(SanPham sanPham, long soLuong) {
        int tonKho = sanPham.getSoLuongTon();
        if (tonKho == 0) {
            throw new IllegalArgumentException("Sản phẩm " + sanPham.getTenSanPham() + " đã hết hàng.");
        }
        if (soLuong > tonKho) {
            throw new IllegalArgumentException("Sản phẩm " + sanPham.getTenSanPham() + " chỉ còn "
                    + tonKho + " " + sanPham.getDonViTinh() + " trong kho. Tổng số lượng yêu cầu: " + soLuong + ".");
        }
    }
}

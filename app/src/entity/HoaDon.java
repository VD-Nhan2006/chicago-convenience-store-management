package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class HoaDon {	
	private String maHoaDon;
	private LocalDateTime ngayGioLap;
	private String phuongThucThanhToan;
	private String trangThaiThanhToan;
	private BigDecimal tienKhachDua;
	private BigDecimal tienGiamDaLuu;
	private BigDecimal tongThanhToanDaLuu;
	private LocalDateTime thoiGianThanhToan;
	private static final double VAT = 8;
	private ArrayList<ChiTietHoaDon> listChiTietHoaDon;
	private ChuongTrinhKhuyenMai chuongTrinhKM;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	
	public HoaDon() {
	}
	public HoaDon(String maHoaDon) {
		setMaHoaDon(maHoaDon);
	}
	public HoaDon(String maHoaDon, LocalDateTime ngayGioLap, String phuongThucThanhToan, String trangThaiThanhToan,
			BigDecimal tienKhachDua, LocalDateTime thoiGianThanhToan, ArrayList<ChiTietHoaDon> listChiTietHoaDon,
			ChuongTrinhKhuyenMai chuongTrinhKM, NhanVien nhanVien, KhachHang khachHang) {
		setMaHoaDon(maHoaDon);
		setNgayGioLap(ngayGioLap);
		setPhuongThucThanhToan(phuongThucThanhToan);
		setTrangThaiThanhToan(trangThaiThanhToan);
		setTienKhachDua(tienKhachDua);
		setThoiGianThanhToan(thoiGianThanhToan);
		setListChiTietHoaDon(listChiTietHoaDon);
		setChuongTrinhKM(chuongTrinhKM);
		setNhanVien(nhanVien);
		setKhachHang(khachHang);
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		if (maHoaDon == null) {
			this.maHoaDon = null;
			return;
		}
		RangBuoc.maDinhDanh(maHoaDon, "HD", 4, 9999, "Mã hóa đơn");
		this.maHoaDon = maHoaDon;
	}
	public LocalDateTime getNgayGioLap() {
		return ngayGioLap;
	}
	public void setNgayGioLap(LocalDateTime ngayGioLap) {
		if (ngayGioLap == null) {
			throw new IllegalArgumentException("Ngày giờ lập không được để trống.");
		}
		if (thoiGianThanhToan != null && thoiGianThanhToan.isBefore(ngayGioLap)) {
			throw new IllegalArgumentException("Thời gian thanh toán không được sớm hơn ngày giờ lập.");
		}
		this.ngayGioLap = ngayGioLap;
	}
	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}
	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		RangBuoc.motTrong(phuongThucThanhToan, 30, "Phương thức thanh toán", "Tiền mặt", "Chuyển khoản");
		this.phuongThucThanhToan = phuongThucThanhToan;
	}
	public String getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}
	public void setTrangThaiThanhToan(String trangThaiThanhToan) {
		RangBuoc.motTrong(trangThaiThanhToan, 30, "Trạng thái thanh toán", "Đã thanh toán");
		this.trangThaiThanhToan = trangThaiThanhToan;
	}
	public BigDecimal getTienKhachDua() {
		return tienKhachDua;
	}
	public void setTienKhachDua(BigDecimal tienKhachDua) {
		RangBuoc.soNguyenKhongAm(tienKhachDua, "Tiền khách đưa");
		this.tienKhachDua = tienKhachDua;
	}
	public LocalDateTime getThoiGianThanhToan() {
		return thoiGianThanhToan;
	}
	public void setThoiGianThanhToan(LocalDateTime thoiGianThanhToan) {
		if (thoiGianThanhToan == null) {
			throw new IllegalArgumentException("Thời gian thanh toán không được để trống.");
		}
		if (ngayGioLap != null && thoiGianThanhToan.isBefore(ngayGioLap)) {
			throw new IllegalArgumentException("Thời gian thanh toán không được sớm hơn ngày giờ lập.");
		}
		this.thoiGianThanhToan = thoiGianThanhToan;
	}
	public ArrayList<ChiTietHoaDon> getListChiTietHoaDon() {
		return listChiTietHoaDon;
	}
	public void setListChiTietHoaDon(ArrayList<ChiTietHoaDon> listChiTietHoaDon) {
		if (listChiTietHoaDon != null) {
			for (ChiTietHoaDon dong : listChiTietHoaDon) {
				if (dong == null || dong.getSanPham() == null || dong.getGiaBan() == null || dong.getSoLuong() <= 0) {
					throw new IllegalArgumentException("Chi tiết hóa đơn không hợp lệ.");
				}
			}
		}
		this.listChiTietHoaDon = listChiTietHoaDon;
	}
	public ChuongTrinhKhuyenMai getChuongTrinhKM() {
		return chuongTrinhKM;
	}
	public void setChuongTrinhKM(ChuongTrinhKhuyenMai chuongTrinhKM) {
		if (chuongTrinhKM != null && chuongTrinhKM.getMaKM() == null) {
			throw new IllegalArgumentException("Chương trình khuyến mãi không hợp lệ.");
		}
		this.chuongTrinhKM = chuongTrinhKM;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null || nhanVien.getMaNhanVien() == null) {
			throw new IllegalArgumentException("Hóa đơn phải gắn với một nhân viên.");
		}
		this.nhanVien = nhanVien;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		if (khachHang != null && khachHang.getMaKhachHang() == null) {
			throw new IllegalArgumentException("Khách hàng không hợp lệ.");
		}
		this.khachHang = khachHang;
	}
	public static double getVat() {
		return VAT;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maHoaDon);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHoaDon, other.maHoaDon);
	}
	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayGioLap=" + ngayGioLap + ", phuongThucThanhToan="
				+ phuongThucThanhToan + ", trangThaiThanhToan=" + trangThaiThanhToan + ", tienKhachDua=" + tienKhachDua
				+ ", thoiGianThanhToan=" + thoiGianThanhToan + ", listChiTietHoaDon=" + listChiTietHoaDon
				+ ", chuongTrinhKM=" + chuongTrinhKM + ", nhanVien=" + nhanVien + ", khachHang=" + khachHang + "]";
	}
	public BigDecimal tinhTongTienSP() {
		BigDecimal tongTienSP = BigDecimal.ZERO;
		if(listChiTietHoaDon == null || listChiTietHoaDon.isEmpty()) {
			return tongTienSP;
		}
		for(ChiTietHoaDon chiTietHoaDon : listChiTietHoaDon) {
			tongTienSP = tongTienSP.add(chiTietHoaDon.tinhThanhTien());
		}
		return tongTienSP;
	}
	
	public BigDecimal tinhTienKhuyenMai(BigDecimal tongTienSP) {
		if (tienGiamDaLuu != null) {
			return tienGiamDaLuu;
		}
		BigDecimal tienKhuyenMai = BigDecimal.ZERO;
		if(chuongTrinhKM == null||tongTienSP == null||tongTienSP.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO; 
		}
		
		BigDecimal giaTriKM = chuongTrinhKM.getGiaTriKM();
		if(giaTriKM == null || giaTriKM.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO;
		}
		
		if("TIEN_MAT".equals(chuongTrinhKM.getLoaiKhuyenMai())) {
			tienKhuyenMai = giaTriKM.setScale(0, RoundingMode.HALF_UP).min(tongTienSP);
		}else if("PHAN_TRAM".equals(chuongTrinhKM.getLoaiKhuyenMai())) {
			tienKhuyenMai = tongTienSP.multiply(giaTriKM).divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP).min(tongTienSP);
		}else {
			tienKhuyenMai = BigDecimal.ZERO;
		}
		return tienKhuyenMai;
	}
	public BigDecimal tinhTienThoi() {
		if(tienKhachDua == null || tienKhachDua.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO;
		}
		return tienKhachDua.subtract(tinhTongTienThanhToan());
	}
	private BigDecimal tinhVAT(BigDecimal tien) {
		return tien.multiply(BigDecimal.valueOf(VAT)).divide(new BigDecimal("100"),0,RoundingMode.HALF_UP);
	}
	public BigDecimal tinhTongTienThanhToan() {
		if (tongThanhToanDaLuu != null) {
			return tongThanhToanDaLuu;
		}
		BigDecimal tongTienSP = tinhTongTienSP();
		BigDecimal tienSauKM = tongTienSP.subtract(tinhTienKhuyenMai(tongTienSP));
		BigDecimal tienVAT = tinhVAT(tienSauKM);
		return tienSauKM.add(tienVAT);
	}

	public void setSoTienDaLuu(BigDecimal tienGiam, BigDecimal tongThanhToan) {
		if (tienGiam != null) {
			RangBuoc.soNguyenKhongAm(tienGiam, "Tiền được giảm");
		}
		if (tongThanhToan != null) {
			RangBuoc.soNguyenKhongAm(tongThanhToan, "Tổng thanh toán");
		}
		this.tienGiamDaLuu = tienGiam;
		this.tongThanhToanDaLuu = tongThanhToan;
	}
	
}	

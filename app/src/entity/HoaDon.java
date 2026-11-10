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
	private LocalDateTime thoiGianThanhToan;
	private static final double VAT = 8;
	private ArrayList<ChiTietHoaDon> listChiTietHoaDon;
	private ChuongTrinhKhuyenMai chuongTrinhKM;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	
	public HoaDon() {
	}
	public HoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public HoaDon(String maHoaDon, LocalDateTime ngayGioLap, String phuongThucThanhToan, String trangThaiThanhToan,
			BigDecimal tienKhachDua, LocalDateTime thoiGianThanhToan, ArrayList<ChiTietHoaDon> listChiTietHoaDon,
			ChuongTrinhKhuyenMai chuongTrinhKM, NhanVien nhanVien, KhachHang khachHang) {
		this.maHoaDon = maHoaDon;
		this.ngayGioLap = ngayGioLap;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.trangThaiThanhToan = trangThaiThanhToan;
		this.tienKhachDua = tienKhachDua;
		this.thoiGianThanhToan = thoiGianThanhToan;
		this.listChiTietHoaDon = listChiTietHoaDon;
		this.chuongTrinhKM = chuongTrinhKM;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public LocalDateTime getNgayGioLap() {
		return ngayGioLap;
	}
	public void setNgayGioLap(LocalDateTime ngayGioLap) {
		this.ngayGioLap = ngayGioLap;
	}
	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}
	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}
	public String getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}
	public void setTrangThaiThanhToan(String trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}
	public BigDecimal getTienKhachDua() {
		return tienKhachDua;
	}
	public void setTienKhachDua(BigDecimal tienKhachDua) {
		this.tienKhachDua = tienKhachDua;
	}
	public LocalDateTime getThoiGianThanhToan() {
		return thoiGianThanhToan;
	}
	public void setThoiGianThanhToan(LocalDateTime thoiGianThanhToan) {
		this.thoiGianThanhToan = thoiGianThanhToan;
	}
	public ArrayList<ChiTietHoaDon> getListChiTietHoaDon() {
		return listChiTietHoaDon;
	}
	public void setListChiTietHoaDon(ArrayList<ChiTietHoaDon> listChiTietHoaDon) {
		this.listChiTietHoaDon = listChiTietHoaDon;
	}
	public ChuongTrinhKhuyenMai getChuongTrinhKM() {
		return chuongTrinhKM;
	}
	public void setChuongTrinhKM(ChuongTrinhKhuyenMai chuongTrinhKM) {
		this.chuongTrinhKM = chuongTrinhKM;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
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
		BigDecimal tienKhuyenMai = BigDecimal.ZERO;
		if(chuongTrinhKM == null||tongTienSP == null||tongTienSP.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO; 
		}
		
		BigDecimal giaTriKM = chuongTrinhKM.getGiaTriKM();
		if(giaTriKM == null || giaTriKM.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO;
		}
		
		if("TIEN_MAT".equals(chuongTrinhKM.getLoaiKhuyenMai())) {
			tienKhuyenMai = giaTriKM.min(tongTienSP);
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
		BigDecimal tongTienSP = tinhTongTienSP();
		BigDecimal tienSauKM = tongTienSP.subtract(tinhTienKhuyenMai(tongTienSP));
		BigDecimal tienVAT = tinhVAT(tienSauKM);
		return tienSauKM.add(tienVAT);
	}
	
}	

package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon {
	private SanPham sanPham;
	private int soLuong;
	private BigDecimal giaBan;
	public ChiTietHoaDon() {
	}
	public ChiTietHoaDon(SanPham sanPham) {
		this.sanPham = sanPham;
	}
	public ChiTietHoaDon(SanPham sanPham, int soLuong, BigDecimal giaBan) {
		this.sanPham = sanPham;
		this.soLuong = soLuong;
		this.giaBan = giaBan;
	}
	public SanPham getSanPham() {
		return sanPham;
	}
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public BigDecimal getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(BigDecimal giaBan) {
		this.giaBan = giaBan;
	}
	public BigDecimal tinhThanhTien() {
		return giaBan.multiply(BigDecimal.valueOf(soLuong));
	}
	@Override
	public String toString() {
		return "ChiTietHoaDon [sanPham=" + sanPham + ", soLuong=" + soLuong + ", giaBan=" + giaBan + "]";
	}
	
	
}

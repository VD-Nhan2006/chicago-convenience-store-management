package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class SanPham {	
	private String maSanPham;
	private String tenSanPham;
	private String donViTinh;
	private int soLuongTon;
	private BigDecimal giaNhap;
	private BigDecimal mucLoiNhuan;
	private BigDecimal giaBan;
	private String trangThai;
	private LoaiSanPham loaiSanPham;
	public SanPham() {
	}
	public SanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public SanPham(String maSanPham, String tenSanPham, String donViTinh, int soLuongTon, BigDecimal giaNhap,
			BigDecimal mucLoiNhuan, String trangThai, LoaiSanPham loaiSanPham) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.donViTinh = donViTinh;
		this.soLuongTon = soLuongTon;
		this.giaNhap = giaNhap;
		this.mucLoiNhuan = mucLoiNhuan;
		this.trangThai = trangThai;
		this.loaiSanPham = loaiSanPham;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public String getDonViTinh() {
		return donViTinh;
	}
	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}
	public int getSoLuongTon() {
		return soLuongTon;
	}
	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}
	public BigDecimal getGiaNhap() {
		return giaNhap;
	}
	public void setGiaNhap(BigDecimal giaNhap) {
		this.giaNhap = giaNhap;
	}
	public BigDecimal getMucLoiNhuan() {
		return mucLoiNhuan;
	}
	public void setMucLoiNhuan(BigDecimal mucLoiNhuan) {
		this.mucLoiNhuan = mucLoiNhuan;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public LoaiSanPham getLoaiSanPham() {
		return loaiSanPham;
	}
	public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
		this.loaiSanPham = loaiSanPham;
	}
	public BigDecimal getGiaBan() {
		giaBan = giaNhap.add(mucLoiNhuan);
		return giaBan;
	}
	@Override
	public String toString() {
		return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham + ", donViTinh=" + donViTinh
				+ ", soLuongTon=" + soLuongTon + ", giaNhap=" + giaNhap + ", mucLoiNhuan=" + mucLoiNhuan + ", giaBan="
				+ giaBan + ", trangThai=" + trangThai + ", loaiSanPham=" + loaiSanPham + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(maSanPham);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return Objects.equals(maSanPham, other.maSanPham);
	}
}

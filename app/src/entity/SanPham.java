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
		setMaSanPham(maSanPham);
	}

	public SanPham(String maSanPham, String tenSanPham, String donViTinh, int soLuongTon, BigDecimal giaNhap,
			BigDecimal mucLoiNhuan, String trangThai, LoaiSanPham loaiSanPham) {
		setMaSanPham(maSanPham);
		setTenSanPham(tenSanPham);
		setDonViTinh(donViTinh);
		setSoLuongTon(soLuongTon);
		setGiaNhap(giaNhap);
		setMucLoiNhuan(mucLoiNhuan);
		setTrangThai(trangThai);
		setLoaiSanPham(loaiSanPham);
	}

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		RangBuoc.maDinhDanh(maSanPham, "SP", 3, 999, "Mã sản phẩm");
		this.maSanPham = maSanPham;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		RangBuoc.chuoiBatBuoc(tenSanPham, 150, "Tên sản phẩm");
		this.tenSanPham = tenSanPham;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		RangBuoc.chuoiBatBuoc(donViTinh, 30, "Đơn vị tính");
		this.donViTinh = donViTinh;
	}

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		if (soLuongTon < 0) {
			throw new IllegalArgumentException("Số lượng tồn không được âm.");
		}
		this.soLuongTon = soLuongTon;
	}

	public BigDecimal getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(BigDecimal giaNhap) {
		RangBuoc.soNguyenKhongAm(giaNhap, "Giá nhập");
		this.giaNhap = giaNhap;
	}

	public BigDecimal getMucLoiNhuan() {
		return mucLoiNhuan;
	}

	public void setMucLoiNhuan(BigDecimal mucLoiNhuan) {
		RangBuoc.soNguyenKhongAm(mucLoiNhuan, "Mức lợi nhuận");
		this.mucLoiNhuan = mucLoiNhuan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		RangBuoc.motTrong(trangThai, 30, "Trạng thái sản phẩm", "Đang hoạt động", "Ngừng hoạt động");
		this.trangThai = trangThai;
	}

	public LoaiSanPham getLoaiSanPham() {
		return loaiSanPham;
	}

	public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
		if (loaiSanPham == null || loaiSanPham.getMaLoaiSanPham() == null) {
			throw new IllegalArgumentException("Sản phẩm phải thuộc một loại sản phẩm.");
		}
		this.loaiSanPham = loaiSanPham;
	}

	public BigDecimal getGiaBan() {
		if (giaNhap == null || mucLoiNhuan == null) {
			throw new IllegalArgumentException("Giá nhập và mức lợi nhuận phải có trước khi tính giá bán.");
		}
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

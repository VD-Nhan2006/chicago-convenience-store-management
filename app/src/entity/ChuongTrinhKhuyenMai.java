package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ChuongTrinhKhuyenMai {
	private String maKM;
	private String tenChuongTrinh;
	private String moTa;
	private String loaiKhuyenMai;
	private BigDecimal giaTriKM;
	private LocalDate ngayBatDau;
	private LocalDate ngayKetThuc;
	private String trangThai;
	public ChuongTrinhKhuyenMai() {
	}
	public ChuongTrinhKhuyenMai(String maKM) {
		this.maKM = maKM;
	}
	public ChuongTrinhKhuyenMai(String maKM, String tenChuongTrinh, String moTa, String loaiKhuyenMai,
			BigDecimal giaTriKM, LocalDate ngayBatDau, LocalDate ngayKetThuc, String trangThai) {
		this.maKM = maKM;
		this.tenChuongTrinh = tenChuongTrinh;
		this.moTa = moTa;
		this.loaiKhuyenMai = loaiKhuyenMai;
		this.giaTriKM = giaTriKM;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.trangThai = trangThai;
	}
	public String getMaKM() {
		return maKM;
	}
	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}
	public String getTenChuongTrinh() {
		return tenChuongTrinh;
	}
	public void setTenChuongTrinh(String tenChuongTrinh) {
		this.tenChuongTrinh = tenChuongTrinh;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getLoaiKhuyenMai() {
		return loaiKhuyenMai;
	}
	public void setLoaiKhuyenMai(String loaiKhuyenMai) {
		this.loaiKhuyenMai = loaiKhuyenMai;
	}
	public BigDecimal getGiaTriKM() {
		return giaTriKM;
	}
	public void setGiaTriKM(BigDecimal giaTriKM) {
		this.giaTriKM = giaTriKM;
	}
	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}
	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}
	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}
	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "ChuongTrinhKhuyenMai [maKM=" + maKM + ", tenChuongTrinh=" + tenChuongTrinh + ", moTa=" + moTa
				+ ", loaiKhuyenMai=" + loaiKhuyenMai + ", giaTriKM=" + giaTriKM + ", ngayBatDau=" + ngayBatDau
				+ ", ngayKetThuc=" + ngayKetThuc + ", trangThai=" + trangThai + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChuongTrinhKhuyenMai other = (ChuongTrinhKhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}
	
	
	
}

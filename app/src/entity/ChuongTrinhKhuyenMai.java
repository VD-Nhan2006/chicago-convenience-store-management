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
		setMaKM(maKM);
	}

	public ChuongTrinhKhuyenMai(String maKM, String tenChuongTrinh, String moTa, String loaiKhuyenMai,
			BigDecimal giaTriKM, LocalDate ngayBatDau, LocalDate ngayKetThuc, String trangThai) {
		setMaKM(maKM);
		setTenChuongTrinh(tenChuongTrinh);
		setMoTa(moTa);
		setLoaiKhuyenMai(loaiKhuyenMai);
		setGiaTriKM(giaTriKM);
		setNgayBatDau(ngayBatDau);
		setNgayKetThuc(ngayKetThuc);
		setTrangThai(trangThai);
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		RangBuoc.maDinhDanh(maKM, "KM", 3, 999, "Mã khuyến mãi");
		this.maKM = maKM;
	}

	public String getTenChuongTrinh() {
		return tenChuongTrinh;
	}

	public void setTenChuongTrinh(String tenChuongTrinh) {
		RangBuoc.chuoiBatBuoc(tenChuongTrinh, 150, "Tên chương trình khuyến mãi");
		this.tenChuongTrinh = tenChuongTrinh;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		RangBuoc.chuoiBatBuoc(moTa, 500, "Mô tả khuyến mãi");
		this.moTa = moTa;
	}

	public String getLoaiKhuyenMai() {
		return loaiKhuyenMai;
	}

	public void setLoaiKhuyenMai(String loaiKhuyenMai) {
		RangBuoc.motTrong(loaiKhuyenMai, 20, "Loại khuyến mãi", "TIEN_MAT", "PHAN_TRAM");
		if ("PHAN_TRAM".equals(loaiKhuyenMai) && giaTriKM != null && giaTriKM.compareTo(new BigDecimal("100")) > 0) {
			throw new IllegalArgumentException("Giá trị khuyến mãi theo phần trăm không được lớn hơn 100.");
		}
		this.loaiKhuyenMai = loaiKhuyenMai;
	}

	public BigDecimal getGiaTriKM() {
		return giaTriKM;
	}

	public void setGiaTriKM(BigDecimal giaTriKM) {
		RangBuoc.giaTriKhuyenMai(giaTriKM, loaiKhuyenMai);
		this.giaTriKM = giaTriKM;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		if (ngayBatDau == null) {
			throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
		}
		if (ngayKetThuc != null && ngayKetThuc.isBefore(ngayBatDau)) {
			throw new IllegalArgumentException("Ngày kết thúc không được sớm hơn ngày bắt đầu.");
		}
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		if (ngayKetThuc == null) {
			throw new IllegalArgumentException("Ngày kết thúc không được để trống.");
		}
		if (ngayBatDau != null && ngayKetThuc.isBefore(ngayBatDau)) {
			throw new IllegalArgumentException("Ngày kết thúc không được sớm hơn ngày bắt đầu.");
		}
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		RangBuoc.motTrong(trangThai, 30, "Trạng thái khuyến mãi", "Đang hoạt động", "Ngừng hoạt động");
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

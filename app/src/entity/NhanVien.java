package entity;

import java.util.Objects;

public class NhanVien {
	private String maNhanVien;
	private String ho;
	private String ten;
	private String soDienThoai;
	private String diaChi;
	private String trangThai;
	private TaiKhoan taiKhoan;
	

	public NhanVien() {
	}

	public NhanVien(String maNhanVien) {
		setMaNhanVien(maNhanVien);
	}

	public NhanVien(String maNhanVien, String ho, String ten, String soDienThoai, String diaChi, String trangThai) {
		setMaNhanVien(maNhanVien);
		setHo(ho);
		setTen(ten);
		setSoDienThoai(soDienThoai);
		setDiaChi(diaChi);
		setTrangThai(trangThai);
	}

	public TaiKhoan getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		RangBuoc.chuoiBatBuoc(diaChi, 255, "Địa chỉ nhân viên");
		this.diaChi = diaChi;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		RangBuoc.maDinhDanh(maNhanVien, "NV", 3, 999, "Mã nhân viên");
		this.maNhanVien = maNhanVien;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		RangBuoc.chuoiBatBuoc(ho, 50, "Họ nhân viên");
		this.ho = ho;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		RangBuoc.chuoiBatBuoc(ten, 50, "Tên nhân viên");
		this.ten = ten;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		RangBuoc.soDienThoai(soDienThoai);
		this.soDienThoai = soDienThoai;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		RangBuoc.motTrong(trangThai, 30, "Trạng thái nhân viên", "Đang hoạt động", "Ngừng hoạt động");
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", ho=" + ho + ", ten=" + ten + ", soDienThoai=" + soDienThoai
				+ ", trangThai=" + trangThai + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}

}

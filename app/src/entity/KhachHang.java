package entity;

import java.util.Objects;

public class KhachHang {
	private String maKhachHang;
	private String ho;
	private String ten;
	private String gioiTinh;
	private String soDienThoai;
	private String diaChi;

	public KhachHang() {
	}

	public KhachHang(String maKhachHang) {
		setMaKhachHang(maKhachHang);
	}

	public KhachHang(String maKhachHang, String ho, String ten, String gioiTinh, String soDienThoai, String diaChi) {
		setMaKhachHang(maKhachHang);
		setHo(ho);
		setTen(ten);
		setGioiTinh(gioiTinh);
		setSoDienThoai(soDienThoai);
		setDiaChi(diaChi);
	}

	public String getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(String maKhachHang) {
		RangBuoc.maDinhDanh(maKhachHang, "KH", 3, 999, "Mã khách hàng");
		this.maKhachHang = maKhachHang;
	}

	public String getHo() {
		return ho;
	}

	public void setHo(String ho) {
		RangBuoc.chuoiBatBuoc(ho, 50, "Họ khách hàng");
		this.ho = ho;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		RangBuoc.chuoiBatBuoc(ten, 50, "Tên khách hàng");
		this.ten = ten;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		RangBuoc.motTrong(gioiTinh, 10, "Giới tính", "Nam", "Nữ");
		this.gioiTinh = gioiTinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		RangBuoc.soDienThoai(soDienThoai);
		this.soDienThoai = soDienThoai;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		RangBuoc.chuoiBatBuoc(diaChi, 255, "Địa chỉ khách hàng");
		this.diaChi = diaChi;
	}

	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", ho=" + ho + ", ten=" + ten + ", gioiTinh=" + gioiTinh
				+ ", soDienThoai=" + soDienThoai + ", diaChi=" + diaChi + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}

}

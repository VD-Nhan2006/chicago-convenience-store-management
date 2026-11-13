package entity;

public class LoaiSanPham {
	private String maLoaiSanPham;
	private String tenLoaiSanPham;
	private String moTa;
	private String trangThai;
	public LoaiSanPham() {
	}
	public LoaiSanPham(String maLoaiSanPham) {
		this.maLoaiSanPham = maLoaiSanPham;
	}
	public LoaiSanPham(String maLoaiSanPham, String tenLoaiSanPham, String moTa, String trangThai) {
		this.maLoaiSanPham = maLoaiSanPham;
		this.tenLoaiSanPham = tenLoaiSanPham;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}
	public String getMaLoaiSanPham() {
		return maLoaiSanPham;
	}
	public void setMaLoaiSanPham(String maLoaiSanPham) {
		this.maLoaiSanPham = maLoaiSanPham;
	}
	public String getTenLoaiSanPham() {
		return tenLoaiSanPham;
	}
	public void setTenLoaiSanPham(String tenLoaiSanPham) {
		this.tenLoaiSanPham = tenLoaiSanPham;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	@Override
	public String toString() {
		return "LoaiSanPham [maLoaiSanPham=" + maLoaiSanPham + ", tenLoaiSanPham=" + tenLoaiSanPham + ", moTa=" + moTa
				+ ", trangThai=" + trangThai + "]";
	}
	
}

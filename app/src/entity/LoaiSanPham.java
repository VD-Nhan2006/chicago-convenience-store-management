package entity;

public class LoaiSanPham {
	private String maLoaiSanPham;
	private String tenLoaiSanPham;
	private String moTa;
	private String trangThai;

	public LoaiSanPham() {
	}

	public LoaiSanPham(String maLoaiSanPham) {
		setMaLoaiSanPham(maLoaiSanPham);
	}

	public LoaiSanPham(String maLoaiSanPham, String tenLoaiSanPham, String moTa, String trangThai) {
		setMaLoaiSanPham(maLoaiSanPham);
		setTenLoaiSanPham(tenLoaiSanPham);
		setMoTa(moTa);
		setTrangThai(trangThai);
	}

	public String getMaLoaiSanPham() {
		return maLoaiSanPham;
	}

	public void setMaLoaiSanPham(String maLoaiSanPham) {
		RangBuoc.maDinhDanh(maLoaiSanPham, "LSP", 3, 999, "Mã loại sản phẩm");
		this.maLoaiSanPham = maLoaiSanPham;
	}

	public String getTenLoaiSanPham() {
		return tenLoaiSanPham;
	}

	public void setTenLoaiSanPham(String tenLoaiSanPham) {
		RangBuoc.chuoiBatBuoc(tenLoaiSanPham, 100, "Tên loại sản phẩm");
		this.tenLoaiSanPham = tenLoaiSanPham;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		RangBuoc.chuoiBatBuoc(moTa, 500, "Mô tả loại sản phẩm");
		this.moTa = moTa;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		RangBuoc.motTrong(trangThai, 30, "Trạng thái loại sản phẩm", "Đang hoạt động", "Ngừng hoạt động");
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "LoaiSanPham [maLoaiSanPham=" + maLoaiSanPham + ", tenLoaiSanPham=" + tenLoaiSanPham + ", moTa=" + moTa
				+ ", trangThai=" + trangThai + "]";
	}

}

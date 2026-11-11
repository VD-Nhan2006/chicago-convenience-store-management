package entity;

/**
 * Danh sách các chức năng / mục trên thanh Menu bên trái hệ thống Chicago
 * Dựa trên thiết kế Figma chuẩn của dự án.
 */
public enum ChucNangMenu {
	DASHBOARD("Dashboard", "dashboard"),
	BAN_HANG("Bán hàng", "ban_hang"),
	TIM_KIEM_HOA_DON("Tìm kiếm hóa đơn", "tim_kiem_hoa_don"),
	QUAN_LY_KHACH_HANG("Quản lý khách hàng", "quan_ly_khach_hang"),
	QUAN_LY_SAN_PHAM("Quản lý sản phẩm", "quan_ly_san_pham"),
	QUAN_LY_LOAI_SAN_PHAM("Quản lý loại sản phẩm", "quan_ly_loai_san_pham"),
	QUAN_LY_NHAN_VIEN("Quản lý nhân viên", "quan_ly_nhan_vien"),
	QUAN_LY_TAI_KHOAN("Quản lý tài khoản", "quan_ly_tai_khoan"),
	QUAN_LY_KHUYEN_MAI("Quản lý khuyến mãi", "quan_ly_khuyen_mai"),
	THONG_KE("Thống kê", "thong_ke");

	private final String tenHienThi;
	private final String maChucNang;

	ChucNangMenu(String tenHienThi, String maChucNang) {
		this.tenHienThi = tenHienThi;
		this.maChucNang = maChucNang;
	}

	public String getTenHienThi() {
		return tenHienThi;
	}

	public String getMaChucNang() {
		return maChucNang;
	}

	@Override
	public String toString() {
		return tenHienThi;
	}
}

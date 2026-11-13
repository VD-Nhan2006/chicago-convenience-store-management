package session;

import entity.VaiTro;

public class UserSession {
	private static String tenDangNhap;
	private static String maNhanVien;
	private static VaiTro vaiTro;
	public UserSession() {
	}
	public static void dangNhap(String ma, String ten, VaiTro vt) {
		maNhanVien = ma;
		tenDangNhap = ten;
		vaiTro = vt;
	}
	public static String getMaNhanVien() {
		return maNhanVien;
	}
	public static String getTenDangNhap() {
		return tenDangNhap;
	}
	public static VaiTro getVaiTro() {
		return vaiTro;
	}
	
	public static void dangXuat() {
		maNhanVien = null;
		tenDangNhap = null;
		vaiTro = null;
	}
	
}

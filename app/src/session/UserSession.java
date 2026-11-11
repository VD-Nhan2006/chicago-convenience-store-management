package session;

import entity.VaiTro;

public class UserSession {
	private static String tenDangNhap;
	private static VaiTro vaiTro;
	public UserSession() {
	}
	public static void dangNhap(String ten, VaiTro vt) {
		tenDangNhap = ten;
		vaiTro = vt;
	}
	public static String getTenDangNhap() {
		return tenDangNhap;
	}
	public static VaiTro getVaiTro() {
		return vaiTro;
	}
	
	public static void dangXuat() {
		tenDangNhap = null;
		vaiTro = null;
	}
	
}

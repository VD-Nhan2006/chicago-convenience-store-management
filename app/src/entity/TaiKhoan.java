package entity;

public class TaiKhoan {
	private NhanVien nhanVien;
	private String matKhau;
	private String vaiTro;
	public TaiKhoan() {
	}
	public TaiKhoan(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public TaiKhoan(NhanVien nhanVien, String matKhau, String vaiTro) {
		this.nhanVien = nhanVien;
		this.matKhau = matKhau;
		this.vaiTro = vaiTro;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public String getVaiTro() {
		return vaiTro;
	}
	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}
	@Override
	public String toString() {
		return "TaiKhoan [nhanVien=" + nhanVien + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro + "]";
	}
	
	
}

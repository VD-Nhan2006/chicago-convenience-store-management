package entity;

public class TaiKhoan {
	private NhanVien nhanVien;
	private String matKhau;
	private VaiTro vaiTro;

	public TaiKhoan() {
	}

	public TaiKhoan(NhanVien nhanVien) {
		setNhanVien(nhanVien);
	}

	public TaiKhoan(NhanVien nhanVien, String matKhau, VaiTro vaiTro) {
		setNhanVien(nhanVien);
		setMatKhau(matKhau);
		setVaiTro(vaiTro);
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null || nhanVien.getMaNhanVien() == null) {
			throw new IllegalArgumentException("Tài khoản phải gắn với một nhân viên.");
		}
		this.nhanVien = nhanVien;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		if (matKhau == null) {
			this.matKhau = null;
			return;
		}
		RangBuoc.chuoiBatBuoc(matKhau, 255, "Mật khẩu");
		this.matKhau = matKhau;
	}

	public VaiTro getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(VaiTro vaiTro) {
		if (vaiTro == null) {
			throw new IllegalArgumentException("Vai trò không được để trống.");
		}
		this.vaiTro = vaiTro;
	}

	@Override
	public String toString() {
		return "TaiKhoan [nhanVien=" + nhanVien + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro + "]";
	}

}

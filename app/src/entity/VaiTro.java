package entity;

public enum VaiTro {
	QUAN_LY("Quản lý"),
	THU_NGAN("Thu ngân");

	private final String tenHienThi;

	VaiTro(String tenHienThi) {
		this.tenHienThi = tenHienThi;
	}

	public String getTenHienThi() {
		return tenHienThi;
	}

	public static VaiTro fromString(String text) {
		if (text == null) return THU_NGAN;
		String clean = text.trim().toLowerCase();
		if (clean.contains("quản lý") || clean.contains("quan ly") || clean.equals("quan_ly")) {
			return QUAN_LY;
		}
		return THU_NGAN;
	}

	@Override
	public String toString() {
		return tenHienThi;
	}
}

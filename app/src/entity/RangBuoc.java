package entity;

import java.math.BigDecimal;

final class RangBuoc {
	private RangBuoc() {
	}

	static void chuoiBatBuoc(String giaTri, int doDaiToiDa, String ten) {
		if (giaTri == null || giaTri.trim().isEmpty()) {
			throw new IllegalArgumentException(ten + " không được để trống.");
		}
		if (giaTri.length() > doDaiToiDa) {
			throw new IllegalArgumentException(ten + " không được dài quá " + doDaiToiDa + " ký tự.");
		}
	}

	static void motTrong(String giaTri, int doDaiToiDa, String ten, String... hopLe) {
		chuoiBatBuoc(giaTri, doDaiToiDa, ten);
		for (String mau : hopLe) {
			if (mau.equals(giaTri)) {
				return;
			}
		}
		throw new IllegalArgumentException(ten + " không hợp lệ.");
	}

	static void maDinhDanh(String ma, String tienTo, int soChuSoThuTu, int thuTuToiDa, String ten) {
		int doDai = tienTo.length() + 6 + soChuSoThuTu;
		if (ma == null || ma.length() != doDai || !ma.startsWith(tienTo)) {
			throw new IllegalArgumentException(ten + " không đúng định dạng.");
		}
		String phanSo = ma.substring(tienTo.length());
		for (int i = 0; i < phanSo.length(); i++) {
			if (!Character.isDigit(phanSo.charAt(i))) {
				throw new IllegalArgumentException(ten + " không đúng định dạng.");
			}
		}
		int ngay = Integer.parseInt(phanSo.substring(0, 2));
		int thang = Integer.parseInt(phanSo.substring(2, 4));
		int thuTu = Integer.parseInt(phanSo.substring(6));
		if (ngay < 1 || ngay > 31 || thang < 1 || thang > 12 || thuTu < 1 || thuTu > thuTuToiDa) {
			throw new IllegalArgumentException(ten + " không đúng định dạng.");
		}
	}

	static void soDienThoai(String soDienThoai) {
		if (soDienThoai == null || !soDienThoai.matches("\\d{10}")) {
			throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số.");
		}
	}

	static void soNguyenKhongAm(BigDecimal tien, String ten) {
		soThapPhan(tien, ten, 0, false);
	}

	static void giaTriKhuyenMai(BigDecimal giaTri, String loaiKhuyenMai) {
		soThapPhan(giaTri, "Giá trị khuyến mãi", 2, true);
		if ("PHAN_TRAM".equals(loaiKhuyenMai) && giaTri.compareTo(new BigDecimal("100")) > 0) {
			throw new IllegalArgumentException("Giá trị khuyến mãi theo phần trăm không được lớn hơn 100.");
		}
	}

	private static void soThapPhan(BigDecimal tien, String ten, int soLeToiDa, boolean phaiDuong) {
		if (tien == null) {
			throw new IllegalArgumentException(ten + " không được để trống.");
		}
		BigDecimal chuan = tien.stripTrailingZeros();
		if (chuan.scale() < 0) {
			chuan = chuan.setScale(0);
		}
		if (chuan.scale() > soLeToiDa || chuan.precision() > 18) {
			throw new IllegalArgumentException(ten + " không đúng định dạng số của cơ sở dữ liệu.");
		}
		int soSanh = tien.compareTo(BigDecimal.ZERO);
		if (phaiDuong && soSanh <= 0) {
			throw new IllegalArgumentException(ten + " phải lớn hơn 0.");
		}
		if (!phaiDuong && soSanh < 0) {
			throw new IllegalArgumentException(ten + " không được âm.");
		}
	}
}

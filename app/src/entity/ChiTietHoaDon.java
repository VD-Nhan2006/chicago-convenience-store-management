package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon {
	private SanPham sanPham;
	private int soLuong;
	private BigDecimal giaBan;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(SanPham sanPham) {
		setSanPham(sanPham);
	}

	public ChiTietHoaDon(SanPham sanPham, int soLuong, BigDecimal giaBan) {
		setSanPham(sanPham);
		setSoLuong(soLuong);
		setGiaBan(giaBan);
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		if (sanPham == null || sanPham.getMaSanPham() == null) {
			throw new IllegalArgumentException("Chi tiết hóa đơn phải gắn với một sản phẩm.");
		}
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng bán phải lớn hơn 0.");
		}
		this.soLuong = soLuong;
	}

	public BigDecimal getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(BigDecimal giaBan) {
		RangBuoc.soNguyenKhongAm(giaBan, "Đơn giá bán");
		this.giaBan = giaBan;
	}

	public BigDecimal tinhThanhTien() {
		if (giaBan == null) {
			throw new IllegalArgumentException("Đơn giá bán không được để trống.");
		}
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng bán phải lớn hơn 0.");
		}
		return giaBan.multiply(BigDecimal.valueOf(soLuong));
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [sanPham=" + sanPham + ", soLuong=" + soLuong + ", giaBan=" + giaBan + "]";
	}

}

package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private String maLoaiSanPham;
    private String tenLoaiSanPham;
    private String donViTinh;
    private int soLuongTon;
    private BigDecimal giaNhap;
    private BigDecimal mucLoiNhuan;
    private BigDecimal giaBan;
    private String trangThai;
    private LoaiSanPham loaiSanPham;

    public SanPham() {
    }

    public SanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public SanPham(String maSanPham, String tenSanPham, String donViTinh, int soLuongTon,
            BigDecimal giaNhap, BigDecimal mucLoiNhuan, String trangThai,
            LoaiSanPham loaiSanPham) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donViTinh = donViTinh;
        this.soLuongTon = soLuongTon;
        this.giaNhap = giaNhap;
        this.mucLoiNhuan = mucLoiNhuan;
        this.trangThai = trangThai;
        this.loaiSanPham = loaiSanPham;
        if (loaiSanPham != null) {
            this.maLoaiSanPham = loaiSanPham.getMaLoaiSanPham();
            this.tenLoaiSanPham = loaiSanPham.getTenLoaiSanPham();
        }
    }

    public SanPham(String maSP, String tenSP, String maLoai, String tenLoai,
            String donViTinh, int soLuongTon, BigDecimal giaNhap,
            BigDecimal mucLoiNhuan, BigDecimal giaBan, boolean trangThai) {
        this.maSanPham = maSP;
        this.tenSanPham = tenSP;
        this.maLoaiSanPham = maLoai;
        this.tenLoaiSanPham = tenLoai;
        this.donViTinh = donViTinh;
        this.soLuongTon = soLuongTon;
        this.giaNhap = giaNhap;
        this.mucLoiNhuan = mucLoiNhuan;
        this.giaBan = giaBan;
        this.trangThai = trangThai ? "Đang hoạt động" : "Ngừng hoạt động";
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
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

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public BigDecimal getMucLoiNhuan() {
        return mucLoiNhuan;
    }

    public void setMucLoiNhuan(BigDecimal mucLoiNhuan) {
        this.mucLoiNhuan = mucLoiNhuan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
        if (loaiSanPham != null) {
            maLoaiSanPham = loaiSanPham.getMaLoaiSanPham();
            tenLoaiSanPham = loaiSanPham.getTenLoaiSanPham();
        }
    }

    public BigDecimal getGiaBan() {
        if (giaBan == null && giaNhap != null && mucLoiNhuan != null) {
            giaBan = giaNhap.add(mucLoiNhuan);
        }
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public String getMaSP() {
        return maSanPham;
    }

    public String getTenSP() {
        return tenSanPham;
    }

    public String getMaLoai() {
        return maLoaiSanPham;
    }

    public String getTenLoai() {
        return tenLoaiSanPham;
    }

    public boolean isTrangThai() {
        return "Đang hoạt động".equals(trangThai);
    }

    @Override
    public String toString() {
        return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham
                + ", donViTinh=" + donViTinh + ", soLuongTon=" + soLuongTon
                + ", giaNhap=" + giaNhap + ", mucLoiNhuan=" + mucLoiNhuan
                + ", giaBan=" + getGiaBan() + ", trangThai=" + trangThai
                + ", loaiSanPham=" + loaiSanPham + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSanPham);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SanPham)) {
            return false;
        }
        SanPham other = (SanPham) obj;
        return Objects.equals(maSanPham, other.maSanPham);
    }
}

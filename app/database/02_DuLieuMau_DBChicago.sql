/*
    Dữ liệu mẫu cho DBChicago.
    Chạy sau 01_TaoCSDL_DBChicago.sql. Chạy lại được: xóa dữ liệu cũ rồi chèn lại.

    Mật khẩu mẫu của mọi tài khoản là 123456.
    matKhau lưu SHA-256 của chuỗi ASCII "123456":
    8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92

    giaBan = giaNhap + mucLoiNhuan, nên mức lời nhuận dưới đây là số tiền:
      Nước suối  8.000 + 2.000 = 10.000
      Bánh quy   6.400 + 1.600 =  8.000
      Sữa tươi  15.000 + 3.000 = 18.000
      Khăn giấy 12.000 + 1.800 = 13.800

    soLuongTon là tồn kho hiện tại, sau khi các hóa đơn mẫu đã trừ hàng.
    giaBan trên chi tiết hóa đơn là giá tại lúc bán. Trong bộ mẫu này giá chưa đổi.

    HD0711260001, KM giảm 10% (PHAN_TRAM):
      2 chai nước 10.000 + 3 gói bánh 8.000 = 44.000
      giảm 4.400, còn 39.600, VAT 8% = 3.168, phải trả 42.768
      khách đưa 50.000, thối 7.232

    HD0711260002, KM giảm 5.000 (TIEN_MAT):
      1 hộp sữa 18.000, giảm 5.000, còn 13.000, VAT 8% = 1.040, phải trả 14.040
      chuyển khoản đúng số tiền phải trả.

    gioiTinh: Nam hoặc Nữ. Nguyễn Thị Mai = Nữ, Lê Hoàng Phúc = Nam.
    Mọi cột đều có giá trị, không dòng nào để trống.
*/
USE DBChicago;
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET ARITHABORT ON;
SET NUMERIC_ROUNDABORT OFF;

BEGIN TRANSACTION;

DELETE FROM dbo.ChiTietHoaDon;
DELETE FROM dbo.HoaDon;
DELETE FROM dbo.TaiKhoan;
DELETE FROM dbo.ChuongTrinhKhuyenMai;
DELETE FROM dbo.SanPham;
DELETE FROM dbo.KhachHang;
DELETE FROM dbo.NhanVien;
DELETE FROM dbo.LoaiSanPham;

INSERT INTO dbo.LoaiSanPham (maLoaiSanPham, tenLoaiSanPham, moTa, trangThai) VALUES
    ('LSP071126001', N'Đồ uống', N'Nước uống đóng chai, lon, hộp', N'Đang hoạt động'),
    ('LSP071126002', N'Thực phẩm', N'Thực phẩm ăn liền và đồ tươi sống ngắn ngày', N'Đang hoạt động'),
    ('LSP071126003', N'Bánh kẹo', N'Bánh, kẹo, snack', N'Đang hoạt động'),
    ('LSP071126004', N'Đồ dùng cá nhân', N'Đồ dùng vệ sinh và chăm sóc cá nhân', N'Đang hoạt động'),
    ('LSP071126005', N'Hàng theo mùa', N'Loại đã ngừng sử dụng, không chọn khi tạo sản phẩm mới', N'Ngừng hoạt động');

INSERT INTO dbo.SanPham (maSanPham, tenSanPham, donViTinh, soLuongTon, giaNhap, mucLoiNhuan, trangThai, maLoaiSanPham) VALUES
    ('SP071126001', N'Nước suối 500ml', N'Chai', 48, 8000, 2000, N'Đang hoạt động', 'LSP071126001'),
    ('SP071126002', N'Bánh quy bơ', N'Gói', 27, 6400, 1600, N'Đang hoạt động', 'LSP071126003'),
    ('SP071126003', N'Sữa tươi 180ml', N'Hộp', 19, 15000, 3000, N'Đang hoạt động', 'LSP071126002'),
    ('SP071126004', N'Khăn giấy bỏ túi', N'Gói', 40, 12000, 1800, N'Ngừng hoạt động', 'LSP071126004');

INSERT INTO dbo.NhanVien (maNhanVien, ho, ten, soDienThoai, trangThai) VALUES
    ('NV071126001', N'Trần Minh', N'Quân', '0901000001', N'Đang hoạt động'),
    ('NV071126002', N'Lê Thu', N'Hà', '0901000002', N'Đang hoạt động'),
    ('NV071126003', N'Phạm Văn', N'Nam', '0901000003', N'Ngừng hoạt động');

INSERT INTO dbo.TaiKhoan (maNhanVien, matKhau, vaiTro) VALUES
    ('NV071126001', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', N'Quản lý'),
    ('NV071126002', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', N'Nhân viên thu ngân'),
    ('NV071126003', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', N'Nhân viên thu ngân');

INSERT INTO dbo.KhachHang (maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi) VALUES
    ('KH071126001', N'Nguyễn Thị', N'Mai', N'Nữ', '0912000001', N'12 Nguyễn Văn Bảo, Gò Vấp'),
    ('KH071126002', N'Lê Hoàng', N'Phúc', N'Nam', '0912000002', N'45 Quang Trung, Gò Vấp');

INSERT INTO dbo.ChuongTrinhKhuyenMai
    (maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKM, ngayBatDau, ngayKetThuc, trangThai)
VALUES
    ('KM071126001', N'Giảm 10% hóa đơn', N'Giảm trên tổng tiền hàng', N'PHAN_TRAM', 10.00, '2026-11-01', '2026-11-30', N'Đang hoạt động'),
    ('KM071126002', N'Giảm 5.000 đồng', N'Giảm một số tiền cố định trên tổng tiền hàng', N'TIEN_MAT', 5000.00, '2026-11-01', '2026-12-31', N'Đang hoạt động'),
    ('KM071126003', N'Giảm 20% đã ngừng', N'Chương trình đã ngừng hoạt động', N'PHAN_TRAM', 20.00, '2026-10-01', '2026-10-31', N'Ngừng hoạt động');

INSERT INTO dbo.HoaDon
    (maHoaDon, ngayGioLap, phuongThucThanhToan, trangThaiThanhToan, tienKhachDua, thoiGianThanhToan, maKM, maNhanVien, maKhachHang)
VALUES
    ('HD0711260001', '2026-11-07T09:15:00', N'Tiền mặt', N'Đã thanh toán', 50000, '2026-11-07T09:15:00', 'KM071126001', 'NV071126002', 'KH071126001'),
    ('HD0711260002', '2026-11-07T14:40:00', N'Chuyển khoản', N'Đã thanh toán', 14040, '2026-11-07T14:40:00', 'KM071126002', 'NV071126002', 'KH071126002');

INSERT INTO dbo.ChiTietHoaDon (maHoaDon, maSanPham, soLuong, giaBan) VALUES
    ('HD0711260001', 'SP071126001', 2, 10000),
    ('HD0711260001', 'SP071126002', 3, 8000),
    ('HD0711260002', 'SP071126003', 1, 18000);

COMMIT TRANSACTION;
GO

SELECT maSanPham, tenSanPham, giaNhap, mucLoiNhuan, giaBan, soLuongTon, trangThai
FROM dbo.SanPham
ORDER BY maSanPham;

SELECT
    h.maHoaDon,
    SUM(ct.thanhTien) AS tongTienSP,
    CASE km.loaiKhuyenMai
        WHEN N'TIEN_MAT' THEN
            CASE
                WHEN km.giaTriKM < SUM(ct.thanhTien) THEN CONVERT(DECIMAL(18, 0), km.giaTriKM)
                ELSE SUM(ct.thanhTien)
            END
        WHEN N'PHAN_TRAM' THEN
            CASE
                WHEN ROUND(SUM(ct.thanhTien) * km.giaTriKM / 100, 0) < SUM(ct.thanhTien)
                    THEN CONVERT(DECIMAL(18, 0), ROUND(SUM(ct.thanhTien) * km.giaTriKM / 100, 0))
                ELSE SUM(ct.thanhTien)
            END
    END AS tienKhuyenMai
FROM dbo.HoaDon AS h
INNER JOIN dbo.ChiTietHoaDon AS ct ON ct.maHoaDon = h.maHoaDon
INNER JOIN dbo.ChuongTrinhKhuyenMai AS km ON km.maKM = h.maKM
GROUP BY h.maHoaDon, km.loaiKhuyenMai, km.giaTriKM
ORDER BY h.maHoaDon;

SELECT maHoaDon, maSanPham, soLuong, giaBan, thanhTien
FROM dbo.ChiTietHoaDon
ORDER BY maHoaDon, maSanPham;
GO

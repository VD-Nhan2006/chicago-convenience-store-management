/*
    Dữ liệu mẫu cho DBChicago.
    Chạy sau 01_TaoCSDL_DBChicago.sql. Chạy lại được: xóa dữ liệu cũ rồi chèn lại.

    Mật khẩu mẫu của mọi tài khoản là 123456.
    matKhauHash là SHA-256 của chuỗi ASCII "123456":
    8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
    Báo cáo chỉ yêu cầu lưu giá trị băm, không chỉ định thuật toán. SHA-256 ở đây là lựa chọn cho dữ liệu mẫu.

    soLuongTon là tồn kho hiện tại, sau khi các hóa đơn mẫu đã trừ hàng.
    donGiaBan trên chi tiết hóa đơn là giá tại lúc bán. Trong bộ mẫu này giá chưa đổi, nên trùng giaBan hiện tại.

    Hóa đơn HD0711260001 lấy đúng ví dụ trong báo cáo:
    2 chai nước 10.000 và 3 gói bánh 8.000, tổng hàng 44.000, giảm 10% còn 39.600.

    gioiTinh: 0 Nam, 1 Nữ. Nguyễn Thị Mai = 1, Lê Hoàng Phúc = 0.
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

INSERT INTO dbo.LoaiSanPham (maLoai, tenLoai, moTa, trangThai) VALUES
    ('LSP071126001', N'Đồ uống', N'Nước uống đóng chai, lon, hộp', 1),
    ('LSP071126002', N'Thực phẩm', N'Thực phẩm ăn liền và đồ tươi sống ngắn ngày', 1),
    ('LSP071126003', N'Bánh kẹo', N'Bánh, kẹo, snack', 1),
    ('LSP071126004', N'Đồ dùng cá nhân', N'Đồ dùng vệ sinh và chăm sóc cá nhân', 1),
    ('LSP071126005', N'Hàng theo mùa', N'Loại đã ngừng sử dụng, không chọn khi tạo sản phẩm mới', 0);

INSERT INTO dbo.SanPham (maSP, tenSP, maLoai, donViTinh, soLuongTon, giaNhap, mucLoiNhuan, trangThai) VALUES
    ('SP071126001', N'Nước suối 500ml', 'LSP071126001', N'Chai', 48, 8000, 25.00, 1),
    ('SP071126002', N'Bánh quy bơ', 'LSP071126003', N'Gói', 27, 6400, 25.00, 1),
    ('SP071126003', N'Sữa tươi 180ml', 'LSP071126002', N'Hộp', 19, 15000, 20.00, 1),
    ('SP071126004', N'Khăn giấy bỏ túi', 'LSP071126004', N'Gói', 40, 12000, 15.00, 0);

INSERT INTO dbo.NhanVien (maNV, hoTen, soDienThoai, vaiTro, trangThai) VALUES
    ('NV071126001', N'Trần Minh Quân', '0901000001', N'Quản lý', 1),
    ('NV071126002', N'Lê Thu Hà', '0901000002', N'Nhân viên thu ngân', 1),
    ('NV071126003', N'Phạm Văn Nam', '0901000003', N'Nhân viên thu ngân', 0);

INSERT INTO dbo.TaiKhoan (maNV, matKhauHash) VALUES
    ('NV071126001', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
    ('NV071126002', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
    ('NV071126003', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

INSERT INTO dbo.KhachHang (maKH, hoTen, gioiTinh, soDienThoai, diaChi) VALUES
    ('KH071126001', N'Nguyễn Thị Mai', 1, '0912000001', N'12 Nguyễn Văn Bảo, Gò Vấp'),
    ('KH071126002', N'Lê Hoàng Phúc', 0, '0912000002', NULL);

INSERT INTO dbo.ChuongTrinhKhuyenMai
    (maKM, tenChuongTrinh, moTa, loaiKhuyenMai, giaTriKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai)
VALUES
    ('KM071126001', N'Giảm 10% hóa đơn', N'Giảm trên tổng tiền hàng', N'Giảm theo phần trăm', 10.00, '2026-11-01T00:00:00', '2026-11-30T23:59:59', 1),
    ('KM071126002', N'Giảm 5.000 đồng', N'Giảm một số tiền cố định trên tổng tiền hàng', N'Giảm số tiền cố định', 5000.00, '2026-11-01T00:00:00', '2026-12-31T23:59:59', 1),
    ('KM071126003', N'Giảm 20% đã ngừng', N'Chương trình đã ngừng hoạt động', N'Giảm theo phần trăm', 20.00, '2026-10-01T00:00:00', '2026-10-31T23:59:59', 0);

INSERT INTO dbo.HoaDon
    (maHD, maKH, maNV, maKM, ngayGioLap, tongTienHang, tienDuocGiam, phuongThucThanhToan, trangThaiThanhToan, soTienKhachDua, thoiGianThanhToan)
VALUES
    ('HD0711260001', 'KH071126001', 'NV071126002', 'KM071126001', '2026-11-07T09:15:00', 44000, 4400, N'Tiền mặt', N'Đã thanh toán', 50000, '2026-11-07T09:15:00'),
    ('HD0711260002', 'KH071126002', 'NV071126002', NULL, '2026-11-07T14:40:00', 18000, 0, N'Chuyển khoản', N'Đã thanh toán', NULL, '2026-11-07T14:40:00');

INSERT INTO dbo.ChiTietHoaDon (maHD, maSP, soLuongBan, donGiaBan) VALUES
    ('HD0711260001', 'SP071126001', 2, 10000),
    ('HD0711260001', 'SP071126002', 3, 8000),
    ('HD0711260002', 'SP071126003', 1, 18000);

COMMIT TRANSACTION;
GO

SELECT maSP, tenSP, giaNhap, mucLoiNhuan, giaBan, soLuongTon, trangThai
FROM dbo.SanPham
ORDER BY maSP;

SELECT maHD, tongTienHang, tienDuocGiam, tongTienThanhToan, phuongThucThanhToan, soTienKhachDua, tienThoi
FROM dbo.HoaDon
ORDER BY maHD;

SELECT maHD, maSP, soLuongBan, donGiaBan, thanhTien
FROM dbo.ChiTietHoaDon
ORDER BY maHD, maSP;
GO

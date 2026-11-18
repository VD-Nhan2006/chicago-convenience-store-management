/*
    Dữ liệu mẫu cho ngày hôm nay (18/11/2026):
      - 5 Khách hàng (KH181126001 -> KH181126005)
      - 10 Sản phẩm (SP181126001 -> SP181126010)
      - 20 Hóa đơn (HD1811260001 -> HD1811260020) kèm chi tiết hóa đơn
    Đã tính toán chính xác tiền hàng, khuyến mãi, VAT 8%, tiền khách đưa.
    Chạy file này trên cơ sở dữ liệu DBChicago.
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
GO

BEGIN TRANSACTION;

-- 1. Xóa dữ liệu cũ của ngày 18/11/2026 nếu đã tồn tại để có thể chạy lại an toàn
DELETE FROM dbo.ChiTietHoaDon WHERE maHoaDon LIKE 'HD181126%';
DELETE FROM dbo.HoaDon WHERE maHoaDon LIKE 'HD181126%';
DELETE FROM dbo.SanPham WHERE maSanPham LIKE 'SP181126%';
DELETE FROM dbo.KhachHang WHERE maKhachHang LIKE 'KH181126%';

-- 2. Chèn 5 Khách hàng mới
INSERT INTO dbo.KhachHang (maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi) VALUES
    ('KH181126001', N'Nguyễn Văn', N'An', N'Nam', '0918000001', N'15 Nguyễn Oanh, Phường 10, Gò Vấp, TP.HCM'),
    ('KH181126002', N'Trần Thị', N'Bích', N'Nữ', '0918000002', N'88 Phan Văn Trị, Phường 7, Gò Vấp, TP.HCM'),
    ('KH181126003', N'Lê Hoàng', N'Cường', N'Nam', '0918000003', N'120 Quang Trung, Phường 8, Gò Vấp, TP.HCM'),
    ('KH181126004', N'Phạm Thùy', N'Dung', N'Nữ', '0918000004', N'45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM'),
    ('KH181126005', N'Đỗ Minh', N'Em', N'Nam', '0918000005', N'73 Cách Mạng Tháng 8, Phường 5, Quận 3, TP.HCM');

-- 3. Chèn 10 Sản phẩm mới (giaBan là cột tính tự động giaNhap + mucLoiNhuan)
INSERT INTO dbo.SanPham (maSanPham, tenSanPham, donViTinh, soLuongTon, giaNhap, mucLoiNhuan, trangThai, maLoaiSanPham) VALUES
    ('SP181126001', N'Nước ngọt Coca-Cola 320ml', N'Lon', 150, 8500, 2500, N'Đang hoạt động', 'LSP071126001'),
    ('SP181126002', N'Trà xanh không độ 455ml', N'Chai', 120, 9000, 3000, N'Đang hoạt động', 'LSP071126001'),
    ('SP181126003', N'Cà phê sữa Highlands 235ml', N'Lon', 80, 14000, 4000, N'Đang hoạt động', 'LSP071126001'),
    ('SP181126004', N'Mì ly Modern lẩu thái 65g', N'Ly', 200, 9000, 3000, N'Đang hoạt động', 'LSP071126002'),
    ('SP181126005', N'Bánh mì sandwich phô mai 100g', N'Gói', 50, 15000, 5000, N'Đang hoạt động', 'LSP071126002'),
    ('SP181126006', N'Snack khoai tây Lays 56g', N'Gói', 100, 12000, 4000, N'Đang hoạt động', 'LSP071126003'),
    ('SP181126007', N'Bánh Chocopie hộp 2 cái', N'Hộp', 90, 10000, 3000, N'Đang hoạt động', 'LSP071126003'),
    ('SP181126008', N'Kẹo cao su Cool Air hũ 58g', N'Hũ', 60, 22000, 6000, N'Đang hoạt động', 'LSP071126003'),
    ('SP181126009', N'Bàn chải Colgate SlimSoft', N'Cây', 70, 25000, 7000, N'Đang hoạt động', 'LSP071126004'),
    ('SP181126010', N'Nước rửa tay Lifebuoy 50ml', N'Chai', 65, 18000, 5000, N'Đang hoạt động', 'LSP071126004');

-- 4. Chèn 20 Hóa đơn cho ngày 18/11/2026
INSERT INTO dbo.HoaDon
    (maHoaDon, ngayGioLap, phuongThucThanhToan, trangThaiThanhToan, tienKhachDua, thoiGianThanhToan, maKM, maNhanVien, maKhachHang, tienDuocGiam, tongThanhToan)
VALUES
    ('HD1811260001', '2026-11-18T07:15:00', N'Tiền mặt', N'Đã thanh toán', 50000, '2026-11-18T07:15:00', 'KM071126001', 'NV071126001', 'KH181126001', 3400, 33048),
    ('HD1811260002', '2026-11-18T07:45:00', N'Chuyển khoản', N'Đã thanh toán', 41040, '2026-11-18T07:45:00', NULL, 'NV071126002', 'KH181126002', 0, 41040),
    ('HD1811260003', '2026-11-18T08:20:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T08:20:00', 'KM071126002', 'NV071126004', 'KH181126003', 5000, 55080),
    ('HD1811260004', '2026-11-18T09:05:00', N'Chuyển khoản', N'Đã thanh toán', 72360, '2026-11-18T09:05:00', NULL, 'NV071126001', 'KH181126004', 0, 72360),
    ('HD1811260005', '2026-11-18T09:40:00', N'Tiền mặt', N'Đã thanh toán', 50000, '2026-11-18T09:40:00', 'KM071126001', 'NV071126002', 'KH181126005', 4100, 39852),
    ('HD1811260006', '2026-11-18T10:15:00', N'Chuyển khoản', N'Đã thanh toán', 82080, '2026-11-18T10:15:00', NULL, 'NV071126004', 'KH181126001', 0, 82080),
    ('HD1811260007', '2026-11-18T11:00:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T11:00:00', 'KM071126002', 'NV071126001', 'KH181126002', 5000, 78840),
    ('HD1811260008', '2026-11-18T11:35:00', N'Tiền mặt', N'Đã thanh toán', 50000, '2026-11-18T11:35:00', NULL, 'NV071126002', 'KH181126003', 0, 41040),
    ('HD1811260009', '2026-11-18T12:10:00', N'Chuyển khoản', N'Đã thanh toán', 60264, '2026-11-18T12:10:00', 'KM071126001', 'NV071126004', 'KH181126004', 6200, 60264),
    ('HD1811260010', '2026-11-18T13:00:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T13:00:00', NULL, 'NV071126001', 'KH181126005', 0, 60480),
    ('HD1811260011', '2026-11-18T14:15:00', N'Chuyển khoản', N'Đã thanh toán', 89640, '2026-11-18T14:15:00', 'KM071126002', 'NV071126002', 'KH181126001', 5000, 89640),
    ('HD1811260012', '2026-11-18T15:00:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T15:00:00', NULL, 'NV071126004', 'KH181126002', 0, 49680),
    ('HD1811260013', '2026-11-18T15:45:00', N'Chuyển khoản', N'Đã thanh toán', 84564, '2026-11-18T15:45:00', 'KM071126001', 'NV071126001', 'KH181126003', 8700, 84564),
    ('HD1811260014', '2026-11-18T16:30:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T16:30:00', NULL, 'NV071126002', 'KH181126004', 0, 58320),
    ('HD1811260015', '2026-11-18T17:15:00', N'Chuyển khoản', N'Đã thanh toán', 89640, '2026-11-18T17:15:00', 'KM071126002', 'NV071126004', 'KH181126005', 5000, 89640),
    ('HD1811260016', '2026-11-18T18:00:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T18:00:00', NULL, 'NV071126001', 'KH181126001', 0, 70200),
    ('HD1811260017', '2026-11-18T18:50:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T18:50:00', 'KM071126001', 'NV071126002', 'KH181126002', 7200, 69984),
    ('HD1811260018', '2026-11-18T19:35:00', N'Chuyển khoản', N'Đã thanh toán', 83160, '2026-11-18T19:35:00', NULL, 'NV071126004', 'KH181126003', 0, 83160),
    ('HD1811260019', '2026-11-18T20:20:00', N'Tiền mặt', N'Đã thanh toán', 100000, '2026-11-18T20:20:00', 'KM071126002', 'NV071126001', 'KH181126004', 5000, 55080),
    ('HD1811260020', '2026-11-18T21:10:00', N'Chuyển khoản', N'Đã thanh toán', 92880, '2026-11-18T21:10:00', NULL, 'NV071126002', 'KH181126005', 0, 92880);

-- 5. Chèn Chi tiết hóa đơn tương ứng cho 20 hóa đơn
INSERT INTO dbo.ChiTietHoaDon (maHoaDon, maSanPham, soLuong, giaBan) VALUES
    ('HD1811260001', 'SP181126001', 2, 11000),
    ('HD1811260001', 'SP181126004', 1, 12000),
    ('HD1811260002', 'SP181126003', 1, 18000),
    ('HD1811260002', 'SP181126005', 1, 20000),
    ('HD1811260003', 'SP181126002', 2, 12000),
    ('HD1811260003', 'SP181126006', 2, 16000),
    ('HD1811260004', 'SP181126007', 3, 13000),
    ('HD1811260004', 'SP181126008', 1, 28000),
    ('HD1811260005', 'SP181126001', 1, 11000),
    ('HD1811260005', 'SP181126002', 1, 12000),
    ('HD1811260005', 'SP181126003', 1, 18000),
    ('HD1811260006', 'SP181126004', 3, 12000),
    ('HD1811260006', 'SP181126005', 2, 20000),
    ('HD1811260007', 'SP181126009', 1, 32000),
    ('HD1811260007', 'SP181126010', 2, 23000),
    ('HD1811260008', 'SP181126006', 1, 16000),
    ('HD1811260008', 'SP181126001', 2, 11000),
    ('HD1811260009', 'SP181126003', 2, 18000),
    ('HD1811260009', 'SP181126007', 2, 13000),
    ('HD1811260010', 'SP181126005', 1, 20000),
    ('HD1811260010', 'SP181126002', 2, 12000),
    ('HD1811260010', 'SP181126004', 1, 12000),
    ('HD1811260011', 'SP181126008', 2, 28000),
    ('HD1811260011', 'SP181126006', 2, 16000),
    ('HD1811260012', 'SP181126001', 3, 11000),
    ('HD1811260012', 'SP181126007', 1, 13000),
    ('HD1811260013', 'SP181126010', 1, 23000),
    ('HD1811260013', 'SP181126009', 2, 32000),
    ('HD1811260014', 'SP181126002', 1, 12000),
    ('HD1811260014', 'SP181126003', 1, 18000),
    ('HD1811260014', 'SP181126004', 2, 12000),
    ('HD1811260015', 'SP181126005', 2, 20000),
    ('HD1811260015', 'SP181126006', 3, 16000),
    ('HD1811260016', 'SP181126007', 2, 13000),
    ('HD1811260016', 'SP181126008', 1, 28000),
    ('HD1811260016', 'SP181126001', 1, 11000),
    ('HD1811260017', 'SP181126004', 4, 12000),
    ('HD1811260017', 'SP181126002', 2, 12000),
    ('HD1811260018', 'SP181126009', 1, 32000),
    ('HD1811260018', 'SP181126001', 2, 11000),
    ('HD1811260018', 'SP181126010', 1, 23000),
    ('HD1811260019', 'SP181126003', 2, 18000),
    ('HD1811260019', 'SP181126005', 1, 20000),
    ('HD1811260020', 'SP181126006', 2, 16000),
    ('HD1811260020', 'SP181126007', 2, 13000),
    ('HD1811260020', 'SP181126008', 1, 28000);

COMMIT TRANSACTION;
GO

-- 6. Kiểm tra lại dữ liệu vừa chèn
SELECT N'Khách hàng vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong FROM dbo.KhachHang WHERE maKhachHang LIKE 'KH181126%';
SELECT N'Sản phẩm vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong FROM dbo.SanPham WHERE maSanPham LIKE 'SP181126%';
SELECT N'Hóa đơn vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong, SUM(tongThanhToan) AS TongDoanhThuNgay FROM dbo.HoaDon WHERE maHoaDon LIKE 'HD181126%';
GO
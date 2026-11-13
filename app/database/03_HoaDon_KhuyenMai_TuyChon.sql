-- Áp dụng cho DBChicago đã tạo từ schema trước đây. Giữ nguyên dữ liệu hiện có.
USE DBChicago;
GO

ALTER TABLE dbo.HoaDon ALTER COLUMN maKM VARCHAR(11) NULL;
GO

IF COL_LENGTH('dbo.HoaDon', 'tienDuocGiam') IS NULL
    ALTER TABLE dbo.HoaDon ADD tienDuocGiam DECIMAL(18, 0) NULL;
GO

IF COL_LENGTH('dbo.HoaDon', 'tongThanhToan') IS NULL
    ALTER TABLE dbo.HoaDon ADD tongThanhToan DECIMAL(18, 0) NULL;
GO

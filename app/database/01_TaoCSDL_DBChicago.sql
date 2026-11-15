/*
    Tạo database DBChicago cho Hệ thống quản lý cửa hàng tiện lợi Chicago.
    Căn cứ các lớp trong app/src/entity.

    Script xóa DBChicago nếu đã có, rồi tạo lại.
    Chạy file này trước 02_DuLieuMau_DBChicago.sql.

    Mã khuyến mãi trên hóa đơn có thể để trống khi không áp dụng.
    Tài khoản có trạng thái: Đang hoạt động hoặc Ngừng hoạt động.

    Cột suy ra từ cột khác trong cùng dòng, khớp getter của entity:
      SanPham.giaBan           = giaNhap + mucLoiNhuan
      ChiTietHoaDon.thanhTien  = soLuong * giaBan

    Hóa đơn mới lưu tiền giảm và tổng thanh toán tại thời điểm giao dịch.
    mucLoiNhuan là số tiền cộng thêm vào giá nhập, không phải phần trăm.
    giaTriKM kiểu PHAN_TRAM lưu theo thang điểm: 10 nghĩa là 10%.

    loaiKhuyenMai: TIEN_MAT hoặc PHAN_TRAM, đúng chuỗi HoaDon.tinhTienKhuyenMai so sánh.

    Mã:
      LoaiSanPham          LSPddMMyyXXX, XXX từ 001 đến 999
      SanPham, KhachHang, NhanVien, ChuongTrinhKhuyenMai
                           XXddMMyyXXX, XXX từ 001 đến 999
      HoaDon               HDddMMyyXXXX, XXXX từ 0001 đến 9999
*/
USE master;
GO

IF DB_ID(N'DBChicago') IS NOT NULL
BEGIN
    ALTER DATABASE DBChicago SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE DBChicago;
END
GO

CREATE DATABASE DBChicago;
GO

USE DBChicago;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET ARITHABORT ON;
SET NUMERIC_ROUNDABORT OFF;
GO

CREATE TABLE dbo.LoaiSanPham (
    maLoaiSanPham  VARCHAR(12)   NOT NULL,
    tenLoaiSanPham NVARCHAR(100) NOT NULL,
    moTa           NVARCHAR(500) NOT NULL,
    trangThai      NVARCHAR(30)  NOT NULL,
    CONSTRAINT PK_LoaiSanPham PRIMARY KEY (maLoaiSanPham),
    CONSTRAINT CK_LoaiSanPham_MaLoaiSanPham CHECK (
        LEN(maLoaiSanPham) = 12
        AND maLoaiSanPham LIKE 'LSP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maLoaiSanPham, 4, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maLoaiSanPham, 6, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maLoaiSanPham, 10, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_LoaiSanPham_TenLoaiSanPham CHECK (LEN(LTRIM(RTRIM(tenLoaiSanPham))) > 0),
    CONSTRAINT CK_LoaiSanPham_MoTa CHECK (LEN(LTRIM(RTRIM(moTa))) > 0),
    CONSTRAINT CK_LoaiSanPham_TrangThai CHECK (
        trangThai IN (N'Đang hoạt động', N'Ngừng hoạt động')
    )
);
GO

CREATE TABLE dbo.SanPham (
    maSanPham      VARCHAR(11)    NOT NULL,
    tenSanPham     NVARCHAR(150)  NOT NULL,
    donViTinh      NVARCHAR(30)   NOT NULL,
    soLuongTon     INT            NOT NULL,
    giaNhap        DECIMAL(18, 0) NOT NULL,
    mucLoiNhuan    DECIMAL(18, 0) NOT NULL,
    giaBan         AS CONVERT(DECIMAL(18, 0), giaNhap + mucLoiNhuan) PERSISTED,
    trangThai      NVARCHAR(30)   NOT NULL,
    maLoaiSanPham  VARCHAR(12)    NOT NULL,
    CONSTRAINT PK_SanPham PRIMARY KEY (maSanPham),
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY (maLoaiSanPham) REFERENCES dbo.LoaiSanPham (maLoaiSanPham),
    CONSTRAINT CK_SanPham_MaSanPham CHECK (
        LEN(maSanPham) = 11
        AND maSanPham LIKE 'SP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maSanPham, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maSanPham, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maSanPham, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_SanPham_TenSanPham CHECK (LEN(LTRIM(RTRIM(tenSanPham))) > 0),
    CONSTRAINT CK_SanPham_DonViTinh CHECK (LEN(LTRIM(RTRIM(donViTinh))) > 0),
    CONSTRAINT CK_SanPham_SoLuongTon CHECK (soLuongTon >= 0),
    CONSTRAINT CK_SanPham_GiaNhap CHECK (giaNhap >= 0),
    CONSTRAINT CK_SanPham_MucLoiNhuan CHECK (mucLoiNhuan >= 0),
    CONSTRAINT CK_SanPham_GiaBan CHECK (giaBan >= 0),
    CONSTRAINT CK_SanPham_TrangThai CHECK (
        trangThai IN (N'Đang hoạt động', N'Ngừng hoạt động')
    )
);
GO

CREATE TABLE dbo.KhachHang (
    maKhachHang  VARCHAR(11)   NOT NULL,
    ho           NVARCHAR(50)  NOT NULL,
    ten          NVARCHAR(50)  NOT NULL,
    gioiTinh     NVARCHAR(10)  NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL,
    diaChi       NVARCHAR(255) NOT NULL,
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKhachHang),
    CONSTRAINT CK_KhachHang_MaKhachHang CHECK (
        LEN(maKhachHang) = 11
        AND maKhachHang LIKE 'KH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maKhachHang, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maKhachHang, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maKhachHang, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_KhachHang_Ho CHECK (LEN(LTRIM(RTRIM(ho))) > 0),
    CONSTRAINT CK_KhachHang_Ten CHECK (LEN(LTRIM(RTRIM(ten))) > 0),
    CONSTRAINT CK_KhachHang_GioiTinh CHECK (gioiTinh IN (N'Nam', N'Nữ')),
    CONSTRAINT CK_KhachHang_SoDienThoai CHECK (
        soDienThoai LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
    ),
    CONSTRAINT CK_KhachHang_DiaChi CHECK (LEN(LTRIM(RTRIM(diaChi))) > 0)
);
GO

CREATE TABLE dbo.NhanVien (
    maNhanVien   VARCHAR(11)   NOT NULL,
    ho           NVARCHAR(50)  NOT NULL,
    ten          NVARCHAR(50)  NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL,
    diaChi       NVARCHAR(255) NOT NULL,
    trangThai    NVARCHAR(30)  NOT NULL,
    CONSTRAINT PK_NhanVien PRIMARY KEY (maNhanVien),
    CONSTRAINT CK_NhanVien_MaNhanVien CHECK (
        LEN(maNhanVien) = 11
        AND maNhanVien LIKE 'NV[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maNhanVien, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maNhanVien, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maNhanVien, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_NhanVien_Ho CHECK (LEN(LTRIM(RTRIM(ho))) > 0),
    CONSTRAINT CK_NhanVien_Ten CHECK (LEN(LTRIM(RTRIM(ten))) > 0),
    CONSTRAINT CK_NhanVien_SoDienThoai CHECK (
        soDienThoai LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
    ),
    CONSTRAINT CK_NhanVien_DiaChi CHECK (LEN(LTRIM(RTRIM(diaChi))) > 0),
    CONSTRAINT CK_NhanVien_TrangThai CHECK (
        trangThai IN (N'Đang hoạt động', N'Ngừng hoạt động')
    )
);
GO

CREATE TABLE dbo.TaiKhoan (
    maNhanVien  VARCHAR(11)   NOT NULL,
    matKhau     NVARCHAR(255) NOT NULL,
    vaiTro      NVARCHAR(30)  NOT NULL,
    trangThai   NVARCHAR(30)  NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (maNhanVien),
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNhanVien) REFERENCES dbo.NhanVien (maNhanVien) ON DELETE CASCADE,
    CONSTRAINT CK_TaiKhoan_MatKhau CHECK (LEN(LTRIM(RTRIM(matKhau))) > 0),
    CONSTRAINT CK_TaiKhoan_VaiTro CHECK (vaiTro IN (N'Nhân viên thu ngân', N'Quản lý')),
    CONSTRAINT CK_TaiKhoan_TrangThai CHECK (
        trangThai IN (N'Đang hoạt động', N'Ngừng hoạt động')
    )
);
GO

CREATE TABLE dbo.ChuongTrinhKhuyenMai (
    maKM             VARCHAR(11)    NOT NULL,
    tenChuongTrinh   NVARCHAR(150)  NOT NULL,
    moTa             NVARCHAR(500)  NOT NULL,
    loaiKhuyenMai    NVARCHAR(20)   NOT NULL,
    giaTriKM         DECIMAL(18, 2) NOT NULL,
    ngayBatDau       DATE           NOT NULL,
    ngayKetThuc      DATE           NOT NULL,
    trangThai        NVARCHAR(30)   NOT NULL,
    CONSTRAINT PK_ChuongTrinhKhuyenMai PRIMARY KEY (maKM),
    CONSTRAINT CK_KhuyenMai_MaKM CHECK (
        LEN(maKM) = 11
        AND maKM LIKE 'KM[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maKM, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maKM, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maKM, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_KhuyenMai_TenChuongTrinh CHECK (LEN(LTRIM(RTRIM(tenChuongTrinh))) > 0),
    CONSTRAINT CK_KhuyenMai_MoTa CHECK (LEN(LTRIM(RTRIM(moTa))) > 0),
    CONSTRAINT CK_KhuyenMai_Loai CHECK (loaiKhuyenMai IN (N'TIEN_MAT', N'PHAN_TRAM')),
    CONSTRAINT CK_KhuyenMai_GiaTri CHECK (
        (
            loaiKhuyenMai = N'PHAN_TRAM'
            AND giaTriKM > 0
            AND giaTriKM <= 100
        )
        OR
        (
            loaiKhuyenMai = N'TIEN_MAT'
            AND giaTriKM > 0
        )
    ),
    CONSTRAINT CK_KhuyenMai_Ngay CHECK (ngayKetThuc >= ngayBatDau),
    CONSTRAINT CK_KhuyenMai_TrangThai CHECK (
        trangThai IN (N'Đang hoạt động', N'Ngừng hoạt động')
    )
);
GO

CREATE TABLE dbo.HoaDon (
    maHoaDon              VARCHAR(12)    NOT NULL,
    ngayGioLap            DATETIME2(0)   NOT NULL,
    phuongThucThanhToan   NVARCHAR(30)   NOT NULL,
    trangThaiThanhToan    NVARCHAR(30)   NOT NULL,
    tienKhachDua          DECIMAL(18, 0) NOT NULL,
    thoiGianThanhToan     DATETIME2(0)   NOT NULL,
    maKM                  VARCHAR(11)    NULL,
    maNhanVien            VARCHAR(11)    NOT NULL,
    maKhachHang           VARCHAR(11)    NOT NULL,
    tienDuocGiam          DECIMAL(18, 0) NULL,
    tongThanhToan         DECIMAL(18, 0) NULL,
    CONSTRAINT PK_HoaDon PRIMARY KEY (maHoaDon),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (maKM) REFERENCES dbo.ChuongTrinhKhuyenMai (maKM),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien) REFERENCES dbo.NhanVien (maNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang) REFERENCES dbo.KhachHang (maKhachHang),
    CONSTRAINT CK_HoaDon_MaHoaDon CHECK (
        LEN(maHoaDon) = 12
        AND maHoaDon LIKE 'HD[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maHoaDon, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maHoaDon, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maHoaDon, 9, 4)) BETWEEN 1 AND 9999
    ),
    CONSTRAINT CK_HoaDon_PhuongThuc CHECK (
        phuongThucThanhToan IN (N'Tiền mặt', N'Chuyển khoản')
    ),
    CONSTRAINT CK_HoaDon_TrangThai CHECK (
        trangThaiThanhToan IN (N'Đã thanh toán')
    ),
    CONSTRAINT CK_HoaDon_TienKhachDua CHECK (tienKhachDua >= 0),
    CONSTRAINT CK_HoaDon_ThoiGianThanhToan CHECK (thoiGianThanhToan >= ngayGioLap)
);
GO

CREATE TABLE dbo.ChiTietHoaDon (
    maHoaDon   VARCHAR(12)    NOT NULL,
    maSanPham  VARCHAR(11)    NOT NULL,
    soLuong    INT            NOT NULL,
    giaBan     DECIMAL(18, 0) NOT NULL,
    thanhTien  AS CONVERT(DECIMAL(18, 0), soLuong * giaBan) PERSISTED,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maHoaDon, maSanPham),
    CONSTRAINT FK_ChiTietHoaDon_HoaDon FOREIGN KEY (maHoaDon) REFERENCES dbo.HoaDon (maHoaDon) ON DELETE CASCADE,
    CONSTRAINT FK_ChiTietHoaDon_SanPham FOREIGN KEY (maSanPham) REFERENCES dbo.SanPham (maSanPham),
    CONSTRAINT CK_ChiTietHoaDon_SoLuong CHECK (soLuong > 0),
    CONSTRAINT CK_ChiTietHoaDon_GiaBan CHECK (giaBan >= 0)
);
GO

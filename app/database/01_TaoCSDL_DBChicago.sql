/*
    Tạo database DBChicago cho Hệ thống quản lý cửa hàng tiện lợi Chicago.
    Căn cứ: doc/BaoCaoDeTai.docx và sơ đồ lớp.

    Script xóa DBChicago nếu đã có, rồi tạo lại.
    Chạy file này trước 02_DuLieuMau_DBChicago.sql.

    Cột suy ra từ cột khác trong cùng dòng, nên để cột tính thay vì nhập tay:
      SanPham.giaBan              = ROUND(giaNhap * (100 + mucLoiNhuan) / 100, 0)
      ChiTietHoaDon.thanhTien     = soLuongBan * donGiaBan
      HoaDon.tongTienThanhToan    = tongTienHang - tienDuocGiam
      HoaDon.tienThoi             = soTienKhachDua - tongTienThanhToan khi trả tiền mặt, NULL khi chuyển khoản

    mucLoiNhuan và giá trị khuyến mãi kiểu phần trăm lưu theo thang điểm:
    20 nghĩa là 20%, không phải 0.20.

    Mã hóa đơn đã chốt bốn chữ số: HDddMMyy + 0001..9999.

    BIT trangThai:
      LoaiSanPham, ChuongTrinhKhuyenMai : 1 đang hoạt động, 0 ngừng
      SanPham                           : 1 đang kinh doanh, 0 ngừng kinh doanh
      NhanVien                          : 1 đang làm việc, 0 ngừng làm việc
    BIT gioiTinh của khách hàng: 0 Nam, 1 Nữ.
    Số điện thoại khách hàng và nhân viên: đúng 10 chữ số.
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
    maLoai    VARCHAR(12)   NOT NULL,
    tenLoai   NVARCHAR(100) NOT NULL,
    moTa      NVARCHAR(500) NULL,
    trangThai BIT           NOT NULL,
    CONSTRAINT PK_LoaiSanPham PRIMARY KEY (maLoai),
    -- LSPddMMyyXXX, XXX từ 001 đến 999
    CONSTRAINT CK_LoaiSanPham_MaLoai CHECK (
        LEN(maLoai) = 12
        AND maLoai LIKE 'LSP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maLoai, 4, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maLoai, 6, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maLoai, 10, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_LoaiSanPham_TenLoai CHECK (LEN(LTRIM(RTRIM(tenLoai))) > 0)
);
GO

CREATE TABLE dbo.SanPham (
    maSP         VARCHAR(11)    NOT NULL,
    tenSP        NVARCHAR(150)  NOT NULL,
    maLoai       VARCHAR(12)    NOT NULL,
    donViTinh    NVARCHAR(30)   NOT NULL,
    soLuongTon   INT            NOT NULL,
    giaNhap      DECIMAL(18, 0) NOT NULL,
    mucLoiNhuan  DECIMAL(6, 2)  NOT NULL,
    giaBan       AS CONVERT(DECIMAL(18, 0), ROUND(giaNhap * (CONVERT(DECIMAL(18, 2), 100) + mucLoiNhuan) / CONVERT(DECIMAL(18, 2), 100), 0)) PERSISTED,
    trangThai    BIT            NOT NULL,
    CONSTRAINT PK_SanPham PRIMARY KEY (maSP),
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY (maLoai) REFERENCES dbo.LoaiSanPham (maLoai),
    -- SPddMMyyXXX, XXX từ 001 đến 999
    CONSTRAINT CK_SanPham_MaSP CHECK (
        LEN(maSP) = 11
        AND maSP LIKE 'SP[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maSP, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maSP, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maSP, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_SanPham_TenSP CHECK (LEN(LTRIM(RTRIM(tenSP))) > 0),
    CONSTRAINT CK_SanPham_DonViTinh CHECK (LEN(LTRIM(RTRIM(donViTinh))) > 0),
    CONSTRAINT CK_SanPham_SoLuongTon CHECK (soLuongTon >= 0),
    CONSTRAINT CK_SanPham_GiaNhap CHECK (giaNhap >= 0),
    CONSTRAINT CK_SanPham_MucLoiNhuan CHECK (mucLoiNhuan >= 0),
    CONSTRAINT CK_SanPham_GiaBan CHECK (giaBan >= 0)
);
GO

CREATE TABLE dbo.KhachHang (
    maKH         VARCHAR(11)   NOT NULL,
    hoTen        NVARCHAR(100) NOT NULL,
    gioiTinh     BIT           NOT NULL, -- 0: Nam, 1: Nữ
    soDienThoai  VARCHAR(10)   NOT NULL,
    diaChi       NVARCHAR(255) NULL,
    CONSTRAINT PK_KhachHang PRIMARY KEY (maKH),
    -- KHddMMyyXXX, XXX từ 001 đến 999
    CONSTRAINT CK_KhachHang_MaKH CHECK (
        LEN(maKH) = 11
        AND maKH LIKE 'KH[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maKH, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maKH, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maKH, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_KhachHang_HoTen CHECK (LEN(LTRIM(RTRIM(hoTen))) > 0),
    CONSTRAINT CK_KhachHang_SoDienThoai CHECK (
        soDienThoai LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
    )
);
GO

CREATE TABLE dbo.NhanVien (
    maNV         VARCHAR(11)   NOT NULL,
    hoTen        NVARCHAR(100) NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL,
    vaiTro       NVARCHAR(30)  NOT NULL,
    trangThai    BIT           NOT NULL,
    CONSTRAINT PK_NhanVien PRIMARY KEY (maNV),
    -- NVddMMyyXXX, XXX từ 001 đến 999
    CONSTRAINT CK_NhanVien_MaNV CHECK (
        LEN(maNV) = 11
        AND maNV LIKE 'NV[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maNV, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maNV, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maNV, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_NhanVien_HoTen CHECK (LEN(LTRIM(RTRIM(hoTen))) > 0),
    CONSTRAINT CK_NhanVien_SoDienThoai CHECK (
        soDienThoai LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
    ),
    CONSTRAINT CK_NhanVien_VaiTro CHECK (vaiTro IN (N'Nhân viên thu ngân', N'Quản lý'))
);
GO

CREATE TABLE dbo.TaiKhoan (
    maNV         VARCHAR(11)   NOT NULL,
    matKhauHash  NVARCHAR(255) NOT NULL,
    CONSTRAINT PK_TaiKhoan PRIMARY KEY (maNV),
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES dbo.NhanVien (maNV) ON DELETE CASCADE,
    CONSTRAINT CK_TaiKhoan_MatKhauHash CHECK (LEN(LTRIM(RTRIM(matKhauHash))) > 0)
);
GO

CREATE TABLE dbo.ChuongTrinhKhuyenMai (
    maKM             VARCHAR(11)    NOT NULL,
    tenChuongTrinh   NVARCHAR(150)  NOT NULL,
    moTa             NVARCHAR(500)  NULL,
    loaiKhuyenMai    NVARCHAR(50)   NOT NULL,
    giaTriKhuyenMai  DECIMAL(18, 2) NOT NULL,
    thoiGianBatDau   DATETIME2(0)   NOT NULL,
    thoiGianKetThuc  DATETIME2(0)   NOT NULL,
    trangThai        BIT            NOT NULL,
    CONSTRAINT PK_ChuongTrinhKhuyenMai PRIMARY KEY (maKM),
    -- KMddMMyyXXX, XXX từ 001 đến 999
    CONSTRAINT CK_KhuyenMai_MaKM CHECK (
        LEN(maKM) = 11
        AND maKM LIKE 'KM[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maKM, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maKM, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maKM, 9, 3)) BETWEEN 1 AND 999
    ),
    CONSTRAINT CK_KhuyenMai_TenChuongTrinh CHECK (LEN(LTRIM(RTRIM(tenChuongTrinh))) > 0),
    CONSTRAINT CK_KhuyenMai_Loai CHECK (
        loaiKhuyenMai IN (N'Giảm theo phần trăm', N'Giảm số tiền cố định')
    ),
    -- Phần trăm lấy trong (0; 100]. Số tiền cố định phải dương. Trần 100% là cách đọc "hợp lệ", cần bạn xác nhận.
    CONSTRAINT CK_KhuyenMai_GiaTri CHECK (
        (
            loaiKhuyenMai = N'Giảm theo phần trăm'
            AND giaTriKhuyenMai > 0
            AND giaTriKhuyenMai <= 100
        )
        OR
        (
            loaiKhuyenMai = N'Giảm số tiền cố định'
            AND giaTriKhuyenMai > 0
        )
    ),
    CONSTRAINT CK_KhuyenMai_ThoiGian CHECK (thoiGianKetThuc >= thoiGianBatDau)
);
GO

CREATE TABLE dbo.HoaDon (
    maHD                  VARCHAR(12)    NOT NULL,
    maKH                  VARCHAR(11)    NOT NULL,
    maNV                  VARCHAR(11)    NOT NULL,
    maKM                  VARCHAR(11)    NULL,
    ngayGioLap            DATETIME2(0)   NOT NULL,
    tongTienHang          DECIMAL(18, 0) NOT NULL,
    tienDuocGiam          DECIMAL(18, 0) NOT NULL,
    tongTienThanhToan     AS CONVERT(DECIMAL(18, 0), tongTienHang - tienDuocGiam) PERSISTED,
    phuongThucThanhToan   NVARCHAR(30)   NOT NULL,
    trangThaiThanhToan    NVARCHAR(30)   NOT NULL,
    soTienKhachDua        DECIMAL(18, 0) NULL,
    tienThoi              AS (
                                CASE
                                    WHEN phuongThucThanhToan = N'Tiền mặt'
                                        THEN CONVERT(DECIMAL(18, 0), soTienKhachDua - (tongTienHang - tienDuocGiam))
                                    ELSE CONVERT(DECIMAL(18, 0), NULL)
                                END
                             ) PERSISTED,
    thoiGianThanhToan     DATETIME2(0)   NOT NULL,
    CONSTRAINT PK_HoaDon PRIMARY KEY (maHD),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH) REFERENCES dbo.KhachHang (maKH),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNV) REFERENCES dbo.NhanVien (maNV),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (maKM) REFERENCES dbo.ChuongTrinhKhuyenMai (maKM),
    -- HDddMMyyXXXX, XXXX từ 0001 đến 9999
    CONSTRAINT CK_HoaDon_MaHD CHECK (
        LEN(maHD) = 12
        AND maHD LIKE 'HD[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]' COLLATE Latin1_General_BIN2
        AND SUBSTRING(maHD, 3, 2) BETWEEN '01' AND '31'
        AND SUBSTRING(maHD, 5, 2) BETWEEN '01' AND '12'
        AND CONVERT(INT, SUBSTRING(maHD, 9, 4)) BETWEEN 1 AND 9999
    ),
    CONSTRAINT CK_HoaDon_TongTienHang CHECK (tongTienHang >= 0),
    CONSTRAINT CK_HoaDon_TienDuocGiam CHECK (
        tienDuocGiam >= 0
        AND tienDuocGiam <= tongTienHang
    ),
    -- Không chọn khuyến mãi thì tiền giảm bằng 0
    CONSTRAINT CK_HoaDon_KhuyenMai CHECK (
        (maKM IS NULL AND tienDuocGiam = 0)
        OR (maKM IS NOT NULL)
    ),
    CONSTRAINT CK_HoaDon_PhuongThuc CHECK (
        phuongThucThanhToan IN (N'Tiền mặt', N'Chuyển khoản')
    ),
    CONSTRAINT CK_HoaDon_TrangThai CHECK (
        trangThaiThanhToan = N'Đã thanh toán'
    ),
    CONSTRAINT CK_HoaDon_TienKhachDua CHECK (
        (
            phuongThucThanhToan = N'Tiền mặt'
            AND soTienKhachDua IS NOT NULL
            AND soTienKhachDua >= (tongTienHang - tienDuocGiam)
        )
        OR
        (
            phuongThucThanhToan = N'Chuyển khoản'
            AND soTienKhachDua IS NULL
        )
    ),
    CONSTRAINT CK_HoaDon_ThoiGianThanhToan CHECK (thoiGianThanhToan >= ngayGioLap)
);
GO

CREATE TABLE dbo.ChiTietHoaDon (
    maHD        VARCHAR(12)    NOT NULL,
    maSP        VARCHAR(11)    NOT NULL,
    soLuongBan  INT            NOT NULL,
    donGiaBan   DECIMAL(18, 0) NOT NULL,
    thanhTien   AS CONVERT(DECIMAL(18, 0), soLuongBan * donGiaBan) PERSISTED,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maHD, maSP),
    CONSTRAINT FK_ChiTietHoaDon_HoaDon FOREIGN KEY (maHD) REFERENCES dbo.HoaDon (maHD) ON DELETE CASCADE,
    CONSTRAINT FK_ChiTietHoaDon_SanPham FOREIGN KEY (maSP) REFERENCES dbo.SanPham (maSP),
    CONSTRAINT CK_ChiTietHoaDon_SoLuongBan CHECK (soLuongBan > 0),
    CONSTRAINT CK_ChiTietHoaDon_DonGiaBan CHECK (donGiaBan >= 0)
);
GO

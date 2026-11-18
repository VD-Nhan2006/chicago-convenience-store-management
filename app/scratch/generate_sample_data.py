import os
from decimal import Decimal, ROUND_HALF_UP

def round_half_up(val):
    return int(Decimal(str(val)).quantize(Decimal("1"), rounding=ROUND_HALF_UP))

# 5 Customers for today 18/11/2026
customers = [
    ("KH181126001", "Nguyễn Văn", "An", "Nam", "0918000001", "15 Nguyễn Oanh, Phường 10, Gò Vấp, TP.HCM"),
    ("KH181126002", "Trần Thị", "Bích", "Nữ", "0918000002", "88 Phan Văn Trị, Phường 7, Gò Vấp, TP.HCM"),
    ("KH181126003", "Lê Hoàng", "Cường", "Nam", "0918000003", "120 Quang Trung, Phường 8, Gò Vấp, TP.HCM"),
    ("KH181126004", "Phạm Thùy", "Dung", "Nữ", "0918000004", "45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM"),
    ("KH181126005", "Đỗ Minh", "Em", "Nam", "0918000005", "73 Cách Mạng Tháng 8, Phường 5, Quận 3, TP.HCM")
]

# 10 Products for today 18/11/2026
# maSP, tenSP, DVT, soLuongTon, giaNhap, mucLoiNhuan, maLoaiSanPham
products = [
    ("SP181126001", "Nước ngọt Coca-Cola 320ml", "Lon", 150, 8500, 2500, "LSP071126001"),       # giaBan: 11000
    ("SP181126002", "Trà xanh không độ 455ml", "Chai", 120, 9000, 3000, "LSP071126001"),       # giaBan: 12000
    ("SP181126003", "Cà phê sữa Highlands 235ml", "Lon", 80, 14000, 4000, "LSP071126001"),     # giaBan: 18000
    ("SP181126004", "Mì ly Modern lẩu thái 65g", "Ly", 200, 9000, 3000, "LSP071126002"),       # giaBan: 12000
    ("SP181126005", "Bánh mì sandwich phô mai 100g", "Gói", 50, 15000, 5000, "LSP071126002"),  # giaBan: 20000
    ("SP181126006", "Snack khoai tây Lays 56g", "Gói", 100, 12000, 4000, "LSP071126003"),      # giaBan: 16000
    ("SP181126007", "Bánh Chocopie hộp 2 cái", "Hộp", 90, 10000, 3000, "LSP071126003"),        # giaBan: 13000
    ("SP181126008", "Kẹo cao su Cool Air hũ 58g", "Hũ", 60, 22000, 6000, "LSP071126003"),      # giaBan: 28000
    ("SP181126009", "Bàn chải Colgate SlimSoft", "Cây", 70, 25000, 7000, "LSP071126004"),      # giaBan: 32000
    ("SP181126010", "Nước rửa tay Lifebuoy 50ml", "Chai", 65, 18000, 5000, "LSP071126004")     # giaBan: 23000
]

prod_dict = {p[0]: {"ten": p[1], "giaBan": p[4] + p[5]} for p in products}

# Available Staff in DB
staff = ["NV071126001", "NV071126002", "NV071126004"]

# Invoices definition for today: 20 invoices across the day
invoice_items_def = [
    [("SP181126001", 2), ("SP181126004", 1)],
    [("SP181126003", 1), ("SP181126005", 1)],
    [("SP181126002", 2), ("SP181126006", 2)],
    [("SP181126007", 3), ("SP181126008", 1)],
    [("SP181126001", 1), ("SP181126002", 1), ("SP181126003", 1)],
    [("SP181126004", 3), ("SP181126005", 2)],
    [("SP181126009", 1), ("SP181126010", 2)],
    [("SP181126006", 1), ("SP181126001", 2)],
    [("SP181126003", 2), ("SP181126007", 2)],
    [("SP181126005", 1), ("SP181126002", 2), ("SP181126004", 1)],
    [("SP181126008", 2), ("SP181126006", 2)],
    [("SP181126001", 3), ("SP181126007", 1)],
    [("SP181126010", 1), ("SP181126009", 2)],
    [("SP181126002", 1), ("SP181126003", 1), ("SP181126004", 2)],
    [("SP181126005", 2), ("SP181126006", 3)],
    [("SP181126007", 2), ("SP181126008", 1), ("SP181126001", 1)],
    [("SP181126004", 4), ("SP181126002", 2)],
    [("SP181126009", 1), ("SP181126001", 2), ("SP181126010", 1)],
    [("SP181126003", 2), ("SP181126005", 1)],
    [("SP181126006", 2), ("SP181126007", 2), ("SP181126008", 1)]
]

times = [
    "07:15:00", "07:45:00", "08:20:00", "09:05:00", "09:40:00",
    "10:15:00", "11:00:00", "11:35:00", "12:10:00", "13:00:00",
    "14:15:00", "15:00:00", "15:45:00", "16:30:00", "17:15:00",
    "18:00:00", "18:50:00", "19:35:00", "20:20:00", "21:10:00"
]

methods = [
    "Tiền mặt", "Chuyển khoản", "Tiền mặt", "Chuyển khoản", "Tiền mặt",
    "Chuyển khoản", "Tiền mặt", "Tiền mặt", "Chuyển khoản", "Tiền mặt",
    "Chuyển khoản", "Tiền mặt", "Chuyển khoản", "Tiền mặt", "Chuyển khoản",
    "Tiền mặt", "Tiền mặt", "Chuyển khoản", "Tiền mặt", "Chuyển khoản"
]

promos = [
    "KM071126001", # 10%
    None,
    "KM071126002", # 5000d
    None,
    "KM071126001", # 10%
    None,
    "KM071126002", # 5000d
    None,
    "KM071126001", # 10%
    None,
    "KM071126002", # 5000d
    None,
    "KM071126001", # 10%
    None,
    "KM071126002", # 5000d
    None,
    "KM071126001", # 10%
    None,
    "KM071126002", # 5000d
    None
]

lines = []
lines.append("/*")
lines.append("    Dữ liệu mẫu cho ngày hôm nay (18/11/2026):")
lines.append("      - 5 Khách hàng (KH181126001 -> KH181126005)")
lines.append("      - 10 Sản phẩm (SP181126001 -> SP181126010)")
lines.append("      - 20 Hóa đơn (HD1811260001 -> HD1811260020) kèm chi tiết hóa đơn")
lines.append("    Đã tính toán chính xác tiền hàng, khuyến mãi, VAT 8%, tiền khách đưa.")
lines.append("    Chạy file này trên cơ sở dữ liệu DBChicago.")
lines.append("*/")
lines.append("USE DBChicago;")
lines.append("GO")
lines.append("")
lines.append("SET NOCOUNT ON;")
lines.append("SET XACT_ABORT ON;")
lines.append("SET ANSI_NULLS ON;")
lines.append("SET QUOTED_IDENTIFIER ON;")
lines.append("SET ANSI_PADDING ON;")
lines.append("SET ANSI_WARNINGS ON;")
lines.append("SET CONCAT_NULL_YIELDS_NULL ON;")
lines.append("SET ARITHABORT ON;")
lines.append("SET NUMERIC_ROUNDABORT OFF;")
lines.append("GO")
lines.append("")
lines.append("BEGIN TRANSACTION;")
lines.append("")
lines.append("-- 1. Xóa dữ liệu cũ của ngày 18/11/2026 nếu đã tồn tại để có thể chạy lại an toàn")
lines.append("DELETE FROM dbo.ChiTietHoaDon WHERE maHoaDon LIKE 'HD181126%';")
lines.append("DELETE FROM dbo.HoaDon WHERE maHoaDon LIKE 'HD181126%';")
lines.append("DELETE FROM dbo.SanPham WHERE maSanPham LIKE 'SP181126%';")
lines.append("DELETE FROM dbo.KhachHang WHERE maKhachHang LIKE 'KH181126%';")
lines.append("")
lines.append("-- 2. Chèn 5 Khách hàng mới")
lines.append("INSERT INTO dbo.KhachHang (maKhachHang, ho, ten, gioiTinh, soDienThoai, diaChi) VALUES")
cust_strs = []
for c in customers:
    cust_strs.append(f"    ('{c[0]}', N'{c[1]}', N'{c[2]}', N'{c[3]}', '{c[4]}', N'{c[5]}')")
lines.append(",\n".join(cust_strs) + ";")
lines.append("")

lines.append("-- 3. Chèn 10 Sản phẩm mới (giaBan là cột tính tự động giaNhap + mucLoiNhuan)")
lines.append("INSERT INTO dbo.SanPham (maSanPham, tenSanPham, donViTinh, soLuongTon, giaNhap, mucLoiNhuan, trangThai, maLoaiSanPham) VALUES")
prod_strs = []
for p in products:
    prod_strs.append(f"    ('{p[0]}', N'{p[1]}', N'{p[2]}', {p[3]}, {p[4]}, {p[5]}, N'Đang hoạt động', '{p[6]}')")
lines.append(",\n".join(prod_strs) + ";")
lines.append("")

lines.append("-- 4. Chèn 20 Hóa đơn cho ngày 18/11/2026")
lines.append("INSERT INTO dbo.HoaDon")
lines.append("    (maHoaDon, ngayGioLap, phuongThucThanhToan, trangThaiThanhToan, tienKhachDua, thoiGianThanhToan, maKM, maNhanVien, maKhachHang, tienDuocGiam, tongThanhToan)")
lines.append("VALUES")

hd_strs = []
cthd_strs = []

for idx in range(20):
    maHD = f"HD181126{idx+1:04d}"
    t = f"2026-11-18T{times[idx]}"
    pttt = methods[idx]
    km = promos[idx]
    nv = staff[idx % len(staff)]
    kh = customers[idx % len(customers)][0]
    
    items = invoice_items_def[idx]
    tongTienSP = sum(prod_dict[sp]["giaBan"] * sl for sp, sl in items)
    
    if km == "KM071126001":
        tienGiam = round_half_up(tongTienSP * 10 / 100)
    elif km == "KM071126002":
        tienGiam = min(5000, tongTienSP)
    else:
        tienGiam = 0
        
    tienSauKM = tongTienSP - tienGiam
    vat = round_half_up(tienSauKM * 8 / 100)
    tongThanhToan = tienSauKM + vat
    
    if pttt == "Tiền mặt":
        if tongThanhToan <= 45000:
            tienKhachDua = 50000
        elif tongThanhToan <= 90000:
            tienKhachDua = 100000
        else:
            tienKhachDua = 200000
    else:
        tienKhachDua = tongThanhToan
        
    km_val = f"'{km}'" if km else "NULL"
    hd_strs.append(
        f"    ('{maHD}', '{t}', N'{pttt}', N'Đã thanh toán', {tienKhachDua}, '{t}', {km_val}, '{nv}', '{kh}', {tienGiam}, {tongThanhToan})"
    )
    
    for sp, sl in items:
        gb = prod_dict[sp]["giaBan"]
        cthd_strs.append(f"    ('{maHD}', '{sp}', {sl}, {gb})")

lines.append(",\n".join(hd_strs) + ";")
lines.append("")

lines.append("-- 5. Chèn Chi tiết hóa đơn tương ứng cho 20 hóa đơn")
lines.append("INSERT INTO dbo.ChiTietHoaDon (maHoaDon, maSanPham, soLuong, giaBan) VALUES")
lines.append(",\n".join(cthd_strs) + ";")
lines.append("")
lines.append("COMMIT TRANSACTION;")
lines.append("GO")
lines.append("")
lines.append("-- 6. Kiểm tra lại dữ liệu vừa chèn")
lines.append("SELECT N'Khách hàng vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong FROM dbo.KhachHang WHERE maKhachHang LIKE 'KH181126%';")
lines.append("SELECT N'Sản phẩm vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong FROM dbo.SanPham WHERE maSanPham LIKE 'SP181126%';")
lines.append("SELECT N'Hóa đơn vừa tạo' AS LoaiThongTin, COUNT(*) AS SoLuong, SUM(tongThanhToan) AS TongDoanhThuNgay FROM dbo.HoaDon WHERE maHoaDon LIKE 'HD181126%';")
lines.append("GO")

output_path = r"app\database\03_DuLieu_HomNay.sql"
with open(output_path, "w", encoding="utf-8") as f:
    f.write("\n".join(lines))

print(f"Successfully generated {output_path}")

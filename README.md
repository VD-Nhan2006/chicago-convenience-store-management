# Chicago Convenience Store Management

Ứng dụng Java Swing quản lý sản phẩm, loại sản phẩm và chương trình khuyến mãi trên SQL Server.

## Chuẩn bị cơ sở dữ liệu

1. Cài SQL Server và bật kết nối TCP/IP, sau đó khởi động dịch vụ SQL Server.
2. Chạy `app/database/01_TaoCSDL_DBChicago.sql` bằng SSMS hoặc `sqlcmd`. **Script này xóa và tạo lại database `DBChicago`**, vì vậy chỉ chạy khi đã sao lưu dữ liệu cần giữ.
3. Chạy `app/database/02_DuLieuMau_DBChicago.sql` để nạp dữ liệu mẫu.
4. JDBC driver SQL Server đã có tại `app/lib/mssql-jdbc-13.6.0.jre11.jar`.

## Cấu hình kết nối

Ứng dụng đọc cấu hình từ biến môi trường hoặc Java system properties. Cấu hình VS Code mặc định dùng Windows Authentication và cổng TCP `1433`. Bật TCP/IP cho SQL Server instance `SQLEXPRESS` và cấu hình cổng này như hướng dẫn bên dưới:

| Tên | Mặc định | Ý nghĩa |
| --- | --- | --- |
| `CHICAGO_DB_SERVER` | `localhost` | Máy chủ SQL Server |
| `CHICAGO_DB_INSTANCE` | *(không đặt)* | Tên instance; đặt giá trị này thay cho cổng nếu dùng SQL Browser/instance discovery |
| `CHICAGO_DB_PORT` | `1433` | Cổng TCP cố định của SQL Server |
| `CHICAGO_DB_NAME` | `DBChicago` | Tên database |
| `CHICAGO_DB_AUTH` | `sql` | `windows` hoặc `sql` |
| `CHICAGO_DB_USER` | `sa` | Tài khoản khi dùng SQL Authentication |
| `CHICAGO_DB_PASSWORD` | rỗng | Mật khẩu khi dùng SQL Authentication |

### Dùng Windows Authentication

Driver Microsoft JDBC cần thư viện xác thực native khớp với driver, hệ điều hành và kiến trúc Java. Tải gói Microsoft JDBC Driver **13.6.0** từ trang Microsoft, giải nén file `mssql-jdbc_auth-13.6.0.x64.dll` (với Java 64-bit) vào `app/lib`. Cấu hình Run and Debug đã thêm thư mục này vào `java.library.path`.

Trong **SQL Server Configuration Manager**, mở **SQL Server Network Configuration → Protocols for SQLEXPRESS**, bật **TCP/IP**, rồi vào **TCP/IP → Properties → IP Addresses → IPAll**. Để `TCP Dynamic Ports` trống và đặt `TCP Port` là `1433`. Nhấn Apply, sau đó restart dịch vụ **SQL Server (SQLEXPRESS)** trong **SQL Server Services**. SQL Server Browser không cần thiết khi dùng cổng tĩnh. Nếu cổng `1433` đang được chương trình khác sử dụng, chọn cổng trống khác và cập nhật `CHICAGO_DB_PORT` trong `.vscode/launch.json`.

### Dùng SQL Server Authentication thay thế

Chọn Mixed Mode trong SQL Server, tạo/bật SQL login có quyền truy cập `DBChicago`, rồi đặt các biến môi trường dưới đây trong cùng terminal trước khi chạy:

```powershell
$env:CHICAGO_DB_SERVER = "localhost"
$env:CHICAGO_DB_INSTANCE = "SQLEXPRESS"
$env:CHICAGO_DB_NAME = "DBChicago"
$env:CHICAGO_DB_AUTH = "sql"
$env:CHICAGO_DB_USER = "sa"
$env:CHICAGO_DB_PASSWORD = "your-sql-server-password"
```

Không commit mật khẩu thật vào Git. Muốn dùng SQL Authentication trong cấu hình F5, cập nhật `CHICAGO_DB_AUTH`, user và password trong `.vscode/launch.json` hoặc đặt chúng ở môi trường VS Code.

Kết nối JDBC bật mã hóa và tin cậy chứng chỉ máy chủ để dùng thuận tiện với SQL Server cục bộ. Khi triển khai, hãy cấu hình chứng chỉ TLS phù hợp thay vì dựa vào tin cậy chứng chỉ tự ký.

## Biên dịch và chạy

Trong VS Code, mở thư mục gốc của dự án, mở tab **Run and Debug**, chọn **Run Chicago product management** rồi nhấn **F5**. Cấu hình có sẵn tại `.vscode/launch.json`; nó chạy main class với toàn bộ source và JDBC driver của project. Không dùng nút **Run Code** của Code Runner trên file Java riêng lẻ vì nút đó chỉ biên dịch file đang mở, không biên dịch các package phụ thuộc.

Hoặc mở PowerShell tại thư mục `app`:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
$sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac --release 11 -encoding UTF-8 -cp lib\mssql-jdbc-13.6.0.jre11.jar -d bin $sources
java -cp "bin;lib\mssql-jdbc-13.6.0.jre11.jar" gui.ProductManagementApp
```

`SanPhamUI`, `LoaiSanPhamUI` và `KhuyenMaiUI` là các `JPanel` đã được đăng ký vào vùng CardLayout của `FrmChinh`; mỗi panel có `public void loadData()` để nạp dữ liệu từ DAO. Mã menu đã dùng:

```java
pnlNoiDung.add(new SanPhamUI(), "SAN_PHAM");
pnlNoiDung.add(new LoaiSanPhamUI(), "LOAI_SAN_PHAM");
pnlNoiDung.add(new KhuyenMaiUI(), "KHUYEN_MAI");
```

Đăng nhập qua `main.Main`; từ menu bên trái trong `FrmChinh`, chọn màn hình tương ứng.

Danh sách được đọc từ SQL Server; các nút thêm, cập nhật và xóa ghi trực tiếp vào database. Mã bản ghi mới được sinh tự động theo ngày.

Giá bán sản phẩm do database tính từ giá nhập và mức lợi nhuận. Mã nguồn JDBC nằm trong `app/src/connectDB`, câu truy vấn CRUD nằm trong `app/src/dao`.

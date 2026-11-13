package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import entity.HoaDon;

public class Dashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    // Bảng màu
    private final Color MAU_NEN    = new Color(250, 247, 240);
    private final Color MAU_XANH   = new Color(23, 52, 81);
    private final Color MAU_DO     = new Color(239, 77, 59);
    private final Color MAU_VIEN   = new Color(23, 52, 81);
    private final Color MAU_XAM    = new Color(230, 237, 245);

    // Card labels
    private JLabel lblDoanhThu;
    private JLabel lblSoHoaDon;
    private JLabel lblTienGiam;
    private JLabel lblSanPham;

    // Bảng hóa đơn
    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    private HoaDonDAO dao = new HoaDonDAO();
    private final DateTimeFormatter FMT_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Dashboard() {
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoTieuDe();
        taoNoiDung();
        taiLai();
    }

    // ── Tiêu đề ──────────────────────────────────────────────────────────────
    private void taoTieuDe() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Dashboard");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(MAU_XANH);

        String homNay = LocalDate.now().format(FMT_NGAY);
        JLabel lblPhu = new JLabel("Số liệu ngày " + homNay + ", tính hóa đơn đã thanh toán");
        lblPhu.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPhu.setForeground(MAU_XANH);

        pnl.add(lblTieuDe);
        pnl.add(Box.createVerticalStrut(2));
        pnl.add(lblPhu);

        add(pnl, BorderLayout.NORTH);
    }

    // ── Nội dung chính ───────────────────────────────────────────────────────
    private void taoNoiDung() {
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 12));
        pnlCenter.setBackground(MAU_NEN);
        pnlCenter.add(taoCacTheThongKe(), BorderLayout.NORTH);
        pnlCenter.add(taoBangHoaDon(),    BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);
    }

    // ── 4 thẻ thống kê ───────────────────────────────────────────────────────
    private JPanel taoCacTheThongKe() {
        JPanel pnl = new JPanel(new GridLayout(1, 4, 12, 0));
        pnl.setBackground(MAU_NEN);
        pnl.setPreferredSize(new Dimension(0, 90));

        lblDoanhThu  = new JLabel("0");
        lblSoHoaDon  = new JLabel("0");
        lblTienGiam  = new JLabel("0");
        lblSanPham   = new JLabel("0");

        pnl.add(taoThe("Doanh thu hôm nay",  lblDoanhThu,  MAU_XANH));
        pnl.add(taoThe("Số hóa đơn",          lblSoHoaDon,  MAU_DO));
        pnl.add(taoThe("Tiền được giảm",       lblTienGiam,  MAU_XANH));
        pnl.add(taoThe("Sản phẩm đã bán",     lblSanPham,   MAU_DO));

        return pnl;
    }

    private JPanel taoThe(String tieuDe, JLabel lblGiaTri, Color mauNen) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel lblTen = new JLabel(tieuDe);
        lblTen.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTen.setForeground(Color.WHITE);

        lblGiaTri.setFont(new Font("Arial", Font.BOLD, 22));
        lblGiaTri.setForeground(Color.WHITE);

        pnl.add(lblTen);
        pnl.add(Box.createVerticalStrut(6));
        pnl.add(lblGiaTri);

        return pnl;
    }

    // ── Bảng hóa đơn ─────────────────────────────────────────────────────────
    private JScrollPane taoBangHoaDon() {
        String[] tieuDe = {
            "Mã HD", "Khách hàng", "Nhân viên", "Thanh toán", "Phương thức"
        };

        tableModel = new DefaultTableModel(tieuDe, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHoaDon = new JTable(tableModel);
        tblHoaDon.setRowHeight(30);
        tblHoaDon.setFont(new Font("Arial", Font.PLAIN, 13));
        tblHoaDon.setForeground(MAU_XANH);
        tblHoaDon.setSelectionBackground(MAU_XAM);
        tblHoaDon.setSelectionForeground(MAU_XANH);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblHoaDon.getTableHeader().setBackground(MAU_XANH);
        tblHoaDon.getTableHeader().setForeground(Color.WHITE);
        tblHoaDon.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tblHoaDon.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(BorderFactory.createLineBorder(MAU_VIEN));
        return scroll;
    }

    // ── Tải dữ liệu ──────────────────────────────────────────────────────────
    public void taiLai() {
        tableModel.setRowCount(0);

        String homNay = LocalDate.now().format(FMT_NGAY);
        ArrayList<HoaDon> dsHD = dao.getDanhSach(homNay, homNay);

        BigDecimal tongDoanhThu = BigDecimal.ZERO;
        BigDecimal tongTienGiam = BigDecimal.ZERO;

        for (HoaDon hd : dsHD) {
            BigDecimal tongSP  = hd.tinhTongTienSP();
            BigDecimal tienKM  = hd.tinhTienKhuyenMai(tongSP);
            BigDecimal thanhToan = hd.tinhTongTienThanhToan();

            tongDoanhThu = tongDoanhThu.add(thanhToan);
            tongTienGiam = tongTienGiam.add(tienKM);

            String tenKH = "";
            if (hd.getKhachHang() != null) {
                tenKH = hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen();
            }
            String tenNV = "";
            if (hd.getNhanVien() != null) {
                tenNV = hd.getNhanVien().getHo() + " " + hd.getNhanVien().getTen();
            }

            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(),
                tenKH.trim(),
                tenNV.trim(),
                dinhDangTien(thanhToan),
                hd.getPhuongThucThanhToan()
            });
        }

        int soSP = dao.demSanPhamBanHomNay();

        lblDoanhThu.setText(dinhDangTien(tongDoanhThu));
        lblSoHoaDon.setText(String.valueOf(dsHD.size()));
        lblTienGiam.setText(dinhDangTien(tongTienGiam));
        lblSanPham.setText(String.valueOf(soSP));
    }

    private String dinhDangTien(BigDecimal so) {
        if (so == null) return "0";
        return String.format("%,.0f", so);
    }
}
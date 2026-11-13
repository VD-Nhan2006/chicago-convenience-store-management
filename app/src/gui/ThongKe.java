package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import entity.HoaDon;

public class ThongKe extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Bảng màu
    private final Color MAU_NEN  = new Color(250, 247, 240);
    private final Color MAU_XANH = new Color(23, 52, 81);
    private final Color MAU_DO   = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_XAM  = new Color(230, 237, 245);

    // Bộ lọc
    private JComboBox<String> cboKy;
    private JTextField txtTuNgay;
    private JTextField txtDenNgay;
    private JButton    btnTim;

    // Card
    private JLabel lblDoanhThu;
    private JLabel lblTienGiam;
    private JLabel lblSoHoaDon;

    // Bảng
    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    private HoaDonDAO dao = new HoaDonDAO();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ThongKe() {
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoTieuDe();
        taoNoiDung();
        // Load mặc định hôm nay
        String homNay = LocalDate.now().format(FMT);
        txtTuNgay.setText(homNay);
        txtDenNgay.setText(homNay);
        taiLai();
    }

    // ── Tiêu đề + bộ lọc + 3 thẻ ────────────────────────────────────────────
    private void taoTieuDe() {
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(MAU_NEN);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("Thống kê");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(MAU_XANH);

        JLabel lblPhu = new JLabel("Doanh thu tổng, tổng tiền thanh toán sau khuyến mãi, Tổng tiền được giảm cộng ròng");
        lblPhu.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPhu.setForeground(MAU_XANH);

        pnlTop.add(lblTieuDe);
        pnlTop.add(Box.createVerticalStrut(2));
        pnlTop.add(lblPhu);
        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(taoBoDanhLoc());
        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(taoCacThe());

        add(pnlTop, BorderLayout.NORTH);
    }

    // ── Bộ lọc ───────────────────────────────────────────────────────────────
    private JPanel taoBoDanhLoc() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
        pnl.setBackground(MAU_NEN);
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);

        cboKy = new JComboBox<>(new String[] {
            "Khoảng thời gian", "Hôm nay", "Tuần này", "Tháng này", "Tùy chọn"
        });
        cboKy.setFont(new Font("Arial", Font.PLAIN, 13));
        cboKy.setMaximumSize(new Dimension(170, 32));
        cboKy.setPreferredSize(new Dimension(170, 32));
        cboKy.addActionListener(this);

        txtTuNgay = new JTextField();
        txtTuNgay.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTuNgay.setMaximumSize(new Dimension(110, 32));
        txtTuNgay.setPreferredSize(new Dimension(110, 32));

        txtDenNgay = new JTextField();
        txtDenNgay.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDenNgay.setMaximumSize(new Dimension(110, 32));
        txtDenNgay.setPreferredSize(new Dimension(110, 32));

        btnTim = new JButton("Xem");
        btnTim.setFont(new Font("Arial", Font.BOLD, 13));
        btnTim.setBackground(MAU_DO);
        btnTim.setForeground(Color.WHITE);
        btnTim.setFocusPainted(false);
        btnTim.setOpaque(true);
        btnTim.setMargin(new Insets(4, 14, 4, 14));
        btnTim.setPreferredSize(new Dimension(80, 32));
        btnTim.setMaximumSize(new Dimension(80, 32));
        btnTim.addActionListener(this);

        JLabel lblTu = new JLabel("Từ ngày");
        lblTu.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTu.setForeground(MAU_XANH);

        JLabel lblDen = new JLabel("Đến ngày");
        lblDen.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDen.setForeground(MAU_XANH);

        pnl.add(cboKy);
        pnl.add(Box.createHorizontalStrut(10));
        pnl.add(lblTu);
        pnl.add(Box.createHorizontalStrut(6));
        pnl.add(txtTuNgay);
        pnl.add(Box.createHorizontalStrut(10));
        pnl.add(lblDen);
        pnl.add(Box.createHorizontalStrut(6));
        pnl.add(txtDenNgay);
        pnl.add(Box.createHorizontalStrut(10));
        pnl.add(btnTim);

        return pnl;
    }

    // ── 3 thẻ ────────────────────────────────────────────────────────────────
    private JPanel taoCacThe() {
        JPanel pnl = new JPanel(new GridLayout(1, 3, 12, 0));
        pnl.setBackground(MAU_NEN);
        pnl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDoanhThu = new JLabel("0");
        lblTienGiam = new JLabel("0");
        lblSoHoaDon = new JLabel("0");

        pnl.add(taoThe("Doanh Thu",       lblDoanhThu, MAU_XANH));
        pnl.add(taoThe("Tiền được giảm",  lblTienGiam, MAU_XANH));
        pnl.add(taoThe("Số hóa đơn",      lblSoHoaDon, MAU_DO));

        return pnl;
    }

    private JPanel taoThe(String tieuDe, JLabel lblGiaTri, Color mauNen) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lblTen = new JLabel(tieuDe);
        lblTen.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTen.setForeground(Color.WHITE);

        lblGiaTri.setFont(new Font("Arial", Font.BOLD, 20));
        lblGiaTri.setForeground(Color.WHITE);

        pnl.add(lblTen);
        pnl.add(Box.createVerticalStrut(4));
        pnl.add(lblGiaTri);

        return pnl;
    }

    // ── Bảng ─────────────────────────────────────────────────────────────────
    private void taoNoiDung() {
        String[] tieuDe = {
            "Mã HD", "Thời điểm thanh toán", "Khách hàng", "Tiền giảm", "Thanh toán"
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

        add(scroll, BorderLayout.CENTER);
    }

    // ── Sự kiện ──────────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cboKy) {
            String chon = (String) cboKy.getSelectedItem();
            LocalDate homNay = LocalDate.now();
            if ("Hôm nay".equals(chon)) {
                txtTuNgay.setText(homNay.format(FMT));
                txtDenNgay.setText(homNay.format(FMT));
                taiLai();
            } else if ("Tuần này".equals(chon)) {
                LocalDate dauTuan = homNay.with(DayOfWeek.MONDAY);
                txtTuNgay.setText(dauTuan.format(FMT));
                txtDenNgay.setText(homNay.format(FMT));
                taiLai();
            } else if ("Tháng này".equals(chon)) {
                LocalDate dauThang = homNay.with(TemporalAdjusters.firstDayOfMonth());
                txtTuNgay.setText(dauThang.format(FMT));
                txtDenNgay.setText(homNay.format(FMT));
                taiLai();
            }
            // "Tùy chọn" và "Khoảng thời gian" => để user tự nhập rồi bấm Xem
        } else if (e.getSource() == btnTim) {
            taiLai();
        }
    }

    // ── Tải dữ liệu ──────────────────────────────────────────────────────────
    public void taiLai() {
        tableModel.setRowCount(0);

        String tuNgay  = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();

        ArrayList<HoaDon> dsHD = dao.getDanhSach(tuNgay, denNgay);

        BigDecimal tongDoanhThu = BigDecimal.ZERO;
        BigDecimal tongTienGiam = BigDecimal.ZERO;

        for (HoaDon hd : dsHD) {
            BigDecimal tongSP   = hd.tinhTongTienSP();
            BigDecimal tienKM   = hd.tinhTienKhuyenMai(tongSP);
            BigDecimal thanhToan = hd.tinhTongTienThanhToan();

            tongDoanhThu = tongDoanhThu.add(thanhToan);
            tongTienGiam = tongTienGiam.add(tienKM);

            String tenKH = "";
            if (hd.getKhachHang() != null) {
                tenKH = hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen();
            }

            String thoiGian = "";
            if (hd.getThoiGianThanhToan() != null) {
                thoiGian = hd.getThoiGianThanhToan().format(FMT_DT);
            } else if (hd.getNgayGioLap() != null) {
                thoiGian = hd.getNgayGioLap().format(FMT_DT);
            }

            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(),
                thoiGian,
                tenKH.trim(),
                dinhDangTien(tienKM),
                dinhDangTien(thanhToan)
            });
        }

        lblDoanhThu.setText(dinhDangTien(tongDoanhThu));
        lblTienGiam.setText(dinhDangTien(tongTienGiam));
        lblSoHoaDon.setText(String.valueOf(dsHD.size()));
    }

    private String dinhDangTien(BigDecimal so) {
        if (so == null) return "0";
        return String.format("%,.0f", so);
    }
}
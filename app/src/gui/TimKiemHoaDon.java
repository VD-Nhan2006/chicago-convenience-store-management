package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.TimKiemHoaDonDAO;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;

public class TimKiemHoaDon extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 1L;

    private static final Color NAVY   = new Color(23, 52, 81);
    private static final Color RED    = new Color(239, 77, 59);
    private static final Color BG     = new Color(250, 247, 240);
    private static final Color SELECT = new Color(230, 237, 245);

    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private final TimKiemHoaDonDAO dao = new TimKiemHoaDonDAO();
    private ArrayList<HoaDon> dsHienTai = new ArrayList<HoaDon>();
    private JTextField txtMaHD, txtTuNgay, txtDenNgay, txtKhachHang;
    private JButton btnTim, btnLamMoi;
    private JTable tblHoaDon;
    private DefaultTableModel modelHoaDon;
    private JLabel valMaHD, valKhachHang, valNhanVien;
    private JLabel valTongTien, valKhuyenMai, valTienGiam, valThanhToan, valKhachDuaTra;
    private JButton btnInHoaDon;

    public TimKiemHoaDon() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(taoTieuDeVaTim(), BorderLayout.NORTH);
        add(taoNoiDung(),     BorderLayout.CENTER);

        taiLai();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                lamMoi();
            }
        });
    }

    private JPanel taoTieuDeVaTim() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(BG);

        JPanel pnlTieuDe = new JPanel();
        pnlTieuDe.setLayout(new BoxLayout(pnlTieuDe, BoxLayout.Y_AXIS));
        pnlTieuDe.setBackground(BG);

        JLabel lblTieuDe = new JLabel("Tìm kiếm hóa đơn");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(NAVY);

        JLabel lblMoTa = new JLabel(
                "Tìm theo mã, khoảng ngày hoặc khách hàng; hóa đơn đã thanh toán không sửa, không xóa.");
        lblMoTa.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMoTa.setForeground(Color.GRAY);

        pnlTieuDe.add(lblTieuDe);
        pnlTieuDe.add(lblMoTa);

        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        pnlTim.setBackground(Color.WHITE);
        pnlTim.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));

        txtMaHD      = newField(120); txtMaHD.addActionListener(e -> timKiem());
        txtTuNgay    = newField(100); txtTuNgay.setToolTipText("dd/MM/yyyy");
        txtDenNgay   = newField(100); txtDenNgay.setToolTipText("dd/MM/yyyy");
        txtKhachHang = newField(150); txtKhachHang.addActionListener(e -> timKiem());

        btnTim    = taoNut("Tìm", NAVY, Color.WHITE);
        btnLamMoi = taoNut("Làm mới", Color.WHITE, NAVY);
        btnLamMoi.setBorder(BorderFactory.createLineBorder(NAVY));
        btnTim.setPreferredSize(new Dimension(80, 32));
        btnLamMoi.setPreferredSize(new Dimension(80, 32));
        btnTim.addActionListener(e -> timKiem());
        btnLamMoi.addActionListener(e -> lamMoi());

        pnlTim.add(label("Mã hóa đơn"));  pnlTim.add(txtMaHD);
        pnlTim.add(label("Từ ngày"));      pnlTim.add(txtTuNgay);
        pnlTim.add(label("Đến ngày"));     pnlTim.add(txtDenNgay);
        pnlTim.add(label("Khách hàng"));   pnlTim.add(txtKhachHang);
        pnlTim.add(btnTim);
        pnlTim.add(btnLamMoi);

        pnl.add(pnlTieuDe, BorderLayout.NORTH);
        pnl.add(pnlTim,    BorderLayout.SOUTH);
        return pnl;
    }

    private JPanel taoNoiDung() {
        JPanel pnl = new JPanel(new BorderLayout(12, 0));
        pnl.setBackground(BG);
        pnl.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        pnl.add(taoBang(),         BorderLayout.CENTER);
        pnl.add(taoPanelChiTiet(), BorderLayout.EAST);
        return pnl;
    }

    private JScrollPane taoBang() {
        String[] cols = { "Mã HD", "Thời điểm thanh toán", "Khách hàng", "Tổng thanh toán", "Phương thức" };
        modelHoaDon = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(30);
        tblHoaDon.setFont(new Font("Arial", Font.PLAIN, 13));
        tblHoaDon.setForeground(NAVY);
        tblHoaDon.setSelectionBackground(SELECT);
        tblHoaDon.setSelectionForeground(NAVY);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setFillsViewportHeight(true);
        tblHoaDon.getTableHeader().setReorderingAllowed(false);
        tblHoaDon.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tblHoaDon.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, false, false, r, c);
                lbl.setBackground(NAVY);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Arial", Font.BOLD, 13));
                lbl.setOpaque(true);
                return lbl;
            }
        });

        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(110);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(110);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        tblHoaDon.getColumnModel().getColumn(3).setCellRenderer(right);

        tblHoaDon.getSelectionModel().addListSelectionListener(this);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(BorderFactory.createLineBorder(NAVY));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JPanel taoPanelChiTiet() {
        JPanel pnl = new JPanel();
        pnl.setPreferredSize(new Dimension(240, 0));
        pnl.setBackground(Color.WHITE);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(NAVY),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        valMaHD = new JLabel("—");
        valMaHD.setFont(new Font("Arial", Font.BOLD, 15));
        valMaHD.setForeground(NAVY);
        valMaHD.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnl.add(valMaHD);
        pnl.add(Box.createVerticalStrut(8));
        pnl.add(gachNgang());
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Khách hàng"));
        valKhachHang = giaTri("—"); pnl.add(valKhachHang);
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Nhân viên"));
        valNhanVien = giaTri("—"); pnl.add(valNhanVien);
        pnl.add(Box.createVerticalStrut(8));
        pnl.add(gachNgang());
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Tổng tiền hàng"));
        valTongTien = giaTri("—"); pnl.add(valTongTien);
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Khuyến mãi"));
        valKhuyenMai = giaTri("—"); pnl.add(valKhuyenMai);
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Tiền giảm"));
        valTienGiam = giaTri("—"); pnl.add(valTienGiam);
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Thanh toán"));
        valThanhToan = giaTri("—");
        valThanhToan.setFont(new Font("Arial", Font.BOLD, 13));
        pnl.add(valThanhToan);
        pnl.add(Box.createVerticalStrut(8));
        pnl.add(gachNgang());
        pnl.add(Box.createVerticalStrut(8));

        pnl.add(nhan("Khách đưa / Trả lại"));
        valKhachDuaTra = giaTri("—"); pnl.add(valKhachDuaTra);
        pnl.add(Box.createVerticalGlue());

        btnInHoaDon = taoNut("In hóa đơn", RED, Color.WHITE);
        btnInHoaDon.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInHoaDon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnInHoaDon.setEnabled(false);
        btnInHoaDon.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Chức năng in hóa đơn đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
        pnl.add(btnInHoaDon);
        return pnl;
    }

    private JTextField newField(int w) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(w, 32));
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        return tf;
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(NAVY);
        return lbl;
    }

    private JLabel nhan(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel giaTri(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(NAVY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JSeparator gachNgang() {
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    private JButton taoNut(String ten, Color bg, Color fg) {
        JButton btn = new JButton(ten);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(5, 10, 5, 10));
        return btn;
    }

    private void taiLai() {
        try {
            hienThi(dao.timKiem("", null, null, ""));
        } catch (SQLException e) {
            baoLoiCsdl(e);
        }
    }

    private void lamMoi() {
        txtMaHD.setText("");
        txtTuNgay.setText("");
        txtDenNgay.setText("");
        txtKhachHang.setText("");
        taiLai();
        xoaChiTiet();
        tblHoaDon.clearSelection();
    }

    private void timKiem() {
        String maHD  = txtMaHD.getText().trim();
        String tuNg  = txtTuNgay.getText().trim();
        String denNg = txtDenNgay.getText().trim();
        String tenKH = txtKhachHang.getText().trim();

        LocalDate tuNgay = null;
        LocalDate denNgay = null;
        if (!tuNg.isEmpty()) {
            tuNgay = docNgay(tuNg);
            if (tuNgay == null) {
                JOptionPane.showMessageDialog(this,
                        "Từ ngày không hợp lệ. Định dạng: dd/MM/yyyy", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (!denNg.isEmpty()) {
            denNgay = docNgay(denNg);
            if (denNgay == null) {
                JOptionPane.showMessageDialog(this,
                        "Đến ngày không hợp lệ. Định dạng: dd/MM/yyyy", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (tuNgay != null && denNgay != null && tuNgay.isAfter(denNgay)) {
            JOptionPane.showMessageDialog(this,
                    "Từ ngày không được sau đến ngày.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            hienThi(dao.timKiem(maHD, tuNgay, denNgay, tenKH));
        } catch (SQLException e) {
            baoLoiCsdl(e);
            return;
        }
        xoaChiTiet();
        tblHoaDon.clearSelection();
    }

    private void hienThi(ArrayList<HoaDon> ds) {
        dsHienTai = ds;
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : ds) {
            String tenKH = hd.getKhachHang() != null
                    ? (hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen()).trim() : "";
            String thoiDiem = hd.getThoiGianThanhToan() != null
                    ? hd.getThoiGianThanhToan().format(FMT_DT) : "";
            modelHoaDon.addRow(new Object[]{
                hd.getMaHoaDon(), thoiDiem, tenKH,
                dinhDangTien(hd.tinhTongTienThanhToan()),
                hd.getPhuongThucThanhToan()
            });
        }
    }

    private void hienThiChiTiet(int row) {
        if (row < 0 || row >= dsHienTai.size()) return;
        HoaDon hd;
        try {
            hd = dao.layChiTiet(dsHienTai.get(row).getMaHoaDon());
        } catch (SQLException e) {
            baoLoiCsdl(e);
            return;
        }
        if (hd == null) {
            JOptionPane.showMessageDialog(this,
                    "Hóa đơn không còn tồn tại.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        valMaHD.setText(hd.getMaHoaDon());

        String tenKH = hd.getKhachHang() != null
                ? (hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen()).trim() : "—";
        valKhachHang.setText(tenKH);

        String tenNV = hd.getNhanVien() != null
                ? (hd.getNhanVien().getHo() + " " + hd.getNhanVien().getTen()).trim() : "—";
        valNhanVien.setText(tenNV);

        BigDecimal tongTienSP   = hd.tinhTongTienSP();
        BigDecimal tienKM       = hd.tinhTienKhuyenMai(tongTienSP);
        BigDecimal thanhToan    = hd.tinhTongTienThanhToan();
        BigDecimal traNguoiMua  = hd.tinhTienThoi();

        valTongTien.setText(dinhDangTien(tongTienSP));

        // Khuyến mãi
        ChuongTrinhKhuyenMai km = hd.getChuongTrinhKM();
        if (km != null) {
            String loai = "PHAN_TRAM".equals(km.getLoaiKhuyenMai())
                    ? "Giảm " + km.getGiaTriKM().stripTrailingZeros().toPlainString() + "%"
                    : "Giảm " + dinhDangTien(km.getGiaTriKM());
            valKhuyenMai.setText(km.getTenChuongTrinh() + " — " + loai);
        } else {
            valKhuyenMai.setText("Không có");
        }

        valTienGiam.setText(dinhDangTien(tienKM));
        valThanhToan.setText(dinhDangTien(thanhToan));

        BigDecimal khachDua = hd.getTienKhachDua() != null ? hd.getTienKhachDua() : BigDecimal.ZERO;
        valKhachDuaTra.setText(dinhDangTien(khachDua) + " / " + dinhDangTien(traNguoiMua));

        btnInHoaDon.setEnabled(true);
    }

    private void xoaChiTiet() {
        valMaHD.setText("—");
        valKhachHang.setText("—");
        valNhanVien.setText("—");
        valTongTien.setText("—");
        valKhuyenMai.setText("—");
        valTienGiam.setText("—");
        valThanhToan.setText("—");
        valKhachDuaTra.setText("—");
        btnInHoaDon.setEnabled(false);
    }

    private LocalDate docNgay(String s) {
        try { return LocalDate.parse(s, FMT_D); }
        catch (DateTimeParseException e) { return null; }
    }

    private void baoLoiCsdl(SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Lỗi cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private static String dinhDangTien(BigDecimal so) {
        if (so == null) return "—";
        return String.format("%,.0f đ", so);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = tblHoaDon.getSelectedRow();
            if (row >= 0) hienThiChiTiet(row);
            else xoaChiTiet();
        }
    }
}
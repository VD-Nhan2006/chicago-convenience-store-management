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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.ThongKeDAO;

public class ThongKe extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final Color MAU_NEN  = new Color(250, 247, 240);
    private final Color MAU_XANH = new Color(23, 52, 81);
    private final Color MAU_DO   = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_XAM  = new Color(230, 237, 245);

    private JComboBox<String> cboKy;
    private JTextField txtTuNgay;
    private JTextField txtDenNgay;
    private JButton    btnTim;

    private JLabel lblDoanhThu;
    private JLabel lblTienGiam;
    private JLabel lblSoHoaDon;

    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    private ThongKeDAO dao = new ThongKeDAO();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ThongKe() {
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoTieuDe();
        taoNoiDung();
        String homNay = LocalDate.now().format(FMT);
        txtTuNgay.setText(homNay);
        txtDenNgay.setText(homNay);
        taiLai();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (!apDungKy() && cboKy.getSelectedIndex() == 0) {
                    String homNayMoi = LocalDate.now().format(FMT);
                    txtTuNgay.setText(homNayMoi);
                    txtDenNgay.setText(homNayMoi);
                }
                taiLai();
            }
        });
    }


    private void taoTieuDe() {
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Thống kê");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(MAU_XANH);

        JLabel lblPhu = new JLabel("Doanh thu, tiền giảm và số hóa đơn đã thanh toán trong khoảng thời gian chọn");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cboKy) {
            if (apDungKy()) {
                taiLai();
            }
        } else if (e.getSource() == btnTim) {
            taiLai();
        }
    }

    private boolean apDungKy() {
        String chon = (String) cboKy.getSelectedItem();
        LocalDate homNay = LocalDate.now();
        LocalDate tuNgay;
        if ("Hôm nay".equals(chon)) {
            tuNgay = homNay;
        } else if ("Tuần này".equals(chon)) {
            tuNgay = homNay.with(DayOfWeek.MONDAY);
        } else if ("Tháng này".equals(chon)) {
            tuNgay = homNay.with(TemporalAdjusters.firstDayOfMonth());
        } else {
            return false;
        }
        txtTuNgay.setText(tuNgay.format(FMT));
        txtDenNgay.setText(homNay.format(FMT));
        return true;
    }


    public void taiLai() {
        LocalDate tuNgay;
        LocalDate denNgay;
        try {
            tuNgay = LocalDate.parse(txtTuNgay.getText().trim(), FMT);
        } catch (DateTimeParseException ex) {
            baoLoi("Từ ngày không hợp lệ. Định dạng: dd/MM/yyyy", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            denNgay = LocalDate.parse(txtDenNgay.getText().trim(), FMT);
        } catch (DateTimeParseException ex) {
            baoLoi("Đến ngày không hợp lệ. Định dạng: dd/MM/yyyy", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tuNgay.isAfter(denNgay)) {
            baoLoi("Từ ngày không được sau đến ngày.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<ThongKeDAO.DongHoaDon> dsHD;
        ThongKeDAO.TongHop tongHop;
        try {
            dsHD = dao.layHoaDon(tuNgay, denNgay);
            tongHop = dao.tongHop(tuNgay, denNgay);
        } catch (SQLException ex) {
            baoLoi("Lỗi cơ sở dữ liệu: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        for (ThongKeDAO.DongHoaDon hd : dsHD) {
            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(),
                hd.getThoiGianThanhToan().format(FMT_DT),
                hd.getTenKhachHang(),
                dinhDangTien(hd.getTienGiam()),
                dinhDangTien(hd.getTongThanhToan())
            });
        }

        lblDoanhThu.setText(dinhDangTien(tongHop.getDoanhThu()));
        lblTienGiam.setText(dinhDangTien(tongHop.getTienGiam()));
        lblSoHoaDon.setText(String.valueOf(tongHop.getSoHoaDon()));
    }

    private void baoLoi(String noiDung, int kieu) {
        JOptionPane.showMessageDialog(this, noiDung, "Lỗi", kieu);
    }

    private String dinhDangTien(BigDecimal so) {
        if (so == null) return "0";
        return String.format("%,.0f", so);
    }
}
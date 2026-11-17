package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;	
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.ThongKeDAO;
import service.ThongKeService;

public class Dashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color MAU_NEN    = new Color(250, 247, 240);
    private final Color MAU_XANH   = new Color(23, 52, 81);
    private final Color MAU_DO     = new Color(239, 77, 59);
    private final Color MAU_VIEN   = new Color(23, 52, 81);
    private final Color MAU_XAM    = new Color(230, 237, 245);

    private JLabel lblDoanhThu;
    private JLabel lblSoHoaDon;
    private JLabel lblTienGiam;
    private JLabel lblSanPham;
    private JLabel lblPhu;
    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    private final ThongKeService thongKeService = new ThongKeService();
    private final DateTimeFormatter FMT_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Dashboard() {
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoTieuDe();
        taoNoiDung();
        taiLai();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                taiLai();
            }
        });
    }

    private void taoTieuDe() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Dashboard");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(MAU_XANH);

        String homNay = LocalDate.now().format(FMT_NGAY);
        lblPhu = new JLabel("Số liệu ngày " + homNay + ", tính hóa đơn đã thanh toán");
        lblPhu.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPhu.setForeground(MAU_XANH);

        pnl.add(lblTieuDe);
        pnl.add(Box.createVerticalStrut(2));
        pnl.add(lblPhu);

        add(pnl, BorderLayout.NORTH);
    }

    private void taoNoiDung() {
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 12));
        pnlCenter.setBackground(MAU_NEN);
        pnlCenter.add(taoCacTheThongKe(), BorderLayout.NORTH);
        pnlCenter.add(taoBangHoaDon(),    BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);
    }

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

    public void taiLai() {
        LocalDate homNay = LocalDate.now();

        ArrayList<ThongKeDAO.DongHoaDon> dsHD;
        ThongKeDAO.TongHop tongHop;
        try {
            dsHD = thongKeService.layHoaDon(homNay, homNay);
            tongHop = thongKeService.tongHop(homNay, homNay);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lblPhu.setText("Số liệu ngày " + homNay.format(FMT_NGAY) + ", tính hóa đơn đã thanh toán");

        tableModel.setRowCount(0);
        for (ThongKeDAO.DongHoaDon hd : dsHD) {
            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(),
                hd.getTenKhachHang(),
                hd.getTenNhanVien(),
                dinhDangTien(hd.getTongThanhToan()),
                hd.getPhuongThucThanhToan()
            });
        }

        lblDoanhThu.setText(dinhDangTien(tongHop.getDoanhThu()));
        lblSoHoaDon.setText(String.valueOf(tongHop.getSoHoaDon()));
        lblTienGiam.setText(dinhDangTien(tongHop.getTienGiam()));
        lblSanPham.setText(String.valueOf(tongHop.getSoSanPham()));
    }

    private String dinhDangTien(BigDecimal so) {
        if (so == null) return "0";
        return String.format("%,.0f", so);
    }
}
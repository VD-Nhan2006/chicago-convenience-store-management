package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

import dao.ThongKeDAO;

public class ThongKe extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Color MAU_NEN = new Color(244, 247, 251);
    private static final Color MAU_XANH = new Color(23, 52, 81);
    private static final Color MAU_DO = new Color(239, 77, 59);
    private static final Color MAU_VIEN = new Color(224, 230, 238);
    private static final Color MAU_LUOI = new Color(232, 237, 244);
    private static final Color MAU_XAM = new Color(120, 130, 145);
    private static final Color MAU_DUONG = new Color(74, 120, 184);
    private static final Color MAU_XANH_LA = new Color(31, 157, 98);
    private static final Color MAU_CAM = new Color(240, 170, 50);
    private static final Color[] MAU_NHOM = {
        new Color(74, 120, 184), new Color(31, 157, 98), new Color(240, 190, 60),
        new Color(135, 115, 200), new Color(239, 110, 80)
    };

    private static final String KY_HOM_NAY = "Hôm nay";
    private static final String KY_7_NGAY = "7 ngày gần đây";
    private static final String KY_30_NGAY = "30 ngày gần đây";
    private static final String KY_THANG = "Tháng này";
    private static final String KY_TUY_CHON = "Tùy chọn";
    private static final String[] CHI_SO = { "Doanh thu", "Lợi nhuận", "Số hóa đơn" };

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FMT_NGAY_THANG = DateTimeFormatter.ofPattern("dd/MM");
    private static final DecimalFormatSymbols KY_HIEU = taoKyHieuSo();

    private JTextField txtTuNgay;
    private JTextField txtDenNgay;
    private JComboBox<String> cboKy;
    private JButton btnApDung;
    private boolean dangDatCombo;

    private final TheSo theDoanhThu = new TheSo();
    private final TheSo theLoiNhuan = new TheSo();
    private final TheSo theHoaDon = new TheSo();

    private JComboBox<String> cboChiSo;
    private JLabel lblTieuDeDuong;
    private JLabel lblDonVi;
    private JLabel lblChuThich;
    private JLabel lblCaoNhat;
    private BieuDoDuong bieuDoDuong;

    private BieuDoTron bieuDoTron;
    private JPanel pnlChuThichNhom;

    private ThongKeDAO dao = new ThongKeDAO();
    private ArrayList<ThongKeDAO.DiemNgay> dsNgay = new ArrayList<ThongKeDAO.DiemNgay>();

    public ThongKe() {
        setLayout(new BorderLayout(0, 14));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel pnlTren = new JPanel(new BorderLayout(0, 14));
        pnlTren.setOpaque(false);
        pnlTren.add(taoTieuDe(), BorderLayout.NORTH);
        pnlTren.add(taoBoLoc(), BorderLayout.CENTER);
        add(pnlTren, BorderLayout.NORTH);

        JPanel pnlGiua = new JPanel(new BorderLayout(0, 14));
        pnlGiua.setOpaque(false);
        pnlGiua.add(taoCacThe(), BorderLayout.NORTH);
        pnlGiua.add(taoCacBieuDo(), BorderLayout.CENTER);
        add(pnlGiua, BorderLayout.CENTER);

        apDungKy();
        taiLai();

        cboKy.addActionListener(this);
        cboChiSo.addActionListener(this);
        btnApDung.addActionListener(this);
        txtTuNgay.addActionListener(this);
        txtDenNgay.addActionListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                apDungKy();
                taiLai();
            }
        });
    }

    private static DecimalFormatSymbols taoKyHieuSo() {
        DecimalFormatSymbols ky = new DecimalFormatSymbols();
        ky.setDecimalSeparator(',');
        ky.setGroupingSeparator('.');
        return ky;
    }

    private static String dinhDangSo(Number so, String mau) {
        return new DecimalFormat(mau, KY_HIEU).format(so);
    }

    private static String rutGon(BigDecimal tien) {
        double x = tien.doubleValue();
        if (x >= 1e9) {
            return dinhDangSo(x / 1e9, "#,##0.#") + " tỷ";
        }
        if (x >= 1e6) {
            return dinhDangSo(x / 1e6, "#,##0.#") + " tr";
        }
        if (x >= 1e3) {
            return dinhDangSo(x / 1e3, "#,##0.#") + " k";
        }
        return dinhDangSo(x, "#,##0");
    }

    private JPanel taoTieuDe() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);

        JPanel pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setOpaque(false);

        JLabel lblTieuDe = new JLabel("Thống kê & báo cáo");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setForeground(MAU_XANH);

        JLabel lblPhu = new JLabel("Theo dõi hiệu quả kinh doanh và cơ cấu doanh thu cửa hàng");
        lblPhu.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPhu.setForeground(MAU_XAM);

        pnlTrai.add(lblTieuDe);
        pnlTrai.add(Box.createVerticalStrut(2));
        pnlTrai.add(lblPhu);

        Card huyHieu = new Card(new FlowLayout(FlowLayout.LEFT, 8, 0));
        huyHieu.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        JLabel lblHuyHieu = new JLabel("Dữ liệu cập nhật theo bộ lọc", new Cham(MAU_XANH_LA, 8), SwingConstants.LEFT);
        lblHuyHieu.setIconTextGap(8);
        lblHuyHieu.setFont(new Font("Arial", Font.PLAIN, 12));
        lblHuyHieu.setForeground(MAU_XANH);
        huyHieu.add(lblHuyHieu);

        JPanel pnlPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlPhai.setOpaque(false);
        pnlPhai.add(huyHieu);

        pnl.add(pnlTrai, BorderLayout.WEST);
        pnl.add(pnlPhai, BorderLayout.EAST);
        return pnl;
    }

    private JPanel taoBoLoc() {
        Card card = new Card(new BorderLayout(12, 0));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setOpaque(false);

        JLabel lblNhan = new JLabel("Khoảng thời gian");
        lblNhan.setFont(new Font("Arial", Font.BOLD, 12));
        lblNhan.setForeground(MAU_XANH);
        lblNhan.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTuNgay = taoONgay();
        txtDenNgay = taoONgay();

        JLabel lblDen = new JLabel("đến");
        lblDen.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDen.setForeground(MAU_XAM);

        cboKy = new JComboBox<String>(new String[] { KY_HOM_NAY, KY_7_NGAY, KY_30_NGAY, KY_THANG, KY_TUY_CHON });
        cboKy.setFont(new Font("Arial", Font.PLAIN, 13));
        cboKy.setPreferredSize(new Dimension(150, 34));
        cboKy.setSelectedItem(KY_7_NGAY);

        JPanel pnlO = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlO.setOpaque(false);
        pnlO.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlO.add(txtTuNgay);
        pnlO.add(lblDen);
        pnlO.add(txtDenNgay);
        pnlO.add(cboKy);

        pnlTrai.add(lblNhan);
        pnlTrai.add(Box.createVerticalStrut(6));
        pnlTrai.add(pnlO);

        btnApDung = new JButton("Áp dụng");
        btnApDung.setFont(new Font("Arial", Font.BOLD, 13));
        btnApDung.setBackground(MAU_XANH);
        btnApDung.setForeground(Color.WHITE);
        btnApDung.setFocusPainted(false);
        btnApDung.setOpaque(true);
        btnApDung.setContentAreaFilled(true);
        btnApDung.setBorderPainted(false);
        btnApDung.setMargin(new Insets(5, 10, 5, 10));
        btnApDung.setPreferredSize(new Dimension(100, 36));

        JPanel pnlPhai = new JPanel(new GridBagLayout());
        pnlPhai.setOpaque(false);
        pnlPhai.add(btnApDung);

        card.add(pnlTrai, BorderLayout.WEST);
        card.add(pnlPhai, BorderLayout.EAST);
        return card;
    }

    private JTextField taoONgay() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        txt.setPreferredSize(new Dimension(110, 34));
        txt.setToolTipText("dd/MM/yyyy");
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)));
        return txt;
    }

    private JPanel taoCacThe() {
        JPanel pnl = new JPanel(new GridLayout(1, 3, 16, 0));
        pnl.setOpaque(false);
        pnl.setPreferredSize(new Dimension(0, 122));
        pnl.add(taoThe("Tổng doanh thu", 0, MAU_DUONG, new Color(227, 236, 250), theDoanhThu));
        pnl.add(taoThe("Lợi nhuận ước tính", 1, MAU_XANH_LA, new Color(224, 245, 234), theLoiNhuan));
        pnl.add(taoThe("Số hóa đơn", 2, MAU_CAM, new Color(255, 243, 214), theHoaDon));
        return pnl;
    }

    private Card taoThe(String tieuDe, int loaiIcon, Color mauIcon, Color nenIcon, TheSo the) {
        Card card = new Card(new BorderLayout(0, 6));

        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 13));
        lblTieuDe.setForeground(MAU_XANH);

        JPanel pnlDau = new JPanel();
        pnlDau.setLayout(new BoxLayout(pnlDau, BoxLayout.X_AXIS));
        pnlDau.setOpaque(false);
        pnlDau.add(new TheIcon(loaiIcon, mauIcon, nenIcon));
        pnlDau.add(Box.createHorizontalStrut(10));
        pnlDau.add(lblTieuDe);

        the.giaTri.setFont(new Font("Arial", Font.BOLD, 28));
        the.giaTri.setForeground(MAU_XANH);
        the.dau.setFont(new Font("Arial", Font.BOLD, 12));
        the.ghiChu.setFont(new Font("Arial", Font.PLAIN, 12));
        the.ghiChu.setForeground(MAU_XAM);

        JPanel pnlCuoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlCuoi.setOpaque(false);
        pnlCuoi.add(the.dau);
        pnlCuoi.add(the.ghiChu);

        card.add(pnlDau, BorderLayout.NORTH);
        card.add(the.giaTri, BorderLayout.CENTER);
        card.add(pnlCuoi, BorderLayout.SOUTH);
        return card;
    }

    private JPanel taoCacBieuDo() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 2;
        c.insets = new Insets(0, 0, 0, 8);
        pnl.add(taoTheBieuDoDuong(), c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 8, 0, 0);
        pnl.add(taoTheBieuDoTron(), c);
        return pnl;
    }

    private Card taoTheBieuDoDuong() {
        Card card = new Card(new BorderLayout(0, 8));

        lblTieuDeDuong = new JLabel("Doanh thu theo ngày");
        lblTieuDeDuong.setFont(new Font("Arial", Font.BOLD, 15));
        lblTieuDeDuong.setForeground(MAU_XANH);
        lblDonVi = new JLabel("Đơn vị: triệu đồng");
        lblDonVi.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDonVi.setForeground(MAU_XAM);

        JPanel pnlChu = new JPanel();
        pnlChu.setLayout(new BoxLayout(pnlChu, BoxLayout.Y_AXIS));
        pnlChu.setOpaque(false);
        pnlChu.add(lblTieuDeDuong);
        pnlChu.add(lblDonVi);

        cboChiSo = new JComboBox<String>(CHI_SO);
        cboChiSo.setFont(new Font("Arial", Font.PLAIN, 12));
        cboChiSo.setPreferredSize(new Dimension(130, 30));

        JPanel pnlCombo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlCombo.setOpaque(false);
        pnlCombo.add(cboChiSo);

        JPanel pnlDau = new JPanel(new BorderLayout());
        pnlDau.setOpaque(false);
        pnlDau.add(pnlChu, BorderLayout.WEST);
        pnlDau.add(pnlCombo, BorderLayout.EAST);

        bieuDoDuong = new BieuDoDuong();

        lblChuThich = new JLabel("Doanh thu thực tế", new Cham(MAU_DUONG, 9), SwingConstants.LEFT);
        lblChuThich.setIconTextGap(8);
        lblChuThich.setFont(new Font("Arial", Font.PLAIN, 12));
        lblChuThich.setForeground(MAU_XAM);
        lblCaoNhat = new JLabel("Cao nhất: —");
        lblCaoNhat.setFont(new Font("Arial", Font.BOLD, 12));
        lblCaoNhat.setForeground(MAU_XANH_LA);

        JPanel pnlChan = new JPanel(new BorderLayout());
        pnlChan.setOpaque(false);
        pnlChan.add(lblChuThich, BorderLayout.WEST);
        pnlChan.add(lblCaoNhat, BorderLayout.EAST);

        card.add(pnlDau, BorderLayout.NORTH);
        card.add(bieuDoDuong, BorderLayout.CENTER);
        card.add(pnlChan, BorderLayout.SOUTH);
        return card;
    }

    private Card taoTheBieuDoTron() {
        Card card = new Card(new BorderLayout(0, 8));

        JLabel lblTieuDe = new JLabel("Cơ cấu doanh thu");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 15));
        lblTieuDe.setForeground(MAU_XANH);
        JLabel lblPhu = new JLabel("Theo nhóm sản phẩm");
        lblPhu.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPhu.setForeground(MAU_XAM);

        JPanel pnlChu = new JPanel();
        pnlChu.setLayout(new BoxLayout(pnlChu, BoxLayout.Y_AXIS));
        pnlChu.setOpaque(false);
        pnlChu.add(lblTieuDe);
        pnlChu.add(lblPhu);

        bieuDoTron = new BieuDoTron();

        pnlChuThichNhom = new JPanel(new GridLayout(0, 1, 0, 6));
        pnlChuThichNhom.setOpaque(false);

        card.add(pnlChu, BorderLayout.NORTH);
        card.add(bieuDoTron, BorderLayout.CENTER);
        card.add(pnlChuThichNhom, BorderLayout.SOUTH);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == cboKy) {
            if (!dangDatCombo && apDungKy()) {
                taiLai();
            }
        } else if (nguon == btnApDung || nguon == txtTuNgay || nguon == txtDenNgay) {
            dongBoCombo();
            taiLai();
        } else if (nguon == cboChiSo) {
            capNhatBieuDoDuong();
        }
    }

    private LocalDate[] khoangCua(String ky) {
        LocalDate homNay = LocalDate.now();
        if (KY_HOM_NAY.equals(ky)) {
            return new LocalDate[] { homNay, homNay };
        }
        if (KY_7_NGAY.equals(ky)) {
            return new LocalDate[] { homNay.minusDays(6), homNay };
        }
        if (KY_30_NGAY.equals(ky)) {
            return new LocalDate[] { homNay.minusDays(29), homNay };
        }
        if (KY_THANG.equals(ky)) {
            return new LocalDate[] { homNay.with(TemporalAdjusters.firstDayOfMonth()), homNay };
        }
        return null;
    }

    private boolean apDungKy() {
        LocalDate[] khoang = khoangCua((String) cboKy.getSelectedItem());
        if (khoang == null) {
            return false;
        }
        txtTuNgay.setText(khoang[0].format(FMT));
        txtDenNgay.setText(khoang[1].format(FMT));
        return true;
    }

    private void dongBoCombo() {
        LocalDate tu = docNgay(txtTuNgay.getText());
        LocalDate den = docNgay(txtDenNgay.getText());
        if (tu == null || den == null) {
            return;
        }
        LocalDate[] khoang = khoangCua((String) cboKy.getSelectedItem());
        if (khoang != null && khoang[0].equals(tu) && khoang[1].equals(den)) {
            return;
        }
        dangDatCombo = true;
        cboKy.setSelectedItem(KY_TUY_CHON);
        dangDatCombo = false;
    }

    private LocalDate docNgay(String s) {
        try {
            return LocalDate.parse(s.trim(), FMT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void taiLai() {
        LocalDate tuNgay = docNgay(txtTuNgay.getText());
        if (tuNgay == null) {
            baoLoi("Từ ngày không hợp lệ. Định dạng: dd/MM/yyyy", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate denNgay = docNgay(txtDenNgay.getText());
        if (denNgay == null) {
            baoLoi("Đến ngày không hợp lệ. Định dạng: dd/MM/yyyy", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tuNgay.isAfter(denNgay)) {
            baoLoi("Từ ngày không được sau đến ngày.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long soNgay = ChronoUnit.DAYS.between(tuNgay, denNgay) + 1;
        ThongKeDAO.TongHop hienTai;
        ThongKeDAO.TongHop truoc;
        ArrayList<ThongKeDAO.NhomSanPham> dsNhom;
        try {
            hienTai = dao.tongHop(tuNgay, denNgay);
            truoc = dao.tongHop(tuNgay.minusDays(soNgay), tuNgay.minusDays(1));
            dsNgay = dao.theoNgay(tuNgay, denNgay);
            dsNhom = dao.theoNhomSanPham(tuNgay, denNgay);
        } catch (SQLException ex) {
            baoLoi("Lỗi cơ sở dữ liệu: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        capNhatCacThe(hienTai, truoc, soNgay);
        capNhatBieuDoDuong();
        capNhatBieuDoTron(dsNhom, hienTai.getDoanhThu());
    }

    private void capNhatCacThe(ThongKeDAO.TongHop hienTai, ThongKeDAO.TongHop truoc, long soNgay) {
        theDoanhThu.giaTri.setText(dinhDangSo(hienTai.getDoanhThu(), "#,##0") + " đ");
        datTangTruong(theDoanhThu, hienTai.getDoanhThu(), truoc.getDoanhThu(), soNgay);

        theLoiNhuan.giaTri.setText(dinhDangSo(hienTai.getLoiNhuan(), "#,##0") + " đ");
        theLoiNhuan.ghiChu.setText(" biên lợi nhuận");
        if (hienTai.getDoanhThu().signum() > 0) {
            BigDecimal bien = hienTai.getLoiNhuan().multiply(BigDecimal.valueOf(100))
                    .divide(hienTai.getDoanhThu(), 1, RoundingMode.HALF_UP);
            theLoiNhuan.dau.setText(dinhDangSo(bien, "0.0") + "%");
            theLoiNhuan.dau.setForeground(bien.signum() >= 0 ? MAU_XANH_LA : MAU_DO);
        } else {
            theLoiNhuan.dau.setText("—");
            theLoiNhuan.dau.setForeground(MAU_XAM);
        }

        theHoaDon.giaTri.setText(String.valueOf(hienTai.getSoHoaDon()));
        datTangTruong(theHoaDon, BigDecimal.valueOf(hienTai.getSoHoaDon()), BigDecimal.valueOf(truoc.getSoHoaDon()),
                soNgay);
    }

    private void datTangTruong(TheSo the, BigDecimal hienTai, BigDecimal truoc, long soNgay) {
        the.ghiChu.setText(" so với " + soNgay + " ngày trước");
        if (truoc.signum() <= 0) {
            the.dau.setText("—");
            the.dau.setForeground(MAU_XAM);
            return;
        }
        BigDecimal phanTram = hienTai.subtract(truoc).multiply(BigDecimal.valueOf(100))
                .divide(truoc, 1, RoundingMode.HALF_UP);
        the.dau.setText((phanTram.signum() > 0 ? "+" : "") + dinhDangSo(phanTram, "0.0") + "%");
        the.dau.setForeground(phanTram.signum() >= 0 ? MAU_XANH_LA : MAU_DO);
    }

    private void capNhatBieuDoDuong() {
        int chiSo = cboChiSo.getSelectedIndex();
        int n = dsNgay.size();
        double[] giaTri = new double[n];
        String[] nhan = new String[n];
        String[] chuThich = new String[n];
        double max = 0;
        int viTriMax = -1;

        for (int i = 0; i < n; i++) {
            ThongKeDAO.DiemNgay d = dsNgay.get(i);
            double v;
            String chu;
            if (chiSo == 0) {
                v = d.getDoanhThu().doubleValue();
                chu = dinhDangSo(d.getDoanhThu(), "#,##0") + " đ";
            } else if (chiSo == 1) {
                v = d.getLoiNhuan().doubleValue();
                chu = dinhDangSo(d.getLoiNhuan(), "#,##0") + " đ";
            } else {
                v = d.getSoHoaDon();
                chu = d.getSoHoaDon() + " hóa đơn";
            }
            giaTri[i] = Math.max(0, v);
            nhan[i] = d.getNgay().format(FMT_NGAY_THANG);
            chuThich[i] = d.getNgay().format(FMT) + ": " + chu;
            if (giaTri[i] > max) {
                max = giaTri[i];
                viTriMax = i;
            }
        }

        double chia;
        String donVi;
        if (chiSo == 2) {
            chia = 1;
            donVi = "Đơn vị: hóa đơn";
        } else if (max >= 1_000_000) {
            chia = 1_000_000;
            donVi = "Đơn vị: triệu đồng";
        } else {
            chia = 1_000;
            donVi = "Đơn vị: nghìn đồng";
        }

        String[] tieuDe = { "Doanh thu theo ngày", "Lợi nhuận theo ngày", "Số hóa đơn theo ngày" };
        String[] chuThichDuong = { "Doanh thu thực tế", "Lợi nhuận ước tính", "Số hóa đơn" };
        lblTieuDeDuong.setText(tieuDe[chiSo]);
        lblDonVi.setText(donVi);
        lblChuThich.setText(chuThichDuong[chiSo]);
        lblCaoNhat.setText("Cao nhất: " + (viTriMax >= 0 ? nhan[viTriMax] : "—"));
        bieuDoDuong.datDuLieu(giaTri, nhan, chuThich, chia);
    }

    private void capNhatBieuDoTron(ArrayList<ThongKeDAO.NhomSanPham> dsNhom, BigDecimal tongDoanhThu) {
        ArrayList<String> ten = new ArrayList<String>();
        ArrayList<BigDecimal> tien = new ArrayList<BigDecimal>();
        BigDecimal tong = BigDecimal.ZERO;
        for (ThongKeDAO.NhomSanPham nhom : dsNhom) {
            tong = tong.add(nhom.getDoanhThu());
        }

        if (dsNhom.size() > MAU_NHOM.length) {
            BigDecimal conLai = BigDecimal.ZERO;
            for (int i = 0; i < dsNhom.size(); i++) {
                if (i < MAU_NHOM.length - 1) {
                    ten.add(dsNhom.get(i).getTenNhom());
                    tien.add(dsNhom.get(i).getDoanhThu());
                } else {
                    conLai = conLai.add(dsNhom.get(i).getDoanhThu());
                }
            }
            ten.add("Khác");
            tien.add(conLai);
        } else {
            for (ThongKeDAO.NhomSanPham nhom : dsNhom) {
                ten.add(nhom.getTenNhom());
                tien.add(nhom.getDoanhThu());
            }
        }

        double[] giaTri = new double[tien.size()];
        Color[] mau = new Color[tien.size()];
        pnlChuThichNhom.removeAll();
        for (int i = 0; i < tien.size(); i++) {
            giaTri[i] = tien.get(i).doubleValue();
            mau[i] = MAU_NHOM[i % MAU_NHOM.length];
            String phanTram = tong.signum() > 0
                    ? dinhDangSo(tien.get(i).multiply(BigDecimal.valueOf(100)).divide(tong, 1, RoundingMode.HALF_UP),
                            "0.#") + "%"
                    : "0%";
            pnlChuThichNhom.add(taoDongNhom(mau[i], ten.get(i), phanTram));
        }
        pnlChuThichNhom.revalidate();
        pnlChuThichNhom.repaint();

        bieuDoTron.datDuLieu(giaTri, mau, "Tổng doanh thu", rutGon(tongDoanhThu));
    }

    private JPanel taoDongNhom(Color mau, String ten, String phanTram) {
        JPanel dong = new JPanel(new BorderLayout(8, 0));
        dong.setOpaque(false);

        JLabel lblTen = new JLabel(ten, new Cham(mau, 9), SwingConstants.LEFT);
        lblTen.setIconTextGap(8);
        lblTen.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTen.setForeground(MAU_XAM);

        JLabel lblPhanTram = new JLabel(phanTram);
        lblPhanTram.setFont(new Font("Arial", Font.BOLD, 12));
        lblPhanTram.setForeground(MAU_XANH);

        dong.add(lblTen, BorderLayout.WEST);
        dong.add(lblPhanTram, BorderLayout.EAST);
        return dong;
    }

    private void baoLoi(String noiDung, int kieu) {
        JOptionPane.showMessageDialog(this, noiDung, "Lỗi", kieu);
    }

    private static class TheSo {
        private final JLabel giaTri = new JLabel("0 đ");
        private final JLabel dau = new JLabel("—");
        private final JLabel ghiChu = new JLabel("");
    }

    private static class Card extends JPanel {
        private static final long serialVersionUID = 1L;

        Card(LayoutManager layout) {
            super(layout);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(MAU_VIEN);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class Cham implements Icon {
        private final Color mau;
        private final int kichThuoc;

        Cham(Color mau, int kichThuoc) {
            this.mau = mau;
            this.kichThuoc = kichThuoc;
        }

        @Override
        public int getIconWidth() {
            return kichThuoc;
        }

        @Override
        public int getIconHeight() {
            return kichThuoc;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(mau);
            g2.fillOval(x, y, kichThuoc, kichThuoc);
            g2.dispose();
        }
    }

    private static class TheIcon extends JComponent {
        private static final long serialVersionUID = 1L;

        private final int loai;
        private final Color mau;
        private final Color nen;

        TheIcon(int loai, Color mau, Color nen) {
            this.loai = loai;
            this.mau = mau;
            this.nen = nen;
            setPreferredSize(new Dimension(34, 34));
            setMaximumSize(new Dimension(34, 34));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2;
            g2.setColor(nen);
            g2.fillOval(0, 0, w - 1, h - 1);
            g2.setColor(mau);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (loai == 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String s = "\u20AB";
                g2.drawString(s, cx - fm.stringWidth(s) / 2, cy + fm.getAscent() / 2 - 2);
            } else if (loai == 1) {
                g2.drawLine(cx - 5, cy + 5, cx + 5, cy - 5);
                g2.drawLine(cx + 5, cy - 5, cx - 1, cy - 5);
                g2.drawLine(cx + 5, cy - 5, cx + 5, cy + 1);
            } else {
                g2.drawRoundRect(cx - 6, cy - 8, 12, 16, 3, 3);
                g2.drawLine(cx - 3, cy - 3, cx + 3, cy - 3);
                g2.drawLine(cx - 3, cy + 1, cx + 3, cy + 1);
                g2.drawLine(cx - 3, cy + 5, cx + 1, cy + 5);
            }
            g2.dispose();
        }
    }

    private static class BieuDoDuong extends JPanel {
        private static final long serialVersionUID = 1L;

        private static final int TRAI = 46;
        private static final int PHAI = 14;
        private static final int TREN = 12;
        private static final int DUOI = 28;

        private double[] giaTri = new double[0];
        private String[] nhan = new String[0];
        private String[] chuThich = new String[0];
        private double chia = 1;
        private int hover = -1;

        BieuDoDuong() {
            setOpaque(false);
            setPreferredSize(new Dimension(400, 250));
            ToolTipManager.sharedInstance().registerComponent(this);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int i = diemGan(e.getX());
                    if (i != hover) {
                        hover = i;
                        setToolTipText(i >= 0 ? chuThich[i] : null);
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hover = -1;
                    setToolTipText(null);
                    repaint();
                }
            });
        }

        void datDuLieu(double[] giaTri, String[] nhan, String[] chuThich, double chia) {
            this.giaTri = giaTri;
            this.nhan = nhan;
            this.chuThich = chuThich;
            this.chia = chia;
            this.hover = -1;
            setToolTipText(null);
            repaint();
        }

        private int diemGan(int x) {
            int n = giaTri.length;
            int rong = getWidth() - TRAI - PHAI;
            if (n == 0 || rong <= 0) {
                return -1;
            }
            int i = (int) Math.floor((x - TRAI) * (double) n / rong);
            return (i >= 0 && i < n) ? i : -1;
        }

        private static double buocDep(double max) {
            if (max <= 0) {
                return 1;
            }
            double tho = max / 4;
            double bac = Math.pow(10, Math.floor(Math.log10(tho)));
            double d = tho / bac;
            double dep = d <= 1 ? 1 : d <= 2 ? 2 : d <= 5 ? 5 : 10;
            return dep * bac;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int rong = getWidth() - TRAI - PHAI;
            int cao = getHeight() - TREN - DUOI;
            if (rong <= 0 || cao <= 0) {
                g2.dispose();
                return;
            }

            int n = giaTri.length;
            double max = 0;
            for (double v : giaTri) {
                max = Math.max(max, v / chia);
            }
            double buoc = buocDep(max);
            double tran = Math.max(buoc, Math.ceil(max / buoc - 1e-9) * buoc);
            int soNac = (int) Math.round(tran / buoc);

            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            FontMetrics fm = g2.getFontMetrics();
            BasicStroke net = new BasicStroke(1f);
            BasicStroke netDut = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
                    new float[] { 3f, 4f }, 0f);
            for (int k = 0; k <= soNac; k++) {
                double v = k * buoc;
                int y = (int) Math.round(TREN + cao - v / tran * cao);
                g2.setColor(MAU_LUOI);
                g2.setStroke(k == 0 ? net : netDut);
                g2.drawLine(TRAI, y, TRAI + rong, y);
                String s = dinhDangSo(v, "#,##0.##");
                g2.setColor(MAU_XAM);
                g2.drawString(s, TRAI - 8 - fm.stringWidth(s), y + fm.getAscent() / 2 - 1);
            }

            if (n == 0) {
                g2.dispose();
                return;
            }

            double[] px = new double[n];
            double[] py = new double[n];
            for (int i = 0; i < n; i++) {
                px[i] = TRAI + (i + 0.5) * rong / n;
                py[i] = TREN + cao - (giaTri[i] / chia) / tran * cao;
            }

            int gap = Math.max(1, (int) Math.ceil(n / (double) Math.max(1, rong / 52)));
            g2.setColor(MAU_XAM);
            for (int i = 0; i < n; i += gap) {
                g2.drawString(nhan[i], (int) px[i] - fm.stringWidth(nhan[i]) / 2, TREN + cao + 18);
            }

            if (max <= 0) {
                String s = "Không có dữ liệu trong khoảng thời gian này";
                g2.drawString(s, TRAI + (rong - fm.stringWidth(s)) / 2, TREN + cao / 2);
                g2.dispose();
                return;
            }

            if (hover >= 0) {
                g2.setColor(MAU_LUOI);
                g2.setStroke(net);
                g2.drawLine((int) px[hover], TREN, (int) px[hover], TREN + cao);
            }

            Path2D vung = new Path2D.Double();
            vung.moveTo(px[0], TREN + cao);
            for (int i = 0; i < n; i++) {
                vung.lineTo(px[i], py[i]);
            }
            vung.lineTo(px[n - 1], TREN + cao);
            vung.closePath();
            g2.setPaint(new GradientPaint(0, TREN, new Color(74, 120, 184, 70), 0, TREN + cao,
                    new Color(74, 120, 184, 6)));
            g2.fill(vung);

            Path2D duong = new Path2D.Double();
            duong.moveTo(px[0], py[0]);
            for (int i = 1; i < n; i++) {
                duong.lineTo(px[i], py[i]);
            }
            g2.setColor(MAU_DUONG);
            g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(duong);

            g2.setStroke(new BasicStroke(2f));
            for (int i = 0; i < n; i++) {
                boolean chon = i == hover;
                if (n > 31 && !chon) {
                    continue;
                }
                double r = chon ? 6 : 4.5;
                g2.setColor(Color.WHITE);
                g2.fill(new java.awt.geom.Ellipse2D.Double(px[i] - r, py[i] - r, 2 * r, 2 * r));
                g2.setColor(MAU_DUONG);
                g2.draw(new java.awt.geom.Ellipse2D.Double(px[i] - r, py[i] - r, 2 * r, 2 * r));
            }
            g2.dispose();
        }
    }

    private static class BieuDoTron extends JPanel {
        private static final long serialVersionUID = 1L;

        private double[] giaTri = new double[0];
        private Color[] mau = new Color[0];
        private String chuTren = "";
        private String chuGiua = "";

        BieuDoTron() {
            setOpaque(false);
            setPreferredSize(new Dimension(200, 190));
        }

        void datDuLieu(double[] giaTri, Color[] mau, String chuTren, String chuGiua) {
            this.giaTri = giaTri;
            this.mau = mau;
            this.chuTren = chuTren;
            this.chuGiua = chuGiua;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int d = Math.min(getWidth(), getHeight()) - 12;
            if (d < 60) {
                g2.dispose();
                return;
            }
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;
            float day = Math.max(14f, d * 0.14f);
            double canh = d - day;
            double x0 = x + day / 2.0;
            double y0 = y + day / 2.0;

            double tong = 0;
            for (double v : giaTri) {
                tong += v;
            }

            if (tong <= 0) {
                g2.setColor(MAU_LUOI);
                g2.setStroke(new BasicStroke(day));
                g2.draw(new Arc2D.Double(x0, y0, canh, canh, 90, 360, Arc2D.OPEN));
            } else {
                double batDau = 90;
                double khe = giaTri.length > 1 ? 1.6 : 0;
                for (int i = 0; i < giaTri.length; i++) {
                    double goc = -360.0 * giaTri[i] / tong;
                    if (-goc > khe) {
                        g2.setColor(mau[i]);
                        g2.setStroke(new BasicStroke(day, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                        g2.draw(new Arc2D.Double(x0, y0, canh, canh, batDau - khe / 2, goc + khe, Arc2D.OPEN));
                    }
                    batDau += goc;
                }
            }

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(MAU_XAM);
            String tren = tong <= 0 ? "Chưa có dữ liệu" : chuTren;
            g2.drawString(tren, cx - fm.stringWidth(tren) / 2, cy - 4);
            if (tong > 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                fm = g2.getFontMetrics();
                g2.setColor(MAU_XANH);
                g2.drawString(chuGiua, cx - fm.stringWidth(chuGiua) / 2, cy + fm.getAscent() - 2);
            }
            g2.dispose();
        }
    }
}
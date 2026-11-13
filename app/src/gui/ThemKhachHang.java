package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.KhachHangDAO;
import entity.KhachHang;

public class ThemKhachHang extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private KhachHangDAO dao = new KhachHangDAO();
    private Runnable onDong;

    private JButton btnQuayLai;
    private JTextField txtMa;
    private JTextField txtHoTen;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtSoDienThoai;
    private JTextField txtDiaChi;
    private JButton btnLuu;
    private JButton btnHuy;

    public ThemKhachHang(Runnable onDong) {
        this.onDong = onDong;

        setLayout(new BorderLayout(0, 12));
        setBackground(QuanLyKhachHang.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoTieuDe();

        JPanel pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(QuanLyKhachHang.BG);
        pnlNoiDung.add(taoForm(), BorderLayout.NORTH);
        add(pnlNoiDung, BorderLayout.CENTER);
    }

    private void taoTieuDe() {
        JPanel pnlTieuDe = new JPanel(new BorderLayout());
        pnlTieuDe.setBackground(QuanLyKhachHang.BG);

        JPanel pnlTrai = new JPanel();
        pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
        pnlTrai.setBackground(QuanLyKhachHang.BG);

        JLabel lblTieuDe = new JLabel("Thêm khách hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(QuanLyKhachHang.NAVY);

        JLabel lblMoTa = new JLabel("Nhập thông tin để tạo hồ sơ khách hàng mới.");
        lblMoTa.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMoTa.setForeground(Color.GRAY);

        pnlTrai.add(lblTieuDe);
        pnlTrai.add(lblMoTa);

        btnQuayLai = taoNut("Quay lại quản lý khách hàng", Color.WHITE, QuanLyKhachHang.NAVY);
        btnQuayLai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(QuanLyKhachHang.NAVY),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btnQuayLai.addActionListener(this);

        JPanel pnlPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlPhai.setBackground(QuanLyKhachHang.BG);
        pnlPhai.add(btnQuayLai);

        pnlTieuDe.add(pnlTrai, BorderLayout.WEST);
        pnlTieuDe.add(pnlPhai, BorderLayout.EAST);
        add(pnlTieuDe, BorderLayout.NORTH);
    }

    private JPanel taoForm() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(QuanLyKhachHang.NAVY),
                BorderFactory.createEmptyBorder(12, 14, 14, 14)));

        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(QuanLyKhachHang.LOCKED);
        txtHoTen = new JTextField();
        cboGioiTinh = new JComboBox<>(new String[] { "Chọn giới tính", "Nam", "Nữ" });
        txtSoDienThoai = new JTextField();
        txtDiaChi = new JTextField();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(3, 6, 3, 6);
        c.weightx = 0.5;

        JLabel lblKhung = taoNhan("Thông tin khách hàng");
        lblKhung.setFont(new Font("Arial", Font.BOLD, 15));
        them(pnlForm, c, 0, 0, 1, lblKhung);
        JLabel lblBatBuoc = taoGhiChu("* Thông tin bắt buộc");
        lblBatBuoc.setHorizontalAlignment(SwingConstants.RIGHT);
        them(pnlForm, c, 0, 1, 1, lblBatBuoc);

        them(pnlForm, c, 1, 0, 1, taoNhan("Mã khách hàng"));
        them(pnlForm, c, 1, 1, 1, taoNhan("Họ tên *"));
        them(pnlForm, c, 2, 0, 1, chinhO(txtMa));
        them(pnlForm, c, 2, 1, 1, chinhO(txtHoTen));
        them(pnlForm, c, 3, 0, 1, taoGhiChu("Mã do hệ thống tạo, không thể chỉnh sửa."));

        them(pnlForm, c, 4, 0, 1, taoNhan("Giới tính *"));
        them(pnlForm, c, 4, 1, 1, taoNhan("Số điện thoại *"));
        them(pnlForm, c, 5, 0, 1, chinhO(cboGioiTinh));
        them(pnlForm, c, 5, 1, 1, chinhO(txtSoDienThoai));

        them(pnlForm, c, 6, 0, 2, taoNhan("Địa chỉ (không bắt buộc)"));
        them(pnlForm, c, 7, 0, 2, chinhO(txtDiaChi));

        c.insets = new Insets(12, 6, 4, 6);
        them(pnlForm, c, 8, 0, 2, new JSeparator());
        c.insets = new Insets(3, 6, 3, 6);
        them(pnlForm, c, 9, 0, 2,
                taoGhiChu("Lưu để thêm khách hàng và quay lại danh sách. Chọn Hủy để quay lại mà không lưu."));

        btnLuu = taoNut("Lưu khách hàng", QuanLyKhachHang.RED, Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnHuy = taoNut("Hủy", Color.WHITE, QuanLyKhachHang.NAVY);
        btnHuy.setBorder(BorderFactory.createLineBorder(QuanLyKhachHang.NAVY));
        btnHuy.setPreferredSize(new Dimension(90, 35));
        btnLuu.addActionListener(this);
        btnHuy.addActionListener(this);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.add(btnLuu);
        pnlNut.add(btnHuy);
        them(pnlForm, c, 10, 0, 2, pnlNut);

        return pnlForm;
    }

    // ===== Hàm dựng giao diện =====

    private void them(JPanel pnl, GridBagConstraints c, int hang, int cot, int rong, JComponent comp) {
        c.gridy = hang;
        c.gridx = cot;
        c.gridwidth = rong;
        pnl.add(comp, c);
        c.gridwidth = 1;
    }

    private JLabel taoNhan(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(QuanLyKhachHang.NAVY);
        return lbl;
    }

    private JLabel taoGhiChu(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    private JComponent chinhO(JComponent input) {
        input.setPreferredSize(new Dimension(200, 32));
        return input;
    }

    private JButton taoNut(String ten, Color mauNen, Color mauChu) {
        JButton btn = new JButton(ten);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(mauNen);
        btn.setForeground(mauChu);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(5, 5, 5, 5));
        return btn;
    }

    // ===== Xử lý =====

    public void chuanBi() {
        txtMa.setText(dao.phatSinhMa());
        txtHoTen.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtSoDienThoai.setText("");
        txtDiaChi.setText("");
        txtHoTen.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnQuayLai || o == btnHuy) {
            onDong.run();
        } else if (o == btnLuu) {
            luu();
        }
    }

    private void luu() {
        String loi = QuanLyKhachHang.kiemTra(
                txtHoTen.getText(),
                cboGioiTinh.getSelectedItem(),
                txtSoDienThoai.getText());
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] ht = QuanLyKhachHang.tachHoTen(txtHoTen.getText());
        KhachHang kh = new KhachHang(
                txtMa.getText(), ht[0], ht[1],
                (String) cboGioiTinh.getSelectedItem(),
                txtSoDienThoai.getText().trim(),
                QuanLyKhachHang.diaChiLuu(txtDiaChi.getText()));
        if (dao.them(kh)) {
            JOptionPane.showMessageDialog(this, "Đã thêm khách hàng " + kh.getMaKhachHang() + ".");
            onDong.run();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
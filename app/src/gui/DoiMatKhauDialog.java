package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import entity.TaiKhoan;
import service.DangNhapService;
import service.QuanLyTaiKhoanService;
import session.UserSession;
import util.AESGCMUtil;

public class DoiMatKhauDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final Color MAU_XANH = new Color(23, 52, 81);
//    private final Color MAU_DO = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_CHU_PHU = new Color(100, 116, 139);

    private JPasswordField txtMatKhauCu;
    private JPasswordField txtMatKhauMoi;
    private JPasswordField txtXacNhanMK;
    private JCheckBox chkHienMK;

    private JButton btnLuu;
    private JButton btnHuy;

    private DangNhapService dangNhapService = new DangNhapService();
    private QuanLyTaiKhoanService taiKhoanService = new QuanLyTaiKhoanService();

    public DoiMatKhauDialog(Frame parent) {
        super(parent, "Đổi mật khẩu", true); // Modal dialog

        setSize(380, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        taoGiaoDien();
    }

    private void taoGiaoDien() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTieuDe = new JLabel("ĐỔI MẬT KHẨU");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
        lblTieuDe.setForeground(MAU_XANH);
        pnlHeader.add(lblTieuDe);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        txtMatKhauCu = new JPasswordField();
        txtMatKhauMoi = new JPasswordField();
        txtXacNhanMK = new JPasswordField();

        pnlCenter.add(taoTruongNhap("Mật khẩu hiện tại:", txtMatKhauCu));
        pnlCenter.add(taoTruongNhap("Mật khẩu mới:", txtMatKhauMoi));
        pnlCenter.add(taoTruongNhap("Xác nhận mật khẩu mới:", txtXacNhanMK));

        chkHienMK = new JCheckBox("Hiển thị mật khẩu");
        chkHienMK.setBackground(Color.WHITE);
        chkHienMK.setFont(new Font("Arial", Font.PLAIN, 12));
        chkHienMK.setForeground(MAU_CHU_PHU);
        chkHienMK.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkHienMK.addActionListener(e -> {
            char echo = chkHienMK.isSelected() ? (char) 0 : '•';
            txtMatKhauCu.setEchoChar(echo);
            txtMatKhauMoi.setEchoChar(echo);
            txtXacNhanMK.setEchoChar(echo);
        });
        pnlCenter.add(chkHienMK);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu thay đổi");
        btnLuu.setFont(new Font("Arial", Font.BOLD, 13));
        btnLuu.setBackground(MAU_XANH);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setPreferredSize(new Dimension(120, 35));
        btnLuu.addActionListener(this);

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Arial", Font.BOLD, 13));
        btnHuy.setBackground(Color.WHITE);
        btnHuy.setForeground(MAU_XANH);
        btnHuy.setFocusPainted(false);
        btnHuy.setPreferredSize(new Dimension(80, 35));
        btnHuy.setBorder(BorderFactory.createLineBorder(MAU_VIEN));
        btnHuy.addActionListener(this);

        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLuu);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private JPanel taoTruongNhap(String nhan, JComponent input) {
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(nhan);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(MAU_XANH);

        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setPreferredSize(new Dimension(0, 32));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        input.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MAU_VIEN),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));

        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(3));
        pnl.add(input);
        pnl.add(Box.createVerticalStrut(8));

        return pnl;
    }

    private void xuLyDoiMatKhau() {
        String matKhauCu = new String(txtMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
        String xacNhanMK = new String(txtXacNhanMK.getPassword()).trim();

        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhanMK.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (matKhauMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có tối thiểu 6 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return;
        }

        if (matKhauMoi.equals(matKhauCu)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không được trùng với mật khẩu hiện tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return;
        }

        if (!matKhauMoi.equals(xacNhanMK)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu mới không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtXacNhanMK.requestFocus();
            return;
        }

        String maNV = UserSession.getMaNhanVien();
        try {
            TaiKhoan tk = dangNhapService.xacThuc(maNV, matKhauCu);
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtMatKhauCu.selectAll();
                txtMatKhauCu.requestFocus();
                return;
            }

            String matKhauMaHoa = AESGCMUtil.maHoaMatKhau(matKhauMoi);

            if (taiKhoanService.doiMatKhau(maNV, matKhauMaHoa)) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLuu) {
            xuLyDoiMatKhau();
        } else if (e.getSource() == btnHuy) {
            dispose();
        }
    }
}
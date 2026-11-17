package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import dao.DangNhapDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import service.DangNhapService;
import session.UserSession;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class DangNhap extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMaNV;
	private JButton btnDangNhap;
	private JPasswordField passwordField;
	private final DangNhapService service = new DangNhapService(new DangNhapDAO());
	public DangNhap() {
		setTitle("Chicago - ĐĂNG NHẬP");
		setSize(1280,800);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel pnlLogo = new JPanel();
		pnlLogo.setBackground(new Color(17, 43, 68));
		pnlLogo.setForeground(new Color(255, 255, 255));
		pnlLogo.setBounds(0, 0, 560, 800);
		getContentPane().add(pnlLogo);
		pnlLogo.setLayout(null);
		
		JLabel lblNewLabel = new JLabel();
		ImageIcon icon = new ImageIcon(DangNhap.class.getResource("/image/LogoNenXanh.png"));
		Image img = icon.getImage().getScaledInstance(420, 368, Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(70, 201, 420, 368);
		pnlLogo.add(lblNewLabel);
		
		JLabel lblTenHeThong = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
		lblTenHeThong.setForeground(UIManager.getColor("Button.disabledShadow"));
		lblTenHeThong.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTenHeThong.setBounds(155, 593, 255, 20);
		pnlLogo.add(lblTenHeThong);
		
		JPanel pnlDangNhap = new JPanel();
		pnlDangNhap.setBackground(new Color(250, 247, 240));
		pnlDangNhap.setBounds(560, 0, 720, 800);
		getContentPane().add(pnlDangNhap);
		pnlDangNhap.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(17, 43, 68), 2, true));
		panel.setBackground(new Color(250, 247, 240));
		panel.setBounds(140, 236, 440, 327);
		pnlDangNhap.add(panel);
		panel.setLayout(null);
		
		JLabel lblTieuDe = new JLabel("CỬA HÀNG TIỆN LỢI");
		lblTieuDe.setBounds(10, 17, 132, 16);
		lblTieuDe.setForeground(new Color(255, 0, 0));
		lblTieuDe.setVerticalAlignment(SwingConstants.TOP);
		lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblTieuDe);
		
		JLabel lblDangNhap = new JLabel("ĐĂNG NHẬP");
		lblDangNhap.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblDangNhap.setBounds(10, 47, 430, 33);
		panel.add(lblDangNhap);
		
		JLabel lblMoTa = new JLabel("Dùng mã nhân viên và mật khẩu được cấp để vào hệ thống.");
		lblMoTa.setBounds(10, 93, 430, 20);
		panel.add(lblMoTa);
		
		JLabel lblMaNV = new JLabel("Mã nhân viên");
		lblMaNV.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMaNV.setBounds(10, 127, 430, 16);
		panel.add(lblMaNV);
		
		txtMaNV = new JTextField();
		txtMaNV.setBounds(10, 149, 420, 32);
		panel.add(txtMaNV);
		txtMaNV.setColumns(10);
		
		JLabel lblMatKhau = new JLabel("Mật khẩu");
		lblMatKhau.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMatKhau.setBounds(10, 195, 430, 16);
		panel.add(lblMatKhau);
		
		JCheckBox chckHienMK = new JCheckBox("Hiện mật khẩu");
		chckHienMK.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckHienMK.setBounds(10, 259, 124, 20);
		panel.add(chckHienMK);
		
		btnDangNhap = new JButton("ĐĂNG NHẬP");
		btnDangNhap.setBackground(new Color(255, 0, 0));
		btnDangNhap.setForeground(new Color(255, 255, 255));
		btnDangNhap.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnDangNhap.setBounds(10, 285, 420, 32);
		btnDangNhap.setOpaque(true);
		btnDangNhap.setContentAreaFilled(true);
		btnDangNhap.setBorderPainted(false);
		panel.add(btnDangNhap);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 221, 420, 32);
		panel.add(passwordField);
		
		// Xử lý ẩn / hiện mật khẩu
		chckHienMK.addActionListener(e -> {
			if (chckHienMK.isSelected()) {
				passwordField.setEchoChar((char) 0);
			} else {
				passwordField.setEchoChar('•');
			}
		});

		btnDangNhap.addActionListener(this);
		passwordField.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			TaiKhoan taiKhoan = service.xacThuc(txtMaNV.getText().trim(), new String(passwordField.getPassword()));
			NhanVien nv = taiKhoan.getNhanVien();
			UserSession.dangNhap(nv.getMaNhanVien(), nv.getHo() + " " + nv.getTen(), taiKhoan.getVaiTro());
			dispose();
			new FrmChinh().setVisible(true);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Đăng nhập", JOptionPane.WARNING_MESSAGE);
			passwordField.selectAll();
			passwordField.requestFocusInWindow();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Không kết nối được cơ sở dữ liệu.",
					"Đăng nhập", JOptionPane.ERROR_MESSAGE);
		}
	}
}

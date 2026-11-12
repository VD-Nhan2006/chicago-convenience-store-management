package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entity.Quyen;
import entity.VaiTro;
import service.PhanQuyenService;
import session.UserSession;

public class FrmChinh extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Color MAU_NEN_MENU = new Color(17, 43, 68);
    private final Color MAU_CHU = Color.WHITE;
    private final Color MAU_NUT_CHON = new Color(239, 77, 59);
    private final Color MAU_VANG = new Color(255, 190, 60);
	private JPanel pnlMenu;
	private JPanel pnlLogo;
	private JPanel pnlDanhSachMenu;
	
	private JPanel pnlNoiDung;
	private CardLayout cardLayout;
	
	private Map<JButton, String> danhSachMenu;
	private JButton btnDashboard;
	private JButton btnBanHang;
	private JButton btnTimKiemHoaDon;
	private JButton btnQuanLyKhachHang;
	private JButton btnQuanLySanPham;
	private JButton btnQuanLyLoaiSanPham;
	private JButton btnQuanLyNhanVien;
	private JButton btnQuanLyTaiKhoan;
	private JButton btnQuanLyKhuyenMai;
	private JButton btnThongKe;
	private JButton btnDangChon;
	private JPanel pnlThongTinTaiKhoan;
	private JLabel lblTitle;
	private JLabel lblTenNhanVien;
	private JLabel lblVaiTro;
	private JButton btnDangXuat;
	private JButton btnDoiMatKhau;
    
    public FrmChinh() {
    	setTitle("Chicago - Hệ thống quản lý");
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setLayout(new BorderLayout());
    	
        danhSachMenu = new LinkedHashMap<JButton, String>();
        
    	pnlMenu = new JPanel();
    	pnlMenu.setPreferredSize(new Dimension(248, 800));
        pnlMenu.setBackground(MAU_NEN_MENU);
        pnlMenu.setLayout(new BorderLayout());
        
        cardLayout = new CardLayout();
        pnlNoiDung = new JPanel(cardLayout);
        pnlNoiDung.setBackground(MAU_CHU);

        
        taoLogo();
        taoDanhSachMenu();
        taoThongTinTaiKhoan();
        
		
		pnlMenu.add(pnlLogo, BorderLayout.NORTH);
		pnlMenu.add(pnlDanhSachMenu, BorderLayout.CENTER);
		pnlMenu.add(pnlThongTinTaiKhoan, BorderLayout.SOUTH);
        
        add(pnlMenu, BorderLayout.WEST);
        add(pnlNoiDung, BorderLayout.CENTER);
        taoCardManHinh();
        
        btnDangXuat.addActionListener(this);
        btnDashboard.addActionListener(this);
        btnBanHang.addActionListener(this);
        btnTimKiemHoaDon.addActionListener(this);
        btnQuanLyKhachHang.addActionListener(this);
        btnQuanLyNhanVien.addActionListener(this);
        btnQuanLyKhuyenMai.addActionListener(this);
        btnQuanLyLoaiSanPham.addActionListener(this);
        btnQuanLySanPham.addActionListener(this);
        btnQuanLyTaiKhoan.addActionListener(this);
        btnThongKe.addActionListener(this);
        btnDoiMatKhau.addActionListener(this);
        
        phanQuyenMenu();
    }
    
    private void phanQuyenMenu() {
    	VaiTro vaiTro = UserSession.getVaiTro();
    	
    	if(vaiTro == null) {
    		return;
    	}
    	btnDashboard.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.DASHBOARD)
    	    );

    	    btnBanHang.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.BAN_HANG)
    	    );

    	    btnTimKiemHoaDon.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.TIM_KIEM_HOA_DON)
    	    );

    	    btnQuanLyKhachHang.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_KHACH_HANG)
    	    );

    	    btnQuanLySanPham.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_SAN_PHAM)
    	    );

    	    btnQuanLyLoaiSanPham.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_LOAI_SAN_PHAM)
    	    );

    	    btnQuanLyNhanVien.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_NHAN_VIEN)
    	    );

    	    btnQuanLyTaiKhoan.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_TAI_KHOAN)
    	    );

    	    btnQuanLyKhuyenMai.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.QUAN_LY_KHUYEN_MAI)
    	    );

    	    btnThongKe.setVisible(
    	        PhanQuyenService.coQuyen(vaiTro, Quyen.THONG_KE)
    	    );
    	}
		

	private void taoDanhSachMenu() {
		pnlDanhSachMenu = new JPanel();
		pnlDanhSachMenu.setBackground(MAU_NEN_MENU);
		pnlDanhSachMenu.setLayout(new BoxLayout(pnlDanhSachMenu, BoxLayout.Y_AXIS));
		
		btnDashboard = taoNutMenu("Dashboard", "DASHBOARD");
		btnBanHang = taoNutMenu("Bán hàng", "BAN_HANG");
        btnTimKiemHoaDon = taoNutMenu(
                "Tìm kiếm hóa đơn", "TIM_KIEM_HOA_DON");
        btnQuanLyKhachHang = taoNutMenu(
                "Quản lý khách hàng", "KHACH_HANG");
        btnQuanLySanPham = taoNutMenu(
                "Quản lý sản phẩm", "SAN_PHAM");
        btnQuanLyLoaiSanPham = taoNutMenu(
                "Quản lý loại sản phẩm", "LOAI_SAN_PHAM");
        btnQuanLyNhanVien = taoNutMenu(
                "Quản lý nhân viên", "NHAN_VIEN");
        btnQuanLyTaiKhoan = taoNutMenu(
                "Quản lý tài khoản", "TAI_KHOAN");
        btnQuanLyKhuyenMai = taoNutMenu(
                "Quản lý khuyến mãi", "KHUYEN_MAI");
        btnThongKe = taoNutMenu(
                "Thống kê", "THONG_KE");
        
        btnDangChon = btnDashboard;
        capNhatNutDangChon(btnDangChon);
        
		
	}

	private void taoLogo() {
    	pnlLogo = new JPanel();
        pnlLogo.setBackground(MAU_NEN_MENU);
        pnlLogo.setPreferredSize(new Dimension(248, 200));
        pnlLogo.setLayout(new BoxLayout(pnlLogo, BoxLayout.Y_AXIS));
        
        JLabel lblLogo = new JLabel();
        ImageIcon icon = new ImageIcon(FrmChinh.class.getResource("/image/LogoNenTrang.png"));
		Image img = icon.getImage().getScaledInstance(216, 150, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(img));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlLogo.add(Box.createVerticalStrut(16));
		pnlLogo.add(lblLogo);
    }
	
	private JButton taoNutMenu(String tenNut, String maManHinh) {

        JButton btn = new JButton(tenNut);

        btn.setPreferredSize(new Dimension(248, 40));

        btn.setMaximumSize(new Dimension(248, 40));

        btn.setMinimumSize(new Dimension(248, 40));

        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setFont(new Font("Arial", Font.PLAIN, 14));

        btn.setForeground(MAU_CHU);

        btn.setBackground(MAU_NEN_MENU);

        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        btn.setFocusPainted(false);

        btn.setOpaque(true);

        btn.setContentAreaFilled(true);

        danhSachMenu.put(btn, maManHinh);

        pnlDanhSachMenu.add(btn);

        return btn;
    }
	
	private void capNhatNutDangChon(JButton btnMoi) {

        if (btnDangChon != null) {

            btnDangChon.setBackground(MAU_NEN_MENU);
        }

        btnDangChon = btnMoi;

        btnDangChon.setBackground(MAU_NUT_CHON);
    }
    
	
	private void taoThongTinTaiKhoan() {

        pnlThongTinTaiKhoan = new JPanel();

        pnlThongTinTaiKhoan.setBackground(MAU_NEN_MENU);

        pnlThongTinTaiKhoan.setLayout(new BoxLayout(
                pnlThongTinTaiKhoan, BoxLayout.Y_AXIS));

        pnlThongTinTaiKhoan.setBorder(
                BorderFactory.createEmptyBorder(10, 16, 15, 10));

        lblTitle = new JLabel("Xin chào, ");

        lblTitle.setForeground(new Color(200, 210, 220));

        lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));

        lblTenNhanVien = new JLabel();

        lblTenNhanVien.setForeground(Color.WHITE);

        lblTenNhanVien.setFont(new Font("Arial", Font.BOLD, 14));

        lblVaiTro = new JLabel();

        lblVaiTro.setForeground(MAU_VANG);

        lblVaiTro.setFont(new Font("Arial", Font.PLAIN, 12));
        
        btnDoiMatKhau = new JButton("Quên mật khẩu");

        btnDoiMatKhau.setForeground(Color.WHITE);
        btnDoiMatKhau.setBackground(MAU_NEN_MENU);
        btnDoiMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.setBorderPainted(false);
        btnDoiMatKhau.setContentAreaFilled(false);

        btnDangXuat = new JButton("Đăng xuất");

        btnDangXuat.setForeground(Color.WHITE);

        btnDangXuat.setBackground(MAU_NEN_MENU);

        btnDangXuat.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnDangXuat.setFocusPainted(false);

        btnDangXuat.setBorderPainted(false);

        pnlThongTinTaiKhoan.add(lblTitle);

        pnlThongTinTaiKhoan.add(Box.createVerticalStrut(5));

        pnlThongTinTaiKhoan.add(lblTenNhanVien);

        pnlThongTinTaiKhoan.add(Box.createVerticalStrut(3));

        pnlThongTinTaiKhoan.add(lblVaiTro);

        pnlThongTinTaiKhoan.add(Box.createVerticalStrut(10));
        
        pnlThongTinTaiKhoan.add(btnDoiMatKhau);
        pnlThongTinTaiKhoan.add(Box.createVerticalStrut(5));

        pnlThongTinTaiKhoan.add(btnDangXuat);

        hienThiThongTinTaiKhoan();
    }

    private void hienThiThongTinTaiKhoan() {


        lblTenNhanVien.setText(UserSession.getTenDangNhap());

        lblVaiTro.setText(UserSession.getVaiTro().getTenHienThi());
//        lblVaiTro.setText("Quan ly");
    }
    
    private void taoCardManHinh() {
    	pnlNoiDung.add(new QuanLyNhanVien(), "NHAN_VIEN");
        pnlNoiDung.add(new BanHang(), "BAN_HANG");
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		
		
		if(btn == btnDangXuat) {
			int choice = JOptionPane.showConfirmDialog(this, 
					"Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {
				UserSession.dangXuat();
				dispose();
				new DangNhap().setVisible(true);
			}
			return;			
		}
		capNhatNutDangChon(btn);
		String maManHinh = danhSachMenu.get(btn);
		if(maManHinh != null) {
			cardLayout.show(pnlNoiDung, maManHinh);
		}
		
	}
    
}

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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entity.NhanVien;
import entity.TaiKhoan;
import entity.VaiTro;
import service.QuanLyNhanVienService;
import service.QuanLyTaiKhoanService;
import session.UserSession;
import util.AESGCMUtil;

public class QuanLyTaiKhoan extends JPanel implements ActionListener, ListSelectionListener {

    private static final long serialVersionUID = 1L;

    // Bảng màu
    private final Color MAU_NEN = new Color(250, 247, 240);
    private final Color MAU_XANH = new Color(23, 52, 81);
    private final Color MAU_DO = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_XAM = new Color(230, 237, 245);

    // Bảng tài khoản
    private JTable tblTaiKhoan;
    private DefaultTableModel tableModel;

    // Form thông tin
    private JComboBox<String> cboMaNhanVien;
    private JTextField txtHoTen;
    private JPasswordField txtMatKhauHienTai;
    private JCheckBox chkHienMKHienTai;
    private JPasswordField txtMatKhauMoi;
    private JCheckBox chkHienMKMoi;

    // Các nút
    private JButton btnThem;
    private JButton btnDoiMatKhau;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private QuanLyTaiKhoanService taiKhoanService = new QuanLyTaiKhoanService();
    private QuanLyNhanVienService nhanVienService = new QuanLyNhanVienService();

    private ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<>();
    private ArrayList<NhanVien> dsNhanVien = new ArrayList<>();

    public QuanLyTaiKhoan() {
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        taoNoiDung();
        loadData();


    }

    // Tạo vùng nội dung: Bảng ở giữa, Form bên phải
    private void taoNoiDung() {
        JPanel pnlNoiDung = new JPanel(new BorderLayout(12, 0));
        pnlNoiDung.setBackground(MAU_NEN);

        pnlNoiDung.add(taoBangTaiKhoan(), BorderLayout.CENTER);
        pnlNoiDung.add(taoFormTaiKhoan(), BorderLayout.EAST);

        add(pnlNoiDung, BorderLayout.CENTER);
    }

    // Tạo bảng danh sách tài khoản (không có cột Mật khẩu)
    private JScrollPane taoBangTaiKhoan() {
        String[] tieuDe = { "Mã NV", "Họ tên", "Vai trò", "Trạng thái" };

        tableModel = new DefaultTableModel(tieuDe, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiKhoan = new JTable(tableModel);
        tblTaiKhoan.setRowHeight(30);
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 13));
        tblTaiKhoan.setForeground(MAU_XANH);
        tblTaiKhoan.setSelectionBackground(MAU_XAM);
        tblTaiKhoan.setSelectionForeground(MAU_XANH);
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setFillsViewportHeight(true);

        tblTaiKhoan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblTaiKhoan.getTableHeader().setBackground(MAU_XANH);
        tblTaiKhoan.getTableHeader().setForeground(Color.WHITE);
        tblTaiKhoan.getTableHeader().setPreferredSize(new Dimension(0, 34));

        tblTaiKhoan.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        scrollPane.setBorder(BorderFactory.createLineBorder(MAU_VIEN));
        return scrollPane;
    }

    // Tạo form thông tin tài khoản bên phải
    private JPanel taoFormTaiKhoan() {
        JPanel pnlForm = new JPanel();
        pnlForm.setPreferredSize(new Dimension(320, 0));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel lblTieuDe = new JLabel("Thông tin tài khoản");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 17));
        lblTieuDe.setForeground(MAU_XANH);
        pnlForm.add(lblTieuDe);
        pnlForm.add(Box.createVerticalStrut(10));

        // 1. Mã nhân viên (gồm tất cả nhân viên để có thể chọn thêm tài khoản mới)
        cboMaNhanVien = new JComboBox<>();
        cboMaNhanVien.setBackground(Color.WHITE);
        cboMaNhanVien.addActionListener(this);
        pnlForm.add(taoTruongNhap("Mã nhân viên", cboMaNhanVien));

        // 2. Họ tên
        txtHoTen = new JTextField();
        txtHoTen.setEditable(false);
        txtHoTen.setBackground(MAU_XAM);
        pnlForm.add(taoTruongNhap("Họ tên", txtHoTen));

        // 3. Mật khẩu hiện tại
        txtMatKhauHienTai = new JPasswordField();
        txtMatKhauHienTai.setEditable(false);
        txtMatKhauHienTai.setBackground(MAU_XAM);
        pnlForm.add(taoTruongNhap("Mật khẩu hiện tại", txtMatKhauHienTai));

        // Checkbox hiển thị mật khẩu hiện tại
        chkHienMKHienTai = new JCheckBox("Hiển thị mật khẩu");
        chkHienMKHienTai.setBackground(Color.WHITE);
        chkHienMKHienTai.setFont(new Font("Arial", Font.PLAIN, 12));
        chkHienMKHienTai.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkHienMKHienTai.addActionListener(e -> {
            txtMatKhauHienTai.setEchoChar(chkHienMKHienTai.isSelected() ? (char) 0 : '•');
        });
        pnlForm.add(chkHienMKHienTai);
        pnlForm.add(Box.createVerticalStrut(8));

        // 4. Mật khẩu mới
        txtMatKhauMoi = new JPasswordField();
        pnlForm.add(taoTruongNhap("Mật khẩu mới", txtMatKhauMoi));

        // Checkbox hiển thị mật khẩu mới
        chkHienMKMoi = new JCheckBox("Hiển thị mật khẩu");
        chkHienMKMoi.setBackground(Color.WHITE);
        chkHienMKMoi.setFont(new Font("Arial", Font.PLAIN, 12));
        chkHienMKMoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkHienMKMoi.addActionListener(e -> {
            txtMatKhauMoi.setEchoChar(chkHienMKMoi.isSelected() ? (char) 0 : '•');
        });
        pnlForm.add(chkHienMKMoi);
        pnlForm.add(Box.createVerticalStrut(12));

        // 4 nút chức năng xếp dạng lưới 2x2 giống QuanLyNhanVien
        taoCacNut();
        JPanel pnlNut = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlNut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        pnlNut.add(btnThem);
        pnlNut.add(btnDoiMatKhau);
        pnlNut.add(btnXoa);
        pnlNut.add(btnLamMoi);

        pnlForm.add(pnlNut);
        pnlForm.add(Box.createVerticalGlue());

        return pnlForm;
    }

    private JPanel taoTruongNhap(String ten, JComponent input) {
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(ten);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(MAU_XANH);

        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setPreferredSize(new Dimension(0, 32));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(3));
        pnl.add(input);
        pnl.add(Box.createVerticalStrut(8));

        return pnl;
    }

    private void taoCacNut() {
        btnThem = taoNut("Thêm", MAU_DO, Color.WHITE);
        btnDoiMatKhau = taoNut("Đổi mật khẩu", MAU_XANH, Color.WHITE);
        btnXoa = taoNut("Xóa", Color.WHITE, MAU_DO);
        btnLamMoi = taoNut("Làm mới", Color.WHITE, MAU_XANH);

        btnXoa.setBorder(BorderFactory.createLineBorder(MAU_DO));
        btnLamMoi.setBorder(BorderFactory.createLineBorder(MAU_XANH));

        btnThem.addActionListener(this);
        btnDoiMatKhau.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
    }

    private JButton taoNut(String ten, Color mauNen, Color mauChu) {
        JButton btn = new JButton(ten);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(mauNen);
        btn.setForeground(mauChu);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setMargin(new Insets(5, 5, 5, 5));
        return btn;
    }

    // =====================================================================
    // NẠP DỮ LIỆU
    // =====================================================================

    public void loadData() {
        tableModel.setRowCount(0);
        cboMaNhanVien.removeActionListener(this);
        cboMaNhanVien.removeAllItems();

        lamMoiForm();

        // 1. Nạp danh sách tài khoản lên bảng
        dsTaiKhoan = taiKhoanService.getAllTaiKhoan();
        if (dsTaiKhoan != null) {
            for (TaiKhoan tk : dsTaiKhoan) {
                tableModel.addRow(new Object[] {
                    tk.getNhanVien().getMaNhanVien(),
                    tk.getNhanVien().getHo() + " " + tk.getNhanVien().getTen(),
                    tk.getVaiTro(),
                    tk.getTrangThai()
                });
            }
        }

        // 2. Nạp TOÀN BỘ nhân viên vào combobox (kể cả người chưa có tài khoản)
        dsNhanVien = nhanVienService.getAllNhanVien();
        if (dsNhanVien != null) {
            for (NhanVien nv : dsNhanVien) {
                cboMaNhanVien.addItem(nv.getMaNhanVien());
            }
        }

        cboMaNhanVien.setSelectedIndex(-1);
        cboMaNhanVien.addActionListener(this);
    }

    private void lamMoiForm() {
        txtHoTen.setText("");
        txtMatKhauHienTai.setText("");
        txtMatKhauMoi.setText("");
        chkHienMKHienTai.setSelected(false);
        chkHienMKMoi.setSelected(false);
        txtMatKhauHienTai.setEchoChar('•');
        txtMatKhauMoi.setEchoChar('•');
    }

    // Đưa dữ liệu dòng được chọn trên bảng lên form
    private void hienThiThongTin(int row) {
        if (row < 0 || row >= dsTaiKhoan.size()) {
            return;
        }

        TaiKhoan tk = dsTaiKhoan.get(row);

        cboMaNhanVien.removeActionListener(this);
        cboMaNhanVien.setSelectedItem(tk.getNhanVien().getMaNhanVien());
        cboMaNhanVien.addActionListener(this);

        txtHoTen.setText(tk.getNhanVien().getHo() + " " + tk.getNhanVien().getTen());

        // Giải mã mật khẩu hiển thị vào ô Mật khẩu hiện tại
        try {
            txtMatKhauHienTai.setText(AESGCMUtil.giaiMa(tk.getMatKhau()));
        } catch (Exception ex) {
            txtMatKhauHienTai.setText(tk.getMatKhau());
        }

        txtMatKhauMoi.setText("");
        chkHienMKHienTai.setSelected(false);
        chkHienMKMoi.setSelected(false);
        txtMatKhauHienTai.setEchoChar('•');
        txtMatKhauMoi.setEchoChar('•');
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = tblTaiKhoan.getSelectedRow();
            if (row != -1) {
                hienThiThongTin(row);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();

        if (nguon == btnLamMoi) {
            tblTaiKhoan.clearSelection();
            loadData();
        } else if (nguon == cboMaNhanVien) {
            Object selected = cboMaNhanVien.getSelectedItem();
            if (selected == null) {
                lamMoiForm();
                return;
            }

            String maNVChon = selected.toString();

            // 1. Tìm họ tên từ danh sách toàn bộ nhân viên
            for (NhanVien nv : dsNhanVien) {
                if (maNVChon.equals(nv.getMaNhanVien())) {
                    txtHoTen.setText(nv.getHo() + " " + nv.getTen());
                    break;
                }
            }

            // 2. Kiểm tra xem nhân viên này đã có tài khoản trên bảng chưa
            boolean daCoTaiKhoan = false;
            for (int i = 0; i < dsTaiKhoan.size(); i++) {
                TaiKhoan tk = dsTaiKhoan.get(i);
                if (maNVChon.equals(tk.getNhanVien().getMaNhanVien())) {
                    tblTaiKhoan.setRowSelectionInterval(i, i);
                    daCoTaiKhoan = true;
                    break;
                }
            }

            // Nếu nhân viên này chưa có tài khoản: bỏ chọn bảng, để trống mật khẩu hiện tại
            if (!daCoTaiKhoan) {
                tblTaiKhoan.clearSelection();
                txtMatKhauHienTai.setText("");
                txtMatKhauMoi.setText("");
                chkHienMKHienTai.setSelected(false);
                chkHienMKMoi.setSelected(false);
            }
        }else if(nguon == btnThem) {
        	themTaiKhoan();
        }else if(nguon == btnDoiMatKhau) {
        	doiMatKhau();
        }else if(nguon == btnXoa) {
        	xoaTaiKhoan();
        }
    }

	private void doiMatKhau() {
		Object selectedNhanVien = cboMaNhanVien.getSelectedItem();
		if (selectedNhanVien == null) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần đổi mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        cboMaNhanVien.requestFocus();
	        return;
	    }
		String maNV = selectedNhanVien.toString();
		boolean daCoTaiKhoan =  false;
		for(TaiKhoan tk : dsTaiKhoan) {
			if(maNV.equals(tk.getNhanVien().getMaNhanVien())) {
				daCoTaiKhoan = true;
				break;
			}
		}
		
		if(!daCoTaiKhoan) {
			JOptionPane.showMessageDialog(this, "Nhân viên này chưa có tài khoản! Hãy dùng nút \"Thêm\".", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        return;
		}
		
		String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
		if(matKhauMoi.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return;
		}
		if(matKhauMoi.length()<6) {
			JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có độ dài tối thiểu 6 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return;
		}
		String matKhauHienTai = new String(txtMatKhauHienTai.getPassword()).trim();
		if(matKhauMoi.equals(matKhauHienTai)) {
			JOptionPane.showMessageDialog(this, "Mật khẩu mới không được trùng với mật khẩu hiện tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return;
		}
		int xacNhan = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đổi mật khẩu cho nhân viên " + maNV + " không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
		if(xacNhan != JOptionPane.YES_OPTION) {
			return;
		}
		String matKhauMaHoa;
		try {
			matKhauMaHoa = AESGCMUtil.maHoaMatKhau(matKhauMoi);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi mã hóa mật khẩu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return;
		}
		
		if(taiKhoanService.doiMatKhau(maNV, matKhauMaHoa)) {
			JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        loadData();
	        cboMaNhanVien.setSelectedItem(maNV);
		}else {
			JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void themTaiKhoan() {
		Object selectedNV = cboMaNhanVien.getSelectedItem();
		if(selectedNV == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn mã nhân viên cần cấp tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			cboMaNhanVien.requestFocus();
			return;
		}
		String maNV = selectedNV.toString();
		
		for(TaiKhoan tk : dsTaiKhoan) {
			if(maNV.equals(tk.getNhanVien().getMaNhanVien())) {
				JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản rồi! Hãy dùng nút Đổi mật khẩu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				txtMatKhauMoi.requestFocus();
				return;
			}
		}
		String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
		if(matKhauMoi.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return;
		}
		if(matKhauMoi.length()<6) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có độ dài tối thiểu 6 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        txtMatKhauMoi.requestFocus();
	        return;
		}
		
		String matKhauDaMaHoa;
		try {
			matKhauDaMaHoa = AESGCMUtil.maHoaMatKhau(matKhauMoi);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi mã hóa mật khẩu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return;
		}
		
		NhanVien nv =new NhanVien(maNV);
		VaiTro vaiTroMacDinh = VaiTro.THU_NGAN;
		String trangThaiMacDinh = "Đang hoạt động";
		TaiKhoan tkMoi = new TaiKhoan(nv, matKhauDaMaHoa, vaiTroMacDinh, trangThaiMacDinh);
		
		if(taiKhoanService.themTaiKhoan(tkMoi)) {
			JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        loadData();
	        cboMaNhanVien.setSelectedItem(maNV);
		}else {
			JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xoaTaiKhoan() {
		Object selectedNV = cboMaNhanVien.getSelectedItem();
		if (selectedNV == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			cboMaNhanVien.requestFocus();
			return;
		}
		String maNV = selectedNV.toString();

		// Kiểm tra xem nhân viên đã có tài khoản trong hệ thống chưa
		TaiKhoan taiKhoanChon = null;
		for (TaiKhoan tk : dsTaiKhoan) {
			if (maNV.equals(tk.getNhanVien().getMaNhanVien())) {
				taiKhoanChon = tk;
				break;
			}
		}

		if (taiKhoanChon == null) {
			JOptionPane.showMessageDialog(this, "Nhân viên này chưa có tài khoản trong hệ thống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Không cho phép xóa (ngừng hoạt động) tài khoản đang đăng nhập hiện tại
		if (maNV.equals(UserSession.getMaNhanVien())) {
			JOptionPane.showMessageDialog(this, "Không thể ngừng hoạt động tài khoản đang đăng nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Kiểm tra nếu tài khoản đã ngừng hoạt động rồi
		if ("Ngừng hoạt động".equals(taiKhoanChon.getTrangThai())) {
			JOptionPane.showMessageDialog(this, "Tài khoản này đã ở trạng thái 'Ngừng hoạt động'!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int xacNhan = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn chuyển tài khoản " + maNV + " sang trạng thái 'Ngừng hoạt động'?",
				"Xác nhận xóa tài khoản", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (xacNhan != JOptionPane.YES_OPTION) {
			return;
		}

		if (taiKhoanService.xoaTaiKhoan(maNV)) {
			JOptionPane.showMessageDialog(this, "Đã chuyển tài khoản " + maNV + " sang trạng thái 'Ngừng hoạt động' thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			loadData();
			cboMaNhanVien.setSelectedItem(maNV);
		} else {
			JOptionPane.showMessageDialog(this, "Chuyển trạng thái tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}

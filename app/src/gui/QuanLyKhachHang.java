	package gui;
	
	import java.awt.BorderLayout;
	import java.awt.Color;
	import java.awt.Component;
	import java.awt.Dimension;
	import java.awt.FlowLayout;
	import java.awt.Font;
	import java.awt.GridLayout;
	import java.awt.Insets;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.ComponentAdapter;
	import java.awt.event.ComponentEvent;
	import java.sql.SQLException;
	import java.util.ArrayList;
	
	import javax.swing.BorderFactory;
	import javax.swing.Box;
	import javax.swing.BoxLayout;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JComponent;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	
	import javax.swing.JTextField;
	import javax.swing.ListSelectionModel;
	import javax.swing.event.ListSelectionEvent;
	import javax.swing.event.ListSelectionListener;
	import javax.swing.table.DefaultTableCellRenderer;
	import javax.swing.table.DefaultTableModel;
	
	
	import service.QuanLyKhachHangService;
	import entity.KhachHang;
	
	public class QuanLyKhachHang extends JPanel implements ActionListener, ListSelectionListener {
	
		private static final long serialVersionUID = 1L;
	
		static final Color NAVY = new Color(23, 52, 81);
		static final Color RED = new Color(239, 77, 59);
		static final Color BG = new Color(250, 247, 240);
		static final Color SELECT = new Color(230, 237, 245);
		static final Color LOCKED = new Color(230, 237, 245);
	
		private static final String DIA_CHI_MAC_DINH = "Chưa cập nhật";
	
		private final QuanLyKhachHangService service = new QuanLyKhachHangService();
		private JPanel pnlDanhSach;
		private ArrayList<KhachHang> dsHienTai = new ArrayList<>();
	
		private JTextField txtTim;
		private JButton btnTim;
	
		private JTable tblKhachHang;
		private DefaultTableModel tableModel;
		private JTextField txtMa;
		private JTextField txtHoTen;
		private JComboBox<String> cboGioiTinh;
		private JTextField txtSoDienThoai;
		private JTextField txtDiaChi;
	
		private JButton btnThem;
		private JButton btnCapNhat;
		private JButton btnLamMoi;
		private JButton btnXoa;
	
		public QuanLyKhachHang() {
	
			    setLayout(new BorderLayout());
	
			    pnlDanhSach = new JPanel(new BorderLayout(12, 12));
			    pnlDanhSach.setBackground(BG);
			    pnlDanhSach.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
	
			    taoTieuDe();
			    taoNoiDung();
	
			    add(pnlDanhSach, BorderLayout.CENTER);
	
			    taiLai();
	
			    addComponentListener(new ComponentAdapter() {
			        @Override
			        public void componentShown(ComponentEvent e) {
			            lamMoi();
			        }
			    });
			}
	
	
	
		private void taoTieuDe() {
			JPanel pnlTieuDe = new JPanel(new BorderLayout());
			pnlTieuDe.setBackground(BG);
	
			JPanel pnlTrai = new JPanel();
			pnlTrai.setLayout(new BoxLayout(pnlTrai, BoxLayout.Y_AXIS));
			pnlTrai.setBackground(BG);
	
			JLabel lblTieuDe = new JLabel("Quản lý khách hàng");
			lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
			lblTieuDe.setForeground(NAVY);
	
			JLabel lblMoTa = new JLabel("Thêm mới, cập nhật và tra cứu theo mã hoặc số điện thoại.");
			lblMoTa.setFont(new Font("Arial", Font.PLAIN, 12));
			lblMoTa.setForeground(Color.GRAY);
	
			pnlTrai.add(lblTieuDe);
			pnlTrai.add(lblMoTa);
	
			JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
			pnlTim.setBackground(BG);
	
			JLabel lblTim = new JLabel("Tìm");
			lblTim.setFont(new Font("Arial", Font.BOLD, 13));
			lblTim.setForeground(NAVY);
	
			txtTim = new JTextField();
			txtTim.setToolTipText("Mã khách hàng hoặc số điện thoại");
			txtTim.setPreferredSize(new Dimension(260, 32));
			txtTim.addActionListener(this);
	
			btnTim = taoNut("Tìm", NAVY, Color.WHITE);
			btnTim.setPreferredSize(new Dimension(80, 32));
	
			pnlTim.add(lblTim);
			pnlTim.add(txtTim);
			pnlTim.add(btnTim);
	
			pnlTieuDe.add(pnlTrai, BorderLayout.WEST);
			pnlTieuDe.add(pnlTim, BorderLayout.EAST);
	
			pnlDanhSach.add(pnlTieuDe, BorderLayout.NORTH);
		}
	
	
		private void taoNoiDung() {
			JPanel pnlNoiDung = new JPanel(new BorderLayout(12, 0));
			pnlNoiDung.setBackground(BG);
	
			pnlNoiDung.add(taoBangKhachHang(), BorderLayout.CENTER);
			pnlNoiDung.add(taoFormKhachHang(), BorderLayout.EAST);
	
			pnlDanhSach.add(pnlNoiDung, BorderLayout.CENTER);
		}
	
	
		private JScrollPane taoBangKhachHang() {
			String[] tieuDe = { "Mã KH", "Họ tên", "Giới tính", "Số điện thoại", "Địa chỉ" };
	
			tableModel = new DefaultTableModel(tieuDe, 0) {
				private static final long serialVersionUID = 1L;
	
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
	
			tblKhachHang = new JTable(tableModel);
			tblKhachHang.setRowHeight(30);
			tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 13));
			tblKhachHang.setForeground(NAVY);
			tblKhachHang.setSelectionBackground(SELECT);
			tblKhachHang.setSelectionForeground(NAVY);
			tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblKhachHang.setFillsViewportHeight(true);
	
			tblKhachHang.getTableHeader().setReorderingAllowed(false);
			tblKhachHang.getTableHeader().setPreferredSize(new Dimension(0, 34));
			tblKhachHang.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r,
						int c) {
					JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, false, false, r, c);
					lbl.setBackground(NAVY);
					lbl.setForeground(Color.WHITE);
					lbl.setFont(new Font("Arial", Font.BOLD, 13));
					lbl.setOpaque(true);
					return lbl;
				}
			});
	
			tblKhachHang.getColumnModel().getColumn(0).setPreferredWidth(110);
			tblKhachHang.getColumnModel().getColumn(1).setPreferredWidth(170);
			tblKhachHang.getColumnModel().getColumn(2).setPreferredWidth(70);
			tblKhachHang.getColumnModel().getColumn(3).setPreferredWidth(110);
			tblKhachHang.getColumnModel().getColumn(4).setPreferredWidth(260);
	
	
			tblKhachHang.getSelectionModel().addListSelectionListener(this);
	
			JScrollPane scrollPane = new JScrollPane(tblKhachHang);
			scrollPane.setBorder(BorderFactory.createLineBorder(NAVY));
			scrollPane.getViewport().setBackground(Color.WHITE);
			return scrollPane;
		}
	
		private JPanel taoFormKhachHang() {
			JPanel pnlForm = new JPanel();
			pnlForm.setPreferredSize(new Dimension(300, 0));
			pnlForm.setBackground(Color.WHITE);
			pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
			pnlForm.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(NAVY),
					BorderFactory.createEmptyBorder(12, 12, 12, 12)));
	
			JLabel lblTieuDe = new JLabel("Thông tin khách hàng");
			lblTieuDe.setFont(new Font("Arial", Font.BOLD, 17));
			lblTieuDe.setForeground(NAVY);
			pnlForm.add(lblTieuDe);
			pnlForm.add(Box.createVerticalStrut(10));
	
			txtMa = new JTextField();
			txtMa.setEditable(false);
			txtMa.setBackground(LOCKED);
			pnlForm.add(taoTruongNhap("Mã khách hàng", txtMa));
	
			txtHoTen = new JTextField();
			pnlForm.add(taoTruongNhap("Họ tên", txtHoTen));
	
			cboGioiTinh = new JComboBox<>(new String[] { "Chọn giới tính", "Nam", "Nữ" });
			pnlForm.add(taoTruongNhap("Giới tính", cboGioiTinh));
	
			txtSoDienThoai = new JTextField();
			pnlForm.add(taoTruongNhap("Số điện thoại", txtSoDienThoai));
	
			txtDiaChi = new JTextField();
			pnlForm.add(taoTruongNhap("Địa chỉ", txtDiaChi));
	
			JLabel lblGhiChu = new JLabel(
				    "<html>Mã tự động tạo khi thêm mới.<br>"
				    + "Chọn một dòng để cập nhật hoặc xóa.</html>"
				);
			lblGhiChu.setFont(new Font("Arial", Font.PLAIN, 11));
			lblGhiChu.setForeground(Color.GRAY);
			lblGhiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnlForm.add(lblGhiChu);
			pnlForm.add(Box.createVerticalStrut(12));
	
			btnThem = taoNut("Thêm", RED, Color.WHITE);
			btnCapNhat = taoNut("Cập nhật", NAVY, Color.WHITE);
			btnLamMoi = taoNut("Làm mới", Color.WHITE, NAVY);
			btnLamMoi.setBorder(BorderFactory.createLineBorder(NAVY));
			btnXoa = taoNut("Xóa", Color.WHITE, RED);
			btnXoa.setBorder(BorderFactory.createLineBorder(RED));
	
			JPanel pnlNut = new JPanel(new GridLayout(1, 3, 8, 0));
			pnlNut.setBackground(Color.WHITE);
			pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
			pnlNut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
			pnlNut.add(btnThem);
			pnlNut.add(btnCapNhat);
			pnlNut.add(btnLamMoi);
			pnlForm.add(pnlNut);
			pnlForm.add(Box.createVerticalStrut(8));
	
			btnXoa.setAlignmentX(Component.LEFT_ALIGNMENT);
			btnXoa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
			pnlForm.add(btnXoa);
	
			btnThem.addActionListener(this);
			btnCapNhat.addActionListener(this);
			btnLamMoi.addActionListener(this);
			btnXoa.addActionListener(this);
			btnTim.addActionListener(this);
	
			return pnlForm;
		}
	
		private JPanel taoTruongNhap(String ten, JComponent input) {
			JPanel pnl = new JPanel();
			pnl.setBackground(Color.WHITE);
			pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
			pnl.setAlignmentX(Component.LEFT_ALIGNMENT);
	
			JLabel lbl = new JLabel(ten);
			lbl.setFont(new Font("Arial", Font.PLAIN, 13));
			lbl.setForeground(NAVY);
			lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
	
			input.setAlignmentX(Component.LEFT_ALIGNMENT);
			input.setPreferredSize(new Dimension(0, 32));
			input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
	
			pnl.add(lbl);
			pnl.add(Box.createVerticalStrut(3));
			pnl.add(input);
			pnl.add(Box.createVerticalStrut(8));
			return pnl;
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
			btn.setPreferredSize(new Dimension(90, 35));
			btn.setMargin(new Insets(5, 5, 5, 5));
			return btn;
		}
	
	
		private void hienThi(ArrayList<KhachHang> ds) {
			dsHienTai = ds;
			tableModel.setRowCount(0);
			for (KhachHang kh : ds) {
				tableModel.addRow(new Object[] { kh.getMaKhachHang(), kh.getHo() + " " + kh.getTen(), kh.getGioiTinh(),
						kh.getSoDienThoai(), diaChiHienThi(kh.getDiaChi()) });
			}
		}
	
		private void taiLai() {
		    try {
		        hienThi(service.layDanhSach());
		    } catch (SQLException e) {
		        baoLoiCsdl(e);
		    }
		}
	
		private void timKiem() {
		    String kw = txtTim.getText().trim();
	
		    try {
		        hienThi(service.timKiem(kw));
		    } catch (SQLException e) {
		        baoLoiCsdl(e);
		        return;
		    }
	
		    xoaForm();
		}
	
		private void hienThiThongTin(int row) {
			if (row < 0 || row >= dsHienTai.size()) {
				return;
			}
			KhachHang kh = dsHienTai.get(row);
			txtMa.setText(kh.getMaKhachHang());
			txtHoTen.setText(kh.getHo() + " " + kh.getTen());
			cboGioiTinh.setSelectedItem(kh.getGioiTinh());
			txtSoDienThoai.setText(kh.getSoDienThoai());
			txtDiaChi.setText(diaChiHienThi(kh.getDiaChi()));
		}
	
		private void xoaForm() {
			tblKhachHang.clearSelection();
			txtMa.setText("");
			txtHoTen.setText("");
			cboGioiTinh.setSelectedIndex(0);
			txtSoDienThoai.setText("");
			txtDiaChi.setText("");
		}
	
		public void lamMoi() {
			txtTim.setText("");
			taiLai();
			xoaForm();
		}
		private void themMoi() {
	
		    String loi = kiemTra(
		        txtHoTen.getText(),
		        cboGioiTinh.getSelectedItem(),
		        txtSoDienThoai.getText()
		    );
	
		    if (loi != null) {
		        JOptionPane.showMessageDialog(
		            this,
		            loi,
		            "Dữ liệu chưa hợp lệ",
		            JOptionPane.WARNING_MESSAGE
		        );
		        return;
		    }
	
		    String[] ht = tachHoTen(txtHoTen.getText());
	
		    try {
		    	String ma = service.themMoi(
		            ht[0],
		            ht[1],
		            (String) cboGioiTinh.getSelectedItem(),
		            txtSoDienThoai.getText().trim(),
		            diaChiLuu(txtDiaChi.getText())
		        );
	
		        JOptionPane.showMessageDialog(
		            this,
		            "Đã thêm khách hàng " + ma + " thành công.",
		            "Thông báo",
		            JOptionPane.INFORMATION_MESSAGE
		        );
	
		        lamMoi();
	
		    } catch (IllegalArgumentException ex) {
	
		        JOptionPane.showMessageDialog(
		            this,
		            ex.getMessage(),
		            "Dữ liệu chưa hợp lệ",
		            JOptionPane.WARNING_MESSAGE
		        );
	
		    } catch (SQLException ex) {
	
		        baoLoiCsdl(ex);
		    }
		}
	
		private void capNhat() {
			String ma = txtMa.getText();
			if (ma.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần cập nhật.");
				return;
			}
			String loi = kiemTra(txtHoTen.getText(), cboGioiTinh.getSelectedItem(), txtSoDienThoai.getText());
			if (loi != null) {
				JOptionPane.showMessageDialog(this, loi, "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
				return;
			}
			String[] ht = tachHoTen(txtHoTen.getText());
			try {
				KhachHang kh = new KhachHang(ma, ht[0], ht[1], (String) cboGioiTinh.getSelectedItem(),
						txtSoDienThoai.getText().trim(), diaChiLuu(txtDiaChi.getText()));
				service.capNhat(kh);
				JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công.");
				lamMoi();
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
			} catch (SQLException ex) {
				baoLoiCsdl(ex);
			}
		}
	
		private void xoa() {
			String ma = txtMa.getText();
			if (ma.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.");
				return;
			}
			int ok = JOptionPane.showConfirmDialog(this, "Xóa khách hàng " + ma + "?",
					"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
			if (ok != JOptionPane.YES_OPTION) return;
	
			try {
				service.xoa(ma);
				lamMoi();
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
				lamMoi();
			} catch (SQLException ex) {
				String noiDung = ex.getErrorCode() == 547
						? "Không thể xóa: khách hàng đã có hóa đơn."
						: "Lỗi cơ sở dữ liệu: " + ex.getMessage();
				JOptionPane.showMessageDialog(this, noiDung, "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	
		private void baoLoiCsdl(SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	
	
		@Override
		public void actionPerformed(ActionEvent e) {
	
		    Object o = e.getSource();
	
		    if (o == btnTim || o == txtTim) {
	
		        timKiem();
	
		    } else if (o == btnThem) {
	
		        themMoi();
	
		    } else if (o == btnCapNhat) {
	
		        capNhat();
	
		    } else if (o == btnLamMoi) {
	
		        lamMoi();
	
		    } else if (o == btnXoa) {
	
		        xoa();
		    }
		}
	
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				int row = tblKhachHang.getSelectedRow();
				if (row != -1) {
					hienThiThongTin(row);
				}
			}
		}
	
	
		static String chuanHoaTen(String s) {
			return s == null ? "" : s.trim().replaceAll("\\s+", " ");
		}
	
	
		static String[] tachHoTen(String hoTen) {
			String s = chuanHoaTen(hoTen);
			int i = s.lastIndexOf(' ');
			return new String[] { s.substring(0, i), s.substring(i + 1) };
		}
	
		static String diaChiLuu(String diaChi) {
			String s = diaChi == null ? "" : diaChi.trim();
			return s.isEmpty() ? DIA_CHI_MAC_DINH : s;
		}
	
		static String diaChiHienThi(String diaChi) {
			return DIA_CHI_MAC_DINH.equals(diaChi) ? "" : diaChi;
		}
	
		static String kiemTra(String hoTen, Object gioiTinh, String sdt) {
			if (chuanHoaTen(hoTen).indexOf(' ') < 0) {
				return "Nhập đầy đủ họ và tên (ví dụ: Nguyễn Văn A).";
			}
			if (!"Nam".equals(gioiTinh) && !"Nữ".equals(gioiTinh)) {
				return "Vui lòng chọn giới tính.";
			}
			if (sdt == null || !sdt.trim().matches("0\\d{9}")) {
			    return "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0.";
			}
			return null;
		}
	}
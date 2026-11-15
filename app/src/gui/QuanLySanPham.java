package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entity.LoaiSanPham;
import entity.SanPham;
import service.QuanLySanPhamService;

public class QuanLySanPham extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final QuanLySanPhamService service = new QuanLySanPhamService();
	private final ArrayList<SanPham> danhSachSanPham = new ArrayList<SanPham>();

	private DefaultTableModel tableModel;
	private JTable tblSanPham;
	private JTextField txtMa;
	private JTextField txtTen;
	private JTextField txtDonVi;
	private JTextField txtSoLuong;
	private JTextField txtGiaNhap;
	private JTextField txtLoiNhuan;
	private JComboBox<LoaiSanPham> cboLoai;
	private JComboBox<String> cboTrangThai;
	private JButton btnThem;
	private JButton btnCapNhat;
	private JButton btnNgung;
	private JButton btnLamMoi;

	public QuanLySanPham() {
		setLayout(new BorderLayout(12, 12));
		setBackground(QuanLyUi.MAU_NEN);
		setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		taoTieuDe();
		taoNoiDung();
	}

	private void taoTieuDe() {
		JLabel tieuDe = new JLabel("Quản lý sản phẩm");
		tieuDe.setFont(new Font("Arial", Font.BOLD, 23));
		tieuDe.setForeground(QuanLyUi.MAU_XANH);
		add(tieuDe, BorderLayout.NORTH);
	}

	private void taoNoiDung() {
		JPanel noiDung = new JPanel(new BorderLayout(12, 0));
		noiDung.setBackground(QuanLyUi.MAU_NEN);
		noiDung.add(taoBang(), BorderLayout.CENTER);

		JPanel form = taoForm();
		form.setPreferredSize(new Dimension(330, 0));
		JScrollPane cuonForm = new JScrollPane(form);
		cuonForm.setBorder(BorderFactory.createLineBorder(QuanLyUi.MAU_XANH));
		cuonForm.getVerticalScrollBar().setUnitIncrement(16);
		noiDung.add(cuonForm, BorderLayout.EAST);
		add(noiDung, BorderLayout.CENTER);
	}

	private JScrollPane taoBang() {
		String[] tieuDe = {"Mã SP", "Tên sản phẩm", "Loại", "Đơn vị", "Tồn kho",
				"Giá nhập", "Lợi nhuận", "Giá bán", "Trạng thái"};
		tableModel = new DefaultTableModel(tieuDe, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblSanPham = new JTable(tableModel);
		tblSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		QuanLyUi.dinhDangBang(tblSanPham);
		tblSanPham.getSelectionModel().addListSelectionListener(this);
		JScrollPane cuon = new JScrollPane(tblSanPham);
		cuon.setBorder(BorderFactory.createLineBorder(QuanLyUi.MAU_XANH));
		return cuon;
	}

	private JPanel taoForm() {
		JPanel form = new JPanel();
		form.setBackground(Color.WHITE);
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel tieuDe = new JLabel("Thông tin sản phẩm");
		tieuDe.setFont(new Font("Arial", Font.BOLD, 17));
		tieuDe.setForeground(QuanLyUi.MAU_XANH);
		tieuDe.setAlignmentX(LEFT_ALIGNMENT);
		form.add(tieuDe);
		form.add(Box.createVerticalStrut(10));

		txtMa = new JTextField();
		txtMa.setEditable(false);
		txtMa.setBackground(QuanLyUi.MAU_XAM);
		form.add(QuanLyUi.taoTruong("Mã sản phẩm", txtMa));
		txtTen = new JTextField();
		form.add(QuanLyUi.taoTruong("Tên sản phẩm", txtTen));
		txtDonVi = new JTextField();
		form.add(QuanLyUi.taoTruong("Đơn vị tính", txtDonVi));
		txtSoLuong = new JTextField("0");
		form.add(QuanLyUi.taoTruong("Số lượng tồn", txtSoLuong));
		txtGiaNhap = new JTextField("0");
		form.add(QuanLyUi.taoTruong("Giá nhập", txtGiaNhap));
		txtLoiNhuan = new JTextField("0");
		form.add(QuanLyUi.taoTruong("Mức lợi nhuận", txtLoiNhuan));
		cboLoai = new JComboBox<LoaiSanPham>();
		cboLoai.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof LoaiSanPham) {
					LoaiSanPham loai = (LoaiSanPham) value;
					String ten = loai.getTenLoaiSanPham();
					setText("Đang hoạt động".equals(loai.getTrangThai())
							? ten : ten + " (Ngừng hoạt động)");
				}
				return this;
			}
		});
		form.add(QuanLyUi.taoTruong("Loại sản phẩm", cboLoai));
		cboTrangThai = new JComboBox<String>(new String[] {"Đang hoạt động", "Ngừng hoạt động"});
		form.add(QuanLyUi.taoTruong("Trạng thái", cboTrangThai));
		taoCacNut(form);
		return form;
	}

	private void taoCacNut(JPanel form) {
		btnThem = QuanLyUi.taoNut("Thêm", true);
		btnCapNhat = QuanLyUi.taoNut("Cập nhật", false);
		btnNgung = QuanLyUi.taoNut("Ngừng hoạt động", false);
		btnLamMoi = QuanLyUi.taoNut("Làm mới", false);
		btnThem.addActionListener(this);
		btnCapNhat.addActionListener(this);
		btnNgung.addActionListener(this);
		btnLamMoi.addActionListener(this);

		JPanel cacNut = QuanLyUi.taoNhomNut(btnThem, btnCapNhat, btnNgung, btnLamMoi);
		form.add(Box.createVerticalStrut(8));
		form.add(cacNut);
	}

	public void taiLaiDuLieu() {
		try {
			ArrayList<LoaiSanPham> cacLoai = service.layDanhSachLoai();
			ArrayList<SanPham> cacSanPham = service.layDanhSach();
			cboLoai.removeAllItems();
			for (LoaiSanPham loai : cacLoai) {
				cboLoai.addItem(loai);
			}
			danhSachSanPham.clear();
			danhSachSanPham.addAll(cacSanPham);
			tableModel.setRowCount(0);
			for (SanPham sanPham : danhSachSanPham) {
				tableModel.addRow(new Object[] {sanPham.getMaSanPham(), sanPham.getTenSanPham(),
						sanPham.getLoaiSanPham().getTenLoaiSanPham(), sanPham.getDonViTinh(),
						sanPham.getSoLuongTon(), sanPham.getGiaNhap(), sanPham.getMucLoiNhuan(),
						sanPham.getGiaBan(), sanPham.getTrangThai()});
			}
			lamMoiForm();
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void hienThiThongTin(int row) {
		String ma = tableModel.getValueAt(row, 0).toString();
		for (SanPham sanPham : danhSachSanPham) {
			if (sanPham.getMaSanPham().equals(ma)) {
				txtMa.setText(sanPham.getMaSanPham());
				txtTen.setText(sanPham.getTenSanPham());
				txtDonVi.setText(sanPham.getDonViTinh());
				txtSoLuong.setText(String.valueOf(sanPham.getSoLuongTon()));
				txtGiaNhap.setText(sanPham.getGiaNhap().toPlainString());
				txtLoiNhuan.setText(sanPham.getMucLoiNhuan().toPlainString());
				cboTrangThai.setSelectedItem(sanPham.getTrangThai());
				chonLoai(sanPham.getLoaiSanPham().getMaLoaiSanPham());
				return;
			}
		}
	}

	private void chonLoai(String maLoai) {
		for (int i = 0; i < cboLoai.getItemCount(); i++) {
			LoaiSanPham loai = cboLoai.getItemAt(i);
			if (loai.getMaLoaiSanPham().equals(maLoai)) {
				cboLoai.setSelectedIndex(i);
				return;
			}
		}
		cboLoai.setSelectedItem(null);
	}

	private SanPham docThongTin(boolean capNhat) {
		LoaiSanPham loai = (LoaiSanPham) cboLoai.getSelectedItem();
		if (loai == null) {
			throw new IllegalArgumentException("Vui lòng tạo hoặc chọn loại sản phẩm.");
		}
		if (!"Đang hoạt động".equals(loai.getTrangThai())) {
			boolean dangDungLoai = false;
			if (capNhat) {
				for (SanPham sanPham : danhSachSanPham) {
					if (sanPham.getMaSanPham().equals(txtMa.getText().trim())
							&& sanPham.getLoaiSanPham().getMaLoaiSanPham()
									.equals(loai.getMaLoaiSanPham())) {
						dangDungLoai = true;
						break;
					}
				}
			}
			if (!dangDungLoai) {
				throw new IllegalArgumentException("Chỉ được chọn loại sản phẩm đang hoạt động.");
			}
		}
		int soLuong;
		try {
			soLuong = Integer.parseInt(txtSoLuong.getText().trim());
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Số lượng tồn phải là số nguyên.");
		}
		return new SanPham(txtMa.getText().trim(), txtTen.getText().trim(), txtDonVi.getText().trim(),
				soLuong, new BigDecimal(txtGiaNhap.getText().trim()),
				new BigDecimal(txtLoiNhuan.getText().trim()),
				(String) cboTrangThai.getSelectedItem(), loai);
	}

	private void themSanPham() {
		try {
			txtMa.setText(service.sinhMa());
			if (service.them(docThongTin(false))) {
				JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void capNhatSanPham() {
		if (tblSanPham.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật.");
			return;
		}
		try {
			if (service.capNhat(docThongTin(true))) {
				JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void ngungSanPham() {
		int row = tblSanPham.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần ngừng hoạt động.");
			return;
		}
		String ma = tableModel.getValueAt(row, 0).toString();
		int luaChon = JOptionPane.showConfirmDialog(this, "Ngừng hoạt động sản phẩm " + ma + "?",
				"Xác nhận", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			if (service.ngungHoatDong(ma)) {
				JOptionPane.showMessageDialog(this, "Đã ngừng hoạt động sản phẩm.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Không thể ngừng hoạt động sản phẩm.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void lamMoiForm() throws SQLException {
		tblSanPham.clearSelection();
		txtMa.setText(service.sinhMa());
		txtTen.setText("");
		txtDonVi.setText("");
		txtSoLuong.setText("0");
		txtGiaNhap.setText("0");
		txtLoiNhuan.setText("0");
		cboTrangThai.setSelectedIndex(0);
		for (int i = 0; i < cboLoai.getItemCount(); i++) {
			if ("Đang hoạt động".equals(cboLoai.getItemAt(i).getTrangThai())) {
				cboLoai.setSelectedIndex(i);
				return;
			}
		}
		cboLoai.setSelectedItem(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnThem) {
			themSanPham();
		} else if (source == btnCapNhat) {
			capNhatSanPham();
		} else if (source == btnNgung) {
			ngungSanPham();
		} else if (source == btnLamMoi) {
			taiLaiDuLieu();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			int row = tblSanPham.getSelectedRow();
			if (row >= 0) {
				hienThiThongTin(row);
			}
		}
	}
}

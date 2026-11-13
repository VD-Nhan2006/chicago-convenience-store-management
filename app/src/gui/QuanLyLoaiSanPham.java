package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entity.LoaiSanPham;
import service.QuanLyLoaiSanPhamService;

public class QuanLyLoaiSanPham extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final QuanLyLoaiSanPhamService service = new QuanLyLoaiSanPhamService();
	private final ArrayList<LoaiSanPham> danhSachLoai = new ArrayList<LoaiSanPham>();

	private DefaultTableModel tableModel;
	private JTable tblLoai;
	private JTextField txtMa;
	private JTextField txtTen;
	private JTextField txtMoTa;
	private JComboBox<String> cboTrangThai;
	private JButton btnThem;
	private JButton btnCapNhat;
	private JButton btnNgung;
	private JButton btnLamMoi;

	public QuanLyLoaiSanPham() {
		setLayout(new BorderLayout(12, 12));
		setBackground(QuanLyUi.MAU_NEN);
		setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		taoTieuDe();
		taoNoiDung();
	}

	private void taoTieuDe() {
		JLabel tieuDe = new JLabel("Quản lý loại sản phẩm");
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
		String[] tieuDe = {"Mã loại", "Tên loại sản phẩm", "Mô tả", "Trạng thái"};
		tableModel = new DefaultTableModel(tieuDe, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblLoai = new JTable(tableModel);
		tblLoai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		QuanLyUi.dinhDangBang(tblLoai);
		tblLoai.getSelectionModel().addListSelectionListener(this);
		JScrollPane cuon = new JScrollPane(tblLoai);
		cuon.setBorder(BorderFactory.createLineBorder(QuanLyUi.MAU_XANH));
		return cuon;
	}

	private JPanel taoForm() {
		JPanel form = new JPanel();
		form.setBackground(Color.WHITE);
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel tieuDe = new JLabel("Thông tin loại sản phẩm");
		tieuDe.setFont(new Font("Arial", Font.BOLD, 17));
		tieuDe.setForeground(QuanLyUi.MAU_XANH);
		tieuDe.setAlignmentX(LEFT_ALIGNMENT);
		form.add(tieuDe);
		form.add(Box.createVerticalStrut(10));

		txtMa = new JTextField();
		txtMa.setEditable(false);
		txtMa.setBackground(QuanLyUi.MAU_XAM);
		form.add(QuanLyUi.taoTruong("Mã loại sản phẩm", txtMa));
		txtTen = new JTextField();
		form.add(QuanLyUi.taoTruong("Tên loại sản phẩm", txtTen));
		txtMoTa = new JTextField();
		form.add(QuanLyUi.taoTruong("Mô tả", txtMoTa));
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
			ArrayList<LoaiSanPham> cacLoai = service.layDanhSach();
			danhSachLoai.clear();
			danhSachLoai.addAll(cacLoai);
			tableModel.setRowCount(0);
			for (LoaiSanPham loai : danhSachLoai) {
				tableModel.addRow(new Object[] {loai.getMaLoaiSanPham(), loai.getTenLoaiSanPham(),
						loai.getMoTa(), loai.getTrangThai()});
			}
			lamMoiForm();
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private LoaiSanPham docThongTin() {
		return new LoaiSanPham(txtMa.getText().trim(), txtTen.getText().trim(),
				txtMoTa.getText().trim(), (String) cboTrangThai.getSelectedItem());
	}

	private void themLoai() {
		try {
			txtMa.setText(service.sinhMa());
			if (service.them(docThongTin())) {
				JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void capNhatLoai() {
		if (tblLoai.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần cập nhật.");
			return;
		}
		try {
			if (service.capNhat(docThongTin())) {
				JOptionPane.showMessageDialog(this, "Cập nhật loại sản phẩm thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật loại sản phẩm thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void ngungLoai() {
		int row = tblLoai.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần ngừng hoạt động.");
			return;
		}
		String ma = tableModel.getValueAt(row, 0).toString();
		int luaChon = JOptionPane.showConfirmDialog(this, "Ngừng hoạt động loại sản phẩm " + ma + "?",
				"Xác nhận", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			if (service.ngungHoatDong(ma)) {
				JOptionPane.showMessageDialog(this, "Đã ngừng hoạt động loại sản phẩm.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Không thể ngừng hoạt động loại sản phẩm.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void lamMoiForm() throws SQLException {
		tblLoai.clearSelection();
		txtMa.setText(service.sinhMa());
		txtTen.setText("");
		txtMoTa.setText("");
		cboTrangThai.setSelectedIndex(0);
	}

	private void hienThiThongTin(int row) {
		String ma = tableModel.getValueAt(row, 0).toString();
		for (LoaiSanPham loai : danhSachLoai) {
			if (loai.getMaLoaiSanPham().equals(ma)) {
				txtMa.setText(loai.getMaLoaiSanPham());
				txtTen.setText(loai.getTenLoaiSanPham());
				txtMoTa.setText(loai.getMoTa());
				cboTrangThai.setSelectedItem(loai.getTrangThai());
				return;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnThem) {
			themLoai();
		} else if (source == btnCapNhat) {
			capNhatLoai();
		} else if (source == btnNgung) {
			ngungLoai();
		} else if (source == btnLamMoi) {
			taiLaiDuLieu();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			int row = tblLoai.getSelectedRow();
			if (row >= 0) {
				hienThiThongTin(row);
			}
		}
	}
}

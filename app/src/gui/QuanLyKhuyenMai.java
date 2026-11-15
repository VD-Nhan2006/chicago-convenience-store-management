package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
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

import entity.ChuongTrinhKhuyenMai;
import service.QuanLyKhuyenMaiService;

public class QuanLyKhuyenMai extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final QuanLyKhuyenMaiService service = new QuanLyKhuyenMaiService();
	private final ArrayList<ChuongTrinhKhuyenMai> danhSachKhuyenMai =
			new ArrayList<ChuongTrinhKhuyenMai>();

	private DefaultTableModel tableModel;
	private JTable tblKhuyenMai;
	private JTextField txtMa;
	private JTextField txtTen;
	private JTextField txtMoTa;
	private JComboBox<String> cboLoai;
	private JTextField txtGiaTri;
	private JTextField txtNgayBatDau;
	private JTextField txtNgayKetThuc;
	private JComboBox<String> cboTrangThai;
	private JButton btnThem;
	private JButton btnCapNhat;
	private JButton btnNgung;
	private JButton btnLamMoi;

	public QuanLyKhuyenMai() {
		setLayout(new BorderLayout(12, 12));
		setBackground(QuanLyUi.MAU_NEN);
		setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		taoTieuDe();
		taoNoiDung();
	}

	private void taoTieuDe() {
		JLabel tieuDe = new JLabel("Quản lý khuyến mãi");
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
		String[] tieuDe = {"Mã KM", "Tên chương trình", "Loại", "Giá trị",
				"Bắt đầu", "Kết thúc", "Trạng thái"};
		tableModel = new DefaultTableModel(tieuDe, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblKhuyenMai = new JTable(tableModel);
		tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		QuanLyUi.dinhDangBang(tblKhuyenMai);
		tblKhuyenMai.getSelectionModel().addListSelectionListener(this);
		JScrollPane cuon = new JScrollPane(tblKhuyenMai);
		cuon.setBorder(BorderFactory.createLineBorder(QuanLyUi.MAU_XANH));
		return cuon;
	}

	private JPanel taoForm() {
		JPanel form = new JPanel();
		form.setBackground(Color.WHITE);
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel tieuDe = new JLabel("Thông tin khuyến mãi");
		tieuDe.setFont(new Font("Arial", Font.BOLD, 17));
		tieuDe.setForeground(QuanLyUi.MAU_XANH);
		tieuDe.setAlignmentX(LEFT_ALIGNMENT);
		form.add(tieuDe);
		form.add(Box.createVerticalStrut(10));

		txtMa = new JTextField();
		txtMa.setEditable(false);
		txtMa.setBackground(QuanLyUi.MAU_XAM);
		form.add(QuanLyUi.taoTruong("Mã khuyến mãi", txtMa));
		txtTen = new JTextField();
		form.add(QuanLyUi.taoTruong("Tên chương trình", txtTen));
		txtMoTa = new JTextField();
		form.add(QuanLyUi.taoTruong("Mô tả", txtMoTa));
		cboLoai = new JComboBox<String>(new String[] {"Tiền mặt", "Phần trăm"});
		form.add(QuanLyUi.taoTruong("Loại khuyến mãi", cboLoai));
		txtGiaTri = new JTextField();
		form.add(QuanLyUi.taoTruong("Giá trị (VND hoặc %)", txtGiaTri));
		txtNgayBatDau = new JTextField();
		form.add(QuanLyUi.taoTruong("Ngày bắt đầu (yyyy-MM-dd)", txtNgayBatDau));
		txtNgayKetThuc = new JTextField();
		form.add(QuanLyUi.taoTruong("Ngày kết thúc (yyyy-MM-dd)", txtNgayKetThuc));
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
			ArrayList<ChuongTrinhKhuyenMai> cacKhuyenMai = service.layDanhSach();
			danhSachKhuyenMai.clear();
			danhSachKhuyenMai.addAll(cacKhuyenMai);
			tableModel.setRowCount(0);
			for (ChuongTrinhKhuyenMai khuyenMai : danhSachKhuyenMai) {
				tableModel.addRow(new Object[] {khuyenMai.getMaKM(), khuyenMai.getTenChuongTrinh(),
						hienThiLoai(khuyenMai.getLoaiKhuyenMai()), khuyenMai.getGiaTriKM(),
						khuyenMai.getNgayBatDau(), khuyenMai.getNgayKetThuc(), khuyenMai.getTrangThai()});
			}
			lamMoiForm();
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private String hienThiLoai(String loai) {
		return "PHAN_TRAM".equals(loai) ? "Phần trăm" : "Tiền mặt";
	}

	private ChuongTrinhKhuyenMai docThongTin() {
		String loai = "Phần trăm".equals(cboLoai.getSelectedItem()) ? "PHAN_TRAM" : "TIEN_MAT";
		return new ChuongTrinhKhuyenMai(txtMa.getText().trim(), txtTen.getText().trim(),
				txtMoTa.getText().trim(), loai, new BigDecimal(txtGiaTri.getText().trim()),
				LocalDate.parse(txtNgayBatDau.getText().trim()),
				LocalDate.parse(txtNgayKetThuc.getText().trim()),
				(String) cboTrangThai.getSelectedItem());
	}

	private void themKhuyenMai() {
		try {
			txtMa.setText(service.sinhMa());
			if (service.them(docThongTin())) {
				JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void capNhatKhuyenMai() {
		if (tblKhuyenMai.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần cập nhật.");
			return;
		}
		try {
			if (service.capNhat(docThongTin())) {
				JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thất bại.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IllegalArgumentException | IllegalStateException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void ngungKhuyenMai() {
		int row = tblKhuyenMai.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần ngừng hoạt động.");
			return;
		}
		String ma = tableModel.getValueAt(row, 0).toString();
		int luaChon = JOptionPane.showConfirmDialog(this, "Ngừng hoạt động khuyến mãi " + ma + "?",
				"Xác nhận", JOptionPane.YES_NO_OPTION);
		if (luaChon != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			if (service.ngungHoatDong(ma)) {
				JOptionPane.showMessageDialog(this, "Đã ngừng hoạt động khuyến mãi.");
				taiLaiDuLieu();
			} else {
				JOptionPane.showMessageDialog(this, "Không thể ngừng hoạt động khuyến mãi.",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException ex) {
			QuanLyUi.baoLoi(this, ex);
		}
	}

	private void lamMoiForm() throws SQLException {
		tblKhuyenMai.clearSelection();
		txtMa.setText(service.sinhMa());
		txtTen.setText("");
		txtMoTa.setText("");
		cboLoai.setSelectedIndex(0);
		txtGiaTri.setText("");
		LocalDate homNay = LocalDate.now();
		txtNgayBatDau.setText(homNay.toString());
		txtNgayKetThuc.setText(homNay.plusDays(7).toString());
		cboTrangThai.setSelectedIndex(0);
	}

	private void hienThiThongTin(int row) {
		String ma = tableModel.getValueAt(row, 0).toString();
		for (ChuongTrinhKhuyenMai khuyenMai : danhSachKhuyenMai) {
			if (khuyenMai.getMaKM().equals(ma)) {
				txtMa.setText(khuyenMai.getMaKM());
				txtTen.setText(khuyenMai.getTenChuongTrinh());
				txtMoTa.setText(khuyenMai.getMoTa());
				cboLoai.setSelectedItem(hienThiLoai(khuyenMai.getLoaiKhuyenMai()));
				txtGiaTri.setText(khuyenMai.getGiaTriKM().toPlainString());
				txtNgayBatDau.setText(khuyenMai.getNgayBatDau().toString());
				txtNgayKetThuc.setText(khuyenMai.getNgayKetThuc().toString());
				cboTrangThai.setSelectedItem(khuyenMai.getTrangThai());
				return;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == btnThem) {
			themKhuyenMai();
		} else if (source == btnCapNhat) {
			capNhatKhuyenMai();
		} else if (source == btnNgung) {
			ngungKhuyenMai();
		} else if (source == btnLamMoi) {
			taiLaiDuLieu();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			int row = tblKhuyenMai.getSelectedRow();
			if (row >= 0) {
				hienThiThongTin(row);
			}
		}
	}
}

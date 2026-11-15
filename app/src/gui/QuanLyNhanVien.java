
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
import javax.swing.table.DefaultTableModel;

import entity.NhanVien;
import service.QuanLyNhanVienService;

public class QuanLyNhanVien extends JPanel implements ActionListener, ListSelectionListener{

    private static final long serialVersionUID = 1L;

    // Bảng màu
    private final Color MAU_NEN = new Color(250, 247, 240);
    private final Color MAU_XANH = new Color(23, 52, 81);
    private final Color MAU_DO = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_XAM = new Color(230, 237, 245);

    // Bảng nhân viên
    private JTable tblNhanVien;
    private DefaultTableModel tableModel;

    // Form thông tin
    private JTextField txtMaNV;
    private JTextField txtHo;
    private JTextField txtTen;
    private JTextField txtSoDienThoai;
    private JTextField txtDiaChi;

//    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;

    // Các nút
    private JButton btnThem;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;
    
    private QuanLyNhanVienService nhanVienService = new QuanLyNhanVienService();

    public QuanLyNhanVien() {

        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);

        setBorder(BorderFactory.createEmptyBorder(
            16, 16, 16, 16
        ));

        taoTieuDe();
        taoNoiDung();
        
        loadDuLieuNhanVien();
        txtMaNV.setText(nhanVienService.sinhMaNhanVien());

    }

    // Tạo tiêu đề
    private void taoTieuDe() {

        JPanel pnlTieuDe = new JPanel();

        pnlTieuDe.setLayout(
            new BoxLayout(pnlTieuDe, BoxLayout.Y_AXIS)
        );

        pnlTieuDe.setBackground(MAU_NEN);

        JLabel lblTieuDe = new JLabel("Quản lý nhân viên");

        lblTieuDe.setFont(
            new Font("Arial", Font.BOLD, 23)
        );

        lblTieuDe.setForeground(MAU_XANH);



        pnlTieuDe.add(lblTieuDe);
        

        add(pnlTieuDe, BorderLayout.NORTH);

    }

    // Tạo vùng nội dung
    private void taoNoiDung() {

        JPanel pnlNoiDung = new JPanel(
            new BorderLayout(12, 0)
        );

        pnlNoiDung.setBackground(MAU_NEN);

        pnlNoiDung.add(
            taoBangNhanVien(),
            BorderLayout.CENTER
        );

        pnlNoiDung.add(
            taoFormNhanVien(),
            BorderLayout.EAST
        );

        add(pnlNoiDung, BorderLayout.CENTER);

    }

    // Tạo bảng danh sách nhân viên
    private JScrollPane taoBangNhanVien() {

    	String[] tieuDe = {
    		    "Mã NV",
    		    "Họ",
    		    "Tên",
    		    "Số điện thoại",
    		    "Địa chỉ",
    		    "Vai trò",
    		    "Trạng thái"
    		};

        tableModel = new DefaultTableModel(tieuDe, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        tblNhanVien = new JTable(tableModel);

        tblNhanVien.setRowHeight(30);

        tblNhanVien.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        tblNhanVien.setForeground(MAU_XANH);

        tblNhanVien.setSelectionBackground(MAU_XAM);

        tblNhanVien.setSelectionForeground(MAU_XANH);

        tblNhanVien.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tblNhanVien.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        tblNhanVien.getTableHeader().setBackground(MAU_XANH);

        tblNhanVien.getTableHeader().setForeground(Color.WHITE);

        tblNhanVien.getTableHeader().setPreferredSize(
            new Dimension(0, 34)
        );

        tblNhanVien.setFillsViewportHeight(true);

        

        // Khi chọn dòng thì đưa dữ liệu lên form    
        tblNhanVien.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(MAU_VIEN)
        );

        return scrollPane;

    }

    // Tạo form thông tin nhân viên
    private JPanel taoFormNhanVien() {

        JPanel pnlForm = new JPanel();

        pnlForm.setPreferredSize(
            new Dimension(320, 0)
        );

        pnlForm.setBackground(Color.WHITE);

        pnlForm.setLayout(
            new BoxLayout(pnlForm, BoxLayout.Y_AXIS)
        );

        pnlForm.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel lblTieuDe = new JLabel("Thông tin nhân viên");

        lblTieuDe.setFont(
            new Font("Arial", Font.BOLD, 17)
        );

        lblTieuDe.setForeground(MAU_XANH);

        pnlForm.add(lblTieuDe);
        pnlForm.add(Box.createVerticalStrut(10));

        // Mã nhân viên
        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(MAU_XAM);

        pnlForm.add(taoTruongNhap("Mã nhân viên", txtMaNV));

     // Họ
        txtHo = new JTextField();

        pnlForm.add(
            taoTruongNhap("Họ", txtHo)
        );

        // Tên
        txtTen = new JTextField();

        pnlForm.add(
            taoTruongNhap("Tên", txtTen)
        );

        // Số điện thoại
        txtSoDienThoai = new JTextField();

        pnlForm.add(
            taoTruongNhap("Số điện thoại", txtSoDienThoai)
        );

        // Địa chỉ
        txtDiaChi = new JTextField();

        pnlForm.add(
            taoTruongNhap("Địa chỉ", txtDiaChi)
        );

        // Vai trò
//        cboVaiTro = new JComboBox<>(new String[] {
//        	    "Chưa có tài khoản",
//        	    "Thu ngân",
//        	    "Quản lý"
//        	});
//
//        	cboVaiTro.setEnabled(false);

//        	pnlForm.add(taoTruongNhap("Vai trò", cboVaiTro));

//        pnlForm.add(taoTruongNhap("Vai trò", cboVaiTro));

        // Trạng thái
        cboTrangThai = new JComboBox<>(new String[] {
            "Đang hoạt động",
            "Ngừng hoạt động"
        });

        pnlForm.add(
            taoTruongNhap("Trạng thái", cboTrangThai)
        );

        pnlForm.add(Box.createVerticalStrut(8));

        

        pnlForm.add(Box.createVerticalStrut(10));

        taoCacNut();

        JPanel pnlNut = new JPanel(new GridLayout(2, 2, 8, 8));

        pnlNut.setBackground(Color.WHITE);
        
        pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlNut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        pnlNut.add(btnThem);
        pnlNut.add(btnCapNhat);
        pnlNut.add(btnXoa);
        pnlNut.add(btnLamMoi);

        pnlForm.add(pnlNut);

        return pnlForm;

    }

    // Tạo một trường nhập liệu gồm nhãn và ô nhập
    private JPanel taoTruongNhap(String ten, JComponent input) {

        JPanel pnl = new JPanel();

        pnl.setBackground(Color.WHITE);

        pnl.setLayout(
            new BoxLayout(pnl, BoxLayout.Y_AXIS)
        );
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JLabel lbl = new JLabel(ten);

        lbl.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        lbl.setForeground(MAU_XANH);
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

    // Tạo các nút chức năng
    private void taoCacNut() {

        btnThem = taoNut("Thêm", MAU_DO, Color.WHITE);

        btnCapNhat = taoNut("Cập nhật", MAU_XANH, Color.WHITE);

        btnXoa = taoNut("Xóa", Color.WHITE, MAU_DO);

        btnLamMoi = taoNut("Làm mới", Color.WHITE, MAU_XANH);

        btnXoa.setBorder(
            BorderFactory.createLineBorder(MAU_DO)
        );

        btnLamMoi.setBorder(
            BorderFactory.createLineBorder(MAU_XANH)
        );
        btnThem.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);

    }

    // Hàm tạo nút dùng chung
    private JButton taoNut(
        String ten,
        Color mauNen,
        Color mauChu
    ) {

        JButton btn = new JButton(ten);

        btn.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        btn.setBackground(mauNen);
        btn.setForeground(mauChu);

        btn.setFocusPainted(false);

        btn.setOpaque(true);

        btn.setPreferredSize(new Dimension(140, 35));

        btn.setMargin(new Insets(5, 5, 5, 5));

        return btn;

    }

    // Hiển thị thông tin dòng được chọn lên form
    private void hienThiThongTin(int row) {

        txtMaNV.setText(
            tableModel.getValueAt(row, 0).toString()
        );

        txtHo.setText(
            tableModel.getValueAt(row, 1).toString()
        );

        txtTen.setText(
            tableModel.getValueAt(row, 2).toString()
        );

        txtSoDienThoai.setText(
            tableModel.getValueAt(row, 3).toString()
        );

        txtDiaChi.setText(
            tableModel.getValueAt(row, 4).toString()
        );

//        int rowModel = tblNhanVien.convertRowIndexToModel(row);
//
//        String vaiTro = tableModel.getValueAt(rowModel, 5).toString();
//
//        cboVaiTro.setSelectedItem(
//            vaiTro.isEmpty() ? "Chưa có tài khoản" : vaiTro
//        );

        cboTrangThai.setSelectedItem(
            tableModel.getValueAt(row, 6).toString()
        );
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnThem) {
			themNhanVien();
		}
		if(e.getSource() == btnCapNhat) {
			capNhatNhanVien();
		}
		if(e.getSource() == btnLamMoi) {
			lamMoi();
		}
		if(e.getSource() == btnXoa) {
			xoaNhanVien();
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (!e.getValueIsAdjusting()) {

            int row = tblNhanVien.getSelectedRow();

            if (row != -1) {
                hienThiThongTin(row);
            }

        }
		
	}
	
	private void loadDuLieuNhanVien() {

	    // Xóa dữ liệu cũ trên bảng
	    tableModel.setRowCount(0);

	    // Lấy danh sách nhân viên từ Service
	    ArrayList<NhanVien> dsNV = nhanVienService.getAllNhanVien();

	    // Duyệt danh sách và đưa từng nhân viên lên bảng
	    for (NhanVien nv : dsNV) {

	    	String vaiTro = "Chưa có tài khoản";

	    	if (nv.getTaiKhoan() != null
	    	        && nv.getTaiKhoan().getVaiTro() != null) {

	    	    vaiTro = nv.getTaiKhoan()
	    	               .getVaiTro()
	    	               .getTenHienThi();
	    	}

	        Object[] row = {
	            nv.getMaNhanVien(),
	            nv.getHo(),
	            nv.getTen(),
	            nv.getSoDienThoai(),
	            nv.getDiaChi(),
	            vaiTro,
	            nv.getTrangThai()
	        };

	        tableModel.addRow(row);
	    }
	}

	private void themNhanVien() {
	
	    try {
	        // Tạo đối tượng nhân viên từ dữ liệu trên form
	        NhanVien nv = new NhanVien(
	            txtMaNV.getText().trim(),
	            txtHo.getText().trim(),
	            txtTen.getText().trim(),
	            txtSoDienThoai.getText().trim(),
	            txtDiaChi.getText().trim(),
	            cboTrangThai.getSelectedItem().toString()
	        );
	
	        // Gọi Service thêm nhân viên
	        boolean ketQua = nhanVienService.themNhanVien(nv);
	
	        if (ketQua) {
	
	            JOptionPane.showMessageDialog(
	                this,
	                "Thêm nhân viên thành công!"
	            );
	
	            // Cập nhật lại bảng
	            loadDuLieuNhanVien();
	
	            // Làm mới form
	            lamMoiForm();
	
	        } else {
	
	            JOptionPane.showMessageDialog(
	                this,
	                "Thêm nhân viên thất bại!",
	                "Thông báo",
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	
	    } catch (IllegalArgumentException ex) {
	
	        JOptionPane.showMessageDialog(
	            this,
	            ex.getMessage(),
	            "Dữ liệu không hợp lệ",
	            JOptionPane.WARNING_MESSAGE
	        );
	    }
	}
	
	private void lamMoiForm() {

	    txtHo.setText("");
	    txtTen.setText("");
	    txtSoDienThoai.setText("");
	    txtDiaChi.setText("");

//	    cboVaiTro.setSelectedItem("Chưa có tài khoản");
	    cboTrangThai.setSelectedIndex(0);

	    tblNhanVien.clearSelection();

	    // Sinh mã nhân viên mới
	    txtMaNV.setText(nhanVienService.sinhMaNhanVien());
	}
	

	private void capNhatNhanVien() {
	
	    try {
	        // Kiểm tra đã chọn nhân viên cần cập nhật
	        if (tblNhanVien.getSelectedRow() == -1) {
	            JOptionPane.showMessageDialog(
	                this,
	                "Vui lòng chọn nhân viên cần cập nhật!"
	            );
	            return;
	        }
	
	        // Tạo đối tượng nhân viên từ dữ liệu trên form
	        NhanVien nv = new NhanVien(
	            txtMaNV.getText().trim(),
	            txtHo.getText().trim(),
	            txtTen.getText().trim(),
	            txtSoDienThoai.getText().trim(),
	            txtDiaChi.getText().trim(),
	            cboTrangThai.getSelectedItem().toString()
	        );
	
	        // Gọi Service cập nhật
	        boolean ketQua = nhanVienService.capNhatNhanVien(nv);
	
	        if (ketQua) {
	
	            JOptionPane.showMessageDialog(
	                this,
	                "Cập nhật nhân viên thành công!"
	            );
	
	            loadDuLieuNhanVien();
	            lamMoiForm();
	
	        } else {
	
	            JOptionPane.showMessageDialog(
	                this,
	                "Cập nhật nhân viên thất bại!"
	            );
	        }
	
	    } catch (IllegalArgumentException ex) {
	
	        JOptionPane.showMessageDialog(
	            this,
	            ex.getMessage(),
	            "Dữ liệu không hợp lệ",
	            JOptionPane.WARNING_MESSAGE
	        );
	    }
	}
	
	private void lamMoi() {
		loadDuLieuNhanVien();
		lamMoiForm();
	}
	
	private void xoaNhanVien() {
	    int dongChon = tblNhanVien.getSelectedRow();

	    if (dongChon == -1) {
	        JOptionPane.showMessageDialog(this,
	                "Vui lòng chọn nhân viên cần ngừng hoạt động!");
	        return;
	    }

	    String maNV = tableModel.getValueAt(dongChon, 0).toString();

	    int luaChon = JOptionPane.showConfirmDialog(
	            this,
	            "Bạn có chắc muốn ngừng hoạt động nhân viên " + maNV + "?",
	            "Xác nhận",
	            JOptionPane.YES_NO_OPTION
	    );

	    if (luaChon == JOptionPane.YES_OPTION) {
	        boolean ketQua = nhanVienService.xoaNhanVien(maNV);

	        if (ketQua) {
	            JOptionPane.showMessageDialog(this,
	                    "Đã chuyển nhân viên sang trạng thái ngừng hoạt động!");

	            loadDuLieuNhanVien();
	            lamMoiForm();
	        } else {
	            JOptionPane.showMessageDialog(this,
	                    "Không thể cập nhật trạng thái nhân viên!");
	        }
	    }
	}



}

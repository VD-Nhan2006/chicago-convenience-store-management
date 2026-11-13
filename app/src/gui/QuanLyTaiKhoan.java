package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class QuanLyTaiKhoan extends JPanel implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;
	 
    // Bảng màu (dùng chung với các màn hình khác)
    private final Color MAU_NEN = new Color(250, 247, 240);
    private final Color MAU_XANH = new Color(23, 52, 81);
    private final Color MAU_DO = new Color(239, 77, 59);
    private final Color MAU_VIEN = new Color(23, 52, 81);
    private final Color MAU_XAM = new Color(230, 237, 245);
    private final Color MAU_CHU_PHU = new Color(100, 116, 139);
 
    // Bảng danh sách tài khoản
    private JTable tblTaiKhoan;
    private DefaultTableModel tableModel;
 
    // Form thông tin
    private JComboBox<String> cboMaNhanVien;
    private JTextField txtHoTen;
    private JPasswordField txtMatKhauMoi;
 
    // Các nút
    private JButton btnCapMatKhau;
    private JButton btnDoiMatKhau;
    private JButton btnXoa;
    private JButton btnLamMoi;
 
    // Tra họ tên theo mã nhân viên, được nạp cùng lúc với combo (xem loadComboNhanVien)
    private final Map<String, String> dsHoTenTheoMa = new HashMap<String, String>();
 
    public QuanLyTaiKhoan() {
 
        setLayout(new BorderLayout(12, 12));
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
 
        taoTieuDe();
        taoNoiDung();
 
        loadData();
    }
 
    // =====================================================================
    // GIAO DIỆN
    // =====================================================================
 
    // Tạo tiêu đề và dòng mô tả
    private void taoTieuDe() {
 
        JPanel pnlTieuDe = new JPanel();
        pnlTieuDe.setLayout(new BoxLayout(pnlTieuDe, BoxLayout.Y_AXIS));
        pnlTieuDe.setBackground(MAU_NEN);
 
        JLabel lblTieuDe = new JLabel("Quản lý tài khoản");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 23));
        lblTieuDe.setForeground(MAU_XANH);
        lblTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        JLabel lblMoTa = new JLabel(
            "Mỗi nhân viên một tài khoản. Mật khẩu lưu dạng băm, không hiện lại mật khẩu cũ."
        );
        lblMoTa.setFont(new Font("Arial", Font.PLAIN, 13));
        lblMoTa.setForeground(MAU_CHU_PHU);
        lblMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        pnlTieuDe.add(lblTieuDe);
        pnlTieuDe.add(Box.createVerticalStrut(2));
        pnlTieuDe.add(lblMoTa);
 
        add(pnlTieuDe, BorderLayout.NORTH);
    }
 
    // Tạo vùng nội dung: bảng ở giữa, form bên phải
    private void taoNoiDung() {
 
        JPanel pnlNoiDung = new JPanel(new BorderLayout(12, 0));
        pnlNoiDung.setBackground(MAU_NEN);
 
        pnlNoiDung.add(taoBangTaiKhoan(), BorderLayout.CENTER);
        pnlNoiDung.add(taoFormTaiKhoan(), BorderLayout.EAST);
 
        add(pnlNoiDung, BorderLayout.CENTER);
    }
 
    // Tạo bảng danh sách tài khoản (chưa có dữ liệu, dữ liệu nạp ở loadDataLenBang)
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
        tblTaiKhoan.setRowHeight(32);
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 13));
        tblTaiKhoan.setForeground(MAU_XANH);
        tblTaiKhoan.setSelectionBackground(MAU_XAM);
        tblTaiKhoan.setSelectionForeground(MAU_XANH);
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setShowVerticalLines(false);
        tblTaiKhoan.setGridColor(new Color(220, 226, 233));
        tblTaiKhoan.setFillsViewportHeight(true);
 
        // Ô dữ liệu căn trái, có đệm bên trái
        tblTaiKhoan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
 
            private static final long serialVersionUID = 1L;
 
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(
                    table, value, isSelected, false, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
 
        // Tiêu đề cột căn trái, nền xanh, chữ trắng
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setOpaque(true);
        headerRenderer.setBackground(MAU_XANH);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
 
        tblTaiKhoan.getTableHeader().setDefaultRenderer(headerRenderer);
        tblTaiKhoan.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tblTaiKhoan.getTableHeader().setReorderingAllowed(false);
 
        // Sự kiện chọn dòng: this implements ListSelectionListener (xem valueChanged)
        tblTaiKhoan.getSelectionModel().addListSelectionListener(this);
 
        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        scrollPane.setBorder(BorderFactory.createLineBorder(MAU_VIEN));
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        return scrollPane;
    }
 
    // Tạo form bên phải
    private JPanel taoFormTaiKhoan() {
 
        JPanel pnlForm = new JPanel();
        pnlForm.setPreferredSize(new Dimension(340, 0));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );
 
        JLabel lblTieuDe = new JLabel("Tài khoản");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 17));
        lblTieuDe.setForeground(MAU_XANH);
        lblTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        pnlForm.add(lblTieuDe);
        pnlForm.add(Box.createVerticalStrut(10));
 
        // Mã nhân viên (combo box)
        cboMaNhanVien = new JComboBox<String>();
        cboMaNhanVien.setBackground(Color.WHITE);
        cboMaNhanVien.setFont(new Font("Arial", Font.PLAIN, 13));
        cboMaNhanVien.addActionListener(this);
        pnlForm.add(taoTruongNhap("Mã nhân viên", cboMaNhanVien));
 
        // Họ tên (chỉ đọc, tự điền theo mã nhân viên)
        txtHoTen = new JTextField();
        txtHoTen.setEditable(false);
        txtHoTen.setBackground(MAU_XAM);
        txtHoTen.setForeground(MAU_CHU_PHU);
        txtHoTen.setBorder(taoVienONhap());
        pnlForm.add(taoTruongNhap("Họ tên", txtHoTen));
 
        // Mật khẩu mới, có chữ gợi ý khi để trống
        txtMatKhauMoi = new JPasswordField() {
 
            private static final long serialVersionUID = 1L;
 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(130, 140, 150));
                    g2.setFont(getFont());
                    int y = (getHeight()
                        + g2.getFontMetrics().getAscent()
                        - g2.getFontMetrics().getDescent()) / 2;
                    g2.drawString("Nhập mật khẩu mới", getInsets().left, y);
                    g2.dispose();
                }
            }
        };
        txtMatKhauMoi.setFont(new Font("Arial", Font.PLAIN, 13));
        txtMatKhauMoi.setBorder(taoVienONhap());
        pnlForm.add(taoTruongNhap("Mật khẩu mới", txtMatKhauMoi));
 
        // Ghi chú
        JLabel lblGhiChu = new JLabel(
            "<html><body style='width:290px'>"
            + "Nhân viên ngừng làm việc thì tài khoản không đăng nhập được."
            + "</body></html>"
        );
        lblGhiChu.setFont(new Font("Arial", Font.PLAIN, 12));
        lblGhiChu.setForeground(MAU_CHU_PHU);
        lblGhiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlForm.add(lblGhiChu);
 
        pnlForm.add(Box.createVerticalStrut(12));
 
        // Bốn nút xếp lưới 2x2
        taoCacNut();
 
        JPanel pnlNut = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlNut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
 
        pnlNut.add(btnCapMatKhau);
        pnlNut.add(btnDoiMatKhau);
        pnlNut.add(btnXoa);
        pnlNut.add(btnLamMoi);
 
        pnlForm.add(pnlNut);
        pnlForm.add(Box.createVerticalGlue());
 
        return pnlForm;
    }
 
    // Viền ô nhập: đường viền xanh + đệm trái phải
    private javax.swing.border.Border taoVienONhap() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MAU_VIEN),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        );
    }
 
    // Tạo một trường nhập liệu gồm nhãn và ô nhập
    private JPanel taoTruongNhap(String ten, JComponent input) {
 
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        JLabel lbl = new JLabel(ten);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
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
 
    // Tạo các nút chức năng và gắn sự kiện
    private void taoCacNut() {
 
        btnCapMatKhau = taoNut("Cấp mật khẩu", MAU_DO, Color.WHITE);
        btnDoiMatKhau = taoNut("Đổi mật khẩu", MAU_XANH, Color.WHITE);
        btnXoa = taoNut("Xóa", Color.WHITE, MAU_DO);
        btnLamMoi = taoNut("Làm mới", Color.WHITE, MAU_XANH);
 
        btnXoa.setBorder(BorderFactory.createLineBorder(MAU_DO));
        btnLamMoi.setBorder(BorderFactory.createLineBorder(MAU_XANH));
 
        // Gắn sự kiện: this implements ActionListener (xem actionPerformed)
        btnCapMatKhau.addActionListener(this);
        btnDoiMatKhau.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
    }
 
    // Hàm tạo nút dùng chung
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
    // NẠP DỮ LIỆU TỪ CSDL
    // =====================================================================
 
    // Nạp lại toàn bộ dữ liệu của màn hình (FrmChinh có thể gọi hàm này khi mở màn hình)
    public void loadData() {
        loadComboNhanVien();
        loadDataLenBang();
    }
 
    // Nạp danh sách nhân viên vào combo "Mã nhân viên"
    private void loadComboNhanVien() {
 
        cboMaNhanVien.removeAllItems();
        dsHoTenTheoMa.clear();
 
        // TODO [DB]: lấy danh sách nhân viên từ CSDL (bảng NhanVien), ví dụ:
        //   SELECT maNhanVien, ho + ' ' + ten AS hoTen FROM NhanVien
        // Mỗi nhân viên:
        //   dsHoTenTheoMa.put(maNhanVien, hoTen);   // đặt TRƯỚC addItem để họ tên tự điền đúng
        //   cboMaNhanVien.addItem(maNhanVien);
        //
        // Nên đưa vào combo tất cả nhân viên: nhân viên chưa có tài khoản dùng "Cấp mật khẩu",
        // nhân viên đã có tài khoản dùng "Đổi mật khẩu" hoặc "Xóa".
 
        cboMaNhanVien.setSelectedIndex(-1);
        txtHoTen.setText("");
    }
 
    // Nạp danh sách tài khoản lên bảng
    private void loadDataLenBang() {
 
        tableModel.setRowCount(0);
 
        // TODO [DB]: lấy danh sách tài khoản từ CSDL, ví dụ:
        //   SELECT tk.maNhanVien, nv.ho + ' ' + nv.ten AS hoTen, tk.vaiTro, nv.trangThai
        //   FROM TaiKhoan tk INNER JOIN NhanVien nv ON nv.maNhanVien = tk.maNhanVien
        // Mỗi dòng:
        //   tableModel.addRow(new Object[] { maNhanVien, hoTen, vaiTro, trangThaiHienThi });
        //
        // Lưu ý trạng thái: trong CSDL là "Đang hoạt động" / "Ngừng hoạt động",
        // còn trên giao diện hiển thị "Đang làm việc" / "Ngừng làm việc",
        // nên cần đổi chuỗi trước khi addRow.
    }
 
    // =====================================================================
    // SỰ KIỆN
    // =====================================================================
 
    
 
    // Hiển thị thông tin dòng được chọn lên form
    private void hienThiThongTin(int row) {
 
        String maNV = tableModel.getValueAt(row, 0).toString();
 
        cboMaNhanVien.setSelectedItem(maNV);
        txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
        txtMatKhauMoi.setText("");
    }
 
    // Chọn mã nhân viên trong combo thì tự điền họ tên
    private void capNhatHoTenTheoCombo() {
 
        Object dangChon = cboMaNhanVien.getSelectedItem();
 
        if (dangChon == null) {
            txtHoTen.setText("");
            return;
        }
 
        String hoTen = dsHoTenTheoMa.get(dangChon.toString());
        txtHoTen.setText(hoTen == null ? "" : hoTen);
    }
 
    // Nút "Cấp mật khẩu": tạo tài khoản mới cho nhân viên chưa có tài khoản
    private void xuLyCapMatKhau() {
 
        String maNV = layMaNhanVienDangChon();
        if (maNV == null) {
            return;
        }
 
        if (daCoTaiKhoan(maNV)) {
            hienThongBao("Nhân viên này đã có tài khoản. Hãy dùng nút \"Đổi mật khẩu\".");
            return;
        }
 
        String matKhau = layMatKhauNhap();
        if (matKhau == null) {
            return;
        }
 
        // TODO [DB]: băm mật khẩu bằng SHA-256 (chuỗi hex chữ thường, cùng cách với màn hình đăng nhập)
        // rồi thêm tài khoản vào CSDL, ví dụ:
        //   INSERT INTO TaiKhoan (maNhanVien, matKhau, vaiTro) VALUES (?, ?, ?)
        // Bảng TaiKhoan.vaiTro là NOT NULL nhưng form chưa có ô chọn vai trò,
        // nên cần thống nhất giá trị mặc định (ví dụ "Nhân viên thu ngân").
        //
        // if (taiKhoanDAO.capTaiKhoan(maNV, matKhauDaBam, vaiTroMacDinh)) {
        //     hienThongBao("Cấp tài khoản thành công.");
        //     xuLyLamMoi();
        // } else {
        //     hienLoi("Cấp tài khoản thất bại.");
        // }
    }
 
    // Nút "Đổi mật khẩu": đặt mật khẩu mới cho tài khoản đã có
    private void xuLyDoiMatKhau() {
 
        String maNV = layMaNhanVienDangChon();
        if (maNV == null) {
            return;
        }
 
        if (!daCoTaiKhoan(maNV)) {
            hienThongBao("Nhân viên này chưa có tài khoản. Hãy dùng nút \"Cấp mật khẩu\".");
            return;
        }
 
        String matKhau = layMatKhauNhap();
        if (matKhau == null) {
            return;
        }
 
        // TODO [DB]: băm mật khẩu mới bằng SHA-256 rồi cập nhật vào CSDL, ví dụ:
        //   UPDATE TaiKhoan SET matKhau = ? WHERE maNhanVien = ?
        //
        // if (taiKhoanDAO.doiMatKhau(maNV, matKhauDaBam)) {
        //     hienThongBao("Đổi mật khẩu thành công.");
        //     xuLyLamMoi();
        // } else {
        //     hienLoi("Đổi mật khẩu thất bại.");
        // }
    }
 
    // Nút "Xóa": xóa tài khoản của nhân viên đang chọn
    private void xuLyXoa() {
 
        String maNV = layMaNhanVienDangChon();
        if (maNV == null) {
            return;
        }
 
        if (!daCoTaiKhoan(maNV)) {
            hienThongBao("Nhân viên này chưa có tài khoản để xóa.");
            return;
        }
 
        int chon = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa tài khoản của nhân viên " + maNV + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );
 
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
 
        // TODO [DB]: xóa tài khoản khỏi CSDL, ví dụ:
        //   DELETE FROM TaiKhoan WHERE maNhanVien = ?
        // Chỉ xóa tài khoản, không xóa nhân viên.
        //
        // if (taiKhoanDAO.xoaTaiKhoan(maNV)) {
        //     hienThongBao("Xóa tài khoản thành công.");
        //     xuLyLamMoi();
        // } else {
        //     hienLoi("Xóa tài khoản thất bại.");
        // }
    }
 
    // Nút "Làm mới": xóa trắng form, bỏ chọn dòng và nạp lại dữ liệu
    private void xuLyLamMoi() {
 
        tblTaiKhoan.clearSelection();
        cboMaNhanVien.setSelectedIndex(-1);
        txtHoTen.setText("");
        txtMatKhauMoi.setText("");
 
        loadData();
    }
 
    // =====================================================================
    // HÀM HỖ TRỢ
    // =====================================================================
 
    // Lấy mã nhân viên đang chọn trong combo, chưa chọn thì báo và trả về null
    private String layMaNhanVienDangChon() {
 
        Object dangChon = cboMaNhanVien.getSelectedItem();
 
        if (dangChon == null) {
            hienThongBao("Vui lòng chọn mã nhân viên.");
            cboMaNhanVien.requestFocus();
            return null;
        }
 
        return dangChon.toString();
    }
 
    // Lấy mật khẩu đã nhập, để trống thì báo và trả về null
    private String layMatKhauNhap() {
 
        String matKhau = new String(txtMatKhauMoi.getPassword());
 
        if (matKhau.isEmpty()) {
            hienThongBao("Vui lòng nhập mật khẩu mới.");
            txtMatKhauMoi.requestFocus();
            return null;
        }
 
        // TODO: nếu nhóm có quy ước độ dài tối thiểu thì kiểm tra thêm ở đây
        return matKhau;
    }
 
    // Nhân viên đã có tài khoản chưa (dựa vào danh sách đang hiển thị trên bảng)
    private boolean daCoTaiKhoan(String maNV) {
 
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (maNV.equals(tableModel.getValueAt(i, 0).toString())) {
                return true;
            }
        }
 
        return false;
    }
 
    private void hienThongBao(String noiDung) {
        JOptionPane.showMessageDialog(
            this, noiDung, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
 
    @SuppressWarnings("unused")
    private void hienLoi(String noiDung) {
        JOptionPane.showMessageDialog(
            this, noiDung, "Lỗi", JOptionPane.ERROR_MESSAGE);
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
		 
        if (nguon == btnCapMatKhau) {
            xuLyCapMatKhau();
        } else if (nguon == btnDoiMatKhau) {
            xuLyDoiMatKhau();
        } else if (nguon == btnXoa) {
            xuLyXoa();
        } else if (nguon == btnLamMoi) {
            xuLyLamMoi();
        } else if (nguon == cboMaNhanVien) {
            capNhatHoTenTheoCombo();
        }
	}

}

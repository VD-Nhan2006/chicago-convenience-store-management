package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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
import javax.swing.table.DefaultTableModel;

import dao.BanHangDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.ChuongTrinhKhuyenMai;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;
import service.BanHangService;
import service.HoaDonService;

public class BanHang extends JPanel implements ActionListener, ListSelectionListener {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_NEN = new Color(250, 247, 240);
    private static final Color MAU_XANH = new Color(23, 52, 81);
    private static final Color MAU_DO = new Color(233, 77, 61);
    private static final Color MAU_O_KET_QUA = new Color(230, 237, 245);

    private final BanHangService service;
    private JTextField txtKhachHang;
    private JLabel lblTenKhachHang;
    private JTextField txtMaSanPham;
    private JTextField txtTenSanPham;
    private JTextField txtDonViTinh;
    private JTextField txtGiaBan;
    private JTextField txtTonKho;
    private JTextField txtSoLuong;
    private JTextField txtTongTienHang;
    private JTable tblDongHang;
    private DefaultTableModel model;
    private JButton btnChon;
    private JButton btnThemDong;
    private JButton btnSua;
    private JButton btnXoa;
    private JComboBox<String> cboKhuyenMai;
    private JComboBox<String> cboPhuongThuc;
    private JTextField txtTienGiam;
    private JTextField txtTongThanhToan;
    private JTextField txtTienKhachDua;
    private JTextField txtTienThoi;
    private JButton btnThanhToan;
    private JButton btnXemHoaDon;
    private String maHoaDonGanNhat;
    private final HoaDonService hoaDonService = new HoaDonService(new HoaDonDAO());
    private ArrayList<ChuongTrinhKhuyenMai> danhSachKhuyenMai = new ArrayList<ChuongTrinhKhuyenMai>();

    public BanHang() {
        this(new BanHangService(new BanHangDAO()));
    }

    BanHang(BanHangService service) {
        this.service = service;
        setBackground(MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new BorderLayout(12, 12));

        JPanel pnlNhap = new JPanel();
        pnlNhap.setOpaque(false);
        pnlNhap.setLayout(new GridLayout(0, 1, 0, 12));
        add(pnlNhap, BorderLayout.NORTH);

        JPanel pnlTieuDe = new JPanel(new BorderLayout(0, 2));
        pnlTieuDe.setOpaque(false);
        JLabel lblTieuDe = new JLabel("Bán hàng");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setForeground(MAU_XANH);
        JLabel lblMoTa = new JLabel("Thêm dòng, sửa số lượng hoặc xóa dòng trước khi thanh toán.");
        lblMoTa.setForeground(new Color(87, 107, 126));
        pnlTieuDe.add(lblTieuDe, BorderLayout.NORTH);
        pnlTieuDe.add(lblMoTa, BorderLayout.CENTER);
        pnlNhap.add(pnlTieuDe);

        JPanel pnlKhachHang = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlKhachHang.setOpaque(false);
        txtKhachHang = new JTextField("");
        dinhDangOChu(txtKhachHang, true);
        txtKhachHang.setToolTipText("Nhập mã khách hàng hoặc số điện thoại");
        btnChon = new JButton("Chọn");
        dinhDangNut(btnChon, MAU_XANH, true);
        lblTenKhachHang = new JLabel("Chưa chọn khách hàng");
        lblTenKhachHang.setForeground(MAU_XANH);
        JPanel pnlMaKhachHang = new JPanel(new BorderLayout(0, 4));
        JLabel lblMaKhachHang = new JLabel("Khách hàng");
        dinhDangTruong(pnlMaKhachHang, lblMaKhachHang, txtKhachHang, 180);
        pnlMaKhachHang.add(lblMaKhachHang, BorderLayout.NORTH);
        pnlMaKhachHang.add(txtKhachHang, BorderLayout.CENTER);
        pnlKhachHang.add(pnlMaKhachHang);
        JPanel pnlChon = new JPanel(new BorderLayout(0, 4));
        JLabel lblChon = new JLabel(" ");
        dinhDangTruong(pnlChon, lblChon, btnChon, 72);
        pnlChon.add(lblChon, BorderLayout.NORTH);
        pnlChon.add(btnChon, BorderLayout.CENTER);
        pnlKhachHang.add(pnlChon);
        JPanel pnlTenKhachHang = new JPanel(new BorderLayout(0, 4));
        JLabel lblTenKhachHangTrong = new JLabel(" ");
        dinhDangTruong(pnlTenKhachHang, lblTenKhachHangTrong, lblTenKhachHang, 210);
        pnlTenKhachHang.add(lblTenKhachHangTrong, BorderLayout.NORTH);
        pnlTenKhachHang.add(lblTenKhachHang, BorderLayout.CENTER);
        pnlKhachHang.add(pnlTenKhachHang);
        pnlNhap.add(pnlKhachHang);

        JPanel pnlSanPham = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSanPham.setOpaque(false);
        txtMaSanPham = new JTextField("");
        dinhDangOChu(txtMaSanPham, true);
        txtMaSanPham.setToolTipText("Nhập mã sản phẩm rồi nhấn Enter để tra cứu");
        txtTenSanPham = new JTextField("");
        dinhDangOChu(txtTenSanPham, false);
        txtDonViTinh = new JTextField("");
        dinhDangOChu(txtDonViTinh, false);
        txtGiaBan = new JTextField("");
        dinhDangOChu(txtGiaBan, false);
        txtTonKho = new JTextField("");
        dinhDangOChu(txtTonKho, false);
        txtSoLuong = new JTextField("1");
        dinhDangOChu(txtSoLuong, true);
        btnThemDong = new JButton("Thêm dòng");
        dinhDangNut(btnThemDong, MAU_DO, true);
        JPanel pnlMaSanPham = new JPanel(new BorderLayout(0, 4));
        JLabel lblMaSanPham = new JLabel("Mã SP");
        dinhDangTruong(pnlMaSanPham, lblMaSanPham, txtMaSanPham, 130);
        pnlMaSanPham.add(lblMaSanPham, BorderLayout.NORTH);
        pnlMaSanPham.add(txtMaSanPham, BorderLayout.CENTER);
        pnlSanPham.add(pnlMaSanPham);
        JPanel pnlTenSanPham = new JPanel(new BorderLayout(0, 4));
        JLabel lblTenSanPham = new JLabel("Tên");
        dinhDangTruong(pnlTenSanPham, lblTenSanPham, txtTenSanPham, 180);
        pnlTenSanPham.add(lblTenSanPham, BorderLayout.NORTH);
        pnlTenSanPham.add(txtTenSanPham, BorderLayout.CENTER);
        pnlSanPham.add(pnlTenSanPham);
        JPanel pnlDonViTinh = new JPanel(new BorderLayout(0, 4));
        JLabel lblDonViTinh = new JLabel("ĐVT");
        dinhDangTruong(pnlDonViTinh, lblDonViTinh, txtDonViTinh, 64);
        pnlDonViTinh.add(lblDonViTinh, BorderLayout.NORTH);
        pnlDonViTinh.add(txtDonViTinh, BorderLayout.CENTER);
        pnlSanPham.add(pnlDonViTinh);
        JPanel pnlGiaBan = new JPanel(new BorderLayout(0, 4));
        JLabel lblGiaBan = new JLabel("Giá bán");
        dinhDangTruong(pnlGiaBan, lblGiaBan, txtGiaBan, 80);
        pnlGiaBan.add(lblGiaBan, BorderLayout.NORTH);
        pnlGiaBan.add(txtGiaBan, BorderLayout.CENTER);
        pnlSanPham.add(pnlGiaBan);
        JPanel pnlTonKho = new JPanel(new BorderLayout(0, 4));
        JLabel lblTonKho = new JLabel("Tồn");
        dinhDangTruong(pnlTonKho, lblTonKho, txtTonKho, 52);
        pnlTonKho.add(lblTonKho, BorderLayout.NORTH);
        pnlTonKho.add(txtTonKho, BorderLayout.CENTER);
        pnlSanPham.add(pnlTonKho);
        JPanel pnlSoLuong = new JPanel(new BorderLayout(0, 4));
        JLabel lblSoLuong = new JLabel("Số lượng");
        dinhDangTruong(pnlSoLuong, lblSoLuong, txtSoLuong, 70);
        pnlSoLuong.add(lblSoLuong, BorderLayout.NORTH);
        pnlSoLuong.add(txtSoLuong, BorderLayout.CENTER);
        pnlSanPham.add(pnlSoLuong);
        JPanel pnlThemDong = new JPanel(new BorderLayout(0, 4));
        JLabel lblThemDong = new JLabel(" ");
        dinhDangTruong(pnlThemDong, lblThemDong, btnThemDong, 110);
        pnlThemDong.add(lblThemDong, BorderLayout.NORTH);
        pnlThemDong.add(btnThemDong, BorderLayout.CENTER);
        pnlSanPham.add(pnlThemDong);
        pnlNhap.add(pnlSanPham);

        JPanel pnlHoaDon = new JPanel(new BorderLayout(12, 0));
        pnlHoaDon.setOpaque(false);
        add(pnlHoaDon, BorderLayout.CENTER);

        JPanel pnlDongHang = new JPanel(new BorderLayout(0, 8));
        pnlDongHang.setOpaque(false);
        pnlHoaDon.add(pnlDongHang, BorderLayout.CENTER);

        String[] cot = {"Mã SP", "Tên", "ĐVT", "SL", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(cot, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDongHang = new JTable(model);
        tblDongHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDongHang.setRowHeight(30);
        tblDongHang.setSelectionBackground(MAU_O_KET_QUA);
        tblDongHang.getTableHeader().setBackground(MAU_XANH);
        tblDongHang.getTableHeader().setForeground(Color.WHITE);
        tblDongHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblDongHang.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tblDongHang.getColumnModel().getColumn(1).setPreferredWidth(210);
        tblDongHang.getColumnModel().getColumn(3).setPreferredWidth(40);
        JScrollPane scrDongHang = new JScrollPane(tblDongHang);
        scrDongHang.getViewport().setBackground(Color.WHITE);
        scrDongHang.setBorder(BorderFactory.createLineBorder(MAU_XANH));
        pnlDongHang.add(scrDongHang, BorderLayout.CENTER);

        JPanel pnlSuaDong = new JPanel(new GridLayout(1, 2, 8, 0));
        btnXoa = new JButton("Xóa dòng");
        dinhDangNut(btnXoa, MAU_DO, false);
        btnSua = new JButton("Sửa số lượng");
        dinhDangNut(btnSua, MAU_XANH, false);
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);
        pnlSuaDong.add(btnXoa);
        pnlSuaDong.add(btnSua);
        pnlDongHang.add(pnlSuaDong, BorderLayout.SOUTH);

        JPanel pnlThanhToan = new JPanel(new BorderLayout());
        pnlThanhToan.setPreferredSize(new Dimension(300, 0));
        pnlThanhToan.setBackground(Color.WHITE);
        pnlThanhToan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_XANH),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlHoaDon.add(pnlThanhToan, BorderLayout.EAST);

        JPanel pnlThongTinThanhToan = new JPanel();
        pnlThongTinThanhToan.setOpaque(false);
        pnlThongTinThanhToan.setLayout(new BoxLayout(pnlThongTinThanhToan, BoxLayout.Y_AXIS));
        JScrollPane scrThanhToan = new JScrollPane(pnlThongTinThanhToan);
        scrThanhToan.setBorder(BorderFactory.createEmptyBorder());
        pnlThanhToan.add(scrThanhToan, BorderLayout.CENTER);

        JLabel lblThanhToan = new JLabel("Thanh toán");
        lblThanhToan.setFont(new Font("Arial", Font.BOLD, 16));
        lblThanhToan.setForeground(MAU_XANH);
        pnlThongTinThanhToan.add(lblThanhToan);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(8));

        txtTongTienHang = new JTextField("0");
        dinhDangOChu(txtTongTienHang, false);
        cboKhuyenMai = new JComboBox<String>(new String[]{"Không áp dụng"});
        txtTienGiam = new JTextField("0");
        dinhDangOChu(txtTienGiam, false);
        txtTongThanhToan = new JTextField("0");
        dinhDangOChu(txtTongThanhToan, false);
        cboPhuongThuc = new JComboBox<String>(new String[]{"Tiền mặt", "Chuyển khoản"});
        txtTienKhachDua = new JTextField("");
        dinhDangOChu(txtTienKhachDua, true);
        txtTienThoi = new JTextField("0");
        dinhDangOChu(txtTienThoi, false);
        btnThanhToan = new JButton("Thanh toán");
        dinhDangNut(btnThanhToan, MAU_DO, true);
        btnThanhToan.setEnabled(false);
        JPanel pnlTongTienHang = new JPanel(new BorderLayout(0, 4));
        JLabel lblTongTienHang = new JLabel("Tổng tiền hàng");
        dinhDangTruong(pnlTongTienHang, lblTongTienHang, txtTongTienHang, 274);
        pnlTongTienHang.add(lblTongTienHang, BorderLayout.NORTH);
        pnlTongTienHang.add(txtTongTienHang, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlTongTienHang);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlKhuyenMai = new JPanel(new BorderLayout(0, 4));
        JLabel lblKhuyenMai = new JLabel("Khuyến mãi");
        dinhDangTruong(pnlKhuyenMai, lblKhuyenMai, cboKhuyenMai, 274);
        pnlKhuyenMai.add(lblKhuyenMai, BorderLayout.NORTH);
        pnlKhuyenMai.add(cboKhuyenMai, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlKhuyenMai);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlTienGiam = new JPanel(new BorderLayout(0, 4));
        JLabel lblTienGiam = new JLabel("Tiền được giảm");
        dinhDangTruong(pnlTienGiam, lblTienGiam, txtTienGiam, 274);
        pnlTienGiam.add(lblTienGiam, BorderLayout.NORTH);
        pnlTienGiam.add(txtTienGiam, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlTienGiam);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlTongThanhToan = new JPanel(new BorderLayout(0, 4));
        JLabel lblTongThanhToan = new JLabel("Tổng thanh toán (gồm VAT 8%)");
        dinhDangTruong(pnlTongThanhToan, lblTongThanhToan, txtTongThanhToan, 274);
        pnlTongThanhToan.add(lblTongThanhToan, BorderLayout.NORTH);
        pnlTongThanhToan.add(txtTongThanhToan, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlTongThanhToan);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlPhuongThuc = new JPanel(new BorderLayout(0, 4));
        JLabel lblPhuongThuc = new JLabel("Phương thức");
        dinhDangTruong(pnlPhuongThuc, lblPhuongThuc, cboPhuongThuc, 274);
        pnlPhuongThuc.add(lblPhuongThuc, BorderLayout.NORTH);
        pnlPhuongThuc.add(cboPhuongThuc, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlPhuongThuc);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlTienKhachDua = new JPanel(new BorderLayout(0, 4));
        JLabel lblTienKhachDua = new JLabel("Tiền khách đưa");
        dinhDangTruong(pnlTienKhachDua, lblTienKhachDua, txtTienKhachDua, 274);
        pnlTienKhachDua.add(lblTienKhachDua, BorderLayout.NORTH);
        pnlTienKhachDua.add(txtTienKhachDua, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlTienKhachDua);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(6));
        JPanel pnlTienThoi = new JPanel(new BorderLayout(0, 4));
        JLabel lblTienThoi = new JLabel("Tiền thối");
        dinhDangTruong(pnlTienThoi, lblTienThoi, txtTienThoi, 274);
        pnlTienThoi.add(lblTienThoi, BorderLayout.NORTH);
        pnlTienThoi.add(txtTienThoi, BorderLayout.CENTER);
        pnlThongTinThanhToan.add(pnlTienThoi);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(8));
        btnThanhToan.setMaximumSize(new Dimension(274, 34));
        pnlThongTinThanhToan.add(btnThanhToan);
        pnlThongTinThanhToan.add(Box.createVerticalStrut(8));
        btnXemHoaDon = new JButton("Xem / xuất hóa đơn");
        dinhDangNut(btnXemHoaDon, MAU_XANH, false);
        btnXemHoaDon.setMaximumSize(new Dimension(274, 34));
        pnlThongTinThanhToan.add(btnXemHoaDon);

        btnChon.addActionListener(this);
        txtKhachHang.addActionListener(this);
        txtMaSanPham.addActionListener(this);
        btnThemDong.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        cboKhuyenMai.addActionListener(this);
        cboPhuongThuc.addActionListener(this);
        txtTienKhachDua.addActionListener(this);
        btnThanhToan.addActionListener(this);
        btnXemHoaDon.addActionListener(this);
        tblDongHang.getSelectionModel().addListSelectionListener(this);
    }

    private void dinhDangTruong(JPanel panel, JLabel label, JComponent oNhap, int chieuRong) {
        panel.setOpaque(false);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        label.setForeground(MAU_XANH);
        label.setLabelFor(oNhap);
        oNhap.setPreferredSize(new Dimension(chieuRong, 32));
    }

    private void dinhDangOChu(JTextField txt, boolean choNhap) {
        txt.setForeground(MAU_XANH);
        txt.setBackground(choNhap ? Color.WHITE : MAU_O_KET_QUA);
        txt.setEditable(choNhap);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_XANH),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)));
    }

    private void dinhDangNut(JButton btn, Color mau, boolean nenDac) {
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(nenDac ? Color.WHITE : mau);
        btn.setBackground(nenDac ? mau : Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(mau));
        btn.setPreferredSize(new Dimension(110, 34));
        btn.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object nguon = event.getSource();
        try {
            if (nguon == btnChon || nguon == txtKhachHang) {
                lblTenKhachHang.setText("Chưa chọn khách hàng");
                KhachHang khachHang = service.chonKhachHang(txtKhachHang.getText().trim());
                lblTenKhachHang.setText(khachHang.getHo() + " " + khachHang.getTen());
            } else if (nguon == txtMaSanPham) {
                hienThiSanPham(null);
                hienThiSanPham(service.timSanPham(txtMaSanPham.getText().trim()));
                txtSoLuong.requestFocusInWindow();
            } else if (nguon == btnThemDong) {
                boolean dongDauTien = !service.coDongHang();
                service.themDong(txtMaSanPham.getText().trim(), docSoLuong());
                capNhatBang();
                xoaNhapSanPham();
                if (dongDauTien) {
                    napKhuyenMai();
                }
            } else if (nguon == btnSua && tblDongHang.getSelectedRow() >= 0) {
                int viTri = tblDongHang.getSelectedRow();
                service.suaSoLuong(viTri, docSoLuong());
                capNhatBang();
                tblDongHang.setRowSelectionInterval(viTri, viTri);
            } else if (nguon == btnXoa && tblDongHang.getSelectedRow() >= 0) {
                service.xoaDong(tblDongHang.getSelectedRow());
                capNhatBang();
                xoaNhapSanPham();
            } else if (nguon == cboKhuyenMai) {
                int viTri = cboKhuyenMai.getSelectedIndex() - 1;
                service.chonKhuyenMai(viTri < 0 ? null : danhSachKhuyenMai.get(viTri));
                capNhatTienThanhToan();
            } else if (nguon == cboPhuongThuc) {
                txtTienKhachDua.setText("");
                capNhatTienThanhToan();
            } else if (nguon == txtTienKhachDua) {
                capNhatTienThanhToan();
            } else if (nguon == btnThanhToan) {
                thanhToanHoaDon();
            } else if (nguon == btnXemHoaDon) {
                String ma = JOptionPane.showInputDialog(this, "Nhập mã hóa đơn", maHoaDonGanNhat);
                if (ma != null && !ma.trim().isEmpty()) {
                    xemHoaDon(ma.trim());
                }
            }
        } catch (SQLException ex) {
            String thongBao = nguon == btnThanhToan
                    ? "Không xác nhận được thanh toán. Kiểm tra hóa đơn đã lưu trước khi thử lại."
                    : "Không đọc được dữ liệu bán hàng từ cơ sở dữ liệu.";
            JOptionPane.showMessageDialog(this, thongBao,
                    "Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Bán hàng", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int viTri = tblDongHang.getSelectedRow();
        btnXoa.setEnabled(viTri >= 0);
        btnSua.setEnabled(viTri >= 0);
        if (viTri >= 0) {
            ChiTietHoaDon dong = service.layDongHang().get(viTri);
            txtMaSanPham.setText(dong.getSanPham().getMaSanPham());
            hienThiSanPham(dong.getSanPham());
            txtGiaBan.setText(dong.getGiaBan().toPlainString());
            txtSoLuong.setText(String.valueOf(dong.getSoLuong()));
        }
    }

    private int docSoLuong() {
        try {
            return Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Số lượng phải là số nguyên dương.");
        }
    }

    private void capNhatBang() {
        tblDongHang.clearSelection();
        model.setRowCount(0);
        for (ChiTietHoaDon dong : service.layDongHang()) {
            SanPham sp = dong.getSanPham();
            model.addRow(new Object[]{sp.getMaSanPham(), sp.getTenSanPham(), sp.getDonViTinh(),
                    dong.getSoLuong(), dong.getGiaBan(), dong.tinhThanhTien()});
        }
        txtTongTienHang.setText(service.tinhTongTienHang().toPlainString());
        btnThanhToan.setEnabled(service.coDongHang());
        capNhatTienThanhToan();
    }

    private void napKhuyenMai() throws SQLException {
        danhSachKhuyenMai = service.layKhuyenMai();
        cboKhuyenMai.removeAllItems();
        cboKhuyenMai.addItem("Không áp dụng");
        for (ChuongTrinhKhuyenMai khuyenMai : danhSachKhuyenMai) {
            cboKhuyenMai.addItem(khuyenMai.getTenChuongTrinh() + " (" + khuyenMai.getMaKM() + ")");
        }
    }

    private void capNhatTienThanhToan() {
        BigDecimal tong = service.tinhTongThanhToan();
        txtTienGiam.setText(service.tinhTienGiam().toPlainString());
        txtTongThanhToan.setText(tong.toPlainString());
        boolean tienMat = cboPhuongThuc.getSelectedIndex() == 0;
        txtTienKhachDua.setEditable(tienMat);
        if (!tienMat) {
            txtTienKhachDua.setText(tong.toPlainString());
        }
        String tienNhap = txtTienKhachDua.getText().trim();
        BigDecimal tienThoi = tienNhap.matches("[0-9]{1,18}")
                ? service.tinhTienThoi(new BigDecimal(tienNhap)) : BigDecimal.ZERO;
        txtTienThoi.setText(tienThoi.toPlainString());
    }

    private void thanhToanHoaDon() throws SQLException {
        if (!service.daChonKhachHang(txtKhachHang.getText().trim())) {
            throw new IllegalArgumentException("Bấm Chọn để xác nhận khách hàng trên hóa đơn.");
        }
        String phuongThuc = (String) cboPhuongThuc.getSelectedItem();
        String tienNhap = txtTienKhachDua.getText().trim();
        BigDecimal tienKhachDua = null;
        if ("Tiền mặt".equals(phuongThuc)) {
            if (!tienNhap.matches("[0-9]{1,18}")) {
                throw new IllegalArgumentException("Nhập tiền khách đưa bằng số nguyên tối đa 18 chữ số.");
            }
            tienKhachDua = new BigDecimal(tienNhap);
            if (tienKhachDua.compareTo(service.tinhTongThanhToan()) < 0) {
                throw new IllegalArgumentException("Tiền khách đưa chưa đủ.");
            }
        }
        capNhatTienThanhToan();
        ArrayList<NhanVien> nhanVien = service.layNhanVienHoatDong();
        if (nhanVien.isEmpty()) {
            throw new IllegalArgumentException("Không có nhân viên đang hoạt động.");
        }
        String[] luaChon = new String[nhanVien.size()];
        for (int i = 0; i < nhanVien.size(); i++) {
            NhanVien nv = nhanVien.get(i);
            luaChon[i] = nv.getMaNhanVien() + " - " + nv.getHo() + " " + nv.getTen();
        }
        Object daChon = JOptionPane.showInputDialog(this,
                "Chọn nhân viên lập hóa đơn. Tổng thanh toán: " + txtTongThanhToan.getText()
                        + " đ. Tiền thối: " + txtTienThoi.getText() + " đ.",
                "Xác nhận thanh toán", JOptionPane.QUESTION_MESSAGE, null, luaChon, null);
        for (int i = 0; i < luaChon.length; i++) {
            if (luaChon[i].equals(daChon)) {
                String ma = service.thanhToan(phuongThuc, tienKhachDua, nhanVien.get(i));
                txtKhachHang.setText("");
                lblTenKhachHang.setText("Chưa chọn khách hàng");
                txtTienKhachDua.setText("");
                cboKhuyenMai.setSelectedIndex(0);
                cboPhuongThuc.setSelectedIndex(0);
                capNhatBang();
                xoaNhapSanPham();
                sauThanhToan(ma);
                return;
            }
        }
    }

    private void sauThanhToan(String ma) {
        maHoaDonGanNhat = ma;
        int luaChon = JOptionPane.showConfirmDialog(this, "Đã lưu hóa đơn " + ma + ". Mở hóa đơn để xem hoặc xuất tệp?",
                "Thanh toán thành công", JOptionPane.YES_NO_OPTION);
        if (luaChon == JOptionPane.YES_OPTION) {
            xemHoaDon(ma);
        }
    }

    private void xemHoaDon(String ma) {
        try {
            HoaDon hoaDon = hoaDonService.layHoaDonDaThanhToan(ma);
            new HoaDonDialog(JOptionPane.getFrameForComponent(this), hoaDon).setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không đọc được hóa đơn " + ma + ".",
                    "Lỗi cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hóa đơn", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void hienThiSanPham(SanPham sp) {
        txtTenSanPham.setText(sp == null ? "" : sp.getTenSanPham());
        txtDonViTinh.setText(sp == null ? "" : sp.getDonViTinh());
        txtGiaBan.setText(sp == null ? "" : sp.getGiaBan().toPlainString());
        txtTonKho.setText(sp == null ? "" : String.valueOf(sp.getSoLuongTon()));
    }

    private void xoaNhapSanPham() {
        txtMaSanPham.setText("");
        txtSoLuong.setText("1");
        hienThiSanPham(null);
        txtMaSanPham.requestFocusInWindow();
    }
}

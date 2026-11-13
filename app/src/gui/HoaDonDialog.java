package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import service.HoaDonService;

public class HoaDonDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final Color MAU_XANH = new Color(23, 52, 81);
    private static final Color MAU_NEN = new Color(230, 237, 245);
    private static final Color MAU_VIEN = new Color(177, 194, 211);
    private final HoaDon hoaDon;
    private final JButton btnXuat;

    public HoaDonDialog(Frame chu, HoaDon hoaDon) {
        super(chu, "Hóa đơn " + hoaDon.getMaHoaDon(), true);
        this.hoaDon = hoaDon;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel trang = new JPanel();
        trang.setBackground(Color.WHITE);
        trang.setLayout(new BoxLayout(trang, BoxLayout.Y_AXIS));
        trang.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        themMuc(trang, taoDauTrang());
        JLabel tieuDe = nhan("HÓA ĐƠN BÁN HÀNG", 32, Font.BOLD);
        tieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel pnlTieuDe = new JPanel(new BorderLayout());
        pnlTieuDe.setOpaque(false);
        pnlTieuDe.add(tieuDe, BorderLayout.CENTER);
        themMuc(trang, pnlTieuDe);
        themMuc(trang, taoThongTin());
        themMuc(trang, taoChiTiet());
        trang.add(taoDongGhiChu());

        JScrollPane cuon = new JScrollPane(trang);
        cuon.setBorder(BorderFactory.createEmptyBorder());
        cuon.setPreferredSize(new Dimension(820, 680));
        add(cuon, BorderLayout.CENTER);

        JPanel thaoTac = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        thaoTac.setBackground(Color.WHITE);
        JButton btnDong = new JButton("Đóng");
        btnXuat = new JButton("Xuất hóa đơn (.txt)");
        thaoTac.add(btnDong);
        thaoTac.add(btnXuat);
        add(thaoTac, BorderLayout.SOUTH);
        btnDong.addActionListener(this);
        btnXuat.addActionListener(this);
        pack();
        setLocationRelativeTo(chu);
    }

    private JPanel taoDauTrang() {
        JPanel dauTrang = new JPanel(new BorderLayout(20, 0));
        dauTrang.setBackground(Color.WHITE);
        dauTrang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, MAU_VIEN),
                BorderFactory.createEmptyBorder(0, 0, 14, 0)));
        ImageIcon anh = new ImageIcon(HoaDonDialog.class.getResource("/image/LogoNenTrang.png"));
        Image logo = anh.getImage().getScaledInstance(132, -1, Image.SCALE_SMOOTH);
        dauTrang.add(new JLabel(new ImageIcon(logo)), BorderLayout.WEST);

        JPanel cuaHang = new JPanel(new GridLayout(4, 1, 0, 3));
        cuaHang.setOpaque(false);
        cuaHang.add(nhan("CỬA HÀNG TIỆN LỢI CHICAGO", 23, Font.BOLD));
        cuaHang.add(nhan("Địa chỉ          —", 13, Font.PLAIN));
        cuaHang.add(nhan("Điện thoại     —", 13, Font.PLAIN));
        cuaHang.add(nhan("Mã số thuế   —", 13, Font.PLAIN));
        dauTrang.add(cuaHang, BorderLayout.CENTER);
        return dauTrang;
    }

    private JPanel taoThongTin() {
        JPanel thongTin = new JPanel(new GridLayout(1, 2, 18, 0));
        thongTin.setBackground(MAU_NEN);
        thongTin.setBorder(khung(14));
        JPanel trai = new JPanel(new GridLayout(4, 1, 0, 7));
        JPanel phai = new JPanel(new GridLayout(4, 1, 0, 7));
        trai.setOpaque(false);
        phai.setOpaque(false);
        trai.add(taoTruong("Mã hóa đơn", hoaDon.getMaHoaDon()));
        trai.add(taoTruong("Ngày lập", hoaDon.getNgayGioLap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        trai.add(taoTruong("Nhân viên", hoaDon.getNhanVien().getHo() + " " + hoaDon.getNhanVien().getTen()));
        trai.add(taoTruong("Khách hàng", hoaDon.getKhachHang().getHo() + " " + hoaDon.getKhachHang().getTen()));
        phai.add(taoTruong("SĐT khách hàng", hoaDon.getKhachHang().getSoDienThoai()));
        phai.add(taoTruong("Phương thức", hoaDon.getPhuongThucThanhToan()));
        phai.add(taoTruong("Trạng thái", hoaDon.getTrangThaiThanhToan()));
        phai.add(Box.createVerticalStrut(28));
        thongTin.add(trai);
        thongTin.add(phai);
        return thongTin;
    }

    private JPanel taoTruong(String ten, String giaTri) {
        JPanel dong = new JPanel(new BorderLayout(8, 0));
        dong.setOpaque(false);
        JLabel tieuDe = nhan(ten, 13, Font.BOLD);
        tieuDe.setPreferredSize(new Dimension(132, 28));
        JLabel noiDung = nhan(giaTri, 13, Font.PLAIN);
        noiDung.setToolTipText(giaTri);
        noiDung.setOpaque(true);
        noiDung.setBackground(Color.WHITE);
        noiDung.setBorder(khung(5));
        dong.add(tieuDe, BorderLayout.WEST);
        dong.add(noiDung, BorderLayout.CENTER);
        return dong;
    }

    private JPanel taoChiTiet() {
        String[] cot = {"STT", "Mã SP", "Tên sản phẩm", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(cot, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int stt = 1;
        long tongSoLuong = 0;
        for (ChiTietHoaDon dong : hoaDon.getListChiTietHoaDon()) {
            SanPham sp = dong.getSanPham();
            model.addRow(new Object[]{stt++, sp.getMaSanPham(), sp.getTenSanPham(), sp.getDonViTinh(),
                    dong.getSoLuong(), tien(dong.getGiaBan()), tien(dong.tinhThanhTien())});
            tongSoLuong += dong.getSoLuong();
        }
        while (model.getRowCount() < 6) {
            model.addRow(new Object[cot.length]);
        }
        JTable bang = new JTable(model);
        bang.setRowHeight(34);
        bang.setForeground(MAU_XANH);
        bang.setGridColor(MAU_VIEN);
        bang.setRowSelectionAllowed(false);
        bang.getTableHeader().setBackground(MAU_NEN);
        bang.getTableHeader().setForeground(MAU_XANH);
        bang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bang.getTableHeader().setReorderingAllowed(false);
        int[] rongCot = {46, 103, 190, 56, 78, 112, 137};
        for (int i = 0; i < rongCot.length; i++) {
            bang.getColumnModel().getColumn(i).setPreferredWidth(rongCot[i]);
        }
        JPanel chiTiet = new JPanel(new BorderLayout());
        chiTiet.setBackground(Color.WHITE);
        chiTiet.setBorder(BorderFactory.createLineBorder(MAU_VIEN));
        chiTiet.add(bang.getTableHeader(), BorderLayout.NORTH);
        chiTiet.add(bang, BorderLayout.CENTER);

        BigDecimal tongHang = hoaDon.tinhTongTienSP();
        BigDecimal giam = hoaDon.tinhTienKhuyenMai(tongHang);
        BigDecimal tong = hoaDon.tinhTongTienThanhToan();
        JPanel tomTat = new JPanel(new GridLayout(7, 1, 0, 5));
        tomTat.setOpaque(false);
        tomTat.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 12));
        tomTat.add(taoTongTien("Tổng số lượng", String.valueOf(tongSoLuong), false));
        tomTat.add(taoTongTien("Tổng tiền hàng", tien(tongHang), false));
        tomTat.add(taoTongTien("Tiền được giảm", tien(giam), false));
        tomTat.add(taoTongTien("VAT 8%", tien(tong.subtract(tongHang.subtract(giam))), false));
        tomTat.add(taoTongTien("Tiền khách đưa", tien(hoaDon.getTienKhachDua()), false));
        tomTat.add(taoTongTien("Tiền thối", tien(hoaDon.tinhTienThoi()), false));
        tomTat.add(taoTongTien("TỔNG THANH TOÁN", tien(tong), true));
        JPanel chanBang = new JPanel(new BorderLayout());
        chanBang.setOpaque(false);
        chanBang.add(tomTat, BorderLayout.EAST);
        chiTiet.add(chanBang, BorderLayout.SOUTH);
        return chiTiet;
    }

    private JPanel taoTongTien(String ten, String giaTri, boolean noiBat) {
        JPanel dong = new JPanel(new BorderLayout(12, 0));
        dong.setOpaque(noiBat);
        dong.setBackground(MAU_NEN);
        dong.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        JLabel tieuDe = nhan(ten, noiBat ? 16 : 14, noiBat ? Font.BOLD : Font.PLAIN);
        tieuDe.setPreferredSize(new Dimension(205, 22));
        JLabel soTien = nhan(giaTri, noiBat ? 18 : 14, Font.BOLD);
        soTien.setHorizontalAlignment(SwingConstants.RIGHT);
        dong.add(tieuDe, BorderLayout.WEST);
        dong.add(soTien, BorderLayout.CENTER);
        return dong;
    }

    private JPanel taoDongGhiChu() {
        JPanel ghiChu = new JPanel(new BorderLayout(16, 0));
        ghiChu.setBackground(MAU_NEN);
        ghiChu.setBorder(khung(10));
        ghiChu.setAlignmentX(LEFT_ALIGNMENT);
        ghiChu.add(nhan("Ghi chú", 14, Font.BOLD), BorderLayout.WEST);
        ghiChu.add(nhan("—", 14, Font.PLAIN), BorderLayout.CENTER);
        return ghiChu;
    }

    private void themMuc(JPanel trang, JPanel muc) {
        muc.setAlignmentX(LEFT_ALIGNMENT);
        trang.add(muc);
        trang.add(Box.createVerticalStrut(16));
    }

    private javax.swing.border.Border khung(int dem) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(dem, dem, dem, dem));
    }

    private JLabel nhan(String noiDung, int coChu, int kieuChu) {
        JLabel nhan = new JLabel(noiDung);
        nhan.setForeground(MAU_XANH);
        nhan.setFont(new Font("Arial", kieuChu, coChu));
        return nhan;
    }

    private String tien(BigDecimal giaTri) {
        return giaTri.toPlainString() + " đ";
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() != btnXuat) {
            dispose();
            return;
        }
        try {
            File tep = HoaDonService.xuatHoaDonVaoDuAn(hoaDon);
            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn: " + tep.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không lưu được hóa đơn trong thư mục HoaDon của dự án.",
                    "Lỗi xuất hóa đơn", JOptionPane.ERROR_MESSAGE);
        }
    }
}

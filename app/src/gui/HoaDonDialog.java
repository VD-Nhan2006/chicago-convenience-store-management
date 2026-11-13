package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
    private final HoaDon hoaDon;
    private final JButton btnXuat;

    public HoaDonDialog(Frame chu, HoaDon hoaDon) {
        super(chu, "Hóa đơn " + hoaDon.getMaHoaDon(), true);
        this.hoaDon = hoaDon;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel noiDung = new JPanel(new BorderLayout(0, 12));
        noiDung.setBackground(Color.WHITE);
        noiDung.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setContentPane(noiDung);

        JPanel tieuDe = new JPanel(new BorderLayout(0, 4));
        tieuDe.setOpaque(false);
        JLabel cuaHang = new JLabel("CỬA HÀNG TIỆN LỢI CHICAGO", SwingConstants.CENTER);
        cuaHang.setForeground(MAU_XANH);
        cuaHang.setFont(new Font("Arial", Font.BOLD, 17));
        JLabel tenHoaDon = new JLabel("HÓA ĐƠN BÁN HÀNG", SwingConstants.CENTER);
        tenHoaDon.setForeground(MAU_XANH);
        tenHoaDon.setFont(new Font("Arial", Font.BOLD, 24));
        tieuDe.add(cuaHang, BorderLayout.NORTH);
        tieuDe.add(tenHoaDon, BorderLayout.CENTER);
        noiDung.add(tieuDe, BorderLayout.NORTH);

        JTable bang = new JTable(taoNoiDungHoaDon(hoaDon));
        bang.setTableHeader(null);
        bang.setRowHeight(28);
        bang.setFocusable(false);
        bang.setRowSelectionAllowed(false);
        bang.setGridColor(new Color(207, 220, 233));
        bang.setFillsViewportHeight(true);
        int[] rongCot = {130, 94, 235, 48, 108, 82, 122};
        for (int i = 0; i < rongCot.length; i++) {
            bang.getColumnModel().getColumn(i).setPreferredWidth(rongCot[i]);
        }
        JScrollPane cuon = new JScrollPane(bang);
        cuon.setPreferredSize(new Dimension(820, 430));
        cuon.setBorder(BorderFactory.createLineBorder(new Color(207, 220, 233)));
        noiDung.add(cuon, BorderLayout.CENTER);

        JPanel thaoTac = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        thaoTac.setOpaque(false);
        JButton btnDong = new JButton("Đóng");
        btnXuat = new JButton("Xuất hóa đơn (.txt)");
        thaoTac.add(btnDong);
        thaoTac.add(btnXuat);
        noiDung.add(thaoTac, BorderLayout.SOUTH);
        btnDong.addActionListener(this);
        btnXuat.addActionListener(this);
        pack();
        setLocationRelativeTo(chu);
    }

    static DefaultTableModel taoNoiDungHoaDon(HoaDon hd) {
        DefaultTableModel model = new DefaultTableModel(0, 7) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addRow(new Object[]{"Mã hóa đơn", "", hd.getMaHoaDon(), "", "SĐT khách hàng", "",
                hd.getKhachHang().getSoDienThoai()});
        model.addRow(new Object[]{"Ngày lập", "", hd.getNgayGioLap().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), "", "Phương thức", "",
                hd.getPhuongThucThanhToan()});
        model.addRow(new Object[]{"Nhân viên", "", hd.getNhanVien().getHo() + " " + hd.getNhanVien().getTen(),
                "", "Trạng thái", "", hd.getTrangThaiThanhToan()});
        model.addRow(new Object[]{"Khách hàng", "", hd.getKhachHang().getHo() + " " + hd.getKhachHang().getTen()});
        model.addRow(new Object[]{"STT", "Mã SP", "Tên sản phẩm", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền"});
        int stt = 1;
        long tongSoLuong = 0;
        for (ChiTietHoaDon dong : hd.getListChiTietHoaDon()) {
            SanPham sp = dong.getSanPham();
            model.addRow(new Object[]{stt, sp.getMaSanPham(), sp.getTenSanPham(), sp.getDonViTinh(),
                    dong.getSoLuong(), dong.getGiaBan().toPlainString(), dong.tinhThanhTien().toPlainString()});
            tongSoLuong += dong.getSoLuong();
            stt++;
        }
        model.addRow(new Object[]{"Tổng số lượng", "", tongSoLuong, "", "Tổng tiền hàng", "",
                hd.tinhTongTienSP().toPlainString()});
        model.addRow(new Object[]{"Tiền được giảm", "", hd.tinhTienKhuyenMai(hd.tinhTongTienSP()).toPlainString(),
                "", "Tiền khách đưa", "", hd.getTienKhachDua().toPlainString()});
        model.addRow(new Object[]{"Tiền thối", "", hd.tinhTienThoi().toPlainString(), "", "Tổng thanh toán", "",
                hd.tinhTongTienThanhToan().toPlainString()});
        return model;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() != btnXuat) {
            dispose();
            return;
        }
        JFileChooser chonTep = new JFileChooser();
        chonTep.setDialogTitle("Lưu hóa đơn TXT");
        chonTep.setSelectedFile(new File(hoaDon.getMaHoaDon() + ".txt"));
        if (chonTep.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File tep = chonTep.getSelectedFile();
        if (!tep.getName().toLowerCase().endsWith(".txt")) {
            tep = new File(tep.getPath() + ".txt");
        }
        if (tep.exists() && JOptionPane.showConfirmDialog(this, "Tệp đã tồn tại. Ghi đè tệp này?",
                "Xác nhận ghi đè", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            HoaDonService.xuatHoaDonTxt(hoaDon, tep);
            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn: " + tep.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không lưu được tệp hóa đơn. Hãy kiểm tra vị trí lưu.",
                    "Lỗi xuất hóa đơn", JOptionPane.ERROR_MESSAGE);
        }
    }
}

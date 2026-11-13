package gui;

import dao.LoaiSanPhamDAO;
import entity.LoaiSanPham;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import static gui.GiaoDienChung.*;

public final class LoaiSanPhamUI extends JPanel {
    private static final long serialVersionUID = 1L;

    private final LoaiSanPhamDAO dao = new LoaiSanPhamDAO();
    private final JTable table = createTable("Mã loại", "Tên loại", "Mô tả", "Trạng thái");
    private final JTextField codeField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JComboBox<String> statusField = new JComboBox<>(
            new String[] {"Đang hoạt động", "Ngừng sử dụng"});
    private String selectedCode;

    public LoaiSanPhamUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel heading = new JPanel(new BorderLayout(4, 4));
        heading.setBackground(PAGE);
        JLabel title = new JLabel("Quản lý loại sản phẩm");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel(
                "Loại ngừng sử dụng không được chọn khi tạo sản phẩm mới.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.CENTER);
        add(heading, BorderLayout.NORTH);
        codeField.setEditable(false);
        codeField.setBackground(new Color(231, 239, 247));
        styleInput(codeField);
        styleInput(nameField);
        styleInput(descriptionField);
        styleInput(statusField);

        JPanel listCard = createCard("");
        listCard.add(tableScroll(table), BorderLayout.CENTER);
        JPanel formCard = createCard("Thông tin loại");
        JPanel fields = createFormFields();
        addField(fields, "Mã loại", codeField, 0);
        addField(fields, "Tên loại", nameField, 1);
        addField(fields, "Mô tả", descriptionField, 2);
        addField(fields, "Trạng thái", statusField, 3);
        formCard.add(fields, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(0, 8));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        javax.swing.JLabel hint = new javax.swing.JLabel("Mã loại được hệ thống tự tạo.");
        hint.setForeground(MUTED);
        hint.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        footer.add(hint, BorderLayout.NORTH);
        footer.add(createActionButtons(event -> insert(), event -> update(),
                event -> delete(), event -> clearForm()), BorderLayout.CENTER);
        formCard.add(footer, BorderLayout.SOUTH);

        add(splitCards(listCard, formCard), BorderLayout.CENTER);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectRow();
            }
        });
        loadData();
    }

    public void loadData() {
        try {
            loadRows();
        } catch (SQLException exception) {
            showError(this, exception);
        }
    }

    private void loadRows() throws SQLException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (LoaiSanPham category : dao.findAll()) {
            model.addRow(new Object[] {
                category.getMaLoai(),
                category.getTenLoai(),
                category.getMoTa(),
                category.isTrangThai() ? "Đang hoạt động" : "Ngừng sử dụng"
            });
        }
    }

    private void selectRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        selectedCode = String.valueOf(table.getModel().getValueAt(row, 0));
        codeField.setText(selectedCode);
        nameField.setText(String.valueOf(table.getModel().getValueAt(row, 1)));
        Object description = table.getModel().getValueAt(row, 2);
        descriptionField.setText(description == null ? "" : description.toString());
        statusField.setSelectedIndex("Đang hoạt động".equals(
                table.getModel().getValueAt(row, 3)) ? 0 : 1);
    }

    private void insert() {
        try {
            validateForm();
            String code = dao.insert(nameField.getText().trim(),
                    descriptionField.getText().trim(), statusField.getSelectedIndex() == 0);
            loadRows();
            clearForm();
            JOptionPane.showMessageDialog(this, "Đã thêm loại sản phẩm " + code + ".");
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void update() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn loại sản phẩm cần cập nhật.");
            return;
        }
        try {
            validateForm();
            dao.update(selectedCode, nameField.getText().trim(),
                    descriptionField.getText().trim(), statusField.getSelectedIndex() == 0);
            loadRows();
            clearForm();
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void delete() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn loại sản phẩm cần xóa.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa loại " + selectedCode + "? Loại đang có sản phẩm sẽ không thể xóa.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(selectedCode);
            loadRows();
            clearForm();
        } catch (SQLException exception) {
            showError(this, exception);
        }
    }

    private void validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại không được để trống.");
        }
        if (nameField.getText().trim().length() > 100) {
            throw new IllegalArgumentException("Tên loại tối đa 100 ký tự.");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả loại sản phẩm không được để trống.");
        }
        if (descriptionField.getText().trim().length() > 500) {
            throw new IllegalArgumentException("Mô tả tối đa 500 ký tự.");
        }
    }

    private void clearForm() {
        selectedCode = null;
        codeField.setText("Tự động tạo");
        nameField.setText("");
        descriptionField.setText("");
        statusField.setSelectedIndex(0);
        table.clearSelection();
    }
}

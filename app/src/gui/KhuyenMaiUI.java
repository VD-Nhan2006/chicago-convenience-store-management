package gui;

import dao.KhuyenMaiDAO;
import entity.ChuongTrinhKhuyenMai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import static gui.GiaoDienChung.*;

public final class KhuyenMaiUI extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final KhuyenMaiDAO dao = new KhuyenMaiDAO();
    private final JTable table = createTable(
            "Mã KM", "Tên chương trình", "Loại", "Giá trị", "Trạng thái");
    private final JTextField codeField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JComboBox<String> typeField = new JComboBox<>(new String[] {
        "Giảm theo phần trăm", "Giảm số tiền cố định"
    });
    private final JTextField valueField = new JTextField();
    private final JTextField startField = new JTextField();
    private final JTextField endField = new JTextField();
    private final JComboBox<String> statusField = new JComboBox<>(
            new String[] {"Đang hoạt động", "Ngừng hoạt động"});
    private final List<ChuongTrinhKhuyenMai> promotions = new ArrayList<>();
    private String selectedCode;

    public KhuyenMaiUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel heading = new JPanel(new BorderLayout(4, 4));
        heading.setBackground(PAGE);
        JLabel title = new JLabel("Quản lý khuyến mãi");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel(
                "Mỗi hóa đơn chỉ áp dụng một chương trình, tính trên tổng tiền hàng.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.CENTER);
        add(heading, BorderLayout.NORTH);
        codeField.setEditable(false);
        codeField.setBackground(new Color(230, 239, 247));
        styleInput(codeField);
        styleInput(nameField);
        styleInput(descriptionField);
        styleInput(typeField);
        styleInput(valueField);
        styleInput(startField);
        styleInput(endField);
        styleInput(statusField);

        JPanel listCard = createCard("");
        listCard.add(tableScroll(table), BorderLayout.CENTER);
        JPanel formCard = createCard("Thông tin khuyến mãi");
        JPanel fields = createFormFields();
        addField(fields, "Mã khuyến mãi", codeField, 0);
        addField(fields, "Tên chương trình", nameField, 1);
        addField(fields, "Mô tả", descriptionField, 2);
        addField(fields, "Loại khuyến mãi", typeField, 3);
        addField(fields, "Giá trị", valueField, 4);
        addField(fields, "Bắt đầu (dd/MM/yyyy HH:mm)", startField, 5);
        addField(fields, "Kết thúc (dd/MM/yyyy HH:mm)", endField, 6);
        addField(fields, "Trạng thái", statusField, 7);
        JScrollPane formScroll = new JScrollPane(fields);
        formScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        formScroll.getViewport().setBackground(Color.WHITE);
        formCard.add(formScroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout(0, 6));
        footer.setBackground(Color.WHITE);
        footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 12, 12));
        JLabel hint = new JLabel("Phần trăm từ 0 đến 100; kết thúc không trước thời gian bắt đầu.");
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
        promotions.clear();
        promotions.addAll(dao.findAll());
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (ChuongTrinhKhuyenMai promotion : promotions) {
            model.addRow(new Object[] {
                promotion.getMaKM(),
                promotion.getTenChuongTrinh(),
                promotion.getLoaiKhuyenMai(),
                promotion.getGiaTriKhuyenMai().stripTrailingZeros().toPlainString(),
                promotion.isTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động"
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
        for (ChuongTrinhKhuyenMai promotion : promotions) {
            if (promotion.getMaKM().equals(selectedCode)) {
                codeField.setText(promotion.getMaKM());
                nameField.setText(promotion.getTenChuongTrinh());
                descriptionField.setText(promotion.getMoTa());
                typeField.setSelectedItem(promotion.getLoaiKhuyenMai());
                valueField.setText(promotion.getGiaTriKhuyenMai()
                        .stripTrailingZeros().toPlainString());
                startField.setText(DATE_FORMAT.format(promotion.getNgayBatDau()));
                endField.setText(DATE_FORMAT.format(promotion.getNgayKetThuc()));
                statusField.setSelectedIndex(promotion.isTrangThai() ? 0 : 1);
                return;
            }
        }
    }

    private void insert() {
        try {
            FormData data = readForm();
            String code = dao.insert(data.name, data.description, data.type, data.value,
                    data.start, data.end, data.active);
            loadRows();
            clearForm();
            JOptionPane.showMessageDialog(this, "Đã thêm chương trình " + code + ".");
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void update() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn chương trình cần cập nhật.");
            return;
        }
        try {
            FormData data = readForm();
            dao.update(selectedCode, data.name, data.description, data.type, data.value,
                    data.start, data.end, data.active);
            loadRows();
            clearForm();
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void delete() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn chương trình cần xóa.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa chương trình " + selectedCode + "?",
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

    private FormData readForm() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || name.length() > 150) {
            throw new IllegalArgumentException("Tên chương trình phải có từ 1 đến 150 ký tự.");
        }
        String description = descriptionField.getText().trim();
        if (description.isEmpty() || description.length() > 500) {
            throw new IllegalArgumentException("Mô tả phải có từ 1 đến 500 ký tự.");
        }
        try {
            BigDecimal value = parseLocalizedDecimal(valueField.getText());
            if (value.signum() <= 0 || value.precision() > 18 || value.scale() > 2
                    || value.compareTo(new BigDecimal("9999999999999999.99")) > 0) {
                throw new IllegalArgumentException("Giá trị khuyến mãi phải là số dương hợp lệ.");
            }
            String type = (String) typeField.getSelectedItem();
            if ("Giảm theo phần trăm".equals(type)
                    && value.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Mức giảm theo phần trăm không được vượt quá 100.");
            }
            LocalDateTime start = LocalDateTime.parse(startField.getText().trim(), DATE_FORMAT);
            LocalDateTime end = LocalDateTime.parse(endField.getText().trim(), DATE_FORMAT);
            if (end.isBefore(start)) {
                throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu.");
            }
            return new FormData(name, description, type, value, start, end,
                    statusField.getSelectedIndex() == 0);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Giá trị khuyến mãi phải là số hợp lệ.");
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Thời gian phải theo định dạng dd/MM/yyyy HH:mm, ví dụ 01/11/2026 00:00.");
        }
    }

    private void clearForm() {
        selectedCode = null;
        codeField.setText("Tự động tạo");
        nameField.setText("");
        descriptionField.setText("");
        typeField.setSelectedIndex(0);
        valueField.setText("");
        startField.setText("");
        endField.setText("");
        statusField.setSelectedIndex(0);
        table.clearSelection();
    }

    private static final class FormData {
        private final String name;
        private final String description;
        private final String type;
        private final BigDecimal value;
        private final LocalDateTime start;
        private final LocalDateTime end;
        private final boolean active;

        private FormData(String name, String description, String type, BigDecimal value,
                LocalDateTime start, LocalDateTime end, boolean active) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.value = value;
            this.start = start;
            this.end = end;
            this.active = active;
        }
    }
}

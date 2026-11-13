package gui;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

public final class SanPhamUI extends JPanel {
    private static final long serialVersionUID = 1L;

    private final SanPhamDAO dao = new SanPhamDAO();
    private final LoaiSanPhamDAO categoryDao = new LoaiSanPhamDAO();
    private final JTable table = createTable(
            "Mã SP", "Tên sản phẩm", "Loại", "ĐVT", "Tồn", "Giá bán", "Trạng thái");
    private final JTextField codeField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JComboBox<LoaiSanPham> categoryField = new JComboBox<>();
    private final JTextField unitField = new JTextField();
    private final JTextField quantityField = new JTextField();
    private final JTextField importPriceField = new JTextField();
    private final JTextField marginField = new JTextField();
    private final JTextField sellPriceField = new JTextField();
    private final JComboBox<String> statusField = new JComboBox<>(
            new String[] {"Đang kinh doanh", "Ngừng kinh doanh"});
    private final List<LoaiSanPham> categories = new ArrayList<>();
    private final List<SanPham> products = new ArrayList<>();
    private String selectedCode;

    public SanPhamUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(PAGE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel heading = new JPanel(new BorderLayout(4, 4));
        heading.setBackground(PAGE);
        JLabel title = new JLabel("Quản lý sản phẩm");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel(
                "Giá bán được tính tự động từ giá nhập và mức lợi nhuận.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.CENTER);
        add(heading, BorderLayout.NORTH);
        codeField.setEditable(false);
        sellPriceField.setEditable(false);
        codeField.setBackground(new Color(231, 239, 247));
        sellPriceField.setBackground(new Color(231, 239, 247));
        styleInput(codeField);
        styleInput(nameField);
        styleInput(categoryField);
        styleInput(unitField);
        styleInput(quantityField);
        styleInput(importPriceField);
        styleInput(marginField);
        styleInput(sellPriceField);
        styleInput(statusField);
        importPriceField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override
                    public void insertUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }

                    @Override
                    public void removeUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }

                    @Override
                    public void changedUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }
                });
        marginField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    @Override
                    public void insertUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }

                    @Override
                    public void removeUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }

                    @Override
                    public void changedUpdate(javax.swing.event.DocumentEvent event) {
                        updateSellPrice();
                    }
                });

        JPanel listCard = createCard("");
        listCard.add(tableScroll(table), BorderLayout.CENTER);
        JPanel formCard = createCard("Thông tin sản phẩm");
        JPanel fields = createFormFields();
        addField(fields, "Mã sản phẩm", codeField, 0);
        addField(fields, "Tên sản phẩm", nameField, 1);
        addField(fields, "Loại sản phẩm", categoryField, 2);
        addField(fields, "Đơn vị tính", unitField, 3);
        addField(fields, "Số lượng tồn", quantityField, 4);
        addField(fields, "Giá nhập (đồng)", importPriceField, 5);
        addField(fields, "Lợi nhuận (đồng)", marginField, 6);
        addField(fields, "Giá bán (đồng)", sellPriceField, 7);
        addField(fields, "Trạng thái", statusField, 8);
        JScrollPane formScroll = new JScrollPane(fields);
        formScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        formScroll.getViewport().setBackground(Color.WHITE);
        formCard.add(formScroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout(0, 6));
        footer.setBackground(Color.WHITE);
        footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 12, 12));
        JLabel hint = new JLabel("Giá bán = Giá nhập + Lợi nhuận.");
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
            loadCategories();
            loadRows();
        } catch (SQLException exception) {
            showError(this, exception);
        }
    }

    private void loadCategories() throws SQLException {
        categories.clear();
        categories.addAll(categoryDao.findAll());
        categoryField.removeAllItems();
        for (LoaiSanPham category : categories) {
            categoryField.addItem(category);
        }
    }

    private void loadRows() throws SQLException {
        products.clear();
        products.addAll(dao.findAll());
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (SanPham product : products) {
            model.addRow(new Object[] {
                product.getMaSP(),
                product.getTenSP(),
                product.getTenLoai(),
                product.getDonViTinh(),
                product.getSoLuongTon(),
                money(product.getGiaBan()),
                product.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh"
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
        for (SanPham product : products) {
            if (product.getMaSP().equals(selectedCode)) {
                codeField.setText(product.getMaSP());
                nameField.setText(product.getTenSP());
                selectCategory(product.getMaLoai());
                unitField.setText(product.getDonViTinh());
                quantityField.setText(String.valueOf(product.getSoLuongTon()));
                importPriceField.setText(product.getGiaNhap().toPlainString());
                marginField.setText(product.getMucLoiNhuan().toPlainString());
                sellPriceField.setText(money(product.getGiaBan()));
                statusField.setSelectedIndex(product.isTrangThai() ? 0 : 1);
                break;
            }
        }
    }

    private void selectCategory(String code) {
        for (int index = 0; index < categoryField.getItemCount(); index++) {
            if (categoryField.getItemAt(index).getMaLoai().equals(code)) {
                categoryField.setSelectedIndex(index);
                return;
            }
        }
    }

    private void insert() {
        try {
            FormData data = readForm();
            if (!data.category.isTrangThai()) {
                throw new IllegalArgumentException(
                        "Không thể tạo sản phẩm thuộc loại đã ngừng sử dụng.");
            }
            String code = dao.insert(data.name, data.category.getMaLoai(), data.unit,
                    data.quantity, data.importPrice, data.margin, data.active);
            loadRows();
            clearForm();
            JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm " + code + ".");
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void update() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần cập nhật.");
            return;
        }
        try {
            FormData data = readForm();
            dao.update(selectedCode, data.name, data.category.getMaLoai(), data.unit,
                    data.quantity, data.importPrice, data.margin, data.active);
            loadRows();
            clearForm();
        } catch (SQLException | IllegalArgumentException exception) {
            showError(this, exception);
        }
    }

    private void delete() {
        if (selectedCode == null) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần xóa.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa sản phẩm " + selectedCode + "?",
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
        String unit = unitField.getText().trim();
        if (name.isEmpty() || name.length() > 150) {
            throw new IllegalArgumentException("Tên sản phẩm phải có từ 1 đến 150 ký tự.");
        }
        if (unit.isEmpty() || unit.length() > 30) {
            throw new IllegalArgumentException("Đơn vị tính phải có từ 1 đến 30 ký tự.");
        }
        if (categoryField.getSelectedItem() == null) {
            throw new IllegalArgumentException("Cần tạo ít nhất một loại sản phẩm trước.");
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            BigDecimal importPrice = parseLocalizedDecimal(importPriceField.getText());
            BigDecimal margin = parseLocalizedDecimal(marginField.getText());
            if (quantity < 0 || importPrice.signum() < 0 || margin.signum() < 0) {
                throw new IllegalArgumentException("Tồn kho, giá nhập và lợi nhuận không được âm.");
            }
            if (importPrice.stripTrailingZeros().scale() > 0
                    || importPrice.compareTo(new BigDecimal("999999999999999999")) > 0
                    || margin.stripTrailingZeros().scale() > 0
                    || margin.compareTo(new BigDecimal("999999999999999999")) > 0) {
                throw new IllegalArgumentException(
                        "Giá nhập và lợi nhuận phải là số nguyên tối đa 18 chữ số.");
            }
            return new FormData(name, (LoaiSanPham) categoryField.getSelectedItem(),
                    unit, quantity, importPrice.setScale(0, RoundingMode.HALF_UP),
                    margin.setScale(0, RoundingMode.HALF_UP), statusField.getSelectedIndex() == 0);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Tồn kho phải là số nguyên; giá nhập và lợi nhuận phải là số hợp lệ.");
        }
    }

    private void updateSellPrice() {
        try {
            BigDecimal price = parseLocalizedDecimal(importPriceField.getText());
            BigDecimal margin = parseLocalizedDecimal(marginField.getText());
            BigDecimal result = price.add(margin)
                    .setScale(0, RoundingMode.HALF_UP);
            sellPriceField.setText(money(result));
        } catch (NumberFormatException | ArithmeticException exception) {
            sellPriceField.setText("");
        }
    }

    private static String money(BigDecimal value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"))
                .format(value.setScale(0, RoundingMode.HALF_UP));
    }

    private void clearForm() {
        selectedCode = null;
        codeField.setText("Tự động tạo");
        nameField.setText("");
        if (categoryField.getItemCount() > 0) {
            categoryField.setSelectedIndex(0);
        }
        unitField.setText("");
        quantityField.setText("");
        importPriceField.setText("");
        marginField.setText("");
        sellPriceField.setText("");
        statusField.setSelectedIndex(0);
        table.clearSelection();
    }

    private static final class FormData {
        private final String name;
        private final LoaiSanPham category;
        private final String unit;
        private final int quantity;
        private final BigDecimal importPrice;
        private final BigDecimal margin;
        private final boolean active;

        private FormData(String name, LoaiSanPham category, String unit, int quantity,
                BigDecimal importPrice, BigDecimal margin, boolean active) {
            this.name = name;
            this.category = category;
            this.unit = unit;
            this.quantity = quantity;
            this.importPrice = importPrice;
            this.margin = margin;
            this.active = active;
        }
    }
}

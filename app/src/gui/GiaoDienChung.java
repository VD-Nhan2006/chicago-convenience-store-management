package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

final class GiaoDienChung {
    static final Color NAVY = new Color(22, 49, 74);
    static final Color CORAL = new Color(239, 76, 61);
    static final Color PAGE = new Color(249, 247, 241);
    static final Color TEXT = new Color(38, 65, 91);
    static final Color MUTED = new Color(116, 135, 151);
    private static final Color BUTTON_CORAL_BACKGROUND = new Color(255, 224, 219);
    private static final Color BUTTON_NAVY_BACKGROUND = new Color(222, 233, 243);

    private GiaoDienChung() {
    }

    static JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(NAVY));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 15));
        heading.setForeground(NAVY);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 12, 7, 8));
        card.add(heading, BorderLayout.NORTH);
        return card;
    }

    static JTable createTable(String... columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(29);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setSelectionBackground(new Color(230, 237, 245));
        table.setSelectionForeground(NAVY);
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 33));
        return table;
    }

    static JScrollPane tableScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    static void addField(JPanel panel, String label, Component field, int row) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row * 2;
        labelConstraints.weightx = 1;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(3, 0, 2, 0);
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldLabel.setForeground(TEXT);
        panel.add(fieldLabel, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 0;
        fieldConstraints.gridy = row * 2 + 1;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 4, 0);
        panel.add(field, fieldConstraints);
    }

    static JPanel createFormFields() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 12));
        return form;
    }

    static void styleInput(JComponent component) {
        component.setFont(new Font("Arial", Font.PLAIN, 13));
        component.setForeground(TEXT);
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(122, 144, 164)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        component.setPreferredSize(new Dimension(220, 32));
    }

    static JButton createButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(background.equals(Color.WHITE)
                ? NAVY : background));
        button.setPreferredSize(new Dimension(100, 34));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        button.setOpaque(true);
        return button;
    }

    static JPanel createActionButtons(ActionListener add, ActionListener update,
            ActionListener delete, ActionListener reset) {
        JPanel buttons = new JPanel(new java.awt.GridLayout(2, 2, 6, 6));
        buttons.setBackground(Color.WHITE);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        JButton addButton = createButton("Thêm", BUTTON_CORAL_BACKGROUND, Color.BLACK);
        JButton updateButton = createButton("Cập nhật", BUTTON_NAVY_BACKGROUND, Color.BLACK);
        JButton deleteButton = createButton("Xóa", Color.WHITE, Color.BLACK);
        JButton resetButton = createButton("Làm mới", Color.WHITE, Color.BLACK);
        deleteButton.setBorder(BorderFactory.createLineBorder(CORAL));
        addButton.addActionListener(add);
        updateButton.addActionListener(update);
        deleteButton.addActionListener(delete);
        resetButton.addActionListener(reset);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(resetButton);
        return buttons;
    }

    static JPanel splitCards(JPanel left, JPanel right) {
        JPanel wrapper = new JPanel(new BorderLayout(12, 0));
        wrapper.setBackground(PAGE);
        right.setPreferredSize(new Dimension(320, right.getPreferredSize().height));
        wrapper.add(left, BorderLayout.CENTER);
        wrapper.add(right, BorderLayout.EAST);
        return wrapper;
    }

    static void showError(Component parent, Exception exception) {
        javax.swing.JOptionPane.showMessageDialog(parent,
                exception.getMessage(), "Không thể hoàn tất thao tác",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    static BigDecimal parseLocalizedDecimal(String input) {
        String value = input.trim().replace(" ", "");
        int comma = value.lastIndexOf(',');
        int dot = value.lastIndexOf('.');
        if (comma >= 0 && dot >= 0) {
            char decimalSeparator = comma > dot ? ',' : '.';
            char groupingSeparator = decimalSeparator == ',' ? '.' : ',';
            value = value.replace(String.valueOf(groupingSeparator), "");
            value = value.replace(decimalSeparator, '.');
        } else if (comma >= 0) {
            int decimals = value.length() - comma - 1;
            if (decimals == 3 || value.indexOf(',') != comma) {
                value = value.replace(",", "");
            } else {
                value = value.replace(".", "").replace(',', '.');
            }
        } else if (dot >= 0) {
            int decimals = value.length() - dot - 1;
            if (decimals == 3 || value.indexOf('.') != dot) {
                value = value.replace(".", "");
            }
        }
        return new BigDecimal(value);
    }
}

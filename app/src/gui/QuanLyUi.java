package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.table.JTableHeader;

final class QuanLyUi {
	static final Color MAU_NEN = new Color(250, 247, 240);
	static final Color MAU_XANH = new Color(23, 52, 81);
	static final Color MAU_DO = new Color(239, 77, 59);
	static final Color MAU_XAM = new Color(230, 237, 245);

	private QuanLyUi() {
	}

	static JPanel taoTruong(String ten, JComponent input) {
		JPanel truong = new JPanel();
		truong.setBackground(Color.WHITE);
		truong.setLayout(new BoxLayout(truong, BoxLayout.Y_AXIS));
		truong.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel nhan = new JLabel(ten);
		nhan.setForeground(MAU_XANH);
		nhan.setFont(new Font("Arial", Font.PLAIN, 13));
		nhan.setAlignmentX(Component.LEFT_ALIGNMENT);

		input.setAlignmentX(Component.LEFT_ALIGNMENT);
		input.setPreferredSize(new Dimension(0, 30));
		input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		truong.add(nhan);
		truong.add(Box.createVerticalStrut(3));
		truong.add(input);
		truong.add(Box.createVerticalStrut(8));
		return truong;
	}

	static JButton taoNut(String ten, boolean nutChinh) {
		JButton nut = new JButton(ten);
		nut.setFont(new Font("Arial", Font.BOLD, 13));
		nut.setBackground(nutChinh ? MAU_DO : Color.WHITE);
		nut.setForeground(nutChinh ? Color.WHITE : MAU_XANH);
		nut.setFocusPainted(false);
		nut.setOpaque(true);
		nut.setPreferredSize(new Dimension(140, 35));
		nut.setBorder(BorderFactory.createLineBorder(nutChinh ? MAU_DO : MAU_XANH));
		return nut;
	}

	static JPanel taoNhomNut(JButton... cacNut) {
		JPanel nhomNut = new JPanel(new GridLayout(2, 2, 8, 8));
		nhomNut.setBackground(Color.WHITE);
		nhomNut.setAlignmentX(Component.LEFT_ALIGNMENT);
		nhomNut.setPreferredSize(new Dimension(288, 78));
		nhomNut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
		for (JButton nut : cacNut) {
			nhomNut.add(nut);
		}
		return nhomNut;
	}

	static void dinhDangBang(JTable bang) {
		bang.setRowHeight(30);
		bang.setFont(new Font("Arial", Font.PLAIN, 13));
		bang.setForeground(MAU_XANH);
		bang.setSelectionBackground(MAU_XAM);
		bang.setSelectionForeground(MAU_XANH);
		bang.setFillsViewportHeight(true);

		JTableHeader header = bang.getTableHeader();
		header.setFont(new Font("Arial", Font.BOLD, 13));
		header.setBackground(MAU_XANH);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(0, 34));
	}

	static void baoLoi(Component parent, Exception exception) {
		String noiDung = exception.getMessage();
		if (noiDung == null || noiDung.trim().isEmpty()) {
			noiDung = exception.getClass().getSimpleName();
		}
		JOptionPane.showMessageDialog(parent, noiDung, "Lỗi", JOptionPane.ERROR_MESSAGE);
	}
}

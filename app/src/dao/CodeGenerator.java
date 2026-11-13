package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

final class CodeGenerator {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyy");

    private CodeGenerator() {
    }

    static String next(Connection connection, String table, String column, String prefix)
            throws SQLException {
        if (!table.matches("[A-Za-z]+") || !column.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Tên bảng hoặc cột không hợp lệ.");
        }

        String codePrefix = prefix + LocalDate.now().format(DATE_FORMAT);
        String sql = "SELECT COALESCE(MAX(TRY_CONVERT(INT, RIGHT([" + column + "], 3))), 0) + 1 "
                + "FROM dbo.[" + table + "] WITH (UPDLOCK, HOLDLOCK) "
                + "WHERE [" + column + "] LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codePrefix + "%");
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                int next = result.getInt(1);
                if (next > 999) {
                    throw new SQLException("Đã hết số mã được cấp trong ngày.");
                }
                return codePrefix + String.format("%03d", next);
            }
        }
    }
}

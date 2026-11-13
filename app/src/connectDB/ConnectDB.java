package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectDB {
    private ConnectDB() {
    }

    public static Connection getConnection() throws SQLException {
        String server = setting("CHICAGO_DB_SERVER", "localhost");
        String port = setting("CHICAGO_DB_PORT", "1433");
        String instance = setting("CHICAGO_DB_INSTANCE", "");
        String database = setting("CHICAGO_DB_NAME", "DBChicago");
        String authentication = setting("CHICAGO_DB_AUTH", "sql");

        StringBuilder url = new StringBuilder("jdbc:sqlserver://").append(server);
        if (!instance.isEmpty()) {
            url.append(";instanceName=").append(instance);
        } else {
            url.append(':').append(port);
        }
        url.append(";databaseName=").append(database)
                .append(";encrypt=true;trustServerCertificate=true;loginTimeout=10;");
        if ("windows".equalsIgnoreCase(authentication)) {
            url.append("integratedSecurity=true;");
            return DriverManager.getConnection(url.toString());
        }
        if (!"sql".equalsIgnoreCase(authentication)) {
            throw new SQLException("CHICAGO_DB_AUTH chỉ nhận giá trị 'windows' hoặc 'sql'.");
        }

        String user = setting("CHICAGO_DB_USER", "sa");
        String password = setting("CHICAGO_DB_PASSWORD", "");
        return DriverManager.getConnection(url.toString(), user, password);
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(name);
        }
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}

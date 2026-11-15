package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	private static Connection con = null;

    private static final ConnectDB INSTANCE = new ConnectDB();

    private static final String SERVER = "localhost";
    private static final int PORT = 1433;
    private static final String DATABASE = "DBChicago";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private ConnectDB() {
    }

    public static ConnectDB getInstance() {
        return INSTANCE;
    }

    public void connect() throws SQLException {

        if (con != null && !con.isClosed()) {
            return;
        }

        String server = System.getProperty("chicago.db.server", SERVER);
        String port = System.getProperty("chicago.db.port", String.valueOf(PORT));
        String url = "jdbc:sqlserver://" + server + ":" + port
                + ";databaseName=" + System.getProperty("chicago.db.name", DATABASE)
                + ";encrypt=true;trustServerCertificate=true;";

        con = DriverManager.getConnection(url,
                System.getProperty("chicago.db.user", USER),
                System.getProperty("chicago.db.password", PASSWORD));
    }

    public static Connection getConnection() throws SQLException {

        if (con == null || con.isClosed()) {
            getInstance().connect();
        }

        return con;
    }

    public void disconnect() {

        if (con == null) {
            return;
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con = null;
        }
    }
}

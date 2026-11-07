package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	private static Connection con = null;
	private static final ConnectDB INSTANCE = new ConnectDB();
	
	private static final String SERVER = "localhost";
	private static final int PORT = 1433;
	private static final String DATABASE = "ChicagoConvenienceStore";
	private static final String USER = "sa";
	private static final String PASSWORD = "sapassword";
	
	private ConnectDB() {	
	}
	
	public static ConnectDB getInstance() {
		return INSTANCE;
	}
	public void connect() throws SQLException {
		if(con != null && !con.isClosed()) {
			return;
		}
		String url = "jdbc:sqlserver://" + SERVER + ":" + PORT
		        + ";databaseName=" + DATABASE
		        + ";encrypt=true;trustServerCertificate=true;";
		con = DriverManager.getConnection(url, USER, PASSWORD);
	}
	
	public static Connection getConnect() throws SQLException {
		if(con == null || con.isClosed()) {
			getInstance().connect();
		}
		return con;
	}
	
	public void disconnect() {
		if(con == null) {
			return;
		}
		try {
			con.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			con = null;
		}
	}
}

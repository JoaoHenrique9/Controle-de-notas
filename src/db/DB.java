package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	private static Connection conn = null;

	public static Connection getConnection() {
		if (conn == null) {
			try {
				/*Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);*/
				
				String url = "jdbc:mysql://localhost:3306/roca?useSSL=false";
			    String username = "developer";        //nome de um usuário de seu BD      
			    String password = "Rocabrasilrec01";      //sua senha de acesso
			    
			    conn = DriverManager.getConnection(url, username, password);
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}

	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	/*
	 * private static Properties loadProperties() { try (FileInputStream fs = new
	 * FileInputStream("C:\\Controle de notas\\db.properties")) { Properties props =
	 * new Properties(); props.load(fs); return props; } catch (IOException e) {
	 * throw new DbException(e.getMessage()); } }
	 */

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}

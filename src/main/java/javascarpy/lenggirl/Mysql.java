package javascarpy.lenggirl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
	Connection conn = null;
	Statement stmt = null;
	public Mysql() throws ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/qingmu?"
				+ "user=root&password=6833066&useUnicode=true&characterEncoding=UTF8";
		Class.forName("com.mysql.jdbc.Driver");
		this.conn = DriverManager.getConnection(url);
		this.stmt = conn.createStatement();

	}
	public Mysql(String url) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.conn = DriverManager.getConnection(url);
		this.stmt = conn.createStatement();

	}
	public void closeall() {
		try {
			this.stmt.close();
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet exectequery(String sql) throws SQLException {
		ResultSet rs = this.stmt.executeQuery(sql);
		return rs;

	}

	public static void main(String[] argu) {
		try {
			Mysql ss = new Mysql();
			String sql = "SELECT distinct title FROM qingmu.tmp_order_detail_for_skx where merchant_sn like '13514C%' order by import_date desc limit 1;";
			ResultSet s = ss.exectequery(sql);
			while (s.next()) {
				System.out.println(s.getString(1));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

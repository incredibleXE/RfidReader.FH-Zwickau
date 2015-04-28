/**
 * 
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySql helper for MySQL Server
 * 
 * @version 0.1
 * @extends helper.SqlHelper.class
 * @author incredibleXE
 *
 */
public class MySqlHelper extends SqlHelper {

	/**
	 * @see helper.SqlHelper#constructor()
	 * @param host server where database is running on
	 * @param database database name to which should connecting to
	 * @param user for authentication
	 * @param passwd for authentication
	 */
	public MySqlHelper(String host, String database, String user, String passwd) {
		super(host, database, user, passwd);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see helper.SqlHelper#connect()
	 */
	@Override
	protected Connection connect() {
		try {
			if(getConnect()==null || getConnect().isClosed()) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					setConnect(DriverManager.getConnection("jdbc:mysql://" + getHost()
							+ "/" + getDatabase() + "?" + "user=" + getUser() + "&password="
							+ getPasswd()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					printSQLException(e);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SqlHelper.printSQLException(e);
		}
		return getConnect();
	}

}

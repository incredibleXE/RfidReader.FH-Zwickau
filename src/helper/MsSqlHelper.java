package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MsSql helper for Microsoft SQL Databases
 * 
 * @version 0.1
 * @extends helper.SqlHelper.class
 * @author incredibleXE
 *
 */
public class MsSqlHelper extends SqlHelper {
	private boolean winlogon = false;

	/**
	 * Constructor 
	 * winlogon is on standard value (false)
	 * 
	 * @see helper.SqlHelper.class#constructor()
	 * @param host server where database is running on
	 * @param database database name to which should connecting to
	 * @param user for authentication
	 * @param passwd for authentication
	 */
	public MsSqlHelper(String host, String database, String user, String passwd) {
		super(host,database,user,passwd);
	}
	
	/**
	 * Constructor 
	 * 
	 * @see helper.SqlHelper.class#constructor()
	 * @param host server where database is running on
	 * @param database database name to which should connecting to
	 * @param user for authentication
	 * @param passwd for authentication
	 * @param winlogon use this value to enable windows authentication to database server
	 */
	public MsSqlHelper(String host, String database, String user,
			String passwd, boolean winlogon) {
		super(host,database,user,passwd);
		this.winlogon=winlogon;
	}
	
	/**
	 * establishes a connection to the database server, with jdbc:jtds driver
	 * Attention: when winlogon is true, then the connector trys to connect with "integratedSecurity=true" phrase in url.
	 * That means you have to put the right "ntlmauth.dll" in java.classpath. Otherwise you'll get an SQLException.
	 * 
	 * @return Connection stable connection or null
	 */
	@Override
	protected Connection connect() throws SQLException {
		if(getConnect()==null || getConnect().isClosed()) {
			try {
				String url = "jdbc:jtds:sqlserver://" + getHost() + ";databaseName="+ getDatabase();
				if (winlogon == false) {
					Class.forName("net.sourceforge.jtds.jdbc.Driver");
					setConnect(DriverManager.getConnection(url, getUser(), getPasswd()));
				} else {
					url = url + ";integratedSecurity=true";
					System.out.println("with winlogon - "+url);
					Class.forName("net.sourceforge.jtds.jdbc.Driver");
					Connection c = DriverManager.getConnection(url); 
					setConnect(c);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return getConnect();
	}
}

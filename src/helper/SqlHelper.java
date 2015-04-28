package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql Helper
 * 
 * @version 0.1 
 * @author incredibleXE
 *
 */
public abstract class SqlHelper {
	private String database = null, user = null, passwd = null, host = null;
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private PreparedStatement preparedStatement = null;
	
	/**
	 * 
	 * @param host server where database is running on
	 * @param database database name to which should connecting to
	 * @param user for authentication
	 * @param passwd for authentication
	 */
	public SqlHelper(String host, String database, String user, String passwd) {
		this.database = database;
		this.host = host;
		this.user = user;
		this.passwd = passwd;
	}

	/**
	 * treats the exception before it print it to console
	 *  
	 * @param ex SQLException
	 * @return error as string
	 */
	public static String printSQLException(SQLException ex) {
		String error = "";
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				if (ignoreSQLException(((SQLException) e).getSQLState()) == false) {

					e.printStackTrace(System.err);
					error = error+"SQLState: "
							+ ((SQLException) e).getSQLState()+"\n";

					error = error+"Error Code: "
							+ ((SQLException) e).getErrorCode()+"\n";

					error = error+"Message: " + e.getMessage()+"\n";

					Throwable t = ex.getCause();
					while (t != null) {
						error = error+"Cause: " + t+"\n";
						t = t.getCause();
					}
				}
			}
		}
		return error;
	}
	
	/**
	 * 
	 * @param sqlState
	 * @return boolean value
	 */
	private static boolean ignoreSQLException(String sqlState) {
		if (sqlState == null) {
			System.out.println("The SQL state is not defined!");
			return false;
		}

		// X0Y32: Jar file already exists in schema
		if (sqlState.equalsIgnoreCase("X0Y32"))
			return true;

		// 42Y55: Table already exists in schema
		if (sqlState.equalsIgnoreCase("42Y55"))
			return true;

		return false;
	}

	/**
	 * executes a normal query with result (SELECT e.g.)
	 * 
	 * @param query a normal sql statement as string 
	 * @return ResultSet
	 * @throws SQLException which can be handled with helper.SqlHelper.class#printSQLException(SQLException e)
	 */
	public List<String[]> executeSQLQuery(String query) throws SQLException {
		List<String[]> rows = null;
		setConnect(connect());
		// Statements allow to issue SQL queries to the database
		setStatement(getConnect().createStatement());
		// Result set get the result of the SQL query
		setResultSet(getStatement()
				.executeQuery(query));
		
		rows = resultSetToList();
		
		close();
		return rows;
	}
	
	/**
	 * for SQL Querys without result (INSERT / DELETE / USE) 
	 * 
	 * @param query sql statement
	 * @throws SQLException which can be handled with helper.SqlHelper.class#printSQLException(SQLException e)
	 */
	public void executeSQLQueryWithoutResult(String query) throws SQLException {
		setConnect(connect());
		// Statements allow to issue SQL queries to the database
		setStatement(getConnect().createStatement());
		
		getStatement().execute(query);
		
		close();
	}
	
	/**
	 * preparedStatement way of talking with the database (UNTESTED)
	 *  
	 * @param statement SQL Query with ?
	 * @param arguments String array with arguments in order to their places in the statement
	 * @return resultSet the answer from database
	 * @throws SQLException which can be handled with helper.SqlHelper.class#printSQLException(SQLException e)
	 */
	public List<String[]> executePreparedStatement(String statement, String[] arguments) throws SQLException {
		List<String[]> rows = null;
		setConnect(connect());
		setPreparedStatement(getConnect().prepareStatement(statement));
		for(int i=1;arguments.length>=i;i++) {
			getPreparedStatement().setString(i, arguments[i-1]);
		}
		setResultSet(getPreparedStatement().executeQuery());
		
		rows = resultSetToList();
		
		close();
		return rows;
	}
	
	/**
	 * connection class
	 * 
	 * @return Connection stable sql connection or null
	 */
	protected abstract Connection connect() throws SQLException;
	
	/**
	 * takes all data with treestructure from ResultSet and puts it into an List
	 * 
	 * @return List<String[]> array construct within all the data from ResultSet
	 * @throws SQLException
	 */
	private List<String[]> resultSetToList() throws SQLException {
		int columnCount = getResultSet().getMetaData().getColumnCount();
		List<String[]> rows = new ArrayList<String[]>();
		
		String[] rowHeader = new String[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			rowHeader[i-1]=resultSet.getMetaData().getColumnName(i);
		}
		rows.add(rowHeader);
		
        while(getResultSet().next()){
            String[] row = new String[columnCount];
            for(int i = 1;i<=columnCount;i++){
                row[i-1]=getResultSet().getString(i).trim();
            }
            rows.add(row);
        }
        
        return rows;
	}
	
	/**
	 * testing the connection
	 * 
	 * @return boolean - true - connection is working | false - not
	 * @throws SQLException which can be handled with helper.SqlHelper.class#printSQLException(SQLException e)
	 */
	public boolean testConnection() throws SQLException {
		System.out.println("=== Test db connection ===");
		if(getConnect()!=null) {
			close();
		}
		setConnect(connect());
		if(getConnect()!=null) {
			close();
			System.out.println("successfull");
			System.out.println("=== Test db connection ===");
			return true;
		}
		System.out.println("db connection failed");
		System.out.println("=== Test db connection finished ===");
		return false;
	}

	/**
	 * closes everything except the connection
	 */
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * closes everything in the right order
	 */
	public void closeConnection() {
		close();
		if (connect != null) {
			try {
				connect.close();
			} catch (SQLException e) {
				
			}
		}
	}
	
	/**
	 * writes header information from given list on console
	 * 
	 * @param list from executed Query method in this class
	 * @link helper.SqlHelper.class#executePreparedStatement(String statement, String[] arguments)
	 * @link helper.SqlHelper.class#executeSQLQuery(String query)  
	 * @throws SQLException
	 */
	private void writeMetaData(List<String[]> list) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		String[] row = list.get(0);
		for (int i = 0; i < row.length; i++) {
			System.out.print(row[i].toString()+" | ");
		}
		System.out.println();
	}

	/**
	 * writes given list in console
	 *  
	 * @param  list from executed Query method on this class
	 * @link helper.SqlHelper.class#executePreparedStatement(String statement, String[] arguments)
	 * @link helper.SqlHelper.class#executeSQLQuery(String query)  
	 * @throws SQLException
	 */
	private void writeResultSet(List<String[]> list) throws SQLException {
		for(int a=1;list.size()>a;a++) {
			String[] row = list.get(a);
			for (int i = 0; i < row.length; i++) {
				System.out.print(row[i].toString()+" | ");
			}
			System.out.println();
		}
	}
	
	/**
	 * writes everything from given SQL list on console
	 * @param  list from executed Query method on this class
	 * @link helper.SqlHelper#executePreparedStatement(String statement, String[] arguments) executePreparedStatement
	 * @link helper.SqlHelper#executeSQLQuery(String query) executeSQLQuery
	 * @link helper.SqlHelper#writeMetaData(List<String[]> list) writeMetaData
	 * @link helper.SqlHelper#writeResultSet(List<String[]> list) writeResultSet
	 */
	public void writeResultSetWithMeta(List<String[]> list) {
		try {
			this.writeMetaData(list);
			this.writeResultSet(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			SqlHelper.printSQLException(e);
		}
	}

	
	/*
	 * GETTER / SETTER 
	 * 
	 */
	/**
	 * @return database String
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return user String
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return passwd String
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @return host String
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @param database String
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * 
	 * @param user String
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @param passwd String
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * 
	 * @param host String
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 
	 * @return connect String
	 */
	public Connection getConnect() {
		return connect;
	}

	/**
	 * 
	 * @return statement String
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * 
	 * @return resultSet String
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * 
	 * @return preparedStatement PreparedStatement
	 */
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	/**
	 * 
	 * @param connect Connection
	 */
	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	/**
	 * 
	 * @param statement Statement
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	/**
	 * 
	 * @param resultSet ResultSet
	 */
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * 
	 * @param preparedStatement PreparedStatement
	 */
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
}

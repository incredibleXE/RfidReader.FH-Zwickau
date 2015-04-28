package model;

import java.util.ArrayList;


/**
 * speichert alle Informationen 
 * @author Fabian Ranacher
 * @version 0.1
 *
 */
public class Databean {
	/**
	 * ENUM for decision which SQL Connector should be used
	 * 
	 * @version 0.1
	 * @author Fabian Ranacher
	 */
	public enum DATABASE_TYPE {
	    MsSQL, MySQL;
	}
	private DATABASE_TYPE type = DATABASE_TYPE.MySQL;
	
	// SQL connection informations
	private String host, user, passwd, databasename;
	
	// winlogon variable for MsSql
	private Boolean winlogon = false;
	
	// UHF Reader thread list
	private ArrayList<Thread> threadList = new ArrayList<Thread>();
	
	// GETTER & SETTER
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return the databasename
	 */
	public String getDatabasename() {
		return databasename;
	}

	/**
	 * @param databasename the databasename to set
	 */
	public void setDatabasename(String databasename) {
		this.databasename = databasename;
	}

	/**
	 * @return the winlogon
	 */
	public Boolean getWinlogon() {
		return winlogon;
	}

	/**
	 * @param winlogon the winlogon to set
	 */
	public void setWinlogon(Boolean winlogon) {
		this.winlogon = winlogon;
	}

	/**
	 * @return the type
	 */
	public DATABASE_TYPE getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DATABASE_TYPE type) {
		this.type = type;
	}
	
	/**
	 * outputs string to console
	 * @param str String
	 */
	public void println(String str) {
		System.out.println(str);
	}
	
	/**
	 * outputs an empty line
	 */
	public void println() {
		System.out.println();
	}
	
	/**
	 * outputs an string on line with no line break
	 * @param str String
	 */
	public void print(String str) {
		System.out.print(str);
	}

	/**
	 * @return the threadList
	 */
	public ArrayList<Thread> getThreadList() {
		return threadList;
	}

	/**
	 * adds thread to threadList
	 * @param thread Thread
	 */
	public void addThread(Thread thread) {
		this.threadList.add((Thread) thread); 
	}
	
	/**
	 * 
	 */
	public void printlnError(String str) {
		System.err.println(str);
	}

}

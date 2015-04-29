package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
	
	// parameter for jar
	private ArrayList<String[]> params = new ArrayList<String[]>();
	
	// map of parameter for easy use
	private Map<String,String[]> paramMap = new HashMap<String,String[]>();
	
	// parameter result
	private Map<String[],String> parameterResult = new HashMap<String[],String>();
	
	public Databean() {
		putInLists(new String[]{"-host"});
		putInLists(new String[]{"-help","-h","/?"});
		putInLists(new String[]{"-database","-d"});
		putInLists(new String[]{"-user","-u"});
		putInLists(new String[]{"-passwd","-p"});
		putInLists(new String[]{"-connection","-c"});
		putInLists(new String[]{"-winlogon"});
	}
	
	/**
	 * puts String[] in to lists
	 * @param str
	 */
	private void putInLists(String[] str) {
		params.add(str);
		paramMap.put(str[0], str);
	}
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
	
	public boolean setHostFromParameter() {
		if(getParameterResult(getParamMapResult("-host"))!=null) {
			this.host = getParameterResult(getParamMapResult("-host"));
			return true;
		}
		return false;
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

	public boolean setUserFromParameter() {
		if(getParameterResult(getParamMapResult("-user"))!=null) {
			this.user = getParameterResult(getParamMapResult("-user"));
			return true;
		}
		return false;
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

	public boolean setPasswdFromParameter() {
		if(getParameterResult(getParamMapResult("-passwd"))!=null) {
			this.passwd = getParameterResult(getParamMapResult("-passwd"));
			return true;
		}
		return false;
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

	public boolean setDatabasenameFromParameter() {
		if(getParameterResult(getParamMapResult("-database"))!=null) {
			this.databasename = getParameterResult(getParamMapResult("-database"));
			return true;
		}
		return false;
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
	
	public boolean setWinlogonFromParameter() {
		if(getParameterResult(getParamMapResult("-winlogon"))!=null) {
			if(getParameterResult(getParamMapResult("-winlogon"))!=null)
				this.winlogon = true;
			return true;
		}
		return false;
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
	

	public boolean setTypeFromParameter() {
		if(getParameterResult(getParamMapResult("-connection"))!=null) {
			switch (getParameterResult(getParamMapResult("-connection"))) {
				case "mssql":
				case "MsSQL":
					setType(DATABASE_TYPE.MsSQL);
					break;
				default:
				case "mysql":
				case "MySQL":
					setType(DATABASE_TYPE.MySQL);
			}
			return true;
		}
		return false;
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
	 * prints errors
	 */
	public void printlnError(String str) {
		System.err.println(str);
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
	 * returns parameter list
	 * @return ArrayList<String[]>
	 */
	public ArrayList<String[]> getParams() {
		return params;
	}

	/**
	 * @return the parameterResults
	 */
	public Map<String[], String> getParameterResults() {
		return parameterResult;
	}
	

	/**
	 * @return the parameterResult for given index
	 */
	public String getParameterResult(String[] index) {
		return parameterResult.get(index);
	}

	/**
	 * @param map the parameterResult to set
	 */
	public void setParameterResult(Map<String[], String> map) {
		this.parameterResult = map;
	}
	
	/**
	 * give back help text
	 * @return String
	 */
	public String getHelpText() {
		return "-h		-help	/?	displays the help\n"
				+ "-host				[hostname] sets SQL host\n"
				+ "-database	-d		[databasename] sets SQL database\n"
				+ "-user		-u		[username] sets SQL username\n"
				+ "-passwd		-p		[passwd] sets SQL password\n"
				+ "-connection	-c		[MySQL/MsSQL] declares to witch SQL Database the programm should connect"
				+ "-winlogon			if used, the programm will try to authenticate with windows integrated security (only for MsSQL)";
	}

	/**
	 * @return the paramMap
	 */
	public String[] getParamMapResult(String key) {
		return paramMap.get(key);
	}

}

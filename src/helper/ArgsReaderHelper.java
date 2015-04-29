package helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArgsReaderHelper {
	private String[] args= null;
	private ArrayList<String[]> params = null;
	private Map<String[],String> parameterMap = new HashMap<String[],String>();
	
	public ArgsReaderHelper(String[] args,ArrayList<String[]> params) {
		this.args = args; this.params = params;
		fillParameterMap();
	}
	
	private void fillParameterMap() {
		for(String[] param : params) {
			parameterMap.put(param, getValue(param));
		}
	}
	
	private String getValue(String[] param) {
		for(int i=0;args.length>i;i++) {
			for(String para : param) {
				if(args[i].equals(para)) {
					if(args.length>i+1) {
						if(isParameter(args[i+1])) return "found";
						else return args[i+1];
					}
					return "found";
				}
			}
		}
		return null;
	}

	private boolean isParameter(String str) {
		for(String[] param : params) {
			for(String para : param) {
				if(para.equals(str)) return true;
			}
		}
		return false;
	}
	/**
	 * @return the parameterMap
	 */
	public Map<String[], String> getParameterMap() {
		return parameterMap;
	}
}

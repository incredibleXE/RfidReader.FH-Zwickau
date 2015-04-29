package main;

import helper.ArgsReaderHelper;
import exceptions.NoReaderFoundException;
import model.Databean;
import uhfReader.IniUhfReader;

public class Main {
	
	public static void main(String[] args) {
		Databean databean = new Databean();
		databean.setParameterResult(new ArgsReaderHelper(args,databean.getParams()).getParameterMap());
		if(databean.getParameterResult(databean.getParamMapResult("-help"))!=null) {
			// output help
			System.out.println(databean.getHelpText());
		} else {
			databean.setHostFromParameter();
			databean.setDatabasenameFromParameter();
			databean.setPasswdFromParameter();
			databean.setUserFromParameter();
			databean.setWinlogonFromParameter();
			databean.setTypeFromParameter();
			
			// Output database informations
			databean.println("database connection:");
			databean.println("	Host: "+databean.getHost());
			databean.println("	Database: "+databean.getDatabasename());
			databean.println("	User: "+databean.getUser());
			databean.println("	Passwd: "+databean.getPasswd());
			databean.println("	Type: "+databean.getType());
			databean.println("	Winlogon: "+databean.getWinlogon());
			
			try {
				new IniUhfReader(databean).initReader();
			} catch (NoReaderFoundException e) {
				databean.printlnError(e.getMessage());
			}
		}
	}
}
